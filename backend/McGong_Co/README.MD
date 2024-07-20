# McGong_Co Backend Server

## Getting Started

Follow these instructions to start your backend server.

### Prerequisites

- Ensure you have [Maven](https://maven.apache.org/install.html) installed.
- Ensure you have [Java](https://www.oracle.com/java/technologies/javase-downloads.html) installed.

### Steps to Start the Server

1. **Open Command Prompt**:
   - Click the **Start Menu** and type `cmd`.
   - Click on **Command Prompt** to open it.

2. **Navigate to Your Backend Directory**:
   - Run the following command:
     ```shell
     cd C:\Users\shaco\Development\backend\McGong_Co
     ```

3. **Build the Project**:
   - Run the following command:
     ```shell
     mvn clean install
     ```

4. **Run the Backend Server**:
   - Run the following command:
     ```shell
     mvn spring-boot:run
     ```

5. **Check for Port Conflicts**:
   - If you encounter a port conflict error, follow these steps to change the port:
     - Open the `application.properties` file in your backend project.
     - Add the following line:
       ```properties
       server.port=8080
       ```
     - Save the file and run the server again with:
       ```shell
       mvn spring-boot:run
       ```

### Additional Notes

- **Stopping the Server**:
  - To stop the server, go to the Command Prompt where it's running and press `Ctrl + C`.

- **Change Port**:
  - You can change the port number to any available port (e.g., `8082`, `9090`, etc.).

### Example Commands Summary

1. Open Command Prompt
2. Navigate to your project folder:
   ```shell
   cd C:\Users\shaco\Development\backend\McGong_Co
3. Build the project:
   ```shell
   mvn clean install
3. Build the project:
   ```shell
   mvn spring-boot:run
