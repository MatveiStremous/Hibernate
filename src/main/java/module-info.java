module com.example.hibernatelab6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;


    opens com.example.hibernate to javafx.fxml;
    exports com.example.hibernate;
    exports com.example.hibernate.entity;
}