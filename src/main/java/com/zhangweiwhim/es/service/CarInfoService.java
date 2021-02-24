package com.zhangweiwhim.es.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangweiwhim.es.document.CarInfoDocument;
import com.zhangweiwhim.es.model.PageModel;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.asyncsearch.AsyncSearchResponse;
import org.elasticsearch.client.asyncsearch.SubmitAsyncSearchRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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

//        SubmitAsyncSearchRequest request
//                = new SubmitAsyncSearchRequest(searchSource, indices);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(input, "brand", "brand.my_pinyin", "series", "series.my_pinyin");
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(input, "series.my_pinyin", "price.my_pinyin");
        multiMatchQueryBuilder.minimumShouldMatch("100%");
        multiMatchQueryBuilder.type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
        searchSourceBuilder.query(multiMatchQueryBuilder);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        List<HighlightBuilder.Field> lst = new ArrayList<>();
//        HighlightBuilder.Field highlightBrand = new HighlightBuilder.Field("brand");
//        highlightBuilder.field(highlightBrand);
//        HighlightBuilder.Field highlightSeries = new HighlightBuilder.Field("series");
//        highlightBuilder.field(highlightSeries);
//        HighlightBuilder.Field highlightBrandPY = new HighlightBuilder.Field("brand.my_pinyin");
//        highlightBuilder.field(highlightBrandPY);
        HighlightBuilder.Field highlightSeriesPY = new HighlightBuilder.Field("series.my_pinyin");
        highlightBuilder.field(highlightSeriesPY);
        HighlightBuilder.Field highlightPricePY = new HighlightBuilder.Field("price.my_pinyin");
        highlightBuilder.field(highlightPricePY);
        highlightBuilder.requireFieldMatch(true);//多个高亮显示
        String leftTag = "<span style='color:#606'>";
        String rightTag = "</span>";
        highlightBuilder.preTags(leftTag);
        highlightBuilder.postTags(rightTag);
        highlightBuilder.requireFieldMatch(true);
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.sort("uptime", SortOrder.DESC);
        searchRequest.source(searchSourceBuilder);

//        AsyncSearchResponse response = client.asyncSearch()
//                .submit(searchRequest, RequestOptions.DEFAULT);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        return getSearchResult(searchResponse);

    }


    /**
     * 分页查询
     *
     * @param input
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<CarInfoDocument> findByInputAndPage(String input, int pageNo, int pageSize) throws IOException {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(input, "series.my_pinyin", "price.my_pinyin");
        multiMatchQueryBuilder.minimumShouldMatch("100%");
        multiMatchQueryBuilder.type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
        searchSourceBuilder.query(multiMatchQueryBuilder);
        searchSourceBuilder.from((pageNo - 1 )* pageSize).size(pageSize).sort("uptime", SortOrder.DESC);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits searchHit = searchResponse.getHits();
        long totalRows = searchHit.getTotalHits().value;
        List<CarInfoDocument> carInfoDocument = new ArrayList<>();
        for (SearchHit hit : searchHit) {
            Map<String, Object> hitSource = hit.getSourceAsMap();
            carInfoDocument
                    .add(objectMapper
                            .convertValue(hitSource, CarInfoDocument.class));
        }

        return carInfoDocument;

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
//            float score = hit.getScore();
            //            hitSource.put("scores", score);
//            String resultStr = hitSource.get("brand") + "-" + hitSource.get("series");

            String hitPrice = (String) hitSource.get("price");
            String hitSeries = (String) hitSource.get("series");
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightSeries = highlightFields.get("series.my_pinyin");
            HighlightField highlightPrice = highlightFields.get("price.my_pinyin");
            String fragmentString = "";
            if (highlightSeries != null) {
                Text[] fragments = highlightSeries.fragments();
                fragmentString = fragments[0].string() + "<br>" + hitPrice + "万";
            }
            if (highlightPrice != null) {
                Text[] fragments = highlightPrice.fragments();
                fragmentString = hitSeries + "<br>" + fragments[0].string() + "万";
            }
            hitSource.put("result", fragmentString);
            carInfoDocument
                    .add(objectMapper
                            .convertValue(hitSource, CarInfoDocument.class));
        }
        return carInfoDocument;
    }
}
