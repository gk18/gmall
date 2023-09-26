package com.king.gmall.list.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.king.gmall.list.service.SearchService;
import com.king.gmall.model.list.Goods;
import com.king.gmall.model.list.SearchResponseAttrVo;
import com.king.gmall.model.list.SearchResponseTmVo;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/***
 * ClassName: SearchServiceImpl
 * Package: com.king.gmall.list.service.impl
 * @author GK
 * @date 2023/9/24 11:44
 * @description 商品查询接口的实现类
 * @version 1.0
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    /**
     * 查询商品
     *
     * @param searchData
     * @return
     */
    @Override
    public Map<String, Object> search(Map<String, String> searchData) throws IOException {
        //查询结果初始化
        Map<String, Object> result = new HashMap<String, Object>();
        //构建查询条件
        SearchRequest searchRequest = buildSearchRequest(searchData);
        //搜索
        SearchResponse searchResponse =
                restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //解析返回结果并返回
        return resolverResponse(searchResponse);
    }

    /**
     * 构建查询条件
     *
     * @param searchData
     * @return
     */
    private SearchRequest buildSearchRequest(Map<String, String> searchData) {
        //声明查询请求
        SearchRequest searchRequest = new SearchRequest("goods_java0107");
        //声明条件构造器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //声明组合查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //搜索框条件(分词)---->searchText=手机
        if (!StringUtils.isEmpty(searchData.get("searchText"))) {
            boolQueryBuilder.must(
                    QueryBuilders.matchQuery("title", searchData.get("searchText")));
        }
        //三级分类id查询(不分词)---->category=分类id:分类名
        if (!StringUtils.isEmpty(searchData.get("category"))) {
            String[] split = searchData.get("category").split(":");
            boolQueryBuilder.must(
                    QueryBuilders.termQuery("category3Id", split[0])
            );
        }
        //品牌数据查询(不分词)---->tm=品牌id
        if (!StringUtils.isEmpty(searchData.get("tm"))) {
            String[] split = searchData.get("tm").split(":");
            boolQueryBuilder.must(
                    QueryBuilders.termQuery("tmId", split[0]));
        }
        //平台属性查询(不分词)---->attr_平台属性名=平台属性名id:平台属性值
        searchData.entrySet().stream().forEach(entry -> {
            if (entry.getKey().startsWith("attr_")) {
                //平台属性值
                String attrValue = entry.getValue();
                String[] split = attrValue.split(":");
                BoolQueryBuilder attrQueryBuild = QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("attrs.attrId", split[0]))
                        .must(QueryBuilders.termQuery("attrs.attrValue", split[1]));
                //使用nested是且关系
                boolQueryBuilder.must(
                        QueryBuilders.nestedQuery(
                                "attrs",
                                attrQueryBuild,
                                ScoreMode.None));
            }
        });
        //价格查询
        if (!StringUtils.isEmpty(searchData.get("price"))) {
            //去除多余字符
            String price = searchData.get("price");
            price = price
                    .replaceAll("元", "")
                    .replaceAll("以上", "");
            String[] split = price.split("-");
            //大于等于第一个值
            boolQueryBuilder.must(QueryBuilders.rangeQuery("price")
                    .gte(split[0]));
            if (split.length > 1) {
                //小于第二个值
                boolQueryBuilder.must(QueryBuilders.rangeQuery("price")
                        .lt(split[1]));
            }
        }
        //设置组合查询条件
        sourceBuilder.query(boolQueryBuilder);
        //高亮查询条件
        HighlightBuilder highlightBuilder = new HighlightBuilder();

        highlightBuilder
                .field("title")//域
                .preTags("<font style=color:red>")//前缀
                .postTags("</font>");//后缀
        //设置高亮查询
        sourceBuilder.highlighter(highlightBuilder);
        //品牌聚合
        TermsAggregationBuilder tmAggregationBuilder = AggregationBuilders.terms("aggTmId").field("tmId")
                .subAggregation(AggregationBuilders.terms("aggTmName").field("tmName"))
                .subAggregation(AggregationBuilders.terms("aggTmLogoUrl").field("tmLogoUrl"))
                .size(100);
        sourceBuilder.aggregation(tmAggregationBuilder);
        //平台属性聚合
        NestedAggregationBuilder attrAggregationBuilder = AggregationBuilders.nested("aggAttrs", "attrs")
                .subAggregation(
                        AggregationBuilders.terms("aggAttrId").field("attrs.attrId")
                                .subAggregation(AggregationBuilders.terms("aggAttrName").field("attrs.attrName"))
                                .subAggregation(AggregationBuilders.terms("aggAttrValue").field("attrs.attrValue"))
                                .size(100));
        sourceBuilder.aggregation(attrAggregationBuilder);
        //排序和每页显示数量
        if (!StringUtils.isEmpty(searchData.get("sortRule"))
                && !StringUtils.isEmpty(searchData.get("sortField"))) {
            //指定排序
            sourceBuilder.sort(searchData.get("sortField"),
                    SortOrder.valueOf(searchData.get("sortRule")));
        } else {
            //默认按id排序,新品
            sourceBuilder.sort("id", SortOrder.DESC);
        }
        //每页50条
        sourceBuilder.size(50);
        //分页
        sourceBuilder.from((getPage(searchData.get("pageNum")) - 1) * 50);

        searchRequest.source(sourceBuilder);
        return searchRequest;
    }

    /**
     * 获取页码
     *
     * @param page
     * @return
     */
    private int getPage(String page) {
        try {
            Integer pageNum = Integer.valueOf(page);
            return pageNum > 0 ? pageNum : 1;

        } catch (Exception e) {
            return 1;
        }
    }


    /**
     * 解析查询结果
     *
     * @param searchResponse
     * @return
     */
    private Map<String, Object> resolverResponse(SearchResponse searchResponse) {
        //初始化返回数据
        Map<String, Object> result = new HashMap<String, Object>();
        //获取命中的数据
        SearchHits hits = searchResponse.getHits();
        //命中数据的数量
        long total = hits.getTotalHits();
        result.put("total", total);
        //初始化集合存放数据
        List<Goods> goodsList = new ArrayList<Goods>();
        //获取命中数据迭代器
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            //获取命中数据
            SearchHit next = iterator.next();
            //获取原始数据
            String sourceAsString = next.getSourceAsString();
            //反序列化
            Goods goods = JSONObject.parseObject(sourceAsString, Goods.class);
            HighlightField highlightField = next.getHighlightFields().get("title");
            if(highlightField != null) {
                Text[] fragments = highlightField.getFragments();
                if(fragments  != null && fragments.length > 0) {
                    goods.setTitle("");
                    for (Text fragment : fragments) {
                        goods.setTitle(goods.getTitle() + fragment);
                    }

                }
            }
            goodsList.add(goods);

        }
        result.put("goodsList", goodsList);
        //获取聚合结果
        Aggregations aggregations = searchResponse.getAggregations();
        //解析品牌聚合结果
        List<SearchResponseTmVo> searchResponseTmVoList = getTmAggResult(aggregations);
        result.put("searchResponseTmVoList", searchResponseTmVoList);
        //解析平台属性聚合结果
        List<SearchResponseAttrVo> searchResponseAttrVoList = getAttrAggResult(aggregations);
        result.put("searchResponseAttrVoList", searchResponseAttrVoList);

        return result;
    }

    /**
     * 解析平台属性聚合结果
     *
     * @param aggregations
     * @return
     */
    private List<SearchResponseAttrVo> getAttrAggResult(Aggregations aggregations) {
        //获取平台属性聚合
        ParsedNested aggAttrs = aggregations.get("aggAttrs");
        //获取子聚合---->aggAttrId
        ParsedLongTerms aggAttrId = aggAttrs.getAggregations().get("aggAttrId");
        return aggAttrId.getBuckets().stream().map(attrId -> {
            //平台属性展示对象初始化
            SearchResponseAttrVo searchResponseAttrVo = new SearchResponseAttrVo();
            searchResponseAttrVo.setAttrId(attrId.getKeyAsNumber().longValue());
            //获取子聚合---->aggAttrName
            ParsedStringTerms aggAttrName = attrId.getAggregations().get("aggAttrName");
            List<? extends Terms.Bucket> aggAttrNameBuckets = aggAttrName.getBuckets();
            if (aggAttrNameBuckets != null && !aggAttrNameBuckets.isEmpty()) {
                searchResponseAttrVo.setAttrName(aggAttrNameBuckets.get(0).getKeyAsString());
            }
            //获取子聚合---->aggAttrValue
            ParsedStringTerms aggAttrValue = attrId.getAggregations().get("aggAttrValue");
            List<? extends Terms.Bucket> aggAttrValueBuckets = aggAttrValue.getBuckets();
            if (aggAttrValueBuckets != null && !aggAttrValueBuckets.isEmpty()) {
                List<String> attrValueList = aggAttrValueBuckets.stream().map(attrValue -> {
                    return attrValue.getKeyAsString();
                }).collect(Collectors.toList());
                searchResponseAttrVo.setAttrValueList(attrValueList);
            }
            return searchResponseAttrVo;
        }).collect(Collectors.toList());

    }

    /**
     * 解析品牌聚合数据
     *
     * @param aggregations
     * @return
     */
    private List<SearchResponseTmVo> getTmAggResult(Aggregations aggregations) {

        ParsedLongTerms aggTmId = aggregations.get("aggTmId");
        //遍历聚合结果
        return aggTmId.getBuckets().stream().map(bucket -> {
            //初始化品牌显示对象
            SearchResponseTmVo searchResponseTmVo = new SearchResponseTmVo();
            //品牌id
            searchResponseTmVo.setTmId(bucket.getKeyAsNumber().longValue());
            //获取子聚合结果:品牌名
            ParsedStringTerms aggTmName = bucket.getAggregations().get("aggTmName");
            List<? extends Terms.Bucket> aggTmNameBuckets = aggTmName.getBuckets();
            if (aggTmNameBuckets != null && !aggTmNameBuckets.isEmpty()) {

                searchResponseTmVo.setTmName(aggTmNameBuckets.get(0).getKeyAsString());
            }
            //获取子聚合结果:品牌logoUrl
            ParsedStringTerms aggTmLogoUrl = bucket.getAggregations().get("aggTmLogoUrl");
            List<? extends Terms.Bucket> aggTmLogoUrlBuckets = aggTmLogoUrl.getBuckets();
            if (aggTmLogoUrlBuckets != null && !aggTmLogoUrlBuckets.isEmpty()) {

                searchResponseTmVo.setTmLogoUrl(aggTmLogoUrlBuckets.get(0).getKeyAsString());
            }
            return searchResponseTmVo;
        }).collect(Collectors.toList());
    }
}
