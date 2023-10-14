package com.igor.blognosql.author.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.igor.blognosql.author.entity.Author;
import com.igor.blognosql.author.service.AuthorService;

@RestController
@RequestMapping("/authors")
class AuthorController {

  @Autowired
  private AuthorService service;

  @GetMapping
  public ResponseEntity<List<Author>> getAll() {
    List<Author> items = new ArrayList<>();
    service.findAll().forEach(items::add);
    if (items.isEmpty())
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    return new ResponseEntity<>(items, HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Author> getById(@PathVariable("id") String authorId) {
    Author existingItemOptional = service.findById(authorId);
    return new ResponseEntity<>(existingItemOptional, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Author> create(@RequestBody Author author) {
    Author savedItem = service.create(author);
    return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
  }

}