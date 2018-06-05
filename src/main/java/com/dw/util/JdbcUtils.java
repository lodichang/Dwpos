package com.dw.util;

import com.dw.properties.ReadProperties;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JdbcUtils {
    private Connection connection = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private String dbuser;
    private String dbpasswd;
    private String dbbase;
    private String serverAdd;
    private String databaseProt;
    private String databaseType;
    public static boolean isOpenConnection = false;


    private static JdbcUtils instance = new JdbcUtils();

    private JdbcUtils() {
        //讀配置文件

        dbuser = ReadProperties.readStringByKey("dbuser");
        dbpasswd = ReadProperties.readStringByKey("dbpasswd");
        dbbase = ReadProperties.readStringByKey("dbbase");
        serverAdd = ReadProperties.readStringByKey("serverAdd");
        databaseProt = ReadProperties.readStringByKey("databaseProt");
        databaseType = ReadProperties.readStringByKey("databaseType");

        getConnection();
    }

    public static JdbcUtils getInstance() {
        return instance;
    }

    /**
     * 获得數据库连接
     *
     * @return
     */
    public Connection getConnection() {
        try {
            String connectionUrl = "jdbc:"+databaseType+"://"+serverAdd+":"+databaseProt+";databaseName="+dbbase+";user="+dbuser+";password="+dbpasswd;

            connection = DriverManager.getConnection(connectionUrl);

            isOpenConnection = connection != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    /**
     * 增加、删除、改
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public boolean updateByPreparedStatement(String sql, List<Object> params) {
        boolean flag = false;
        try {
            int result = -1;
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(sql);
            int index = 1;
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            result = pstmt.executeUpdate();
            connection.commit();
            flag = result > 0;
        } catch (Exception e) {
            try {
                //.在catch块内添加回滚事务，表示操作出现异常，撤销事务：
                connection.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generatedcatch block
                e1.printStackTrace();
            }
            flag = false;
            e.printStackTrace();
        }finally{
            try {
                //设置事务提交方式为自动提交：
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                // TODO Auto-generatedcatch block
                e.printStackTrace();
            }
        }
        return flag;
    }

    public boolean updateBatch(List<String> sqls) {
        try {
            connection.setAutoCommit(false);
            Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            for (String sql : sqls) {
                stmt.addBatch(sql);
            }
            stmt.executeBatch();
            connection.commit();
            return true;
        } catch (Exception e) {
            try {
                //.在catch块内添加回滚事务，表示操作出现异常，撤销事务：
                connection.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generatedcatch block
                e1.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }finally{
            try {
                //设置事务提交方式为自动提交：
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                // TODO Auto-generatedcatch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询单条记录
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public Map<String, Object> findSimpleResult(String sql, List<Object> params) throws SQLException {
        Map<String, Object> map = new HashMap<String, Object>();
        int index = 1;
        pstmt = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(index++, params.get(i));
            }
        }
        resultSet = pstmt.executeQuery();//返回查询结果
        ResultSetMetaData metaData = resultSet.getMetaData();
        int col_len = metaData.getColumnCount();
        while (resultSet.next()) {
            for (int i = 0; i < col_len; i++) {
                String cols_name = metaData.getColumnName(i + 1);
                Object cols_value = resultSet.getObject(cols_name);
                if (cols_value == null) {
                    cols_value = "";
                }
                map.put(cols_name, cols_value);
            }
        }
        return map;
    }

    /**
     * 查询多条记录
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> findModeResult(String sql, List<Object> params) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int index = 1;
        pstmt = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(index++, params.get(i));
            }
        }
        resultSet = pstmt.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols_len = metaData.getColumnCount();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < cols_len; i++) {
                String cols_name = metaData.getColumnName(i + 1);
                Object cols_value = resultSet.getObject(cols_name);
                if (cols_value == null) {
                    cols_value = "";
                }
                map.put(cols_name, cols_value);
            }
            list.add(map);
        }

        return list;
    }

    /**
     * 通过反射机制查询单条记录
     *
     * @param sql
     * @param params
     * @param cls
     * @return
     * @throws Exception
     */
    public <T> T findSimpleRefResult(String sql, List<Object> params, Class<T> cls) {
        T resultObject = null;
        try {
            int index = 1;
            pstmt = connection.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (resultSet.next()) {
                //通过反射机制创建一个实例
                //resultObject = cls.newInstance();

                resultObject = cls.getConstructor().newInstance();

                for (int i = 0; i < cols_len; i++) {
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = resultSet.getObject(cols_name);
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    Field field = cls.getDeclaredField(cols_name);
                    field.setAccessible(true);
                    field.set(resultObject, cols_value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObject;

    }

    /**
     * 通过反射机制查询多条记录
     *
     * @param sql
     * @param params
     * @param cls
     * @return
     * @throws Exception
     */
    public <T> List<T> findMoreRefResult(String sql, List<Object> params, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            int index = 1;
            System.out.println("****************" + connection);
            pstmt = connection.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (resultSet.next()) {
                //通过反射机制创建一个实例
//                T resultObject = cls.newInstance();
                T resultObject = cls.getConstructor().newInstance();


                for (int i = 0; i < cols_len; i++) {
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = resultSet.getObject(cols_name);
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    Field field = cls.getDeclaredField(cols_name);
                    field.setAccessible(true); //打开javabean的访问权限
                    field.set(resultObject, cols_value);
                }
                list.add(resultObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 释放數据库连接
     */
    public void releaseConn() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {


     /*   Map<String, List<PosOrderDto>> posmap = PosOrderDao.getPosOrderList("0G", "7200");



        System.out.println("臺號列表："+ posmap.size());

        posmap.forEach((k, posmaplist) -> {


            int loopCount = (int) Math.ceil((double) posmaplist.size() / 10.0);
            System.out.println("臺號:" + k + "長度：" + posmaplist.size() + "分組：" + loopCount);
            for (int i = 1; i <= loopCount; i++) {
                int fromindex = i * 10 - 10;
                int toindex = i * 10 > posmaplist.size() ? posmaplist.size() : i * 10;
                ObservableList<PosOrder> posOrderData = FXCollections.observableArrayList();
                System.out.println("開始：" + fromindex + "至：" + toindex);
                int finalI = i;
                posmaplist.subList(fromindex, toindex).forEach(posDetail -> {
                    System.out.println("臺號" + k + "組別:" + finalI + " 單號：" + posDetail.getBill_no() + " 食品：" + posDetail.getName1());
                    posOrderData.add(new PosOrder(posDetail.getBill_no(), posDetail.getSub_no(), posDetail.getType(), posDetail.getTable_no(), posDetail.getGoodsno(),
                            posDetail.getOrder_idx(), posDetail.getName1(), posDetail.getPrint_info(), DateUtil.getFormatDay(posDetail.getRt_op_date()), DateUtil.getFormatTime(posDetail.getRt_op_time()), posDetail.getS_code(), posDetail.getSeal_count(), posDetail.getAmt().doubleValue(), posDetail.getPrint_state(), posDetail.getSingle_prt(), posDetail.getT_kic_msg(), posDetail.getPos_id(), posDetail.getPrint_name(), posDetail.getBarcode(), posDetail.getStaff(), posDetail.getZone(), posDetail.getR_desc(), posDetail.getPrint_msg(), posDetail.getRegion_id(), posDetail.getPerson_num(), posDetail.getAtt_name(), posDetail.getOrder_type()));
                    PrintStateEnum oldPrintState = PrintStateEnum.getPrintStateEnumByValue(posDetail.getPrint_state());

                    PosOrderDao.updataPrintState(PrintStateEnum.PREPRINT, oldPrintState, posDetail.getBill_no(), posDetail.getOrder_idx(), posDetail.getType());

                });
            }


        });
*/

    }
}
