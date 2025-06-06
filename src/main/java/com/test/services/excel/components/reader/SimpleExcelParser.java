package com.test.services.excel.components.reader;

import java.util.List;
import java.util.Optional;

public interface SimpleExcelParser<T> {

  public Optional<T> parse(List<String> row);

}
