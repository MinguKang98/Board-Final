package com.study.boardfinaltemplate.error.files;

import com.study.boardfinaltemplate.error.common.Messages;
import com.study.boardfinaltemplate.error.common.ResourceNotFoundException;

/**
 * 존재하지 않는 File 접근 시 throw
 */
public class FileNotFoundException extends ResourceNotFoundException {
    public FileNotFoundException() {
        super(Messages.NO_FILE_MESSAGE);
    }
}
