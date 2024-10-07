<template>
  <v-container fluid>
    <v-row>
      <!-- Code Editor Column -->
      <v-col cols="12" md="8">
        <v-card>
          <v-card-title>Java Compiler</v-card-title>
          <v-card-text>
            <div id="aceEditor" class="code-editor"></div>
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" @click="compileCode">Compile & Run</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>

      <!-- Ace Editor Console and Input -->
      <v-col cols="12" md="4">
        <v-card>
          <v-card-title>Console</v-card-title>
          <v-card-text>
            <div class="ace-console">
              <pre v-html="consoleOutput"></pre>
              <v-text-field
                v-model="userInput"
                label="Input"
                outlined
                dense
                @keydown.enter.prevent="handleUserInput"
              ></v-text-field>
              <v-btn color="primary" @click="compileCode">Submit</v-btn>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from '../../../axios';

export default {
  name: 'JavaCompiler',

  data() {
    return {
      javaCode: '',
      editor: null,
      consoleOutput: '', // Initialize console output variable
      userInput: '', // User input for console
    };
  },

  methods: {
    async compileCode() {
      try {
        const response = await axios.post('/compile', {
          javaCode: this.javaCode,
          input: this.userInput, // Pass user input to backend
        });

        // Clear the console before updating
        this.consoleOutput = '';

        if(response.data) {
          this.consoleOutput += `Program output:\n${response.data}\n\n`;
        }

        // Optionally clear user input after submission
        this.userInput = '';

      } catch (err) {
        console.error('Error during compilation:', err);
        this.consoleOutput = `Error: ${err.message}\n`;
      }
    },

    handleUserInput() {
      this.compileCode(); // Directly call compileCode on button click
    },

    initializeAceEditor() {
      if (typeof ace !== 'undefined') {
        this.editor = ace.edit("aceEditor");
        this.editor.setTheme("ace/theme/white");
        this.editor.session.setMode("ace/mode/java");

        // Bind editor value to Vue data property
        this.editor.session.on('change', () => {
          this.javaCode = this.editor.getValue();
        });
      } else {
        setTimeout(this.initializeAceEditor, 100);
      }
    },
  },

  mounted() {
    this.initializeAceEditor();
  },
};
</script>

<style scoped>
.code-editor {
  width: 100%;
  height: 300px;
}

.ace-console {
  height: 200px; /* Adjust height as needed */
  border: 1px solid #ccc;
  background-color: #f7f7f7;
  font-size: 14px;
  padding: 10px;
  overflow: auto;
}

.ace-console input {
  width: calc(100% - 85px); /* Adjust width */
  box-sizing: border-box;
  padding: 5px;
  font-size: 14px;
  border: 1px solid #ccc;
  margin-bottom: 10px; /* Optional: Add margin */
}

.ace-console button {
  margin-top: 5px; /* Optional: Adjust margin */
}
</style>
