package com.king.es;


import com.alibaba.fastjson.JSONObject;
import com.king.es.pojo.Book;
import com.sun.org.apache.xpath.internal.operations.Lte;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.network.InetAddresses;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/***
 * ClassName: ElasticSearchTest
 * Package: com.king.es
 * @author GK
 * @date 2023/9/23 15:25
 * @description es测试
 * @version 1.0
 */
public class ElasticSearchTest {
    private TransportClient client;

    @Before
    public void initClient() {
        //连接初始化
        if (client == null) {
            client = new PreBuiltTransportClient(Settings.EMPTY);
            //设置ip地址和端口号
            client.addTransportAddress(
                    new TransportAddress(InetAddresses.forString("127.0.0.1"), 9300));
            System.out.println("初始化连接成功");

        }
    }

    @After
    public void closeClient() {
        //关闭连接
        if (client != null) {
            client.close();
            System.out.println("关闭连接成功");
        }
    }

    /**
     * 创建es索引库
     */
    @Test
    public void createIndex() {
        client.admin()
                .indices()
                .prepareCreate("king")
                .get();
    }

    /**
     * 创建索引库和映射
     */
    @Test
    public void createIndexAndMapping() throws IOException, ExecutionException, InterruptedException {
        //创建索引
        client.admin()
                .indices()
                .prepareCreate("king_new").get();
        //构建映射
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject()
                .startObject("book")
                .startObject("properties")
                .startObject("id")
                .field("type", "long")
                .endObject()
                .startObject("title")
                .field("analyzer", "ik_max_word")
                .field("type", "text")
                .endObject()
                .startObject("content")
                .field("analyzer", "ik_max_word")
                .field("type", "text")
                .endObject()
                .endObject()
                .endObject()
                .endObject();
        client.admin().indices().putMapping(
                        new PutMappingRequest("king_new").type("book").source(builder))
                .get();

    }

    /**
     * 新增数据
     */
    @Test
    public void insertData() {
        //构建数据
        for (long i = 1; i <= 100; i++) {
            Book book = new Book(
                    i,
                    i + "Elasticsearch 是一个分布式的、开源的搜索分析引擎，支持各种数据类型，包括文本、数字、地理、结构化、非结构化。",
                    i + "Elasticsearch 是基于 Apache Lucene 的。Elasticsearch 因其简单的 REST API、分布式特性、告诉、可扩展而闻名。" +
                            "Elasticsearch 是 Elastic 产品栈的核心，Elastic 产品栈是个开源工具集合，用于数据接收、存储、分析、可视化。");
            //存储数据
            client.prepareIndex("king_new", "book", i + "")
                    .setSource(JSONObject.toJSONString(book), XContentType.JSON)
                    .get();
        }
    }

    /**
     * 批量新增数据
     */
    @Test
    public void insertBatch() throws ExecutionException, InterruptedException {
        //批量对象
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        //构建数据
        for (long i = 1; i <= 100; i++) {
            Book book = new Book(
                    i,
                    i + "Elasticsearch 是一个分布式的、开源的搜索分析引擎，支持各种数据类型，包括文本、数字、地理、结构化、非结构化。",
                    i + "Elasticsearch 是基于 Apache Lucene 的。Elasticsearch 因其简单的 REST API、分布式特性、告诉、可扩展而闻名。" +
                            "Elasticsearch 是 Elastic 产品栈的核心，Elastic 产品栈是个开源工具集合，用于数据接收、存储、分析、可视化。");
            //向批量对象存储数据

            bulkRequestBuilder.add(new IndexRequest("king_new", "book", i + "")
                    .source(JSONObject.toJSONString(book), XContentType.JSON));
        }
        //批量执行
        bulkRequestBuilder.execute().get();
    }

    /**
     * 根据下标查询
     * 查询一次文档域,没有分词
     */
    @Test
    public void queryIndex() {
        GetResponse getResponse = client.prepareGet("king_new", "book", "1").get();
        //获取原始数据
        System.out.println(getResponse.getSourceAsString());
    }

