package com.company.dao;

import com.company.domain.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImp implements CustomerDao {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    public CustomerDaoImp() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS customer " +
                "(id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                " name VARCHAR(255) NOT NULL UNIQUE, " +
                " rented_car_id INTEGER, " +
                " CONSTRAINT fk_customer_car FOREIGN KEY(rented_car_id) REFERENCES car(id))";
        String sqlRestartId = "ALTER TABLE customer ALTER COLUMN id RESTART WITH 1";

        try(Connection connection = daoFactory.getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlCreate);
            statement.executeUpdate(sqlRestartId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int insert(String customerName) {
        String sql = "INSERT INTO customer(name) VALUES(?)";
        int id = -1;

        try(Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customerName);
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Customer with the same name already exists");
        }
        return id;
    }

    @Override
    public Customer getById(int customerId) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        Customer customer = null;

        try(Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    customer = new Customer(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("rented_car_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public List<Customer> getAll() {
        String sql = "SELECT * FROM customer";
        List<Customer> customers = new ArrayList<>();

        try(Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    customers.add(new Customer(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("rented_car_id")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public List<String> getCarAndCompanyName(int customerId) {
        String sql = "SELECT car.name, company.name " +
                "FROM company JOIN car " +
                "ON company.id = car.company_id " +
                "JOIN customer " +
                "ON car.id = customer.rented_car_id " +
                "WHERE customer.id = ?";
        List<String> names = null;
        try(Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    names = new ArrayList<>();
                    names.add(resultSet.getString(1));
                    names.add(resultSet.getString(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }
    @Override
    public Customer updateRentedCarId(int customerId, String customerName, int rentedCarId) {
        String sql = "UPDATE customer SET rented_car_id = ? WHERE id = ?";
        Customer customer = null;

        try(Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, rentedCarId);
            statement.setInt(2, customerId);
            statement.executeUpdate();
            customer = new Customer(
                    customerId,
                    customerName,
                    rentedCarId
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public Customer updateRentedCarIdToNull(int customerId, String customerName) {
        String sql = "UPDATE customer SET rented_car_id = NULL WHERE id = ?";
        Customer customer = null;

        try(Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
            customer = new Customer(
                    customerId,
                    customerName,
                    0
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
}
