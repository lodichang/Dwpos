package com.dw.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PosStaffRightDto implements Serializable {

    private String rightKey;
    private String rightValue;

}