    /**
     * 查询所有数据
     * 查询一次文档域,没有分词
     */
    @Test
    public void matchAllQuery() {
        SearchResponse searchResponse = client.prepareSearch("king_new")
                .setTypes("book")
                .setQuery(QueryBuilders.matchAllQuery())
                .get();
        //获取命中数据
        SearchHits hits = searchResponse.getHits();
        //获取迭代器
        Iterator<SearchHit> iterator = hits.iterator();
        //遍历
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
//            System.out.println(next.getSourceAsString());
            System.out.println(JSONObject.parseObject(next.getSourceAsString(), Book.class));
        }
    }


    /**
     * 字符串查询
     * 2次查询,分词
     */
    @Test
    public void queryStringQuery() {
        SearchResponse searchResponse = client.prepareSearch("king_new")
                .setTypes("book")
                .setQuery(QueryBuilders.queryStringQuery("ELASTICSEARCH").field("title"))
                .get();
        //获取命中数据
        SearchHits hits = searchResponse.getHits();
        //获取迭代器
        Iterator<SearchHit> iterator = hits.iterator();
        //遍历
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
//            System.out.println(next.getSourceAsString());
            System.out.println(JSONObject.parseObject(next.getSourceAsString(), Book.class));
        }
    }

    /**
     * 匹配查询
     * 2次查询,分词
     */
    @Test
    public void matchQuery() {
        SearchResponse searchResponse = client.prepareSearch("king_new")
                .setTypes("book")
                .setQuery(QueryBuilders.matchQuery("title", "ELASTICSEARCH"))
                .get();
        //获取命中数据
        SearchHits hits = searchResponse.getHits();
        //获取迭代器
        Iterator<SearchHit> iterator = hits.iterator();
        //遍历
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
//            System.out.println(next.getSourceAsString());
            System.out.println(JSONObject.parseObject(next.getSourceAsString(), Book.class));
        }
    }

    /**
     * 关键字查询
     * 2次查询,不分词
     */
    @Test
    public void termQuery() {
        SearchResponse searchResponse = client.prepareSearch("king_new")
                .setTypes("book")
                .setQuery(QueryBuilders.termQuery("title", "elasticsearch"))
                .get();
        //获取命中数据
        SearchHits hits = searchResponse.getHits();
        //获取迭代器
        Iterator<SearchHit> iterator = hits.iterator();
        //遍历
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
//            System.out.println(next.getSourceAsString());
            System.out.println(JSONObject.parseObject(next.getSourceAsString(), Book.class));
        }
    }

    /**
     * 通配符查询
     * 2次查询,不分词
     */
    @Test
    public void wildcardQuery() {
        SearchResponse searchResponse = client.prepareSearch("king_new")
                .setTypes("book")
                .setQuery(QueryBuilders.wildcardQuery("title", "*ELASTIC*"))
                .get();
        //获取命中数据
        SearchHits hits = searchResponse.getHits();
        //获取迭代器
        Iterator<SearchHit> iterator = hits.iterator();
        //遍历
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
//            System.out.println(next.getSourceAsString());
            System.out.println(JSONObject.parseObject(next.getSourceAsString(), Book.class));
        }
    }

    /**
     * 模糊查询
     * 2次查询,不分词
     */
    @Test
    public void fuzzyQuery() {
        SearchResponse searchResponse = client.prepareSearch("king_new")
                .setTypes("book")
                .setQuery(QueryBuilders.fuzzyQuery("title", "ElasticSearch"))
                .get();
        //获取命中数据
        SearchHits hits = searchResponse.getHits();
        //获取迭代器
        Iterator<SearchHit> iterator = hits.iterator();
        //遍历
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
//            System.out.println(next.getSourceAsString());
            System.out.println(JSONObject.parseObject(next.getSourceAsString(), Book.class));
        }

    }

    /**
     * 范围查询
     */
    @Test
    public void rangeQuery() {
        SearchResponse searchResponse = client.prepareSearch("king_new")
                .setTypes("book")
                .setQuery(QueryBuilders.rangeQuery("id").gt(10).lte(16))
                .get();
        //获取命中数据
        SearchHits hits = searchResponse.getHits();
        //获取迭代器
        Iterator<SearchHit> iterator = hits.iterator();
        //遍历
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
//            System.out.println(next.getSourceAsString());
            System.out.println(JSONObject.parseObject(next.getSourceAsString(), Book.class));
        }

    }

    /**
     * 排序和分页
     *
     */
    @Test
    public void SortAndFromSize() {
        SearchResponse searchResponse = client.prepareSearch("king_new")
                .setTypes("book")
                .setQuery(QueryBuilders.matchAllQuery())
                //排序
                .addSort(new FieldSortBuilder("id").order(SortOrder.DESC))
                //分页
                .setFrom(10)
                .setSize(20)
                .get();
        //获取命中数据
        SearchHits hits = searchResponse.getHits();
        //获取迭代器
        Iterator<SearchHit> iterator = hits.iterator();
        //遍历
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
//            System.out.println(next.getSourceAsString());
            System.out.println(JSONObject.parseObject(next.getSourceAsString(), Book.class));
        }

    }

    /**
     * 组合查询
     */
    @Test
    public void boolQuery() {
        SearchResponse searchResponse = client.prepareSearch("king_new")
                .setTypes("book")
                .setQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.rangeQuery("id").gte(20).lte(80))
                                .must(QueryBuilders.termQuery("title","搜索"))
                                .should(QueryBuilders.matchQuery("content","Lucene")))
                //排序
                .addSort(new FieldSortBuilder("id").order(SortOrder.DESC))
                //分页
                .setFrom(10)
                .setSize(20)
                .get();
        //获取命中数据
        SearchHits hits = searchResponse.getHits();
        //获取迭代器
        Iterator<SearchHit> iterator = hits.iterator();
        //遍历
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
//            System.out.println(next.getSourceAsString());
            System.out.println(JSONObject.parseObject(next.getSourceAsString(), Book.class));
        }

    }

    /**
     * 高亮查询
     */
    @Test
    public void highlighter() {
        //高亮条件
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("content")
                .preTags("<font sytle=color:red>")
                .postTags("</font>");
        SearchResponse searchResponse = client.prepareSearch("king_new")
                .setTypes("book")
                .setQuery(QueryBuilders.matchQuery("content","ELASTICSEARCH"))
                //排序
                .addSort(new FieldSortBuilder("id").order(SortOrder.DESC))
                //分页
                .setFrom(0)
                .setSize(100)
                .highlighter(highlightBuilder)
                .get();
        //获取命中数据
        SearchHits hits = searchResponse.getHits();
        //获取迭代器
        Iterator<SearchHit> iterator = hits.iterator();
        //遍历
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
//            System.out.println(next.getSourceAsString());
//            System.out.println(JSONObject.parseObject(next.getSourceAsString(), Book.class));
            Book book = JSONObject.parseObject(next.getSourceAsString(), Book.class);
            HighlightField highlightField = next.getHighlightFields().get("content");
            if(highlightField != null) {

                Text[] fragments = highlightField.getFragments();
                if(fragments != null && fragments.length > 0) {
                    String content = "";
                    for (Text fragment : fragments) {
                        content += fragment;
                    }
                    book.setContent(content);
                }
            }
            System.out.println(book);

        }

    }


}
