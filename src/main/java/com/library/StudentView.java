package com.library;

import com.library.model.Student;
import com.library.config.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class StudentView extends VBox {
    private TextField broncoIdField;
    private TextField nameField;
    private TextField addressField;
    private TextField degreeField;
    private ListView<Student> studentList;
    private ObservableList<Student> students;
    
    public StudentView() {
        setPadding(new Insets(10));
        setSpacing(10);
        
        students = FXCollections.observableArrayList();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        form.add(new Label("Bronco ID:"), 0, 0);
        broncoIdField = new TextField();
        form.add(broncoIdField, 1, 0);
        
        form.add(new Label("Name:"), 0, 1);
        nameField = new TextField();
        form.add(nameField, 1, 1);
        
        form.add(new Label("Address:"), 0, 2);
        addressField = new TextField();
        form.add(addressField, 1, 2);
        
        form.add(new Label("Degree:"), 0, 3);
        degreeField = new TextField();
        form.add(degreeField, 1, 3);

        HBox buttons = new HBox(10);
        Button addButton = new Button("Add Student");
        Button deleteButton = new Button("Delete Student");
        
        addButton.setOnAction(e -> addStudent());
        deleteButton.setOnAction(e -> deleteStudent());
        
        buttons.getChildren().addAll(addButton, deleteButton);

        studentList = new ListView<>(students);
        studentList.setPrefHeight(300);
        studentList.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    populateFields(newVal);
                }
            });
        
        getChildren().addAll(
            new Label("Student Management"),
            form,
            buttons,
            new Label("Students:"),
            studentList
        );
        
        loadStudents();
    }
    
    private void addStudent() {
        try {
            Student student = new Student(
                broncoIdField.getText().trim(),
                nameField.getText().trim(),
                addressField.getText().trim(),
                degreeField.getText().trim()
            );
            
            if (student.getBroncoId().isEmpty() || student.getName().isEmpty()) {
                return;
            }
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.save(student);
            transaction.commit();
            session.close();
            
            loadStudents();
            
        } catch (Exception ignored) {
        }
    }
    
    private void deleteStudent() {
        Student selected = studentList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(session.get(Student.class, selected.getId()));
            transaction.commit();
            session.close();
            
            loadStudents();
            
        } catch (Exception ignored) {
        }
    }
    
    private void loadStudents() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Student> studentList = session.createQuery("FROM Student", Student.class).list();
            students.clear();
            students.addAll(studentList);
            session.close();
        } catch (Exception ignored) {
        }
    }
    
    private void populateFields(Student student) {
        broncoIdField.setText(student.getBroncoId());
        nameField.setText(student.getName());
        addressField.setText(student.getAddress());
        degreeField.setText(student.getDegree());
    }
}