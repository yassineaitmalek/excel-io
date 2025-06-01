package com.test.controllers;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.controllers.config.AbstractController;
import com.test.controllers.config.ApiDataResponse;
import com.test.dto.FileDTO;
import com.test.exception.config.ServerSideException;
import com.test.models.local.TestModel;
import com.test.services.excel.ExcelService;
import com.test.services.excel.ExcelType;
import com.test.services.excel.components.reader.ExcelSheetData;
import com.test.services.testComponents.TestModelSimpleExcelWriter;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/excel/simple")
@RequiredArgsConstructor
public class ExcelController implements AbstractController {

  private final ExcelService excelService;

  @GetMapping("/download/xls")
  public ResponseEntity<byte[]> downLoadSimpleXLS() {
    TestModelSimpleExcelWriter writer = new TestModelSimpleExcelWriter();
    writer.setHeader(Arrays.asList("str1", "str2", "str3", "str4"));
    writer.setSheetName("str");
    writer.setLines(IntStream.range(1, 1000).mapToObj(TestModel::new)
        .collect(Collectors.toList()));

    return download(excelService.exportWorkBook(ExcelType.XLS, "TestModelSimple", writer));

  }

  @GetMapping("/download/xlsx")
  public ResponseEntity<byte[]> downloadSimpleXLSX() {
    TestModelSimpleExcelWriter writer = new TestModelSimpleExcelWriter();
    writer.setHeader(Arrays.asList("str1", "str2", "str3", "str4"));
    writer.setSheetName("str");
    writer.setLines(IntStream.range(1, 1000).mapToObj(TestModel::new)
        .collect(Collectors.toList()));

    return download(excelService.exportWorkBook(ExcelType.XLSX, "TestModelSimple", writer));

  }

  @PutMapping(value = "/import/xls", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<ApiDataResponse<List<ExcelSheetData>>> importSimpleXLS(
      @ModelAttribute FileDTO fileDTO) {

    try (InputStream in = fileDTO.getFile().getInputStream()) {
      return ok(() -> excelService.readExcel(in, ExcelType.XLS, true));
    } catch (Exception e) {
      throw new ServerSideException(e);
    }

  };

  @PutMapping(value = "/import/xlsx", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<ApiDataResponse<List<ExcelSheetData>>> importSimpleXLSX(
      @ModelAttribute FileDTO fileDTO) {

    try (InputStream in = fileDTO.getFile().getInputStream()) {
      return ok(() -> excelService.readExcel(in, ExcelType.XLSX, true));
    } catch (Exception e) {
      throw new ServerSideException(e);
    }

  };

}
