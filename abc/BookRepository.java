package com.training.demoTraining.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.training.demoTraining.Model.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, String>{

}