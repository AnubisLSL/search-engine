package com.anubis.li.searchengine.core.query;

import com.anubis.li.searchengine.core.LuceneService;
import com.anubis.li.searchengine.core.common.enums.LinkEnum;
import com.anubis.li.searchengine.core.common.enums.OperatorEnum;
import com.anubis.li.searchengine.core.common.enums.QueryEnum;
import com.anubis.li.searchengine.core.common.utils.StringUtil;
import com.anubis.li.searchengine.core.handle.TypeHandler;
import com.anubis.li.searchengine.core.model.FieldConfig;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class QueryBuilder {

    private static final Logger log = LoggerFactory.getLogger(QueryBuilder.class);
    private String indexName;
    private String field;
    private int page=1;
    private int pageSize=10;

    private List<QueryNode> queryNodes;
    private QueryEnum type;
    private List<QueryNode> nodes;
    List<FieldConfig> fieldConfigs ;
    BooleanQuery.Builder builder = new BooleanQuery.Builder();
    private Map<String, Boolean> orders = new LinkedHashMap<>();

    public QueryBuilder(ObjectNode jsonObject, List<FieldConfig> fieldConfigs) {
        this.page  = jsonObject.get("page").asInt();
        this.pageSize  = jsonObject.get("pageSize").asInt();
        this.indexName  = jsonObject.get("name").asText();
        this.field  = jsonObject.get("field").asText();
        this.type = QueryEnum.valueOf(jsonObject.get("type").asText());
        buildNode(jsonObject);
        buildOrders(jsonObject);
        this.fieldConfigs = fieldConfigs;
    }

    public Query build(){
        if (nodes != null){
            for(QueryNode node : nodes ){
                buildQuery(builder,node);
            }
        }
        Query query = builder.build();
        if (query == null || StringUtil.isEmpty(query.toString())){
            query = new MatchAllDocsQuery();
        }
        return query;
    }
    private void buildNode(ObjectNode jsonObject){
        if (jsonObject.has("nodes")   &&  !jsonObject.get("nodes").isEmpty()){
            this.nodes = JsonUtil.toObjectList(jsonObject.get("nodes").toString(),QueryNode.class);
        }
    }
    private void buildOrders(ObjectNode jsonObject)  {
        if(!jsonObject.has("orders")   ||  jsonObject.get("orders").isEmpty())
        {
            return;
        }
        jsonObject.get("orders").forEach(ordersNodes -> {
            orders.putAll(JsonUtil.deserializeJsonToMap(ordersNodes.toString(),String.class,Boolean.class));
        });
    }
    private void buildQuery(BooleanQuery.Builder parentBuilder, QueryNode node){

        if(!OperatorEnum.SECTION.equals(node.getOperator())){
            Query query = createQuery(node);
            if(query != null){
                parentBuilder.add(query,getOccur(node));
            }
        }else{
            BooleanQuery.Builder sectionBuilder =  new BooleanQuery.Builder();
            for(QueryNode childNode : node.getChildren() ){
                buildQuery(sectionBuilder,childNode);
            }
            parentBuilder.add(sectionBuilder.build(),  getOccur(node));
        }
    }
    private BooleanClause.Occur getOccur(QueryNode node){
        LinkEnum link = node.getLink();
        OperatorEnum operator = node.getOperator();
        switch (operator) {
            case NOTIN:
            case ISNULL:
            case NE:
                return BooleanClause.Occur.MUST_NOT;
            default:
                if (LinkEnum.AND.equals(link)){
                    return BooleanClause.Occur.MUST;
                }else if (LinkEnum.NOT.equals(link)){
                    return BooleanClause.Occur.MUST_NOT;
                }else {
                    return BooleanClause.Occur.SHOULD;
                }
        }
    }
    private Query createQuery(QueryNode node){
        FieldConfig fieldConfig = getFieldConfig(node.getField());
        if (fieldConfig == null){
            throw  new SearchException("未找到字段配置："+node.getField());
        }
        TypeHandler typeHandler =  fieldConfig.getHandler() ;
        if (typeHandler == null){
            throw  new SearchException("未找到类型处理器："+node.getField()+",type:"+fieldConfig.getTypeName());
        }
        if (node.getValues() == null || node.getValues().size()==0){
            throw  new SearchException("没有值："+node.getField());
        }
        if(OperatorEnum.Parser.equals(node.getOperator())){
            return typeHandler.parser(node.getField(),node.getValues(), LuceneService.getIndex(this.indexName).getAnalyzer());
        }else if(OperatorEnum.Phrase.equals(node.getOperator())){
            return typeHandler.phrase(node.getField(),node.getValues(),LuceneService.getIndex(this.indexName).getAnalyzer());
        }else{
            return typeHandler.createQuery(node);
        }

    }

    private FieldConfig getFieldConfig(String field){
        for(FieldConfig fieldConfig : fieldConfigs){
            if(fieldConfig.getFieldName().equals(field)){
                return fieldConfig;
            }
        }
        return null;
    }

    public BooleanQuery buildFieldQuery(String field){
        BooleanQuery.Builder builderField = new BooleanQuery.Builder();
        for(QueryNode node : nodes ){
            if (field.equals(node.getField())){
                buildQuery(builderField,node);
            }
        }
        return builderField.build();
    }
    public Term buildFieldTerm(String field){
        BooleanQuery.Builder builderField = new BooleanQuery.Builder();
        for(QueryNode node : nodes ){
            if (field.equals(node.getField())){
                return new Term(field,node.getValues().get(0));
            }
        }
        return null;
    }
    public Document buildDocument(){
        Map<String,Object> fields = new HashMap<>();
        for(QueryNode node : nodes ){
            if (node.getOperator().equals(OperatorEnum.EQ)){
                fields.put(node.getField(),node.getValues().get(0));
            }
        }
        return DocumentUtil.createDocument(fields,fieldConfigs);
    }

    public Sort getSort(){
        if (this.orders == null || this.orders.isEmpty()){
            return null;
        }
        Sort sort = new Sort();
        this.orders.forEach((field,order)->{
            sort.setSort( getFieldConfig(field).getHandler().getSortField(field, order));
        });
        return sort;
    }
    public List<QueryNode> getNodes() {
        return nodes;
    }

    public QueryEnum getType() {
        return type;
    }

    public List<QueryNode> getQueryNodes() {
        return queryNodes;
    }

    public String getField() { return  field==null || field.isEmpty() ? "*" : field; }

    public String getIndexName() {
        return indexName;
    }

    public Integer getPage() { return this.page<=0 ? 1:this.page; }

    public Integer getPageSize() { return this.pageSize; }

}
