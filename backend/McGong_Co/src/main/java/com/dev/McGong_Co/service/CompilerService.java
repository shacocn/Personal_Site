package com.dev.McGong_Co.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CompilerService {

    private static final Logger logger = LoggerFactory.getLogger(CompilerService.class);

    public String compileAndRun(String sourceCode, String input) {
        logger.info("Received source code:\n{}", sourceCode);

        long startTime = System.nanoTime();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        // Extract the public class name from the source code
        String className = extractPublicClassName(sourceCode);
        if (className == null) {
            return "Compilation failed: No public class found in the source code.";
        }

        logger.info("Extracted public class name: {}", className);

        // Prepare a new Java source file object with the provided source code
        JavaFileObject sourceFile = new DynamicJavaSourceObject(className, sourceCode);

        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(sourceFile);
        CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);

        StringBuilder output = new StringBuilder();

        try {
            boolean compilationSuccess = task.call();

            if (compilationSuccess) {
                long compilationTime = System.nanoTime() - startTime;
                double compilationTimeInSeconds = TimeUnit.NANOSECONDS.toSeconds(compilationTime);
                output.append("Compilation successful.\n");
                output.append("Compilation time: ").append(compilationTimeInSeconds).append(" seconds.\n\n");

                // Load compiled class
                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File("").toURI().toURL()});
                Class<?> compiledClass = Class.forName(className, true, classLoader);

                // Redirect System.out for capturing output
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                PrintStream originalOut = System.out;
                System.setOut(printStream);

                // Redirect System.in for handling input
                InputStream originalIn = System.in;
                ByteArrayInputStream simulatedInput = new ByteArrayInputStream(input.getBytes());
                System.setIn(simulatedInput);

                // Invoke main method if exists
                Method mainMethod = compiledClass.getMethod("main", String[].class);
                mainMethod.invoke(null, new Object[]{new String[]{}});

                // Restore System.in and System.out
                System.setIn(originalIn);
                System.setOut(originalOut);

                // Append output to response
                output.append("Program output:\n").append(outputStream.toString());
            } else {
                output.append("Compilation failed:\n");
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    output.append("Error on line ")
                          .append(diagnostic.getLineNumber())
                          .append(": ")
                          .append(diagnostic.getMessage(null))
                          .append("\n");
                }
            }
        } catch (Exception e) {
            logger.error("Error during execution:", e);
            output.append("Execution failed: ").append(e.getMessage());
        } finally {
            try {
                fileManager.close();
            } catch (IOException e) {
                logger.error("Failed to close the file manager: ", e);
            }
        }

        return output.toString();
    }

    // Helper method to extract the public class name from source code
    private String extractPublicClassName(String sourceCode) {
        // Implement logic to extract the public class name
        // This should handle various cases such as package declarations and class modifiers
        // For simplicity, assuming a basic scenario here:
        if (sourceCode.contains("class ")) {
            int startIndex = sourceCode.indexOf("class ") + 6;
            int endIndex = sourceCode.indexOf(" ", startIndex);
            if (endIndex == -1) {
                endIndex = sourceCode.indexOf("{", startIndex);
            }
            return sourceCode.substring(startIndex, endIndex).trim();
        }
        return null;
    }

    // Custom implementation of JavaFileObject for dynamic code
    static class DynamicJavaSourceObject extends SimpleJavaFileObject {
        private final String sourceCode;

        protected DynamicJavaSourceObject(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.sourceCode = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return sourceCode;
        }
    }
}
