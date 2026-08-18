package com.expenseguard.service.impl;

import com.expenseguard.dto.CategoryResponse;
import com.expenseguard.repository.CategoryRepository;
import com.expenseguard.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryResponse(
                        category.getId(), category.getName(), category.getType()))
                .toList();
    }
}
