package com.test.services.excel.components;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import lombok.Data;

@Data
public abstract class ComplexExcelWriter<T> implements ExcelSheetWriter<T> {

  private T page;

  private String sheetName;

  private AtomicInteger sheetIndex;

  public abstract void write(T entity, Workbook workbook, Sheet sheet);

  @Override
  public void write(Workbook workbook) {
    Sheet sheet = workbook.createSheet(sheetName);
    write(page, workbook, sheet);
    autoSize(sheet);
  }

  public void autoSize(Sheet sheet) {

    IntStream.range(0, sheet.getRow(0).getPhysicalNumberOfCells()).forEach(sheet::autoSizeColumn);

  }

}
