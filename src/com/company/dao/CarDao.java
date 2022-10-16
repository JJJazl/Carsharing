package com.company.dao;

import com.company.domain.Car;

import java.util.List;

public interface CarDao {
    int insert(String carName, int companyId);
    List<Car> getAllById(int companyId);
    List<Car> getAllFreeById(int companyId);
}
