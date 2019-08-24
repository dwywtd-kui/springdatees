package com.inspur.springdataelasticsearch5.controller;


import com.inspur.springdataelasticsearch5.entity.Article;
import com.inspur.springdataelasticsearch5.repository.ESRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class QueryController {
    @Autowired
    private ESRepository esRepository;
    @Autowired
    private ElasticsearchTemplate esTemplate;

    /**
     * 根据内容查询
     * @return
     */
    @RequestMapping("/findbytext")
    public String find01(@RequestParam("text")String text, ModelMap model){
        List<Article> articles = esRepository.findByText(text);
        //for(Article article:articles){
        //     System.out.println(article.toString());
        //}
        model.addAttribute("articlelist",articles);
        return "articles";
    }
    /**
     * 分析查询
     * @return
     */
    @RequestMapping("/querystring")
    public String find02(){
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("500强")
                .defaultField("text")
                .analyzer("ik_smart");

        Pageable pageable = PageRequest.of(0,5);

        HighlightBuilder.Field field = new HighlightBuilder
                .Field("text")
                .preTags("<em>")
                .postTags("</em>");

        SearchQuery searchQuery=new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                . withHighlightFields(field)
                .build();

        //不使用高亮显示
        //AggregatedPage<Article> articleAggregatedPage1 = esTemplate.queryForPage(searchQuery, Article.class);

        //使用高亮显示
        AggregatedPage<Article> articleAggregatedPage = esTemplate.queryForPage(searchQuery, Article.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                List<Article> articleList = new ArrayList<>();
                SearchHits searchHits = searchResponse.getHits();
                if (searchHits.getHits().length<=0){
                    return null;
                }
                System.out.println(searchHits.getTotalHits()+"--------------");

                for (SearchHit searchHit:searchHits){

                    Article article=new Article();

                    Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

                    System.out.println(highlightFields);

                    //取content高亮显示的结果
                    HighlightField field = highlightFields.get("text");
                    Text[] fragments = field.getFragments();

                    StringBuilder sb=new StringBuilder();

                    for(Text t :fragments){
                        if(!t.equals("")){
                            sb.append(t.toString());
                        }
                    }
                    article.setText(sb.toString());
                    article.setId(sourceAsMap.get("id").toString());
                    article.setName(sourceAsMap.get("name").toString());

                    articleList.add(article);
                }

                return new AggregatedPageImpl<T>((List<T>) articleList);
            }
        });

        List<Article> articles = articleAggregatedPage.getContent();
        articles.forEach(article -> System.out.println(article));

        return "查询成功了！";
    }

    @RequestMapping("/search")
    public String find03(@RequestParam("text")String text, ModelMap model){

        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(text)
                .defaultField("text")
                .analyzer("ik_smart");

        Pageable pageable = PageRequest.of(0,20);

        HighlightBuilder.Field field = new HighlightBuilder
                .Field("text")
                .preTags("<span><font color='#FF0000'>")
                .postTags("</font></span>");

        SearchQuery searchQuery=new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                . withHighlightFields(field)
                .build();

        //使用高亮显示
        AggregatedPage<Article> articleAggregatedPage = esTemplate.queryForPage(searchQuery, Article.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                List<Article> articleList = new ArrayList<>();
                SearchHits searchHits = searchResponse.getHits();
                if (searchHits.getHits().length<=0){
                    return null;
                }
                System.out.println(searchHits.getTotalHits()+"--------------");

                for (SearchHit searchHit:searchHits){

                    Article article=new Article();

                    Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

                    System.out.println(highlightFields);

                    //取content高亮显示的结果
                    HighlightField field = highlightFields.get("text");
                    Text[] fragments = field.getFragments();

                    StringBuilder sb=new StringBuilder();

                    for(Text t :fragments){
                        if(!t.equals("")){
                            sb.append(t.toString());
                        }
                    }
                    article.setText(sb.toString());
                    article.setId(sourceAsMap.get("id").toString());
                    article.setName(sourceAsMap.get("name").toString());

                    articleList.add(article);
                }

                return new AggregatedPageImpl<T>((List<T>) articleList);
            }
        });

        List<Article> articles = articleAggregatedPage.getContent();
        model.addAttribute("articlelist",articles);
        return "articles";
    }


}
