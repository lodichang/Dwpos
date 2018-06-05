package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by li.yongliang on 2018/5/23.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CleanDto implements Serializable{

    private String code;

    private String desc1;
    private String desc2;
    private String desc3;
    private String desc4;
    private String cou;
    private String amt1;
    private String amt2;
    private String amt3;
    private int seq;
    private String titletype;


}
