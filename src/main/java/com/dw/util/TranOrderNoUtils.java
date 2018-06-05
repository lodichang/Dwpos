package com.dw.util;

public class TranOrderNoUtils {

//    public static String getTranOrderNo() {
//        String tranOrderNo = null;
//        try {
//            SqlSessionUtil.getSqlSession().getConnection().setAutoCommit(false);
//            // 查詢賬單編號
//            PosSettingDto settingDto = SqlSessionUtil.getSqlSession().getMapper(PosSettingMapper.class).queryTranOrderNo();
//            tranOrderNo = settingDto.getPosValue();
//            // 更新賬單編號
//            SqlSessionUtil.getSqlSession().getMapper(PosSettingMapper.class).update(settingDto.getId(), String.valueOf(Integer.parseInt(settingDto.getPosValue()) + 1));
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            SqlSessionUtil.getSqlSession().commit(true);
//            try {
//                SqlSessionUtil.getSqlSession().getConnection().setAutoCommit(true);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return tranOrderNo;
//    }
}
