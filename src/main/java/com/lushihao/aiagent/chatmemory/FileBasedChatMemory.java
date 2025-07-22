package com.lushihao.aiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-09   20:23
 * 基于文件的序列化记忆
 */
public class FileBasedChatMemory  implements ChatMemory {
    // zi
    private final String BASE_DIR;
    // Kryo高性能序列化库
    private static final Kryo kryo = new Kryo();

    static{
        kryo.setRegistrationRequired(false);
        // 设置实例化策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }
    // 构造函数 构造对象时 指定文件保存目录
    public FileBasedChatMemory(String dir){
        this.BASE_DIR = dir;
        File baseDir = new File(dir);
        if (!baseDir.exists()){
            baseDir.mkdirs();
        }
    }

    /**
     * 保存单条消息
     * @param conversationId
     * @param message
     */
    @Override
    public void add(String conversationId, Message message) {
        List<Message> messageList = new ArrayList<>(getOrCreateConversation(conversationId));
        messageList.add(message);
        saveConversation(conversationId, messageList);
//        saveConversation(conversationId, List.of(message));
    }

    /**
     * 保存多条消息
     * @param conversationId
     * @param messages
     */
//    @Override
//    public void add(String conversationId, List<Message> messages) {
//        List<Message> messageList = getOrCreateConversation(conversationId);
//        messageList.addAll(messages);
//        saveConversation(conversationId,messageList);
//    }
    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> messageList = new ArrayList<>(getOrCreateConversation(conversationId));
        messageList.addAll(messages);
        saveConversation(conversationId, messageList);
    }


    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> messageList = getOrCreateConversation(conversationId);
        return  messageList.stream().skip(Math.max(0,messageList.size()-lastN)).toList();
    }

    @Override
    public void clear(String conversationId) {
        // 获取某个文件
        File file = getConversationFile(conversationId);
        // 如果文件存在 就删除这个文件
        if(file.exists()){
            file.delete();
        }

    }

    /**
     * 获取或者创建对话消息的列表
     * @param conversationId
     * @return
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        List<Message> messages = new ArrayList<>();
        if (file.exists()) {
            try (Input input = new Input(new FileInputStream(file))) {
//                messages = kryo.readObject(input, ArrayList.class);
                Object obj = kryo.readClassAndObject(input);
                if (obj instanceof List<?>) {
                    messages = (List<Message>) obj;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }


    /**
     * 保存对话消息
     * @param conversationId
     * @param messages
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
//            kryo.writeObject(output, messages);
            kryo.writeClassAndObject(output, messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 每个会话文件单独保存
     * @param conversationId
     *
     * @return
     */
    private File getConversationFile(String conversationId){
        return new File(BASE_DIR,conversationId + ".kyro");
    }
}
