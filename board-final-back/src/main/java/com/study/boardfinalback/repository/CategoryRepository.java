package com.study.boardfinalback.repository;

import com.study.boardfinalback.domain.Category;
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
