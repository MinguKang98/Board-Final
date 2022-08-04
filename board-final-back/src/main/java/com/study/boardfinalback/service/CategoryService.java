package com.study.boardfinalback.service;

import com.study.boardfinalback.domain.Category;
import com.study.boardfinalback.error.CategoryNotFoundException;
import com.study.boardfinalback.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategoryList() {

        List<Category> categoryList = categoryRepository.getCategoryList();
        return categoryList;
    }

    public Category getCategoryBySeq(int categorySeq) {

        Category category = categoryRepository.getCategoryBySeq(categorySeq)
                .orElseThrow(CategoryNotFoundException::new);
        return category;
    }

}
