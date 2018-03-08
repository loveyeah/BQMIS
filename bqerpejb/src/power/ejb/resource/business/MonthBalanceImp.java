/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.InvJBalance;
import power.ejb.resource.form.IssueInfoForReport;
import power.ejb.resource.form.MaterialDiscrepancyForReport;
import power.ejb.resource.form.MonthBalanceInfo;
import power.ejb.resource.form.WarehouseThisMonthInfo;

/**
 * 库存月结 implements
 * @author huangweijie
 */
@Stateless
public class MonthBalanceImp implements MonthBalance{
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**
     * 批号本期接收
     * @param startId 结账起始流水号
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getReceipt(Long startId, String enterpriseCode) {
        LogUtil.log("finding Lot Receipt Quantity with startId: " + startId,
                Level.INFO, null);
        try {
            // 入库和退库
            String sql =
            "SELECT\n" + 
                "A.LOT_NO,\n" + 
                "A.FROM_WHS_NO AS WHS_NO,\n" + 
                "A.FROM_LOCATION_NO AS LOCATION_NO,\n" + 
                "A.MATERIAL_ID,\n" + 
                "SUM (A.TRANS_QTY) AS QTY\n" + 
            "FROM\n" + 
                "INV_J_TRANSACTION_HIS A,\n" + 
                "INV_C_WAREHOUSE B,\n" + 
                "INV_C_TRANSACTION C\n" + 
            "WHERE\n" + 
                "(C.TRANS_CODE = 'P' OR \n" + 
                "C.TRANS_CODE = 'R') AND\n" + 
                "C.ENTERPRISE_CODE = ? AND\n" + 
                "C.IS_USE = 'Y' AND\n" + 
                "C.TRANS_ID = A.TRANS_ID AND\n" +
                "A.TRANS_HIS_ID >= ? AND\n" + 
                "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.FROM_WHS_NO = B.WHS_NO AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.IS_INSPECT = 'N' AND\n" + 
                "B.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "A.LOT_NO, A.FROM_WHS_NO, A.FROM_LOCATION_NO, A.MATERIAL_ID\n";
            // 查询数据库
            List list = bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, 
                    startId, enterpriseCode, enterpriseCode});
            // 移库
            sql = 
                "SELECT\n" + 
                "A.LOT_NO,\n" + 
                "A.TO_WHS_NO AS WHS_NO,\n" + 
                "A.TO_LOCATION_NO AS LOCATION_NO,\n" + 
                "A.MATERIAL_ID,\n" + 
                "SUM (A.TRANS_QTY) AS QTY\n" + 
            "FROM\n" + 
                "INV_J_TRANSACTION_HIS A,\n" + 
                "INV_C_WAREHOUSE B,\n" + 
                "INV_C_TRANSACTION C\n" + 
            "WHERE\n" + 
                "C.TRANS_CODE = 'TT' AND\n" + 
                "C.ENTERPRISE_CODE = ? AND\n" + 
                "C.IS_USE = 'Y' AND\n" + 
                "C.TRANS_ID = A.TRANS_ID AND\n" +
                "A.TRANS_HIS_ID >= ? AND\n" + 
                "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.TO_WHS_NO = B.WHS_NO AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.IS_INSPECT = 'N' AND\n" + 
                "B.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "A.LOT_NO, A.TO_WHS_NO, A.TO_LOCATION_NO, A.MATERIAL_ID\n";
            // 查询数据库，并添加到list中
            list.addAll(bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, 
                    startId, enterpriseCode, enterpriseCode}));
            List<MonthBalanceInfo> arrlist = new ArrayList<MonthBalanceInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) { 
                MonthBalanceInfo balanceinfo = new MonthBalanceInfo();                
                Object[] data = (Object[]) it.next();
                // 批号
                if(null != data[0])
                balanceinfo.setLotNo(data[0].toString());
                // 仓库编码
                if(null != data[1])
                balanceinfo.setWhsNo(data[1].toString());
                // 库位编码
                if(null != data[2])
                balanceinfo.setLocationNo(data[2].toString());
                // 物料ID
                if(null != data[3])
                balanceinfo.setMaterialId(Long.parseLong(data[3].toString()));
                // 物料数量
                if(null != data[4])
                balanceinfo.setQuantity(Double.parseDouble(data[4].toString()));
                arrlist.add(balanceinfo);
            }
            return arrlist;
        } catch (RuntimeException e) {
            LogUtil.log("find Lot Receipt Quantity failed", Level.SEVERE, e);
            throw e;
        }
    }
    /**
     * 批号本期调整
     * @param startId 结账起始流水号
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getAdjust(Long startId, String enterpriseCode) {
        LogUtil.log("finding Lot Adjust Quantity with startId: " + startId,
                Level.INFO, null);
        try {
            // 事务类型为盘点
            String sql = 
                "SELECT\n" + 
                "A.LOT_NO,\n" + 
                "A.FROM_WHS_NO AS WHS_NO,\n" + 
                "A.FROM_LOCATION_NO AS LOCATION_NO,\n" + 
                "A.MATERIAL_ID,\n" + 
                "SUM (A.TRANS_QTY) AS QTY\n" + 
            "FROM\n" + 
                "INV_J_TRANSACTION_HIS A,\n" + 
                "INV_C_WAREHOUSE B,\n" + 
                "INV_C_TRANSACTION C\n" + 
            "WHERE\n" + 
                "C.TRANS_CODE = 'A' AND\n" + 
                "C.ENTERPRISE_CODE = ? AND\n" + 
                "C.IS_USE = 'Y' AND\n" + 
                "C.TRANS_ID = A.TRANS_ID AND\n" +
                "A.TRANS_HIS_ID >= ? AND\n" + 
                "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.FROM_WHS_NO = B.WHS_NO AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.IS_INSPECT = 'N' AND\n" + 
                "B.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "A.LOT_NO, A.FROM_WHS_NO, A.FROM_LOCATION_NO, A.MATERIAL_ID\n";
            // 查询数据库
            List list = bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, 
                    startId, enterpriseCode, enterpriseCode});
            List<MonthBalanceInfo> arrlist = new ArrayList<MonthBalanceInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) { 
                MonthBalanceInfo balanceinfo = new MonthBalanceInfo();                
                Object[] data = (Object[]) it.next();
                // 批号
                if(null != data[0])
                balanceinfo.setLotNo(data[0].toString());
                // 仓库编码
                if(null != data[1])
                balanceinfo.setWhsNo(data[1].toString());
                // 库位编码
                if(null != data[2])
                balanceinfo.setLocationNo(data[2].toString());
                // 物料ID
                if(null != data[3])
                balanceinfo.setMaterialId(Long.parseLong(data[3].toString()));
                // 物料数量
                if(null != data[4])
                balanceinfo.setQuantity(Double.parseDouble(data[4].toString()));
                arrlist.add(balanceinfo);
            }
            return arrlist;
        } catch (RuntimeException e) {
            LogUtil.log("find Lot Adjust Quantity failed", Level.SEVERE, e);
            throw e;
        }
    }
    /**
     * 批号本期出库
     * @param startId 结账起始流水号
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getIssue(Long startId, String enterpriseCode) {
        LogUtil.log("finding Lot Issue Quantity with startId: " + startId,
                Level.INFO, null);
        try {
            // 事务类型为盘点
            String sql = 
            "SELECT\n" + 
                "A.LOT_NO,\n" + 
                "A.FROM_WHS_NO AS WHS_NO,\n" + 
                "A.FROM_LOCATION_NO AS LOCATION_NO,\n" + 
                "A.MATERIAL_ID,\n" + 
                "SUM (A.TRANS_QTY) AS QTY\n" + 
            "FROM\n" + 
                "INV_J_TRANSACTION_HIS A,\n" + 
                "INV_C_WAREHOUSE B,\n" + 
                "INV_C_TRANSACTION C\n" + 
            "WHERE\n" + 
                "(C.TRANS_CODE = 'I' OR \n" + 
                "C.TRANS_CODE = 'TT') AND\n" + 
                "C.ENTERPRISE_CODE = ? AND\n" + 
                "C.IS_USE = 'Y' AND\n" + 
                "C.TRANS_ID = A.TRANS_ID AND\n" +
                "A.TRANS_HIS_ID >= ? AND\n" + 
                "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.FROM_WHS_NO = B.WHS_NO AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.IS_INSPECT = 'N' AND\n" + 
                "B.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "A.LOT_NO, A.FROM_WHS_NO, A.FROM_LOCATION_NO, A.MATERIAL_ID\n";
            // 查询数据库
            List list = bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, 
                    startId, enterpriseCode, enterpriseCode});
            List<MonthBalanceInfo> arrlist = new ArrayList<MonthBalanceInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) { 
                MonthBalanceInfo balanceinfo = new MonthBalanceInfo();                
                Object[] data = (Object[]) it.next();
                // 批号
                if(null != data[0])
                balanceinfo.setLotNo(data[0].toString());
                // 仓库编码
                if(null != data[1])
                balanceinfo.setWhsNo(data[1].toString());
                // 库位编码
                if(null != data[2])
                balanceinfo.setLocationNo(data[2].toString());
                // 物料ID
                if(null != data[3])
                balanceinfo.setMaterialId(Long.parseLong(data[3].toString()));
                // 物料数量
                if(null != data[4])
                balanceinfo.setQuantity(Double.parseDouble(data[4].toString()));
                arrlist.add(balanceinfo);
            }
            return arrlist;
        } catch (RuntimeException e) {
            LogUtil.log("find Lot Issue Quantity failed", Level.SEVERE, e);
            throw e;
        }
    }
    
    /**
     * 仓库本期接收
     * @param startId 结账起始流水号
     * @param whoNo 仓库编码
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getReceipt(Long startId, String whsNo, String enterpriseCode) {
        LogUtil.log("finding Warehouse Receipt Quantity with startId: " + startId 
                + " and whosNo: " + whsNo, Level.INFO, null);
        try {
            // 入库和退库
            String sql =
            "SELECT\n" + 
                "A.MATERIAL_ID,\n" + 
                "SUM (A.TRANS_QTY) AS QTY\n" + 
            "FROM\n" + 
                "INV_J_TRANSACTION_HIS A,\n" + 
                "INV_C_WAREHOUSE B,\n" + 
                "INV_C_TRANSACTION C\n" + 
            "WHERE\n" + 
                "(C.TRANS_CODE = 'P' OR \n" + 
                "C.TRANS_CODE = 'R') AND\n" + 
                "C.ENTERPRISE_CODE = ? AND\n" + 
                "C.IS_USE = 'Y' AND\n" + 
                "C.TRANS_ID = A.TRANS_ID AND\n" +
                "A.TRANS_HIS_ID >= ? AND\n";
            if (!("".equals(whsNo) || null == whsNo)) {
                sql += "A.FROM_WHS_NO = '" + whsNo + "' AND\n";
            } else {
                sql += "A.FROM_WHS_NO IS NULL AND\n";
            }
            sql += "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.FROM_WHS_NO = B.WHS_NO AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.IS_INSPECT = 'N' AND\n" + 
                "B.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "A.MATERIAL_ID\n";
            // 查询数据库
            List list = bll.queryByNativeSQL(sql, new Object[]{enterpriseCode,
                    startId, enterpriseCode, enterpriseCode});
            // 移库
            sql = 
            "SELECT\n" + 
                "A.MATERIAL_ID,\n" + 
                "SUM (A.TRANS_QTY) AS QTY\n" + 
            "FROM\n" + 
                "INV_J_TRANSACTION_HIS A,\n" + 
                "INV_C_WAREHOUSE B,\n" + 
                "INV_C_TRANSACTION C\n" + 
            "WHERE\n" + 
                "C.TRANS_CODE = 'TT' AND\n" + 
                "C.ENTERPRISE_CODE = ? AND\n" + 
                "C.IS_USE = 'Y' AND\n" + 
                "C.TRANS_ID = A.TRANS_ID AND\n" +
                "A.TRANS_HIS_ID >= ? AND\n" + 
                "A.TO_WHS_NO = '" + whsNo + "' AND\n" + 
                "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.TO_WHS_NO = B.WHS_NO AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.IS_INSPECT = 'N' AND\n" + 
                "B.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "A.MATERIAL_ID\n";
            // 查询数据库，并添加到list中
            list.addAll(bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, 
                    startId, enterpriseCode, enterpriseCode}));
            List<MonthBalanceInfo> arrlist = new ArrayList<MonthBalanceInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) { 
                MonthBalanceInfo balanceinfo = new MonthBalanceInfo();                
                Object[] data = (Object[]) it.next();
                // 物料ID
                if(null != data[0])
                balanceinfo.setMaterialId(Long.parseLong(data[0].toString()));
                // 物料数量
                if(null != data[1])
                balanceinfo.setQuantity(Double.parseDouble(data[1].toString()));
                arrlist.add(balanceinfo);
            }
            return arrlist;
        } catch (RuntimeException e) {
            LogUtil.log("find Warehouse Receipt Quantity failed", Level.SEVERE, e);
            throw e;
        }
    }
    /**
     * 仓库本期调整
     * @param startId 结账起始流水号
     * @param whoNo 仓库编码
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getAdjust(Long startId, String whsNo, String enterpriseCode) {
        LogUtil.log("finding Warehouse Adjust Quantity with startId: " + startId + 
                " and whsNo:" + whsNo, Level.INFO, null);
        try {
            // 事务类型为盘点
            String sql = 
            "SELECT\n" + 
                "A.MATERIAL_ID,\n" + 
                "SUM (A.TRANS_QTY) AS QTY\n" + 
            "FROM\n" + 
                "INV_J_TRANSACTION_HIS A,\n" + 
                "INV_C_WAREHOUSE B,\n" + 
                "INV_C_TRANSACTION C\n" + 
            "WHERE\n" + 
                "C.TRANS_CODE = 'A' AND\n" + 
                "C.ENTERPRISE_CODE = ? AND\n" + 
                "C.IS_USE = 'Y' AND\n" + 
                "C.TRANS_ID = A.TRANS_ID AND\n" +
                "A.TRANS_HIS_ID >= ? AND\n" + 
                "A.FROM_WHS_NO = ? AND\n" + 
                "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.FROM_WHS_NO = B.WHS_NO AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.IS_INSPECT = 'N' AND\n" + 
                "B.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "A.MATERIAL_ID\n";
            // 查询数据库
            List list = bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, startId, 
                    whsNo, enterpriseCode, enterpriseCode});
            List<MonthBalanceInfo> arrlist = new ArrayList<MonthBalanceInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) { 
                MonthBalanceInfo balanceinfo = new MonthBalanceInfo();                
                Object[] data = (Object[]) it.next();
                // 物料ID
                if(null != data[0])
                balanceinfo.setMaterialId(Long.parseLong(data[0].toString()));
                // 物料数量
                if(null != data[1])
                balanceinfo.setQuantity(Double.parseDouble(data[1].toString()));
                arrlist.add(balanceinfo);
            }
            return arrlist;
        } catch (RuntimeException e) {
            LogUtil.log("find Warehouse Adjust Quantity failed", Level.SEVERE, e);
            throw e;
        }
    }
    /**
     * 仓库本期出库
     * @param startId 结账起始流水号
     * @param whoNo 仓库编码
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getIssue(Long startId, String whsNo, String enterpriseCode) {
        LogUtil.log("finding Warehouse Issue Quantity with startId: " + startId + 
                " and whsNo:" + whsNo, Level.INFO, null);
        try {
            // 事务类型为盘点
            String sql = 
            "SELECT\n" + 
                "A.MATERIAL_ID,\n" + 
                "SUM (A.TRANS_QTY) AS QTY\n" + 
            "FROM\n" + 
                "INV_J_TRANSACTION_HIS A,\n" + 
                "INV_C_WAREHOUSE B,\n" + 
                "INV_C_TRANSACTION C\n" + 
            "WHERE\n" + 
                "(C.TRANS_CODE = 'I' OR \n" + 
                "C.TRANS_CODE = 'TT') AND\n" + 
                "C.ENTERPRISE_CODE = ? AND\n" + 
                "C.IS_USE = 'Y' AND\n" + 
                "C.TRANS_ID = A.TRANS_ID AND\n" +
                "A.TRANS_HIS_ID >= ? AND\n" + 
                "A.FROM_WHS_NO = ? AND\n" + 
                "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.FROM_WHS_NO = B.WHS_NO AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.IS_INSPECT = 'N' AND\n" + 
                "B.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "A.MATERIAL_ID\n";
            // 查询数据库
            List list = bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, startId,
                    whsNo, enterpriseCode, enterpriseCode});
            List<MonthBalanceInfo> arrlist = new ArrayList<MonthBalanceInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) { 
                MonthBalanceInfo balanceinfo = new MonthBalanceInfo();                
                Object[] data = (Object[]) it.next();
                // 物料ID
                if(null != data[0])
                balanceinfo.setMaterialId(Long.parseLong(data[0].toString()));
                // 物料数量
                if(null != data[1])
                balanceinfo.setQuantity(Double.parseDouble(data[1].toString()));
                arrlist.add(balanceinfo);
            }
            return arrlist;
        } catch (RuntimeException e) {
            LogUtil.log("find Warehouse Issue Quantity failed", Level.SEVERE, e);
            throw e;
        }
    }
    
    /**
     * 库位本期接收
     * @param startId 结账起始流水号
     * @param whoNo 仓库编码
     * @param locationNo 库位编码
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getReceipt(Long startId, String whsNo, String locationNo, String enterpriseCode) {
        LogUtil.log("finding Location Receipt Quantity with startId: " + startId + 
                " and whsNo:" + whsNo + " and locationNo:" + locationNo, Level.INFO, null);
        try {
            // 入库和退库
            String sql =
            "SELECT\n" + 
                "A.MATERIAL_ID,\n" + 
                "SUM (A.TRANS_QTY) AS QTY\n" + 
            "FROM\n" + 
                "INV_J_TRANSACTION_HIS A,\n" + 
                "INV_C_WAREHOUSE B,\n" + 
                "INV_C_TRANSACTION C\n" + 
            "WHERE\n" + 
                "(C.TRANS_CODE = 'P' OR \n" + 
                "C.TRANS_CODE = 'R') AND\n" + 
                "C.ENTERPRISE_CODE = ? AND\n" + 
                "C.IS_USE = 'Y' AND\n" + 
                "C.TRANS_ID = A.TRANS_ID AND\n" +
                "A.TRANS_HIS_ID >= ? AND\n" + 
                "A.FROM_WHS_NO = ? AND\n";
                if (!("".equals(locationNo) || null == locationNo)) {
                    sql += "A.FROM_LOCATION_NO = '" + locationNo + "' AND\n";
                } else {
                    sql += "A.FROM_LOCATION_NO IS NULL AND\n";
                }
                sql += "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.FROM_WHS_NO = B.WHS_NO AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.IS_INSPECT = 'N' AND\n" + 
                "B.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "A.MATERIAL_ID\n";
            // 查询数据库
            List list = bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, 
                    startId, whsNo, enterpriseCode, enterpriseCode});
            // 移库
            sql = 
            "SELECT\n" + 
                "A.MATERIAL_ID,\n" + 
                "SUM (A.TRANS_QTY) AS QTY\n" + 
            "FROM\n" + 
                "INV_J_TRANSACTION_HIS A,\n" + 
                "INV_C_WAREHOUSE B,\n" + 
                "INV_C_TRANSACTION C\n" + 
            "WHERE\n" + 
                "C.TRANS_CODE = 'TT' AND\n" + 
                "C.ENTERPRISE_CODE = ? AND\n" + 
                "C.IS_USE = 'Y' AND\n" + 
                "C.TRANS_ID = A.TRANS_ID AND\n" +
                "A.TRANS_HIS_ID >= ? AND\n" + 
                "A.TO_WHS_NO = ? AND\n";
                if (!("".equals(locationNo) || null == locationNo)) {
                    sql += "A.TO_LOCATION_NO = '" + locationNo + "' AND\n";
                } else {
                    sql += "A.TO_LOCATION_NO IS NULL AND\n";
                }
                sql += "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.TO_WHS_NO = B.WHS_NO AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.IS_INSPECT = 'N' AND\n" + 
                "B.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "A.MATERIAL_ID\n";
            // 查询数据库，并添加到list中
            list.addAll(bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, 
                    startId, whsNo, enterpriseCode, enterpriseCode}));
            List<MonthBalanceInfo> arrlist = new ArrayList<MonthBalanceInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) { 
                MonthBalanceInfo balanceinfo = new MonthBalanceInfo();                
                Object[] data = (Object[]) it.next();
                // 物料ID
                if(null != data[0])
                balanceinfo.setMaterialId(Long.parseLong(data[0].toString()));
                // 物料数量
                if(null != data[1])
                balanceinfo.setQuantity(Double.parseDouble(data[1].toString()));
                arrlist.add(balanceinfo);
            }
            return arrlist;
        } catch (RuntimeException e) {
            LogUtil.log("find Location Receipt Quantity failed", Level.SEVERE, e);
            throw e;
        }
    }
    /**
     * 库位本期调整
     * @param startId 结账起始流水号
     * @param whoNo 仓库编码
     * @param locationNo 库位编码
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getAdjust(Long startId, String whsNo, String locationNo, String enterpriseCode) {
        LogUtil.log("finding Location Adjust Quantity with startId: " + startId + 
                " and whsNo:" + whsNo + " and locationNo:" + locationNo, Level.INFO, null);
        try {
            // 事务类型为盘点
            String sql = 
                "SELECT\n" + 
                "A.MATERIAL_ID,\n" + 
                "SUM (A.TRANS_QTY) AS QTY\n" + 
            "FROM\n" + 
                "INV_J_TRANSACTION_HIS A,\n" + 
                "INV_C_WAREHOUSE B,\n" + 
                "INV_C_TRANSACTION C\n" + 
            "WHERE\n" + 
                "C.TRANS_CODE = 'A' AND\n" + 
                "C.ENTERPRISE_CODE = ? AND\n" + 
                "C.IS_USE = 'Y' AND\n" + 
                "C.TRANS_ID = A.TRANS_ID AND\n" +
                "A.TRANS_HIS_ID >= ? AND\n" + 
                "A.FROM_WHS_NO = ? AND\n";
                if (!("".equals(locationNo) || null == locationNo)) {
                    sql += "A.FROM_LOCATION_NO = '" + locationNo + "' AND\n";
                } else {
                    sql += "A.FROM_LOCATION_NO IS NULL AND\n";
                }
                sql += "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.FROM_WHS_NO = B.WHS_NO AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.IS_INSPECT = 'N' AND\n" + 
                "B.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "A.MATERIAL_ID\n";
            // 查询数据库
            List list = bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, 
                    startId, whsNo, enterpriseCode, enterpriseCode});
            List<MonthBalanceInfo> arrlist = new ArrayList<MonthBalanceInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) { 
                MonthBalanceInfo balanceinfo = new MonthBalanceInfo();                
                Object[] data = (Object[]) it.next();
                // 物料ID
                if(null != data[0])
                balanceinfo.setMaterialId(Long.parseLong(data[0].toString()));
                // 物料数量
                if(null != data[1])
                balanceinfo.setQuantity(Double.parseDouble(data[1].toString()));
                arrlist.add(balanceinfo);
            }
            return arrlist;
        } catch (RuntimeException e) {
            LogUtil.log("find Location Adjust Quantity failed", Level.SEVERE, e);
            throw e;
        }
    }
    /**
     * 库位本期出库
     * @param startId 结账起始流水号
     * @param whoNo 仓库编码
     * @param locationNo 库位编码
     * @param enterpriseCode 企业编码
     * @return 库存月结 bean
     */
    public List<MonthBalanceInfo> getIssue(Long startId, String whsNo, String locationNo, String enterpriseCode) {
        LogUtil.log("finding Location Issue Quantity with startId: " + startId + 
                " and whsNo:" + whsNo + " and locationNo:" + locationNo, Level.INFO, null);
        try {
            // 事务类型为出库
            String sql = 
            "SELECT\n" + 
                "A.MATERIAL_ID,\n" + 
                "SUM (A.TRANS_QTY) AS QTY\n" + 
            "FROM\n" + 
                "INV_J_TRANSACTION_HIS A,\n" + 
                "INV_C_WAREHOUSE B,\n" + 
                "INV_C_TRANSACTION C\n" + 
            "WHERE\n" + 
                "(C.TRANS_CODE = 'I' OR \n" + 
                "C.TRANS_CODE = 'TT') AND\n" + 
                "C.ENTERPRISE_CODE = ? AND\n" + 
                "C.IS_USE = 'Y' AND\n" + 
                "C.TRANS_ID = A.TRANS_ID AND\n" +
                "A.TRANS_HIS_ID >= ? AND\n" + 
                "A.FROM_WHS_NO = ? AND\n";
                if (!("".equals(locationNo) || null == locationNo)) {
                    sql += "A.FROM_LOCATION_NO = '" + locationNo + "' AND\n";
                } else {
                    sql += "A.FROM_LOCATION_NO IS NULL AND\n";
                }
                sql += "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.FROM_WHS_NO = B.WHS_NO AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.IS_INSPECT = 'N' AND\n" + 
                "B.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "A.MATERIAL_ID\n";
            // 查询数据库
            List list = bll.queryByNativeSQL(sql, new Object[]{enterpriseCode,
                    startId, whsNo, enterpriseCode, enterpriseCode});
            List<MonthBalanceInfo> arrlist = new ArrayList<MonthBalanceInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) { 
                MonthBalanceInfo balanceinfo = new MonthBalanceInfo();                
                Object[] data = (Object[]) it.next();
                // 物料ID
                if(null != data[0])
                balanceinfo.setMaterialId(Long.parseLong(data[0].toString()));
                // 物料数量
                if(null != data[1])
                balanceinfo.setQuantity(Double.parseDouble(data[1].toString()));
                arrlist.add(balanceinfo);
            }
            return arrlist;
        } catch (RuntimeException e) {
            LogUtil.log("find Location Issue Quantity failed", Level.SEVERE, e);
            throw e;
        }
    }
    
    /**
     * 批号记录表
     * @param enterpriseCode 企业编码
     * @return 批号月结bean
     */
    public List<MonthBalanceInfo> getLotTable(String enterpriseCode) {
        LogUtil.log("finding Lot Quantity with enterpriseCode: " + enterpriseCode,
                Level.INFO, null);
        try {
            // 查找批号记录表
            String sql = 
                "SELECT\n" + 
                "B.LOT_NO, B.WHS_NO, B.LOCATION_NO,\n" + 
                "B.MATERIAL_ID, SUM(B.RECEIPT) AS RECEIPT,\n" + 
                "SUM (B.ADJUST) AS ADJUST,\n" + 
                "SUM (B.ISSUE) AS ISSUE\n" + 
            "FROM\n" + 
                "INV_C_WAREHOUSE A,\n" + 
                "INV_J_LOT B\n" + 
            "WHERE\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.WHS_NO = A.WHS_NO AND\n" + 
                "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.IS_INSPECT = 'N' AND\n" + 
                "A.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "B.LOT_NO, B.WHS_NO, B.LOCATION_NO, B.MATERIAL_ID\n";
            // 查询数据库
            List list = bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, 
                    enterpriseCode});
            List<MonthBalanceInfo> arrlist = new ArrayList<MonthBalanceInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) { 
                MonthBalanceInfo balanceinfo = new MonthBalanceInfo();                
                Object[] data = (Object[]) it.next();
                // 批号
                if(null != data[0])
                balanceinfo.setLotNo(data[0].toString());
                // 仓库编码
                if(null != data[1])
                balanceinfo.setWhsNo(data[1].toString());
                // 库位编码
                if(null != data[2])
                balanceinfo.setLocationNo(data[2].toString());
                // 物料ID
                if(null != data[3])
                balanceinfo.setMaterialId(Long.parseLong(data[3].toString()));
                // 本期接收
                if(null != data[4])
                balanceinfo.setReceipt(Double.parseDouble(data[4].toString()));
                // 本期调整
                if(null != data[5])
                balanceinfo.setAdjust(Double.parseDouble(data[5].toString()));
                // 本期出库
                if(null != data[6])
                balanceinfo.setIssue(Double.parseDouble(data[6].toString()));
                arrlist.add(balanceinfo);
            }
            return arrlist;
        } catch (RuntimeException e) {
            LogUtil.log("find Lot Quantity failed", Level.SEVERE, e);
            throw e;
        }
    }
    /**
     * 库存记录表
     * @param whsNo 仓库编码
     * @param enterpriseCode 企业编码
     * @return 库存月结bean
     */
    public List<MonthBalanceInfo> getWarehouseTable(String whsNo, String enterpriseCode) {
        LogUtil.log("finding WareHouse Quantity with whsNo: " + whsNo,
                Level.INFO, null);
        try {
            // 查找库存记录表
            String sql = 
            "SELECT\n" + 
                "B.MATERIAL_ID, \n" + 
                "SUM (B.RECEIPT) AS RECEIPT,\n" + 
                "SUM (B.ADJUST) AS ADJUST,\n" + 
                "SUM (B.ISSUE) AS ISSUE\n" + 
            "FROM\n" + 
                "INV_C_WAREHOUSE A,\n" + 
                "INV_J_WAREHOUSE B\n" + 
            "WHERE\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.WHS_NO = ? AND\n" + 
                "B.WHS_NO = A.WHS_NO AND\n" + 
                "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.IS_INSPECT = 'N' AND\n" + 
                "A.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "B.MATERIAL_ID\n";
            // 查询数据库
            List list = bll.queryByNativeSQL(sql, new Object[]{
                    enterpriseCode, whsNo, enterpriseCode});
            List<MonthBalanceInfo> arrlist = new ArrayList<MonthBalanceInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) { 
                MonthBalanceInfo balanceinfo = new MonthBalanceInfo();                
                Object[] data = (Object[]) it.next();
                // 物料ID
                if(null != data[0])
                balanceinfo.setMaterialId(Long.parseLong(data[0].toString()));
                // 本期接收
                if(null != data[1])
                balanceinfo.setReceipt(Double.parseDouble(data[1].toString()));
                // 本期调整
                if(null != data[2])
                balanceinfo.setAdjust(Double.parseDouble(data[2].toString()));
                // 本期出库
                if(null != data[3])
                balanceinfo.setIssue(Double.parseDouble(data[3].toString()));
                arrlist.add(balanceinfo);
            }
            return arrlist;
        } catch (RuntimeException e) {
            LogUtil.log("find WareHouse Quantity failed", Level.SEVERE, e);
            throw e;
        }
    }
    /**
     * 库位记录表
     * @param whsNo 仓库编码
     * @param locationNo 库位编码
     * @param enterpriseCode 企业编码
     * @return 库位月结bean
     */
    public List<MonthBalanceInfo> getLocationTable(String whsNo, String locationNo, String enterpriseCode) {
        LogUtil.log("finding Location Quantity with whsNo: " + whsNo 
                + " and locationNo:" + locationNo,
                Level.INFO, null);
        try {
            // 查找库位记录表
            String sql = 
            "SELECT\n" + 
                "B.MATERIAL_ID, \n" + 
                "SUM (B.RECEIPT) AS RECEIPT,\n" + 
                "SUM (B.ADJUST) AS ADJUST,\n" + 
                "SUM (B.ISSUE) AS ISSUE\n" + 
            "FROM\n" + 
                "INV_C_WAREHOUSE A,\n" + 
                "INV_J_LOCATION B\n" + 
            "WHERE\n" + 
                "B.IS_USE = 'Y' AND\n" + 
                "B.ENTERPRISE_CODE = ? AND\n" + 
                "B.WHS_NO = ? AND\n";
            if (!("".equals(locationNo) || null == locationNo)) {
                sql += "B.LOCATION_NO = '" + locationNo + "' AND\n"; 
            } else {
                sql += "B.LOCATION_NO IS NULL AND\n";
            }
            sql += "B.WHS_NO = A.WHS_NO AND\n" + 
                "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y' AND\n" + 
                "A.IS_INSPECT = 'N' AND\n" + 
                "A.IS_COST = 'Y'\n" + 
            "GROUP BY\n" + 
                "B.MATERIAL_ID\n";
            // 查询数据库
            List list = bll.queryByNativeSQL(sql, new Object[]{
                    enterpriseCode, whsNo, enterpriseCode});
            List<MonthBalanceInfo> arrlist = new ArrayList<MonthBalanceInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                MonthBalanceInfo balanceinfo = new MonthBalanceInfo();
                Object[] data = (Object[]) it.next();
                // 物料ID
                if(null != data[0])
                balanceinfo.setMaterialId(Long.parseLong(data[0].toString()));
                // 本期接收
                if(null != data[1])
                balanceinfo.setReceipt(Double.parseDouble(data[1].toString()));
                // 本期调整
                if(null != data[2])
                balanceinfo.setAdjust(Double.parseDouble(data[2].toString()));
                // 本期出库
                if(null != data[3])
                balanceinfo.setIssue(Double.parseDouble(data[3].toString()));
                arrlist.add(balanceinfo);
            }
            return arrlist;
        } catch (RuntimeException e) {
            LogUtil.log("find Location Quantity failed", Level.SEVERE, e);
            throw e;
        }
    }
    
    /**
     * 由物料ID从库存物料记录表中得到异动数量
     * @param materialIdList 【物料ID】list
     * @param enterpriseCode 企业编码
     * @return 【物料ID，异动数量】list 
     */
    public List<MonthBalanceInfo> getMandQ(String materialIdList, String enterpriseCode) {
        LogUtil.log("finding Material Quantity with materialIdList: " + materialIdList,
                Level.INFO, null);
        try {
            String sql = 
            "SELECT\n" + 
                "A.MATERIAL_ID,\n" + 
                "SUM(A.OPEN_BALANCE + A.RECEIPT \n" + 
                "+ A.ADJUST - A.ISSUE) AS QUANTITY\n" + 
            "FROM\n" + 
                "INV_J_WAREHOUSE A\n" + 
            "WHERE\n" + 
                "A.MATERIAL_ID IN (" + materialIdList + 
                ") AND\n" + 
                "A.ENTERPRISE_CODE = ? AND\n" + 
                "A.IS_USE = 'Y'\n" + 
            "GROUP BY\n" + 
                "A.MATERIAL_ID\n";
            // 查询数据库
            List list = bll.queryByNativeSQL(sql, new Object[]{enterpriseCode});
            List<MonthBalanceInfo> arrlist = new ArrayList<MonthBalanceInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                MonthBalanceInfo balanceinfo = new MonthBalanceInfo();
                Object[] data = (Object[]) it.next();
                // 物料ID
                if(null != data[0])
                balanceinfo.setMaterialId(Long.parseLong(data[0].toString()));
                // 异动数量
                if(null != data[1])
                balanceinfo.setQuantity(Double.parseDouble(data[1].toString()));
                arrlist.add(balanceinfo);
            }
            return arrlist;
        } catch (RuntimeException e) {
            LogUtil.log("find Material Quantity failed", Level.SEVERE, e);
            throw e;
        }
    }
	
    /**
     * 材料收发统计报表总信息
     * modify by fyyang 090811
     * @return List<WarehouseThisMonthInfo>
     * add by ywliu 2009/7/4
     */
