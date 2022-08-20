package com.study.boardfinalback.error.categories;

import com.study.boardfinalback.error.common.Messages;
import com.study.boardfinalback.error.common.ResourceNotFoundException;

public class CategoryNotFoundException extends ResourceNotFoundException {
    public CategoryNotFoundException() {
        super(Messages.NO_CATEGORY_MESSAGE);
    }
}
