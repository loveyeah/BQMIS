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
import power.ejb.administration.form.LeaderAuditInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
/**
 * 值长审核查询Remote
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
@Stateless
public class LeaderAuditInfoFacade implements LeaderAuditInfoFacadeRemote {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 通过查询条件获得相应的值长审核信息数据
	 * 
	 * @param strMenuDate
	 * @param strMenuType
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 * @throws SQLException 
	 */
	public PageObject getLeaderAuditInfo(String strMenuDate,String strMenuType,
			String enterpriseCode, final int ...rowStartIdxAndCount) throws SQLException
	{
		try{
			PageObject pobj = new PageObject();	
			// 查询sql
			String strSql = "select A.M_ID, "
				          + "  A.MENU_TYPE, "
				          + "  e.chs_name, "
						  + "  G.Dept_Name,"
						  + "  to_char(A.INSERTDATE,'yyyy-mm-dd'), "
						  + "  A.Place, "
						  + "  D.MENU_NAME, "
						  + "  C.MENUTYPE_NAME, "
						  + "  B.MENU_AMOUNT, "
						  + "  B.MENU_PRICE, "
						  + "  B.MEMO, "
						  + "  to_char(A.Update_Time,'yyyy-mm-dd hh24:mi:ss')"
						  + "from "
						  + "  AD_J_USER_MENU      A, "
						  + "  AD_J_USER_SUB       B, "
						  + "  AD_C_MENU_TYPE      C, "
						  + "  AD_C_MENU_WH        D, "
						  + "  HR_J_EMP_INFO       E, "
						  + "  HR_C_DEPT           G  ";

			String strSqlWhere = "where A.IS_USE = ? "
			                   + "  AND B.IS_USE = ? "
			                   + "  AND C.IS_USE = ? "
			                   + "  AND D.IS_USE = ? "
			                   //+ "  AND G.IS_USE = ? "
			                   // 企业代码
			                   + "  AND A.ENTERPRISE_CODE = ?"
			                   + "  AND C.ENTERPRISE_CODE = ?"
							   + "  AND D.ENTERPRISE_CODE = ?"
							   + "  AND E.ENTERPRISE_CODE = ?"
							   + "  AND G.ENTERPRISE_CODE = ?"
							   
			                   + "  AND A.M_ID = B.M_ID "
			                   + "  AND B.MENU_CODE = D.MENU_CODE "
			                   + "  AND D.MENUTYPE_CODE = c.MENUTYPE_CODE "
			                   + "  AND A.INSERTBY = e.emp_code "
			                   + "  AND e.dept_id = G.dept_id "
			                   + "  AND A.MENU_INFO = ? "
			                   + "  AND to_char(A.MENU_DATE,'yyyy-mm-dd') = ?"
			                   + "  AND A.MENU_TYPE = ? "
			                   + "ORDER BY A.M_ID";

			// 查询参数
            Object[] params = new Object[12];
			params[0] = "Y";
			params[1] = "Y";
			params[2] = "Y";
			params[3] = "Y";
			//params[4] = "Y";
			// 企业代码
			params[5] = enterpriseCode;
			params[6] = enterpriseCode;
			params[7] = enterpriseCode;
			params[8] = enterpriseCode;
			params[4] = enterpriseCode;
			params[9] = "2";
			params[10] = strMenuDate;
			params[11] = strMenuType;
			//拼接查询sql
			strSql += strSqlWhere;
			
			LogUtil.log("EJB:值长审核查询开始。", Level.INFO, null);
			LogUtil.log("SQL=" + strSql, Level.INFO, null);
						
			//list
			List list = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<LeaderAuditInfo> arrList = new ArrayList<LeaderAuditInfo>();
			Iterator it = list.iterator();
			// 订单号
			String strMId = "";
			String strTemp = "";
			while(it.hasNext()){
				Object[] data = (Object[])it.next();
				LeaderAuditInfo info = new LeaderAuditInfo();
				// 订单号
				if(data[0] != null){
					// 设置隐藏订单号
					info.setMenuHidId(data[0].toString());
					strMId = data[0].toString();
				}
				// 订单号不同时显示
				if (!strTemp.equals(strMId)) {
					strTemp = strMId;
					// 订单号
					if(data[0] != null){
						info.setMenuId(data[0].toString());
					}
					// 订餐类别
					if(data[1] != null){
						info.setMenuType(data[1].toString());
					}
					// 订餐人
					if(data[2] != null){
						info.setWorkerName(data[2].toString());
					}
					// 所属部门
					if(data[3] != null){
						info.setDepName(data[3].toString());
					}
					// 填单日期
					if(data[4] != null){
						info.setInsertDate(data[4].toString());
					}
					// 就餐地点
					if(data[5] != null){
						info.setPlace(data[5].toString());
					}
				}
				// 菜谱名称
				if(data[6] != null){
					info.setMenuName(data[6].toString());
				}
				// 菜谱类别
				if(data[7] != null){
					info.setMenuTypeName(data[7].toString());
				}
				// 份数
				if(data[8] != null){
					info.setMenuAmount(data[8].toString());
				}
				// 单价
				if(data[9] != null){
					info.setMenuPrice(data[9].toString());
				}
				// 备注
				if(data[10] != null){
					info.setMemo(data[10].toString());
				}
				// 上次更新时间
				if(data[11] != null){
					info.setUpdateTime(data[11].toString());
				}
				arrList.add(info);
			}
			// 按查询结果集设置返回结果
			if (arrList.size() == 0) {
				// 设置查询结果集
				pobj.setList(null);
				// 设置行数
				pobj.setTotalCount(0L);
			} else {
				pobj.setList(arrList);
				pobj.setTotalCount(Long.parseLong(String.valueOf(arrList.size())));
			}
			LogUtil.log("EJB:值长审核查询结束。", Level.INFO, null);
			//返回PageObject
			return pobj;
		}catch(RuntimeException e){
			LogUtil.log("EJB:值长审核查询失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}
}
