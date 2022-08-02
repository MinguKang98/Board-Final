package com.study.boardfinalback.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

/**
 * 게시글의 카테고리 클래스
 */
@Getter
@Alias(value = "Category")
@NoArgsConstructor
public class Category {

    int categorySeq;
    String name;

}
