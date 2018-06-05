package com.dw.dto;

import com.dw.enums.NettyMessageTypeEnum;
import com.dw.enums.PrinterTypeEnums;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrinterDto extends NettyMessageDto {
    private PrinterTypeEnums type;
    private String refNum;
    private String subRef;
    private String stationId;
    private String itemIdx;
    private String serialNumbers;
    private String fromTableNum;
    private String toTableNum;

    public PrinterDto(NettyMessageTypeEnum msgType) {
        super(msgType,null);
    }
}
