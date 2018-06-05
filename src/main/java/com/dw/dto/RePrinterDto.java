package com.dw.dto;

import com.dw.enums.NettyMessageTypeEnum;
import com.dw.enums.PrinterTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RePrinterDto {
    private String id;
    private String serialNumber;
    private String station;
    private String refNum;
    private String subRef;
    private String tableNum;
    private String type;
    private int persons;
    private String staffName;
    private String printerName;
    private Date sendTime;
    private String printStatus;
    private String itemName;
    private String remark;
}
