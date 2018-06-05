package com.dw.dto;

import com.dw.entity.PosAddress;
import com.dw.entity.PosStreet;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lodi on 2018/5/28.
 */
@Setter
@Getter
public class PosStreetDto extends PosStreet {

    private List<PosAddress> posAddressList;
}
