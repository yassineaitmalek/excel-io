package com.test.services.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.test.controllers.config.ApiDownloadInput;
import com.test.exception.config.ServerSideException;
import com.test.services.excel.components.ExcelSheetData;
import com.test.services.excel.components.ExcelSheetWriter;
import com.test.services.excel.reader.ExcelReaderService;
import com.test.services.excel.writer.ExcelWriterService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelService {

  private final ExcelReaderService excelReaderService;

  private final ExcelWriterService excelWriterService;

  public ApiDownloadInput exportWorkBook(ExcelType type, String fileName, List<ExcelSheetWriter<?>> writers) {
    return excelWriterService.exportWorkBook(type, fileName, writers);
  }

  public ApiDownloadInput exportWorkBook(ExcelType type, String fileName, ExcelSheetWriter<?>... writers) {
    return excelWriterService.exportWorkBook(type, fileName, writers);
  }

  public List<ExcelSheetData> readExcel(InputStream in, ExcelType type, boolean isHeader) {
    return excelReaderService.readExcel(in, type, isHeader);
  }

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
          throw new ServerSideException("the extention is not supproted");
      }
    }

    public static Workbook loadWorkbook(InputStream in, ExcelType ext) throws IOException {
      switch (ext) {
        case XLS:
          return new HSSFWorkbook(in);
        case XLSX:
          return new XSSFWorkbook(in);
        default:
          throw new ServerSideException("the extention is not supproted");
      }

    }

    public static ExcelType of(String ext) {
      return Stream.of(values())
          .filter(e -> e.getExt().equals(ext))
          .findFirst()
          .orElseThrow(() -> new ServerSideException("the extention is not supproted"));

    }

  }

}
