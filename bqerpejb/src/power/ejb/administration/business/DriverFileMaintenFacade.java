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
import power.ejb.administration.form.DriverFileMaintenInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * @author chaihao
 * 
 */
@Stateless
public class DriverFileMaintenFacade implements DriverFileMaintenFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 按属性查找司机档案
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @param strDepCode 部门编码
	 * @param strWorkerCode 人员编码
	 * @param strLicence 驾照类型
	 * @param rowStartIdxAndCount 检索数据附加参数
	 * @return 结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject findDriverFile(String strEnterpriseCode, String strDepCode, String strWorkerCode,
			String strLicence, int... rowStartIdxAndCount) throws SQLException  {
		LogUtil.log("EJB:司机档案查询开始", Level.INFO, null);
		try {
			// 查询SQL语句
			String strSql = "";
			// 查询行数SQL语句
			String strSqlCount = "";
			// 需要返回的结果
			PageObject result = new PageObject();
			// 构造查询SQL语句
			strSql = "SELECT "
					+ "A.ID,"
					+ "B.CHS_NAME,"
					+ "B.SEX,"
					+ "CEIL(Months_between(sysdate,B.BRITHDAY)/12) AS AGES,"
					+ "C.DEPT_NAME,"
					+ "A.LICENCE,"
					+ "A.LICENCE_NO,"
					+ "to_char(A.LICENCE_DATE, 'yyyy-mm-dd'),"
					+ "to_char(A.CHECK_DATE, 'yyyy-mm-dd'),"
					+ "A.MOBILE_NO,"
					+ "A.TEL_NO,"
					+ "A.HOME_ADDR,"
					+ "A.COM_ADDR "
					+ "FROM AD_J_DRIVERFILE A LEFT JOIN HR_J_EMP_INFO B "
					+ "ON A.DRIVER_CODE=B.EMP_CODE AND B.ENTERPRISE_CODE=? "
					+ "LEFT JOIN HR_C_DEPT C "
					+ "ON B.DEPT_ID=C.DEPT_ID AND C.ENTERPRISE_CODE=? "
					+ "WHERE "
					+ "A.IS_USE=? "
					+ "AND A.ENTERPRISE_CODE=? ";
			// 构造查询行数SQL语句
			strSqlCount = "SELECT "
					+ "COUNT(A.ID) "
					+ "FROM AD_J_DRIVERFILE A LEFT JOIN HR_J_EMP_INFO B "
					+ "ON A.DRIVER_CODE=B.EMP_CODE AND B.ENTERPRISE_CODE=? "
					+ "LEFT JOIN HR_C_DEPT C "
					+ "ON B.DEPT_ID=C.DEPT_ID AND C.ENTERPRISE_CODE=? "
					+ "WHERE "
					+ "A.IS_USE=? "
					+ "AND A.ENTERPRISE_CODE=? ";
			// 计算参数个数
			int intParamsCount = 4;
			// 是否有部门编码
			if (strDepCode != null && !strDepCode.equals("")) {
				intParamsCount++;
			}
			// 是否有人员编码
			if (strWorkerCode != null && !strWorkerCode.equals("")) {
				intParamsCount++;
			}
			// 是否有驾照类型
			if (strLicence != null && !strLicence.equals("")) {
				intParamsCount++;
			}
			// 构造参数数组
			Object[] params = new Object[intParamsCount];
			int i = 0;
			// 填充参数
			params[i++] = strEnterpriseCode;
			params[i++] = strEnterpriseCode;
			params[i++] = "Y";
			params[i++] = strEnterpriseCode;
			if (strDepCode != null && !strDepCode.equals("")) {
				strSql += "AND C.DEPT_CODE LIKE ? ";
				strSqlCount += "AND C.DEPT_CODE LIKE ? ";
				params[i++] = strDepCode + "%";
			}
			if (strWorkerCode != null && !strWorkerCode.equals("")) {
				strSql += "AND B.EMP_CODE=? ";
				strSqlCount += "AND B.EMP_CODE=? ";
				params[i++] = strWorkerCode;
			}
			if (strLicence != null && !strLicence.equals("")) {
				strSql += "AND A.LICENCE=? ";
				strSqlCount += "AND A.LICENCE=? ";
				params[i++] = strLicence;
			}
			LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			// 执行SQL语句
			Long lngTotalCount = Long.parseLong(bll.getSingal(strSqlCount,
					params).toString());
			List lst = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<DriverFileMaintenInfo> lstDriverFileMaintenInfo = new ArrayList<DriverFileMaintenInfo>();
			if (null != lst) {
				Iterator it = lst.iterator();
				while (it.hasNext()) {
					DriverFileMaintenInfo driverFileMaintenInfo = new DriverFileMaintenInfo();
					Object[] obj = (Object[]) it.next();
					// 设置序号
					if (null != obj[0]) {
						driverFileMaintenInfo.setId(obj[0].toString());
					}
					// 设置姓名
					if (null != obj[1]) {
						driverFileMaintenInfo.setName(obj[1].toString());
					}
					// 设置性别
					if (null != obj[2]) {
						driverFileMaintenInfo.setSex(obj[2].toString());
					}
					// 设置年龄
					if (null != obj[3]) {
						driverFileMaintenInfo.setAges(obj[3].toString());
					}
					// 设置所在部门
					if (null != obj[4]) {
						driverFileMaintenInfo.setDepName(obj[4].toString());
					}
					// 设置驾照类型
					if (null != obj[5]) {
						driverFileMaintenInfo.setLicence(obj[5].toString());
					}
					// 设置驾照号码
					if (null != obj[6]) {
						driverFileMaintenInfo.setLicenceNo(obj[6].toString());
					}
					// 设置办照时间
					if (null != obj[7]) {
						driverFileMaintenInfo.setLicenceDate(obj[7].toString());
					}
					// 设置年检时间
					if (null != obj[8]) {
						driverFileMaintenInfo.setCheckDate(obj[8].toString());
					}
					// 设置手机号码
					if (null != obj[9]) {
						driverFileMaintenInfo.setMobileNo(obj[9].toString());
					}
					// 设置家庭电话
					if (null != obj[10]) {
						driverFileMaintenInfo.setTelNo(obj[10].toString());
					}
					// 设置序号家庭住址
					if (null != obj[11]) {
						driverFileMaintenInfo.setHomeAddr(obj[11].toString());
					}
					// 设置通讯地址
					if (null != obj[12]) {
						driverFileMaintenInfo.setComAddr(obj[12].toString());
					}
					lstDriverFileMaintenInfo.add(driverFileMaintenInfo);
				}
			}
			// 设置查询结果集
			result.setList(lstDriverFileMaintenInfo);
			// 设置行数
			result.setTotalCount(lngTotalCount);
			LogUtil.log("EJB:司机档案查询结束", Level.INFO, null);
			// 返回结果
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	
	/**
	 * 按人员编码查询人员信息
	 * 
	 * params strWorkerCode 人员编码
	 * return PageObject 结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByWorkCode(String strWorkerCode) throws SQLException {

		LogUtil.log("EJB:查询人员信息开始", Level.INFO, null);
		try {
			String strSql = "";
			PageObject result = new PageObject();
			strSql = "SELECT "
				+ "A.SEX,"
				+ "CEIL(Months_between(sysdate,A.BRITHDAY)/12) AS AGES "
				+ "FROM HR_J_EMP_INFO A "
				+ "WHERE A.EMP_CODE=? ";

			// 参数
			Object[] params = new Object[1];
			params[0] = strWorkerCode;
			LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			// 执行SQL语句
			List lst = bll.queryByNativeSQL(strSql, params);
			List<String> lstInfor = new ArrayList<String>();
			if (lst != null && lst.size() != 0) {
				Object[] obj = (Object[]) lst.get(0);
				// 设置性别
				if (null != obj[0]) {
					lstInfor.add(obj[0].toString());
				} else {
					lstInfor.add("");
				}
				// 设置年龄
				if (null != obj[1]) {
					lstInfor.add(obj[1].toString());
				} else {
					lstInfor.add("");
				}
			}
			result.setList(lstInfor);
			LogUtil.log("EJB:查询人员信息结束", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}
}
