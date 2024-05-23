
package com.test.services.excel.writer;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.test.controllers.config.ApiDownloadInput;
import com.test.exception.config.ServerSideException;

import com.test.services.excel.ExcelService.ExcelType;
import com.test.services.excel.components.ExcelSheetWriter;
import com.test.utility.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelWriterService {

  public ApiDownloadInput exportWorkBook(ExcelType type, String fileName, ExcelSheetWriter<?>... writers) {
    return exportWorkBook(type, fileName, Arrays.asList(writers));
  }

  public ApiDownloadInput exportWorkBook(ExcelType type, String fileName, List<ExcelSheetWriter<?>> writers) {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try (Workbook workbook = ExcelType.createWorkbook(type)) {
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

}
