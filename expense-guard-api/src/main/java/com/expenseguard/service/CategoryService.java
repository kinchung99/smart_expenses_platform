package com.expenseguard.service;

import com.expenseguard.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> findAll();
}
