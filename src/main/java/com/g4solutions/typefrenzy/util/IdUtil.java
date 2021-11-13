package com.g4solutions.typefrenzy.util;

import java.util.Locale;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdUtil {

  public static String generateId(Class<?> clazz) {
    return clazz.getSimpleName().toLowerCase(Locale.ROOT) + "-" + UUID.randomUUID();
  }

}
