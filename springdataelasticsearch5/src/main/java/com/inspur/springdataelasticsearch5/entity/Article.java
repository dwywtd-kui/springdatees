package com.inspur.springdataelasticsearch5.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "file_index",type = "artcle",shards = 5,replicas = 2)
public class Article {
    @Id
    @Field(store = true,type = FieldType.Keyword)
    private String id;
    @Field(store = true,type = FieldType.Text,analyzer = "ik_max_word")
    private String name;
    @Field(store = true,type = FieldType.Text,analyzer = "ik_max_word")
    private String text;
    public Article() {
    }

    public Article(String id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
