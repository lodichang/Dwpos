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
public class LanguageConfigDto implements Serializable {
    private String id;
    private String langKey;
    private String langCountry;
    private String langValue;
    private String langDescript;
}
