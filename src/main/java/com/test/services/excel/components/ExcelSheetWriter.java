package com.test.services.excel.components;

import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelSheetWriter<T> {

  public void write(Workbook workbook);

}
