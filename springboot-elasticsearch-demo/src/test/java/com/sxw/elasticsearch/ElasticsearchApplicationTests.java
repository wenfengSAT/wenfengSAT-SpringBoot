package com.sxw.elasticsearch;

import com.sxw.elasticsearch.mapper.ExtResultMapper;
import com.sxw.elasticsearch.model.Item;
import com.sxw.elasticsearch.repository.ItemRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 根据官方文档测试常用的api
 * 文档地址:https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchApplicationTests.class);
    @Autowired private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired private ItemRepository itemRepository;
    @Resource private ExtResultMapper extResultMapper;


    /**
     * @Description:创建索引，会根据Item类的@Document注解信息来创建
     */
    @Test
    public void testCreateIndex() {
        elasticsearchTemplate.createIndex(Item.class);
    }

    /**
     * 索引数据
     */
    @Test
    public void indexItem(){
        Item item = new Item();
        item.setId(1L);
        item.setTitle("MacBook Pro");
        item.setCategory("笔记本电脑");
        item.setBrand("苹果");
        item.setPrice(12999.0);
        item.setImages("https://www.apple.com/mac.png");

        Item item1 = new Item();
        item1.setId(2L);
        item1.setTitle("重构 改善既有代码的设计");
        item1.setCategory("程序设计");
        item1.setBrand("马丁·福勒(Martin Fowler)");
        item1.setPrice(118.00);
        item1.setImages("http://product.dangdang.com/26913154.html");

        Item item2 = new Item();
        item2.setId(3L);
        item2.setTitle("Python编程 从入门到实践");
        item2.setCategory("Python");
        item2.setBrand("埃里克·马瑟斯（Eric Matthes）");
        item2.setPrice(61.40);
        item2.setImages("http://bang.dangdang.com/books/bestsellers/01.54.00.00.00.00-recent7-0-0-1-1");

        Item item3 = new Item();
        item3.setId(4L);
        item3.setTitle("统计之美：人工智能时代的科学思维");
        item3.setCategory("数学");
        item3.setBrand("李舰");
        item3.setPrice(56.70);
        item3.setImages("http://product.dangdang.com/26915070.html");

        Item item4 = new Item();
        item4.setId(5L);
        item4.setTitle("机器学习");
        item4.setCategory("人工智能");
        item4.setBrand("周志华");
        item4.setPrice(61.60);
        item4.setImages("http://product.dangdang.com/23898620.html");


        itemRepository.index(item1);
        itemRepository.index(item2);
        itemRepository.index(item3);
        itemRepository.index(item4);
    }

    /**
     * 搜索
     */
    @Test
    public void testSearch(){
        List<Item> itemList = itemRepository.findByTitleLike("Mac");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    /**
     * 返回实体数量
     */
    @Test
    public void testCount(){
        long count = itemRepository.count();
        System.out.println(count);
    }

    /**
     * 查找全部
     */
    @Test
    public void testFindAll(){
        Iterable<Item> items = itemRepository.findAll();
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()){
            logger.info(iterator.next().toString());
        }
    }

    /**
     * 返回由给定ID标识的实体
     */
    @Test
    public void testFindById(){
        Optional<Item> item = itemRepository.findById(1L);
        logger.info(item.get().toString());
    }

    /**
     * 指示是否存在具有给定ID的实体
     */
    @Test
    public void testExistsById(){
        logger.info(itemRepository.existsById(2L) + "");
    }


    /**
     * 测试分页
     */
    @Test
    public void testPage(){
        // 注意：页数从 0 开始，0 代表第一页
        Page<Item> page = itemRepository.findAll(PageRequest.of(0, 3));
        // 总条数
        logger.info(page.getTotalElements() + "");

        Iterator<Item> iterator = page.iterator();
        while (iterator.hasNext()){
            logger.info(iterator.next().toString());
        }

        // 总页数
        logger.info(page.getTotalPages() + "");
    }

    /**
     * 区间检索
     */
    @Test
    public void testBetween(){
        List<Item> items = itemRepository.findByPriceBetween(50.0,70.0);
        for (Item item : items) {
            logger.info(item.toString());
        }
    }

    /**
     * 测试查询
     */
    @Test
    public void testQuery(){
        String keyword = "程序设计";

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 给name字段更高的权重
        queryBuilder.should(QueryBuilders.matchQuery("title", keyword).boost(3));
        // description 默认权重 1
        queryBuilder.should(QueryBuilders.matchQuery("category", keyword));
        // 至少一个should条件满足
        queryBuilder.minimumShouldMatch(1);

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder)
                .withPageable(PageRequest.of(0, 10))
                .build();
        logger.info("\n search(): searchContent [" + keyword + "] \n DSL  = \n " + searchQuery.getQuery().toString());

        Page<Item> page = itemRepository.search(searchQuery);

        // 总条数
        logger.info(page.getTotalElements() + "");

        Iterator<Item> iterator = page.iterator();
        while (iterator.hasNext()){
            logger.info(iterator.next().toString());
        }

        // 总页数
        logger.info(page.getTotalPages() + "");
    }

    /**
     * 测试查询高亮
     * 参考文章：https://www.cnblogs.com/vcmq/p/9966693.html
     */
    @Test
    public void testHighlightQuery(){
        String keyword = "程序设计";

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 至少一个should条件满足
        boolQuery.minimumShouldMatch(1);

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<span style=\"color:red\">").postTags("</span>"),
                        new HighlightBuilder.Field("brand").preTags("<span style=\"color:red\">").postTags("</span>"))
                .withPageable(PageRequest.of(0, 10));
        // 最佳字段  + 降低除了name之外字段的权重系数
        MatchQueryBuilder nameQuery = QueryBuilders.matchQuery("title", keyword).analyzer("ik_max_word");
        MatchQueryBuilder authorQuery = QueryBuilders.matchQuery("brand", keyword).boost(0.8f);
        DisMaxQueryBuilder disMaxQueryBuilder = QueryBuilders.disMaxQuery().add(nameQuery).add(authorQuery);
        queryBuilder.withQuery(disMaxQueryBuilder);

        NativeSearchQuery searchQuery = queryBuilder.build();
        Page<Item> items = elasticsearchTemplate.queryForPage(searchQuery, Item.class, extResultMapper);

        items.forEach(e -> logger.info("{}", e));
    }
}
