package com.study.boardfinalback.error;

public class CategoryNotFoundException extends BusinessException {
    public CategoryNotFoundException() {
        super(Messages.NO_CATEGORY_MESSAGE);
    }
}
