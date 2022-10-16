package com.company.dao;

import com.company.domain.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImp implements CarDao {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    public CarDaoImp() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS car " +
                "(id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                " name VARCHAR(255) NOT NULL UNIQUE, " +
                " company_id INTEGER NOT NULL, " +
                " CONSTRAINT fk_car_company FOREIGN KEY(company_id) REFERENCES company(id))";
        String sqlRestartId = "ALTER TABLE car ALTER COLUMN id RESTART WITH 1";

        try(Connection connection = daoFactory.getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlCreate);
            statement.executeUpdate(sqlRestartId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int insert(String carName, int companyId) {
        String sql = "INSERT INTO car(name, company_id) VALUES(?, ?)";
        int id = -1;

        try(Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, carName);
            statement.setInt(2, companyId);
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Car with the same name already exists");
        }
        return id;
    }

    @Override
    public List<Car> getAllById(int companyId) {
        String sql = "SELECT * FROM car WHERE company_id = ?";
        List<Car> cars = new ArrayList<>();

        try(Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, companyId);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cars.add(new Car(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("company_id")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public List<Car> getAllFreeById(int companyId) {
        String sql = "SELECT car.id, car.name, car.company_id " +
                "FROM car " +
                "LEFT JOIN customer " +
                "ON car.id = customer.rented_car_id " +
                "WHERE car.company_id = ? AND customer.name IS NULL";
        List<Car> cars = new ArrayList<>();

        try(Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, companyId);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cars.add(new Car(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("company_id")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
}
