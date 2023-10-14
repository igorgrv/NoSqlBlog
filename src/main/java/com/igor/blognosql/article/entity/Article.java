package com.igor.blognosql.article.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.igor.blognosql.author.entity.Author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

@Data
@Document
public class Article {

  @Id
  private String id;

  @NotBlank(message = "Title is required")
  private String title;

  @NotBlank(message = "Text is required")
  private String text;

  @NotBlank(message = "url is required")
  private String url;

  @NotNull(message = "createdDate is mandatory")
  @DateTimeFormat(pattern = "YYYY-MM-dd")
  @Past
  private LocalDate createdDate;

  @NotNull(message = "Status is required")
  private Integer status;

  @DBRef
  private Author author;

  @Version
  private Long version;

}
