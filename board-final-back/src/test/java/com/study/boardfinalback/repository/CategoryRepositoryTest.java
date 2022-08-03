package com.study.boardfinalback.repository;

import com.study.boardfinalback.domain.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void 카테고리_모두_가져오기() throws Exception {
        //when
        List<Category> categoryList = categoryRepository.getCategoryList();

        //then
        Assertions.assertThat(categoryList.size()).isEqualTo(4);
    }

    @Test
    public void 카테고리_가져오기_정상() throws Exception {
        //when
        Optional<Category> category1 = categoryRepository.getCategoryBySeq(1);
        Optional<Category> category2 = categoryRepository.getCategoryBySeq(2);
        Optional<Category> category3 = categoryRepository.getCategoryBySeq(3);
        Optional<Category> category4 = categoryRepository.getCategoryBySeq(4);

        //then
        Assertions.assertThat(category1.get().getName()).isEqualTo("공지사항");
        Assertions.assertThat(category2.get().getName()).isEqualTo("자유게시판");
        Assertions.assertThat(category3.get().getName()).isEqualTo("회원전용 게시판");
        Assertions.assertThat(category4.get().getName()).isEqualTo("뉴스");
    }

    @Test
    public void 카테고리_가져오기_오류() throws Exception {
        ///when
        Optional<Category> category = categoryRepository.getCategoryBySeq(13);

        //then
        Assertions.assertThat(category.isEmpty()).isTrue();
    }

}