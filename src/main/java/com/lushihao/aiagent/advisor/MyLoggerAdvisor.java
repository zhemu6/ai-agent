package com.lushihao.aiagent.advisor;

import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisorChain;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.MessageAggregator;
import org.springframework.ai.model.ModelOptionsUtils;
import reactor.core.publisher.Flux;


/**
 * 建议我们在实现
 */
@Slf4j
public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
    /**
     * 提供唯一名称 为Advisor提供唯一标识符
     * @return
     */
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 设置执行顺序 通过getOrder方法 指定Advisor在链中的执行顺序，值越小越先执行
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }

    private AdvisedRequest before(AdvisedRequest request) {
//        logger.debug("request: {}", this.requestToString.apply(request));
        log.info("AI Request; {}", request.userText());
        return request;
    }

    private void observeAfter(AdvisedResponse advisedResponse) {
        log.info("AI Respionse; {}", advisedResponse.response().getResult().getOutput().getText());
    }

    @Override
    public String toString() {
        return MyLoggerAdvisor.class.getSimpleName();
    }

    /**
     * 对于实现的CallAroundAdvisor 重写aroundCall方法
     * @param advisedRequest
     * @param chain
     * @return
     */
    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        // 1. 处理请求，前置处理
        advisedRequest = this.before(advisedRequest);
        // 2. 调用链中的下一个Advisor
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        // 3. 处理响应，后置处理
        this.observeAfter(advisedResponse);

        return advisedResponse;
    }

    /**
     * 对于实现的StreamAroundAdvisor 重写aroundStream
     * @param advisedRequest
     * @param chain
     * @return
     */
    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        // 1. 处理请求
        advisedRequest = this.before(advisedRequest);
        // 2. 调用链中的下一个Advisor并处理流式相应
        Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);
        return (new MessageAggregator()).aggregateAdvisedResponse(advisedResponses, this::observeAfter);
    }
}
