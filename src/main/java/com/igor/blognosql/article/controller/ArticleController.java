package com.igor.blognosql.article.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.igor.blognosql.article.entity.Article;
import com.igor.blognosql.article.entity.ArticleStatusCount;
import com.igor.blognosql.article.service.ArticleService;
import com.igor.blognosql.author.entity.AuthorArticleCount;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/articles")
class ArticleController {

  @Autowired
  private ArticleService service;

  @GetMapping
  public ResponseEntity<List<Article>> getAll() {
    List<Article> items = new ArrayList<>();
    service.findAll().forEach(items::add);
    if (items.isEmpty())
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    return new ResponseEntity<>(items, HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Article> getById(@PathVariable("id") String articleId) {
    Article existingItemOptional = service.findById(articleId);
    return new ResponseEntity<>(existingItemOptional, HttpStatus.OK);
  }

  @GetMapping("findByDate")
  public ResponseEntity<List<Article>> getByDateGreaterThan(@RequestParam LocalDate date) {
    List<Article> articles = new ArrayList<>();
    service.findByDateGreaterThan(date).forEach(articles::add);
    if (articles.isEmpty())
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    return new ResponseEntity<>(articles, HttpStatus.OK);
  }

@PostMapping
public ResponseEntity<Article> create(@Valid @RequestBody Article article) {
  Article savedItem = service.create(article);
  return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
}

  @PutMapping
  public ResponseEntity<Article> update(@Valid  @RequestBody Article article) {
    Article updatedItem = service.update(article);
    return new ResponseEntity<>(updatedItem, HttpStatus.OK);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Article> delete(@PathVariable("id") String articleId) {
    service.delete(articleId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("period")
  public ResponseEntity<List<Article>> getByDateGreaterThanAndLessThan(@RequestParam LocalDate starts,
      @RequestParam LocalDate ends) {
    List<Article> articles = new ArrayList<>();
    service.findByCreatedDateGreaterThanAndLessThan(starts, ends).forEach(articles::add);
    if (articles.isEmpty())
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    return new ResponseEntity<>(articles, HttpStatus.OK);
  }

  @GetMapping("pageable")
  public ResponseEntity<Page<Article>> getAll(Pageable pageable) {
    Sort sort = Sort.by("titles").ascending();
    Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    Page<Article> articles = service.findAllPageble(pageableSorted);
    return new ResponseEntity<>(articles, HttpStatus.OK);
  }

  @GetMapping("title-ordered")
  public ResponseEntity<List<Article>> getByStatusOrderByTitleUsingQuery(@RequestParam String title) {
    List<Article> articles = new ArrayList<>();
    service.findByStatusOrderByTitleAsc(title).forEach(articles::add);
    return new ResponseEntity<>(articles, HttpStatus.OK);
  }

  @GetMapping("status-grouped")
  public ResponseEntity<List<ArticleStatusCount>> getStatusCount() {
    List<ArticleStatusCount> articles = new ArrayList<>();
    service.getStatusCount().forEach(articles::add);
    return new ResponseEntity<>(articles, HttpStatus.OK);
  }

  @GetMapping("author-grouped")
  public ResponseEntity<List<AuthorArticleCount>> getAuthorArticleCount(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
    List<AuthorArticleCount> authorArticleCount = new ArrayList<>();
    service.getArticlesGroupedByAuthorFrom(startDate, endDate).forEach(authorArticleCount::add);
    return new ResponseEntity<>(authorArticleCount, HttpStatus.OK);
  }
}