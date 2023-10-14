package com.igor.blognosql.article.entity;

import lombok.Data;

@Data
public class ArticleStatusCount {
  
  private String status;
  private int quantity;
}
