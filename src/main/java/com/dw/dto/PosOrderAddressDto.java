package com.dw.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lodi on 2018/5/27.
 */
@Setter
@Getter
public class PosOrderAddressDto implements Serializable {
    private String address;

    private String remark;

    private String linkName;

    private String telephone;

}
