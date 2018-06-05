package com.dw.dto;

import com.dw.enums.BillTypeEnum;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PrintTaskDetailGroupByDto implements Serializable {
    private String itemCode;
    private String attCode;
    private String attGroupCode;
    private String attActionCode;
    private int kicmType;
}
