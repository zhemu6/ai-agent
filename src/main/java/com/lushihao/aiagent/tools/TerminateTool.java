package com.lushihao.aiagent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * 终止调用工具
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-26   15:52
 */

public class TerminateTool {
    @Tool(description = "Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.\n" +
            "When you have finished all the tasks, call this tool to end the work.")
    public String doTerminate(){
        return "任务结束！";
    }
}
