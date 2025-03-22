package com.anubis.li.searchengine.core.controller;

import com.anubis.li.searchengine.core.LuceneIndex;
import com.anubis.li.searchengine.core.LuceneService;
import com.anubis.li.searchengine.core.common.Result;
import com.anubis.li.searchengine.core.common.SearchPage;
import com.anubis.li.searchengine.core.common.enums.QueryEnum;
import com.anubis.li.searchengine.core.common.utils.JsonUtil;
import com.anubis.li.searchengine.core.query.QueryBuilder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/search")
public class SearchController {

    @PostMapping(value = "/wrapper")
    public Result<?> wrapper(@RequestBody String body) throws Exception {

        ObjectNode jsonObject = JsonUtil.getJsonObject(body);

        LuceneIndex luceneIndex =  LuceneService.getIndex(jsonObject.get("name").asText());
        if (luceneIndex == null){
            return Result.error("索引不存在,请检查From参数name值");
        }
        QueryBuilder queryBuilder = new QueryBuilder(jsonObject, luceneIndex.getIndexConfig().getFieldsConfig());
        if (queryBuilder.getType() == QueryEnum.SELECT){
            SearchPage searchPage = new SearchPage(queryBuilder.getPage(),queryBuilder.getPageSize());
            searchPage = luceneIndex.getSearchService().searchList(queryBuilder.build(),searchPage,queryBuilder.getSort(),queryBuilder.getField());
            return Result.OK(searchPage);
        }else if (queryBuilder.getType() == QueryEnum.DELETE){
            luceneIndex.getDeleteService().deleteDocuments(queryBuilder.build());
        }else if (queryBuilder.getType() == QueryEnum.UPDATE){
            Document document = queryBuilder.buildDocument();
            luceneIndex.getUpdateService().updateDocumentFieldsValue(document,queryBuilder.buildFieldQuery(luceneIndex.getIndexConfig().getKey()));
        }else if (queryBuilder.getType() == QueryEnum.GROUP){
            return Result.OK(luceneIndex.getGroupService().groupSearch(queryBuilder.build(),queryBuilder.getField(),"totalHits",queryBuilder.getField()));
        }else if (queryBuilder.getType() == QueryEnum.COUNT){
            return Result.OK(luceneIndex.getSearchService().count(queryBuilder.build()));
        }
        return Result.OK();
    }

}
