package com.library;

import com.library.model.Loan;
import com.library.config.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class LoanView extends VBox {
    private TextField studentBroncoIdField;
    private TextField bookIsbnField;
    private TextField durationField;
    private ListView<Loan> loanList;
    private ObservableList<Loan> loans;
    private TextArea receiptArea;
    
    public LoanView() {
        setPadding(new Insets(10));
        setSpacing(10);
        
        loans = FXCollections.observableArrayList();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        form.add(new Label("Student Bronco ID:"), 0, 0);
        studentBroncoIdField = new TextField();
        form.add(studentBroncoIdField, 1, 0);
        
        form.add(new Label("Book ISBN:"), 0, 1);
        bookIsbnField = new TextField();
        form.add(bookIsbnField, 1, 1);
        
        form.add(new Label("Duration (days):"), 0, 2);
        durationField = new TextField("14");
        form.add(durationField, 1, 2);

        HBox buttons = new HBox(10);
        Button createLoanButton = new Button("Create Loan");
        Button returnLoanButton = new Button("Return Loan");
        Button generateReceiptButton = new Button("Generate Receipt");
        Button clearButton = new Button("Clear");
        
        createLoanButton.setOnAction(e -> createLoan());
        returnLoanButton.setOnAction(e -> returnLoan());
        generateReceiptButton.setOnAction(e -> generateReceipt());
        
        buttons.getChildren().addAll(createLoanButton, returnLoanButton, generateReceiptButton, clearButton);

        loanList = new ListView<>(loans);
        loanList.setPrefHeight(200);

        receiptArea = new TextArea();
        receiptArea.setPrefHeight(150);
        receiptArea.setEditable(false);
        
        getChildren().addAll(
            new Label("Loan Management"),
            form,
            buttons,
            new Label("Active Loans:"),
            loanList,
            new Label("Receipt:"),
            receiptArea
        );
        
        loadLoans();
    }
    
    private void createLoan() {
        try {
            String studentBroncoId = studentBroncoIdField.getText().trim();
            String bookIsbn = bookIsbnField.getText().trim();
            String durationText = durationField.getText().trim();
            
            if (studentBroncoId.isEmpty() || bookIsbn.isEmpty() || durationText.isEmpty()) {
                return;
            }
            
            int duration = Integer.parseInt(durationText);
            if (duration <= 0 || duration > 180) {
                return;
            }

            Session session = HibernateUtil.getSessionFactory().openSession();
            Long studentCount = (Long) session.createQuery(
                "SELECT COUNT(*) FROM Student WHERE broncoId = :broncoId")
                .setParameter("broncoId", studentBroncoId)
                .uniqueResult();
            
            if (studentCount == 0) {
                session.close();
                return;
            }

            Long bookCount = (Long) session.createQuery(
                "SELECT COUNT(*) FROM Book WHERE isbn = :isbn")
                .setParameter("isbn", bookIsbn)
                .uniqueResult();
            
            if (bookCount == 0) {
                session.close();
                return;
            }

            Long activeLoanCount = (Long) session.createQuery(
                "SELECT COUNT(*) FROM Loan WHERE studentBroncoId = :broncoId AND status = 'ACTIVE'")
                .setParameter("broncoId", studentBroncoId)
                .uniqueResult();
            
            if (activeLoanCount >= 5) {
                session.close();
                return;
            }

            Loan loan = new Loan(
                "LN" + System.currentTimeMillis(),
                studentBroncoId,
                bookIsbn,
                LocalDate.now(),
                LocalDate.now().plusDays(duration)
            );
            
            Transaction transaction = session.beginTransaction();
            session.save(loan);
            transaction.commit();
            session.close();
            
            loadLoans();
            
        } catch (Exception ignored) {
        }
    }
    
    private void returnLoan() {
        Loan selected = loanList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        
        if (!"ACTIVE".equals(selected.getStatus())) {
            return;
        }
        
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            
            Loan loan = session.get(Loan.class, selected.getId());
            loan.setReturnDate(LocalDate.now());
            loan.setStatus("RETURNED");
            
            session.update(loan);
            transaction.commit();
            session.close();
            
            loadLoans();

            
        } catch (Exception ignored) {

        }
    }
    
    private void generateReceipt() {
        Loan selected = loanList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        
        StringBuilder receipt = new StringBuilder();
        receipt.append("=== LOAN RECEIPT ===\n");
        receipt.append("Loan Number: ").append(selected.getLoanNumber()).append("\n");
        receipt.append("Student Bronco ID: ").append(selected.getStudentBroncoId()).append("\n");
        receipt.append("Book ISBN: ").append(selected.getBookIsbn()).append("\n");
        receipt.append("Borrowing Date: ").append(selected.getBorrowingDate()).append("\n");
        receipt.append("Due Date: ").append(selected.getDueDate()).append("\n");
        receipt.append("Status: ").append(selected.getStatus()).append("\n");
        if (selected.getReturnDate() != null) {
            receipt.append("Return Date: ").append(selected.getReturnDate()).append("\n");
        }
        receipt.append("===================\n");
        
        receiptArea.setText(receipt.toString());
    }
    
    private void loadLoans() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Loan> loanList = session.createQuery("FROM Loan ORDER BY borrowingDate DESC", Loan.class).list();
            loans.clear();
            loans.addAll(loanList);
            session.close();
        } catch (Exception ignored) {

        }
    }
}