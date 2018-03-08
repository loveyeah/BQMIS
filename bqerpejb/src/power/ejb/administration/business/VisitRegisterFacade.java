/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;


import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.VisitRegisterInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class VisitRegisterFacade implements VisitRegisterFacadeRemote {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**是否使用*/
	private String IS_USE_Y = "Y";

	/**
	 * 来访人员登记	
	 * @param 页面控制
	 * @return PageObject
	 * @author daichunlin
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject getVisitInfoList(String strEnterpriseCode,
			final int... rowStartIdxAndCount) throws SQLException {
		LogUtil.log("EJB:来访人员登记正常开始", Level.INFO, null);
		try {
			PageObject pobj = new PageObject();
			// 查询sql
			String StrSql = "SELECT "
				          + " A.ID, "
					      + " A.INSERTBY, "					   
					      + " TO_CHAR(A.INSERTDATE, 'yyyy-MM-DD HH24:mi:ss'), "
					      + " A.PAPER_ID, "
					      + " A.FIRM, "	
					      + " A.VISITEDMAN, "
					      + " A.VISITEDDEP, "	
					      + " TO_CHAR(A.IN_DATE, 'yyyy-MM-DD HH24:mi:ss'), "
					      + " TO_CHAR(A.OUT_DATE, 'yyyy-MM-DD HH24:mi:ss'), "
					      + " A.NOTE, "
					      + " A.ONDUTY, "
					      + " A.PAPERTYPE_CD, "
					      + " B.CHS_NAME, "					      
					      + " TO_CHAR(A.UPDATE_TIME, 'yyyy-MM-DD HH24:mi:ss'), "
					      + " C.DEPT_NAME "
					      + " FROM " 
					      + " AD_J_MANPASS A LEFT JOIN HR_J_EMP_INFO B "
					      + " ON A.VISITEDMAN = B.EMP_CODE  "
					    //  + " B.IS_USE = ? "    //modify by drdu 090909
					      + " LEFT JOIN HR_C_DEPT C "
					      + " ON A.VISITEDDEP = C.DEPT_CODE AND "
					      + " C.IS_USE = 'Y' " //update by sychen 20100902
//					      + " C.IS_USE = 'U' " 
					      + " WHERE "					    
					      + " A.IS_USE = ? AND"	
					      + " A.ENTERPRISE_CODE = ? AND"
					      + " A.INSERTDATE <= SYSDATE AND"
					      + " A.INSERTDATE >= ADD_MONTHS(SYSDATE,-1)";
			
					
			// 查询参数数组
			Object[] params = new Object[2];
			params[0] = IS_USE_Y;  //modify by drdu 090909
//			params[1] = IS_USE_Y;
//			params[2] = IS_USE_Y;
			params[1] = strEnterpriseCode;
			
			String sqlCount = "SELECT count(A.ID) "
				  + " FROM " 
			      + " AD_J_MANPASS A LEFT JOIN HR_J_EMP_INFO B "
			      + " ON A.VISITEDMAN = B.EMP_CODE  "
			     // + " B.IS_USE = ? "        //modify by drdu 090909
			      + " LEFT JOIN HR_C_DEPT C "
			      + " ON A.VISITEDDEP = C.DEPT_CODE AND "
			      + " C.IS_USE = 'Y' " //update by sychen 20100902
//			      + " C.IS_USE = 'U' " 
			      + " WHERE "					    
			      + " A.IS_USE = ? AND"	
			      + " A.ENTERPRISE_CODE = ? AND"
			      + " A.INSERTDATE <= SYSDATE AND"
			      + " A.INSERTDATE >= ADD_MONTHS(SYSDATE,-1)";

			List<VisitRegisterInfo> list = bll.queryByNativeSQL(StrSql,
					params, rowStartIdxAndCount);
			LogUtil.log("EJB: SQL =" + StrSql, Level.INFO, null);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
					.toString());

			List<VisitRegisterInfo> arrlist = new ArrayList<VisitRegisterInfo>();
			Iterator it = list.iterator();
			while (it.hasNext()) {				
				VisitRegisterInfo materialInfo = new VisitRegisterInfo();
				Object[] data = (Object[]) it.next();
				// 序号
				if (null != data[0]) {
					materialInfo.setId(Long.parseLong(data[0].toString()));
				}
				// 来访人
				if (null != data[1]) {
					materialInfo.setInsertBy(data[1].toString());
				}
				// 日期
				if (null != data[2]) {										
					materialInfo.setInsertDate(data[2].toString());					
				}
				// 证件号
				if (null != data[3]) {
					materialInfo.setPaperId(data[3].toString());
				}
				// 单位
				if (null != data[4]) {
					materialInfo.setFirm(data[4].toString());
				}
				// 被访人
				if (null != data[5]) {					
				   materialInfo.setVisitedMan(data[5].toString());				
				}
				// 被访人部门
				if (null != data[6]) {					
					materialInfo.setVisitedDep(data[6].toString());								
				}	
				// 进厂时间
				if (null != data[7]) {					
					materialInfo.setInDate(data[7].toString());								
				}
				// 出厂时间
				if (null != data[8]) {					
					materialInfo.setOutDate(data[8].toString());							
				}
				// 备注
				if (null != data[9]) {					
					materialInfo.setNote(data[9].toString());							
				}
				// 值班人
				if (null != data[10]) {					
					materialInfo.setOnDuty(data[10].toString());							
				}
				// 证件类别
				if (null != data[11]) {					
					materialInfo.setPapertypeCd(data[11].toString());							
				}
				// 中文名
				if (null != data[12]) {					
					materialInfo.setChsName(data[12].toString());							
				}
				// 更新时间
				if (null != data[13]) {	
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date;
					try {
						date = format.parse(data[13].toString());
						materialInfo.setUpdateTime(date);	
					} catch (ParseException e) {						
						e.printStackTrace();
					}											
				}
				// 部门名称
				if (null != data[14]) {
					materialInfo.setDeptName(data[14].toString());					
				}
				arrlist.add(materialInfo);
			}
			pobj.setList(arrlist);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:来访人员登记正常结束", Level.INFO, null);
			return pobj;
		} catch (Exception e) {
			LogUtil.log("EJB:来访人员登记登记失败", Level.SEVERE, e);
			throw new SQLException();
		}		
	}	
}
