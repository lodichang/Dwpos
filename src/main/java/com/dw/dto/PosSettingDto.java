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
public class PosSettingDto implements Serializable {
    private String id;
    private String posKey;
    private String posValue;
    private String posDescript;
}
