/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.receptioncoststatistics.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.business.ReceptionCostStatisticsFacadeRemote;
import power.ejb.administration.form.ReceptionCostStatisticsInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.ExportXsl;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 接待费用统计
 * 
 * @author zhouxu
 * 
 */
@SuppressWarnings("serial")
public class ReceptionCostStatisticsAction extends AbstractAction {
    /** 接待费用查询的实现类接口 */
    private ReceptionCostStatisticsFacadeRemote remote;
    /** 画面参数开始时间* */
    public String dteStartDate;
    /** 画面参数结束时间* */
    public String dteEndDate;
    /** 定义接待费用常量 */
    private String STR_FILE_NAME = "接待费用统计";
    /** 定义空字符串 */
    private String STR_BLANK = "";
    /** 超支：是 */
    private String STR_YES = "是";
    /** 超支：否 */
    private String STR_NO = "否";
    /** 合计常量 */
    private String STR_TOTAL = "合计";

    public ReceptionCostStatisticsAction() {
        remote = (ReceptionCostStatisticsFacadeRemote) factory.getFacadeRemote("ReceptionCostStatisticsFacade");
    }

    /**
     * 获取接待费用数据
     */
    public void getReceptionExpense() {
        PageObject objResult = null;
        String str = "";
        LogUtil.log("Action:接待费用统计查询开始", Level.INFO, null);
        try {
            // 调用查询接待费用的方法
            objResult = remote.findByFuzzy(dteStartDate, dteEndDate, employee.getEnterpriseCode());
            request.getSession().setAttribute("pageObjReceptionCost", objResult);
            if (objResult.getTotalCount() > 0) {
                str = JSONUtil.serialize(objResult);
            } else {
                str = "{\"list\":[],\"totalCount\":null}";
            }
            write(str);
            LogUtil.log("Action:接待费用统计查询结束", Level.INFO, null);
        } catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:接待费用统计查询失败", Level.SEVERE, jsone);
        } catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:接待费用统计查询失败。", Level.SEVERE, sqle);
        }
    }

    /**
     * 导出文件
     * 
     */
    @SuppressWarnings("unchecked")
    public void exportReceptionCostFile() {
        try {
            LogUtil.log("Action:导出文件开始", Level.INFO, null);
            // 操作excel文件对象
            ExportXsl exsl = new ExportXsl();
            // 设置response
            exsl.setResponse(response);
            // 当前时间作为文件名以部分
            Date dte = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            String strDate = sdf.format(dte);
            // 设定文件名
            exsl.setStrFileName(STR_FILE_NAME + strDate);
            // excel文件每列标题头
            List<String> lstHeader = new ArrayList<String>();
            lstHeader.add("行号");
            lstHeader.add("审批单号");
            lstHeader.add("申请人");
            lstHeader.add("申请部门");
            lstHeader.add("接待日期");
            lstHeader.add("就餐费用(元)");
            lstHeader.add("住宿费用(元)");
            lstHeader.add("标准支出(元)");
            lstHeader.add("实际支出(元)");
            lstHeader.add("是否超支");
            exsl.setLstTitle(lstHeader);
            /** 就餐费用合计* */
            Double dblRepastTotal = 0.00;
            /** 住宿费用合计 */
            Double dblRoomTotal = 0.00;
            /** 标准支出合计 */
            Double dblPayoutBz = 0.00;
            /** 实际支出合计 */
            Double dblPayout = 0.00;
            // excel文件中的一行
            List<String> lstRow = null;
            // excel文件中的所有行集合
            List lstRowSet = new ArrayList();
            // excel文件单行实体
            ReceptionCostStatisticsInfo tempCostInfo = null;
            // 获得需要导出的excel文件内容
            PageObject pageObj = (PageObject) request.getSession().getAttribute("pageObjReceptionCost");
            for (int i = 0; i < pageObj.getList().size(); i++) {
                lstRow = new ArrayList();
                tempCostInfo = (ReceptionCostStatisticsInfo) pageObj.getList().get(i);
                // 设置行号
                lstRow.add(i + 1 + STR_BLANK);
                // 设置申请单号
                if (tempCostInfo.getApplyId() == null) {
                    lstRow.add(STR_BLANK);
                } else {
                    lstRow.add(tempCostInfo.getApplyId());
                }
                // 设置申请人
                if (tempCostInfo.getName() == null) {
                    lstRow.add(STR_BLANK);
                } else {
                    lstRow.add(tempCostInfo.getName());
                }
                // 设置申请部门
                if (tempCostInfo.getDeptName() == null) {
                    lstRow.add(STR_BLANK);
                } else {
                    lstRow.add(tempCostInfo.getDeptName());
                }
                // 设置接待日期
                if (tempCostInfo.getMeetDate() == null) {
                    lstRow.add(STR_BLANK);
                } else {
                    lstRow.add(tempCostInfo.getMeetDate());
                }
                // 设置就餐费用
                if (tempCostInfo.getRepastTotal() == null) {
                    lstRow.add(STR_BLANK);
                } else {
                    lstRow.add(tempCostInfo.getRepastTotal());
                    dblRepastTotal += Double.parseDouble(tempCostInfo.getRepastTotal());
                }
                // 设置住宿费用
                if (tempCostInfo.getRoomTotal() == null) {
                    lstRow.add(STR_BLANK);
                } else {
                    lstRow.add(tempCostInfo.getRoomTotal());
                    dblRoomTotal += Double.parseDouble(tempCostInfo.getRoomTotal());
                }
                // 设置标准支出
                if (tempCostInfo.getPayoutBz() == null) {
                    lstRow.add(STR_BLANK);
                } else {
                    lstRow.add(tempCostInfo.getPayoutBz());
                    dblPayoutBz += Double.parseDouble(tempCostInfo.getPayoutBz());
                }
                // 设置实际费用
                if (tempCostInfo.getPayout() == null) {
                    lstRow.add(STR_BLANK);
                } else {
                    lstRow.add(tempCostInfo.getPayout());
                    dblPayout += Double.parseDouble(tempCostInfo.getPayout());
                }
                // 设置是否超支
                if (tempCostInfo.getBalance() == null) {
                    lstRow.add(STR_NO);
                } else {
                    double dblBalance = Double.parseDouble(tempCostInfo.getBalance());
                    if (dblBalance >= 0) {
                        lstRow.add(STR_NO);
                    } else {
                        lstRow.add(STR_YES);
                    }
                }
                lstRowSet.add(lstRow);
            }
            lstRow = new ArrayList();
            lstRow.add(STR_TOTAL);
            lstRow.add(STR_BLANK);
            lstRow.add(STR_BLANK);
            lstRow.add(STR_BLANK);
            lstRow.add(STR_BLANK);
            lstRow.add(doubleFormat(dblRepastTotal));
            lstRow.add(doubleFormat(dblRoomTotal));
            lstRow.add(doubleFormat(dblPayoutBz));
            lstRow.add(doubleFormat(dblPayout));
            lstRow.add(STR_BLANK);
            lstRowSet.add(lstRow);
            // 设置所有行内容
            exsl.setLstRow(lstRowSet);
            // 创建导出excel文件
            exsl.createXsl();
            LogUtil.log("Action:导出文件结束", Level.INFO, null);
        } catch (IOException e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:导出文件失败。", Level.SEVERE, e);
        } catch (Exception e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:导出文件失败。", Level.SEVERE, e);
        }

    }

    /**
     * @return the dteStartDate
     */
    public String getDteStartDate() {
        return dteStartDate;
    }

    /**
     * @param dteStartDate
     *            the dteStartDate to set
     */
    public void setDteStartDate(String dteStartDate) {
        this.dteStartDate = dteStartDate;
    }

    /**
     * @return the dteEndDate
     */
    public String getDteEndDate() {
        return dteEndDate;
    }

    /**
     * @param dteEndDate
     *            the dteEndDate to set
     */
    public void setDteEndDate(String dteEndDate) {
        this.dteEndDate = dteEndDate;
    }

    /**
     * double转string保留2位小数
     * @param d
     * @return
     */
    public static String doubleFormat(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

}
