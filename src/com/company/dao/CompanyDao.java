package com.company.dao;

import com.company.domain.Company;

import java.util.List;

public interface CompanyDao {
    int insert(String companyName);
    Company getById(int companyId);
    List<Company> getAll();
}