//	public List<WarehouseThisMonthInfo> getWarehouseThisMonthInfo(String dateMonth) {
//		List<WarehouseThisMonthInfo> resultList = new ArrayList<WarehouseThisMonthInfo>();
//		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
//		String balanceSql=
//			"select t.balance_id,\n" + 
//			"       t.balance_type,\n" + 
//			"       t.balance_year_month,\n" + 
//			"       to_char(t.balance_date, 'yyyy-MM-dd'),\n" + 
//			"       t.trans_his_minid,\n" + 
//			"       t.trans_his_maxid,\n" + 
//			"       (select to_char(a.balance_date, 'yyyy-MM-dd')\n" + 
//			"          from inv_j_balance a\n" + 
//			"         where a.balance_id = t.balance_id + 1) as nextDate,\n" + 
//			"       (select to_char(a.balance_date, 'yyyy-MM-dd')\n" + 
//			"          from inv_j_balance a\n" + 
//			"         where a.balance_id = t.balance_id - 1) as lastDate,\n" + 
//			"       (select a.trans_his_minid\n" + 
//			"          from inv_j_balance a\n" + 
//			"         where a.balance_id = t.balance_id - 1) as lastmin,\n" + 
//			"       (select a.trans_his_maxid\n" + 
//			"          from inv_j_balance a\n" + 
//			"         where a.balance_id = t.balance_id - 1) as lastmax\n" + 
//			"  from inv_j_balance t\n" + 
//			" where t.balance_year_month = "+dateMonth+"\n" + 
//			"   and rownum = 1";
//
//
//		List balanceList=bll.queryByNativeSQL(balanceSql);
//		
//		if(balanceList!=null&&balanceList.size()>0)
//		{
//			InvJBalance model=new InvJBalance();
//			String thisDate="";
//			String lastDate="0000-00-00";
//			String nextDate="0000-00-00";
//			String lastMin="0";
//			String lastMax="0";
//			Long lastId=0l;
//			Object [] objData=(Object [])balanceList.get(0);
//			if(objData[0]!=null)
//			{
//			 model.setBalanceId(Long.parseLong(objData[0].toString()));
//			 lastId=model.getBalanceId()-1;
//			}
//			if(objData[1]!=null)
//			{
//				model.setBalanceType(objData[1].toString());
//			}
//			if(objData[2]!=null)
//			{
//				model.setBalanceYearMonth(Long.parseLong(objData[2].toString()));
//			}
//			if(objData[3]!=null)
//			{
//				thisDate=objData[3].toString();
//			}
//			if(objData[4]!=null)
//			{
//				model.setTransHisMinid(Long.parseLong(objData[4].toString()));
//			}
//			if(objData[5]!=null)
//			{
//				model.setTransHisMaxid(Long.parseLong(objData[5].toString()));
//			}
//			if(objData[6]!=null)
//			{
//				nextDate=objData[6].toString();
//			}
//			if(objData[7]!=null)
//			{
//				lastDate=objData[7].toString();
//			}
//			if(objData[8]!=null)
//			{
//				lastMin=objData[8].toString();
//			}
//			if(objData[9]!=null)
//			{
//				lastMax=objData[9].toString();
//			}
//			
//			String sql=
//				"select x.whs_no,\n" +
//				"       x.whs_name,\n" + 
//				"       nvl(a.lastqty, 0),\n" + 
//				"       nvl(a.inqty, 0),\n" + 
//				"       nvl(a.outqty, 0),\n" + 
//				"       nvl(nvl(a.lastqty, 0) + nvl(a.inqty, 0) - nvl(a.outqty, 0), 0)\n" + 
//				"       ,nvl(a.thisZanShou,0) \n"+
//				"  from (select m.default_whs_no,\n" + 
//				"               getwhsname(m.default_whs_no),\n" + 
//				"               sum(nvl(ttt.lastqty, 0)) as lastqty,\n" + 
//				"               sum(nvl(ttt.inqty, 0)) as inqty,\n" + 
//				"               sum(nvl(ttt.outqty, 0)) as outqty,\n" + 
//				"               sum(nvl(ttt.thisqty * ttt.stdCost, 0)) as thisqty\n" + 
//				"               ,sum(nvl(ttt.zanShou,0))  as thisZanShou\n"+
//				"          from (\n" + 
//				"\n" + 
//				"                select tt4.material_id,\n" + 
//				"                        tt7.price7 as zanShou,"+
//				"                        tt1.qty1 as inqty,\n" + 
//				"                        tt2.qty2 - nvl(tt5.lastInQty, 0) +\n" + 
//				"                        nvl(tt6.lastOutQty, 0) as lastqty,\n" + 
//				"                        tt3.qty3 as outqty,\n" + 
//				"                        tt4.qty4 as thisqty,\n" + 
//				"                        (select b.std_cost\n" + 
//				"                           from inv_c_material b\n" + 
//				"                          where b.material_id = tt4.material_id and b.is_use='Y') as stdCost,\n" + 
//				"                        (select c.std_cost\n" + 
//				"                           from inv_j_transaction_his c\n" + 
//				"                          where c.is_use='Y' and c.trans_his_id =\n" + 
//				"                                (select max(d.trans_his_id)\n" + 
//				"                                   from inv_j_transaction_his d\n" + 
//				"                                  where d.is_use='Y' and d.trans_id = 6\n" + 
//				"                                    and d.trans_his_id >= "+model.getTransHisMinid()+"\n" + 
//				"                                    and d.trans_his_id <= "+model.getTransHisMaxid()+"\n" + 
//				"                                    and tt4.material_id = d.material_id)) as lastStdCost\n" + 
//				"                  from (select t1.material_id,\n" + 
//				"                                sum(round(t1.trans_qty * t1.price,2)) as qty1\n" + 
//				"                           from inv_j_transaction_his t1, pur_j_arrival t11\n" + 
//				"                          where t1.is_use='Y' and t11.is_use='Y' and t1.arrival_no = t11.arrival_no\n" + 
//				"                            and t1.trans_id = 1\n" + 
//				"                            and t11.check_date >\n" + 
//				"                                (select c.balance_date\n" + 
//				"                                   from inv_j_balance c\n" + 
//				"                                  where c.is_use='Y' and c.balance_id = "+lastId+")\n" + 
//				"                            and t11.check_date <\n" + 
//				"                                (select c.balance_date\n" + 
//				"                                   from inv_j_balance c\n" + 
//				"                                  where  c.is_use='Y' and c.balance_id = "+model.getBalanceId()+")\n" + 
//				//"                            and (t11.check_state = '2' or t11.check_state = 'N')\n" + //红单也需要审核
//				"                            and t11.check_state = '2' \n" + 
//				"                          group by t1.material_id) tt1,\n" + 
//				"\n" + 
//				"                        (select t2.material_id,\n" + 
//				"                                sum(round(t2.trans_qty * t2.std_cost,2)) qty2\n" + 
//				"                           from inv_j_transaction_his t2\n" + 
//				"                          where t2.is_use='Y' and t2.trans_id = 6\n" + 
//				"                            and t2.trans_his_id >= "+model.getTransHisMinid()+"\n" + 
//				"                            and t2.trans_his_id <= "+model.getTransHisMaxid()+"\n" + 
//				"                          group by t2.material_id\n" + 
//				"\n" + 
//				"                         ) tt2,\n" + 
//				"\n" + 
//				"                        (select t3.material_id,\n" + 
//				"                                sum(round(t3.trans_qty * t3.std_cost,2)) qty3\n" + 
//				"                           from inv_j_transaction_his t3\n" + //, inv_j_issue_head t33 modify by ywliu 20100201
//				"                          where t3.is_use='Y' and t3.trans_id = 4\n" + 
////				"                            and t3.order_no = t33.issue_no\n" + 
//				"                            and t3.check_date >\n" + 
//				"                                (select c.balance_date\n" + 
//				"                                   from inv_j_balance c\n" + 
//				"                                  where c.is_use='Y' and c.balance_id = "+lastId+")\n" + 
//				"                            and t3.check_date <\n" + 
//				"                                (select c.balance_date\n" + 
//				"                                   from inv_j_balance c\n" + 
//				"                                  where c.is_use='Y' and c.balance_id = "+model.getBalanceId()+")\n" + 
//				"                            and t3.check_status = 'C'\n" + 
//				"                          group by t3.material_id) tt3,\n" + 
//				"\n" + 
//				"                        (select t4.material_id, sum(t4.trans_qty) qty4\n" + 
//				"                           from inv_j_transaction_his t4\n" + 
//				"                          where t4.is_use='Y' and t4.trans_id = 6\n" + 
//				"                            and to_char(t4.last_modified_date, 'yyyy-MM-dd') =\n" + 
//				"                                '"+thisDate+"'\n" + 
//				"                          group by t4.material_id) tt4,\n" + 
//
//				//------------------modify by fyyang 20100111-------------------------------------------
//
//				"(select t5.material_id, sum(round(t5.trans_qty * t5.price,2)) as lastInQty\n" +
//				"  from inv_j_transaction_his t5\n" + 
//				" where t5.is_use='Y' and t5.trans_his_id < "+model.getTransHisMinid()+"\n" + 
//				"   and t5.trans_id = 1\n" + 
//				"\n" + 
//				"   and t5.trans_his_id not in\n" + 
//				"       (select t55.trans_his_id\n" + 
//				"          from inv_j_transaction_his t55, pur_j_arrival b\n" + 
//				"         where t55.is_use='Y' and  t55.is_use='Y' and\n" + 
//				"            t55.arrival_no = b.arrival_no\n" + 
//				"           and t55.trans_id = 1\n" + 
//				//"           and (b.check_state = '2' or b.check_state = 'N')\n" + 
//				"           and b.check_state = '2' \n" +   //modify by fyyang 20100202
//				"           and b.check_date <= (select c.balance_date\n" + 
//				"                                  from inv_j_balance c\n" + 
//				"                                 where  c.is_use='Y' and c.balance_id = "+lastId+"))\n" + 
//				"\n" + 
//				" group by t5.material_id\n" + 
//				" ) tt5,\n"+
//
//				//-------------------------------------------------------------
//
//				//---------------------modify by fyyang 20100111--------------------------
//
//				"(\n" +
//				" select t6.material_id, sum(round(t6.trans_qty * t6.std_cost,2)) as lastOutQty\n" + 
//				"   from inv_j_transaction_his t6\n" + 
//				"  where t6.is_use='Y' and t6.trans_id = 4\n" + 
//				"    and t6.trans_his_id < "+model.getTransHisMinid()+"\n" + 
//				"    and t6.trans_his_id not in\n" + 
//				"        (select t67.trans_his_id\n" + 
//				"           from inv_j_transaction_his t67\n" + // , inv_j_issue_head t66 modify by ywliu 20100201
//				"          where t67.is_use='Y' and t67.trans_id = 4\n" + 
////				"            and t67.order_no = t66.issue_no\n" + 
//				"            and t67.check_status = 'C'\n" + 
//				"            and t67.check_date <=\n" + 
//				"                (select c.balance_date\n" + 
//				"                   from inv_j_balance c\n" + 
//				"                  where c.is_use='Y' and c.balance_id = "+lastId+"))\n" + 
//				"  group by t6.material_id\n" + 
//				"  ) tt6"+
//
//				//-----------------------------------------------
//                ",("+
//				"select t7.material_id, sum(round(t7.trans_qty * t7.price,2)) as price7\n" +
//				"  from inv_j_transaction_his t7, pur_j_arrival t71\n" + 
//				" where t7.is_use='Y' and t71.is_use='Y' and  t7.trans_id = 1\n" + 
//				//"   and t7.trans_his_id > ="+model.getTransHisMinid()+"\n" +  //modify by fyyang 20100113
//				"   and t7.trans_his_id < ="+model.getTransHisMaxid()+"\n" + 
//				"   and t7.arrival_no = t71.arrival_no\n" + 
//				//"   and ((t71.check_state <> '2' and t71.check_state <> 'N') or\n" + //modify by fyyang 20100202
//				"   and (t71.check_state <> '2'  or\n" +
//				"       t71.check_state is null)\n" + 
//				"   and t7.is_use = 'Y'\n" + 
//				"   and t71.is_use = 'Y'\n" + 
//				" group by t7.material_id)tt7 \n"+
//				"\n" + 
//				"                 where tt4.material_id = tt2.material_id(+)\n" + 
//				"                   and tt4.material_id = tt3.material_id(+)\n" + 
//				"                   and tt4.material_id = tt1.material_id(+)\n" + 
//				"                   and tt4.material_id = tt5.material_id(+)\n" + 
//				"                   and tt4.material_id = tt7.material_id(+)\n" + 
//				"                   and tt4.material_id = tt6.material_id(+)) ttt,\n" + 
//				"               inv_c_material m\n" + 
//				"         where ttt.material_id = m.material_id\n" + 
//				"\n" + 
//				"         group by m.default_whs_no) a,\n" + 
//				"       inv_c_warehouse x\n" + 
//				" where a.default_whs_no(+) = x.whs_no order by x.whs_no asc ";
//		List list = bll.queryByNativeSQL(sql);
//		Double totalReceiptCost=0d;
//		Double totalIssueCost=0d;
//		Double totalTBCost=0d;
//		Double totalOBCost=0d;
//		Double totalZanShou=0d;
//        Iterator it = list.iterator();
//        while (it.hasNext()) {
//        	WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
//            Object[] data = (Object[]) it.next();
//            // 仓库号
//            if(null != data[0])
//            monthInfo.setWhsNo(data[0].toString());
//            // 仓库名称
//            if(null != data[1])
//            monthInfo.setWhsName(data[1].toString());
//            // 统计日期
////            if(null != data[2])
////                monthInfo.setMonth(data[2].toString());
//            // 上期结存金额
//            if(null != data[2])
//            {
//                monthInfo.setOBCost(data[2].toString());
//                totalOBCost+=Double.parseDouble(monthInfo.getOBCost());
//            }
//            // 本期入库金额
//            if(null != data[3])
//            {
//                monthInfo.setReceiptCost(data[3].toString());
//                totalReceiptCost+=Double.parseDouble(monthInfo.getReceiptCost());
//            }
//            if(null != data[4])
//            {
//                monthInfo.setIssueCost(data[4].toString());
//                totalIssueCost+=Double.parseDouble(monthInfo.getIssueCost());
//            }
//            if(null != data[5])
//            {
//                monthInfo.setTBCost(data[5].toString());
//                totalTBCost+=Double.parseDouble(monthInfo.getTBCost());
//            }
//            if(null!=data[6])
//            {
//            	//暂收
//            	monthInfo.setZanShou(data[6].toString());
//            	totalZanShou+=Double.parseDouble(monthInfo.getZanShou());
//            }
//            
//            arrlist.add(monthInfo);
//        }
//        if(dateMonth.equals("201001"))
//        {
//        WarehouseThisMonthInfo addInfo=new WarehouseThisMonthInfo();
//        
//        addInfo.setWhsNo("");
//        addInfo.setWhsName("调整");
//        arrlist.add(addInfo);
//        }
//        for(int i=0;i<arrlist.size();i++)
//        {
//        	WarehouseThisMonthInfo entity=arrlist.get(i);
//        	if(dateMonth.equals("201001"))
//    		{
//        		//5-D+56;B-H+200;5-N+300;4-1 -255 add by fyyang 20100208 
//    			entity.setOBCost(dataOperate(Double.parseDouble(totalOBCost.toString())+301));
//    			entity.setReceiptCost(dataOperate(Double.parseDouble(totalReceiptCost.toString())-287290.14));
//    			entity.setTBCost(dataOperate(Double.parseDouble(totalTBCost.toString())+301-287290.14));
//    		}
//        	else if(dateMonth.equals("201002"))
//    		{
//        		// add by fyyang 20100302
//    			entity.setOBCost(dataOperate(Double.parseDouble(totalOBCost.toString())-1163.516052+300.98));
//    			entity.setReceiptCost(dataOperate(Double.parseDouble(totalReceiptCost.toString())));
//    			entity.setTBCost(dataOperate(Double.parseDouble(totalTBCost.toString())-1163.516052+300.98));
//    		}
//        	else
//        	{
//        	entity.setOBCost(dataOperate(Double.parseDouble(totalOBCost.toString())));
//        	entity.setReceiptCost(dataOperate(Double.parseDouble(totalReceiptCost.toString())));
//        	entity.setTBCost(dataOperate(Double.parseDouble(totalTBCost.toString())));
//        	}
//        	
//        	entity.setIssueCost(dataOperate(Double.parseDouble(totalIssueCost.toString())));
//        	
//        	if(dateMonth.equals("200912"))
//        	{
//        		entity.setZanShou("2,945,560.35");
//        	}
//        	else
//        	{
//        	entity.setZanShou(dataOperate(Double.parseDouble(totalZanShou.toString())));
//        	}
//        		
//        	
//        	resultList.add(entity);
//        	
//        }
//        
//		}
//		return resultList;
//	}
    
    /*
     *  add by fyyang 20100308 材料收发统计报表
     */
    public List<WarehouseThisMonthInfo> getWarehouseThisMonthInfo(String dateMonth) {
		List<WarehouseThisMonthInfo> resultList = new ArrayList<WarehouseThisMonthInfo>();
		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
		String balanceSql=
			"select t.balance_id\n" + 
			"  from inv_j_balance t\n" + 
			" where t.balance_year_month = "+dateMonth+"\n" + 
			"   and rownum = 1";
       Long balanceId=Long.parseLong(bll.getSingal(balanceSql).toString());

		
			
        String sql=
        	"select w.whs_no,\n" +
        	"       w.whs_name,\n" + 
        	"       nvl(t.lastprice, 0),\n" + 
        	"       nvl(t.inprice, 0),\n" + 
        	"       nvl(t.outprice, 0),\n" + 
        	"       nvl(t.thisprice, 0),\n" + 
        	"       nvl(t.zanshou, 0)\n" + 
        	"  from (select a.whs_no,\n" + 
        	"               sum(round(nvl(a.openbalance_price, 0), 2)) lastprice,\n" + 
        	"               sum(round(nvl(a.check_in_price, 0), 2)) inprice,\n" + 
        	"               sum(round(nvl(a.check_out_price, 0), 2)) outprice,\n" + 
        	"               sum(round(nvl(a.openbalance_price, 0), 2) +\n" + 
        	"                   round(nvl(a.check_in_price, 0), 2) -\n" + 
        	"                   round(nvl(a.check_out_price, 0), 2)) thisprice,\n" + 
        	"               sum(round(nvl(a.nocheck_in_price, 0), 2)) zanshou\n" + 
        	"          from inv_j_transaction_his_money a, inv_c_material m\n" + 
        	"         where a.balance_id = "+balanceId+"\n" + 
        	"           and a.material_id = m.material_id\n" + 
        	"           and a.is_use = 'Y'\n" + 
        	"           and m.is_use = 'Y'\n" + 
        	"         group by a.whs_no) t,\n" + 
        	"       inv_c_warehouse w\n" + 
        	" where w.whs_no = t.whs_no(+)\n" + 
        	" order by w.whs_no asc";


		List list = bll.queryByNativeSQL(sql);
		Double totalReceiptCost=0d;
		Double totalIssueCost=0d;
		Double totalTBCost=0d;
		Double totalOBCost=0d;
		Double totalZanShou=0d;
        Iterator it = list.iterator();
        while (it.hasNext()) {
        	WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
            Object[] data = (Object[]) it.next();
            // 仓库号
            if(null != data[0])
            monthInfo.setWhsNo(data[0].toString());
            // 仓库名称
            if(null != data[1])
            monthInfo.setWhsName(data[1].toString());
    
            // 上期结存金额
            if(null != data[2])
            {
                monthInfo.setOBCost(data[2].toString());
                totalOBCost+=Double.parseDouble(monthInfo.getOBCost());
            }
            // 本期入库金额
            if(null != data[3])
            {
                monthInfo.setReceiptCost(data[3].toString());
                totalReceiptCost+=Double.parseDouble(monthInfo.getReceiptCost());
            }
            if(null != data[4])
            {
                monthInfo.setIssueCost(data[4].toString());
                totalIssueCost+=Double.parseDouble(monthInfo.getIssueCost());
            }
            if(null != data[5])
            {
                monthInfo.setTBCost(data[5].toString());
                totalTBCost+=Double.parseDouble(monthInfo.getTBCost());
            }
            if(null!=data[6])
            {
            	//暂收
            	monthInfo.setZanShou(data[6].toString());
            	totalZanShou+=Double.parseDouble(monthInfo.getZanShou());
            }
            
            arrlist.add(monthInfo);
        }
        if(dateMonth.equals("201001"))
        {
        WarehouseThisMonthInfo addInfo=new WarehouseThisMonthInfo();
        
        addInfo.setWhsNo("");
        addInfo.setWhsName("调整");
        arrlist.add(addInfo);
        }
        for(int i=0;i<arrlist.size();i++)
        {
        	WarehouseThisMonthInfo entity=arrlist.get(i);
        	if(dateMonth.equals("201001"))
    		{
        		
    			entity.setOBCost(dataOperate(Double.parseDouble(totalOBCost.toString())));
    			entity.setReceiptCost(dataOperate(Double.parseDouble(totalReceiptCost.toString())-287290.14));
    			entity.setTBCost(dataOperate(Double.parseDouble(totalTBCost.toString())-287290.14));
    		}
//        	else if(dateMonth.substring(0, 4).equals("2010"))
//    		{
//        		
//    			entity.setOBCost(dataOperate(Double.parseDouble(totalOBCost.toString())-287290.14));
//    			entity.setReceiptCost(dataOperate(Double.parseDouble(totalReceiptCost.toString())));
//    			entity.setTBCost(dataOperate(Double.parseDouble(totalTBCost.toString())-287290.14));
//    		}
        	else
        	{
        	entity.setOBCost(dataOperate(Double.parseDouble(totalOBCost.toString())));
        	entity.setReceiptCost(dataOperate(Double.parseDouble(totalReceiptCost.toString())));
        	entity.setTBCost(dataOperate(Double.parseDouble(totalTBCost.toString())));
        	}
        	
        	entity.setIssueCost(dataOperate(Double.parseDouble(totalIssueCost.toString())));
        	
        	if(dateMonth.equals("200912"))
        	{
        		entity.setZanShou("2,945,560.35");
        	}
        	else
        	{
        	entity.setZanShou(dataOperate(Double.parseDouble(totalZanShou.toString())));
        	}
        		
        	
        	resultList.add(entity);
        	
        }
        
		
		return resultList;
	}
    
    /**
     * add by fyyang 20100308 材料收发统计报表明细
     */
    public List<WarehouseThisMonthInfo> getWarehouseThisMonthDetailInfo(String dateMonth,String whsNo) {
		
    	if(dateMonth.equals("201001")&&whsNo.equals(""))
    	{
    		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
    		WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
    		monthInfo.setWhsNo("扩建");
    		monthInfo.setReceiptCost("-287290.14");
    		arrlist.add(monthInfo);
    		return arrlist;
    	}
    	else
    	{
    	
    		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
    		String balanceSql=
    			"select t.balance_id\n" + 
    			"  from inv_j_balance t\n" + 
    			" where t.balance_year_month = "+dateMonth+"\n" + 
    			"   and rownum = 1";
           Long balanceId=Long.parseLong(bll.getSingal(balanceSql).toString());
    		
    		
    			
    			String sql=
    				"select b.class_no,\n" +
    				"       b.class_name,\n" + 
    				"       nvl(t.inprice, 0),\n" + 
    				"       nvl(t.lastprice, 0),\n" + 
    				"       nvl(t.outprice, 0),\n" + 
    				"       nvl(t.thisprice, 0),\n" + 
    				"       nvl(t.zanshou, 0)\n" + 
    				"  from (select substr(c.class_no, 0, 3) class_no,\n" + 
    				"               sum(round(nvl(a.openbalance_price, 0), 2)) lastprice,\n" + 
    				"               sum(round(nvl(a.check_in_price, 0), 2)) inprice,\n" + 
    				"               sum(round(nvl(a.check_out_price, 0), 2)) outprice,\n" + 
    				"               sum(round(nvl(a.openbalance_price, 0), 2) +\n" + 
    				"                   round(nvl(a.check_in_price, 0), 2) -\n" + 
    				"                   round(nvl(a.check_out_price, 0), 2)) thisprice,\n" + 
    				"               sum(round(nvl(a.nocheck_in_price, 0), 2)) zanshou\n" + 
    				"          from inv_j_transaction_his_money a,\n" + 
    				"               inv_c_material              m,\n" + 
    				"               inv_c_material_class        c\n" + 
    				"         where a.material_id = m.material_id\n" + 
    				"           and m.maertial_class_id = c.maertial_class_id\n" + 
    				"           and a.balance_id="+balanceId+" \n"+
    				"           and a.is_use = 'Y'\n" + 
    				"           and m.is_use = 'Y'\n" + 
    				"           and c.is_use = 'Y'\n" + 
    				"         group by substr(c.class_no, 0, 3)) t,\n" + 
    				"       inv_c_material_class b\n" + 
    				" where b.class_no = t.class_no(+)\n" + 
    				"   and length(b.class_no) = 3\n" + 
    				"   and b.is_use = 'Y'\n" + 
    				"   and b.class_no like '"+whsNo+"%'\n" + 
    				" order by b.class_no asc";



    		List list = bll.queryByNativeSQL(sql);
    		Double totalReceiptCost=0d;
    		Double totalIssueCost=0d;
    		Double totalTBCost=0d;
    		Double totalOBCost=0d;
    		Double totalZanShou=0d;
    		
            Iterator it = list.iterator();
            while (it.hasNext()) {
            	WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
                Object[] data = (Object[]) it.next();
                // 类别编码
                if(null != data[0])
                monthInfo.setWhsNo(data[0].toString());
                // 类别名称
                if(null != data[1])
                monthInfo.setWhsName(data[1].toString());
                // 本期入库金额
                if(null != data[2])
                	
                {
                    monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())));
                    totalReceiptCost+=Double.parseDouble(data[2].toString());
                }
                // 上期结存金额
                if(null != data[3])
                {
                	
//                  if(dateMonth.equals("201002"))
//                 {
//                	 if(monthInfo.getWhsNo().equals("5-0"))
//                  	{
//                  		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())-287290.14));
//                  		 totalOBCost+=Double.parseDouble(data[3].toString())-287290.14;
//                  	}
//                	 else
//                  	{
//                  		 monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())));
//                          totalOBCost+=Double.parseDouble(data[3].toString());
//                  	}
//                 }
//                 else
//                 {
                	 monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())));
                     totalOBCost+=Double.parseDouble(data[3].toString());
