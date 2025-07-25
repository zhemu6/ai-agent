package com.lushihao.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-24   14:53
 */
@SpringBootTest
class FileOperationToolTest {

    @Test
    void readFile() {
        String fileName = "编程导航.txt";
        FileOperationTool fileOperationTool = new FileOperationTool();
        String result = fileOperationTool.readFile(fileName);
        Assertions.assertNotNull(result);
    }

    @Test
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "编程导航.txt";

        String content = "这是一个测试的！";
        String result = fileOperationTool.writeFile(fileName, content);
        Assertions.assertNotNull(result);
    }
}