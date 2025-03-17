package com.anubis.li.searchengine.core.common.event;

import com.anubis.li.searchengine.core.common.enums.TaskEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EventData {
    private String database;
    private String table;
    private TaskEnum taskType;
    /**
     * 1 CDC 2web提交
     * */
    private int source = 1;
    private Map<String,Object> before = new HashMap<>();
    private Map<String,Object> after = new HashMap<>();
    public EventData(){}
    public EventData(TaskEnum taskType, String tableInfo){
        this.taskType = taskType;
        setTableInfo(tableInfo);
    }
    public void setTableInfo(String tableInfo){
        if(tableInfo ==null){
            return;
        }
        if (tableInfo.contains("::")){
            this.database = tableInfo.split("::")[0];
            this.table = tableInfo.split("::")[1];
        }else {
            this.database= tableInfo;
            this.table = tableInfo;
        }
    }
}
