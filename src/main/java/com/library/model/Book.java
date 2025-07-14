package com.library.model;

import javax.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "isbn", unique = true, nullable = false)
    private String isbn;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "authors")
    private String authors;
    
    @Column(name = "publisher")
    private String publisher;
    
    @Column(name = "publication_date")
    private String publicationDate;
    
    public Book() {}
    
    public Book(String isbn, String title, String authors, String publisher, String publicationDate) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }
    
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    
    public String getPublicationDate() { return publicationDate; }
    public void setPublicationDate(String publicationDate) { this.publicationDate = publicationDate; }
    
    @Override
    public String toString() {
        return title + " (" + isbn + ")";
    }
}