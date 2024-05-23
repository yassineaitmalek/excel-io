package com.test.services.excel.components;

import java.util.List;
import java.util.Optional;

public interface ComplexExcelParser<T> {

  public Optional<T> parse(List<List<String>> row);

}