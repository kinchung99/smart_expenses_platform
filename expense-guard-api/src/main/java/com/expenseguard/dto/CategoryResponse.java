package com.expenseguard.dto;

import com.expenseguard.entity.CategoryType;

public record CategoryResponse(
        Long id,
        String name,
        CategoryType type) {
}
