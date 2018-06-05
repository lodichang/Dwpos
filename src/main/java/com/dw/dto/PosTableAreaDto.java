package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
* 桌台区域表 实体类
*/
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosTableAreaDto implements Serializable{

     private String id ; //

     private Date version;

     private String desc1 ; //区域名称

     private String desc2 ; //

     private String desc3 ; //

     private String desc4 ; //

     private String printDesc ; //

     private Integer sort ; //

     private String lastUpdateNameId ;

     private Date lastUpdateTime ; //
}
