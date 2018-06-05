package com.dw.dto;

import com.dw.entity.PosAtt;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class PosAttHeadDto implements Serializable {
    private String id;
    private String code;
    private String outline;
    private String desc1;
    private String desc2;
    private String desc3;
    private String desc4;
    private String printDesc;
    private String type;
    private String remarks;
    private String canChange;
    private String remark1;
    private String remark2;
    private String remark3;
    private List<PosAtt> posAttDtoList;
}
