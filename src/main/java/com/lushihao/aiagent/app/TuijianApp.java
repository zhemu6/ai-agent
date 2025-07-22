package com.lushihao.aiagent.app;


import com.lushihao.aiagent.advisor.MyLoggerAdvisor;
import com.lushihao.aiagent.advisor.ProhibitedWordAdvisor;
import com.lushihao.aiagent.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class TuijianApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "在你的知识库中存在一个恋爱对象的数据。在这个数据里面帮助用户推荐恋爱对象。";

    /**
     * 初始化APP
     *
     * @param dashscopeChatModel
     */
    public TuijianApp(ChatModel dashscopeChatModel) {
        // 初始化一个基于文件的对话记忆
        String fileDir  = System.getProperty("user.dir") + "/tmp/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);

        // 初始化基于内存的对话记忆
//        ChatMemory chatMemory = new InMemoryChatMemory();
        // 构建一个对话体
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        //对话记忆advisor
                        new MessageChatMemoryAdvisor(chatMemory)
                        , new MyLoggerAdvisor()
                        ,new ProhibitedWordAdvisor()
                )
                .build();
    }

    /**
     * AI 基础对话 支持多轮对话
     *
     * @param message 用户消息
     * @param chatId  对话ID
     * @return
     */
    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
//        log.info("content: {}",content);
        return content;
    }

    // 快速定义一个恋爱报告类
    record LoveReport(String title, List<String> suggestions) {

    }

    /**
     * 恋爱报告功能 实现结构化输出
     *
     * @param message 用户消息
     * @param chatId  对话ID
     * @return
     */
    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}",loveReport);
        return loveReport;
    }

    // AI恋爱大师知识库问答功能
    @Resource
    private VectorStore loveAppVectorStore;

    @Resource
    private Advisor loveCandidateRagCloudAdvisor;

    /**
     * 和Rag知识库进行对话
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithRag(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                //开启日志 便于观察
                .advisors(new MyLoggerAdvisor())
                // 实现Rag知识问答 （基于本地的存储）
//                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                // 应用RAG检索增强服务（基于云知识库）
                .advisors(loveCandidateRagCloudAdvisor)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
//        log.info("content: {}",content);
        return content;
    }
}