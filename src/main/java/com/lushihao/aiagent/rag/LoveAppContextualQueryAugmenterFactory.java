package com.lushihao.aiagent.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.stereotype.Component;

/**
 * 创建上下文查询增强器的工坊
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-23   21:18
 */

@Component
public class LoveAppContextualQueryAugmenterFactory {


    public static ContextualQueryAugmenter createInstance(){

        PromptTemplate emptyContextPromptTemplate = new PromptTemplate("""
                你应该输出下面的内容：
                                抱歉，我只能回答恋爱相关的问题，别的没办法帮到您哦，
                                有问题可以联系编程导航客服 https://codefather.cn
                """);

        return ContextualQueryAugmenter
                .builder()
                .allowEmptyContext(false)
                .emptyContextPromptTemplate(emptyContextPromptTemplate)
                .build();

    }
}
