package com.study.boardfinalback.apiController;

import com.study.boardfinalback.domain.Category;
import com.study.boardfinalback.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Category 관련 api 요청 처리하는 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    /**
     * 모든 Category를 가져온다.
     *
     * @return 200 With Category List
     */
    @GetMapping("/api/categories")
    public ResponseEntity<List<Category>> categories() {

        List<Category> categoryList = categoryService.getCategoryList();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    /**
     * 입력받은 categorySeq 가지는 Cateogry의 정보를 가져온다.
     *
     * @param categorySeq : 가져올 Category의 categorySeq
     * @return : 200 with Category
     */
    @GetMapping("/api/categories/{categorySeq}")
    public ResponseEntity<Category> categoryDetail(@PathVariable("categorySeq") int categorySeq) {

        Category category = categoryService.getCategoryBySeq(categorySeq);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

}