//                 }
                 
                 
                 
                
                }
             // 本期出库金额
                if(null != data[4])
                {
                	
                   monthInfo.setIssueCost(dataOperate(Double.parseDouble(data[4].toString())));
                   totalIssueCost+=Double.parseDouble(data[4].toString());
                }
                // 本期结余金额
                if(null != data[5])
                {
                	if(dateMonth.equals("201001")&&monthInfo.getWhsNo().equals("5-0"))
                	{
                		monthInfo.setTBCost(dataOperate(Double.parseDouble(data[5].toString())-287290.14));
                        totalTBCost+=Double.parseDouble(data[5].toString())-287290.14;
                	}
                	else
                	{
                		monthInfo.setTBCost(dataOperate(Double.parseDouble(data[5].toString())));
                        totalTBCost+=Double.parseDouble(data[5].toString());
                	}
                }
                //本期暂收金额
                 if(null!=data[6])
                 {
                	 monthInfo.setZanShou(dataOperate(Double.parseDouble(data[6].toString())));
                	 totalZanShou+=Double.parseDouble(data[6].toString());
                 }
                arrlist.add(monthInfo);
            }
            WarehouseThisMonthInfo entity = new WarehouseThisMonthInfo();
            entity.setWhsNo("小计");
           
            
            	entity.setOBCost(dataOperate(totalOBCost));
            
            entity.setReceiptCost(dataOperate(totalReceiptCost));
            
            entity.setIssueCost(dataOperate(totalIssueCost));
            entity.setTBCost(dataOperate(totalTBCost));
            entity.setZanShou(dataOperate(totalZanShou));
            arrlist.add(entity);
            
    		
    		return arrlist;
    	}
    	
    	}
        
	
	//add by fyyang 091202
//public List<WarehouseThisMonthInfo> getWarehouseThisMonthDetailInfo(String dateMonth,String whsNo) {
//		
//	if(dateMonth.equals("201001")&&whsNo.equals(""))
//	{
//		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
//		WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
//		monthInfo.setWhsNo("扩建");
//		monthInfo.setReceiptCost("-287290.14");
//		arrlist.add(monthInfo);
//		return arrlist;
//	}
//	else
//	{
//	
//		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
//		String balanceSql=
//			"select t.balance_id,\n" + 
//			"       t.balance_type,\n" + 
//			"       t.balance_year_month,\n" + 
//			"       to_char(t.balance_date, 'yyyy-MM-dd'),\n" + 
//			"       t.trans_his_minid,\n" + 
//			"       t.trans_his_maxid,\n" + 
//			"       (select to_char(a.balance_date, 'yyyy-MM-dd')\n" + 
//			"          from inv_j_balance a\n" + 
//			"         where a.is_use='Y' and a.balance_id = t.balance_id + 1) as nextDate,\n" + 
//			"       (select to_char(a.balance_date, 'yyyy-MM-dd')\n" + 
//			"          from inv_j_balance a\n" + 
//			"         where a.is_use='Y' and a.balance_id = t.balance_id - 1) as lastDate,\n" + 
//			"       (select a.trans_his_minid\n" + 
//			"          from inv_j_balance a\n" + 
//			"         where a.is_use='Y' and a.balance_id = t.balance_id - 1) as lastmin,\n" + 
//			"       (select a.trans_his_maxid\n" + 
//			"          from inv_j_balance a\n" + 
//			"         where a.is_use='Y' and a.balance_id = t.balance_id - 1) as lastmax\n" + 
//			"  from inv_j_balance t\n" + 
//			" where t.balance_year_month = "+dateMonth+"\n" + 
//			"   and rownum = 1";
//		
//
//		List balanceList=bll.queryByNativeSQL(balanceSql);
//		
//		if(balanceList!=null&&balanceList.size()>0)
//		{
//			String lastMin="0";
//			String lastMax="0";
//			InvJBalance model=new InvJBalance();
//
//			Long lastId=0l;
//			Object [] objData=(Object [])balanceList.get(0);
//			if(objData[0]!=null)
//			{
//			 model.setBalanceId(Long.parseLong(objData[0].toString()));
//			 lastId=model.getBalanceId()-1;
//			}
//			if(objData[1]!=null)
//			{
//				model.setBalanceType(objData[1].toString());
//			}
//			if(objData[2]!=null)
//			{
//				model.setBalanceYearMonth(Long.parseLong(objData[2].toString()));
//			}
//
//			if(objData[4]!=null)
//			{
//				model.setTransHisMinid(Long.parseLong(objData[4].toString()));
//			}
//			if(objData[5]!=null)
//			{
//				model.setTransHisMaxid(Long.parseLong(objData[5].toString()));
//			}
//
//			if(objData[8]!=null)
//			{
//				lastMin=objData[8].toString();
//			}
//			if(objData[9]!=null)
//			{
//				lastMax=objData[9].toString();
//			}
//
//			//期初算法：如 12月的期初=11月的月结-11月月结前入库且11月月结前未审核的入库单+11月月结前出库且11月月结前未审核的出库单
//			
//			String sql=
//
//				"select ttt.class_no,\n" +
//				"       ttt.class_name,\n" + 
//				"       nvl(ttt.inqty, 0),\n" + 
//				"       nvl(qty2, 0) - nvl(lastInQty, 0) + nvl(lastOutQty, 0) as lastPrice,\n" + 
//				"       nvl(outqty, 0),\n" + 
//				"       nvl(qty2, 0) - nvl(lastInQty, 0) + nvl(lastOutQty, 0) +\n" + 
//				"       nvl(ttt.inqty, 0) - nvl(outqty, 0) thisprice\n" + 
//				" ,nvl(ttt.thisIn,0) \n"+ //add by fyyang 091210
//				"  from (\n" + 
//				"\n" + 
//				"        select m.class_no,\n" + 
//				"                m.class_name,\n" + 
//				"\n" + 
//				"                (\n" + 
//				"\n" + 
//				"                 select sum(round(t1.trans_qty * t1.price,2)) as qty1\n" + 
//				"                   from inv_j_transaction_his t1,\n" + 
//				"                         pur_j_arrival         t11,\n" + 
//				"                         inv_c_material        t12,\n" + 
//				"                         inv_c_material_class  t13\n" + 
//				"                  where t1.is_use='Y'  and  t11.is_use='Y'  and  t12.is_use='Y' and t13.is_use='Y' and t1.arrival_no = t11.arrival_no\n" + 
//				"                    and t1.trans_id = 1\n" + 
//				"                    and t11.check_date >\n" + 
//				"                        (select c.balance_date\n" + 
//				"                           from inv_j_balance c\n" + 
//				"                          where c.is_use='Y' and c.balance_id = "+lastId+")\n" + 
//				"                    and t11.check_date <\n" + 
//				"                        (select c.balance_date\n" + 
//				"                           from inv_j_balance c\n" + 
//				"                          where c.is_use='Y' and c.balance_id = "+model.getBalanceId()+")\n" + 
//				//"                    and (t11.check_state = '2' or t11.check_state = 'N')\n" +//modify by fyyang 20100202
//				"                    and t11.check_state = '2' \n" + 
//				"                    and t1.material_id = t12.material_id\n" + 
//				"                    and t12.maertial_class_id = t13.maertial_class_id\n" + 
//				"                    and t13.class_no like m.class_no || '%') as inqty,\n" + 
//				"                (select sum(round(t2.trans_qty * t2.std_cost,2))\n" + 
//				"                   from inv_j_transaction_his t2,\n" + 
//				"                        inv_c_material        t12,\n" + 
//				"                        inv_c_material_class  t13\n" + 
//				"                  where t2.is_use='Y' and t12.is_use='Y' and t13.is_use='Y' and t2.trans_id = 6\n" + 
//				"                    and t2.trans_his_id >= "+model.getTransHisMinid()+"\n" + 
//				"                    and t2.trans_his_id <= "+model.getTransHisMaxid()+"\n" + 
//				"                    and t2.material_id = t12.material_id\n" + 
//				"                    and t12.maertial_class_id = t13.maertial_class_id\n" + 
//				"                    and t13.class_no like m.class_no || '%') as qty2,\n" + 
////				"                (select sum(t5.trans_qty * t5.price)\n" + 
////				"                   from inv_j_transaction_his t5,\n" + 
////				"                        pur_j_arrival         t55,\n" + 
////				"                        inv_c_material        t12,\n" + 
////				"                        inv_c_material_class  t13\n" + 
////				"                  where t5.trans_id = 1\n" + 
////				"                    and t5.arrival_no = t55.arrival_no\n" + 
////				"                    and ((t55.last_modified_date <\n" + 
////				"                        (select c.balance_date\n" + 
////				"                             from inv_j_balance c\n" + 
////				"                            where c.balance_id = "+lastId+") and\n" + 
////				"                        ((t55.check_state <> '2' and t55.check_state <> 'N') or\n" + 
////				"                        t55.check_state is null)) or\n" + 
////				"                        (t55.check_date >\n" + 
////				"                        (select c.balance_date\n" + 
////				"                             from inv_j_balance c\n" + 
////				"                            where c.balance_id = "+lastId+") and\n" + 
////				"                        (t55.check_state = '2' or t55.check_state = 'N')) and t5.trans_his_id<"+lastMax+")\n" + 
////				"                    and t5.material_id = t12.material_id\n" + 
////				"                    and t12.maertial_class_id = t13.maertial_class_id\n" + 
////				"                    and t13.class_no like m.class_no || '%') as lastInQty,\n" + 
////				"                (select sum(t6.trans_qty * t6.std_cost)\n" + 
////				"                   from inv_j_transaction_his t6,\n" + 
////				"                        inv_j_issue_head      t66,\n" + 
////				"                        inv_c_material        t12,\n" + 
////				"                        inv_c_material_class  t13\n" + 
////				"                  where t6.trans_id = 4\n" + 
////				"                    and t6.order_no = t66.issue_no\n" + 
////				"                    and ((t66.last_modified_date <\n" + 
////				"                        (select c.balance_date\n" + 
////				"                             from inv_j_balance c\n" + 
////				"                            where c.balance_id = "+lastId+") and\n" + 
////				"                        (t66.issue_status <> 'C' or t66.issue_status is null)) or\n" + 
////				"                        (t66.checked_date >\n" + 
////				"                        (select c.balance_date\n" + 
////				"                             from inv_j_balance c\n" + 
////				"                            where c.balance_id = "+lastId+") and\n" + 
////				"                        t66.issue_status = 'C') and t6.trans_his_id<"+lastMax+")\n" + 
////				"                    and t6.material_id = t12.material_id\n" + 
////				"                    and t12.maertial_class_id = t13.maertial_class_id\n" + 
////				"                    and t13.class_no like m.class_no || '%'\n" + 
////				"\n" + 
////				"                 ) as lastOutQty,\n" + 
////------------------------------------modify by fyyang 20100111----------------------------
//
//				"(select sum(round(t.trans_qty * t.price,2))\n" +
//				"  from inv_j_transaction_his t, inv_c_material a, inv_c_material_class t13\n" + 
//				" where t.is_use='Y'  and a.is_use='Y' and t13.is_use='Y' and t.trans_his_id < "+model.getTransHisMinid()+"\n" + 
//				"   and t.material_id = a.material_id\n" + 
//				"   and a.maertial_class_id = t13.maertial_class_id\n" + 
//				"   and t.trans_id = 1\n" + 
//				"   and t13.class_no like  m.class_no || '%'\n" + 
//				"\n" + 
//				"   and t.trans_his_id not in\n" + 
//				"       (select t.trans_his_id\n" + 
//				"          from inv_j_transaction_his t,\n" + 
//				"               inv_c_material        a,\n" + 
//				"               pur_j_arrival         b,\n" + 
//				"               inv_c_material_class  t13\n" + 
//				"         where t.is_use='Y' and  a.is_use='Y' and  b.is_use='Y' and t13.is_use='Y' and \n" + 
//				"            t.material_id = a.material_id\n" + 
//				"           and t.arrival_no = b.arrival_no\n" + 
//				"           and a.maertial_class_id = t13.maertial_class_id\n" + 
//				"           and t13.class_no like  m.class_no || '%'\n" + 
//				"           and t.trans_id = 1\n" + 
//				//"           and (b.check_state = '2' or b.check_state = 'N')\n" +  //modify by fyyang 20100202
//				" and b.check_state = '2' \n "+
//				"           and b.check_date <= (select c.balance_date\n" + 
//				"                                  from inv_j_balance c\n" + 
//				"                                 where  c.is_use='Y' and c.balance_id = "+lastId+"))"+
//				") as lastInQty, \n"+
//
//				
////-----------------------------------------------------------------
//
//				"(select sum(round(t6.trans_qty * t6.std_cost,2))\n" +
//				"  from inv_j_transaction_his t6,\n" + 
//				"       inv_c_material        t12,\n" + 
//				"       inv_c_material_class  t13\n" + 
//				" where t6.is_use='Y' and t12.is_use='Y' and t13.is_use='Y' and t6.trans_id = 4\n" + 
//				"   and t6.trans_his_id < "+model.getTransHisMinid()+"\n" + 
//				"   and t6.material_id = t12.material_id\n" + 
//				"   and t12.maertial_class_id = t13.maertial_class_id\n" + 
//				"   and t13.class_no like m.class_no || '%'\n" + 
//				"   and t6.trans_his_id not in\n" + 
//				"       (select t6.trans_his_id\n" + 
//				"          from inv_j_transaction_his t6,\n" + 
//				//"               inv_j_issue_head      t66,\n" +  //modify by fyyang 20100202
//				"               inv_c_material        t12,\n" + 
//				"               inv_c_material_class  t13\n" + 
//				"         where t6.is_use='Y' and t12.is_use='Y'   and  t13.is_use='Y' and t6.trans_id = 4\n" + 
//				//"           and t6.order_no = t66.issue_no\n" + 
//				"\n" + 
//				//"           and t66.issue_status = 'C'\n" + 
//				"           and t6.check_status='C'\n" + 
//				//"           and t66.checked_date <=\n" + 
//				"           and t6.check_date<=\n" + 
//				"               (select c.balance_date\n" + 
//				"                  from inv_j_balance c\n" + 
//				"                 where c.is_use='Y' and c.balance_id = "+lastId+")\n" + 
//				"           and t6.material_id = t12.material_id\n" + 
//				"           and t12.maertial_class_id = t13.maertial_class_id\n" + 
//				"          and t13.class_no like m.class_no || '%')\n"+
//				") as lastOutQty,\n"+
//		
////-----------------------------------------------------------------				
//				
//				"                (\n" + 
//				"\n" + 
//				"                 select sum(round(t3.trans_qty * t3.std_cost,2))\n" + 
//				"                   from inv_j_transaction_his t3,\n" + 
//				//"                         inv_j_issue_head      t33,\n" + //modify by fyyang 20100202
//				"                         inv_c_material        t12,\n" + 
//				"                         inv_c_material_class  t13\n" + 
//				"                  where t3.is_use='Y' and  t12.is_use='Y' and t13.is_use='Y' and t3.trans_id = 4\n" + 
//				//"                    and t3.order_no = t33.issue_no\n" + 
//				//"                    and t33.checked_date >\n" + 
//				"                    and t3.check_date >\n" + 
//				"                        (select c.balance_date\n" + 
//				"                           from inv_j_balance c\n" + 
//				"                          where c.is_use='Y' and c.balance_id = "+lastId+")\n" + 
//				//"                    and t33.checked_date <\n" + 
//				"                    and t3.check_date <\n" + 
//				"                        (select c.balance_date\n" + 
//				"                           from inv_j_balance c\n" + 
//				"                          where c.is_use='Y' and c.balance_id = "+model.getBalanceId()+")\n" + 
//				//"                    and t33.issue_status = 'C'\n" + 
//				"                    and t3.check_status='C'\n" +
//				"                    and t3.material_id = t12.material_id\n" + 
//				"                    and t12.maertial_class_id = t13.maertial_class_id\n" + 
//				"                    and t13.class_no like m.class_no || '%') as outqty\n" + 
//				//-----------add by fyyang 091210--------
//
//				"        ,(\n" +
//				"select sum(round(t4.trans_qty * t4.price,2))\n" + 
//				"  from inv_j_transaction_his t4,\n" + 
//				"       inv_c_material        t41,\n" + 
//				"       inv_c_material_class  t42,\n" + 
//				"       pur_j_arrival         t43\n" + 
//				" where  t4.material_id = t41.material_id\n" + 
//				"   and t41.maertial_class_id = t42.maertial_class_id\n" + 
//				"   and t42.class_no like m.class_no || '%'\n" + 
//				"   and t4.trans_id = 1\n" + 
//				//"   and t4.trans_his_id >= "+model.getTransHisMinid()+"\n" +//modify by fyyang 20100113
//				"   and t4.trans_his_id <= "+model.getTransHisMaxid()+"\n" + 
//				"   and t4.arrival_no = t43.arrival_no\n" + 
//			//	"   and ((t43.check_state <> '2' and t43.check_state <> 'N') or\n" +  //modify by fyyang 20100202
//				"   and (t43.check_state <> '2'  or\n" +
//				"       t43.check_state is null)\n" + 
//				"   and t4.is_use = 'Y'\n" + 
//				"   and t41.is_use = 'Y'\n" + 
//				"   and t42.is_use = 'Y'  and t43.is_use='Y'\n" + 
//				"\n" + 
//				"        ) as thisIn \n"+
//
//				//--------------------
//				"          from inv_c_material_class m\n" + 
//				"         where length(m.class_no) = 3\n" + 
//				"           and m.class_no like '"+whsNo+"%'\n" + 
//				"           and m.is_use = 'Y') ttt   order by ttt.class_no asc\n" ; 
////				" where nvl(ttt.inqty, 0) <> 0\n" + 
////				"    or nvl(qty2, 0) - nvl(lastInQty, 0) + nvl(lastOutQty, 0) <> 0\n" + 
////				"    or nvl(outqty, 0) <> 0";
//
//		List list = bll.queryByNativeSQL(sql);
//		Double totalReceiptCost=0d;
//		Double totalIssueCost=0d;
//		Double totalTBCost=0d;
//		Double totalOBCost=0d;
//		Double totalZanShou=0d;
//		
//        Iterator it = list.iterator();
//        while (it.hasNext()) {
//        	WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
//            Object[] data = (Object[]) it.next();
//            // 类别编码
//            if(null != data[0])
//            monthInfo.setWhsNo(data[0].toString());
//            // 类别名称
//            if(null != data[1])
//            monthInfo.setWhsName(data[1].toString());
//            // 本期入库金额
//            if(null != data[2])
//            	// monthInfo.setReceiptCost(data[2].toString());
//            {
//                monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())));
//                totalReceiptCost+=Double.parseDouble(data[2].toString());
//            }
//            // 上期结存金额
//            if(null != data[3])
//            {
//            	 // monthInfo.setOBCost(data[3].toString());
////             monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())));
////            	 totalOBCost+=Double.parseDouble(data[3].toString());
//             if(dateMonth.equals("201001"))
//             {
//             	if(monthInfo.getWhsNo().equals("5-D"))
//             	{
//             		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())+56));
//             		 totalOBCost+=Double.parseDouble(data[3].toString())+56;
//             	}
//             	else if(monthInfo.getWhsNo().equals("5-N"))
//             	{
//             		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())+300));
//            		 totalOBCost+=Double.parseDouble(data[3].toString())+300;
//             	}
//             	else if(monthInfo.getWhsNo().equals("B-H"))
//             	{
//             		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())+200));
//            		 totalOBCost+=Double.parseDouble(data[3].toString())+200;
//             	}
//             	else if(monthInfo.getWhsNo().equals("4-1"))
//             	{
//             		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())-255));
//            		 totalOBCost+=Double.parseDouble(data[3].toString())-255;
//             	}
//             	else
//             	{
//             		 monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())));
//                     totalOBCost+=Double.parseDouble(data[3].toString());
//             	}
//             	
//             	
//             }
//             else  if(dateMonth.equals("201002"))
//             {
//            	 if(monthInfo.getWhsNo().equals("5-E"))
//              	{
//              		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())+2295.2649477));
//              		 totalOBCost+=Double.parseDouble(data[3].toString())+2295.2649477;
//              	}
//            	 else if(monthInfo.getWhsNo().equals("5-D"))
//               	{
//               		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())-380));
//               		 totalOBCost+=Double.parseDouble(data[3].toString())-380;
//               	}
//            	 else if(monthInfo.getWhsNo().equals("2-5"))
//                	{
//                		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())-1340));
//                		 totalOBCost+=Double.parseDouble(data[3].toString())-1340;
//                	}
//            	 else if(monthInfo.getWhsNo().equals("3-3"))
//             	{
//             		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())-24));
//             		 totalOBCost+=Double.parseDouble(data[3].toString())-24;
//             	}
//            	 else if(monthInfo.getWhsNo().equals("5-H"))
//              	{
//              		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())-450));
//              		 totalOBCost+=Double.parseDouble(data[3].toString())-450;
//              	}
//            	 else if(monthInfo.getWhsNo().equals("5-F"))
//               	{
//               		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())-856.781));
//               		 totalOBCost+=Double.parseDouble(data[3].toString())-856.781;
//               	}
//            	 else if(monthInfo.getWhsNo().equals("5-L"))
//                	{
//                		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())-408));
//                		 totalOBCost+=Double.parseDouble(data[3].toString())-408;
//                	}
//            	 else
//              	{
//              		 monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())));
//                      totalOBCost+=Double.parseDouble(data[3].toString());
//              	}
//             }
//             else
//             {
//            	 monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())));
//                 totalOBCost+=Double.parseDouble(data[3].toString());
//             }
//             
//             
//             
//            
//            }
//         // 本期出库金额
//            if(null != data[4])
//            {
//            	// monthInfo.setIssueCost(data[4].toString());
//               monthInfo.setIssueCost(dataOperate(Double.parseDouble(data[4].toString())));
//               totalIssueCost+=Double.parseDouble(data[4].toString());
//            }
//            // 本期结余金额
//            if(null != data[5])
//            {
//            	// monthInfo.setTBCost(data[5].toString());
//                monthInfo.setTBCost(dataOperate(Double.parseDouble(data[5].toString())));
//                totalTBCost+=Double.parseDouble(data[5].toString());
//            }
//            //本期暂收金额
//             if(null!=data[6])
//             {
//            	 monthInfo.setZanShou(dataOperate(Double.parseDouble(data[6].toString())));
//            	 totalZanShou+=Double.parseDouble(data[6].toString());
//             }
//            arrlist.add(monthInfo);
//        }
//        WarehouseThisMonthInfo entity = new WarehouseThisMonthInfo();
//        entity.setWhsNo("小计");
//       
//        
//        	entity.setOBCost(dataOperate(totalOBCost));
//        
//        entity.setReceiptCost(dataOperate(totalReceiptCost));
//        
//        entity.setIssueCost(dataOperate(totalIssueCost));
//        entity.setTBCost(dataOperate(totalTBCost));
//        entity.setZanShou(dataOperate(totalZanShou));
//        arrlist.add(entity);
//        
//		}
//		return arrlist;
//	}
//	}
//    
	/**
     * 材料收发统计报表明细信息
     * modify by fyyang 090811
     * @return List<WarehouseThisMonthInfo>
     * add by ywliu 2009/7/4
     */
