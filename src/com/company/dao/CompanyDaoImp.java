package com.company.dao;

import com.company.domain.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImp implements CompanyDao {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    public CompanyDaoImp() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS company " +
                "(id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                " name VARCHAR(255) NOT NULL UNIQUE)";
        String sqlRestartId = "ALTER TABLE company ALTER COLUMN id RESTART WITH 1";

        try(Connection connection = daoFactory.getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlCreate);
            statement.executeUpdate(sqlRestartId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int insert(String companyName) {
        String sql = "INSERT INTO company(name) VALUES(?)";
        int id = -1;

        try(Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, companyName);
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Company with the same name already exists");
        }
        return id;
    }

    @Override
    public List<Company> getAll() {
        String sql = "SELECT * FROM company";
        List<Company> companies = new ArrayList<>();

        try(Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    companies.add(new Company(
                            resultSet.getInt("id"),
                            resultSet.getString("name")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public Company getById(int companyId) {
        String sql = "SELECT * FROM company WHERE id = ?";
        Company company = null;

        try(Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, companyId);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    company = new Company(
                            resultSet.getInt("id"),
                            resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return company;
    }
}
