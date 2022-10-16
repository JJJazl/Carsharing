package com.company.dao;

import com.company.domain.Customer;

import java.util.List;

public interface CustomerDao {
    int insert(String customerName);
    Customer getById(int customerId);
    List<Customer> getAll();
    List<String> getCarAndCompanyName(int customerId);
    Customer updateRentedCarId(int customerId, String customerName, int rentedCarId);
    Customer updateRentedCarIdToNull(int customerId, String customerName);
}
