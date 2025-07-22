package com.lushihao.aiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatModel;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-08   16:06
 */
public class LangChain4jAiInvoke {
    public static void main(String[] args) {

        ChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey(TestApiKey.API_KEY)
                .modelName("qwen-max")
                .build();
        String answer = qwenChatModel.chat("你好 我是卢世豪");
        System.out.println(answer);
    }

}
