package com.dev.McGong_Co.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.McGong_Co.dto.CompileRequest;
import com.dev.McGong_Co.service.CompilerService;

@RestController
@RequestMapping("/api")
public class CompilerController {

    @Autowired
    private CompilerService compilerService;

    @PostMapping("/compile")
    public ResponseEntity<String> compileCode(@RequestBody CompileRequest request) {
        try {
            String result = compilerService.compileAndRun(request.getJavaCode(), request.getInput());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Compilation failed: " + e.getMessage());
        }
    }
}

