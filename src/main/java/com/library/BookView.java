package com.library;

import com.library.model.Book;
import com.library.config.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class BookView extends VBox {
    private TextField isbnField;
    private TextField titleField;
    private TextField authorsField;
    private TextField publisherField;
    private TextField publicationDateField;
    private ListView<Book> bookList;
    private ObservableList<Book> books;
    
    public BookView() {
        setPadding(new Insets(10));
        setSpacing(10);
        
        books = FXCollections.observableArrayList();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        form.add(new Label("ISBN:"), 0, 0);
        isbnField = new TextField();
        form.add(isbnField, 1, 0);
        
        form.add(new Label("Title:"), 0, 1);
        titleField = new TextField();
        form.add(titleField, 1, 1);
        
        form.add(new Label("Authors:"), 0, 2);
        authorsField = new TextField();
        form.add(authorsField, 1, 2);
        
        form.add(new Label("Publisher:"), 0, 3);
        publisherField = new TextField();
        form.add(publisherField, 1, 3);
        
        form.add(new Label("Publication Date:"), 0, 4);
        publicationDateField = new TextField();
        form.add(publicationDateField, 1, 4);

        HBox buttons = new HBox(10);
        Button addButton = new Button("Add Book");
        Button deleteButton = new Button("Delete Book");
        Button clearButton = new Button("Clear");
        
        addButton.setOnAction(e -> addBook());
        deleteButton.setOnAction(e -> deleteBook());
        
        buttons.getChildren().addAll(addButton, deleteButton, clearButton);

        bookList = new ListView<>(books);
        bookList.setPrefHeight(300);
        bookList.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    populateFields(newVal);
                }
            });
        
        getChildren().addAll(
            new Label("Book Management"),
            form,
            buttons,
            new Label("Books:"),
            bookList
        );
        
        loadBooks();
    }
    
    private void addBook() {
        try {
            Book book = new Book(
                isbnField.getText().trim(),
                titleField.getText().trim(),
                authorsField.getText().trim(),
                publisherField.getText().trim(),
                publicationDateField.getText().trim()
            );
            
            if (book.getIsbn().isEmpty() || book.getTitle().isEmpty()) {
                return;
            }
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.save(book);
            transaction.commit();
            session.close();
            
            loadBooks();
            
        } catch (Exception ignored) {
        }
    }
    
    private void deleteBook() {
        Book selected = bookList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(session.get(Book.class, selected.getId()));
            transaction.commit();
            session.close();
            
            loadBooks();
            
        } catch (Exception ignored) {
        }
    }
    
    private void loadBooks() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Book> bookList = session.createQuery("FROM Book", Book.class).list();
            books.clear();
            books.addAll(bookList);
            session.close();
        } catch (Exception ignored) {
        }
    }
    
    private void populateFields(Book book) {
        isbnField.setText(book.getIsbn());
        titleField.setText(book.getTitle());
        authorsField.setText(book.getAuthors());
        publisherField.setText(book.getPublisher());
        publicationDateField.setText(book.getPublicationDate());
    }
}