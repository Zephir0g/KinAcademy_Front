package com.Zephir0g.kinacademy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
public class CategoryDTO {
    private String name;
    private String label;
    private List<CategoryDTO> items;

    public CategoryDTO(String name, String label) {
        this.name = name;
        this.label = label;
        this.items = new ArrayList<CategoryDTO>();
    }

    public CategoryDTO() {
        this.items = new ArrayList<CategoryDTO>();
    }
}

