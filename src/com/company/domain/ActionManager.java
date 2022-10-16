package com.company.domain;

import java.util.List;

public interface ActionManager {
    int createCompany(String companyName);
    int createCustomer(String customerName);
    int createCar(String carName, int companyId);
    Customer rentACar(int customerId, String customerName, int rentedCarId);
    Customer returnRentedCar(int customerId, String customerName);
    List<String> myRentedCar(int customerId);

    List<Company> getAllCompanies();
    List<Customer> getAllCustomers();
    List<Car> getAllCarsByCompanyId(int companyId);
    List<Car> getAllFreeCarsByCompanyId(int companyId);

    Company getCompanyById(int companyId);
    Customer getCustomerById(int customerId);

}
