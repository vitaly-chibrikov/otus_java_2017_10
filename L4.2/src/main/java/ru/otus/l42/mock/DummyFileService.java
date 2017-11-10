package ru.otus.l42.mock;

import java.io.File;

/**
 *
 */
public class DummyFileService implements FileService {
    @Override
    public String readAllLines(File file) {
        throw new UnsupportedOperationException();
    }
}

class FakeFileService implements FileService {
    @Override
    public String readAllLines(File file) {
        return "aaa";
    }
}

class StubFileService implements FileService {
    @Override
    public String readAllLines(File file) {
        return "user1=qwerty\nuser2=1234\nuser3=1111";
    }
}
