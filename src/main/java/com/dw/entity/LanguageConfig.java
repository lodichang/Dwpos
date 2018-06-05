package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

/**
*  实体类
*/
@TableName("tb_language_config")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LanguageConfig implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("COMPANY_ID")
    private String companyId; //
    @TableField("REGION")
    private String region; //
    @TableField("LANG_KEY")
    private String langKey; //
    @TableField("LANG_COUNTRY")
    private String langCountry; //
    @TableField("LANG_VALUE")
    private String langValue; //
    @TableField("LANG_DESCRIPT")
    private String langDescript; //
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =  FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =  FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
}
