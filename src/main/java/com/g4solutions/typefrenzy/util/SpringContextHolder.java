package com.g4solutions.typefrenzy.util;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(value = false)
public class SpringContextHolder implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringContextHolder.applicationContext = applicationContext;
  }

  public static <T> T getBean(Class<T> beanType) {
    return SpringContextHolder.applicationContext.getBean(beanType);
  }

  public static <T> Optional<T> getBeanByName(Class<T> beanType, String beanName) {
    try {
      return Optional.of(SpringContextHolder.applicationContext.getBean(beanName, beanType));
    } catch (NoSuchBeanDefinitionException ex) {
      return Optional.empty();
    }
  }

  public static <T> List<T> getBeansByGenericParameters(
      Class<T> beanType,
      Class<?>... genericParameters
  ) {
    return Arrays
        .stream(SpringContextHolder.applicationContext.getBeanNamesForType(beanType))
        .map(currBeanName -> SpringContextHolder.applicationContext.getBean(currBeanName, beanType))
        .filter(currBean -> ReflectionUtil
            .genericParametersMatch(currBean.getClass(), genericParameters)
        )
        .collect(Collectors.toList());
  }

  public static <T> Optional<T> getBeanByGenericParameters(
      Class<T> beanType,
      Class<?>... genericParameters
  ) {
    List<T> foundBeans = SpringContextHolder.getBeansByGenericParameters(
        beanType, genericParameters
    );

    if (foundBeans.size() == 1) {
      return Optional.of(foundBeans.get(0));
    } else if (foundBeans.size() > 1) {
      throw new IllegalStateException(
          "More than one bean of type "
              + beanType.getName()
              + " with generic parameters: ["
              + Stream.of(genericParameters).map(Class::getName).collect(Collectors.joining(", "))
              + "] found"
      );
    }

    return Optional.empty();
  }

}