//	@SuppressWarnings("unchecked")
//	public List<WarehouseThisMonthInfo> getThisMonthInfoByWarehouse(String whsNo,String dateMonth) {
//		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
//		//String dateMonth="200907";
//		String balanceSql=
//			"select t.balance_id,\n" + 
//			"       t.balance_type,\n" + 
//			"       t.balance_year_month,\n" + 
//			"       to_char(t.balance_date, 'yyyy-MM-dd'),\n" + 
//			"       t.trans_his_minid,\n" + 
//			"       t.trans_his_maxid,\n" + 
//			"       (select to_char(a.balance_date, 'yyyy-MM-dd')\n" + 
//			"          from inv_j_balance a\n" + 
//			"         where a.balance_id = t.balance_id + 1) as nextDate,\n" + 
//			"       (select to_char(a.balance_date, 'yyyy-MM-dd')\n" + 
//			"          from inv_j_balance a\n" + 
//			"         where a.balance_id = t.balance_id - 1) as lastDate,\n" + 
//			"       (select a.trans_his_minid\n" + 
//			"          from inv_j_balance a\n" + 
//			"         where a.balance_id = t.balance_id - 1) as lastmin,\n" + 
//			"       (select a.trans_his_maxid\n" + 
//			"          from inv_j_balance a\n" + 
//			"         where a.balance_id = t.balance_id - 1) as lastmax\n" + 
//			"  from inv_j_balance t\n" + 
//			" where t.balance_year_month = "+dateMonth+"\n" + 
//			"   and rownum = 1";
//
//
//		List balanceList=bll.queryByNativeSQL(balanceSql);
//		
//		if(balanceList!=null&&balanceList.size()>0)
//		{
//			InvJBalance model=new InvJBalance();
//			String thisDate="";
//			String lastDate="0000-00-00";
//			String nextDate="0000-00-00";
//			String lastMin="0";
//			String lastMax="0";
//			Long lastId=0l;
//			Object [] objData=(Object [])balanceList.get(0);
//			if(objData[0]!=null)
//			{
//			 model.setBalanceId(Long.parseLong(objData[0].toString()));
//			 lastId=model.getBalanceId()-1;
//			}
//			if(objData[1]!=null)
//			{
//				model.setBalanceType(objData[1].toString());
//			}
//			if(objData[2]!=null)
//			{
//				model.setBalanceYearMonth(Long.parseLong(objData[2].toString()));
//			}
//			if(objData[3]!=null)
//			{
//				thisDate=objData[3].toString();
//			}
//			if(objData[4]!=null)
//			{
//				model.setTransHisMinid(Long.parseLong(objData[4].toString()));
//			}
//			if(objData[5]!=null)
//			{
//				model.setTransHisMaxid(Long.parseLong(objData[5].toString()));
//			}
//			if(objData[6]!=null)
//			{
//				nextDate=objData[6].toString();
//			}
//			if(objData[7]!=null)
//			{
//				lastDate=objData[7].toString();
//			}
//			if(objData[8]!=null)
//			{
//				lastMin=objData[8].toString();
//			}
//			if(objData[9]!=null)
//			{
//				lastMax=objData[9].toString();
//			}
//			
////			String sql=
////				"select getwhsname('"+whsNo+"'),m.material_no,m.material_name,m.spec_no,GETUNITNAME(m.stock_um_id),\n" +
////				"nvl(ttt.stdCost,'0'),nvl(ttt.lastqty,'0'), nvl(ttt.lastqty*ttt.lastStdCost,'0'),nvl(ttt.inqty,'0'),nvl(ttt.inqty*ttt.stdCost,'0'), " +
////				"nvl(ttt.outqty,'0'),nvl(ttt.outqty*ttt.stdCost,'0'),nvl(ttt.thisqty,'0'),nvl(ttt.thisqty*ttt.stdCost,'0') \n" +
////				"from\n" + 
////				"(\n" + 
////				"\n" + 
////				"select tt4.material_id,\n" + 
////				"       tt1.qty1 as inqty,\n" + 
////				"       tt2.qty2 as lastqty,\n" + 
////				"       tt3.qty3 as outqty,\n" + 
////				"       tt4.qty4 as thisqty,\n" + 
////				"       (select b.std_cost\n" + 
////				"          from inv_c_material b\n" + 
////				"         where b.material_id = tt4.material_id) as stdCost,\n" + // modify by ywliu 20091102
////				"       (select c.std_cost\n" + 
////				"          from inv_j_transaction_his c\n" + 
////				"         where c.trans_his_id =\n" + 
////				"               (select max(d.trans_his_id)\n" + 
////				"                  from inv_j_transaction_his d\n" + 
////				"                 where d.trans_id = 6\n" + // modify by ywliu 20091102
////				"                   and d.trans_his_id >= "+model.getTransHisMinid()+"\n" + // modify by ywliu 20091102
////				"                   and d.trans_his_id <= "+model.getTransHisMaxid()+"\n" + // modify by ywliu 20091102
////				"                   and tt4.material_id = d.material_id )) as lastStdCost\n" + 
////				"  from (select t1.material_id, sum(t1.trans_qty) as qty1\n" + 
////				"          from inv_j_transaction_his t1\n" + 
////				"         where t1.trans_his_id >= "+model.getTransHisMinid()+"\n" + 
////				"           and t1.trans_his_id <= "+model.getTransHisMaxid()+"\n" + 
////				"           and t1.trans_id in (1, 9)\n" + 
////				"           and t1.from_whs_no='"+whsNo+"'\n" + 
////				"         group by t1.material_id,t1.from_whs_no) tt1,\n" + 
////				"\n" + 
////				"       (select t2.material_id, sum(t2.trans_qty) qty2\n" + 
////				"          from inv_j_transaction_his t2\n" + 
////				"         where t2.trans_id = 6\n" + 
////				"         and to_char(t2.last_modified_date,'yyyy-MM-dd')='"+lastDate+"' \n"+
////				"         group by t2.material_id) tt2,\n" + 
////				"\n" + 
////				"       (select t3.material_id, sum(t3.trans_qty) qty3\n" + 
////				"          from inv_j_transaction_his t3\n" + 
////				"         where t3.trans_his_id >= "+model.getTransHisMinid()+"\n" + 
////				"           and t3.trans_his_id <= "+model.getTransHisMaxid()+"\n" + 
////				"           and t3.from_whs_no='"+whsNo+"'\n" + 
////				"           and t3.trans_id = 4\n" + 
////				"         group by t3.material_id,t3.from_whs_no) tt3,\n" + 
////				"\n" + 
////				"         (select t4.material_id, sum(t4.trans_qty) qty4\n" + 
////				"          from inv_j_transaction_his t4\n" + 
////				"         where t4.trans_id = 6\n" + 
////				"         and to_char(t4.last_modified_date,'yyyy-MM-dd')='"+thisDate+"' \n"+
////				"         group by t4.material_id\n" + 
////				"         ) tt4\n" + 
////				" where tt4.material_id = tt2.material_id(+)\n" + 
////				"   and tt4.material_id = tt3.material_id(+)\n" + 
////				"   and  tt4.material_id=tt1.material_id(+)\n" + 
////				"\n" + 
////				"   ) ttt,inv_c_material m\n" + 
////				"   where ttt.material_id=m.material_id  and m.default_whs_no='"+whsNo+"' ";
//
//			
//		
////
////		
//////		String sql = "select getwhsname(b.whs_no) as a1,\n" +
//////				"       a.material_no as a2,\n" + 
//////				"       a.material_name as a3,\n" + 
//////				"       a.spec_no as a4,\n" + 
//////				"       getunitname(a.stock_um_id) as a5,\n" + 
//////				"       a.std_cost as a6,\n" + 
//////				"       b.open_balance as a7,\n" + 
//////				"       b.open_balance * a.std_cost as ob_cost,\n" + 
//////				"       b.receipt as a8,\n" + 
//////				"       b.receipt * a.std_cost as r_cost,\n" + 
//////				"       b.issue,\n" + 
//////				"       b.issue * a.std_cost as i_cost,\n" + 
//////				"       b.open_balance+b.receipt-b.issue as this_balance,\n" + 
//////				"       b.open_balance * a.std_cost+b.receipt * a.std_cost-b.issue * a.std_cost as this_cost,\n" + 
//////				"       to_char(b.last_modified_date ,'YYYY-MM-DD')\n" + 
//////				"  from inv_c_material a, inv_j_lot b\n" + 
//////				" where a.material_id = b.material_id and b.whs_no = '" + whsNo +
//////				"'\n";
//			String sql=
//				"select getwhsname('" + whsNo +"'),\n" +
//				"       m.material_no,\n" + 
//				"       m.material_name,\n" + 
//				"       m.spec_no,\n" + 
//				"       GETUNITNAME(m.stock_um_id),\n" + 
//				"       nvl(ttt.stdCost, '0'),\n" + 
//				"       nvl(ttt.lastqty, '0'),\n" + 
//				"       nvl(ttt.lastprice, '0'),\n" + 
//				"       nvl(ttt.inqty, '0'),\n" + 
//				"       nvl(ttt.inprice, '0'),\n" + 
//				"       nvl(ttt.outqty, '0'),\n" + 
//				"       nvl(ttt.outprice, '0'),\n" + 
//				"       nvl(ttt.lastqty, '0')+ nvl(ttt.inqty, '0')-nvl(ttt.outqty, '0'),\n" + 
//				"        nvl(ttt.lastprice, '0')+nvl(ttt.inprice, '0')-nvl(ttt.outprice, '0')\n" + 
//				"  from (\n" + 
//				"\n" + 
//				"        select tt4.material_id,\n" + 
//				"                tt1.qty1 as inqty,\n" + 
//				"                 tt2.qty2 - nvl(tt5.qty5, 0) +\n" + 
//				"                        nvl(tt6.qty6, 0) as lastqty,\n" + 
//				"                 tt2.price2 - nvl(tt5.price5, 0) +\n" + 
//				"                        nvl(tt6.price6, 0) as lastprice,\n" + 
//				"                tt3.qty3 as outqty,\n" + 
//				"                tt4.qty4 as thisqty,\n" + 
//				"                tt1.price1 as inprice,\n" + 
//				"                 tt3.price3 as outprice,\n" + 
//				"                (select b.std_cost\n" + 
//				"                   from inv_c_material b\n" + 
//				"                  where b.material_id = tt4.material_id) as stdCost,\n" + 
//				"                (select c.std_cost\n" + 
//				"                   from inv_j_transaction_his c\n" + 
//				"                  where  c.is_use='Y' and c.trans_his_id =\n" + 
//				"                        (select max(d.trans_his_id)\n" + 
//				"                           from inv_j_transaction_his d\n" + 
//				"                          where d.is_use='Y' and d.trans_id = 6\n" + 
//				"                            and d.trans_his_id >= "+model.getTransHisMinid()+"\n" + 
//				"                            and d.trans_his_id <= "+model.getTransHisMaxid()+"\n" + 
//				"                            and tt4.material_id = d.material_id)) as lastStdCost\n" + 
//				"          from (select t1.material_id,\n" + 
//				"                        sum(round(t1.trans_qty * t1.price,2)) as price1,\n" + 
//				"                        sum(round(t1.trans_qty,2)) as qty1\n" + 
//				"                   from inv_j_transaction_his t1, pur_j_arrival t11\n" + 
//				"                  where t1.is_use='Y' and t1.arrival_no = t11.arrival_no\n" + 
//				"                    and t1.trans_id = 1\n" + 
//				"                    and t11.check_date >\n" + 
//				"                        (select c.balance_date\n" + 
//				"                           from inv_j_balance c\n" + 
//				"                          where c.is_use='Y' and c.balance_id = "+lastId+")\n" + 
//				"                    and t11.check_date <\n" + 
//				"                        (select c.balance_date\n" + 
//				"                           from inv_j_balance c\n" + 
//				"                          where c.is_use='Y' and c.balance_id = "+model.getBalanceId()+")\n" + 
//				//"                    and (t11.check_state = '2' or t11.check_state = 'N')\n" + //modify by fyyang 20100202
//				"                    and t11.check_state = '2' \n" +
//				"                    and t1.from_whs_no = '" + whsNo +"'\n" + 
//				"                  group by t1.material_id, t1.from_whs_no) tt1,\n" + 
//				"\n" + 
//				"                (select t2.material_id,\n" + 
//				"                        sum(round(t2.trans_qty,2)) qty2,\n" + 
//				"                        sum(round(t2.trans_qty * t2.std_cost,2)) price2\n" + 
//				"                   from inv_j_transaction_his t2\n" + 
//				"                  where t2.is_use='Y' and t2.trans_id = 6\n" + 
//				"                    and to_char(t2.last_modified_date, 'yyyy-MM-dd') =\n" + 
//				"                        '"+lastDate+"'\n" + 
//				"                  group by t2.material_id) tt2,\n" + 
//				"\n" + 
//				"                (select t3.material_id,\n" + 
//				"                                sum(round(t3.trans_qty * t3.std_cost,2)) price3,sum(round(t3.trans_qty,2)) qty3\n" + 
//				"                           from inv_j_transaction_his t3 \n" + // , inv_j_issue_head t66 modify by ywliu 20100201
//				"                          where  t3.is_use='Y' and t3.trans_id = 4\n" + 
////				"                            and t3.order_no = t33.issue_no\n" + 
//				"                            and t3.check_date >\n" + 
//				"                                (select c.balance_date\n" + 
//				"                                   from inv_j_balance c\n" + 
//				"                                  where c.is_use='Y' and c.balance_id = "+lastId+")\n" + 
//				"                            and t3.check_date <\n" + 
//				"                                (select c.balance_date\n" + 
//				"                                   from inv_j_balance c\n" + 
//				"                                  where c.is_use='Y' and c.balance_id = "+model.getBalanceId()+")\n" + 
//				"                            and t3.check_status = 'C'\n" + 
//				"                    and t3.from_whs_no = '" + whsNo +"'\n" + 
//				"                  group by t3.material_id, t3.from_whs_no) tt3,\n" + 
//				"\n" + 
//				"                (select t4.material_id, sum(round(t4.trans_qty,2)) qty4\n" + 
//				"                   from inv_j_transaction_his t4\n" + 
//				"                  where t4.is_use='Y' and t4.trans_id = 6\n" + 
//				"                    and to_char(t4.last_modified_date, 'yyyy-MM-dd') =\n" + 
//				"                        '"+thisDate+"'\n" + 
//				"                  group by t4.material_id) tt4,\n" + 
////				"                  (select t5.material_id,\n" + 
////				"                                sum(t5.trans_qty * t5.price)  price5,sum(t5.trans_qty) qty5\n" + 
////				"                           from inv_j_transaction_his t5, pur_j_arrival t55\n" + 
////				"                          where t5.trans_id = 1\n" + 
////				"                            and t5.arrival_no = t55.arrival_no\n" + 
////				"                            and ((t55.last_modified_date <\n" + 
////				"                                (select c.balance_date\n" + 
////				"                                     from inv_j_balance c\n" + 
////				"                                    where c.balance_id = "+lastId+") and\n" + 
////				"                                ((t55.check_state <> '2' and\n" + 
////				"                                t55.check_state <> 'N')  or t55.check_state is null )) or\n" + 
////				"                                (t55.check_date >\n" + 
////				"                                (select c.balance_date\n" + 
////				"                                     from inv_j_balance c\n" + 
////				"                                    where c.balance_id = "+lastId+") and\n" + 
////				"                                (t55.check_state = '2' or\n" + 
////				"                                t55.check_state = 'N')) and    t5.trans_his_id <"+lastMax+")\n" + 
////				"                                and t5.from_whs_no='" + whsNo +"'\n" + 
////				"                          group by t5.material_id,t5.from_whs_no\n" + 
////				"\n" + 
////				"                         ) tt5,\n" + 
//				//-----------------------modify by fyyang 20100118---------------------------------------------------------
//
//				"(select t5.material_id,\n" +
//				"       sum(round(t5.trans_qty,2)) qty5,\n" + 
//				"       sum(round(t5.trans_qty * t5.price,2)) price5\n" + 
//				"  from inv_j_transaction_his t5\n" + 
//				" where t5.is_use='Y' and t5.trans_his_id < "+model.getTransHisMinid()+"\n" + 
//				"   and t5.trans_id = 1\n" + 
//				"   and t5.from_whs_no = '" + whsNo +"'\n" + 
//				"   and t5.trans_his_id not in\n" + 
//				"       (select t.trans_his_id\n" + 
//				"          from inv_j_transaction_his t, pur_j_arrival b\n" + 
//				"         where t.is_use='Y' and b.is_use='Y' and t.arrival_no = b.arrival_no\n" + 
//				"           and t.trans_id = 1\n" + 
//				"           and t.from_whs_no = '" + whsNo +"'\n" + 
//				//"           and (b.check_state = '2' or b.check_state = 'N')\n" + //modify by fyyang 20100202
//				"           and b.check_state = '2' \n" +
//				"           and b.check_date <= (select c.balance_date\n" + 
//				"                                  from inv_j_balance c\n" + 
//				"                                 where c.is_use='Y' and c.balance_id = "+lastId+"))\n" + 
//				" group by t5.material_id, t5.from_whs_no ) tt5,\n"+
//				
//
//				"(select t6.material_id, sum(round(t6.trans_qty,2)) qty6, sum(round(t6.trans_qty * t6.std_cost,2)) price6\n" +
//				"  from inv_j_transaction_his t6\n" + 
//				" where t6.is_use='Y' and t6.trans_id = 4\n" + 
//				"   and t6.trans_his_id <"+model.getTransHisMinid()+"\n" + 
//				"   and t6.from_whs_no = '" + whsNo +"'\n" + 
//				"   and t6.trans_his_id not in\n" + 
//				"       (select t61.trans_his_id\n" + 
//				"          from inv_j_transaction_his t61\n" + // modify by ywliu 20100201 , inv_j_issue_head t66
//				"         where  t61.is_use='Y' and t61.trans_id = 4\n" + 
////				"           and t61.order_no = t66.issue_no\n" + 
//				"           and t61.from_whs_no = '" + whsNo +"'\n" + 
//				"           and t61.check_status = 'C'\n" + 
//				"           and t61.check_date <=\n" + 
//				"               (select c.balance_date\n" + 
//				"                  from inv_j_balance c\n" + 
//				"                 where c.is_use='Y' and c.balance_id = "+lastId+"))\n" + 
//				" group by t6.material_id, t6.from_whs_no) tt6\n"+
//
//
//				//--------------------------------------------------------------------------------
////				"                        (select t6.material_id,\n" + 
////				"                                sum(t6.trans_qty * t6.std_cost) price6,sum(t6.trans_qty) qty6\n" + 
////				"                           from inv_j_transaction_his t6, inv_j_issue_head t66\n" + 
////				"                          where t6.trans_id = 4\n" + 
////				"                            and t6.order_no = t66.issue_no\n" + 
////				"                            and ((t66.last_modified_date <\n" + 
////				"                                (select c.balance_date\n" + 
////				"                                     from inv_j_balance c\n" + 
////				"                                    where c.balance_id = "+lastId+") and\n" + 
////				"                                (t66.issue_status <> 'C' or t66.issue_status is null)) or\n" + 
////				"                                (t66.checked_date >\n" + 
////				"                                (select c.balance_date\n" + 
////				"                                     from inv_j_balance c\n" + 
////				"                                    where c.balance_id = "+lastId+") and\n" + 
////				"                                t66.issue_status = 'C') and    t6.trans_his_id <"+lastMax+")\n" + 
////				"                                and t6.from_whs_no='" + whsNo +"'\n" + 
////				"                          group by t6.material_id,t6.from_whs_no\n" + 
////				"\n" + 
////				"                         ) tt6\n" + 
//				"         where tt4.material_id = tt2.material_id(+)\n" + 
//				"           and tt4.material_id = tt3.material_id(+)\n" + 
//				"           and tt4.material_id = tt1.material_id(+)\n" + 
//				"           and tt4.material_id = tt5.material_id(+)\n" + 
//				"           and tt4.material_id = tt6.material_id(+)\n" + 
//				"\n" + 
//				"        ) ttt,\n" + 
//				"       inv_c_material m\n" + 
//				" where m.is_use='Y' and ttt.material_id = m.material_id\n" + 
//				"   and m.default_whs_no = '" + whsNo +"'"
//				+"  order by m.material_no  asc";
//
//			
//		List list = bll.queryByNativeSQL(sql);
//	
//        Iterator it = list.iterator();
//        while (it.hasNext()) {
//        	WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
//            Object[] data = (Object[]) it.next();
//            // 仓库名称
//            if(null != data[0])
//            monthInfo.setWhsName(data[0].toString());
//            // 物资编码
//            if(null != data[1])
//                monthInfo.setMaterialNo(data[1].toString());
//            // 物资名称
//            if(null != data[2])
//                monthInfo.setMaterialName(data[2].toString());
//            // 规格型号
//            if(null != data[3])
//                monthInfo.setSpecNo(data[3].toString());
//            // 单位
//            if(null != data[4])
//                monthInfo.setPurUm(data[4].toString());
//            // 计划单价
//            if(null != data[5])
//                monthInfo.setStdCost(data[5].toString());
//            // 上期结存数
//            if(null != data[6])
//                monthInfo.setOpenBalance(data[6].toString());
//            // 上期结存金额
//            if(null != data[7])
//                monthInfo.setOBCost(data[7].toString());
//            // 本期入库数
//            if(null != data[8])
//                monthInfo.setReceipt(data[8].toString());
//            // 本期入库金额
//            if(null != data[9])
//                monthInfo.setReceiptCost(data[9].toString());
//            // 本期支出数量
//            if(null != data[10])
//                monthInfo.setIssue(data[10].toString());
//            // 本期支出金额
//            if(null != data[11])
//                monthInfo.setIssueCost(data[11].toString());
//            // 本期结余数量
//            if(null != data[12])
//                monthInfo.setThisBalance(data[12].toString());
//            // 本期结余金额
//            if(null != data[13])
//                monthInfo.setTBCost(data[13].toString());
//            // 统计日期
////            if(null != data[14]) {
////            	if(Long.valueOf(data[14].toString().substring(data[14].toString().length()-2,data[14].toString().length())) < 26) {
////            		monthInfo.setMonth(data[14].toString().substring(5, 7));
////            	} else {
////            		monthInfo.setMonth(data[14].toString());
////            	}
////            	
////            }
//            monthInfo.setMonth(thisDate);
//            
//            arrlist.add(monthInfo);
//        }
//        
//	}
//		return arrlist;
//	}
	
    /**
     * add by fyyang 20100309 材料收发报表仓库月份查询
     * 过滤掉本期未发生且无结存的物资 modify by fyyang 20100318
     */
	public List<WarehouseThisMonthInfo> getThisMonthInfoByWarehouse(String whsNo,String dateMonth) {
		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
		//String dateMonth="200907";
		String balanceSql=
			"select t.balance_id\n" + 
			"  from inv_j_balance t\n" + 
			" where t.balance_year_month = "+dateMonth+"\n" + 
			"   and rownum = 1";
       Long balanceId=Long.parseLong(bll.getSingal(balanceSql).toString());


			

			String sql=
				"select getwhsname(t.whs_no),\n" +
				"       a.material_no,\n" + 
				"       a.material_name,\n" + 
				"       a.spec_no,\n" + 
				"       getunitname(a.stock_um_id),\n" + 
				"       t.std_cost,\n" + 
				"       nvl(t.openbalance_qty,0),\n" + 
				"       nvl(t.openbalance_price,0),\n" + 
				"       nvl(t.check_in_qty,0),\n" + 
				"       nvl(t.check_in_price,0),\n" + 
				"       nvl(t.check_out_qty,0),\n" + 
				"       nvl(t.check_out_price,0),\n" + 
				"       nvl(t.openbalance_qty, 0) + nvl(t.check_in_qty, 0) -\n" + 
				"       nvl(t.check_out_qty, 0),\n" + 
				"       nvl(t.openbalance_price, 0) + nvl(t.check_in_price, 0) -\n" + 
				"       nvl(t.check_out_price, 0)\n" + 
				"\n" + 
				"  from inv_j_transaction_his_money t, inv_c_material a\n" + 
				" where t.balance_id = "+balanceId+"\n" + 
				"   and t.material_id = a.material_id\n" + 
				"  and t.whs_no='"+whsNo+"' \n"+
				"   and t.is_use = 'Y' \n"+
				"  and ( nvl(t.check_in_price,0)<>0  \n"+
				"  or  nvl(t.check_out_price,0)<>0 \n"+
				"  or nvl(t.openbalance_price, 0) + nvl(t.check_in_price, 0) - nvl(t.check_out_price, 0) <> 0)";


			
		List list = bll.queryByNativeSQL(sql);
	
        Iterator it = list.iterator();
        while (it.hasNext()) {
        	WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
            Object[] data = (Object[]) it.next();
            // 仓库名称
            if(null != data[0])
            monthInfo.setWhsName(data[0].toString());
            // 物资编码
            if(null != data[1])
                monthInfo.setMaterialNo(data[1].toString());
            // 物资名称
            if(null != data[2])
                monthInfo.setMaterialName(data[2].toString());
            // 规格型号
            if(null != data[3])
                monthInfo.setSpecNo(data[3].toString());
            // 单位
            if(null != data[4])
                monthInfo.setPurUm(data[4].toString());
            // 计划单价
            if(null != data[5])
                monthInfo.setStdCost(data[5].toString());
            // 上期结存数
            if(null != data[6])
                monthInfo.setOpenBalance(data[6].toString());
            // 上期结存金额
            if(null != data[7])
                monthInfo.setOBCost(data[7].toString());
            // 本期入库数
            if(null != data[8])
                monthInfo.setReceipt(data[8].toString());
            // 本期入库金额
            if(null != data[9])
                monthInfo.setReceiptCost(data[9].toString());
            // 本期支出数量
            if(null != data[10])
                monthInfo.setIssue(data[10].toString());
            // 本期支出金额
            if(null != data[11])
                monthInfo.setIssueCost(data[11].toString());
            // 本期结余数量
            if(null != data[12])
                monthInfo.setThisBalance(data[12].toString());
            // 本期结余金额
            if(null != data[13])
                monthInfo.setTBCost(data[13].toString());

            monthInfo.setMonth(dateMonth);
            
            arrlist.add(monthInfo);
        }
        
	
		return arrlist;
	}
	
	/**
	 *  材料差异报表信息
	 */
	@SuppressWarnings("unchecked")
	public MaterialDiscrepancyForReport getMaterialDiscrepancyInfo(String dateMonth)
	{
		MaterialDiscrepancyForReport reportModel=new MaterialDiscrepancyForReport();
		String balanceSql=
			"select t.balance_id,\n" + 
			"       t.balance_type,\n" + 
			"       t.balance_year_month,\n" + 
			"       t.trans_his_minid,\n" + 
			"       t.trans_his_maxid,\n" + 
			"       (select to_char(a.balance_date, 'yyyy-MM-dd')\n" + 
			"          from inv_j_balance a\n" + 
			"         where a.balance_id = t.balance_id - 1) as lastDate,\n" + 
			"       (select a.trans_his_minid\n" + 
			"          from inv_j_balance a\n" + 
			"         where a.balance_id = t.balance_id - 1) as lastmin,\n" + 
			"       (select a.trans_his_maxid\n" + 
			"          from inv_j_balance a\n" + 
			"         where a.balance_id = t.balance_id - 1) as lastmax\n" + 
			"  from inv_j_balance t\n" + 
			" where t.balance_year_month = "+dateMonth+"\n" + 
			"   and rownum = 1";


		List balanceList=bll.queryByNativeSQL(balanceSql);
		if(balanceList != null && balanceList.size() > 0)
		{
			InvJBalance model=new InvJBalance();
			String lastDate="0000-00-00";
			String lastMin="0";
			String lastMax="0";
			Object [] objData=(Object [])balanceList.get(0);
			if(objData[0]!=null)
			{
			 model.setBalanceId(Long.parseLong(objData[0].toString()));
			}
			if(objData[1]!=null)
			{
				model.setBalanceType(objData[1].toString());
			}
			if(objData[2]!=null)
			{
				model.setBalanceYearMonth(Long.parseLong(objData[2].toString()));
			}
			
			if(objData[3]!=null)
			{
				model.setTransHisMinid(Long.parseLong(objData[3].toString()));
			}
			if(objData[4]!=null)
			{
				model.setTransHisMaxid(Long.parseLong(objData[4].toString()));
			}
			
			if(objData[5]!=null)
			{
				lastDate=objData[5].toString();
			}
			if(objData[6]!=null)
			{
				lastMin=objData[6].toString();
			}
			if(objData[7]!=null)
			{
				lastMax=objData[7].toString();
			}
			
//			String sqlReceipt=
//				"select sum(a.price / (1 + a.tax_rate) * a.trans_qty),\n" +
//				"       sum(a.price * a.trans_qty),\n" + 
//				"       sum(a.price / (1 + a.tax_rate) * a.trans_qty) -\n" + 
//				"       sum(a.price * a.trans_qty)\n" + 
//				"  from inv_j_transaction_his a\n" + 
//				" where a.trans_id=1\n" + 
//				"   and a.trans_his_id >="+model.getTransHisMinid()+" and a.trans_his_id<="+model.getTransHisMaxid();
			Long lastBalanceId=model.getBalanceId()-1;
			String sqlReceipt=
				"select sum(a.price / (1 + a.tax_rate) * a.trans_qty),\n" +
				"       sum(a.price * a.trans_qty),\n" + 
				"       sum(a.price / (1 + a.tax_rate) * a.trans_qty) -\n" + 
				"       sum(a.price * a.trans_qty)\n" + 
				"  from inv_j_transaction_his a,pur_j_arrival b\n" + 
				" where a.trans_id=1\n" + 
				"   and b.check_date>( select c.balance_date from inv_j_balance c where  c.balance_id="+lastBalanceId+")\n" + 
				"     and b.check_date<( select c.balance_date from inv_j_balance c where c.balance_id="+model.getBalanceId()+")\n" + 
				"   and a.arrival_no=b.arrival_no\n" + 
				//"   and  (b.check_state='2' or b.check_state='N')"; //modify by fyyang 20100202
				"   and  b.check_state='2' ";
			List receiptList=bll.queryByNativeSQL(sqlReceipt);
		
			if(receiptList!=null&&receiptList.size()>0)
			{
			   Object [] obj= (Object [])receiptList.get(0);
			   if (obj[1] != null) {
					reportModel.setOriReceipt(this.formatNumber(obj[1].toString()));
				}
			   else 
				reportModel.setOriReceipt("0.00");
			   
			   if (obj[2] != null) {
					reportModel.setDisReceipt(this.formatNumber(obj[2].toString()));
				}
			   else 
				   reportModel.setDisReceipt("0.00");
			}
			
//			String sqlBalance=
//				"select sum(lastStdCost * qty)\n" +
//				"  from (select (select c.std_cost\n" + 
//				"                  from inv_j_transaction_his c\n" + 
//				"                 where c.trans_his_id =\n" + 
//				"                       (select max(d.trans_his_id)\n" + 
//				"                          from inv_j_transaction_his d\n" + 
//				"                         where d.trans_id = 1\n" + 
//				"                           and d.trans_his_id >= "+lastMin+"\n" + 
//				"                           and d.trans_his_id <= "+lastMax+"\n" + 
//				"                           and tt1.material_id = d.material_id)) as lastStdCost,\n" + 
//				"               tt1.qty\n" + 
//				"          from (select t2.material_id, sum(t2.trans_qty) qty\n" + 
//				"                  from inv_j_transaction_his t2\n" + 
//				"                 where t2.trans_id = 6\n" + 
//				"                   and to_char(t2.last_modified_date, 'yyyy-MM-dd') =\n" + 
//				"                       '"+lastDate+"'\n" + 
//				"                 group by t2.material_id) tt1\n" + 
//				"\n" + 
//				"        )";
			String sqlBalance=
				"select sum(t.trans_qty*t.std_cost),sum(t.std_cost / (1 + 0.17) * t.trans_qty),sum(t.std_cost / (1 + 0.17) * t.trans_qty)-sum(t.trans_qty*t.std_cost)  from inv_j_transaction_his t\n" +
				"where t.trans_his_id>="+model.getTransHisMinid()+" and t.trans_his_id<="+model.getTransHisMaxid()+"\n" + 
				"and t.trans_id=6";
			 List objOpenBalance=bll.queryByNativeSQL(sqlBalance);
			 Iterator itOpenBalance=objOpenBalance.iterator();
			 while(itOpenBalance.hasNext())
			 {
				 Object [] data=(Object [])itOpenBalance.next();
				 // modified by liuyi 091126 格式化数据，截断四位小数
//				 if(data[0]!=null) reportModel.setOriOpenBalance(data[0].toString());
//				 if(data[2]!=null) reportModel.setDisOpenBalance(data[2].toString());
				 if(data[0]!=null) reportModel.setOriOpenBalance(this.formatNumber(data[0].toString()));
				 if(data[2]!=null) reportModel.setDisOpenBalance(this.formatNumber(data[2].toString()));
			 
			 }
			 if(reportModel.getDisOpenBalance() == null || reportModel.getDisOpenBalance().equals(""))
				 reportModel.setDisOpenBalance("0.00");
			 if(reportModel.getOriOpenBalance() == null || reportModel.getOriOpenBalance().equals(""))
				 reportModel.setOriOpenBalance("0.00");
			 Double fla = Double.parseDouble(reportModel.getDisOpenBalance()) + Double.parseDouble(reportModel.getDisReceipt());
			 reportModel.setDisTotal(this.formatNumber(fla.toString()));
			 
			 Double ofl = Double.parseDouble(reportModel.getOriOpenBalance()) + Double.parseDouble(reportModel.getOriReceipt());
			 reportModel.setOriTotal(this.formatNumber(ofl.toString()));
			 
			 // modified by liuyi 091126  数值格式化后，默认值为0.0
//			 if(!reportModel.getOriTotal().equals("0.00"))
			 if(!reportModel.getOriTotal().equals("0.0"))
			 {
				 Double d = Double.parseDouble(reportModel.getDisTotal()) / Double.parseDouble(reportModel.getOriTotal());
				 Double das = Math.rint(d * 100000000) /100000000;
				 reportModel.setDisRate(das.toString()); 
			 }
			 else 
				 reportModel.setDisRate("0.00");
			 
//			 String sqlList=
//				 "select getdeptname(tt.receive_dept),sum(price)\n" +
//				 "from\n" + 
//				 "(\n" + 
//				 "select a.material_id,a.receive_dept,\n" + 
//				 "(select c.std_cost\n" + 
//				 "                 from inv_j_transaction_his c\n" + 
//				 "                where c.trans_his_id =\n" + 
//				 "                      (select max(d.trans_his_id)\n" + 
//				 "                         from inv_j_transaction_his d\n" + 
//				 "                        where d.trans_id = 1\n" + 
//				 "                          and d.trans_his_id >= "+model.getTransHisMinid()+"\n" + 
//				 "                          and d.trans_his_id <= "+model.getTransHisMaxid()+"\n" + 
//				 "                          and a.material_id = d.material_id))*a.trans_qty  as price\n" + 
//				 "\n" + 
//				 "from inv_j_transaction_his a\n" + 
//				 "where a.trans_id=4\n" + 
//				 ") tt\n" + 
//				 "group by tt.receive_dept";
			 String sqlList=
				 "select getdeptname(tt.receive_dept),sum(price)\n" +
				 "from (\n" + 
				 " select  a.receive_dept,a.material_id,a.trans_qty*a.std_cost as price from  inv_j_transaction_his a\n" + // modify by ywliu ,inv_j_issue_head b 20100201
				 " where a.trans_id=4\n" + 
				 "   and a.check_date>( select c.balance_date from inv_j_balance c where  c.balance_id="+lastBalanceId+")\n" + 
				 "    and a.check_date<( select c.balance_date from inv_j_balance c where c.balance_id="+model.getBalanceId()+")\n" + 
//				 "       and a.order_no=b.issue_no\n" + 
				 "   and  a.check_status='C'\n" + 
				 "   ) tt\n" + 
				 "   group by tt.receive_dept";

				 
			 List<IssueInfoForReport> issueList=new ArrayList<IssueInfoForReport>();
			 List reportList=bll.queryByNativeSQL(sqlList);
			 Iterator it=reportList.iterator();
			 while(it.hasNext())
			 {
				 Object [] data=(Object [])it.next();
				 IssueInfoForReport entity=new IssueInfoForReport();
				 if(data[0]!=null)
				 {
					 entity.setIssueDeptName(data[0].toString());
				 }
				 if(data[1]!=null)
				 {
					 entity.setReceiptCount(this.formatNumber(data[1].toString()));
				 }
				 else 
					 entity.setReceiptCount("0.00");
				 Double ou = Double.parseDouble(reportModel.getDisRate()) * Double.parseDouble(entity.getReceiptCount());
				 entity.setAssignDis(this.formatNumber(ou.toString()));
				 issueList.add(entity);				 
			 }
			 
//			 ************
			 //----------------查暂收
			 String sqlZanShiReceipt=
					"select sum(a.price / (1 + a.tax_rate) * a.trans_qty),\n" +
					"       sum(a.price * a.trans_qty),\n" + 
					"       sum(a.price / (1 + a.tax_rate) * a.trans_qty) -\n" + 
					"       sum(a.price * a.trans_qty)\n" + 
					"  from inv_j_transaction_his a,pur_j_arrival b\n" + 
					" where a.trans_id=1\n" + 
					"   and a.trans_his_id>="+model.getTransHisMinid()+"\n" + 
					"   and a.trans_his_id<="+model.getTransHisMaxid()+"\n" + 
					"   and a.arrival_no=b.arrival_no\n" + 
					//"   and  ((b.check_state<>'2' and  b.check_state<>'N') or b.check_state is null)"; //modify by fyyang 20100202
					"   and  (b.check_state<>'2'  or b.check_state is null)";
					List zanShiReceiptList=bll.queryByNativeSQL(sqlZanShiReceipt);
			
				if(zanShiReceiptList!=null&&zanShiReceiptList.size()>0)
				{
				   Object [] obj= (Object [])zanShiReceiptList.get(0);
				   if (obj[1] != null) {
						reportModel.setOriZanShou(this.formatNumber(obj[1].toString()));
					}
				   else 
					reportModel.setOriZanShou("0.00");
				   
				   if (obj[2] != null) {
						reportModel.setDisZanShou(this.formatNumber(obj[2].toString()));
					}
				   else 
					   reportModel.setDisZanShou("0.00");
				}
//			 reportModel.setDisZanShou("123");
//			 reportModel.setOriZanShou("456");
			 reportModel.setIssueList(issueList);
		}
		
		return reportModel;
	}
	
	public String formatNumber(String value)
	{
		// modified by liuyi 091126 数据格式化为截断四位小数
//		Double dd = Math.rint(Double.parseDouble(value)*100)/100;
//		value = dd.toString();
//		int in = value.indexOf(".");
//		if(in == -1)
//		{
//			return value + ".00";
//		}
//		else if(value.substring(in,value.length()).length() == 1)
//		{
//			return value + "0";
//		}
		Double dd = Math.floor(Double.parseDouble(value)*10000)/10000;
		value = dd.toString();
		return value;
	}
	
	private String format(String s) {
		int index = s.lastIndexOf(".");
		if (index < 0)
			index = s.length();
		for (int i = index; i > 3; i -= 3) {
			s = s.substring(0, i - 3) + "," + s.substring(i - 3, s.length());
		}
		return s;
	}
	/**
	 * 	  保留两位小数
	 * @param data
	 * @return
	 */
	private String dataOperate(Double data){
		NumberFormat formatter = new DecimalFormat("0.00");
		return format(formatter.format(data.doubleValue()));
	}
	
	
	
	 /**
     * add by liuyi 20100319 材料收发报表仓库月份查询  仓库收发存统计报表
     * modify by fyyang 20100415 whsNo 仓库改成物料类别
     */
	public List<WarehouseThisMonthInfo> getMonthInfoOfWareByCodeAndMonth(String whsNo,String dateMonth) {

		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
		//String dateMonth="200907";
		String balanceSql=
			"select t.balance_id\n" + 
			"  from inv_j_balance t\n" + 
			" where t.balance_year_month = "+dateMonth+"\n" + 
			"   and rownum = 1";
		Object obj=bll.getSingal(balanceSql);
		Long balanceId=0l;
		if(obj!=null&&!obj.equals(""))
		{
        balanceId=Long.parseLong(obj.toString());
		}
       String yString = dateMonth.substring(0,4);
       String mString = dateMonth.substring(4);
       String currentMonth = yString + "-" + mString;
       GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(yString),Integer.parseInt(mString) - 1, 1);
       gc.add(Calendar.MONTH, 1);
       int nextMInt = gc.get(Calendar.MONTH) + 1;
       String nextM = nextMInt > 9 ? String.valueOf(nextMInt) : "0" + String.valueOf(nextMInt);
       String nextMonth = String.valueOf(gc.get(Calendar.YEAR)) + "-" + nextM;
			

			String sql=
