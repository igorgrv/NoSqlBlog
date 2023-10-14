package com.igor.blognosql.author.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Author {
  
  @Id
  private String id;
  private String name;
  private String biography;
  private String image;

}
