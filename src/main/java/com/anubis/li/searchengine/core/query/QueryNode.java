package com.anubis.li.searchengine.core.query;

import com.anubis.li.searchengine.core.common.enums.LinkEnum;
import com.anubis.li.searchengine.core.common.enums.OperatorEnum;

import java.util.ArrayList;
import java.util.List;

public class QueryNode {
    /**
     * 查询节点的值列表。
     */
    private List<String> values = new ArrayList<>();
    /**
     * 查询字段。
     */
    private String field;
    /**
     * 查询操作符，用于定义查询条件的比较方式。
     */
    private OperatorEnum operator;
    /**
     * 查询连接类型，用于定义多个查询条件之间的逻辑关系。
     */
    private LinkEnum link;
    /**
     * 子查询节点列表，用于构建查询条件树的子节点。
     */
    private List<QueryNode> children = new ArrayList<>();


    /**
     * 构造函数，用于创建一个空的查询节点对象。
     */
    public QueryNode(){}

    /**
     * 构造函数，用于创建一个具有指定属性的查询节点对象。
     *
     * @param field   查询字段
     * @param operator 查询操作符
     * @param link     查询连接类型
     * @param value    查询值
     */
    public QueryNode(String field, OperatorEnum operator, LinkEnum link, Object value) {
        this.field = field;
        this.operator = operator;
        this.link = link;
        if (value instanceof List) {
            List<String> listValue = (List<String>) value;
            this.values.addAll(listValue);
        } else {
            this.values.add(String.valueOf(value));
        }
    }


    public List<QueryNode> getChildren() {
        return this.children;
    }

    public void setChildren(List<QueryNode> children) {
        this.children = children;
    }

    public LinkEnum getLink() {
        return this.link;
    }

    public void setLink(LinkEnum link) {
        this.link = link;
    }

    public OperatorEnum getOperator() {
        return this.operator;
    }

    public void setOperator(OperatorEnum operator) {
        this.operator = operator;
    }
    public String getField() {
        return this.field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public List<String> getValues() {
        return this.values;
    }
    public void setValues(List<String> values) {
        this.values.addAll(values) ;
    }
}
