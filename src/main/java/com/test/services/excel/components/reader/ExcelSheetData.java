package com.test.services.excel.components.reader;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.test.utility.Utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExcelSheetData {

  private String sheetName;

  private Integer index;

  private List<String> header;

  private List<List<String>> data;

  private Integer nbRows;

  private Integer nbColumns;

  public <T> List<T> parse(SimpleExcelParser<T> parser) {

    return Utils.checkStream(data)
        .map(parser::parse)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());

  }

  public <T> Optional<T> parse(ComplexExcelParser<T> parser) {

    return parser.parse(data);

  }
}
