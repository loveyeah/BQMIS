/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.MeetCostStatisticsInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 会务费用统计.
 * 
 * @author zhouxu
 */
@Stateless
public class MeetCostStatisticsFacade implements MeetCostStatisticsFacadeRemote {
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**
     * 根据开始时间，结束时间查询会务费用统计
     * @param strStartDate
     * @param strEndDate
     * @return PageObject
     */
    @SuppressWarnings("unchecked")
    public PageObject findByFuzzy(String startDate, String endDate, String enterpriseCode) throws SQLException{
        try {
            // 定义查询sql语句 
            String strSql = "SELECT A.MEET_ID , " 
                + "     B.DEPT_NAME , " 
                + "     C.CHS_NAME ,"
                + "     A.DINNER_NUM * A.DINNER_BZ, "
                + "     A.CIG_PRICE * A.CIG_NUM , " 
                + "     A.WINE_PRICE * A.WINE_NUM ,"
                + "     NVL(A.TF_NUM * ROOMTF.PRICE,0) + NVL(A.DJ_NUM * ROOMDJ.PRICE,0) + NVL(A.BJ_NUM * ROOMBJ.PRICE,0) ,"
                + "     A.REALPAY_INALL ,"
                + "     NVL(A.REALPAY_INALL,0) - NVL(A.BUDPAY_INALL,0) ,"
                + "     to_char(A.STARTMEET_DATE,'YYYY-mm-dd HH24:mi') , " 
                + "     A.MEET_NAME ," 
                + "     A.BUDPAY_INALL"
                + " FROM AD_J_MEET A  LEFT JOIN HR_J_EMP_INFO C "
                + " ON  A.APPLY_MAN = C.EMP_CODE  AND C.ENTERPRISE_CODE = ? "
                + " LEFT JOIN HR_C_DEPT B"
                + " ON  C.DEPT_ID = B.DEPT_ID  AND B.ENTERPRISE_CODE = ?"
                + " LEFT JOIN AD_J_ROOM_PRICE ROOMDJ ON ROOMDJ.IS_USE= ? AND ROOMDJ.ROOM_TYPE_CODE= ? "
                + " LEFT JOIN AD_J_ROOM_PRICE ROOMTF ON ROOMTF.IS_USE= ? AND ROOMTF.ROOM_TYPE_CODE= ? "
                + " LEFT JOIN AD_J_ROOM_PRICE ROOMBJ ON ROOMBJ.IS_USE= ? AND ROOMBJ.ROOM_TYPE_CODE= ? "
                + " WHERE  "
                + "     A.IS_USE = ? "
                + "     AND A.DCM_STATUS = ? "
                + "     AND A.ENTERPRISE_CODE = ?";

            int paramsCnt = 11;
            // 如果开始日期不为空
            if(checkIsNotNull(startDate)){
                paramsCnt++;
            }
            // 如果结束日期不为空
            if(checkIsNotNull(endDate)){
                paramsCnt++;
            }
            Object[] params = new Object[paramsCnt];
            int i = 0 ;
            params[i++] = enterpriseCode ;
            params[i++] = enterpriseCode ;
            params[i++] = "Y" ;
            params[i++] = "01" ;
            params[i++] = "Y" ;
            params[i++] = "02" ;
            params[i++] = "Y" ;
            params[i++] = "03" ;
            params[i++] = "Y" ;
            params[i++] = "2" ;
            params[i++] = enterpriseCode ;
            if(checkIsNotNull(startDate)){
                strSql += "AND to_char(A.STARTMEET_DATE , 'yyyy-MM-dd') >= ?";
                params[i++] = startDate ;
            }
            if(checkIsNotNull(endDate)){
                strSql += "AND to_char(A.STARTMEET_DATE , 'yyyy-MM-dd') <= ?";
                params[i++] = endDate ;
            }
            List list=new ArrayList();
            list = bll.queryByNativeSQL(strSql,params);
            PageObject pObj = new PageObject();
            List<MeetCostStatisticsInfo> arrList = new ArrayList<MeetCostStatisticsInfo>();
            Iterator it=list.iterator();
            while(it.hasNext()){
                MeetCostStatisticsInfo model = new MeetCostStatisticsInfo();
                Object[] data=(Object[])it.next();
                // 设置会议审批单号
                if(data[0]!=null){
                    model.setMeetId(data[0].toString());
                }
                // 设置申请部门
                if(data[1]!=null){
                    model.setDeptName(data[1].toString());
                }
                // 设置申请人
                if(data[2]!=null){
                    model.setName(data[2].toString());
                }
                // 设置用餐费用
                if(data[3]!=null){
                    model.setRepastTotal(data[3].toString());
                }
                // 设置用烟费用
                if(data[4]!=null){
                    model.setCigTotal(data[4].toString());
                }
                // 设置用酒费用
                if(data[5]!=null){
                    model.setWineTotal(data[5].toString());
                }
                // 设置住宿费用
                if(data[6]!=null){
                    model.setRoomTotal(data[6].toString());
                }
                // 设置实际支出
                if(data[7]!=null){
                    model.setPayout(data[7].toString());
                }
                // 设置超支
                if(data[8]!=null){
                    model.setBalance(data[8].toString());
                }
                // 设置会议开始时间
                if(data[9]!=null){
                    model.setStartMeetDate(data[9].toString());
                }
                // 设置会议名称
                if(data[10]!=null){
                    model.setMeetName(data[10].toString());
                }
                // 设置标准支出
                if(data[11]!=null){
                    model.setBudpayInall(data[11].toString());
                }
                arrList.add(model);
            }
            pObj.setList(arrList);
            pObj.setTotalCount((long)arrList.size());
            LogUtil.log("EJB:按开始时间结束时间查询会务费用开始结束。", Level.INFO, null);
            // 返回结果
            return pObj;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:按开始时间结束时间查询会务费用失败。", Level.SEVERE, e);
            throw new SQLException();
        }
    }

    /**
     * 判断参数是否为空
     * @param value
     * @return
     */
    private boolean checkIsNotNull(String value){
        if(value != null && !"".equals(value)){
            return true;
        }else {
            return false;
        }
    }

}
