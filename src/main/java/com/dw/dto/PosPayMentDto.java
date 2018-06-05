package com.dw.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PosPayMentDto implements Serializable {
    private String code;
    private String desc1;
    private String desc2;
    private String desc3;
    private String desc4;
    private String nonSales;
    private String pic;
    private String overType;
}
