package com.study.boardfinalback.error.files;

import com.study.boardfinalback.error.common.Messages;
import com.study.boardfinalback.error.common.ResourceNotFoundException;

public class FileNotFoundException extends ResourceNotFoundException {
    public FileNotFoundException() {
        super(Messages.NO_FILE_MESSAGE);
    }
}
