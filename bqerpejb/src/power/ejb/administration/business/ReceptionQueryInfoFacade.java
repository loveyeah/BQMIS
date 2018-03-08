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
import power.ejb.administration.form.ReceptionQueryInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class ReceptionQueryInfoFacade implements ReceptionQueryInfoFacadeRemote {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
     * 通过查询条件获得相应的接待信息数据
     * @param strStartDate
     * @param strEndDate
     * @param strDeptCode
     * @param strWorkerCode
     * @param strIsOver
     * @param strDcmStatus
     * @param rowStartIdxAndCount
     * @return PageObject
     */
	public PageObject getReceptionQueryInfo(String strStartDate,String strEndDate,String strDeptCode,
			String strWorkerCode,String strIsOver, String strDcmStatus,
			String enterpriseCode, final int ...rowStartIdxAndCount) throws SQLException
	{
		try{
			PageObject pobj = new PageObject();	
			// 查询sql
			String strSql = "select r.APPLY_ID, "
				          + "  e.chs_name, "
				          + "  d.Dept_Name,"
						  + "  to_char(r.LOG_DATE,'yyyy-mm-dd'), "
						  + "  to_char(r.MEET_DATE,'yyyy-mm-dd'), "
						  + "  r.REPAST_NUM, "
						  + "  r.ROOM_NUM, "
						  + "  r.MEET_NOTE, "
						  + "  r.REPAST_BZ, "
						  + "  r.ROOM_BZ, "
						  + "  r.REPAST_PLAN, "						  
						  + "  r.ROOM_PLAN, "
						  + "  r.OTHER, "
						  + "  r.PAYOUT_BZ, "
						  + "  r.PAYOUT, "
						  + "  r.BALANCE, "
						  + "  r.DCM_STATUS "						  
						  + "from "
						  + "  AD_J_RECEPTION r "
						  + "left join "
						  + "  (HR_J_EMP_INFO e " 
						  +	"   left join "
						  + "   HR_C_DEPT d on "
						  +	"   e.dept_id =d.dept_id AND d.ENTERPRISE_CODE = ?) "
						  + "on r.APPLY_MAN = e.emp_code AND e.ENTERPRISE_CODE = ?";

			String strSqlWhere = "where r.IS_USE = ? "
				               + "AND r.ENTERPRISE_CODE = ?";

			// 查询参数数量
			int paramsCnt = 4;
			if(checkIsNotNull(strStartDate)){
				paramsCnt++;
			}
			if(checkIsNotNull(strEndDate)){
				paramsCnt++;
			}
			if(checkIsNotNull(strDeptCode)){
				paramsCnt++;
			}
			if(checkIsNotNull(strWorkerCode)){
				paramsCnt++;
			}
			if(checkIsNotNull(strDcmStatus)){
				paramsCnt++;
			}
			// 查询参数数组
			Object[] params = new Object[paramsCnt];
			int i = 0;
			params[i++] = enterpriseCode;
			params[i++] = enterpriseCode;
			params[i++] = "Y";
			params[i++] = enterpriseCode;
//			params[i++] = "Y";
            // 开始日期
			if(checkIsNotNull(strStartDate)){
				strSqlWhere += "  and to_char(r.MEET_DATE,'yyyy-mm-dd') >= ? ";
				params[i++] = strStartDate;
			}
			// 结束日期
			if(checkIsNotNull(strEndDate)){
				strSqlWhere += "  and to_char(r.MEET_DATE,'yyyy-mm-dd') <= ? ";
				params[i++] = strEndDate;
			}
			// 部门编码
			if(checkIsNotNull(strDeptCode)){
//				strSqlWhere += "  and e.dept_id = ? ";
				strSqlWhere += "  and d.dept_code like ? ";
				params[i++] = strDeptCode + "%";
			}
			// 人员编码
			if(checkIsNotNull(strWorkerCode)){
				strSqlWhere += "  and r.APPLY_MAN = ? ";
				params[i++] = strWorkerCode;
			}
			// 是否超支
			if(checkIsNotNull(strIsOver)){
				if("Y".equals(strIsOver)){
				    strSqlWhere += "  and r.BALANCE < 0 ";
				}else if("N".equals(strIsOver)){
				    strSqlWhere += "  and (r.BALANCE >= 0 or r.BALANCE is null) ";
				}
			}
			// 单据状态
			if(checkIsNotNull(strDcmStatus)){
				strSqlWhere += "  and r.DCM_STATUS = ? ";
				params[i++] = strDcmStatus;
			}          
			//拼接查询sql
			strSql += strSqlWhere;
			
			LogUtil.log("EJB:接待审批查询开始。", Level.INFO, null);
			LogUtil.log("SQL=" + strSql, Level.INFO, null);
						
			//list
			List list = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<ReceptionQueryInfo> arrList = new ArrayList<ReceptionQueryInfo>();
			Iterator it = list.iterator();
			while(it.hasNext()){
				Object[] data = (Object[])it.next();
				ReceptionQueryInfo info = new ReceptionQueryInfo();
				// 审批单号
				if(data[0] != null){
					info.setApplyId(data[0].toString());
				}
				// 申请部门
				if(data[1] != null){
					info.setApplyManName(data[1].toString());
				}
				// 申请人
				if(data[2] != null){
					info.setApplyDeptName(data[2].toString());
				}
				// 登记日期
				if(data[3] != null){
					info.setLogDate(data[3].toString());
				}
				// 接待日期
				if(data[4] != null){
					info.setMeetDate(data[4].toString());
				}
				// 接待说明
				if(data[5] != null){
					info.setRepastNum(data[5].toString());
				}
				// 就餐人数
				if(data[6] != null){
					info.setRoomNum(data[6].toString());
				}
				// 就餐标准
				if(data[7] != null){
					info.setMeetNote(data[7].toString());
				}
				// 就餐安排
				if(data[8] != null){
					info.setRepastBz(data[8].toString());
				}
				// 住宿人数
				if(data[9] != null){
					info.setRoomBz(data[9].toString());
				}
				// 住宿标准
				if(data[10] != null){
					info.setRepastPlan(data[10].toString());
				}
				// 住宿安排
				if(data[11] != null){
					info.setRoomPlan(data[11].toString());
				}
				// 标准支出
				if(data[12] != null){
					info.setOther(data[12].toString());
				}
				// 实际支出
				if(data[13] != null){
					info.setPayoutBz(data[13].toString());
				}
				// 差额
				if(data[14] != null){
					info.setPayout(data[14].toString());
				}
				// 其他
				if(data[15] != null){
					info.setBalance(data[15].toString());
				}
				// 单据状态
				if(data[16] != null){
					info.setDcmStatus(data[16].toString());
				}
				arrList.add(info);
			}
			// 按查询结果集设置返回结果
			if (arrList.size() == 0) {
				// 设置查询结果集
				pobj.setList(null);
				// 设置行数
				pobj.setTotalCount(Long.parseLong(0 + ""));
			} else {
				pobj.setList(arrList);
				pobj.setTotalCount(Long.parseLong(arrList.size()
						+ ""));
			}
			LogUtil.log("EJB:接待审批查询结束。", Level.INFO, null);
			//返回PageObject
			return pobj;
		} catch(RuntimeException e){
			LogUtil.log("EJB:接待审批查询失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	/**
	 * 为空判断
	 * 
	 * @param value
	 * @return boolean
	 */
	private boolean checkIsNotNull(String value){
		if(value != null && !"".equals(value)){
			return true;
		}else {
			return false;
		}
	}
}
