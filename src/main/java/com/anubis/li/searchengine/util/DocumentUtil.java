package com.anubis.li.searchengine.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.IndexableFieldType;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public final class DocumentUtil {
    /**
     * 字段信息 Key = indexName + type+ typename + field
     * indexName：索引名称
     * type:Object ResultSet Map
     * typename：类名称，表名，集合名称
     * field：字段名称
     * <p>
     * 返回对象的字段名称
     */
    private static HashMap<String, String> fieldsMap = new HashMap<>();


    /**
     * List<Map<String,Object>>
     */
    public static List<Document> createDocuments(List<Map<String, Object>> values, List<FieldConfig> fieldConfigs) {
        return values.stream().map(e -> createDocument(e, fieldConfigs)).collect(Collectors.toList());
    }

    public static Document createDocument(Map<String, Object> mapValues, List<FieldConfig> fieldConfigs) {
        Document doc = new Document();

        for (FieldConfig e : fieldConfigs) {
            Object fieldNValue = mapValues.containsKey(e.getFieldName()) ? mapValues.get(e.getFieldName()) : mapValues.get(e.getFieldName());
            if (e.isExclude()) {
                continue;
            }
            if (fieldNValue != null && !StringUtil.isEmpty(fieldNValue.toString())) {
                try {
                    e.getHandler().createFields(doc, fieldNValue, e);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    log.info("创建Document报错：字段{}-{}", e.getFieldName(), ex.getMessage());
                }
            }
        }
        return doc;
    }


    /**
     * 根据ResultSet对象创建文档
     */
    public static Document createDocument(ResultSet resultSet, List<FieldConfig> fieldConfigs) {
        Document doc = new Document();
        for (FieldConfig e : fieldConfigs) {
            if (e.isExclude()) {
                continue;
            }
            try {
                Object fieldNValue = e.getHandler().getResultSetValue(resultSet, e.getFieldName());
                if (fieldNValue == null) {
                    continue;
                }
                e.getHandler().createFields(doc, fieldNValue, e);
            } catch (Exception ex) {
                log.info("创建Document报错：字段{}", e.getFieldName(), ex);
            }
        }
        return doc;
    }

    public static List<Map<String, Object>> getModels(List<Document> documents, List<FieldConfig> fieldsConfig) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Document document : documents) {
            list.add(DocumentUtil.getModel(document, fieldsConfig));
        }
        return list;
    }

    public static Map<String, Object> getModel(Document document, List<FieldConfig> fieldsConfig) {

        if (document == null) {
            return null;
        }

        Map<String, Object> model = new HashMap<>();

        for (FieldConfig e : fieldsConfig) {
            if (e.isExclude()) {
                continue;
            }
            String fieldName = e.getFieldName();
            Object fieldValue = document.get(fieldName);
            if (fieldValue != null) {
                if (e.isSets()) {
                    fieldValue = String.join(e.getSetsSplit(), document.getValues(fieldName));
                    model.put(fieldName, fieldValue);
                } else if ("time".equalsIgnoreCase(e.getTypeName())) {
                    model.put(fieldName, e.getHandler().getStringValue(fieldValue));
                } else {
                    model.put(fieldName, fieldValue);
                }
            }
        }
        return model;
    }

    public static void printDocument(Document document) {
        System.out.println("+-----------------+-----------------+--------------------------+-------------------+-----------------+");
        System.out.printf("| %-15s | %-15s | %-30s | %-14s | %-12s |\n", "Field Name", "Value", "docvalue", "Stored", "Indexed");
        System.out.println("+-----------------+-----------------+--------------------------+-------------------+-----------------+");

        for (IndexableField field : document.getFields()) {

            IndexableFieldType fieldType = field.fieldType();
            String docvalues = fieldType.docValuesType().name();
            String stored = fieldType.stored() ? "Yes" : "No";
            String indexed = fieldType.tokenized() ? "Yes" : "No";

            // 如果字段是多值的，只打印第一个值或'N/A'
            String value = field.stringValue() != null ? field.stringValue() : "N/A";

            System.out.printf("| %-15s | %-15s | %-30s | %-14s | %-12s |\n",
                    field.name(), value, docvalues, stored, indexed);
        }

        System.out.println("+-----------------+-----------------+--------------------------+-------------------+-----------------+");
    }

    public static void createFields(Document document, String returnField) {
    }
}
