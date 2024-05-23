
package com.test.services.excel.reader;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.test.exception.config.ServerSideException;
import com.test.services.excel.ExcelService.ExcelType;
import com.test.services.excel.components.ExcelSheetData;
import com.test.utility.CheckUtility;
import com.test.utility.DateUtility;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelReaderService {

  public List<ExcelSheetData> readExcel(InputStream in, ExcelType ext, boolean isHeader) {

    try (Workbook workbook = ExcelType.loadWorkbook(in, ext)) {

      AtomicInteger index = new AtomicInteger(0);
      return IntStream.range(0, workbook.getNumberOfSheets())
          .mapToObj(workbook::getSheetAt)
          .map(e -> getSheetData(e, index, isHeader))
          .collect(Collectors.toList());

    } catch (Exception e) {
      throw new ServerSideException(e);
    }

  }

  public int maxColumns(Sheet sheet) {

    return IntStream.range(0, sheet
        .getPhysicalNumberOfRows())
        .mapToObj(sheet::getRow)
        .filter(Objects::nonNull)
        .map(Row::getPhysicalNumberOfCells)
        .filter(Objects::nonNull)
        .max(Integer::compareTo)
        .filter(Objects::nonNull)
        .orElseGet(() -> 0);

  }

  public ExcelSheetData getSheetData(Sheet sheet, AtomicInteger index, boolean isHeader) {

    int maxColumns = maxColumns(sheet);
    List<List<String>> rows = IntStream.range((isHeader) ? 1 : 0, sheet.getPhysicalNumberOfRows())
        .mapToObj(sheet::getRow)
        .map(row -> readLine(row, maxColumns))
        .collect(Collectors.toList());

    List<String> header = IntStream.range(0, (isHeader) ? 1 : 0)
        .mapToObj(sheet::getRow)
        .map(row -> readLine(row, maxColumns))
        .findFirst()
        .orElseGet(Collections::emptyList);

    return ExcelSheetData.builder()
        .sheetName(sheet.getSheetName())
        .index(index.getAndIncrement())
        .header(header)
        .data(rows)
        .nbRows(rows.size())
        .nbColumns(maxColumns)
        .build();
  }

  public String numericOrDate(Cell cell) {
    int isCellDateFormatted = DateUtil.isCellDateFormatted(cell) ? 1 : 0;
    switch (isCellDateFormatted) {
      case 1:
        return DateUtility.getSimpleDateFormat().format(cell.getDateCellValue());
      case 0:
        return CheckUtility.formatDoubleXls(cell.getNumericCellValue());
      default:
        return null;
    }

  }

  public String getCellValue(Cell cell) {

    switch (cell.getCellType()) {
      case NUMERIC:
        return numericOrDate(cell);
      case STRING:
        return CheckUtility.checkString(cell.getStringCellValue());
      case BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());
      case FORMULA:
        return String.valueOf(cell.getCellFormula());
      case ERROR:
        return String.valueOf(cell.getErrorCellValue());
      default:
        return null;
    }

  }

  public List<String> readLine(Row row, int maxColumn) {

    return IntStream.range(0, maxColumn)
        .mapToObj(index -> readLineImpl(row, index))
        .collect(Collectors.toList());

  }

  public String readLineImpl(Row row, int index) {

    return Try.of(() -> row)
        .filter(Objects::nonNull)
        .mapTry(e -> e.getCell(index))
        .map(this::getCellValue)
        .map(this::checkField)
        .getOrNull();

  }

  public String checkField(String field) {

    return Optional.ofNullable(field).map(String::trim).filter(e -> !"".equals(e)).orElse(null);
  }
}
