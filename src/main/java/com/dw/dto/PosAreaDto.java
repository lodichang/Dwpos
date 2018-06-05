package com.dw.dto;

import com.dw.entity.PosArea;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lodi on 2018/5/28.
 */
@Getter
@Setter
public class PosAreaDto extends PosArea {
    private List<PosStreetDto> posStreetDtoList;
}
