package com.test.services.excel.components.writer;

import java.util.Collections;
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
import org.apache.poi.ss.util.CellRangeAddress;

import com.test.utility.Utils;

import lombok.Data;

@Data
public abstract class SimpleExcelWriter<T> implements ExcelSheetWriter {

  protected List<String> header;

  protected List<T> lines;

  protected String sheetName;

  public abstract void write(T entity, Sheet sheet, AtomicInteger row, AtomicInteger column);

  @Override
  public void write(Workbook workbook) {
    Sheet sheet = workbook.createSheet(getSheetName());
    this.createHeader(workbook, sheet);
    this.createLines(sheet);
    autoSize(sheet);
  }

  private void autoSize(Sheet sheet) {

    IntStream.range(0, sheet.getRow(0).getPhysicalNumberOfCells()).forEach(sheet::autoSizeColumn);

  }

  private Sheet createLines(Sheet sheet) {

    AtomicInteger row = new AtomicInteger(1);
    Utils.checkStream(getLines())
        .forEach(line -> {
          write(line, sheet, row, new AtomicInteger(0));
          row.getAndIncrement();
        });
    return sheet;

  }

  private Sheet createHeader(Workbook workbook, Sheet sheet) {
    CellStyle style = style(workbook);
    AtomicInteger col = new AtomicInteger(0);
    AtomicInteger row = new AtomicInteger(0);
    Optional.ofNullable(getHeader())
        .orElseGet(Collections::emptyList)
        .stream().forEach(e -> addCaption(sheet, col, row, e, style));
    if (!getHeader().isEmpty()) {
      sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, getHeader().size() - 1));
    }

    return sheet;

  }

  private String print(Object attribute) {
    return Optional.ofNullable(attribute).map(Object::toString).orElseGet(String::new);
  }

  private Row getOrCreateRow(Sheet sheet, AtomicInteger rowIndex) {

    return Optional.ofNullable(sheet.getRow(rowIndex.get())).orElseGet(() -> sheet.createRow(rowIndex.get()));

  }

  private void addCaption(Sheet sheet, AtomicInteger column, AtomicInteger row, Object attribute, CellStyle style) {

    Cell cell = getOrCreateRow(sheet, row).createCell(column.get());
    cell.setCellValue(print(attribute));
    cell.setCellStyle(style);
    column.getAndIncrement();

  }

  public void addLabel(Sheet sheet, AtomicInteger column, AtomicInteger row, Object attribute) {

    getOrCreateRow(sheet, row).createCell(column.get()).setCellValue(print(attribute));
    column.getAndIncrement();

  }

  private Font font(Workbook workbook) {

    Font font = workbook.createFont();
    font.setColor(IndexedColors.BLACK.index);
    font.setBold(true);
    font.setFontHeight((short) 10);
    font.setFontHeightInPoints((short) 10);
    return font;
  }

  private CellStyle style(Workbook workbook) {

    CellStyle style = workbook.createCellStyle();
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setFont(font(workbook));
    return style;

  }

  public static <T> SimpleExcelWriter<T> empty(List<String> newHeder, String newSheetName) {
    return new SimpleExcelWriter<T>() {

      @Override
      public void write(T entity, Sheet sheet, AtomicInteger row, AtomicInteger column) {
        // empty writer
      }

      @Override
      public List<String> getHeader() {
        return newHeder;
      }

      @Override
      public String getSheetName() {
        return newSheetName;
      }

      @Override
      public List<T> getLines() {
        return Collections.emptyList();
      }

    };
  }

}
