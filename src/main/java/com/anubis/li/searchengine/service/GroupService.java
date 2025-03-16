package com.anubis.li.searchengine.service;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupService {
    LuceneIndex luceneIndex;
    public GroupService(LuceneIndex luceneIndex){
        this.luceneIndex = luceneIndex;
    }

    /**
     * 根据指定的查询条件和分组字段执行分组搜索。
     *
     * @param query 查询条件
     * @param groupField 分组字段,一般为类型等字段，按照类型分组
     * @param resultType 结果类型：
     *                   "totalHits" 表示返回每个分组的文档总数，
     *                   "fieldValue" 表示返回每个分组的指定字段值列表，如返回分组对应的企业名称或者id值。
     * @param resultField 当 resultType 为 "fieldValue" 时，此参数指定要返回的字段名称
     * @return 包含分组结果的 Map，键为分组值，值为总命中数或字段值列表
     * @throws IOException 如果在搜索过程中发生 I/O 错误
     */
    public Map<String, Object> groupSearch(Query query, String groupField, String resultType , String resultField) throws IOException {

        GroupingSearch groupingSearch = new GroupingSearch("_group_"+groupField);
        groupingSearch.setCachingInMB(1000.0, true);
        groupingSearch.setAllGroups(true);
        groupingSearch.setGroupDocsLimit(1000);

        Map<String, Object> groupResult = new HashMap<>();

        TopGroups<BytesRef> searchResult = groupingSearch.search(luceneIndex.getIndexSearcher(), query, 0, 1000);

        for (GroupDocs<BytesRef> item : searchResult.groups)
        {
            if (item.groupValue  != null)
            {
                if ("fieldValue".equals(resultType) && StringUtil.isEmpty(resultField))
                {
                    List<String> groupsFieldResult = new ArrayList<>();
                    for (ScoreDoc scoreDoc : item.scoreDocs)
                    {
                        Document doc = luceneIndex.getIndexSearcher().doc(scoreDoc.doc);
                        groupsFieldResult.add(doc.get(resultField));
                    }
                    groupResult.put(decodeBytesRef(item.groupValue), groupsFieldResult);

                }
                else
                {
                    groupResult.put(decodeBytesRef(item.groupValue), item.totalHits.value);
                }
            }
        }
        return groupResult;
    }

    public static String decodeBytesRef(BytesRef ref) {
        try {
            return ref.utf8ToString();
        }
        catch(Exception e){
            return ref.toString();
        }
    }

}
