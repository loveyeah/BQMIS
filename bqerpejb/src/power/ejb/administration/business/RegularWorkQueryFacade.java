/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.RegularWorkInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 定期工作查询Facade
 * 
 * @author chaihao
 * 
 */
@Stateless
public class RegularWorkQueryFacade implements RegularWorkQueryFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 定期工作查询
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @param startDate 起始时间
	 * @param endDate 截止时间
	 * @param subWorkType 子类别编码
	 * @param flag 标志
	 * @param workType 工作类别编码
	 * @param rowStartIdxAndCount 检索数据附加参数
	 * @return PageObject 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject findRegularWork(String strEnterpriseCode, String strStartDate, String strEndDate,
			String strSubWorkType, String strFlag, String strWorkType,
			int... rowStartIdxAndCount) throws SQLException {
		LogUtil.log("EJB:定期工作查询开始", Level.INFO, null);
		try {
			// 查询SQL语句
			String strSql = "";
			// 查询行数SQL语句
			String strSqlCount = "";
			// 需要返回的结果
			PageObject result = new PageObject();
			// 构造查询SQL语句
			strSql = "SELECT B.WORKITEM_NAME,"
					+ "A.WORK_DATE,"
					+ "A.WORK_EXPLAIN,"
					+ "A.RESULT,"
					+ "A.MARK,"
					+ "A.OPERATOR,"
					+ "A.MEMO "
					+ "FROM AD_J_TIMEWORK A LEFT JOIN AD_C_TIMEWORK B ON A.WORKITEM_CODE=B.WORKITEM_CODE "
					+ "AND B.ENTERPRISE_CODE=? "
					+ "WHERE "
					+ "A.WORKTYPE_CODE=? "
					+ "AND A.ENTERPRISE_CODE=? "
					+ "AND A.MARK=? "
					+ "AND A.IS_USE=?";
			// 构造查询行数SQL语句
			strSqlCount = "SELECT COUNT(A.ID) "
				    + "FROM AD_J_TIMEWORK A LEFT JOIN AD_C_TIMEWORK B ON A.WORKITEM_CODE=B.WORKITEM_CODE "
				    + "AND B.ENTERPRISE_CODE=? "
				    + "WHERE "
				    + "A.WORKTYPE_CODE=? "
				    + "AND A.ENTERPRISE_CODE=? "
				    + "AND A.MARK=? "
				    + "AND A.IS_USE=?";
			List lstParams = new ArrayList();
			lstParams.add(strEnterpriseCode);
			lstParams.add(strWorkType);
			lstParams.add(strEnterpriseCode);
			lstParams.add(strFlag);
			lstParams.add("Y");
			// 是否具有开始时间
			if ((strStartDate != null) && (strStartDate.length() > 0)) {
				strSql += " AND to_char(A.WORK_DATE, 'yyyy-mm-dd')>=?";
				strSqlCount += " AND to_char(A.WORK_DATE, 'yyyy-mm-dd')>=?";
				lstParams.add(strStartDate);
			}
			// 是否具有截止时间
			if ((strEndDate != null) && (strEndDate.length() > 0)) {
				strSql += " AND to_char(A.WORK_DATE, 'yyyy-mm-dd')<=?";
				strSqlCount += " AND to_char(A.WORK_DATE, 'yyyy-mm-dd')<=?";
				lstParams.add(strEndDate);
			}
			// 画面的工作类别不为空
			if ((strSubWorkType != null) && (strSubWorkType.length() > 0)) {
				strSql += " AND B.SUB_WORKTYPE_CODE=?";
				strSqlCount += " AND B.SUB_WORKTYPE_CODE=?";
				lstParams.add(strSubWorkType);
			}
			Object[] params = lstParams.toArray();
			LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			// 行数
			Long lngTotalCount=Long.parseLong(bll.getSingal(strSqlCount, params).toString());
			// 查询结果List
			List lst = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List lstRegularWorkInfo = new ArrayList();
			if (lst != null) {
				Iterator it = lst.iterator();
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					RegularWorkInfo regularWork = new RegularWorkInfo();
					// 设置工作项目名称
					if (null != data[0]) {
						regularWork.setWorkItemName(data[0].toString());
					}
					// 设置工作日期和星期
					if (null != data[1]) {
						Date dt = (Date) data[1];
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						String date = sdf.format(dt);
						regularWork.setWorkDate(date);
						sdf = new SimpleDateFormat("E");
						String week = sdf.format(dt);
						regularWork.setWeek(week);
					}
					// 设置具体工作内容
					if (null != data[2]) {
						regularWork.setWorkExplain(data[2].toString());
					}
					// 设置工作结果
					if (null != data[3]) {
						regularWork.setResult(data[3].toString());
					}
					// 设置标记
					if (null != data[4]) {
						regularWork.setMark(data[4].toString());
					}
					// 设置操作人
					if (null != data[5]) {
						regularWork.setOperator(data[5].toString());
					}
					// 设置备注
					if (null != data[6]) {
						regularWork.setMemo(data[6].toString());
					}
					lstRegularWorkInfo.add(regularWork);
				}
			}
			// 设置查询结果集
			result.setList(lstRegularWorkInfo);
			// 设置行数
			result.setTotalCount(lngTotalCount);
			LogUtil.log("EJB:定期工作查询结束", Level.INFO, null);
			// 返回查询结果
			return result;
		} catch (Exception re) {
			LogUtil.log("EJB:查询失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

}