//				"select getwhsname(t.whs_no),\n" +
//				"       a.material_no,\n" + 
//				"       a.material_name,\n" + 
//				"       a.spec_no,\n" + 
//				"       getunitname(a.stock_um_id),\n" + 
//				"       t.std_cost,\n" + 
//				"       nvl(t.openbalance_qty,0),\n" + 
//				"       nvl(t.openbalance_price,0),\n" + 
//				"       nvl(t.check_in_qty,0),\n" + 
//				"       nvl(t.check_in_price,0),\n" + 
//				"       nvl(t.check_out_qty,0),\n" + 
//				"       nvl(t.check_out_price,0),\n" + 
//				"       nvl(t.openbalance_qty, 0) + nvl(t.check_in_qty, 0) -\n" + 
//				"       nvl(t.check_out_qty, 0),\n" + 
//				"       nvl(t.openbalance_price, 0) + nvl(t.check_in_price, 0) -\n" + 
//				"       nvl(t.check_out_price, 0)\n" + 
//				"\n" + 
//				"  from inv_j_transaction_his_money t, inv_c_material a\n" + 
//				" where t.balance_id = "+balanceId+"\n" + 
//				"   and t.material_id = a.material_id\n" + 
//				"  and t.whs_no='"+whsNo+"' \n"+
//				"   and t.is_use = 'Y' \n"+
//				"  and ( nvl(t.check_in_price,0)<>0  \n"+
//				"  or  nvl(t.check_out_price,0)<>0 \n"+
//				"  or nvl(t.openbalance_price, 0) + nvl(t.check_in_price, 0) - nvl(t.check_out_price, 0) <> 0)";

				"select getwhsname(a.default_whs_no),\n" +
				"       a.material_no,\n" + 
				"       a.material_name,\n" + 
				"       a.spec_no,\n" + 
				"       getunitname(a.stock_um_id),\n" + 
				"       t.std_cost,\n" + 
				"       nvl(t.openbalance_qty,0),\n" + 
				"       nvl(t.openbalance_price,0),\n" + 
				"       nvl(t.check_in_qty,0),\n" + 
				"       nvl(t.check_in_price,0),\n" + 
				"       nvl(t.check_out_qty,0),\n" + 
				"       nvl(t.check_out_price,0),\n" + 
				"       nvl(t.openbalance_qty, 0) + nvl(t.check_in_qty, 0) -\n" + 
				"       nvl(t.check_out_qty, 0),\n" + 
				"       nvl(t.openbalance_price, 0) + nvl(t.check_in_price, 0) -\n" + 
				"       nvl(t.check_out_price, 0),\n" + 
				"             nvl(t1.startqty,0)+nvl(t2.lastqty,0) as whlastqty,\n" + 
				"         nvl(t1.startprice,0)+nvl(t2.lastprice,0) as whlastprice,\n" + 
				"           nvl(t3.inqty,0) as whinqty,\n" + 
				"       nvl(t3.inprice,0) as whinprice,\n" + 
				"       nvl(t3.outqty,0) as whoutqty,\n" + 
				"       nvl(t3.outprice,0) as whoutprice,\n" + 
				"         nvl(t1.startqty,0)+nvl(t4.thisqty,0) whthisqty,\n" + 
				"        nvl(t1.startprice,0)+nvl(t4.thisprice,0) whthisprice\n" + 
				"  from inv_j_transaction_his_money t, inv_c_material a,\n" + 
				"   (select t.material_id,\n" + 
				"               sum(round(nvl(t.trans_qty * t.std_cost, 0), 2)) startprice,\n" + 
				"                sum(t.trans_qty) startqty\n" + 
				"          from inv_j_transaction_his t\n" + 
				"         where t.trans_his_id <= (select a.trans_his_maxid\n" + 
				"                                    from inv_j_balance a\n" + 
				"                                   where a.balance_id = 2)\n" + 
				"           and t.trans_id = 6\n" + 
				"\n" + 
				"           and t.is_use = 'Y'\n" + 
				"\n" + 
				"         group by t.material_id) t1,\n" + 
				"       (select  t.material_id,\n" + 
				"               sum(round(decode(t.trans_id,1,t.trans_qty * t.price, 4, -t.trans_qty * t.std_cost,  0), 2)) lastprice,\n" + 
				"               sum(decode(t.trans_id, 1, t.trans_qty, 4, -t.trans_qty, 0)  ) lastqty\n" + 
				"          from inv_j_transaction_his t\n" + 
				"         where t.trans_id in (1, 4)\n" + 
				"           and t.is_use = 'Y'\n" + 
				"           and t.last_modified_date <\n" + 
				"               to_date('"+currentMonth+"' || '-01 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
				"         group by  t.material_id) t2,\n" + 
				"       (select t.material_id,\n" + 
				"               sum(round(decode(t.trans_id, 1, t.trans_qty * t.price, 0), 2)) inprice,\n" + 
				"               sum(round(decode(t.trans_id, 4, t.trans_qty * t.std_cost, 0), 2)) outprice,\n" + 
				"                sum(round(decode(t.trans_id, 1, t.trans_qty, 0), 2)) inqty,\n" + 
				"               sum(round(decode(t.trans_id, 4, t.trans_qty, 0), 2)) outqty\n" + 
				"          from inv_j_transaction_his t\n" + 
				"         where t.trans_id in (1, 4)\n" + 
				"           and t.is_use = 'Y'\n" + 
				"\n" + 
				"           and t.last_modified_date >=\n" + 
				"               to_date('"+currentMonth+"' || '-01 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
				"           and t.last_modified_date <\n" + 
				"               to_date('"+nextMonth+"' || '-01 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
				"         group by t.material_id) t3,\n" + 
				"         (\n" + 
				"             select t.material_id,\n" + 
				"         sum(round(decode(t.trans_id, 1, t.trans_qty * t.price,  4, -t.trans_qty * t.std_cost, 0),2)) thisprice,\n" + 
				"         sum(round(decode(t.trans_id,  1, t.trans_qty, 4,  -t.trans_qty , 0),2)) thisqty\n" + 
				"    from inv_j_transaction_his t\n" + 
				"   where t.trans_id in (1, 4)\n" + 
				"     and t.is_use = 'Y'\n" + 
				"     and t.last_modified_date <\n" + 
				"         to_date('"+nextMonth+"' || '-01 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
				"   group by t.material_id\n" + 
				"\n" + 
				"         )t4\n" + 
				" where t.balance_id(+) = "+balanceId+" \n" + 
				"   and t.material_id(+) = a.material_id\n" + 
				"  and a.material_no like '"+whsNo+"%'\n" + 
				"   and t.is_use(+) = 'Y'\n" + 
				"   and  a.material_id=t1.material_id(+)\n" + 
				"   and a.material_id=t2.material_id(+)\n" + 
				"     and a.material_id=t3.material_id(+)\n" + 
				"       and a.material_id=t4.material_id(+)\n" + 
				"and (nvl(t3.inprice, 0) <> 0 or  nvl(t3.outprice, 0) <> 0 or\n" +
				"     nvl(t1.startprice, 0) + nvl(t4.thisprice, 0) <> 0)";

//				"  and ( nvl(t.check_in_price,0)<>0\n" + 
//				"  or  nvl(t.check_out_price,0)<>0\n" + 
//				"  or nvl(t.openbalance_price, 0) + nvl(t.check_in_price, 0) - nvl(t.check_out_price, 0) <> 0)";



			
		List list = bll.queryByNativeSQL(sql);
		
		 Double lastQty=0.0;
		 Double lastPrice=0.0;
		 Double inQty=0.0;
		 Double inPrice=0.0;
		 Double outQty=0.0;
		 Double outPrice=0.0;
		 Double thisQty=0.0;
		 Double thisPrice=0.0;
		 
		 Double ewlastQty=0.0;
		 Double ewlastPrice=0.0;
		 Double ewinQty=0.0;
		 Double ewinPrice=0.0;
		 Double ewoutQty=0.0;
		 Double ewoutPrice=0.0;
		 Double ewthisQty=0.0;
		 Double ewthisPrice=0.0;
		 
	        
        Iterator it = list.iterator();
        while (it.hasNext()) {
        	WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
            Object[] data = (Object[]) it.next();
            // 仓库名称
            if(null != data[0])
            monthInfo.setWhsName(data[0].toString());
            // 物资编码
            if(null != data[1])
                monthInfo.setMaterialNo(data[1].toString());
            // 物资名称
            if(null != data[2])
                monthInfo.setMaterialName(data[2].toString());
            // 规格型号
            if(null != data[3])
                monthInfo.setSpecNo(data[3].toString());
            // 单位
            if(null != data[4])
                monthInfo.setPurUm(data[4].toString());
            // 计划单价
            if(null != data[5])
                monthInfo.setStdCost(data[5].toString());
            // 上期结存数
            if(null != data[6])
            {
            	monthInfo.setOpenBalance(data[6].toString());
            	if(monthInfo.getMaterialNo().trim().length()!=3)
                {
                lastQty+=Double.parseDouble(data[6].toString());
                }
            }
            // 上期结存金额
            if(null != data[7])
            {
				monthInfo.setOBCost(data[7].toString());
				if (monthInfo.getMaterialNo().trim().length() != 3) {
					lastPrice += Double.parseDouble(data[7].toString());
				}
			}
            // 本期入库数
            if(null != data[8])
            {
            	monthInfo.setReceipt(data[8].toString());
            	if(monthInfo.getMaterialNo().trim().length()!=3)
                {
                inQty+=Double.parseDouble(data[8].toString());
                }
            }
            // 本期入库金额
            if(null != data[9])
            {
            	monthInfo.setReceiptCost(data[9].toString());
            	if(monthInfo.getMaterialNo().trim().length()!=3)
                {
                inPrice+=Double.parseDouble(data[9].toString());
                }
            }
            // 本期支出数量
            if(null != data[10])
            {
            	monthInfo.setIssue(data[10].toString());
            	if(monthInfo.getMaterialNo().trim().length()!=3)
                {
                outQty+=Double.parseDouble(data[10].toString());
                }
            }
            // 本期支出金额
            if(null != data[11])
            {
            	monthInfo.setIssueCost(data[11].toString());
            	if(monthInfo.getMaterialNo().trim().length()!=3)
                {
                 outPrice+=Double.parseDouble(data[11].toString());
                }
            }
            // 本期结余数量
            if(null != data[12])
            {
            	monthInfo.setThisBalance(data[12].toString());
            	if(monthInfo.getMaterialNo().trim().length()!=3)
                {
                 thisQty+=Double.parseDouble(data[12].toString());
                }
            }
            // 本期结余金额
            if(null != data[13])
            {
            	monthInfo.setTBCost(data[13].toString());
            	 if(monthInfo.getMaterialNo().trim().length()!=3)
                 {
                  thisPrice+=Double.parseDouble(data[13].toString());
                 }
            }
            
            //  库存/额外用 上期结存数
            if(null != data[14])
            {
            	monthInfo.setEWopenBalance(data[14].toString());
            	if(monthInfo.getMaterialNo().trim().length()!=3)
                {
                ewlastQty+=Double.parseDouble(data[14].toString());
                }
            }
        //  库存/额外用上期结存金额
            if(null != data[15])
            {
            	monthInfo.setEWOBCost(data[15].toString());
            	if (monthInfo.getMaterialNo().trim().length() != 3) {
					ewlastPrice += Double.parseDouble(data[15].toString());
				}
            }
        //  库存/额外用本期入库数
            if(null != data[16])
            {
            	monthInfo.setEWreceipt(data[16].toString());
            	if(monthInfo.getMaterialNo().trim().length()!=3)
                {
                ewinQty+=Double.parseDouble(data[16].toString());
                }
            }
        //  库存/额外用本期入库金额
            if(null != data[17])
            {
            	monthInfo.setEWreceiptCost(data[17].toString());
            	if(monthInfo.getMaterialNo().trim().length()!=3)
                {
                ewinPrice+=Double.parseDouble(data[17].toString());
                }
            }
            //  库存/额外用本期支出数量
            if(null != data[18])
            {
            	monthInfo.setEWissue(data[18].toString());
            	if(monthInfo.getMaterialNo().trim().length()!=3)
                {
                ewoutQty+=Double.parseDouble(data[18].toString());
                }
            }
            //  库存/额外用本期支出金额
            if(null != data[19])
            {
            	monthInfo.setEWissueCost(data[19].toString());
            	if(monthInfo.getMaterialNo().trim().length()!=3)
                {
                 ewoutPrice+=Double.parseDouble(data[19].toString());
                }
            }
            //  库存/额外用本期结余数量
            if(null != data[20])
            {
            	monthInfo.setEWthisBalance(data[20].toString());
            	if(monthInfo.getMaterialNo().trim().length()!=3)
                {
                 ewthisQty+=Double.parseDouble(data[20].toString());
                }
            }
        //  库存/额外用本期结余金额
            if(null != data[21])
            {
            	monthInfo.setEWTBCost(data[21].toString());
            	 if(monthInfo.getMaterialNo().trim().length()!=3)
                 {
                  ewthisPrice+=Double.parseDouble(data[21].toString());
                 }
            }

            monthInfo.setMonth(dateMonth);
            
            arrlist.add(monthInfo);
        }
        
        WarehouseThisMonthInfo model = new WarehouseThisMonthInfo();
        model.setMaterialNo("合计");
        model.setOpenBalance(lastQty.toString());
        model.setOBCost(lastPrice.toString());
        model.setReceipt(inQty.toString());
        model.setReceiptCost(inPrice.toString());
        model.setIssue(outQty.toString());
        model.setIssueCost(outPrice.toString());
        model.setThisBalance(thisQty.toString());
        model.setTBCost(thisPrice.toString());
        
        model.setEWopenBalance(ewlastQty.toString());
        model.setEWOBCost(ewlastPrice.toString());
        model.setEWreceipt(ewinQty.toString());
        model.setEWreceiptCost(ewinPrice.toString());
        model.setEWissue(ewoutQty.toString());
        model.setEWissueCost(ewoutPrice.toString());
        model.setEWthisBalance(ewthisQty.toString());
        model.setEWTBCost(ewthisPrice.toString());
        arrlist.add(model);
	
		return arrlist;
	
