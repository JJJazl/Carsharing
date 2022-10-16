package com.company.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/com/company/db/carsharing";
    private static final String USER = "sa";
    private static final String PASSWORD = "1234";
    private static DaoFactory instance;
    public DaoFactory() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static DaoFactory getInstance() {
        if (instance == null) {
            instance = new DaoFactory();
        }
        return instance;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public CompanyDao getCompanyDao() {
        return new CompanyDaoImp();
    }

    public CarDao getCarDao() {
        return new CarDaoImp();
    }

    public CustomerDao getCustomerDao() {
        return new CustomerDaoImp();
    }
}
