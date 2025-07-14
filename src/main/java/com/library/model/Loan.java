package com.library.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "loan_number", unique = true, nullable = false)
    private String loanNumber;
    
    @Column(name = "student_bronco_id", nullable = false)
    private String studentBroncoId;
    
    @Column(name = "book_isbn", nullable = false)
    private String bookIsbn;
    
    @Column(name = "borrowing_date", nullable = false)
    private LocalDate borrowingDate;
    
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    
    @Column(name = "return_date")
    private LocalDate returnDate;
    
    @Column(name = "status")
    private String status = "ACTIVE";
    
    public Loan() {}
    
    public Loan(String loanNumber, String studentBroncoId, String bookIsbn, 
                LocalDate borrowingDate, LocalDate dueDate) {
        this.loanNumber = loanNumber;
        this.studentBroncoId = studentBroncoId;
        this.bookIsbn = bookIsbn;
        this.borrowingDate = borrowingDate;
        this.dueDate = dueDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getLoanNumber() { return loanNumber; }
    public void setLoanNumber(String loanNumber) { this.loanNumber = loanNumber; }
    
    public String getStudentBroncoId() { return studentBroncoId; }
    public void setStudentBroncoId(String studentBroncoId) { this.studentBroncoId = studentBroncoId; }
    
    public String getBookIsbn() { return bookIsbn; }
    public void setBookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; }
    
    public LocalDate getBorrowingDate() { return borrowingDate; }
    public void setBorrowingDate(LocalDate borrowingDate) { this.borrowingDate = borrowingDate; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return loanNumber + " - " + studentBroncoId + " - " + bookIsbn;
    }
}