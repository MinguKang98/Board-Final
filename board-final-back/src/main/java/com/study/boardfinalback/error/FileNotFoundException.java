package com.study.boardfinalback.error;

public class FileNotFoundException extends BusinessException {
    public FileNotFoundException() {
        super(Messages.NO_FILE_MESSAGE);
    }
}
