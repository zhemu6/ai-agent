package com.lushihao.aiagent.tools;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-24   16:09
 */
@Slf4j
class TerminalOperationToolTest {

    @Test
    void executeTerminalCommand() {
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();

        String command = System.getProperty("os.name").toLowerCase().contains("win") ? "git -v" : "ls";
        String result = terminalOperationTool.executeTerminalCommand(command);

        log.info("Terminal command result:\n{}", result);

        assertNotNull(result);
        assertFalse(result.isEmpty(), "命令输出不应为空");
        assertFalse(result.contains("Error executing terminal command"), "执行命令过程中出现错误");
        assertFalse(result.contains("exit code"), "命令非正常退出");
    }
}
