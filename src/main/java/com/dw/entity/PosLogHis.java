package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by li.yongliang on 2018/4/30.
 */
@TableName("tb_pos_log_his")
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@ToString
public class PosLogHis extends PosLog implements Serializable {
}
