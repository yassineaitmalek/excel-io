package com.test.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExcelUtils {

  public InputStream loadTemplate(String templateFullName) throws IOException {
    ClassPathResource resource = new ClassPathResource("templates/" + templateFullName);
    return resource.getInputStream();
  }

  public String checkField(String field) {

    return Optional.ofNullable(field).map(String::trim).filter(e -> !e.equals(new String())).orElse(null);
  }

  public boolean checkEmptyString(String field) {
    return !Optional.ofNullable(field).map(String::trim).filter(e -> !e.equals(new String())).isPresent();
  }

  public boolean checkEmptyStringList(List<String> list) {

    return Utils.checkStream(list).allMatch(ExcelUtils::checkEmptyString);

  }

}
