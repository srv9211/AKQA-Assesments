package com.training.demoTraining.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.demoTraining.Model.Book;
import com.training.demoTraining.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	BookRepository bookRepository;

	public List<Book> BookList =new ArrayList(Arrays.asList(new Book("2","Harry Potter", "Hogwarts"), 
			new Book("5","Ghost", "Scary"),new Book("10","Tumbadd", "Thriller")));

	public List<Book> getAllBooks(){
//		return BookList;
		List<Book> BookDataList = new ArrayList();
		bookRepository.findAll().forEach(Book -> BookDataList.add(Book));
		return BookDataList;
	}

	public Book getBook(String bookId) {
//		return BookList.stream().filter(data -> data.getBookid().equals(bookId)).findFirst().get();
		return bookRepository.findById(bookId).get();
	}

	public void addBook(Book Book) {
//		BookList.add(Book);
		bookRepository.save(Book);
	}

	public void updateBook(Book Book, String bookId) {

//		for(int i = 0; i<BookList.size();i++) {
//			if(BookList.get(i).getBookid().equals(bookId)) {
//				BookList.set(i, Book);
//			}
//		}
		bookRepository.save(Book);
	}

	public void delete(String bookId) {
//		BookList.removeIf(data -> data.getBookid().equals(bookId));
		bookRepository.deleteById(bookId);;
	}

}
