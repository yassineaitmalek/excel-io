package com.test.services.excel;

import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.test.controllers.config.ApiDownloadInput;
import com.test.services.excel.components.reader.ExcelSheetData;
import com.test.services.excel.components.writer.ExcelSheetWriter;
import com.test.services.excel.reader.ExcelReaderService;
import com.test.services.excel.writer.ExcelWriterService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelService {

  @Value("${excel.ext:xlsx}")
  private String ext;

  private final ExcelReaderService excelReaderService;

  private final ExcelWriterService excelWriterService;

  public ApiDownloadInput exportWorkBook(ExcelType type, String fileName, List<? extends ExcelSheetWriter> writers) {
    return excelWriterService.exportWorkBook(type, fileName, writers);
  }

  public ApiDownloadInput exportWorkBook(ExcelType type, String fileName, ExcelSheetWriter... writers) {
    return excelWriterService.exportWorkBook(type, fileName, writers);
  }

  public ApiDownloadInput exportWorkBook(String fileName, List<? extends ExcelSheetWriter> writers) {
    return excelWriterService.exportWorkBook(ExcelType.of(ext), fileName, writers);
  }

  public ApiDownloadInput exportWorkBook(String fileName, ExcelSheetWriter... writers) {
    return excelWriterService.exportWorkBook(ExcelType.of(ext), fileName, writers);
  }

  public List<ExcelSheetData> readExcel(InputStream in, ExcelType type, boolean isHeader) {
    return excelReaderService.readExcel(in, type, isHeader);
  }

  public void exportWorkBook(ExcelType type, String folderPath, String fileName,
      List<? extends ExcelSheetWriter> writers) {
    excelWriterService.exportWorkBook(type, folderPath, fileName, writers);
  }

  public void exportWorkBook(ExcelType type, String folderPath, String fileName, ExcelSheetWriter... writers) {
    excelWriterService.exportWorkBook(type, folderPath, fileName, writers);
  }

  public void exportWorkBook(String folderPath, String fileName, ExcelSheetWriter... writers) {
    excelWriterService.exportWorkBook(ExcelType.of(ext), folderPath, fileName, writers);
  }

  public void exportWorkBook(Workbook workbook, String folderPath, String fileName) {
    excelWriterService.exportWorkBook(workbook, folderPath, fileName);
  }

  public Workbook creaWorkbook() {
    return ExcelType.createWorkbook(ExcelType.of(ext));
  }

  public void writeToWorkbook(Workbook workbook, List<? extends ExcelSheetWriter> writers) {
    excelWriterService.writeToWorkbook(workbook, writers);
  }

  public void writeToWorkbook(Workbook workbook, ExcelSheetWriter... writers) {
    excelWriterService.writeToWorkbook(workbook, writers);
  }

}