//		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
//		String falg="0";
//		String balanceSql=
//			"select t.balance_id\n" + 
//			"  from inv_j_balance t\n" + 
//			" where t.balance_year_month = "+dateMonth+"\n" + 
//			"   and rownum = 1";
//		Object obj=bll.getSingal(balanceSql);
//		Long balanceId=0l;
//		if(obj!=null&&!obj.equals(""))
//		{
//			balanceId=Long.parseLong(obj.toString());
//		}
//		else
//		{
//			String maxMonth="select max(t.balance_year_month) from inv_j_balance t where t.is_use = 'Y' ";
//			int year=Integer.parseInt(dateMonth.substring(0, 4));
//			int month=Integer.parseInt(dateMonth.substring(5,6));
//			if(month==1)
//			{
//				month=12;
//				year--;
//			}
//			else
//			{
//				month--;
//			}
//			if(bll.getSingal(maxMonth).toString().equals(year+""+month)||bll.getSingal(maxMonth).toString().equals(year+"0"+month))
//			{
//				     falg="1";
//			}
//		}
//
//       String maxSql=
//			"select max(t.balance_id)\n" + 
//			"  from inv_j_balance t\n" + 
//			" where t.is_use='Y'\n"; 
//	 Long maxBalanceId=Long.parseLong(bll.getSingal(maxSql).toString());	
//	 
//	
//     
//	 Double lastQty=0.0;
//	 Double lastPrice=0.0;
//	 Double inQty=0.0;
//	 Double inPrice=0.0;
//	 Double outQty=0.0;
//	 Double outPrice=0.0;
//	 Double thisQty=0.0;
//	 Double thisPrice=0.0;
//
////			String sql= 
////				"select getwhsname(a.default_whs_no),\n" +
////				"       a.material_no,\n" + 
////				"       a.material_name,\n" + 
////				"       a.spec_no,\n" + 
////				"       getunitname(a.stock_um_id),\n" + 
////				"       b.std_cost,\n" + 
////				"       nvl(c.trans_qty,0),\n" + 
////				"       nvl(c.trans_qty*c.std_cost,0),\n" + 
////				"       nvl(t.inqty,0),\n" + 
////				"       nvl(t.inprice,0),\n" + 
////				"       nvl(t.outqty,0),\n" + 
////				"       nvl(t.outprice,0),\n" + 
////				"       nvl(b.trans_qty,0),\n" + 
////				"       nvl(b.trans_qty*b.std_cost,0)\n" + 
////				"  from (select t.material_id,\n" + 
////				"               sum(decode(t.trans_id, 1, t.trans_qty, 0)) inqty,\n" + 
////				"               sum(decode(t.trans_id, 1, t.trans_qty * t.price, 0)) inprice,\n" + 
////				"               sum(decode(t.trans_id, 4, t.trans_qty, 0)) outqty,\n" + 
////				"               sum(decode(t.trans_id, 4, t.trans_qty * t.std_cost, 0)) outprice\n" + 
////				"          from Inv_j_Transaction_His t\n" + 
////				"         where t.is_use = 'Y'\n" + 
////				"           and t.trans_his_id >= (select b.trans_his_minid from inv_j_balance b where b.balance_id="+balanceId+" and b.is_use='Y')\n" + 
////				"           and t.trans_his_id <= (select b.trans_his_maxid from inv_j_balance b where b.balance_id="+balanceId+" and b.is_use='Y')\n" + 
////				"         group by t.material_id) t,\n" + 
////				"       (select *\n" + 
////				"          from inv_j_transaction_his t\n" + 
////				"         where t.trans_id = 6\n" + 
////				"         and t.is_use='Y'\n";
////			 if(maxBalanceId==balanceId)
////			 {
////				 sql+="           and t.trans_his_id >= (select b.trans_his_maxid from inv_j_balance b where b.balance_id="+balanceId +" and b.is_use='Y'))b,\n";
////			 }
////			 else
////			 {
////				 sql+="           and t.trans_his_id >= (select b.trans_his_minid from inv_j_balance b where b.balance_id="+(balanceId + 1)+" and b.is_use='Y')\n" + 
////					"           and t.trans_his_id < (select b.trans_his_maxid from inv_j_balance b where b.balance_id="+(balanceId + 1)+" and b.is_use='Y')) b,\n";
////			 }
////				
////				sql+="       (select *\n" + 
////				"          from inv_j_transaction_his t\n" + 
////				"         where t.trans_id = 6\n" + 
////				"         and t.is_use='Y'\n" + 
////				"           and t.trans_his_id >= (select b.trans_his_minid from inv_j_balance b where b.balance_id="+balanceId+" and b.is_use='Y')\n" + 
////				"           and t.trans_his_id <(select b.trans_his_maxid from inv_j_balance b where b.balance_id="+balanceId+" and b.is_use='Y')) c,\n" + 
////				"       inv_c_material a\n" + 
////				" where t.material_id = a.material_id\n" + 
////				"   and t.material_id = b.material_id(+)\n" + 
////				"   and t.material_id = c.material_id(+)\n" + 
////				"   and a.default_whs_no = '"+whsNo+"'\n" + 
////				"   and a.is_use='Y'\n" + 
////				"   and (t.inqty<>0 or t.outqty<>0 or b.trans_qty<>0) \n";
//	 
//	 String sql="";
//	 if(falg.equals("0"))
//	 {
//	 
//		 sql="\n" +
//		 "select ttt.*,material_no||(10-length(material_no)) r from (\n" + 
//		 "select getwhsname(a.default_whs_no),\n" + 
//		 "       a.material_no,\n" + 
//		 "       a.material_name,\n" + 
//		 "       a.spec_no,\n" + 
//		 "       getunitname(a.stock_um_id),\n" + 
//		 "       b.std_cost,\n" + 
//		 "       nvl(c.trans_qty,0),\n" + 
//		 "       nvl(c.trans_qty*c.std_cost,0),\n" + 
//		 "       nvl(t.inqty,0),\n" + 
//		 "       nvl(t.inprice,0),\n" + 
//		 "       nvl(t.outqty,0),\n" + 
//		 "       nvl(t.outprice,0),\n" + 
//		 "       nvl(b.trans_qty,0),\n" + 
//		 "       nvl(b.trans_qty*b.std_cost,0)\n" + 
//		 "  from (select t.material_id,\n" + 
//		 "               sum(decode(t.trans_id, 1, t.trans_qty, 0)) inqty,\n" + 
//		 "               sum(decode(t.trans_id, 1, t.trans_qty * t.price, 0)) inprice,\n" + 
//		 "               sum(decode(t.trans_id, 4, t.trans_qty, 0)) outqty,\n" + 
//		 "               sum(decode(t.trans_id, 4, t.trans_qty * t.std_cost, 0)) outprice\n" + 
//		 "          from Inv_j_Transaction_His t\n" + 
//		 "         where t.is_use = 'Y'\n" + 
//		 "           and t.trans_his_id >= (select b.trans_his_minid from inv_j_balance b where b.balance_id="+balanceId+" and b.is_use='Y')\n" + 
//		 "           and t.trans_his_id <= (select b.trans_his_maxid from inv_j_balance b where b.balance_id="+balanceId+" and b.is_use='Y')\n" + 
//		 "         group by t.material_id) t,\n" + 
//		 "       (select *\n" + 
//		 "          from inv_j_transaction_his t\n" + 
//		 "         where t.trans_id = 6\n" + 
//		 "         and t.is_use='Y'\n";
//	 if(maxBalanceId==balanceId)
//	 {
//		 sql+="           and t.trans_his_id >= (select b.trans_his_maxid from inv_j_balance b where b.balance_id="+balanceId+" and b.is_use='Y'))b,\n" ; 
//	 }
//	 else
//	 {
//		 sql+="           and t.trans_his_id >= (select b.trans_his_minid from inv_j_balance b where b.balance_id="+(balanceId + 1)+" and b.is_use='Y')\n" + 
//			"           and t.trans_his_id < (select b.trans_his_maxid from inv_j_balance b where b.balance_id="+(balanceId + 1)+" and b.is_use='Y')) b,\n";
//	 }
//		 
//		sql+= "       (select *\n" + 
//		 "          from inv_j_transaction_his t\n" + 
//		 "         where t.trans_id = 6\n" + 
//		 "         and t.is_use='Y'\n" + 
//		 "           and t.trans_his_id >= (select b.trans_his_minid from inv_j_balance b where b.balance_id="+balanceId+" and b.is_use='Y')\n" + 
//		 "           and t.trans_his_id <(select b.trans_his_maxid from inv_j_balance b where b.balance_id="+balanceId+" and b.is_use='Y')) c,\n" + 
//		 "       inv_c_material a\n" + 
//		 " where t.material_id = a.material_id\n" + 
//		 "   and t.material_id = b.material_id(+)\n" + 
//		 "   and t.material_id = c.material_id(+)\n" + 
//		 "   and a.default_whs_no = '"+whsNo+"'\n" + 
//		 "   and a.is_use='Y'\n" + 
//		 "   and (t.inqty<>0 or t.outqty<>0 or b.trans_qty<>0)\n" + 
//		 "\n" + 
//		 "   union\n" + 
//		 "\n" + 
//		 "\n" + 
//		 "\n" + 
//		 "select whsName,\n" + 
//		 "       substr(tt.material_no, 0, 3),\n" + 
//		 "       cl.class_name,\n" + 
//		 "       '',\n" + 
//		 "       '',\n" + 
//		 "       0,\n" + 
//		 "       sum(tt.lasttrans_qty),\n" + 
//		 "       sum(tt.lastprice),\n" + 
//		 "       sum(tt.inqty),\n" + 
//		 "       sum(tt.inprice),\n" + 
//		 "       sum(tt.outqty),\n" + 
//		 "       sum(tt.outprice),\n" + 
//		 "       sum(tt.thisqty),\n" + 
//		 "       sum(tt.thisprice)\n" + 
//		 "\n" + 
//		 "  from (select getwhsname(a.default_whs_no) whsName,\n" + 
//		 "               a.material_no,\n" + 
//		 "               a.material_name,\n" + 
//		 "               a.spec_no,\n" + 
//		 "               getunitname(a.stock_um_id),\n" + 
//		 "               b.std_cost,\n" + 
//		 "               nvl(c.trans_qty, 0) lasttrans_qty,\n" + 
//		 "               nvl(c.trans_qty * c.std_cost, 0) lastprice,\n" + 
//		 "               nvl(t.inqty, 0) inqty,\n" + 
//		 "               nvl(t.inprice, 0) inprice,\n" + 
//		 "               nvl(t.outqty, 0) outqty,\n" + 
//		 "               nvl(t.outprice, 0) outprice,\n" + 
//		 "               nvl(b.trans_qty, 0) thisqty,\n" + 
//		 "               nvl(b.trans_qty * b.std_cost, 0) thisprice\n" + 
//		 "          from (select t.material_id,\n" + 
//		 "                       sum(decode(t.trans_id, 1, t.trans_qty, 0)) inqty,\n" + 
//		 "                       sum(decode(t.trans_id, 1, t.trans_qty * t.price, 0)) inprice,\n" + 
//		 "                       sum(decode(t.trans_id, 4, t.trans_qty, 0)) outqty,\n" + 
//		 "                       sum(decode(t.trans_id, 4, t.trans_qty * t.std_cost, 0)) outprice\n" + 
//		 "                  from Inv_j_Transaction_His t\n" + 
//		 "                 where t.is_use = 'Y'\n" + 
//		 "                   and t.trans_his_id >=\n" + 
//		 "                       (select b.trans_his_minid\n" + 
//		 "                          from inv_j_balance b\n" + 
//		 "                         where b.balance_id = "+balanceId+"\n" + 
//		 "                           and b.is_use = 'Y')\n" + 
//		 "                   and t.trans_his_id <=\n" + 
//		 "                       (select b.trans_his_maxid\n" + 
//		 "                          from inv_j_balance b\n" + 
//		 "                         where b.balance_id = "+balanceId+"\n" + 
//		 "                           and b.is_use = 'Y')\n" + 
//		 "                 group by t.material_id) t,\n" + 
//		 "               (select *\n" + 
//		 "                  from inv_j_transaction_his t\n" + 
//		 "                 where t.trans_id = 6\n" + 
//		 "                   and t.is_use = 'Y'\n";
//		 if(maxBalanceId==balanceId)
//		 {
//			 sql+="           and t.trans_his_id >= (select b.trans_his_maxid from inv_j_balance b where b.balance_id="+balanceId+" and b.is_use='Y'))b,\n" ; 
//		 }
//		 else
//		 {
//			 sql+="           and t.trans_his_id >= (select b.trans_his_minid from inv_j_balance b where b.balance_id="+(balanceId + 1)+" and b.is_use='Y')\n" + 
//				"           and t.trans_his_id < (select b.trans_his_maxid from inv_j_balance b where b.balance_id="+(balanceId + 1)+" and b.is_use='Y')) b,\n";
//		 }
//		
//		sql+= "               (select *\n" + 
//		 "                  from inv_j_transaction_his t\n" + 
//		 "                 where t.trans_id = 6\n" + 
//		 "                   and t.is_use = 'Y'\n" + 
//		 "                   and t.trans_his_id >=\n" + 
//		 "                       (select b.trans_his_minid\n" + 
//		 "                          from inv_j_balance b\n" + 
//		 "                         where b.balance_id = "+balanceId+"\n" + 
//		 "                           and b.is_use = 'Y')\n" + 
//		 "                   and t.trans_his_id <\n" + 
//		 "                       (select b.trans_his_maxid\n" + 
//		 "                          from inv_j_balance b\n" + 
//		 "                         where b.balance_id = "+balanceId+"\n" + 
//		 "                           and b.is_use = 'Y')) c,\n" + 
//		 "               inv_c_material a\n" + 
//		 "         where t.material_id = a.material_id\n" + 
//		 "           and t.material_id = b.material_id(+)\n" + 
//		 "           and t.material_id = c.material_id(+)\n" + 
//		 "           and a.default_whs_no = '"+whsNo+"'\n" + 
//		 "           and a.is_use = 'Y'\n" + 
//		 "           and (t.inqty <> 0 or t.outqty <> 0 or b.trans_qty <> 0)) tt,\n" + 
//		 "       inv_c_material_class cl\n" + 
//		 " where substr(tt.material_no, 0, 3) = cl.class_no\n" + 
//		 " group by whsName, substr(tt.material_no, 0, 3), cl.class_name\n" + 
//		 ")ttt\n" + 
//		 "order by  r\n" + 
//		 "\n" + 
//		 "\n" + 
//		 "\n" + 
//		 "\n" + 
//		 "";
//	 }
//	 else 
//	 {
//		
//		 balanceId=maxBalanceId;
//		
//		sql=
//			"\n" +
//			"select ttt.*, material_no || (10 - length(material_no)) r\n" + 
//			"  from (select getwhsname(a.default_whs_no),\n" + 
//			"               a.material_no,\n" + 
//			"               a.material_name,\n" + 
//			"               a.spec_no,\n" + 
//			"               getunitname(a.stock_um_id),\n" + 
//			"               a.std_cost,\n" + 
//			"               nvl(c.trans_qty, 0),\n" + 
//			"               nvl(c.trans_qty * c.std_cost, 0),\n" + 
//			"               nvl(t.inqty, 0),\n" + 
//			"               nvl(t.inprice, 0),\n" + 
//			"               nvl(t.outqty, 0),\n" + 
//			"               nvl(t.outprice, 0),\n" + 
//			"               nvl(c.trans_qty, 0) + nvl(t.inqty, 0) - nvl(t.outqty, 0) thisQty,\n" + 
//			"               nvl(c.trans_qty * c.std_cost, 0) + nvl(t.inprice, 0) -\n" + 
//			"               nvl(t.outprice, 0)\n" + 
//			"          from (select t.material_id,\n" + 
//			"                       sum(decode(t.trans_id, 1, t.trans_qty, 0)) inqty,\n" + 
//			"                       sum(decode(t.trans_id, 1, t.trans_qty * t.price, 0)) inprice,\n" + 
//			"                       sum(decode(t.trans_id, 4, t.trans_qty, 0)) outqty,\n" + 
//			"                       sum(decode(t.trans_id, 4, t.trans_qty * t.std_cost, 0)) outprice\n" + 
//			"                  from Inv_j_Transaction_His t\n" + 
//			"                 where t.is_use = 'Y'\n" + 
//			"                   and t.trans_his_id >=\n" + 
//			"                       (select b.trans_his_maxid\n" + 
//			"                          from inv_j_balance b\n" + 
//			"                         where b.balance_id = "+balanceId+"\n" + 
//			"                           and b.is_use = 'Y')\n" + 
//			"                 group by t.material_id) t,\n" + 
//			"               (select *\n" + 
//			"                  from inv_j_transaction_his t\n" + 
//			"                 where t.trans_id = 6\n" + 
//			"                   and t.is_use = 'Y'\n" + 
//			"                   and t.trans_his_id >=\n" + 
//			"                       (select b.trans_his_maxid\n" + 
//			"                          from inv_j_balance b\n" + 
//			"                         where b.balance_id = "+balanceId+"\n" + 
//			"                           and b.is_use = 'Y')) c,\n" + 
//			"               inv_c_material a\n" + 
//			"         where t.material_id = a.material_id\n" + 
//			"           and t.material_id = c.material_id(+)\n" + 
//			"           and a.default_whs_no = '"+whsNo+"'\n" + 
//			"           and a.is_use = 'Y'\n" + 
//			"           and (t.inqty <> 0 or t.outqty <> 0 or\n" + 
//			"               nvl(c.trans_qty, 0) + nvl(t.inqty, 0) - nvl(t.outqty, 0) <> 0)\n" + 
//			"\n" + 
//			"        union\n" + 
//			"\n" + 
//			"        select whsName,\n" + 
//			"               substr(tt.material_no, 0, 3),\n" + 
//			"               cl.class_name,\n" + 
//			"               '',\n" + 
//			"               '',\n" + 
//			"               0,\n" + 
//			"               sum(tt.lasttrans_qty),\n" + 
//			"               sum(tt.lastprice),\n" + 
//			"               sum(tt.inqty),\n" + 
//			"               sum(tt.inprice),\n" + 
//			"               sum(tt.outqty),\n" + 
//			"               sum(tt.outprice),\n" + 
//			"               sum(tt.thisqty),\n" + 
//			"               sum(tt.thisprice)\n" + 
//			"\n" + 
//			"          from (select getwhsname(a.default_whs_no) whsName,\n" + 
//			"                       a.material_no,\n" + 
//			"                       a.material_name,\n" + 
//			"                       a.spec_no,\n" + 
//			"                       getunitname(a.stock_um_id),\n" + 
//			"                       a.std_cost,\n" + 
//			"                       nvl(c.trans_qty, 0) lasttrans_qty,\n" + 
//			"                       nvl(c.trans_qty * c.std_cost, 0) lastprice,\n" + 
//			"                       nvl(t.inqty, 0) inqty,\n" + 
//			"                       nvl(t.inprice, 0) inprice,\n" + 
//			"                       nvl(t.outqty, 0) outqty,\n" + 
//			"                       nvl(t.outprice, 0) outprice,\n" + 
//			"                       nvl(c.trans_qty, 0) + nvl(t.inqty, 0) -\n" + 
//			"                       nvl(t.outqty, 0) thisqty,\n" + 
//			"                       nvl(c.trans_qty * c.std_cost, 0) + nvl(t.inprice, 0) -\n" + 
//			"                       nvl(t.outprice, 0) thisprice\n" + 
//			"                  from (select t.material_id,\n" + 
//			"                               sum(decode(t.trans_id, 1, t.trans_qty, 0)) inqty,\n" + 
//			"                               sum(decode(t.trans_id,\n" + 
//			"                                          1,\n" + 
//			"                                          t.trans_qty * t.price,\n" + 
//			"                                          0)) inprice,\n" + 
//			"                               sum(decode(t.trans_id, 4, t.trans_qty, 0)) outqty,\n" + 
//			"                               sum(decode(t.trans_id,\n" + 
//			"                                          4,\n" + 
//			"                                          t.trans_qty * t.std_cost,\n" + 
//			"                                          0)) outprice\n" + 
//			"                          from Inv_j_Transaction_His t\n" + 
//			"                         where t.is_use = 'Y'\n" + 
//			"                           and t.trans_his_id >=\n" + 
//			"                               (select b.trans_his_maxid\n" + 
//			"                                  from inv_j_balance b\n" + 
//			"                                 where b.balance_id = "+balanceId+"\n" + 
//			"                                   and b.is_use = 'Y')\n" + 
//			"                         group by t.material_id) t,\n" + 
//			"                       (select *\n" + 
//			"                          from inv_j_transaction_his t\n" + 
//			"                         where t.trans_id = 6\n" + 
//			"                           and t.is_use = 'Y'\n" + 
//			"                           and t.trans_his_id >=\n" + 
//			"                               (select b.trans_his_maxid\n" + 
//			"                                  from inv_j_balance b\n" + 
//			"                                 where b.balance_id = "+balanceId+"\n" + 
//			"                                   and b.is_use = 'Y')) c,\n" + 
//			"                       inv_c_material a\n" + 
//			"                 where t.material_id = a.material_id\n" + 
//			"                   and t.material_id = c.material_id(+)\n" + 
//			"                   and a.default_whs_no = '"+whsNo+"'\n" + 
//			"                   and a.is_use = 'Y'\n" + 
//			"                   and (t.inqty <> 0 or t.outqty <> 0 or\n" + 
//			"                       nvl(c.trans_qty, 0) + nvl(t.inqty, 0) -\n" + 
//			"                       nvl(t.outqty, 0) <> 0)) tt,\n" + 
//			"               inv_c_material_class cl\n" + 
//			"         where substr(tt.material_no, 0, 3) = cl.class_no\n" + 
//			"         group by whsName, substr(tt.material_no, 0, 3), cl.class_name) ttt\n" + 
//			" order by r";
//	 }
//
//			
//		List list = bll.queryByNativeSQL(sql);
//	
//        Iterator it = list.iterator();
//        while (it.hasNext()) {
//        	WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
//            Object[] data = (Object[]) it.next();
//            // 仓库名称
//            if(null != data[0])
//            monthInfo.setWhsName(data[0].toString());
//            // 物资编码
//            if(null != data[1])
//                monthInfo.setMaterialNo(data[1].toString());
//            // 物资名称
//            if(null != data[2])
//                monthInfo.setMaterialName(data[2].toString());
//            // 规格型号
//            if(null != data[3])
//                monthInfo.setSpecNo(data[3].toString());
//            // 单位
//            if(null != data[4])
//                monthInfo.setPurUm(data[4].toString());
//            // 计划单价
//            if(null != data[5])
//                monthInfo.setStdCost(data[5].toString());
//            // 上期结存数
//            if(null != data[6])
//            {
//                monthInfo.setOpenBalance(data[6].toString());
//                if(monthInfo.getMaterialNo().trim().length()!=3)
//                {
//                lastQty+=Double.parseDouble(data[6].toString());
//                }
//            }
//            // 上期结存金额
//            if(null != data[7])
//            {
//                monthInfo.setOBCost(data[7].toString());
//                if(monthInfo.getMaterialNo().trim().length()!=3)
//                {
//                lastPrice+=Double.parseDouble(data[7].toString());
//                }
//            }
//            // 本期入库数
//            if(null != data[8])
//            {
//                monthInfo.setReceipt(data[8].toString());
//                if(monthInfo.getMaterialNo().trim().length()!=3)
//                {
//                inQty+=Double.parseDouble(data[8].toString());
//                }
//            }
//            // 本期入库金额
//            if(null != data[9])
//            {
//                monthInfo.setReceiptCost(data[9].toString());
//                if(monthInfo.getMaterialNo().trim().length()!=3)
//                {
//                inPrice+=Double.parseDouble(data[9].toString());
//                }
//            }
//            // 本期支出数量
//            if(null != data[10])
//            {
//                monthInfo.setIssue(data[10].toString());
//                if(monthInfo.getMaterialNo().trim().length()!=3)
//                {
//                outQty+=Double.parseDouble(data[10].toString());
//                }
//            }
//            // 本期支出金额
//            if(null != data[11])
//            {
//                monthInfo.setIssueCost(data[11].toString());
//                if(monthInfo.getMaterialNo().trim().length()!=3)
//                {
//                 outPrice+=Double.parseDouble(data[11].toString());
//                }
//            }
//            // 本期结余数量
//            if(null != data[12])
//            {
//                monthInfo.setThisBalance(data[12].toString());
//                if(monthInfo.getMaterialNo().trim().length()!=3)
//                {
//                 thisQty+=Double.parseDouble(data[12].toString());
//                }
//            }
//            // 本期结余金额
//            if(null != data[13])
//            {
//                monthInfo.setTBCost(data[13].toString());
//                if(monthInfo.getMaterialNo().trim().length()!=3)
//                {
//                 thisPrice+=Double.parseDouble(data[13].toString());
//                }
//            }
//
//            monthInfo.setMonth(dateMonth);
//            
//            arrlist.add(monthInfo);
//        }
//        
//        WarehouseThisMonthInfo model = new WarehouseThisMonthInfo();
//        model.setMaterialNo("合计");
//        model.setOpenBalance(lastQty.toString());
//        model.setOBCost(lastPrice.toString());
//        model.setReceipt(inQty.toString());
//        model.setReceiptCost(inPrice.toString());
//        model.setIssue(outQty.toString());
//        model.setIssueCost(outPrice.toString());
//        model.setThisBalance(thisQty.toString());
//        model.setTBCost(thisPrice.toString());
//        model.setStdCost("0");
//        arrlist.add(model);
//	
//		return arrlist;
	}
	
	
	public List<WarehouseThisMonthInfo> findWareOfMonthInfo(String dateMonth) {

		List<WarehouseThisMonthInfo> resultList = new ArrayList<WarehouseThisMonthInfo>();
		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
		String balanceSql=
			"select t.balance_id\n" + 
			"  from inv_j_balance t\n" + 
			" where t.balance_year_month = "+dateMonth+"\n" + 
			"   and rownum = 1";
		Object obj=bll.getSingal(balanceSql);
		Long balanceId=0l;
		if(obj!=null&&!obj.equals(""))
		{
           balanceId=Long.parseLong(bll.getSingal(balanceSql).toString());
		}

       String yString = dateMonth.substring(0,4);
       String mString = dateMonth.substring(4);
       String currentMonth = yString + "-" + mString;
       GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(yString),Integer.parseInt(mString) - 1, 1);
       gc.add(Calendar.MONTH, 1);
       int nextMInt = gc.get(Calendar.MONTH) + 1;
       String nextM = nextMInt > 9 ? String.valueOf(nextMInt) : "0" + String.valueOf(nextMInt);
       String nextMonth = String.valueOf(gc.get(Calendar.YEAR)) + "-" + nextM;
			
        String sql=
