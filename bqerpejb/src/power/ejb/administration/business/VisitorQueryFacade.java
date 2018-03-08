/**
 * Copyright ustcsoft.com
 * All right reserved
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
import power.ejb.administration.form.VisitorInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 来访人员查询Facade
 * 
 * @author chaihao
 * 
 */
@Stateless
public class VisitorQueryFacade implements VisitorQueryFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 来访人员查询
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @param startDate 起始时间
	 * @param endDate 截止时间
	 * @param depCode 部门编码
	 * @param rowStartIdxAndCount 数据检索附加参数
	 * @return PageObject 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject findVisitor(String strEnterpriseCode, String strStartDate, String strEndDate,
			String strDepCode, int... rowStartIdxAndCount) throws SQLException {
		LogUtil.log("EJB:来访人员查询开始", Level.INFO, null);
		try {
			// 查询SQL语句
			String strSql = "";
			// 查询行数SQL语句
			String strSqlCount = "";
			// 需要返回的结果
			PageObject result = new PageObject();
			// 构造查询SQL语句
			strSql = "SELECT A.INSERTBY,"
					+ "to_char(A.INSERTDATE,'yyyy-mm-dd  hh24:mi'),"
					+ "A.PAPER_ID,"
					+ "A.FIRM,"
					+ "D.CHS_NAME,"
					+ "C.DEPT_NAME,"
					+ "to_char(A.IN_DATE,'yyyy-mm-dd  hh24:mi'),"
					+ "to_char(A.OUT_DATE,'yyyy-mm-dd  hh24:mi'),"
					+ "A.NOTE,"
					+ "A.ONDUTY,"
					+ "B.PAPERTYPE_NAME "
					+ "FROM AD_J_MANPASS A LEFT JOIN "
					+ "AD_C_PAPER B ON A.PAPERTYPE_CD=B.PAPERTYPE_CODE AND B.ENTERPRISE_CODE=? LEFT JOIN " 
					+ "HR_J_EMP_INFO D ON A.VISITEDMAN=D.EMP_CODE AND D.ENTERPRISE_CODE=? LEFT JOIN "
					+ "HR_C_DEPT C ON A.VISITEDDEP=C.DEPT_CODE AND C.ENTERPRISE_CODE=? "
					+ "WHERE "
					+ "A.IS_USE=? "
					+ "AND A.ENTERPRISE_CODE=?";
			// 构造查询行数SQL语句
			strSqlCount = "SELECT COUNT(A.ID)"
				    + "FROM AD_J_MANPASS A LEFT JOIN "
				    + "AD_C_PAPER B ON A.PAPERTYPE_CD=B.PAPERTYPE_CODE AND B.ENTERPRISE_CODE=? LEFT JOIN " 
				    + "HR_J_EMP_INFO D ON A.VISITEDMAN=D.EMP_CODE AND D.ENTERPRISE_CODE=? LEFT JOIN "
				    + "HR_C_DEPT C ON A.VISITEDDEP=C.DEPT_CODE AND C.ENTERPRISE_CODE=? "
				    + "WHERE "
				    + "A.IS_USE=? "
				    + "AND A.ENTERPRISE_CODE=?";
			List lstParams = new ArrayList();
			lstParams.add(strEnterpriseCode);
			lstParams.add(strEnterpriseCode);
			lstParams.add(strEnterpriseCode);
			lstParams.add("Y");
			lstParams.add(strEnterpriseCode);
			// 是否具有开始时间
			if ((strStartDate != null) && (strStartDate.length() > 0)) {
				strSql += " AND to_char(A.INSERTDATE,'yyyy-mm-dd')>=?";
				strSqlCount += " AND to_char(A.INSERTDATE,'yyyy-mm-dd')>=?";
				lstParams.add(strStartDate);
			}
			// 是否具有截止时间
			if ((strEndDate != null) && (strEndDate.length() > 0)) {
				strSql += " AND to_char(A.INSERTDATE,'yyyy-mm-dd')<=?";
				strSqlCount += " AND to_char(A.INSERTDATE,'yyyy-mm-dd')<=?";
				lstParams.add(strEndDate);
			}
			// 是否具有部门编码参数
			if ((strDepCode != null) && (strDepCode.length() > 0)) {
				strSql += " AND A.VISITEDDEP LIKE ?";
				strSqlCount += " AND A.VISITEDDEP LIKE ?";
				lstParams.add(strDepCode + "%");
			}
			Object[] params = lstParams.toArray();
			LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);

			// 行数
			Long lngTotalCount=Long.parseLong(bll.getSingal(strSqlCount, params).toString());
			// 查询结果List
			List lst = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<VisitorInfo> lstVisitorInfo = new ArrayList<VisitorInfo>();
			if (lst != null) {
				Iterator it = lst.iterator();
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					VisitorInfo visitorInfo = new VisitorInfo();
					// 设置来访人
					if (null != data[0]) {
						visitorInfo.setInsertBy(data[0].toString());
					}
					// 设置来访时间
					if (null != data[1]) {
						visitorInfo.setInsertDate(data[1].toString());
					}
					// 设置证件号
					if (null != data[2]) {
						visitorInfo.setPaperId(data[2].toString());
					}
					// 设置单位
					if (null != data[3]) {
						visitorInfo.setFirm(data[3].toString());
					}
					// 设置被访人
					if (null != data[4]) {
						visitorInfo.setName(data[4].toString());
					}
					// 设置被访人部门
					if (null != data[5]) {
						visitorInfo.setDepName(data[5].toString());
					}
					// 设置进厂时间
					if (null != data[6]) {
						visitorInfo.setInDate(data[6].toString());
					}
					// 设置出厂时间
					if (null != data[7]) {
						visitorInfo.setOutDate(data[7].toString());
					}
					// 设置备注
					if (null != data[8]) {
						visitorInfo.setNote(data[8].toString());
					}
					// 设置值班人
					if (null != data[9]) {
						visitorInfo.setOnDuty(data[9].toString());
					}
					// 设置证件类别
					if (null != data[10]) {
						visitorInfo.setPaperTypeName(data[10].toString());
					}
					lstVisitorInfo.add(visitorInfo);
				}
			}
			// 设置查询结果集
			result.setList(lstVisitorInfo);
			// 设置行数
			result.setTotalCount(lngTotalCount);
			LogUtil.log("EJB:来访人员查询结束。", Level.INFO, null);
			// 返回查询结果
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

}