package com.lushihao.aiagent.app;


import com.lushihao.aiagent.advisor.MyLoggerAdvisor;
import com.lushihao.aiagent.advisor.ProhibitedWordAdvisor;
import com.lushihao.aiagent.chatmemory.FileBasedChatMemory;
import com.lushihao.aiagent.rag.LoveAppContextualQueryAugmenterFactory;
import com.lushihao.aiagent.rag.LoveAppRagCustomAdvisorFactory;
import com.lushihao.aiagent.rag.QueryRewriter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.ai.chat.model.ChatModel;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = " You are MingZhe, a respected Fortune Master skilled in traditional Chinese metaphysics and astrology.\n" +
            "\n" +
            "You require the user to provide their **full name**, **birthdate**, and **confirm whether the birthdate is based on the lunar or solar calendar**.  \n" +
            "If the information is missing or unclear, you must politely ask the user to provide it — as accurate fortune reading depends on these details.\n" +
            "\n" +
            "Your task is to generate a detailed **Ben Ming Nian (本命年)** fortune report for the user, combining classical Chinese fortune-telling principles with the latest 2025 zodiac predictions.\n" +
            "\n" +
            "Please incorporate the following aspects:\n" +
            "- The user’s zodiac sign and whether it clashes with Tai Sui (太岁), with explanations\n" +
            "- Heavenly Stems and Earthly Branches (天干地支), Five Elements (五行), and fortune tendencies based on the birthdate\n" +
            "- Modern interpretations of fortune: career, wealth, health, and relationships\n" +
            "- Traditional wisdom such as I Ching (易经), face reading, numerology, etc.\n" +
            "- Online insights about the 2025 zodiac outlook, if necessary\n" +
            "\n" +
            "Organize the report in the following structure:\n" +
            "1. General overview of the Ben Ming Nian  \n" +
            "2. Zodiac and Tai Sui clash analysis  \n" +
            "3. Career and academic fortune  \n" +
            "4. Wealth and financial trends  \n" +
            "5. Health and well-being advice  \n" +
            "6. Relationship and social life  \n" +
            "7. Auspicious guidance: lucky colors, numbers, charms, taboos  \n" +
            "8. Final summary and spiritual message for the year\n" +
            "\n" +
            "Speak in a warm, wise, and poetic tone, as a compassionate master offering insight.  \n" +
            "The report should be written in **Chinese**, and formatted appropriately for a final **PDF document**.\n" +
            "\n" +
            "If any personal information is missing, politely request it before beginning the analysis.\n" +
            "\n" +
            "Please begin.\n";

    /**
     * 初始化APP
     *
     * @param dashscopeChatModel
     */
    public LoveApp(ChatModel dashscopeChatModel) {
        // 初始化一个基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
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
                        //关键词过滤的advisor
//                        , new ProhibitedWordAdvisor()
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
        // 调用chatClient的prompt方法，设置系统提示，用户消息，以及对话ID和对话大小
        LoveReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);
        // 打印恋爱报告
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }

    // AI恋爱大师知识库问答功能
    // 基于内存的向量数据库存储
    @Resource
    private VectorStore loveAppVectorStore;

//    @Resource
//    private Advisor loveAppRagCloudAdvisor;

    //    @Resource
//    private VectorStore pgVectorVectorStore;
    //引入查询重写器
    @Resource
    private QueryRewriter queryRewriter;


    /**
     * 和Rag知识库进行对话
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithRag(String message, String chatId) {
        // 执行查询改写
        String rewriteQuery = queryRewriter.doQueryRewrite(message);

        ChatResponse chatResponse = chatClient
                .prompt()
                // 使用改写后的对话
                .user(rewriteQuery)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                //开启日志 便于观察
                .advisors(new MyLoggerAdvisor())
                // 实现Rag知识问答
                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                // 应用RAG检索增强服务（基于云知识库）
//                .advisors(loveAppRagCloudAdvisor)
                // 应用pg数据库
//                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                // 应用自定义的RAG检索增强服务（文档检索增强+上下文增强）
                /*               .advisors(
                                       LoveAppRagCustomAdvisorFactory.createLoveAppRagCustomAdvisor(loveAppVectorStore,"单身")
                               )*/

                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
//        log.info("content: {}",content);
        return content;
    }

//    @Autowired
//    private JavaMailSender mailSender;

    // AI调用工具能力
    @Resource
    private ToolCallback[] allTools;

    public String doChatWithTools(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志 便于观察效果
                .advisors(new MyLoggerAdvisor())
                .tools(allTools)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    // AI调用MCP服务
    // 从配置文件中读取并返回可以调用的工具
    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    /**
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithMCP(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志 便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 使用MCP服务
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }


    /**
     * AI 基础对话 支持多轮对话 流失输出
     *
     * @param message 用户消息
     * @param chatId  对话ID
     * @return
     */
    public Flux<String> doChatByStream(String message, String chatId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .stream()
                .content();
    }

}