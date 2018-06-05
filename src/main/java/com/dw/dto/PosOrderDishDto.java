package com.dw.dto;

import java.util.List;

/**
 * Created by lodi on 2018/4/27.
 */
public class PosOrderDishDto extends PosOrderDto {

    private String prn_name;

    private String zone;

    private String table_Name;

    private String message;

    private String barCode;

    private String station;

    private String s_code;

    private List<String> orderList;

    public String getPrn_name() {
        return prn_name;
    }

    public void setPrn_name(String prn_name) {
        this.prn_name = prn_name;
    }

    public String getTable_Name() {
        return table_Name;
    }

    public void setTable_Name(String table_Name) {
        this.table_Name = table_Name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getS_code() {
        return s_code;
    }

    public void setS_code(String s_code) {
        this.s_code = s_code;
    }

    public List<String> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<String> orderList) {
        this.orderList = orderList;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
