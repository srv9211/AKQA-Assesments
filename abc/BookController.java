package com.training.demoTraining.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.training.demoTraining.Model.Book;
import com.training.demoTraining.service.BookService;

@RestController
public class BookController {
	
	@Autowired
	BookService bookService;
	
	@RequestMapping("/book")
	public List<Book> getAllBooks(){
		return bookService.getAllBooks();
	}
	@RequestMapping("/book/{id}")
	public Book getTopic(@PathVariable String  id) {
		return bookService.getBook(id);
	}
	@RequestMapping(method = RequestMethod.POST, value = "/book")
	public void addBook(@RequestBody Book book ) {
		bookService.addBook(book);
	}
	@RequestMapping(method = RequestMethod.PUT, value = "/book/{id}")
	public void updateBook(@RequestBody Book book ,@PathVariable String id ) {
		bookService.updateBook(book, id);
	}
	@RequestMapping(method = RequestMethod.DELETE, value = "/book/{id}")
	public void updateBook(@PathVariable String id ) {
		bookService.delete(id);
	}
}