//        	"select w.whs_no,\n" +
//        	"       w.whs_name,\n" + 
//        	"       nvl(t.lastprice, 0),\n" + 
//        	"       nvl(t.inprice, 0),\n" + 
//        	"       nvl(t.outprice, 0),\n" + 
//        	"       nvl(t.thisprice, 0),\n" + 
//        	"       nvl(t.zanshou, 0)\n" + 
//        	"  from (select a.whs_no,\n" + 
//        	"               sum(round(nvl(a.openbalance_price, 0), 2)) lastprice,\n" + 
//        	"               sum(round(nvl(a.check_in_price, 0), 2)) inprice,\n" + 
//        	"               sum(round(nvl(a.check_out_price, 0), 2)) outprice,\n" + 
//        	"               sum(round(nvl(a.openbalance_price, 0), 2) +\n" + 
//        	"                   round(nvl(a.check_in_price, 0), 2) -\n" + 
//        	"                   round(nvl(a.check_out_price, 0), 2)) thisprice,\n" + 
//        	"               sum(round(nvl(a.nocheck_in_price, 0), 2)) zanshou\n" + 
//        	"          from inv_j_transaction_his_money a, inv_c_material m\n" + 
//        	"         where a.balance_id = "+balanceId+"\n" + 
//        	"           and a.material_id = m.material_id\n" + 
//        	"           and a.is_use = 'Y'\n" + 
//        	"           and m.is_use = 'Y'\n" + 
//        	"         group by a.whs_no) t,\n" + 
//        	"       inv_c_warehouse w\n" + 
//        	" where w.whs_no = t.whs_no(+)\n" + 
//        	" order by w.whs_no asc";

        	"select w.whs_no,\n" +
        	"       w.whs_name,\n" + 
        	"       nvl(t.lastprice, 0),\n" + 
        	"       nvl(t.inprice, 0),\n" + 
        	"       nvl(t.outprice, 0),\n" + 
        	"       nvl(t.thisprice, 0),\n" + 
        	"       nvl(t.zanshou, 0),\n" + 
        	"       nvl(t1.startprice, 0) + nvl(t2.lastprice, 0) as whlastprice,\n" + 
        	"       nvl(t3.inprice, 0) as whinprice,\n" + 
        	"       nvl(t3.outprice, 0) as whoutprice,\n" + 
        	"       nvl(t1.startprice, 0) + nvl(t4.thisprice, 0) whthisprice\n" + 
        	"  from (select a.whs_no,\n" + 
        	"               sum(round(nvl(a.openbalance_price, 0), 2)) lastprice,\n" + 
        	"               sum(round(nvl(a.check_in_price, 0), 2)) inprice,\n" + 
        	"               sum(round(nvl(a.check_out_price, 0), 2)) outprice,\n" + 
        	"               sum(round(nvl(a.openbalance_price, 0), 2) +\n" + 
        	"                   round(nvl(a.check_in_price, 0), 2) -\n" + 
        	"                   round(nvl(a.check_out_price, 0), 2)) thisprice,\n" + 
        	"               sum(round(nvl(a.nocheck_in_price, 0), 2)) zanshou\n" + 
        	"          from inv_j_transaction_his_money a, inv_c_material m\n" + 
        	"         where a.balance_id = "+balanceId+"\n" + 
        	"           and a.material_id = m.material_id\n" + 
        	"           and a.is_use = 'Y'\n" + 
        	"           and m.is_use = 'Y'\n" + 
        	"         group by a.whs_no) t,\n" + 
        	"       (select a.default_whs_no,\n" + 
        	"               sum(round(nvl(t.trans_qty * t.std_cost, 0), 2)) startprice\n" + 
        	"          from inv_j_transaction_his t, inv_c_material a\n" + 
        	"         where t.trans_his_id <= (select a.trans_his_maxid\n" + 
        	"                                    from inv_j_balance a\n" + 
        	"                                   where a.balance_id = 2)\n" + 
        	"           and t.trans_id = 6\n" + 
        	"           and t.material_id = a.material_id\n" + 
        	"           and t.is_use = 'Y'\n" + 
        	"           and a.is_use = 'Y'\n" + 
        	"         group by a.default_whs_no) t1,\n" + 
        	"       (select a.default_whs_no,\n" + 
        	"               sum(round(decode(t.trans_id,\n" + 
        	"                                1,\n" + 
        	"                                t.trans_qty * t.price,\n" + 
        	"                                4,\n" + 
        	"                                -t.trans_qty * t.std_cost,\n" + 
        	"                                0),\n" + 
        	"                         2)) lastprice\n" + 
        	"          from inv_j_transaction_his t, inv_c_material a\n" + 
        	"         where t.trans_id in (1, 4)\n" + 
        	"           and t.is_use = 'Y'\n" + 
        	"           and t.material_id = a.material_id\n" + 
        	"           and a.is_use = 'Y'\n" + 
        	"           and t.last_modified_date <\n" + 
        	"               to_date('"+currentMonth+"' || '-01 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
        	"         group by a.default_whs_no) t2,\n" + 
        	"       (select a.default_whs_no,\n" + 
        	"               sum(round(decode(t.trans_id, 1, t.trans_qty * t.price, 0), 2)) inprice,\n" + 
        	"               sum(round(decode(t.trans_id, 4, t.trans_qty * t.std_cost, 0),\n" + 
        	"                         2)) outprice\n" + 
        	"          from inv_j_transaction_his t, inv_c_material a\n" + 
        	"         where t.trans_id in (1, 4)\n" + 
        	"           and t.is_use = 'Y'\n" + 
        	"           and t.material_id = a.material_id\n" + 
        	"           and a.is_use = 'Y'\n" + 
        	"           and t.last_modified_date >=\n" + 
        	"               to_date('"+currentMonth+"' || '-01 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
        	"           and t.last_modified_date <\n" + 
        	"               to_date('"+nextMonth+"' || '-01 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
        	"         group by a.default_whs_no) t3,\n" + 
        	"       (select a.default_whs_no,\n" + 
        	"               sum(round(decode(t.trans_id,\n" + 
        	"                                1,\n" + 
        	"                                t.trans_qty * t.price,\n" + 
        	"                                4,\n" + 
        	"                                -t.trans_qty * t.std_cost,\n" + 
        	"                                0),\n" + 
        	"                         2)) thisprice\n" + 
        	"          from inv_j_transaction_his t, inv_c_material a\n" + 
        	"         where t.trans_id in (1, 4)\n" + 
        	"           and t.is_use = 'Y'\n" + 
        	"           and t.material_id = a.material_id\n" + 
        	"           and a.is_use = 'Y'\n" + 
        	"           and t.last_modified_date <\n" + 
        	"               to_date('"+nextMonth+"' || '-01 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
        	"         group by a.default_whs_no\n" + 
        	"\n" + 
        	"        ) t4,\n" + 
        	"       inv_c_warehouse w\n" + 
        	" where w.whs_no = t.whs_no(+)\n" + 
        	"   and w.whs_no = t1.default_whs_no(+)\n" + 
        	"   and w.whs_no = t2.default_whs_no(+)\n" + 
        	"   and w.whs_no = t3.default_whs_no(+)\n" + 
        	"   and w.whs_no = t4.default_whs_no(+)\n" + 
        	" order by w.whs_no asc";

        	


		List list = bll.queryByNativeSQL(sql);
		Double totalReceiptCost=0d;
		Double totalIssueCost=0d;
		Double totalTBCost=0d;
		Double totalOBCost=0d;
		Double totalZanShou=0d;
		
		Double eWtotalReceiptCost=0d;
		Double eWtotalIssueCost=0d;
		Double eWtotalTBCost=0d;
		Double eWtotalOBCost=0d;
        Iterator it = list.iterator();
        while (it.hasNext()) {
        	WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
            Object[] data = (Object[]) it.next();
            // 仓库号
            if(null != data[0])
            monthInfo.setWhsNo(data[0].toString());
            // 仓库名称
            if(null != data[1])
            monthInfo.setWhsName(data[1].toString());
    
            // 上期结存金额
            if(null != data[2])
            {
                monthInfo.setOBCost(data[2].toString());
                totalOBCost+=Double.parseDouble(monthInfo.getOBCost());
            }
            // 本期入库金额
            if(null != data[3])
            {
                monthInfo.setReceiptCost(data[3].toString());
                totalReceiptCost+=Double.parseDouble(monthInfo.getReceiptCost());
            }
            if(null != data[4])
            {
                monthInfo.setIssueCost(data[4].toString());
                totalIssueCost+=Double.parseDouble(monthInfo.getIssueCost());
            }
            if(null != data[5])
            {
                monthInfo.setTBCost(data[5].toString());
                totalTBCost+=Double.parseDouble(monthInfo.getTBCost());
            }
            if(null!=data[6])
            {
            	//暂收
            	monthInfo.setZanShou(data[6].toString());
            	totalZanShou+=Double.parseDouble(monthInfo.getZanShou());
            }
            
            // add by liuyi 20100412 
            if(null != data[7])
            {
            	monthInfo.setEWOBCost(data[7].toString());
            	eWtotalOBCost += Double.parseDouble(monthInfo.getEWOBCost());
            }
            if(null != data[8])
            {
            	monthInfo.setEWreceiptCost(data[8].toString());
            	eWtotalReceiptCost += Double.parseDouble(monthInfo.getEWreceiptCost());
            }
            if(null != data[9])
            {
            	monthInfo.setEWissueCost(data[9].toString());
            	eWtotalIssueCost += Double.parseDouble(monthInfo.getEWissueCost());
            }
            if(null != data[10])
            {
            	monthInfo.setEWTBCost(data[10].toString());
            	eWtotalTBCost += Double.parseDouble(monthInfo.getEWTBCost());
            }
            arrlist.add(monthInfo);
        }
        if(dateMonth.equals("201001"))
        {
        WarehouseThisMonthInfo addInfo=new WarehouseThisMonthInfo();
        
        addInfo.setWhsNo("");
        addInfo.setWhsName("调整");
        arrlist.add(addInfo);
        }
        for(int i=0;i<arrlist.size();i++)
        {
        	WarehouseThisMonthInfo entity=arrlist.get(i);
        	if(dateMonth.equals("201001"))
    		{
        		
    			entity.setOBCost(dataOperate(Double.parseDouble(totalOBCost.toString())));
    			entity.setReceiptCost(dataOperate(Double.parseDouble(totalReceiptCost.toString())-287290.14));
    			entity.setTBCost(dataOperate(Double.parseDouble(totalTBCost.toString())-287290.14));
    		}
//        	else if(dateMonth.substring(0, 4).equals("2010"))
//    		{
//        		
//    			entity.setOBCost(dataOperate(Double.parseDouble(totalOBCost.toString())-287290.14));
//    			entity.setReceiptCost(dataOperate(Double.parseDouble(totalReceiptCost.toString())));
//    			entity.setTBCost(dataOperate(Double.parseDouble(totalTBCost.toString())-287290.14));
//    		}
        	else
        	{
        	entity.setOBCost(dataOperate(Double.parseDouble(totalOBCost.toString())));
        	entity.setReceiptCost(dataOperate(Double.parseDouble(totalReceiptCost.toString())));
        	entity.setTBCost(dataOperate(Double.parseDouble(totalTBCost.toString())));
        	}
        	
        	entity.setIssueCost(dataOperate(Double.parseDouble(totalIssueCost.toString())));
        	
        	if(dateMonth.equals("200912"))
        	{
        		entity.setZanShou("2,945,560.35");
        	}
        	else
        	{
        	entity.setZanShou(dataOperate(Double.parseDouble(totalZanShou.toString())));
        	}
        	
        	
        	 if(gc.after(new GregorianCalendar(Integer.parseInt("2010"),Integer.parseInt("02") - 1, 1)))
          	{
        		 entity.setEWOBCost(dataOperate(Double.parseDouble(eWtotalOBCost.toString())-287290.14));
          		
          	}
          	else
          	{
          		entity.setEWOBCost(dataOperate(eWtotalOBCost));
          	}
        	 
        	 if(gc.after(new GregorianCalendar(Integer.parseInt("2010"),Integer.parseInt("01") - 1, 1)))
           	{
         		 entity.setEWTBCost(dataOperate(Double.parseDouble(eWtotalTBCost.toString())-287290.14));
           		
           	}
           	else
           	{
           		entity.setEWTBCost(dataOperate(eWtotalTBCost));
           	}
        	//entity.setEWOBCost(dataOperate(eWtotalOBCost));
        	entity.setEWreceiptCost(dataOperate(eWtotalReceiptCost));
        	entity.setEWissueCost(dataOperate(eWtotalIssueCost));
        	//entity.setEWTBCost(dataOperate(eWtotalTBCost));
        	
        	resultList.add(entity);
        	
        }
        
		
		return resultList;
//	
//		List<WarehouseThisMonthInfo> resultList = new ArrayList<WarehouseThisMonthInfo>();
//		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
//		String falg="0";
//		String balanceSql=
//			"select t.balance_id\n" + 
//			"  from inv_j_balance t\n" + 
//			" where t.balance_year_month = "+dateMonth+"\n" + 
//			"   and rownum = 1";
//		Object obj=bll.getSingal(balanceSql);
//		Long balanceId=0l;
//		if(obj!=null&&!obj.equals(""))
//		{
//			balanceId=Long.parseLong(obj.toString());
//		}
//		else
//		{
//			String maxMonth="select max(t.balance_year_month) from inv_j_balance t where t.is_use = 'Y' ";
//			int year=Integer.parseInt(dateMonth.substring(0, 4));
//			int month=Integer.parseInt(dateMonth.substring(5,6));
//			if(month==1)
//			{
//				month=12;
//				year--;
//			}
//			else
//			{
//				month--;
//			}
//			if(bll.getSingal(maxMonth).toString().equals(year+""+month)||bll.getSingal(maxMonth).toString().equals(year+"0"+month))
//			{
//				     falg="1";
//			}
//		}
//
//       String maxSql=
//			"select max(t.balance_id)\n" + 
//			"  from inv_j_balance t\n" + 
//			" where t.is_use='Y'\n"; 
//	 Long maxBalanceId=Long.parseLong(bll.getSingal(maxSql).toString());		
//      
//	   
//			
//        String sql="";
//        if(falg.equals("0"))
//        {
//        	sql="select d.whs_no, d.whs_name, nvl(c.price,0), nvl(a.inprice,0), nvl(a.outprice,0), nvl(b.price,0),'0'\n" +
//        	"  from (select t.from_whs_no,\n" + 
//        	"\n" + 
//        	"               nvl(sum(decode(t.trans_id, 1, t.trans_qty * t.price, 0)),0) inprice,\n" + 
//        	"\n" + 
//        	"               nvl(sum(decode(t.trans_id, 4, t.trans_qty * t.std_cost, 0)),0) outprice\n" + 
//        	"          from Inv_j_Transaction_His t\n" + 
//        	"         where t.is_use = 'Y'\n" + 
//        	"           and t.trans_his_id >= (select b.trans_his_minid\n" + 
//        	"                                    from inv_j_balance b\n" + 
//        	"                                   where b.balance_id = "+ balanceId +" \n" + 
//        	"                                     and b.is_use = 'Y')\n" + 
//        	"           and t.trans_his_id <= (select b.trans_his_maxid\n" + 
//        	"                                    from inv_j_balance b\n" + 
//        	"                                   where b.balance_id = "+ balanceId +"\n" + 
//        	"                                     and b.is_use = 'Y')\n" + 
//        	"         group by t.from_whs_no) a,\n" + 
//        	"       (select m.default_whs_no, sum(t.trans_qty * t.std_cost) price\n" + 
//        	"          from inv_j_transaction_his t, Inv_c_Material m\n" + 
//        	"         where t.trans_id = 6\n" + 
//        	"           and t.is_use = 'Y'\n";
//           if(maxBalanceId==balanceId)
//           {
//        	   sql+="           and t.trans_his_id >(select b.trans_his_maxid\n" + 
//        	"                                    from inv_j_balance b\n" + 
//        	"                                   where b.balance_id = "+ balanceId+"\n" + 
//        	"                                     and b.is_use = 'Y')\n";
//           }
//           else
//           {
//        	   sql+="           and t.trans_his_id >= (select b.trans_his_minid\n" + 
//        	"                                    from inv_j_balance b\n" + 
//        	"                                   where b.balance_id = "+ (balanceId + 1) +"\n" + 
//        	"                                     and b.is_use = 'Y')\n" + 
//        	"           and t.trans_his_id < (select b.trans_his_maxid\n" + 
//        	"                                   from inv_j_balance b\n" + 
//        	"                                  where b.balance_id = "+ (balanceId + 1) +"\n" + 
//        	"                                    and b.is_use = 'Y')\n";
//           }
//        	
//        	sql+="           and t.material_id = m.material_id\n" + 
//        	"         group by m.default_whs_no) b,\n" + 
//        	"       (select m.default_whs_no, sum(t.trans_qty * t.std_cost) price\n" + 
//        	"          from inv_j_transaction_his t, Inv_c_Material m\n" + 
//        	"         where t.trans_id = 6\n" + 
//        	"           and t.is_use = 'Y'\n" + 
//        	"           and t.trans_his_id >= (select b.trans_his_minid\n" + 
//        	"                                    from inv_j_balance b\n" + 
//        	"                                   where b.balance_id = "+ balanceId +"\n" + 
//        	"                                     and b.is_use = 'Y')\n" + 
//        	"           and t.trans_his_id < (select b.trans_his_maxid\n" + 
//        	"                                   from inv_j_balance b\n" + 
//        	"                                  where b.balance_id = "+ balanceId +"\n" + 
//        	"                                    and b.is_use = 'Y')\n" + 
//        	"           and t.material_id = m.material_id\n" + 
//        	"         group by m.default_whs_no) c,\n" + 
//        	"       inv_c_warehouse d\n" + 
//        	" where d.whs_no = a.from_whs_no(+)\n" + 
//        	"   and d.whs_no = b.default_whs_no(+)\n" + 
//        	"   and d.whs_no = c.default_whs_no(+)\n" + 
//        	"   and d.is_use = 'Y'\n" + 
//        	" order by d.whs_no";
//
//        }
//        else
//        {
//        	
//        	sql=
//        		"select d.whs_no,\n" +
//        		"       d.whs_name,\n" + 
//        		"       nvl(c.price, 0),\n" + 
//        		"       nvl(a.inprice, 0),\n" + 
//        		"       nvl(a.outprice, 0),\n" + 
//        		"       nvl(c.price, 0) + nvl(a.inprice, 0) - nvl(a.outprice, 0),\n" + 
//        		"       '0'\n" + 
//        		"  from (select t.from_whs_no,\n" + 
//        		"               nvl(sum(decode(t.trans_id, 1, t.trans_qty * t.price, 0)), 0) inprice,\n" + 
//        		"\n" + 
//        		"               nvl(sum(decode(t.trans_id, 4, t.trans_qty * t.std_cost, 0)),\n" + 
//        		"                   0) outprice\n" + 
//        		"          from Inv_j_Transaction_His t\n" + 
//        		"         where t.is_use = 'Y'\n" + 
//        		"           and t.trans_his_id >= (select b.trans_his_maxid\n" + 
//        		"                                    from inv_j_balance b\n" + 
//        		"                                   where b.balance_id = "+maxBalanceId+"\n" + 
//        		"                                     and b.is_use = 'Y')\n" + 
//        		"\n" + 
//        		"         group by t.from_whs_no) a,\n" + 
//        		"       (select m.default_whs_no, sum(t.trans_qty * t.std_cost) price\n" + 
//        		"          from inv_j_transaction_his t, Inv_c_Material m\n" + 
//        		"         where t.trans_id = 6\n" + 
//        		"           and t.is_use = 'Y'\n" + 
//        		"           and t.trans_his_id >= (select b.trans_his_maxid\n" + 
//        		"                                    from inv_j_balance b\n" + 
//        		"                                   where b.balance_id = "+maxBalanceId+"\n" + 
//        		"                                     and b.is_use = 'Y')\n" + 
//        		"\n" + 
//        		"           and t.material_id = m.material_id\n" + 
//        		"         group by m.default_whs_no) c,\n" + 
//        		"       inv_c_warehouse d\n" + 
//        		" where d.whs_no = a.from_whs_no(+)\n" + 
//        		"\n" + 
//        		"   and d.whs_no = c.default_whs_no(+)\n" + 
//        		"   and d.is_use = 'Y'\n" + 
//        		" order by d.whs_no";
//        }
//
//		List list = bll.queryByNativeSQL(sql);
//		Double totalReceiptCost=0d;
//		Double totalIssueCost=0d;
//		Double totalTBCost=0d;
//		Double totalOBCost=0d;
//		Double totalZanShou=0d;
//        Iterator it = list.iterator();
//        while (it.hasNext()) {
//        	WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
//            Object[] data = (Object[]) it.next();
//            // 仓库号
//            if(null != data[0])
//            monthInfo.setWhsNo(data[0].toString());
//            // 仓库名称
//            if(null != data[1])
//            monthInfo.setWhsName(data[1].toString());
//    
//            // 上期结存金额
//            if(null != data[2])
//            {
//                monthInfo.setOBCost(data[2].toString());
//                totalOBCost+=Double.parseDouble(monthInfo.getOBCost());
//            }
//            // 本期入库金额
//            if(null != data[3])
//            {
//                monthInfo.setReceiptCost(data[3].toString());
//                totalReceiptCost+=Double.parseDouble(monthInfo.getReceiptCost());
//            }
//            if(null != data[4])
//            {
//                monthInfo.setIssueCost(data[4].toString());
//                totalIssueCost+=Double.parseDouble(monthInfo.getIssueCost());
//            }
//            if(null != data[5])
//            {
//                monthInfo.setTBCost(data[5].toString());
//                totalTBCost+=Double.parseDouble(monthInfo.getTBCost());
//            }
//            if(null!=data[6])
//            {
//            	//暂收
//            	monthInfo.setZanShou(data[6].toString());
//            	totalZanShou+=Double.parseDouble(monthInfo.getZanShou());
//            }
//            
//            arrlist.add(monthInfo);
//        }
////        if(dateMonth.equals("201001"))
////        {
////        WarehouseThisMonthInfo addInfo=new WarehouseThisMonthInfo();
////        
////        addInfo.setWhsNo("");
////        addInfo.setWhsName("调整");
////        arrlist.add(addInfo);
////        }
//        for(int i=0;i<arrlist.size();i++)
//        {
//        	WarehouseThisMonthInfo entity=arrlist.get(i);
//        	if(dateMonth.equals("200912"))
//    		{
//        		
//    			entity.setOBCost(dataOperate(Double.parseDouble(totalOBCost.toString())));
//    			entity.setReceiptCost(dataOperate(Double.parseDouble(totalReceiptCost.toString())-301));
//    			entity.setTBCost(dataOperate(Double.parseDouble(totalTBCost.toString())));
//    		}
//        	else if(dateMonth.equals("201002"))
//    		{
//        		
//    			entity.setOBCost(dataOperate(Double.parseDouble(totalOBCost.toString())));
//    			entity.setReceiptCost(dataOperate(Double.parseDouble(totalReceiptCost.toString())+12193.08));
//    			entity.setTBCost(dataOperate(Double.parseDouble(totalTBCost.toString())));
//    		}
//        	
//
//        	else
//        	{
//        	entity.setOBCost(dataOperate(Double.parseDouble(totalOBCost.toString())));
//        	entity.setReceiptCost(dataOperate(Double.parseDouble(totalReceiptCost.toString())));
//        	entity.setTBCost(dataOperate(Double.parseDouble(totalTBCost.toString())));
//        	}
//        	
//        	entity.setIssueCost(dataOperate(Double.parseDouble(totalIssueCost.toString())));
//        	
////        	if(dateMonth.equals("200912"))
////        	{
////        		entity.setZanShou("2,945,560.35");
////        	}
////        	else
////        	{
//        	entity.setZanShou(dataOperate(Double.parseDouble(totalZanShou.toString())));
////        	}
//        		
//        	
//        	resultList.add(entity);
//        	
//        }
//        
//		
//		return resultList;
	}
	public List<WarehouseThisMonthInfo> getWarehousOfMonthDetailInfo(
			String dateMonth, String whsNo) {

		
    	if(dateMonth.equals("201001")&&whsNo.equals(""))
    	{
    		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
    		WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
    		monthInfo.setWhsNo("扩建");
    		monthInfo.setReceiptCost("-287290.14");
    		arrlist.add(monthInfo);
    		return arrlist;
    	}
    	else
    	{
    	
    		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
    		String balanceSql=
    			"select t.balance_id\n" + 
    			"  from inv_j_balance t\n" + 
    			" where t.balance_year_month = "+dateMonth+"\n" + 
    			"   and rownum = 1";
    		Object obj=bll.getSingal(balanceSql);
    		Long balanceId=0l;
    		if(obj!=null&&!obj.equals(""))
    		{
            balanceId=Long.parseLong(obj.toString());
    		}
           String yString = dateMonth.substring(0,4);
           String mString = dateMonth.substring(4);
           String currentMonth = yString + "-" + mString;
           GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(yString),Integer.parseInt(mString) - 1, 1);
           gc.add(Calendar.MONTH, 1);
        
           System.out.println(gc);
           int nextMInt = gc.get(Calendar.MONTH) + 1;
           String nextM = nextMInt > 9 ? String.valueOf(nextMInt) : "0" + String.valueOf(nextMInt);
           String nextMonth = String.valueOf(gc.get(Calendar.YEAR)) + "-" + nextM;
    			
    			String sql=
//    				"select b.class_no,\n" +
//    				"       b.class_name,\n" + 
//    				"       nvl(t.inprice, 0),\n" + 
//    				"       nvl(t.lastprice, 0),\n" + 
//    				"       nvl(t.outprice, 0),\n" + 
//    				"       nvl(t.thisprice, 0),\n" + 
//    				"       nvl(t.zanshou, 0)\n" + 
//    				"  from (select substr(c.class_no, 0, 3) class_no,\n" + 
//    				"               sum(round(nvl(a.openbalance_price, 0), 2)) lastprice,\n" + 
//    				"               sum(round(nvl(a.check_in_price, 0), 2)) inprice,\n" + 
//    				"               sum(round(nvl(a.check_out_price, 0), 2)) outprice,\n" + 
//    				"               sum(round(nvl(a.openbalance_price, 0), 2) +\n" + 
//    				"                   round(nvl(a.check_in_price, 0), 2) -\n" + 
//    				"                   round(nvl(a.check_out_price, 0), 2)) thisprice,\n" + 
//    				"               sum(round(nvl(a.nocheck_in_price, 0), 2)) zanshou\n" + 
//    				"          from inv_j_transaction_his_money a,\n" + 
//    				"               inv_c_material              m,\n" + 
//    				"               inv_c_material_class        c\n" + 
//    				"         where a.material_id = m.material_id\n" + 
//    				"           and m.maertial_class_id = c.maertial_class_id\n" + 
//    				"           and a.balance_id="+balanceId+" \n"+
//    				"           and a.is_use = 'Y'\n" + 
//    				"           and m.is_use = 'Y'\n" + 
//    				"           and c.is_use = 'Y'\n" + 
//    				"         group by substr(c.class_no, 0, 3)) t,\n" + 
//    				"       inv_c_material_class b\n" + 
//    				" where b.class_no = t.class_no(+)\n" + 
//    				"   and length(b.class_no) = 3\n" + 
//    				"   and b.is_use = 'Y'\n" + 
//    				"   and b.class_no like '"+whsNo+"%'\n" + 
//    				" order by b.class_no asc";

    				"select b.class_no,\n" +
    				"       b.class_name,\n" + 
    				"       nvl(t.inprice, 0),\n" + 
    				"       nvl(t.lastprice, 0),\n" + 
    				"       nvl(t.outprice, 0),\n" + 
    				"       nvl(t.thisprice, 0),\n" + 
    				"       nvl(t.zanshou, 0),\n" + 
    				"         nvl(t1.startprice,0)+nvl(t2.lastprice,0) as whlastprice,\n" + 
    				"       nvl(t3.inprice,0) as whinprice,\n" + 
    				"       nvl(t3.outprice,0) as whoutprice,\n" + 
    				"        nvl(t1.startprice,0)+nvl(t4.thisprice,0) whthisprice\n" + 
    				"  from (select substr(c.class_no, 0, 3) class_no,\n" + 
    				"               sum(round(nvl(a.openbalance_price, 0), 2)) lastprice,\n" + 
    				"               sum(round(nvl(a.check_in_price, 0), 2)) inprice,\n" + 
    				"               sum(round(nvl(a.check_out_price, 0), 2)) outprice,\n" + 
    				"               sum(round(nvl(a.openbalance_price, 0), 2) +\n" + 
    				"                   round(nvl(a.check_in_price, 0), 2) -\n" + 
    				"                   round(nvl(a.check_out_price, 0), 2)) thisprice,\n" + 
    				"               sum(round(nvl(a.nocheck_in_price, 0), 2)) zanshou\n" + 
    				"          from inv_j_transaction_his_money a,\n" + 
    				"               inv_c_material              m,\n" + 
    				"               inv_c_material_class        c\n" + 
    				"         where a.material_id = m.material_id\n" + 
    				"           and m.maertial_class_id = c.maertial_class_id\n" + 
    				"           and a.balance_id = "+balanceId+" \n" + 
    				"           and a.is_use = 'Y'\n" + 
    				"           and m.is_use = 'Y'\n" + 
    				"           and c.is_use = 'Y'\n" + 
    				"         group by substr(c.class_no, 0, 3)) t,\n" + 
    				"       inv_c_material_class b,\n" + 
    				"       (select substr(a.material_no, 0, 3) classno,\n" + 
    				"               sum(round(nvl(t.trans_qty * t.std_cost, 0), 2)) startprice\n" + 
    				"          from inv_j_transaction_his t, inv_c_material a\n" + 
    				"         where t.trans_his_id <= (select a.trans_his_maxid\n" + 
    				"                                    from inv_j_balance a\n" + 
    				"                                   where a.balance_id = 2)\n" + 
    				"           and t.trans_id = 6\n" + 
    				"           and t.material_id = a.material_id\n" + 
    				"           and t.is_use = 'Y'\n" + 
    				"           and a.is_use = 'Y'\n" + 
    				"         group by substr(a.material_no, 0, 3)) t1,\n" + 
    				"       (select substr(a.material_no, 0, 3) classno,\n" + 
    				"               sum(round(decode(t.trans_id,\n" + 
    				"                                1,\n" + 
    				"                                t.trans_qty * t.price,\n" + 
    				"                                4,\n" + 
    				"                                -t.trans_qty * t.std_cost,\n" + 
    				"                                0),\n" + 
    				"                         2)) lastprice\n" + 
    				"          from inv_j_transaction_his t, inv_c_material a\n" + 
    				"         where t.trans_id in (1, 4)\n" + 
    				"           and t.is_use = 'Y'\n" + 
    				"           and t.material_id = a.material_id\n" + 
    				"           and a.is_use = 'Y'\n" + 
    				"           and t.last_modified_date <\n" + 
    				"               to_date('"+currentMonth+"' || '-01 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
    				"         group by substr(a.material_no, 0, 3)) t2,\n" + 
    				"       (select substr(a.material_no, 0, 3) classno,\n" + 
    				"               sum(round(decode(t.trans_id, 1, t.trans_qty * t.price, 0), 2)) inprice,\n" + 
    				"               sum(round(decode(t.trans_id, 4, t.trans_qty * t.std_cost, 0),\n" + 
    				"                         2)) outprice\n" + 
    				"          from inv_j_transaction_his t, inv_c_material a\n" + 
    				"         where t.trans_id in (1, 4)\n" + 
    				"           and t.is_use = 'Y'\n" + 
    				"           and t.material_id = a.material_id\n" + 
    				"           and a.is_use = 'Y'\n" + 
    				"           and t.last_modified_date >=\n" + 
    				"               to_date('"+currentMonth+"' || '-01 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
    				"           and t.last_modified_date <\n" + 
    				"               to_date('"+nextMonth+"' || '-01 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
    				"         group by substr(a.material_no, 0, 3)) t3,\n" + 
    				"       (select substr(a.material_no, 0, 3) classno,\n" + 
    				"               sum(round(decode(t.trans_id,\n" + 
    				"                                1,\n" + 
    				"                                t.trans_qty * t.price,\n" + 
    				"                                4,\n" + 
    				"                                -t.trans_qty * t.std_cost,\n" + 
    				"                                0),\n" + 
    				"                         2)) thisprice\n" + 
    				"          from inv_j_transaction_his t, inv_c_material a\n" + 
    				"         where t.trans_id in (1, 4)\n" + 
    				"           and t.is_use = 'Y'\n" + 
    				"           and t.material_id = a.material_id\n" + 
    				"           and a.is_use = 'Y'\n" + 
    				"           and t.last_modified_date <\n" + 
    				"               to_date('"+nextMonth+"' || '-01 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
    				"         group by substr(a.material_no, 0, 3)\n" + 
    				"\n" + 
    				"        ) t4\n" + 
    				" where b.class_no = t.class_no(+)\n" + 
    				"   and length(b.class_no) = 3\n" + 
    				"   and b.is_use = 'Y'\n" + 
    				"   and b.class_no = t1.classno(+)\n" + 
    				"   and b.class_no = t2.classno(+)\n" + 
    				"   and b.class_no = t3.classno(+)\n" + 
    				"   and b.class_no = t4.classno(+)\n" + 
    				"   and b.class_no like '"+whsNo+"%'\n" + 
    				" order by b.class_no asc";




    		List list = bll.queryByNativeSQL(sql);
    		Double totalReceiptCost=0d;
    		Double totalIssueCost=0d;
    		Double totalTBCost=0d;
    		Double totalOBCost=0d;
    		Double totalZanShou=0d;
    		
    		Double eWtotalReceiptCost=0d;
    		Double eWtotalIssueCost=0d;
    		Double eWtotalTBCost=0d;
    		Double eWtotalOBCost=0d;
            Iterator it = list.iterator();
            while (it.hasNext()) {
            	WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
                Object[] data = (Object[]) it.next();
                // 类别编码
                if(null != data[0])
                monthInfo.setWhsNo(data[0].toString());
                // 类别名称
                if(null != data[1])
                monthInfo.setWhsName(data[1].toString());
                // 本期入库金额
                if(null != data[2])
                	
                {
                    monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())));
                    totalReceiptCost+=Double.parseDouble(data[2].toString());
                }
                // 上期结存金额
                if(null != data[3])
                {
                	
//                  if(dateMonth.equals("201002"))
//                 {
//                	 if(monthInfo.getWhsNo().equals("5-0"))
//                  	{
//                  		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())-287290.14));
//                  		 totalOBCost+=Double.parseDouble(data[3].toString())-287290.14;
//                  	}
//                	 else
//                  	{
//                  		 monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())));
//                          totalOBCost+=Double.parseDouble(data[3].toString());
//                  	}
//                 }
//                 else
//                 {
                	 monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())));
                     totalOBCost+=Double.parseDouble(data[3].toString());
