package com.lushihao.aiagent.agent.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 抽象基础代理类，用于管理代理状态和执行基本流程
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-26   14:36
 */
@Data
@Slf4j
public abstract class BaseAgent {

    //核心属性
    private String name;
    // 提示词
    private String systemPrompt;
    // 下一步提示词
    private String nextStepPrompt;
    // 代理状态 默认为空闲状态
    private AgentState agentState = AgentState.IDLE;
    // 当前执行步骤
    private int currentStep = 0;
    // 默认最大执行步骤数为10
    private int maxSteps = 10;

    // LLM大模型
    private ChatClient chatClient;

    // Memory记忆 用于保存上下文
    private List<Message> messageList = new ArrayList<>();

    /**
     * 调用智能体
     *
     * @param userPrompt 用户输入
     * @return 执行结果
     */
    public String run(String userPrompt) {
        // 如果当前状态不是空闲状态 抛出异常 当前智能体正在繁忙
        if (agentState != AgentState.IDLE) {
            throw new RuntimeException("Cannot run agent. Current agent is busy.");
        }
        // 如果用户输入为空 抛出异常
        if (StrUtil.isBlank(userPrompt)) {
            throw new RuntimeException("Cannot run agent. User prompt is empty.");
        }
        // 执行 更改状态
        agentState = AgentState.RUNNING;
        // 记录消息上下文
        messageList.add(new UserMessage(userPrompt));
        // 保存结果列表
        List<String> results = new ArrayList<>();

        try {
            // 如果当前状态不是未完成并且没有达到最大步骤 就继续执行
            for (int i = 0; i < maxSteps && agentState != AgentState.FINISHED; i++) {
                //更新当前步骤数
                currentStep++;
                // 日志输出
                log.info("Executing step{} of {} for agent {}", currentStep, maxSteps, name);
                // 执行step方法
                String stepResult = step();
                String result = "Seep" + currentStep + " result:" + stepResult;
                results.add(result);
            }
            // 检查是否超出最大步骤
            if (currentStep >= maxSteps) {
                agentState = AgentState.FINISHED;
                results.add("Terminated:Reached max steps(" + maxSteps + ")");
            }
            // 将结果拼接成一个字段 用回车分隔
            return String.join("\n", results);
        } catch (Exception e) {
            agentState = AgentState.ERROR;
            log.info("Exception occurred while running agent {}", name, e);
            return "Exception occurred while running agent " + name + ": " + e.getMessage();
        } finally {
            // 执行完之后 清理资源
            cleanUp();
        }
    }


    /**
     * 调用智能体 按照stream流
     *
     * @param userPrompt 用户输入
     * @return 执行结果
     */
    public SseEmitter runStream(String userPrompt) {
        // 设置超时时间 为5min
        SseEmitter sseEmitter = new SseEmitter(300000L);
        // 使用线程异步处理 避免使用主线程
        CompletableFuture.runAsync(() -> {
            try {
                // 如果当前状态不是空闲状态 抛出异常 当前智能体正在繁忙
                if (agentState != AgentState.IDLE) {
                    sseEmitter.send("错误：无法从当前状态运行代理：" + agentState);
                    sseEmitter.complete();
                    return;
                }
                // 如果用户输入为空 抛出异常
                if (StrUtil.isBlank(userPrompt)) {
                    sseEmitter.send("错误：不能使用空提示词：");
                    sseEmitter.complete();
                    return;
                }
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }


            // 执行 更改状态
            agentState = AgentState.RUNNING;
            // 记录消息上下文
            messageList.add(new UserMessage(userPrompt));
            // 保存结果列表
            List<String> results = new ArrayList<>();

            try {
                // 如果当前状态不是未完成并且没有达到最大步骤 就继续执行
                for (int i = 0; i < maxSteps && agentState != AgentState.FINISHED; i++) {
                    //更新当前步骤数
                    currentStep++;
                    // 日志输出
                    log.info("Executing step{} of {} for agent {}", currentStep, maxSteps, name);
                    // 执行step方法
                    String stepResult = step();
                    String result = "Seep" + currentStep + " result:" + stepResult;
                    results.add(result);
                    // 发送结果
                    sseEmitter.send(result);
                }
                // 检查是否超出最大步骤
                if (currentStep >= maxSteps) {
                    agentState = AgentState.FINISHED;
                    results.add("Terminated:Reached max steps(" + maxSteps + ")");
                    sseEmitter.send("Terminated:Reached max steps(" + maxSteps + ")");
                }
                sseEmitter.complete();
                // 将结果拼接成一个字段 用回车分隔
            } catch (Exception e) {
                agentState = AgentState.ERROR;
                log.info("Exception occurred while running agent {}", name, e);
                try {
                    sseEmitter.send("执行错误" + e.getMessage());
                    sseEmitter.complete();
                } catch (IOException ex) {
                    sseEmitter.completeWithError(ex);
                }
            } finally {
                // 执行完之后 清理资源
                cleanUp();
            }
        });

        sseEmitter.onTimeout(()->{
            this.agentState = AgentState.ERROR;
            this.cleanUp();
            log.warn("SSE connection timeout");
        });

        sseEmitter.onCompletion(()->{
            if(this.agentState != AgentState.RUNNING){
                this.agentState = AgentState.FINISHED;
            }
            this.cleanUp();
            log.warn("SSE connection completed");
        });

        return sseEmitter;
    }


    /**
     * 定义单个步骤
     *
     * @return
     */
    public abstract String step();

    /**
     * 子类重写 用于清理资源
     */
    protected void cleanUp() {
    }

}
