/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.AdCTimeWorkAllInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class AdCTimeWorkAllFacade implements AdCTimeWorkAllFacadeRemote {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**空值*/
	private String BLANK = "";
	/**是否使用*/
	private String IS_USE_Y = "Y";

	/**
	 * 子画面全部定期工作查询
	 * 
	 * @param 工作类别
	 * @param 页面控制
	 * @return PageObject
	 * @author daichunlin
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAllRegularQuery(String strEnterpriseCode,String fuzzy, String strWorkType,
			final int... rowStartIdxAndCount) {
		LogUtil.log("EJB:子画面全部定期工作查询正常开始", Level.INFO, null);
		try {
			PageObject pobj = new PageObject();
			// 查询sql
			String StrSql = "SELECT "
				          + " A.WORKITEM_NAME, "
					      + " A.WORKRANGE_TYPE, "
					      + " TO_CHAR(A.START_TIME, 'yyyy-MM-DD HH24:mi:ss'),"
					      + " TO_CHAR(A.START_TIME,'Day') START_WEEK, "
					      + " A.WORK_EXPLAIN, "
					      + " A.IF_WEEKEND "					    
					      + " FROM " 
					      + " AD_C_TIMEWORK A "
					      + " WHERE "
					      + " A.IS_USE = ? AND"
					      + " A.ENTERPRISE_CODE = ? AND"
					      + " A.WORKTYPE_CODE = ? ";
			
			// 查询参数数量
			int paramsCnt = 3;
			if(checkNull(fuzzy))
				paramsCnt++;
			
			// 查询参数数组
			Object[] params = new Object[paramsCnt];
			int i = 0;
			params[i++] = IS_USE_Y;
			params[i++] = strEnterpriseCode;
			params[i++] = strWorkType;	
			
			String sqlCount = "SELECT count(A.ID) "
				  + " FROM " 
			      + " AD_C_TIMEWORK A "
			      + " WHERE "
			      + " A.IS_USE = ? AND"
			      + " A.ENTERPRISE_CODE = ? AND"
			      + " A.WORKTYPE_CODE = ? ";
			// 如果画面查询条件有值
			if(checkNull(fuzzy)) {
				StrSql += " AND A.SUB_WORKTYPE_CODE = ? ";
				sqlCount += " AND A.SUB_WORKTYPE_CODE = ? ";
				params[i++] = fuzzy;
			}

			List<AdCTimeWorkAllInfo> list = bll.queryByNativeSQL(StrSql,
					params, rowStartIdxAndCount);
			LogUtil.log("EJB: SQL =" + StrSql, Level.INFO, null);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
					.toString());

			List<AdCTimeWorkAllInfo> arrlist = new ArrayList<AdCTimeWorkAllInfo>();
			Iterator it = list.iterator();
			while (it.hasNext()) {				
				AdCTimeWorkAllInfo materialInfo = new AdCTimeWorkAllInfo();
				Object[] data = (Object[]) it.next();
				// 工作项目名称
				if (null != data[0]) {
					materialInfo.setWorkitemName(data[0].toString());
				}
				// 周期类别
				if (null != data[1]) {
					switch (Integer.parseInt(data[1].toString())) {
					case 0:
						materialInfo.setWorkrangeType("没有设置");
						break;
					case 1:
						materialInfo.setWorkrangeType("每日");
						break;
					case 2:
						materialInfo.setWorkrangeType("隔日");
						break;
					case 3:
						materialInfo.setWorkrangeType("每周");
						break;
					case 4:
						materialInfo.setWorkrangeType("隔周");
						break;
					case 5:
						materialInfo.setWorkrangeType("每月");
						break;
					case 6:
						materialInfo.setWorkrangeType("隔月");
						break;
					case 7:
						materialInfo.setWorkrangeType("隔N天");
						break;
					default:
						break;
					}
				}
				// 开始时间
				if (null != data[2]) {										
					materialInfo.setStartTime(data[2].toString());					
				}
				// 星期
				if (null != data[3]) {
					materialInfo.setStartWeek(data[3].toString());
				}
				// 工作说明
				if (null != data[4]) {
					materialInfo.setWorkExplain(data[4].toString());
				}
				// 节假日是否工作
				if (null != data[5]) {
					if (data[5].toString().equals("Y")) {
						materialInfo.setIfWeekend("工作");
					} else {
						materialInfo.setIfWeekend("不工作");
					}
				}						
				arrlist.add(materialInfo);
			}
			pobj.setList(arrlist);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:子画面全部定期工作查询正常结束", Level.INFO, null);
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:子画面全部定期工作查询失败", Level.SEVERE, e);
			throw e;
		}		
	}
	/**
	 * check 字符串是否为空
	 * @param str
	 * @return 空返回false，非空返回true
	 */
	public Boolean checkNull(String str) {
		if(str != null && !(BLANK).equals(str))
			return true;
		else return false;
	}
}
