
package com.test.services.excel.writer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.test.controllers.config.ApiDownloadInput;
import com.test.exception.config.ServerSideException;
import com.test.services.excel.ExcelType;
import com.test.services.excel.components.writer.ExcelSheetWriter;
import com.test.utility.Utils;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class ExcelWriterService {

  public ApiDownloadInput exportWorkBook(ExcelType type, String fileName, ExcelSheetWriter... writers) {
    return exportWorkBook(type, fileName, Arrays.asList(writers));
  }

  public ApiDownloadInput exportWorkBook(ExcelType type, String fileName, List<? extends ExcelSheetWriter> writers) {

    try (Workbook workbook = ExcelType.createWorkbook(type);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
      Utils.checkStream(writers).forEach(e -> e.write(workbook));
      workbook.write(baos);
      return ApiDownloadInput.builder()
          .bytes(baos.toByteArray())
          .fileName(fileName)
          .ext(type.getExt())
          .build();

    } catch (Exception e) {
      throw new ServerSideException(e);
    }

  }

  public void exportWorkBook(ExcelType type, String folderPath, String fileName, ExcelSheetWriter... writers) {
    exportWorkBook(type, folderPath, fileName, Arrays.asList(writers));
  }

  @SneakyThrows
  public void exportWorkBook(ExcelType type, String folderPath, String fileName,
      List<? extends ExcelSheetWriter> writers) {
    String fullPath = Paths.get(folderPath, fileName + "." + type.getExt()).toAbsolutePath().normalize().toString();
    @Cleanup
    FileOutputStream fos = new FileOutputStream(fullPath);
    @Cleanup
    Workbook workbook = ExcelType.createWorkbook(type);
    Utils.checkStream(writers).forEach(e -> e.write(workbook));
    workbook.write(fos);
  }

  @SneakyThrows
  public void exportWorkBook(Workbook workbook, String folderPath, String fileName) {
    String fullPath = Paths.get(folderPath, fileName + "." + ExcelType.of(workbook).getExt())
        .toAbsolutePath().normalize().toString();
    @Cleanup
    FileOutputStream fos = new FileOutputStream(fullPath);
    createEmptySheetIfEmpty(workbook);
    workbook.write(fos);
  }

  public void createEmptySheetIfEmpty(Workbook workbook) {
    if (workbook.getNumberOfSheets() == 0) {
      workbook.createSheet("sheet");
    }
  }

  @SneakyThrows
  public void writeToWorkbook(Workbook workbook, List<? extends ExcelSheetWriter> writers) {
    Utils.checkStream(writers).forEach(e -> e.write(workbook));
  }

  @SneakyThrows
  public void writeToWorkbook(Workbook workbook, ExcelSheetWriter... writers) {
    writeToWorkbook(workbook, Arrays.asList(writers));
  }

}
