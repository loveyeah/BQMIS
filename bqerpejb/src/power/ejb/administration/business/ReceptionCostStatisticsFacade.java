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
import power.ejb.administration.form.ReceptionCostStatisticsInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 接待费用统计方法.
 * 
 * @author zhouxu
 */
@Stateless
public class ReceptionCostStatisticsFacade implements ReceptionCostStatisticsFacadeRemote {
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**
     * 根据开始日期、结束日期查找接待费用统计
     * @param startDate
     * @param endDate
     * @return PageObject
     */
    @SuppressWarnings("unchecked")
    public PageObject findByFuzzy(String startDate, String endDate, String enterpriseCode) throws SQLException{
        try {
            //定义查询sql语句 
            String strSql = "SELECT A.APPLY_ID , " 
                + "     to_char(A.MEET_DATE,'YYYY-mm-dd') , " 
                + "     A.REPAST_NUM*A.REPAST_BZ , "
                + "     A.ROOM_NUM *A.ROOM_BZ , " 
                + "     A.PAYOUT_BZ ,"
                + "     A.PAYOUT , " 
                + "     A.BALANCE , " 
                + "     B.DEPT_NAME , " 
                + "     C.CHS_NAME "
                + " FROM AD_J_RECEPTION A  LEFT JOIN HR_J_EMP_INFO C "
                + " ON  A.APPLY_MAN = C.EMP_CODE AND C.ENTERPRISE_CODE = ? "
                + " LEFT JOIN HR_C_DEPT B"
                + " ON  C.DEPT_ID = B.DEPT_ID AND B.ENTERPRISE_CODE = ? "
                + " WHERE "
                + "     A.IS_USE = ? "
                + "     AND A.DCM_STATUS = ? "
                + "     AND A.ENTERPRISE_CODE = ?";
            int paramsCnt = 5;
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
            params[i++] = enterpriseCode;
            params[i++] = enterpriseCode;
            params[i++] = "Y" ;
            params[i++] = "2" ;
            params[i++] = enterpriseCode;
            if(checkIsNotNull(startDate)){
                strSql += "AND to_char(A.MEET_DATE , 'yyyy-MM-dd') >= ?";
                params[i++] = startDate ;
            }
            if(checkIsNotNull(endDate)){
                strSql += "AND to_char(A.MEET_DATE , 'yyyy-MM-dd') <= ?";
                params[i++] = endDate ;
            }
            LogUtil.log("EJB:按开始时间结束时间查询接待费用开始。SQL=" + strSql, Level.INFO, null);
            List list=new ArrayList();
            // 如果既有开始时间又有结束时间 
            list = bll.queryByNativeSQL(strSql,params);            
            PageObject pObj = new PageObject();
            List<ReceptionCostStatisticsInfo> arrList = new ArrayList<ReceptionCostStatisticsInfo>();
            Iterator it=list.iterator();
            while(it.hasNext()){
                ReceptionCostStatisticsInfo model = new ReceptionCostStatisticsInfo();
                Object[] data=(Object[])it.next();
                if(data[0]!=null){
                    model.setApplyId(data[0].toString());
                }
                if(data[1]!=null){
                    model.setMeetDate(data[1].toString());
                }
                if(data[2]!=null){
                    model.setRepastTotal(data[2].toString());
                }
                if(data[3]!=null){
                    model.setRoomTotal(data[3].toString());
                }
                if(data[4]!=null){
                    model.setPayoutBz(data[4].toString());
                }
                if(data[5]!=null){
                    model.setPayout(data[5].toString());
                }
                if(data[6]!=null){
                    model.setBalance(data[6].toString());
                }
                if(data[7]!=null){
                    model.setDeptName(data[7].toString());
                }
                if(data[8]!=null){
                    model.setName(data[8].toString());
                }
                arrList.add(model);
            }
            pObj.setList(arrList);
            pObj.setTotalCount((long)arrList.size());
            LogUtil.log("EJB:按开始时间结束时间查询接待费用开始结束。", Level.INFO, null);
            // 返回结果
            return pObj;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:按开始时间结束时间查询接待费用失败。", Level.SEVERE, e);
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
