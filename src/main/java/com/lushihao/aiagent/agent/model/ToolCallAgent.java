package com.lushihao.aiagent.agent.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgentOptions;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import dev.langchain4j.agent.tool.P;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 具体实现了think 和act方法 用于创建具体实例
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-26   14:36
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class ToolCallAgent extends ReActAgent {

    // 定义工具对象
    private final ToolCallback[] availableTools;

    // 保存工具调用信息的相应结果，要调用哪些工具
    private ChatResponse toolCallResponse;

    // 工具调用管理者
    private final ToolCallingManager toolCallingManager;

    // 禁用Spring AI 内置的工具调用机制，自己维护选项和消息上下文
    private final ChatOptions chatOptions;

    // Constructor for the ToolCallAgent class
    public ToolCallAgent(ToolCallback[] availableTools) {
        // Call the superclass constructor
        super();
        // Set the available tools
        this.availableTools = availableTools;
        // Create a new ToolCallingManager
        this.toolCallingManager = ToolCallingManager.builder().build();
        // 禁用Spring AI的内置工具调用机制 自己维护选项和消息上下文
        this.chatOptions = DashScopeChatOptions.builder().withProxyToolCalls(true).build();
    }

    @Override
    public boolean think() {
        List<Message> messageList = getMessageList();
        // 校验，是否有nextPrompt,拼接用户提示词
        if (StrUtil.isNotBlank(getNextStepPrompt())) {
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            messageList.add(userMessage);
        }
        // 调用AI大模型，获取工具调用结果
        Prompt prompt = new Prompt(messageList, this.chatOptions);

        try {
            ChatResponse chatResponse = getChatClient()
                    .prompt(prompt)
                    .system(getSystemPrompt())
                    .tools(availableTools)
                    .call()
                    .chatResponse();
            // 保存工具调用信息
            this.toolCallResponse = chatResponse;
            // 获得输出的助手信息
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            // 获取要调用的工具列表
            List<AssistantMessage.ToolCall> toolCalls = assistantMessage.getToolCalls();
            // 获取助手信息中的文本内容
            String result = assistantMessage.getText();
            log.info(getName() + "的思考：", result);
            log.info(getName() + "选择了：", toolCalls.size() + "个工具");

            String toolCallInfo = toolCalls.stream()
                    .map(toolCall -> String.format("工具名称：%s，参数：%s",
                            toolCall.name(),
                            toolCall.arguments())
                    )
                    .collect(Collectors.joining("\n"));
            log.info(toolCallInfo);
            // 如果不需要调用工具，则返回false
            if (StrUtil.isBlank(toolCallInfo)) {
                // 只有不调用工具时 才需要手动记录助手信息
                getMessageList().add(assistantMessage);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(getName() + "的思考异常：", e.getMessage());
            getMessageList().add(new AssistantMessage("处理时遇到了错误" + e.getMessage()));
            return false;
        }
    }

    /**
     * 执行工具调用
     *
     * @return
     */
    @Override
    public String act() {
        // 如果没有工具调用信息
        if (!toolCallResponse.hasToolCalls()) {
            return "没有工具需要调用";
        }
        Prompt prompt = new Prompt(getMessageList(), this.chatOptions);
        // 执行工具调用 获得返回结果
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, toolCallResponse);
        List<Message> newMessageList = toolExecutionResult.conversationHistory();
        setMessageList(newMessageList);
        // 从对话上下文中获取最后一条消息
        ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(newMessageList);
        // 是否调用终止工具
        boolean terminateToolCalled = toolResponseMessage.getResponses()
                .stream()
                .anyMatch(response -> response.name().equals("doTerminate"));
        //如果调用了
        if(terminateToolCalled){
            setAgentState(AgentState.FINISHED);
        }

        String results = toolResponseMessage.getResponses().stream().map(
                response -> "工具" + response.name() + "返回的结果:" + response.responseData()
        ).collect(Collectors.joining("\n"));
        log.info(results);
        return results;
    }
}


