package com.training.demoTraining.Model;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Book {
	@Id
	private String bookId;
	private String name;
	private String description;
	public String getBookid() {
		return bookId;
	}
	public void setBookid(String bookid) {
		this.bookId = bookid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Book() {
		super();
	}
	public Book(String bookid, String name, String description) {
		super();
		this.bookId = bookid;
		this.name = name;
		this.description = description;
	}
}
