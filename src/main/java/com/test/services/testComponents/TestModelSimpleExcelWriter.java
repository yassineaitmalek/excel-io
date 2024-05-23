package com.test.services.testComponents;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.Sheet;

import com.test.models.local.TestModel;
import com.test.services.excel.components.SimpleExcelWriter;

public class TestModelSimpleExcelWriter extends SimpleExcelWriter<TestModel> {

  @Override
  public void write(TestModel entity, Sheet sheet, AtomicInteger row, AtomicInteger column) {

    addLabel(sheet, column, row, entity.getStr1());
    addLabel(sheet, column, row, entity.getStr2());
    addLabel(sheet, column, row, entity.getStr3());
    addLabel(sheet, column, row, entity.getStr3());

  }

}
