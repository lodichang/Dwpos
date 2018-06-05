package com.dw.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.Main;
import com.dw.dto.PosTableDto;
import com.dw.entity.PosTable;
import com.dw.enums.TableOperateType;
import com.dw.enums.TranTypeEnum;
import com.dw.mapper.PosTableMapper;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.HttpClientUtil;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.dw.util.DateUtil.CM_DATE_FORMAT;

@Service
public class PosTableService extends ServiceImpl<PosTableMapper, PosTable> {

    @Transactional(rollbackFor = Exception.class)
    public void addTable(PosTable posTable) throws Exception {
        try {
            baseMapper.insert(posTable);
            //Wrapper<PosTable> posTableWrapper  = new EntityWrapper<>();
            //  posTableWrapper.eq("ROOM_NUM",posTable.getRoomNum());
            // List<PosTable>  posTableList = baseMapper.selectList(posTableWrapper);
           /* if(AppUtils.isNotBlank(posTableList)) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject tableJson = JSONObject.fromObject(posTableList.get(0));
                tableJson.put("version",posTableList.get(0).getVersion().getTime());
                tableJson.put("lastUpdateTime",posTableList.get(0).getLastUpdateTime().getTime());
                params.add(new BasicNameValuePair("outlet", Main.posOutlet));
                params.add(new BasicNameValuePair("regionId",Main.posOutlet));
                params.add(new BasicNameValuePair("tableData",tableJson.toString()));
                params.add(new BasicNameValuePair("type", TableOperateType.ADD.getValue()));
                String url = Main.posSetting.get("apiUrl")+Main.posSetting.get("editTableUrl");
                String responseStr = HttpClientUtil.post(url, params);
                com.alibaba.fastjson.JSONObject resultJson = JSON.parseObject(responseStr);
                if(resultJson != null && AppUtils.isNotBlank(resultJson.get("code")) && resultJson.get("code").toString().equals("1")){
                  return;
                }else {
                   throw new Exception();
                }
            }
            else{
                throw new Exception();
            }*/
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateTable(PosTable posTable) throws Exception {
        try {
            baseMapper.updateById(posTable);
           /* List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject tableJson = JSONObject.fromObject(posTable);
            params.add(new BasicNameValuePair("outlet", Main.posOutlet));
            params.add(new BasicNameValuePair("regionId",Main.posOutlet));
            params.add(new BasicNameValuePair("tableData",tableJson.toString()));
            params.add(new BasicNameValuePair("type", TableOperateType.UPDATE.getValue()));
            tableJson.put("version",posTable.getVersion().getTime());
            tableJson.put("lastUpdateTime",posTable.getLastUpdateTime().getTime());
            String url = Main.posSetting.get("apiUrl")+Main.posSetting.get("editTableUrl");
            String responseStr = HttpClientUtil.post(url, params);
            com.alibaba.fastjson.JSONObject resultJson = JSON.parseObject(responseStr);
            if(resultJson != null && AppUtils.isNotBlank(resultJson.get("code")) && "1".equals(resultJson.get("code").toString())){
                return;
            }
            else {
                throw new Exception();
            }*/
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteTable(PosTable posTable) throws Exception {
        try {
            baseMapper.deleteById(posTable.getId());
           /* List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject tableJson = JSONObject.fromObject(posTable);
            tableJson.put("version",posTable.getVersion().getTime());
            tableJson.put("lastUpdateTime",posTable.getLastUpdateTime().getTime());
            params.add(new BasicNameValuePair("outlet", Main.posOutlet));
            params.add(new BasicNameValuePair("regionId",Main.posOutlet));
            params.add(new BasicNameValuePair("tableData",tableJson.toString()));
            params.add(new BasicNameValuePair("type", TableOperateType.DELETE.getValue()));
            String url = Main.posSetting.get("apiUrl")+Main.posSetting.get("editTableUrl");
            String responseStr = HttpClientUtil.post(url, params);
            com.alibaba.fastjson.JSONObject resultJson = JSON.parseObject(responseStr);
            if(resultJson != null && AppUtils.isNotBlank(resultJson.get("code")) && "1".equals(resultJson.get("code").toString())){
                return;
            }else {
                throw new Exception();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<PosTableDto> getTablesByFloor(Integer page, String floor, String tableNum, boolean checkLeaveSeat) {
        return baseMapper.getTablesByFloor(page, floor, tableNum, checkLeaveSeat);
    }

    public void updatePosTableXY(BigDecimal xRatio, BigDecimal yRatio, String roomNum) {
        baseMapper.updatePosTableXY(xRatio, yRatio, roomNum);
    }

    public List<PosTableDto> getSamePosTable(BigDecimal xRatio, BigDecimal yRatio, String roomNum) {
        return baseMapper.getSamePosTable(xRatio, yRatio, roomNum);
    }

    public List<PosTable> getsetUpTableByTableNum(String tableNum, String disPlayType) {
        return baseMapper.getsetUpTableByTableNum(tableNum, disPlayType);
    }

    public List<PosTableDto> getDisplayTableByTableNum(String tableNum, boolean checkLeaveSeat) {
        return baseMapper.getDisplayTableByTableNum(tableNum, checkLeaveSeat);
    }

    public PosTable getTableByNum(String tableNum) {
        return baseMapper.getTableByNum(tableNum);
    }
    
    public String getTableTypeByTableNum(String tableNum) {
        return baseMapper.getTableTypeByTableNum(tableNum);
    }
}
