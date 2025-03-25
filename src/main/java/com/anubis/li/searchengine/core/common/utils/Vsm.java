package com.anubis.li.searchengine.core.common.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Vsm {
        /**
         * 计算两个向量（以Map形式表示）之间的余弦相似度
         *
         * @param v1 第一个向量，键为特征，值为特征的权重
         * @param v2 第二个向量，键为特征，值为特征的权重
         * @return 余弦相似度值，范围在[-1, 1]之间，越接近1表示越相似
         */
        public static double calCosSim(Map<String, Double> v1, Map<String, Double> v2) {
            double scalar = 0.0, norm1 = 0.0, norm2 = 0.0, similarity = 0.0;
            // 获取v1的所有键
            Set<String> v1Keys = v1.keySet();
            // 获取v2的所有键
            Set<String> v2Keys = v2.keySet();
            // 创建一个集合，用于存储v1和v2共有的键
            Set<String> both = new HashSet<>();
            both.addAll(v1Keys);
            // 取交集，得到v1和v2共有的键
            both.retainAll(v2Keys);
            System.out.println(both);
            // 计算点积
            for (String str1 : both) {
                scalar += v1.get(str1) * v2.get(str1);
            }
            // 计算v1的模的平方
            for (String str1 : v1.keySet()) {
                norm1 += Math.pow(v1.get(str1), 2);
            }
            // 计算v2的模的平方
            for (String str2 : v2.keySet()) {
                norm2 += Math.pow(v2.get(str2), 2);
            }
            // 根据余弦相似度公式计算相似度
            similarity = scalar / Math.sqrt(norm1 * norm2);
            System.out.println("scalar:" + scalar);
            System.out.println("norm1:" + norm1);
            System.out.println("norm2:" + norm2);
            System.out.println("similarity:" + similarity);
            return similarity;
        }

        public static void main(String[] args) {
            Map<String, Double> m1 = new HashMap<>();
            m1.put("Hello", 1.0);
            m1.put("css", 2.0);
            m1.put("Lucene", 3.0);

            Map<String, Double> m2 = new HashMap<>();
            m2.put("Hello", 1.0);
            m2.put("Word", 2.0);
            m2.put("Hadoop", 3.0);
            m2.put("java", 4.0);
            m2.put("html", 1.0);
            m2.put("css", 2.0);

            // 调用方法计算m1和m2之间的余弦相似度
            calCosSim(m1, m2);
        }
    }
