package com.igor.blognosql.author.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.igor.blognosql.author.entity.Author;
import com.igor.blognosql.author.repository.AuthorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {

  private final AuthorRepository repository;

  public List<Author> findAll() {
    return repository.findAll();
  }

  public Author findById(String authorId) {
    return repository
        .findById(authorId)
        .orElseThrow(() -> new IllegalArgumentException("Could not find author"));
  }

  public Author create(Author author) {
    return repository.save(author);
  }

}
