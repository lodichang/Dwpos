package com.dw.dto;

import com.dw.entity.PosOrder;
import com.dw.entity.PosOrderHis;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by lodi on 2018/5/13.
 */
@Getter
@Setter
@ToString
public class PosOrderCancleDto extends PosOrderHis {

    private String reasonContent;//取消原因

    private String itemName;//商品名称
}
