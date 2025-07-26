package com.lushihao.aiagent.controller;

import com.lushihao.aiagent.agent.model.FortuneTellerManus;
import com.lushihao.aiagent.app.LoveApp;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-26   16:30
 */

@RequestMapping("/ai/fortune_teller")
@RestController
public class AiController {


    @Resource
    private LoveApp loveApp;
    @Resource
    private ChatModel dashscopeChatModel;
    @Resource
    ToolCallback[] allTools;

//    /**
//     * 同步调用应用
//     *
//     * @param message
//     * @param chatId
//     * @return
//     */
//    @GetMapping("/love_app/chat/sync")
//    public String doChatWithLoveAppSync(String message, String chatId) {
//        return loveApp.doChat(message, chatId);
//    }

//
//    /**
//     * SSE 流式调用同步调用应用
//     * @param message
//     * @param chatId
//     * @return
//     */
//    @GetMapping(value = "/love_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<String> doChatWithLoveAppSSE(String message, String chatId) {
//        return loveApp.doChatByStream(message, chatId);
//    }
//
//    @GetMapping(value = "/love_app/chat/sse")
//    public Flux<ServerSentEvent<String>> doChatWithLoveAppSSE(String message, String chatId) {
//        return loveApp.doChatByStream(message, chatId)
//                .map(chunk -> ServerSentEvent.<String>builder()
//                        .data(chunk)
//                        .build());
//    }

    /**
     * SSE 流式调用同步调用应用
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/love_app/chat/sse/emitter")
    public SseEmitter doChatWithLoveAppSseEmitter(String message, String chatId) {
        // 创建一个超时时间较长的 SseEmitter  3分钟超时
        SseEmitter emitter = new SseEmitter(180000L);
        // 获取 Flux 数据流并直接订阅
        loveApp.doChatByStream(message, chatId)
                .subscribe(
                        // 处理每条消息
                        chunk -> {
                            try {
                                emitter.send(chunk);
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        // 处理错误
                        emitter::completeWithError,
                        // 处理完成
                        emitter::complete
                );
        // 返回emitter
        return emitter;
    }



    @GetMapping("/fortune_teller_manus/chat/sse/emitter")
    public SseEmitter doChatWithFortuneTellerManusSseEmitter(String message) {

        FortuneTellerManus fortuneTellerManus = new FortuneTellerManus(allTools,dashscopeChatModel);

        return fortuneTellerManus.runStream(message);
    }


}
