package com.test.services.excel.components;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.test.utility.Utils;

import lombok.Data;

@Data
public abstract class SimpleExcelWriter<T> implements ExcelSheetWriter<T> {

  private List<String> header;

  private List<T> lines;

  private String sheetName;

  public abstract void write(T entity, Sheet sheet, AtomicInteger row, AtomicInteger column);

  @Override
  public void write(Workbook workbook) {
    Sheet sheet = workbook.createSheet(sheetName);
    this.createHeader(workbook, sheet);
    this.createLines(sheet);
    autoSize(sheet);
  }

  public void autoSize(Sheet sheet) {

    IntStream.range(0, sheet.getRow(0).getPhysicalNumberOfCells()).forEach(sheet::autoSizeColumn);

  }

  public Sheet createLines(Sheet sheet) {

    AtomicInteger row = new AtomicInteger(1);
    Utils.checkStream(lines)
        .forEach(line -> {
          write(line, sheet, row, new AtomicInteger(0));
          row.getAndIncrement();
        });
    return sheet;

  }

  public Sheet createHeader(Workbook workbook, Sheet sheet) {
    CellStyle style = style(workbook);
    AtomicInteger col = new AtomicInteger(0);
    AtomicInteger row = new AtomicInteger(0);
    Utils.checkStream(header).forEach(e -> addCaption(sheet, col, row, e, style));
    return sheet;

  }

  public String print(Object attribute) {
    return Optional.ofNullable(attribute).map(Object::toString).orElseGet(String::new);
  }

  public Row getOrCreateRow(Sheet sheet, AtomicInteger rowIndex) {

    return Optional.ofNullable(sheet.getRow(rowIndex.get())).orElseGet(() -> sheet.createRow(rowIndex.get()));

  }

  public void addCaption(Sheet sheet, AtomicInteger column, AtomicInteger row, Object attribute, CellStyle style) {

    Cell cell = getOrCreateRow(sheet, row).createCell(column.get());
    cell.setCellValue(print(attribute));
    cell.setCellStyle(style);
    column.getAndIncrement();

  }

  public void addLabel(Sheet sheet, AtomicInteger column, AtomicInteger row, Object attribute) {

    getOrCreateRow(sheet, row).createCell(column.get()).setCellValue(print(attribute));
    column.getAndIncrement();

  }

  public Font font(Workbook workbook) {

    Font font = workbook.createFont();
    font.setColor(IndexedColors.BLACK.index);
    font.setBold(true);
    font.setFontHeight((short) 10);
    font.setFontHeightInPoints((short) 10);
    return font;
  }

  public CellStyle style(Workbook workbook) {

    CellStyle style = workbook.createCellStyle();
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setFont(font(workbook));
    return style;

  }

}
