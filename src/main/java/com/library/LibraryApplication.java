package com.library;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.library.config.HibernateUtil;

public class LibraryApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        HibernateUtil.getSessionFactory();

        TabPane tabPane = new TabPane();

        Tab studentTab = new Tab("Students", new StudentView());
        Tab bookTab = new Tab("Books", new BookView());
        Tab loanTab = new Tab("Loans", new LoanView());

        studentTab.setClosable(false);
        bookTab.setClosable(false);
        loanTab.setClosable(false);
        
        tabPane.getTabs().addAll(studentTab, bookTab, loanTab);
        
        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setTitle("Library Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    @Override
    public void stop() throws Exception {
        HibernateUtil.shutdown();
        super.stop();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}