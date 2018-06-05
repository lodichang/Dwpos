package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

/**
 * Created by liang.caixing on 2018/4/24.
 */
@TableName(value = "tb_pos_outlet", resultMap = "OutletResultMap")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosOutlet implements Serializable {
    @TableField("id")
    private String id; //
    @TableField("version")
    private Date version = getNowTime(); //
    @TableField("OUTLET")
    private String outlet; //
    @TableField("NAME1")
    private String name1; //
    @TableField("NAME2")
    private String name2; //
    @TableField("REGION_ID")
    private String regionId; //线编号
    @TableField("TYPE")
    private String type; //
    @TableField("ADDR")
    private String addr; //
    @TableField("TEL")
    private String tel; //
    @TableField("IP")
    private String ip; //
    @TableField("REMARK1")
    private String remark1; //
    @TableField("REMARK2")
    private String remark2; //
    @TableField("REMARK3")
    private String remark3; //
    @TableField("ISRESERVATION")
    private String isreservation; //
    @TableField("ISHAO")
    private String ishao; //
    @TableField("IMAGE")
    private String image; //
    @TableField("LATITUDE")
    private String latitude; // 维度
    @TableField("LONGITUDE")
    private String longitude; // 经度
    @TableField("NETWORK_STATUS")
    private String networkStatus; // 分店网络状态
    @TableField("COMPANY_ID")
    private String companyId; //
    @TableField("ONLINEBILL")
    private Integer onlinebill; //是否开启在線支付

    @TableField("ONLINEORDER")
    private Integer onlineorder; //是否开启在线点菜

    @TableField("APP_AUTH_TOKEN")
    private String appAuthToken;//美团授权token

    @TableField("E_POI_ID")
    private String ePoiId;//erp厂商分配给门店的唯一标识

    @TableField("E_ID")
    private String eId;//商家登陆ERP账号id

    @TableField("E_NAME")
    private String eName;//商家登陆ERP帐号名称

    @TableField(value = "LAST_UPDATE_NAME_ID", strategy = FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME", strategy = FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
    @TableField(value = "TIME_SPAN")
    private String timeSpan;
    @TableField(value = "TIME_REMIND")
    private String timeRemind;
}
