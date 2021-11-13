package com.g4solutions.typefrenzy.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ReflectionUtil {

  /**
   * Searches a field with fieldName and fieldType inside the clazz.
   *
   * @param clazz the class to search the field in
   * @param fieldName the name of the field to be searched
   * @param fieldType the type of the field
   * @return an Optional of the found field (empty if the field is not found)
   */
  public static Optional<Field> getField(Class<?> clazz, String fieldName, Class<?> fieldType) {
    Field field;

    try {
      field = clazz.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      try {
        field = clazz.getField(fieldName);
      } catch (NoSuchFieldException noSuchFieldException) {
        return Optional.empty();
      }
    }

    if (!fieldType.equals(field.getType())) {
      return Optional.empty();
    }

    field.setAccessible(true);

    return Optional.of(field);
  }

  /**
   * Searches a method with methodName and parameterTypes inside the clazz.
   *
   * @param clazz the class to search the method in
   * @param methodName the name of the method to be searched
   * @param parameterTypes the types of parameters of the method
   * @return an Optional of the found method (empty if the method is not found)
   */
  public static Optional<Method> getMethod(
      Class<?> clazz,
      String methodName,
      Class<?>... parameterTypes
  ) {
    try {
      return Optional.of(clazz.getDeclaredMethod(methodName, parameterTypes));
    } catch (NoSuchMethodException e) {
      try {
        return Optional.of(clazz.getMethod(methodName, parameterTypes));
      } catch (NoSuchMethodException ex) {
        return Optional.empty();
      }
    }
  }

  /**
   * Checks if all generic parameters (in generic super class and/or interfaces) of the given clazz
   * match the given genericParameters. <br/> <br/>
   *
   * It is checked if each generic parameter of the clazz is equal or assignable to the given
   * generic parameter from the genericParameters. <br/> <br/>
   *
   * Note: The order of the genericParameters must be the same as the order, in which the clazz
   * specifies its super class and/or interface generics <br/> <br/>
   *
   * Example: if there is a class <br/> <br/>
   *
   * <code> public class Foo extends Bar&lt;A&gt; implements IFoo&lt;B&gt;, IBar&lt;C&gt; </code>
   * <br/> <br/>
   *
   * then the order of genericParameters must be: A, B, C
   *
   * @param clazz the class to be checked
   * @param genericParameters the generic parameters the class to be checked for
   * @return true if the class matches the generics parameters, otherwise false
   */
  public static boolean genericParametersMatch(Class<?> clazz, Class<?>... genericParameters) {
    try {
      Type[] classGenericParameters = ReflectionUtil.resolveGenerics(clazz);

      if (classGenericParameters.length != genericParameters.length) {
        return false;
      }

      boolean allGenericsMatch = true;
      for (int i = 0; i < genericParameters.length; i++) {
        try {
          Class<?> currGenericClass = Class.forName(classGenericParameters[i].getTypeName());
          boolean currGenericMatches = currGenericClass.equals(genericParameters[i])
              || currGenericClass.isAssignableFrom(genericParameters[i]);
          allGenericsMatch = allGenericsMatch && currGenericMatches;
        } catch (ClassNotFoundException e) {
          return false;
        }
      }

      return allGenericsMatch;
    } catch (Exception ex) {
      return false;
    }
  }

  private static Type[] resolveGenerics(Class<?> clazz) {
    List<Type> classGenericParameters = new LinkedList<>();

    try {
      Type[] superClassGenerics = ((ParameterizedType) clazz
          .getGenericSuperclass())
          .getActualTypeArguments();

      classGenericParameters.addAll(Arrays.asList(superClassGenerics));
    } catch (ClassCastException ex) {
      ReflectionUtil.log.debug(
          "The class {} does not extend any generic abstract class",
          clazz.getName()
      );
    }

    Type[] genericInterfaces = clazz.getGenericInterfaces();
    for (Type genericInterface : genericInterfaces) {
      try {
        Type[] currInterfaceGenerics = ((ParameterizedType) genericInterface)
            .getActualTypeArguments();

        classGenericParameters.addAll(Arrays.asList(currInterfaceGenerics));
      } catch (ClassCastException ex) {
        ReflectionUtil.log.debug(
            "The interface {} does not have any generic parameters",
            genericInterface
        );
      }
    }

    Type[] types = new Type[classGenericParameters.size()];

    return classGenericParameters.toArray(types);
  }
}
