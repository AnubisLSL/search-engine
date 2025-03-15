package com.anubis.li.searchengine.core.model;

import com.anubis.li.searchengine.core.common.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Table(name="index_config")
public class IndexConfig {
    // 主键
   private String id;
   // 备注
   private String remark;
   // 名称
   private String name;
   // 是否是数据库表
   private boolean dbBase =true;

   // 路径
   private String path;
   // 数据库连接配置名称，即connection的名称
   private String conn;
   // 数据库名
   private String database;
   // 数据库表名
   private String table;

   // 索引存储类型 nio mmap,默认nio
   private String fsdType;
   // 索引的字段
    private String fields="*";
    // 主键字段
    private String key;
    // 索引类型：normal或者facet 是否使用Facet todo 还未实现
    private String type = "normal";

    // 是否接收从CANAL或者FLINK的CDC数据
    private boolean cdc = false;

    // 重建计划任务Cron表达式
    private String cron;
   
    // 分词器 simple、 smartcn、whitespace 默认是smartcn
    private String analyzer = "smartcn";
    public String getFields() {
        return fields != null ? fields : "*";
    }
   
    // 字段配置如果没有就用默认的配置，不是每个字段都需要配置
    private List<FieldConfig> fieldsConfig = new ArrayList<>();
}
