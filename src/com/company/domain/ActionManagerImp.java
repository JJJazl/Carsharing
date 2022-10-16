package com.company.domain;

import com.company.dao.CarDao;
import com.company.dao.CompanyDao;
import com.company.dao.CustomerDao;
import com.company.dao.DaoFactory;

import java.util.List;

public class ActionManagerImp implements ActionManager {
    private final DaoFactory daoFactory;
    private final CompanyDao companyDao;
    private final CustomerDao customerDao;
    private final CarDao carDao;

    public ActionManagerImp() {
        daoFactory = DaoFactory.getInstance();
        companyDao = daoFactory.getCompanyDao();
        carDao = daoFactory.getCarDao();
        customerDao = daoFactory.getCustomerDao();
    }

    @Override
    public int createCompany(String companyName) {
        return companyDao.insert(companyName);
    }

    @Override
    public int createCustomer(String customerName) {
        return customerDao.insert(customerName);
    }

    @Override
    public int createCar(String carName, int companyId) {
        return carDao.insert(carName, companyId);
    }


    @Override
    public Customer rentACar(int customerId, String customerName, int rentedCarId) {
        return customerDao.updateRentedCarId(customerId, customerName, rentedCarId);
    }

    @Override
    public Customer returnRentedCar(int customerId, String customerName) {
        return customerDao.updateRentedCarIdToNull(customerId, customerName);
    }

    @Override
    public List<String> myRentedCar(int customerId) {
        return customerDao.getCarAndCompanyName(customerId);
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyDao.getAll();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.getAll();
    }

    @Override
    public List<Car> getAllCarsByCompanyId(int companyId) {
        return carDao.getAllById(companyId);
    }

    @Override
    public List<Car> getAllFreeCarsByCompanyId(int companyId) {
        return carDao.getAllFreeById(companyId);
    }

    @Override
    public Company getCompanyById(int companyId) {
        return companyDao.getById(companyId);
    }

    @Override
    public Customer getCustomerById(int customerId) {
        return customerDao.getById(customerId);
    }

}
