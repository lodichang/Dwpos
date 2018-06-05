package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by li.yongliang on 2018/4/14.
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class PosOutletDto implements Serializable {
    private String id; //

    private Date version; //

    private String outlet; //

    private String name1; //

    private String name2; //

    private String regionId; //线编号

    private String type; //

    private String addr; //

    private String tel; //

    private String ip; //

    private String remark1; //

    private String remark2; //

    private String remark3; //

    private String isreservation; //

    private String ishao; //

    private String image; //

    private String latitude; // 维度

    private String longitude; // 经度

    private String networkStatus; // 分店网络状态

    private String companyId; //

    private Integer onlinebill; //是否开启在線支付

    private Integer onlineorder; //是否开启在线点菜

    private String timeSpan;

    private String timeRemind;
}
