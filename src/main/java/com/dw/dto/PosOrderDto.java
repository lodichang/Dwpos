package com.dw.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lodi on 2018/4/27.
 */
@Setter
@Getter
public class  PosOrderDto {

     private String outletName;

     private String tel;

     private String billNo;

     private String inTime;

     private String dateTime;

     private String sCode;

     private String tableNo;

     private String person;

     private String address;

     private int printLanguage;//打印語言

}
