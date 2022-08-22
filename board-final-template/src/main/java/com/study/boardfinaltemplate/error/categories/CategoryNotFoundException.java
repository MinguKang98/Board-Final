package com.study.boardfinaltemplate.error.categories;

import com.study.boardfinaltemplate.error.common.Messages;
import com.study.boardfinaltemplate.error.common.ResourceNotFoundException;

/**
 * 존재하지 않는 Category 접근 시 throw
 */
public class CategoryNotFoundException extends ResourceNotFoundException {
    public CategoryNotFoundException() {
        super(Messages.NO_CATEGORY_MESSAGE);
    }
}
