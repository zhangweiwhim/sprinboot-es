package com.zhangweiwhim.es.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangweiwhim.es.document.CarInfoDocument;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zhangweiwhim.es.utils.Constant.INDEX;

/**
 * Description: sprinboot-es
 * Created by zhangwei on 2021/2/2 09:39
 */
@Service
public class CarInfoService {
    private RestHighLevelClient client;
    private ObjectMapper objectMapper;

    public CarInfoService(RestHighLevelClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    /**
     * 对数据进行算分查询
     *
     * @param input
     * @return
     * @throws Exception
     */
    public List<CarInfoDocument> findByInput(String input) throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(input, "brand", "brand.my_pinyin", "series","series.my_pinyin");
        multiMatchQueryBuilder.minimumShouldMatch("100%");
        multiMatchQueryBuilder.type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
        searchSourceBuilder.query(multiMatchQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        return getSearchResult(searchResponse);

    }


    /**
     * 返回数据
     *
     * @param response
     * @return
     */
    private List<CarInfoDocument> getSearchResult(SearchResponse response) {
        SearchHit[] searchHit = response.getHits().getHits();
        List<CarInfoDocument> carInfoDocument = new ArrayList<>();
        for (SearchHit hit : searchHit) {
            Map<String, Object> hitSource = hit.getSourceAsMap();
            float score = hit.getScore();
            String resultStr = hitSource.get("brand") + "-" + hitSource.get("series");
            hitSource.put("result", resultStr);
            hitSource.put("scores", score);
            carInfoDocument
                    .add(objectMapper
                            .convertValue(hitSource, CarInfoDocument.class));
        }
        return carInfoDocument;
    }
}
