package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by li.yongliang on 2018/4/16.
 * @author
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PosAttActionDto implements Serializable {

    private String actionCode;
    private String isShow;
    private String isMust;
    private String desc1;
    private String desc2;
    private String desc3;
    private String desc4;
    private String printDec;
    private String remark1;
    private String remark2;
    private String remark3;
}
