package com.test.models.local;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestModel implements Serializable {

  private String str1;

  private String str2;

  private String str3;

  private String str4;

  private String str5;

  public TestModel(int i) {
    this("str1 " + i, "str2 " + i, "str3 " + i, "str4 " + i, "str5 " + i);
  }

}
