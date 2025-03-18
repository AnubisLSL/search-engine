package com.anubis.li.searchengine.deepseek.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeepSeekResponse {

    /**
     * API 模型名称，标识使用的模型版本。
     */
    private String model;

    /**
     * 响应创建时间，格式为 ISO 8601 标准的时间戳。
     */
    private String createdAt;

    /**
     * 消息内容，包含角色和具体内容。
     */
    private Message message;

    /**
     * 完成原因，表示请求完成的具体原因。
     */
    private String doneReason;

    /**
     * 是否完成，表示请求是否成功完成。
     */
    private boolean done;

    /**
     * 总持续时间，单位为纳秒，表示整个请求处理的总时间。
     */
    private long totalDuration;

    /**
     * 加载持续时间，单位为纳秒，表示加载阶段的耗时。
     */
    private long loadDuration;

    /**
     * 提示评估次数，表示提示评估的次数。
     */
    private int promptEvalCount;

    /**
     * 提示评估持续时间，单位为纳秒，表示提示评估的总耗时。
     */
    private long promptEvalDuration;

    /**
     * 评估次数，表示总的评估次数。
     */
    private int evalCount;

    /**
     * 评估持续时间，单位为纳秒，表示总的评估耗时。
     */
    private long evalDuration;
    /**
     * 内部静态类，用于封装消息内容。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        /**
         * 消息的角色，例如 "system" 或 "user"。
         */
        private String role;
        /**
         * 消息的具体内容。
         */
        private String content;
    }
}

