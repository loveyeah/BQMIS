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
import java.util.zip.DataFormatException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJReception;
import power.ejb.administration.AdJReceptionFacadeRemote;
import power.ejb.administration.form.ReceptionApplyInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 接待申请上报方法体.
 * 
 * @author liugonglei
 */
@Stateless
public class ReceptionApplyFacade implements ReceptionApplyFacadeRemote {
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	@EJB(beanName = "AdJReceptionFacade")
	AdJReceptionFacadeRemote adJReceptRemote;
	 /**
     * 检索接待申请一览
     * @param strWorkCode
     * @return
	 * @throws SQLException 
     */
	@SuppressWarnings("unchecked")
	public PageObject findReceptApply(String strWorkCode,String strEnterPriseCode, int start, int limit) throws SQLException {
		LogUtil.log("EJB:接待申请一览检索开始。", Level.INFO, null);
		try {
			// 新规PageObject
			PageObject pbjResult = new PageObject();
			// 检索数据sql文
			String strSql = "SELECT " 
				    + "A.ID, " 
				    + "A.LOG_DATE, "
					+ "A.MEET_DATE, " 
					+ "A.REPAST_NUM, " 
					+ "A.REPAST_BZ, "
					+ "A.ROOM_NUM, " 
					+ "A.MEET_NOTE, " 
					+ "A.ROOM_BZ, "
					+ "A.REPAST_PLAN, " 
					+ "A.ROOM_PLAN, " 
					+ "A.OTHER, "
					+ "A.PAYOUT_BZ, " 
					+ "A.PAYOUT, " 
					+ "A.BALANCE, "
					+ "D.CHS_NAME, " 
					+ "C.DEPT_NAME, " 
					+ "A.UPDATE_TIME, " 
					+ "A.APPLY_ID " 
					+ "FROM "
					+ "AD_J_RECEPTION A "
					+ "left join "
					+ "HR_J_EMP_INFO D "
					+ "on A.APPLY_MAN = D.EMP_CODE "
					+ "AND D.ENTERPRISE_CODE = ? "
					+ "left join  "
					+ "HR_C_DEPT C  "
					+ "on D.DEPT_ID = C.DEPT_ID  "
					+ "AND C.ENTERPRISE_CODE = ? "
					+ "WHERE " 
					+ "A.APPLY_MAN = ? " 
					+ "AND A.DCM_STATUS IN ('0','3') " 
					+ "AND A.ENTERPRISE_CODE = ? "
					+ "AND A.IS_USE = ? ";
			 LogUtil.log("EJB:SQL=" +strSql, Level.INFO, null);
			// 检索总条数sql文
			String strCount = "SELECT " 
			    + "COUNT(A.ID) " 
				+ "FROM "
				+ "AD_J_RECEPTION A "
				+ "left join "
				+ "HR_J_EMP_INFO D "
				+ "on A.APPLY_MAN = D.EMP_CODE "
				+ "AND D.ENTERPRISE_CODE = ? "
				+ "left join  "
				+ "HR_C_DEPT C  "
				+ "on D.DEPT_ID = C.DEPT_ID  "
				+ "AND C.ENTERPRISE_CODE = ? "
				+ "WHERE " 
				+ "A.APPLY_MAN = ? " 
				+ "AND A.DCM_STATUS IN ('0','3') " 
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "AND A.IS_USE = ? ";
			Object[] params = {strEnterPriseCode,strEnterPriseCode,strWorkCode,strEnterPriseCode,"Y"};
			// 取得数据
			List list = bll.queryByNativeSQL(strSql, params, start,limit);
			List<ReceptionApplyInfo> arrarList = new ArrayList<ReceptionApplyInfo>();
			 // 判断是否取到了数据
			if(null != list && list.size()>0){
				Iterator it = list.iterator();
				while (it.hasNext()){
					ReceptionApplyInfo receptAppInfo = new ReceptionApplyInfo();
					Object[] objListDate = (Object[]) it.next();
					if(null != objListDate[0]){
						 // 设定id
						receptAppInfo.setId(objListDate[0].toString());
					}
					if(null != objListDate[1]){
						 // 设定填表日期
						receptAppInfo.setLogDate(objListDate[1].toString());
					}
					if(null != objListDate[2]){
						 // 设定接待日期
						receptAppInfo.setMeetDate(objListDate[2].toString());
					}
					if(null != objListDate[3]){
						 // 设定就餐人数
						receptAppInfo.setRepastNum(objListDate[3].toString());
					}
					if(null != objListDate[4]){
						 // 设定就餐标准
						receptAppInfo.setRepastBz(objListDate[4].toString());
					}
					if(null != objListDate[5]){
						 // 设定住宿人数
						receptAppInfo.setRoomNum(objListDate[5].toString());
					}
					if(null != objListDate[6]){
						 // 设定接待说明
						receptAppInfo.setMeetNote(objListDate[6].toString());
					}
					if(null != objListDate[7]){
						 // 设定住宿标准
						receptAppInfo.setRoomBz(objListDate[7].toString());
					}
					if(null != objListDate[8]){
						 // 设定就餐安排
						receptAppInfo.setRepastPlan(objListDate[8].toString());
					}
					if(null != objListDate[9]){
						 // 设定住宿安排
						receptAppInfo.setRoomPlan(objListDate[9].toString());
					}
					if(null != objListDate[10]){
						 // 设定其他
						receptAppInfo.setOther(objListDate[10].toString());
					}
					if(null != objListDate[11]){
						 // 设定标准金额
						receptAppInfo.setPayoutBz(objListDate[11].toString());
					}
					if(null != objListDate[12]){
						 // 设定实际金额
						receptAppInfo.setPayout(objListDate[12].toString());
					}
					if(null != objListDate[13]){
						 // 设定差额
						receptAppInfo.setBalance(objListDate[13].toString());
					}
					if(null != objListDate[14]){
						 // 设定设定姓名
						receptAppInfo.setName(objListDate[14].toString());
					}
					if(null != objListDate[15]){
						 // 设定设定部门名称
						receptAppInfo.setDepName(objListDate[15].toString());
					}
					if (null != objListDate[16]) {
						 // 设定设定修改时间
						AdJReception adJrEntity = adJReceptRemote
								.findById(new Long(objListDate[0].toString()));
						receptAppInfo.setUpdateTime(adJrEntity.getUpdateTime()
								.getTime());
					}
					if (null != objListDate[17]){
						// 设定接待审批单号
						receptAppInfo.setApplyId(objListDate[17].toString());
					}
					arrarList.add(receptAppInfo);
				}
			}
			// 取得总条数
			Object countList = bll.getSingal(strCount, params);
			pbjResult.setList(arrarList);
			pbjResult.setTotalCount(new Long(countList.toString()));
			LogUtil.log("EJB:接待申请一览检索正常结束。", Level.INFO, null);
			// 返回数据
			return pbjResult;
		} catch (Exception e) {
			LogUtil.log("EJB:接待申请一览检索异常结束。", Level.SEVERE, null);
			throw new SQLException();
		}
	}
	/**
	 * 更新来宾接待审批单
	 * @param entity
	 * @param lngUpdateTime
	 * @throws SQLException
	 * @throws DataFormatException
	 */
	public AdJReception updateReceptApply(AdJReception entity, Long lngUpdateTime)
			throws SQLException, DataChangeException {
		    LogUtil.log("EJB:接待申请一览更新开始。", Level.INFO, null);
	        try {
	        	AdJReception result = new AdJReception();
	        	AdJReception adjReceptEntity = adJReceptRemote.findById(entity.getId());
	        	Long lngNewUpdateTime = adjReceptEntity.getUpdateTime().getTime();
	        	if (!lngNewUpdateTime.equals(lngUpdateTime)) {
					throw new DataChangeException(null);
				} else {
	            result = adJReceptRemote.update(entity);
				}
	            LogUtil.log("update successful", Level.INFO, null);
	            LogUtil.log("EJB:接待申请一览更新正常结束。", Level.INFO, null);
	            return result;
	        } catch (DataChangeException re) {
	        	LogUtil.log("EJB:接待申请一览更新异常结束。", Level.SEVERE, re);
	            throw re;
	        } catch (Exception e){
	        	LogUtil.log("EJB:接待申请一览更新异常结束。", Level.SEVERE, e);
	        	throw new SQLException();
	        }
		
	}
	/**
     * 取得接待申请id
     * @return
     * @throws SQLException
     */
	public Long getReceptionId() throws SQLException {
		LogUtil.log("EJB:取得新的接待申请id开始。", Level.INFO, null);
        try {
            // 取得id
            Long lngId = bll.getMaxId("AD_J_RECEPTION", "id");
            LogUtil.log("EJB:取得新的接待申请id正常结束。", Level.INFO, null);
            // 返回id
            return lngId;
        } catch (Exception er) {
            LogUtil.log("Get 取得新的接待申请id异常结束。",Level.SEVERE, er);
            throw new SQLException();
        }
	}
}
