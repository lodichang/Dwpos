package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@TableName("tb_pos_order_his")
@Accessors(chain = true)
@Getter
@Setter
@ToString
public class PosOrderHis  extends PosOrder implements Serializable {

}
