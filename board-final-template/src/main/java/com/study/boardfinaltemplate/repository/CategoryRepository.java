package com.study.boardfinaltemplate.repository;

import com.study.boardfinaltemplate.domain.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CategoryRepository {

    List<Category> getCategoryList();

    Optional<Category> getCategoryBySeq(int categorySeq);

}
