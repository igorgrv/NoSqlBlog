package com.igor.blognosql.article.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igor.blognosql.article.entity.Article;
import com.igor.blognosql.article.entity.ArticleStatusCount;
import com.igor.blognosql.article.repository.ArticleRepository;
import com.igor.blognosql.author.entity.Author;
import com.igor.blognosql.author.entity.AuthorArticleCount;
import com.igor.blognosql.author.service.AuthorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final AuthorService authorService;
  private final MongoTemplate mongoTemplate;

  @Transactional(readOnly = true)
  public List<Article> findAll() {
    return articleRepository.findAll();
  }

  public Article findById(String articleId) {
    return articleRepository
        .findById(articleId)
        .orElseThrow(() -> new IllegalArgumentException("Could not find article"));
  }

  public List<Article> findByDateGreaterThan(LocalDate date) {
    Query query = new Query(Criteria.where("createdDate").gt(date));
    return mongoTemplate.find(query, Article.class);
  }

  @Transactional
  public Article create(Article article) {
    if (article.getAuthor() != null) {
      Author author = authorService.findById(article.getAuthor().getId());
      article.setAuthor(author);
    } else {
      article.setAuthor(null);
    }
    return articleRepository.save(article);
  }

  public Article update(Article article) {
    try {
      return articleRepository.save(article);
    } catch (OptimisticLockingFailureException e) {
      Article articleUpdated = findById(article.getId());
      articleUpdated.setAuthor(article.getAuthor());
      articleUpdated.setCreatedDate(article.getCreatedDate());
      articleUpdated.setStatus(article.getStatus());
      articleUpdated.setVersion(articleUpdated.getVersion() + 1);

      return articleRepository.save(article);
    }
  }

  public void delete(String articleId) {
    Article article = findById(articleId);
    articleRepository.delete(article);
  }

  // Update using mongoTemplate
  public void updateArticleUrl(String id, Article article) {
    Query query = new Query(Criteria.where("id").is(id));
    Update update = new Update().set("url", article.getUrl());
    mongoTemplate.updateFirst(query, update, Article.class);
  }

  // Find using mongoTemplate
  public List<Article> findByStatusDateAndTitle(String status, LocalDate date, String title) {
    Criteria criteria = new Criteria();

    criteria.and("date").lte(date);
    if (status != null)
      criteria.and("status").is(status);
    if (title != null && !title.isEmpty())
      criteria.and("title").is(title);

    Query query = new Query(criteria);
    return mongoTemplate.find(query, Article.class);
  }

  // Find using @Query
  public List<Article> findByCreatedDateGreaterThanAndLessThan(LocalDate starts, LocalDate ends) {
    return articleRepository.findByCreatedDateGreaterThanAndLessThan(starts, ends);
  }

  // Find pageable
  public Page<Article> findAllPageble(Pageable pageable) {
    return articleRepository.findAll(pageable);
  }

  // Find using simple @Query with orderBy method
  public List<Article> findByStatusOrderByTitleAsc(String title) {
    return articleRepository.findByTitleWithQuery(title);
  }

  public List<ArticleStatusCount> getStatusCount() {
    TypedAggregation<Article> typedAggregation = Aggregation.newAggregation(
        Article.class,
        Aggregation.group("status").count().as("quantity"),
        Aggregation.project("quantity").and("status").previousOperation());

    AggregationResults<ArticleStatusCount> result = mongoTemplate.aggregate(typedAggregation, ArticleStatusCount.class);
    return result.getMappedResults();
  }

  public List<AuthorArticleCount> getArticlesGroupedByAuthorFrom(LocalDate startDate, LocalDate endDate) {
    Criteria criteria = Criteria.where("createdDate").gte(startDate.atStartOfDay())
        .lt(endDate.plusDays(1).atStartOfDay());
    AggregationOperation match = Aggregation.match(criteria);
    AggregationOperation group = Aggregation.group("author").count().as("totalArticles");
    AggregationOperation project = Aggregation.project("totalArticles").and("author").previousOperation();

    TypedAggregation<Article> typedAggregation = Aggregation.newAggregation(
        Article.class,
        match,
        group,
        project);

    AggregationResults<AuthorArticleCount> result = mongoTemplate.aggregate(typedAggregation, AuthorArticleCount.class);
    return result.getMappedResults();
  }

}
