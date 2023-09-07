package io.devridge.api.mock;

import io.devridge.api.util.time.TimeProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class FakeTimeProvider implements TimeProvider {
    public LocalDateTime currentTime;

    public FakeTimeProvider(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    @Override
    public Date convertToJavaUtilDate(LocalDateTime dateToConvert) {
        return null;
    }
}
