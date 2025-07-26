package com.lushihao.aiagent.agent.model;

/**
 * 代理执行的状态
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-26   14:34
 */
public enum AgentState {
    /**
     * 空闲状态
     */
    IDLE,
    /**
     * 执行中
     */
    RUNNING,
    /**
     * 已完成
     */
    FINISHED,
    /**
     * 错误状态
     */
    ERROR
}

