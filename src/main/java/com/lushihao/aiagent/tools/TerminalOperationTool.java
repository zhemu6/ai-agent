package com.lushihao.aiagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 终端操作工具
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-24   16:00
 */
public class TerminalOperationTool {
   @Tool(description = "Execute a command in the terminal")
   public String executeTerminalCommand(@ToolParam(description = "Command to execute in the terminal") String command) {
      StringBuilder output = new StringBuilder();

      try {
         String[] finalCommand = System.getProperty("os.name").toLowerCase().contains("win") ?
                 new String[]{"cmd.exe", "/c", command} :
                 new String[]{"sh", "-c", command};
         Process process = Runtime.getRuntime().exec(finalCommand);

         try (
                 BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))
         ) {
            String line;
            while ((line = stdOutReader.readLine()) != null) {
               output.append(line).append("\n");
            }
            while ((line = stdErrReader.readLine()) != null) {
               output.append("ERROR: ").append(line).append("\n");
            }
         }

         int exitCode = process.waitFor();
         if (exitCode != 0) {
            output.append("Command execution failed with exit code: ").append(exitCode);
         }

      } catch (Exception e) {
         return "Error executing terminal command: " + e.getMessage();
      }

      return output.toString();
   }
}
