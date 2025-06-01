package com.test.services.excel;

import java.io.InputStream;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.test.exception.config.ServerSideException;
import com.test.exception.excel.ExtentionNotSupportedException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
@AllArgsConstructor
public enum ExcelType {

  XLS("xls"),

  XLSX("xlsx");

  private final String ext;

  public static Workbook createWorkbook(ExcelType ext) {
    switch (ext) {
      case XLS:
        return new HSSFWorkbook();
      case XLSX:
        return new XSSFWorkbook();
      default:
        throw new ExtentionNotSupportedException();
    }
  }

  @SneakyThrows
  public static Workbook loadWorkbook(InputStream in, ExcelType ext) {
    switch (ext) {
      case XLS:
        return new HSSFWorkbook(in);
      case XLSX:
        return new XSSFWorkbook(in);
      default:
        throw new ExtentionNotSupportedException();
    }

  }

  public static ExcelType of(String ext) {
    return Stream.of(values())
        .filter(e -> e.getExt().equals(ext))
        .findFirst()
        .orElseThrow(ExtentionNotSupportedException::new);

  }

  public static ExcelType of(Workbook workbook) {
    if (Objects.isNull(workbook)) {
      throw new ServerSideException("null workbook");
    }
    if (workbook instanceof XSSFWorkbook) {
      return XLSX;
    }
    if (workbook instanceof HSSFWorkbook) {
      return XLS;
    }
    throw new ServerSideException("unsupported workbook");
  }

}