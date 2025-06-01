package com.test.services.excel.components.writer;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import lombok.Data;

@Data
public abstract class ComplexExcelWriter<T> implements ExcelSheetWriter {

  protected T entity;

  protected String sheetName;

  protected AtomicInteger sheetIndex;

  public abstract void write(T entity, Workbook workbook, Sheet sheet);

  @Override
  public void write(Workbook workbook) {
    Sheet sheet = workbook.createSheet(getSheetName());
    write(entity, workbook, sheet);
    autoSize(sheet);
  }

  public void autoSize(Sheet sheet) {

    IntStream.range(0, 11).forEach(sheet::autoSizeColumn);

  }

  public Font blackFont(Workbook workbook, Short value) {

    Font font = workbook.createFont();
    font.setColor(IndexedColors.BLACK.index);
    font.setBold(true);
    font.setFontHeight((short) 10);
    font.setFontHeightInPoints(value);

    return font;
  }

  public Font whiteFont(Workbook workbook, Short value) {

    Font font = workbook.createFont();
    font.setColor(IndexedColors.WHITE.index);
    font.setBold(true);
    font.setFontHeight((short) 10);
    font.setFontHeightInPoints(value);

    return font;
  }

  public CellStyle styleOutside(Workbook workbook, Font font) {

    CellStyle style = workbook.createCellStyle();
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setFont(font);
    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    return style;

  }

  public CellStyle styleIntside(Workbook workbook, Font font) {

    CellStyle style = workbook.createCellStyle();
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setFont(font);
    style.setFillForegroundColor(IndexedColors.YELLOW1.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    return style;

  }

  public CellStyle styleSubRow(Workbook workbook, Font font) {

    CellStyle style = workbook.createCellStyle();
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setFont(font);
    style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    return style;

  }

  public CellStyle styleNeutral(Workbook workbook, Font font) {

    CellStyle style = workbook.createCellStyle();
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setFont(font);

    return style;

  }

  private String print(Object attribute) {
    return Optional.ofNullable(attribute)
        .map(Object::toString)
        .orElseGet(String::new);
  }

  public Row getOrCreateRow(Sheet sheet, int rowIndex) {

    return Optional.ofNullable(sheet.getRow(rowIndex)).orElseGet(() -> sheet.createRow(rowIndex));

  }

  public void addLabel(Sheet sheet, int row, int column, Object attribute, CellStyle style) {

    Cell cell = getOrCreateRow(sheet, row).createCell(column);
    cell.setCellValue(print(attribute));
    cell.setCellStyle(style);

  }

}
