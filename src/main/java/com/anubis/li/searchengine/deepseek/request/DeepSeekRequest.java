package com.anubis.li.searchengine.deepseek.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class DeepSeekRequest {
    /**
     * 消息列表，包含对话中的消息对象
     */
    private List<Message> messages;

    /**
     * 模型名称，指定要使用的模型
     */
    private String model;

    /**
     * 频率惩罚，用于减少重复内容的概率
     * 介于 -2.0 和 2.0 之间的数字。如果该值为正，那么新 token 会根据其在已有文本中的出现频率受到相应的惩罚，降低模型重复相同内容的可能性
     */
    private double frequency_penalty;

    /**
     * 最大生成的令牌数
     * 介于 1 到 8192 间的整数，限制一次请求中模型生成 completion 的最大 token 数。输入 token 和输出 token 的总长度受模型的上下文长度的限制。
     * 如未指定 max_tokens参数，默认使用 4096
     */
    private int max_tokens;

    /**
     * 存在惩罚，用于增加新话题的概率
     */
    private double presence_penalty;

    /**
     * 响应格式，指定返回的响应格式
     */
    private ResponseFormat response_format;

    /**
     * 停止序列，指定生成文本时的停止条件
     */
    private Object stop;

    /**
     * 是否流式返回结果
     */
    private boolean stream;

    /**
     * 流式选项，指定流式返回的选项
     * stream 为 true 时，才可设置此参数
     */
    private Object stream_options;
    /**
     * 温度，控制生成文本的随机性
     * 介于 0 和 2 之间的数字，值越低，更加准确
     */
    private double temperature;
    /**
     * 核采样参数，控制生成文本的多样性
     */
    private double top_p;

    /**
     * 工具列表，指定可用的工具
     */
    private Object tools;

    /**
     * 工具选择，指定使用的工具
     */
    private String tool_choice;

    /**
     * 是否返回对数概率
     */
    private boolean logprobs;

    /**
     * 对数概率选项，指定返回的对数概率选项
     */
    private Object top_logprobs;

    /**
     * 消息对象，包含单个消息的内容和角色
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        /**
         * 消息内容
         */
        private String content;

        /**
         * 消息角色，例如 "system" 或 "user"
         */
        private String role;
    }

    /**
     * 响应格式对象，指定返回的响应格式类型
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseFormat {
        /**
         * 响应格式类型，例如 "text"
         */
        private String type;
    }
}

