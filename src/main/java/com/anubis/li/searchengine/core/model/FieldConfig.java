package com.anubis.li.searchengine.core.model;

import com.anubis.li.searchengine.core.common.Table;
import com.anubis.li.searchengine.core.common.utils.TypeUtil;
import com.anubis.li.searchengine.core.handle.TypeHandler;
import com.anubis.li.searchengine.core.util.QueryUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@Table(name = "index_field")
public class FieldConfig {
    /// <summary>
    /// 主键
    /// </summary>
    private String id;

    /// <summary>
    /// 索引名称
    /// </summary>
    private String indexName;

    /// <summary>
    ///  字段名称：数据库字段，对象的字段等
    /// </summary>
    private String fieldName;

    /// <summary>
    /// 值的类型
    /// </summary>
    @JsonIgnore
    private TypeHandler handler;

    private String  typeName = "";

    /// <summary>
    /// 是否FacetField
    /// </summary>
    private boolean facet  = false;

    /// <summary>
    /// 是否存储
    /// </summary>
    private boolean stored  = true;

    /// <summary>
    /// 是否分词
    /// </summary>
    private boolean analyzerd  = false;

    /// <summary>
    /// 是否启用字典功能
    /// </summary>
    private boolean sets = false;
    /// <summary>
    /// 字典分隔符
    /// </summary>
    private String setsSplit=",";

    /// <summary>
    /// 是否参与Group搜索
    /// </summary>
    private boolean grouping=false;

    /// <summary>
    /// 是否参与排序
    /// </summary>
    private boolean sort=false;

    /// <summary>
    /// 是否排除
    /// </summary>
    private boolean exclude=false;
    /// <summary>
    /// 是否虚拟字段，合并实体字段成虚拟字段
    /// </summary>
    private boolean virtual = false;
    /// <summary>
    /// 虚拟字段需要合并的实体字段，用英文逗号隔开
    /// </summary>
    private String virtualFields="";

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
        this.handler = QueryUtil.getTypeHandler(TypeUtil.getType(typeName)) ;
    }
    public String getSetsSplit()
    {
        if (setsSplit == null || setsSplit.length()==0){
            return ",";
        }else {
            return setsSplit;
        }
    }

}
