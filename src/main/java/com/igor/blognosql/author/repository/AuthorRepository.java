package com.igor.blognosql.author.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.igor.blognosql.author.entity.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {
  
}
