package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.dw.dto.PrintTaskDetailGroupByDto;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

@TableName("tb_pos_printer_task_detail")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosPrinterTaskDetail implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("SERIAL_NUMBER")
    private String serialNumber; // 打印任務序號
    @TableField("QTY")
    private BigDecimal qty; // 數量
    @TableField("SPEC")
    private String spec; // 數量打印描述
    @TableField("AMT")
    private BigDecimal amt; // 總金額
    @TableField("ITEM_CODE")
    private String itemCode; // 食品code
    @TableField("ITEM_NAME")
    private String itemName; // 打印食品名稱
    @TableField("ITEM_IDX")
    private String itemIdx; // order itemidx
    @TableField("SORT")
    private int sort; // 排序
    @TableField("TYPE")
    private String type; // 類型
    @TableField("PRINT_STATUS")
    private String printStatus; // 類型
    @TableField("KICM_TYPE")
    private int kicmType; // 廚房訊息,0-取消叫起或者未叫起,1-叫起,2-即起
    @TableField("ATT_CODE")
    private String attCode; // 類型
    @TableField("ATT_GROUP_CODE")
    private String attGroupCode; // 類型
    @TableField("ATT_ACTION_CODE")
    private String attActionCode; // 類型

}
