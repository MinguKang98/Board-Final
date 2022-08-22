package com.study.boardfinaltemplate.service;

import com.study.boardfinaltemplate.domain.Category;
import com.study.boardfinaltemplate.error.categories.CategoryNotFoundException;
import com.study.boardfinaltemplate.repository.CategoryRepository;
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
