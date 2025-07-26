package com.lushihao.aiagent.agent.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 实现思考 ReAct中 think act step方法
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-26   14:36
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public abstract class ReActAgent extends BaseAgent {


    /**
     *  处理当前状态 并决定是否执行下一步行动
     * @return 是否执行
     */
    public abstract boolean think();

    /**
     * 执行决定的行动
     * @return String 返回结果
     */
    public abstract String act();

    @Override
    public String step() {
        try{
            // 定义一个是否需要执行
            boolean shouldAct = think();
            // 如果需要执行
            if (shouldAct) {
                // 执行并返回执行结果
                return act();
            } else {
                // 不需要执行 返回null
                return "Thinking complete - no action needed";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "步骤执行失败：" + e.getMessage();
        }

    }

}
