package com.anubis.li.searchengine.core.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayes {
    // 训练数据，格式为：（文本，类别）
    private List<String> sportsTexts = new ArrayList<>();
    private List<String> nonSportsTexts = new ArrayList<>();

    // 存储每个词在各类别中出现的次数
    private Map<String, Integer> sportsWordCount = new HashMap<>();
    private Map<String, Integer> nonSportsWordCount = new HashMap<>();

    // 各类别中的文档数量
    private int sportsDocCount = 0;
    private int nonSportsDocCount = 0;

    // 训练方法
    public void train() {
        // 初始化训练数据
        sportsTexts.add("我喜欢打篮球");
        sportsTexts.add("足球比赛很精彩");
        sportsTexts.add("今天去跑步了");

        nonSportsTexts.add("我喜欢看电影");
        nonSportsTexts.add("这本书非常有趣");
        nonSportsTexts.add("听音乐能放松心情");

        // 处理运动类文本
        for (String text : sportsTexts) {
            sportsDocCount++;
            String[] words = text.split("");
            for (String word : words) {
                sportsWordCount.put(word, sportsWordCount.getOrDefault(word, 0) + 1);
            }
        }

        // 处理非运动类文本
        for (String text : nonSportsTexts) {
            nonSportsDocCount++;
            String[] words = text.split("");
            for (String word : words) {
                nonSportsWordCount.put(word, nonSportsWordCount.getOrDefault(word, 0) + 1);
            }
        }
    }

    // 计算单词在某类别中的概率
    private double wordProbability(String word, Map<String, Integer> wordCount, int docCount) {
        int count = wordCount.getOrDefault(word, 0);
        return (double) (count + 1) / (docCount + wordCount.size());
    }

    // 计算文本属于某类别的概率
    private double textProbability(String[] words, Map<String, Integer> wordCount, int docCount) {
        double probability = 1.0;
        for (String word : words) {
            probability *= wordProbability(word, wordCount, docCount);
        }
        return probability;
    }

    // 预测文本类别
    public String predict(String text) {
        String[] words = text.split("");
        double sportsProbability = textProbability(words, sportsWordCount, sportsDocCount);
        double nonSportsProbability = textProbability(words, nonSportsWordCount, nonSportsDocCount);

        if (sportsProbability > nonSportsProbability) {
            return "运动相关";
        } else {
            return "非运动相关";
        }
    }

    public static void main(String[] args) {
        NaiveBayes naiveBayes = new NaiveBayes();
        naiveBayes.train();

        String testText1 = "我刚打完羽毛球";
        String testText2 = "我在看一部好看的电视剧";

        System.out.println("文本 \"" + testText1 + "\" 的分类结果: " + naiveBayes.predict(testText1));
        System.out.println("文本 \"" + testText2 + "\" 的分类结果: " + naiveBayes.predict(testText2));
    }
}
