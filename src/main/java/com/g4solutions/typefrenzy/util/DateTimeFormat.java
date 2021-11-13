package com.g4solutions.typefrenzy.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeFormat {

  public static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SS'Z'";

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
      DateTimeFormat.PATTERN
  );

  public static LocalDateTime parse(String dateString) {
    return LocalDateTime.parse(dateString, DateTimeFormat.FORMATTER);
  }

  public static String toString(LocalDateTime dateTime) {
    return DateTimeFormat.FORMATTER.format(dateTime);
  }

}
