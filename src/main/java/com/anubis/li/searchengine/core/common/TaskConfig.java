package com.anubis.li.searchengine.core.common;

import com.anubis.li.searchengine.core.common.enums.TaskEnum;
import lombok.Data;

@Data
@Table( name="task")
public class TaskConfig {
    private String id;
    private String name;
    private String indexName;
    private TaskEnum type;
    private String cron;
    private String dbsql;
}
