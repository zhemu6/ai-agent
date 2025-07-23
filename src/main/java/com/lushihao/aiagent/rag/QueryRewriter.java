package com.lushihao.aiagent.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

/**
 * 查询重写器
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-23   20:44
 */


@Component
public class QueryRewriter {

    private final QueryTransformer queryTransformer;

    public QueryRewriter(ChatModel dashscopeChatModel) {
        ChatClient.Builder builder = ChatClient.builder(dashscopeChatModel);

        // 创建查询重写转换器
        queryTransformer = RewriteQueryTransformer
                .builder()
                .chatClientBuilder(builder)
                .build();
    }

    public String doQueryRewrite(String prompt) {
        // 将prompt变成一个查询
        Query query = new Query(prompt);
        // 执行查询重写
        Query transformQuery = queryTransformer.transform(query);
        // 输出重写后的查询
        return transformQuery.text();
    }
}
