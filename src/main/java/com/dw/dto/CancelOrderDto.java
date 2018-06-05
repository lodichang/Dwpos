package com.dw.dto;

import com.dw.entity.PosOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CancelOrderDto implements Serializable {
    private boolean isCanceled;
    private long ogriItemIdx; // 原下单记录idx
    private String itemPrn; // 菜品印记编号
    private PosOrder cancelOrder; // 取消的下单记录
}
