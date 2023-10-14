package com.igor.blognosql.article.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.igor.blognosql.article.entity.Article;

public interface ArticleRepository extends MongoRepository<Article, String> {

  @Query("{ $and: [ { 'createdDate': {$gte: ?0}}, { 'createdDate': {$lte: ?1} } ]}")
  List<Article> findByCreatedDateGreaterThanAndLessThan(LocalDate stars, LocalDate ends);

  List<Article> findByStatusOrderByTitleAsc(String title);

  @Query(value = "{ 'title': /?0/i }", sort = "{ 'title' : 1 }")
  List<Article> findByTitleWithQuery(String title);
}