//                 }
                 
                 
                 
                
                }
             // 本期出库金额
                if(null != data[4])
                {
                	
                   monthInfo.setIssueCost(dataOperate(Double.parseDouble(data[4].toString())));
                   totalIssueCost+=Double.parseDouble(data[4].toString());
                }
                // 本期结余金额
                if(null != data[5])
                {
                	if(dateMonth.equals("201001")&&monthInfo.getWhsNo().equals("5-0"))
                	{
                		monthInfo.setTBCost(dataOperate(Double.parseDouble(data[5].toString())-287290.14));
                        totalTBCost+=Double.parseDouble(data[5].toString())-287290.14;
                	}
                	else
                	{
                		monthInfo.setTBCost(dataOperate(Double.parseDouble(data[5].toString())));
                        totalTBCost+=Double.parseDouble(data[5].toString());
                	}
                }
                //本期暂收金额
                 if(null!=data[6])
                 {
                	 monthInfo.setZanShou(dataOperate(Double.parseDouble(data[6].toString())));
                	 totalZanShou+=Double.parseDouble(data[6].toString());
                 }
                 // add by liuyi 20100412 
                 if(null != data[7])
                 {
                	 if(gc.after(new GregorianCalendar(Integer.parseInt("2010"),Integer.parseInt("02") - 1, 1))&&monthInfo.getWhsNo().equals("5-0"))
                  	{
                  		monthInfo.setEWOBCost(dataOperate(Double.parseDouble(data[7].toString())-287290.14));
                  		eWtotalOBCost+=Double.parseDouble(data[7].toString())-287290.14;
                  	}
                  	else
                  	{
                  		monthInfo.setEWOBCost(data[7].toString());
                     	eWtotalOBCost += Double.parseDouble(monthInfo.getEWOBCost());
                  	}
//                	 monthInfo.setEWOBCost(data[7].toString());
//                  	eWtotalOBCost += Double.parseDouble(monthInfo.getEWOBCost());
                 }
                 if(null != data[8])
                 {
                 	monthInfo.setEWreceiptCost(data[8].toString());
                 	eWtotalReceiptCost += Double.parseDouble(monthInfo.getEWreceiptCost());
                 }
                 if(null != data[9])
                 {
                 	monthInfo.setEWissueCost(data[9].toString());
                 	eWtotalIssueCost += Double.parseDouble(monthInfo.getEWissueCost());
                 }
                 if(null != data[10])
                 {
                	 if(gc.after(new GregorianCalendar(Integer.parseInt("2010"),Integer.parseInt("01") - 1, 1))&&monthInfo.getWhsNo().equals("5-0"))
                 	{
                 		monthInfo.setEWTBCost(dataOperate(Double.parseDouble(data[10].toString())-287290.14));
                 		eWtotalTBCost+=Double.parseDouble(data[10].toString())-287290.14;
                 	}
                 	else
                 	{
                 		monthInfo.setEWTBCost(data[10].toString());
                     	eWtotalTBCost += Double.parseDouble(monthInfo.getEWTBCost());
                 	}
//                 	monthInfo.setEWTBCost(data[10].toString());
//                 	eWtotalTBCost += Double.parseDouble(monthInfo.getEWTBCost());
                 }
                 
                arrlist.add(monthInfo);
            }
            WarehouseThisMonthInfo entity = new WarehouseThisMonthInfo();
            entity.setWhsNo("小计");
           
            
            	entity.setOBCost(dataOperate(totalOBCost));
            
            entity.setReceiptCost(dataOperate(totalReceiptCost));
            
            entity.setIssueCost(dataOperate(totalIssueCost));
            entity.setTBCost(dataOperate(totalTBCost));
            entity.setZanShou(dataOperate(totalZanShou));
            
            entity.setEWOBCost(dataOperate(eWtotalOBCost));
        	entity.setEWreceiptCost(dataOperate(eWtotalReceiptCost));
        	entity.setEWissueCost(dataOperate(eWtotalIssueCost));
        	entity.setEWTBCost(dataOperate(eWtotalTBCost));
            
            arrlist.add(entity);
            
    		
    		return arrlist;
    	}
    	
    	
//    	
//    		List<WarehouseThisMonthInfo> arrlist = new ArrayList<WarehouseThisMonthInfo>();
//    		
//    		String falg="0";
//    		String balanceSql=
//    			"select t.balance_id\n" + 
//    			"  from inv_j_balance t\n" + 
//    			" where t.balance_year_month = "+dateMonth+"\n" + 
//    			"   and rownum = 1";
//    		Object obj=bll.getSingal(balanceSql);
//    		Long balanceId=0l;
//    		if(obj!=null&&!obj.equals(""))
//    		{
//    			balanceId=Long.parseLong(obj.toString());
//    		}
//    		else
//    		{
//    			String maxMonth="select max(t.balance_year_month) from inv_j_balance t where t.is_use = 'Y' ";
//    			int year=Integer.parseInt(dateMonth.substring(0, 4));
//    			int month=Integer.parseInt(dateMonth.substring(5,6));
//    			if(month==1)
//    			{
//    				month=12;
//    				year--;
//    			}
//    			else
//    			{
//    				month--;
//    			}
//    			if(bll.getSingal(maxMonth).toString().equals(year+""+month)||bll.getSingal(maxMonth).toString().equals(year+"0"+month))
//    			{
//    				     falg="1";
//    			}
//    		}
//
//           String maxSql=
//    			"select max(t.balance_id)\n" + 
//    			"  from inv_j_balance t\n" + 
//    			" where t.is_use='Y'\n"; 
//    	 Long maxBalanceId=Long.parseLong(bll.getSingal(maxSql).toString());	
//    	 String sql="";
//    	 if(falg.equals("0"))
//    	 {
//    			 sql=
//    				"select d.class_no, d.class_name, nvl(a.inprice,0), nvl(c.price,0), nvl(a.outprice,0), nvl(b.price,0) bprice\n" +
//    				"  from (select substr(m.material_no, 0, 3) class_no,\n" + 
//    				"\n" + 
//    				"               sum(decode(t.trans_id, 1, t.trans_qty * t.price, 0)) inprice,\n" + 
//    				"\n" + 
//    				"               sum(decode(t.trans_id, 4, t.trans_qty * t.std_cost, 0)) outprice\n" + 
//    				"          from Inv_j_Transaction_His t, inv_c_material m\n" + 
//    				"         where t.is_use = 'Y'\n" + 
//    				"           and t.trans_his_id >= (select b.trans_his_minid\n" + 
//    				"                                    from inv_j_balance b\n" + 
//    				"                                   where b.balance_id = "+balanceId+"\n" + 
//    				"                                     and b.is_use = 'Y')\n" + 
//    				"           and t.trans_his_id <= (select b.trans_his_maxid\n" + 
//    				"                                    from inv_j_balance b\n" + 
//    				"                                   where b.balance_id = "+balanceId+"\n" + 
//    				"                                     and b.is_use = 'Y')\n" + 
//    				"           and m.material_id = t.material_id\n" + 
//    				"         group by substr(m.material_no, 0, 3)) a,\n" + 
//    				"       (select substr(m.material_no, 0, 3) class_no,\n" + 
//    				"               sum(t.trans_qty * t.std_cost) price\n" + 
//    				"          from inv_j_transaction_his t, Inv_c_Material m\n" + 
//    				"         where t.trans_id = 6\n" + 
//    				"           and t.is_use = 'Y'\n";
//    			    if(maxBalanceId==balanceId)
//    			    {
//    			    	sql+="           and t.trans_his_id >= (select b.trans_his_maxid\n" + 
//        				"                                    from inv_j_balance b\n" + 
//        				"                                   where b.balance_id = "+balanceId +"\n" + 
//        				"                                     and b.is_use = 'Y')\n";
//    			    }
//    			    else
//    			    {
//    			    	sql+="           and t.trans_his_id >= (select b.trans_his_minid\n" + 
//        				"                                    from inv_j_balance b\n" + 
//        				"                                   where b.balance_id = "+(balanceId + 1)+"\n" + 
//        				"                                     and b.is_use = 'Y')\n" + 
//        				"           and t.trans_his_id < (select b.trans_his_maxid\n" + 
//        				"                                   from inv_j_balance b\n" + 
//        				"                                  where b.balance_id = "+(balanceId + 1)+"\n" + 
//        				"                                    and b.is_use = 'Y')\n";
//    			    }
//    				
//    				sql+="           and t.material_id = m.material_id\n" + 
//    				"         group by substr(m.material_no, 0, 3)) b,\n" + 
//    				"       (select substr(m.material_no, 0, 3) class_no,\n" + 
//    				"               sum(t.trans_qty * t.std_cost) price\n" + 
//    				"          from inv_j_transaction_his t, Inv_c_Material m\n" + 
//    				"         where t.trans_id = 6\n" + 
//    				"           and t.is_use = 'Y'\n" + 
//    				"           and t.trans_his_id >= (select b.trans_his_minid\n" + 
//    				"                                    from inv_j_balance b\n" + 
//    				"                                   where b.balance_id = "+balanceId+"\n" + 
//    				"                                     and b.is_use = 'Y')\n" + 
//    				"           and t.trans_his_id < (select b.trans_his_maxid\n" + 
//    				"                                   from inv_j_balance b\n" + 
//    				"                                  where b.balance_id = "+balanceId+"\n" + 
//    				"                                    and b.is_use = 'Y')\n" + 
//    				"           and t.material_id = m.material_id\n" + 
//    				"         group by substr(m.material_no, 0, 3)) c,\n" + 
//    				"       inv_c_material_class d\n" + 
//    				" where d.class_no like '"+whsNo+"' || '%'\n" + 
//    				"   and length(d.class_no) = 3\n" + 
//    				"   and d.class_no = a.class_no(+)\n" + 
//    				"   and d.class_no = b.class_no(+)\n" + 
//    				"   and d.class_no = c.class_no(+)\n" + 
//    				"   and d.is_use = 'Y'\n" + 
//    				" order by d.class_no";
//    	 }
//    	 else
//    	 {
//
//    				
//    				sql=
//    					"select d.class_no,\n" +
//    					"       d.class_name,\n" + 
//    					"       nvl(a.inprice, 0),\n" + 
//    					"       nvl(c.price, 0),\n" + 
//    					"       nvl(a.outprice, 0),\n" + 
//    					"       nvl(a.inprice, 0) + nvl(c.price, 0) - nvl(a.outprice, 0) bprice\n" + 
//    					"  from (select substr(m.material_no, 0, 3) class_no,\n" + 
//    					"\n" + 
//    					"               sum(decode(t.trans_id, 1, t.trans_qty * t.price, 0)) inprice,\n" + 
//    					"\n" + 
//    					"               sum(decode(t.trans_id, 4, t.trans_qty * t.std_cost, 0)) outprice\n" + 
//    					"          from Inv_j_Transaction_His t, inv_c_material m\n" + 
//    					"         where t.is_use = 'Y'\n" + 
//    					"           and t.trans_his_id >= (select b.trans_his_maxid\n" + 
//    					"                                    from inv_j_balance b\n" + 
//    					"                                   where b.balance_id = "+maxBalanceId+"\n" + 
//    					"                                     and b.is_use = 'Y')\n" + 
//    					"\n" + 
//    					"           and m.material_id = t.material_id\n" + 
//    					"         group by substr(m.material_no, 0, 3)) a,\n" + 
//    					"\n" + 
//    					"       (select substr(m.material_no, 0, 3) class_no,\n" + 
//    					"               sum(t.trans_qty * t.std_cost) price\n" + 
//    					"          from inv_j_transaction_his t, Inv_c_Material m\n" + 
//    					"         where t.trans_id = 6\n" + 
//    					"           and t.is_use = 'Y'\n" + 
//    					"           and t.trans_his_id >= (select b.trans_his_maxid\n" + 
//    					"                                    from inv_j_balance b\n" + 
//    					"                                   where b.balance_id = "+maxBalanceId+"\n" + 
//    					"                                     and b.is_use = 'Y')\n" + 
//    					"\n" + 
//    					"           and t.material_id = m.material_id\n" + 
//    					"         group by substr(m.material_no, 0, 3)) c,\n" + 
//    					"       inv_c_material_class d\n" + 
//    					" where d.class_no like '"+whsNo+"' || '%'\n" + 
//    					"   and length(d.class_no) = 3\n" + 
//    					"   and d.class_no = a.class_no(+)\n" + 
//    					"\n" + 
//    					"   and d.class_no = c.class_no(+)\n" + 
//    					"   and d.is_use = 'Y'\n" + 
//    					" order by d.class_no";
//    	 }
//
//
//    		List list = bll.queryByNativeSQL(sql);
//    		Double totalReceiptCost=0d;
//    		Double totalIssueCost=0d;
//    		Double totalTBCost=0d;
//    		Double totalOBCost=0d;
//    		Double totalZanShou=0d;
//    		
//            Iterator it = list.iterator();
//            while (it.hasNext()) {
//            	WarehouseThisMonthInfo monthInfo = new WarehouseThisMonthInfo();
//                Object[] data = (Object[]) it.next();
//                // 类别编码
//                if(null != data[0])
//                monthInfo.setWhsNo(data[0].toString());
//                // 类别名称
//                if(null != data[1])
//                monthInfo.setWhsName(data[1].toString());
//                // 本期入库金额
//                if(null != data[2])
//                	
//                {
//                	if(dateMonth.equals("200912"))
//                	{
//                		if(monthInfo.getWhsNo().equals("4-1"))
//                		{
//                			
//                			monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())+255));
//                            totalReceiptCost+=Double.parseDouble(data[2].toString())+255;
//                		}
//                		else if(monthInfo.getWhsNo().equals("5-D"))
//                		{
//                			
//                			monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())-56));
//                            totalReceiptCost+=Double.parseDouble(data[2].toString())-56;
//                		}
//                		else if(monthInfo.getWhsNo().equals("5-N"))
//                		{
//                			
//                			monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())-300));
//                            totalReceiptCost+=Double.parseDouble(data[2].toString())-300;
//                		}
//                		else if(monthInfo.getWhsNo().equals("B-H"))
//                		{
//                			
//                			monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())-200));
//                            totalReceiptCost+=Double.parseDouble(data[2].toString())-200;
//                		}
//                		else
//                		{
//                			 monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())));
//                             totalReceiptCost+=Double.parseDouble(data[2].toString());
//                		}
//                	}
//                	else	if(dateMonth.equals("201002"))
//                	{
//                		if(monthInfo.getWhsNo().equals("3-2"))
//                		{
//                			
//                			monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())-3.18));
//                            totalReceiptCost+=Double.parseDouble(data[2].toString())-3.18;
//                		}
//                		else if(monthInfo.getWhsNo().equals("3-3"))
//                		{
//                			
//                			monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())+127.24));
//                            totalReceiptCost+=Double.parseDouble(data[2].toString())+127.24;
//                		}
//                		else if(monthInfo.getWhsNo().equals("5-D"))
//                		{
//                			
//                			monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())+728.61));
//                            totalReceiptCost+=Double.parseDouble(data[2].toString())+728.61;
//                		}
//                		else if(monthInfo.getWhsNo().equals("5-E"))
//                		{
//                			
//                			monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())-254.56));
//                            totalReceiptCost+=Double.parseDouble(data[2].toString())-254.56;
//                		}
//                		else if(monthInfo.getWhsNo().equals("5-N"))
//                		{
//                			
//                			monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())+4350));
//                            totalReceiptCost+=Double.parseDouble(data[2].toString())+4350;
//                		}
//                		else if(monthInfo.getWhsNo().equals("4-1"))
//                		{
//                			
//                			monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())-255.01));
//                            totalReceiptCost+=Double.parseDouble(data[2].toString())-255.01;
//                		}
//                		else if(monthInfo.getWhsNo().equals("B-H"))
//                		{
//                			
//                			monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())+7500));
//                            totalReceiptCost+=Double.parseDouble(data[2].toString())+7500;
//                		}
//                		else
//                		{
//                			 monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())));
//                             totalReceiptCost+=Double.parseDouble(data[2].toString());
//                		}
//                	}
//                	else
//                	{
//                    monthInfo.setReceiptCost(dataOperate(Double.parseDouble(data[2].toString())));
//                    totalReceiptCost+=Double.parseDouble(data[2].toString());
//                	}
//                }
//                // 上期结存金额
//                if(null != data[3])
//                {
//                	
////                  if(dateMonth.equals("201002"))
////                 {
////                	 if(monthInfo.getWhsNo().equals("5-0"))
////                  	{
////                  		monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())-287290.14));
////                  		 totalOBCost+=Double.parseDouble(data[3].toString())-287290.14;
////                  	}
////                	 else
////                  	{
////                  		 monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())));
////                          totalOBCost+=Double.parseDouble(data[3].toString());
////                  	}
////                 }
////                 else
////                 {
//                	 monthInfo.setOBCost(dataOperate(Double.parseDouble(data[3].toString())));
//                     totalOBCost+=Double.parseDouble(data[3].toString());
////                 }
//                 
//                 
//                 
//                
//                }
//             // 本期出库金额
//                if(null != data[4])
//                {
//                	
//                   monthInfo.setIssueCost(dataOperate(Double.parseDouble(data[4].toString())));
//                   totalIssueCost+=Double.parseDouble(data[4].toString());
//                }
//                // 本期结余金额
//                if(null != data[5])
//                {
////                	if(dateMonth.equals("201001")&&monthInfo.getWhsNo().equals("5-0"))
////                	{
////                		monthInfo.setTBCost(dataOperate(Double.parseDouble(data[5].toString())-287290.14));
////                        totalTBCost+=Double.parseDouble(data[5].toString())-287290.14;
////                	}
////                	else
////                	{
//                		monthInfo.setTBCost(dataOperate(Double.parseDouble(data[5].toString())));
//                        totalTBCost+=Double.parseDouble(data[5].toString());
////                	}
//                }
//                //本期暂收金额
////                 if(null!=data[6])
////                 {
////                	 monthInfo.setZanShou(dataOperate(Double.parseDouble(data[6].toString())));
//                monthInfo.setZanShou("0.00");
////                	 totalZanShou+=Double.parseDouble(data[6].toString());
////                 }
//                arrlist.add(monthInfo);
//            }
//            WarehouseThisMonthInfo entity = new WarehouseThisMonthInfo();
//            entity.setWhsNo("小计");
//           
//            
//            	entity.setOBCost(dataOperate(totalOBCost));
//            
//            entity.setReceiptCost(dataOperate(totalReceiptCost));
//            
//            entity.setIssueCost(dataOperate(totalIssueCost));
//            entity.setTBCost(dataOperate(totalTBCost));
//            entity.setZanShou(dataOperate(totalZanShou));
//            arrlist.add(entity);
//            
//    		
//    		return arrlist;
//    	
//    	
	}

}
