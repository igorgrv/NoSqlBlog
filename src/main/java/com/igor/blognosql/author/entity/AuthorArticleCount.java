package com.igor.blognosql.author.entity;

import lombok.Data;

@Data
public class AuthorArticleCount {
  private Author author;
  private int totalArticles;
}
