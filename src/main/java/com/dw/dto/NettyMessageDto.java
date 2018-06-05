package com.dw.dto;

import com.dw.enums.NettyMessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NettyMessageDto {
    protected NettyMessageTypeEnum msgType;
    private String tableNum;
    private String refNum;
    private String toRefNum;
    private String posHoldOnTables;

    public NettyMessageDto(NettyMessageTypeEnum msgType, String posHoldOnTables) {
        this.msgType = msgType;
        this.posHoldOnTables = posHoldOnTables;
    }
}
