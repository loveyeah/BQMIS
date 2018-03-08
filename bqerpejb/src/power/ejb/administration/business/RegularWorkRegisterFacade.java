/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJTimework;
import power.ejb.administration.form.AllRegularWorkInfo;
import power.ejb.administration.form.RegularWorkRegisterInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 定期工作登记
 
 * @author daichunlin
 */
@Stateless
public class RegularWorkRegisterFacade implements
		RegularWorkRegisterFacadeRemote {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**是否使用*/
	private String IS_USE_Y = "Y";
	/**是否*/
	private String IS_FLAG_Y = "Y";
	/**是否节假日*/
	private String IF_WEEKEND_Y = "Y";
	/**是否节假日*/
	private String IF_WEEKEND_N = "N";

	/**
	 * 定期工作登记取得
	 * 
	 * @param strWorkType
	 *            工作类别
	 * @param rowStartIdxAndCount
	 *            分页
	 * @return PageObject
	 */
	public PageObject getRegisterList(String strEnterpriseCode,String strWorkType,
			final int... rowStartIdxAndCount) {
		try {
			LogUtil.log("EJB:定期工作登记取得开始", Level.INFO, null);
			PageObject pobj = new PageObject();
			// 查询sql
			String StrSql = "SELECT "
				          + " A.ID, "
				          + " A.WORK_DATE, "				   
					      + " A.WORK_EXPLAIN, "
					      + " A.RESULT, "
					      + " A.MARK, "
					      + " A.OPERATOR, "
					      + " A.MEMO, "
					      + " B.WORKITEM_NAME, "
					      + " C.SUB_WORKTYPE_NAME, "
					      + " TO_CHAR(A.WORK_DATE,'Day') WORK_WEEK, "
					      + " TO_CHAR(A.UPDATE_TIME,'yyyy-MM-dd HH24:mi:ss') "
					      + "FROM "
					      + " AD_J_TIMEWORK A LEFT JOIN AD_C_TIMEWORK B "
					      + " ON A.WORKITEM_CODE = B.WORKITEM_CODE AND"
					      + " B.ENTERPRISE_CODE = ? "
					      + " LEFT JOIN AD_C_WORKTYPE C "
					      + " ON A.SUB_WORKTYPE_CODE = C.SUB_WORKTYPE_CODE AND "
					      + " C.ENTERPRISE_CODE = ? "
					      + "WHERE "
					      + " A.IS_USE = ? AND "
					      + " A.ENTERPRISE_CODE = ? AND "
					      + " TRUNC(A.WORK_DATE) = TRUNC(SYSDATE) AND "
			              + " A.WORKTYPE_CODE = ? ";
			          
			              
			// 查询参数数量
			int paramsCnt = 5;

			// 查询参数数组
			Object[] params = new Object[paramsCnt];
			int i = 0;
			params[i++] = strEnterpriseCode;
			params[i++] = strEnterpriseCode;	
			params[i++] = IS_USE_Y;	
			params[i++] = strEnterpriseCode;	
			params[i++] = strWorkType;	         
			              
			List<RegularWorkRegisterInfo> list = bll.queryByNativeSQL(StrSql,
					params, rowStartIdxAndCount);
			String sqlCount = "SELECT count(A.ID) "
			      + "FROM "
			      + " AD_J_TIMEWORK A LEFT JOIN AD_C_TIMEWORK B "
			      + " ON A.WORKITEM_CODE = B.WORKITEM_CODE AND"
			      + " B.ENTERPRISE_CODE = ? "
			      + " LEFT JOIN AD_C_WORKTYPE C "
			      + " ON A.SUB_WORKTYPE_CODE = C.SUB_WORKTYPE_CODE AND "
			      + " C.ENTERPRISE_CODE = ? "
			      + "WHERE "
			      + " A.IS_USE = ? AND "
			      + " A.ENTERPRISE_CODE = ? AND "
			      + " TRUNC(A.WORK_DATE) = TRUNC(SYSDATE) AND "
	              + " A.WORKTYPE_CODE = ? ";
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
					.toString());
			LogUtil.log("EJB:定期工作登记取得 SQL= " + StrSql, Level.INFO, null);
			
			List<RegularWorkRegisterInfo> arrlist = new ArrayList<RegularWorkRegisterInfo>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				RegularWorkRegisterInfo materialInfo = new RegularWorkRegisterInfo();
				Object[] data = (Object[]) it.next();
				// ID
				if (null != data[0])
					materialInfo.setId(Long.parseLong(data[0].toString()));
				// 工作日期
				if (null != data[1])
					materialInfo.setWorkDate(data[1].toString());
				// 工作说明
				if (null != data[2])
					materialInfo.setWorkExplain(data[2].toString());
				// 工作结果
				if (null != data[3]) {
					materialInfo.setResult(data[3].toString());
				}
				// 标志
				if (null != data[4])
					if (("Y").equals(data[4].toString())) {
						materialInfo.setMark("完成");
					} else {
						materialInfo.setMark("未完成");
					}
				// 操作人
				if (null != data[5])
					materialInfo.setOperator(data[5].toString());
				// 备注
				if (null != data[6])
					materialInfo.setMemo(data[6].toString());
				// 工作项目名称
				if (null != data[7])
					materialInfo.setWorkItemName(data[7].toString());
				// 子类别编码名称
				if (null != data[8]) {
					materialInfo.setSubWorkTypeName(data[8].toString());
				}
				// 星期
				if (null != data[9]) {
					materialInfo.setWorkWeek(data[9].toString());
				}	
				// 更新时间
				if (null != data[10]) {
					materialInfo.setUpdateTime(data[10].toString());
				}	
				arrlist.add(materialInfo);
			}
			pobj.setList(arrlist);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:定期工作登记取得结束", Level.INFO, null);
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:定期工作登记取得失败", Level.SEVERE, e);	
			throw e;
		}		
	}

	/**
	 * 取得当天定期工作
	 * 
	 * @param strWorkType
	 *            工作类别
	 * @return PageObject
	 */
	public PageObject getAllRegularWorkList(String strEnterpriseCode,String strWorkType) {
		LogUtil.log("EJB:取得当天定期工作开始", Level.INFO, null);
		try {
			PageObject pobj = new PageObject();
			// 查询sql
			String StrSql = "SELECT "
				          + " A.WORKITEM_CODE,  "
					      + " A.WORKTYPE_CODE,  "
					      + " A.SUB_WORKTYPE_CODE,  "
					      + " A.WORKRANGE_TYPE,  "
					      + " B.RANGE_NUMBER,  "
					      + " A.START_TIME,  "
					      + " A.WORK_EXPLAIN  "
					      + " FROM  "
					      + " AD_C_TIMEWORK A "
					      + " LEFT JOIN AD_C_TIMEWORKD B "
					      + " ON A.WORKITEM_CODE = B.WORKITEM_CODE AND "					    
					      + " B.IS_USE = ? "
					      + " WHERE  "	
					      + " A.IS_USE = ? AND "					    
					      + " TRUNC(A.START_TIME) <= TRUNC(SYSDATE) AND "
					      + " (A.IF_WEEKEND = ? OR (A.IF_WEEKEND = ? AND "
					      + " ((SELECT            "
					   // modify by liuyi 090908 15:45 数据库中无该属性
//					      + "     HOLIDAY_KBN      "
					      + " holiday_type "
					      + "   FROM              "
					      + "     HR_C_HOLIDAY    "
					      + "   WHERE            "
					      + "     TRUNC(HOLIDAY_DATE) = TRUNC(SYSDATE) AND ENTERPRISE_CODE = ? and is_use='Y') = 1))) AND "// modify by ywliu 20091028
					      + " A.WORKTYPE_CODE = ? AND "
					      + " A.ENTERPRISE_CODE = ? ";
					    
			// 查询参数数量
			int paramsCnt = 7;

			// 查询参数数组
			Object[] params = new Object[paramsCnt];
			int i = 0;
			params[i++] = IS_USE_Y;
			params[i++] = IS_USE_Y;		
			params[i++] = IF_WEEKEND_Y;
			params[i++] = IF_WEEKEND_N;
			params[i++] = strEnterpriseCode;
			params[i++] = strWorkType;
			params[i++] = strEnterpriseCode;
			
			List<AllRegularWorkInfo> list = bll
					.queryByNativeSQL(StrSql, params);
			String sqlCount = "SELECT count(A.ID) "
				  + " FROM  "
			      + " AD_C_TIMEWORK A "
			      + " LEFT JOIN AD_C_TIMEWORKD B "
			      + " ON A.WORKITEM_CODE = B.WORKITEM_CODE AND "					    
			      + " B.IS_USE = ? "
			      + " WHERE  "	
			      + " A.IS_USE = ? AND "					    
			      + " A.START_TIME <= SYSDATE AND "
			      + " (A.IF_WEEKEND = ? OR (A.IF_WEEKEND = ? AND "
			      + " ((SELECT            "
			      // modify by liuyi 090908 15:45 数据库中无该属性
//			      + "     HOLIDAY_KBN      "
			      + " holiday_type "
			      + "   FROM              "
			      + "     HR_C_HOLIDAY    "
			      + "   WHERE            "
			      + "     TRUNC(HOLIDAY_DATE) = TRUNC(SYSDATE) AND ENTERPRISE_CODE = ? and is_use='Y') = 1))) AND " // modify by ywliu 20091028
			      + " A.WORKTYPE_CODE = ? AND "
			      + " A.ENTERPRISE_CODE = ? ";
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount, params).toString());
			LogUtil.log("EJB:取得当天定期工作 SQL= " + StrSql, Level.INFO, null);
			List<AllRegularWorkInfo> arrlist = new ArrayList<AllRegularWorkInfo>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				AllRegularWorkInfo materialInfo = new AllRegularWorkInfo();
				Object[] data = (Object[]) it.next();
				// 工作项目编码
				if (null != data[0])
					materialInfo.setWorkItemCode(data[0].toString());
				// 工作类别编码
				if (null != data[1])
					materialInfo.setWorkTypeCode(data[1].toString());
				// 子类别编码
				if (null != data[2])
					materialInfo.setSubWorkTypeCode(data[2].toString());
				// 周期类别
				if (null != data[3]) {
					materialInfo.setWorkRangeType(data[3].toString());
				}
				// 周期号
				if (null != data[4])
					materialInfo.setRangeNumber(data[4].toString());
				// 开始时间
				if (null != data[5])
					materialInfo.setStartTime(data[5].toString());
				// 工作说明
				if (null != data[6])
					materialInfo.setWorkExplain(data[6].toString());
				arrlist.add(materialInfo);
			}
			pobj.setList(arrlist);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:取得当天定期工作结束", Level.INFO, null);
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:取得当天定期工作失败", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * 通过ID查询定期工作
	 * 
	 * @param id
	 *            序号
	 * @return PageObject
	 */
	public PageObject findById(Long id) {
		LogUtil.log("通过ID查询定期工作正常开始", Level.SEVERE, null);
		try {
			PageObject pobj = new PageObject();
			// 查询sql
			String StrSql = "SELECT "
					      + " TO_CHAR(A.UPDATE_TIME,'yyyy-MM-dd HH:mi:ss'), "
					      + " A.WORK_DATE, "
					      + " A.CLASS_SEQUENCE, "
					      + " A.IS_USE, "
					      + " A.WORKTYPE_CODE, "
					      + " A.SUB_WORKTYPE_CODE, "
					      + " A.WORKITEM_CODE, "
					      + " A.CRT_USER, "
					      + " A.DCM_STATUS "
					      + " FROM "
					      + " AD_J_TIMEWORK A "
					      + " WHERE "
					      + " A.IS_USE = ? AND "
					      + " A.ID = ? ";
					      
			// 查询参数数量
			int paramsCnt = 2;

			// 查询参数数组
			Object[] params = new Object[paramsCnt];
			int i = 0;
			params[i++] = IS_USE_Y;		
			params[i++] = id;
			
			List<AdJTimework> list = bll.queryByNativeSQL(StrSql, params);
			List<AdJTimework> arrlist = new ArrayList<AdJTimework>();
			Iterator it = list.iterator();

			while (it.hasNext()) {
				AdJTimework materialInfo = new AdJTimework();
				Object[] data = (Object[]) it.next();
				// 更新时间
				if (null != data[0]) {
					Date dteUpdate;
					try {
						DateFormat formatLong = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						dteUpdate = formatLong.parse(data[0].toString());
						materialInfo.setUpdateTime(dteUpdate);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				// 工作日期
				if (null != data[1]) {
					Date dteWork;
					try {
						DateFormat formatShort = new SimpleDateFormat(
								"yyyy-MM-dd");
						dteWork = formatShort.parse(data[1].toString());
						materialInfo.setWorkDate(dteWork);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				// 班次
				if (null != data[2]) {
					materialInfo.setClassSequence(data[2].toString());
				}
				// 是否可用
				if (null != data[3]) {
					materialInfo.setIsUse(data[3].toString());
				}
				// 类别编码
				if (null != data[4]) {
					materialInfo.setWorktypeCode(data[4].toString());
				}
				// 子类别编码
				if (null != data[5]) {
					materialInfo.setSubWorktypeCode(data[5].toString());
				}
				// 工作项目编码
				if (null != data[6]) {
					materialInfo.setWorkitemCode(data[6].toString());
				}
				// 登记人
				if (null != data[7]) {
					materialInfo.setCrtUser(data[7].toString());
				}
				// 单据状态
				if (null != data[8]) {
					materialInfo.setDcmStatus(data[8].toString());
				}
				arrlist.add(materialInfo);
			}
			pobj.setList(arrlist);
			LogUtil.log("通过ID查询定期工作正常结束", Level.SEVERE, null);
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("通过ID查询定期工作异常结束", Level.SEVERE, e);
			throw e;
		}
	}
}
