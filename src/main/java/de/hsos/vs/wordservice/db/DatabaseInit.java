//package de.hsos.vs.wordservice.db;
//
//import jakarta.servlet.ServletContextEvent;
//import jakarta.servlet.ServletContextListener;
//import jakarta.servlet.annotation.WebListener;
//
//import java.sql.SQLException;
//
//@WebListener
//public class DatabaseInit implements ServletContextListener {
//
//    @Override
//    public void contextInitialized(ServletContextEvent event) {
//        try {
//            Database.init();
//        } catch (SQLException exception) {
//            throw new RuntimeException(exception);
//        }
//    }
//}
