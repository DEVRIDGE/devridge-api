package io.devridge.api.util.time;

import java.time.LocalDateTime;
import java.util.Date;

public interface TimeProvider {
    LocalDateTime getCurrentTime();

    Date convertToJavaUtilDate(LocalDateTime dateToConvert);
}
