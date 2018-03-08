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
import power.ejb.administration.form.CarFileMaintenInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * @author chaihao
 * 
 */
@Stateless
public class CarFileMaintenFacade implements CarFileMaintenFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 按指定属性查找车辆档案
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @param strStartDate 开始时间
	 * @param strEndDate 截止时间
	 * @param strDepCode 部门编码
	 * @param rowStartIdxAndCount 检索数据库附加参数
	 * @return 结果
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findCarFile(String strEnterpriseCode, String strStartDate, String strEndDate,
			String strDepCode, int... rowStartIdxAndCount)
			throws SQLException {
		LogUtil.log("EJB:车辆档案查询开始", Level.INFO, null);
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
					+ "A.CAR_NO,"
					+ "A.CAR_NAME,"
					+ "A.CAR_KIND,"
					+ "A.CAR_TYPE,"
					+ "A.CAR_FRAME,"
					+ "A.ENGINE_NO,"
					+ "A.LOADMAN,"
					+ "A.WEIGHT,"
					+ "A.EQUIP,"
					+ "A.USE_STATUS,"
					+ "B.DEPT_NAME,"
					+ "C.CHS_NAME," 
					+ "A.RUN_LIC,"
					+ "A.RUN_ALL_LIC,"
					+ "A.BUY_DATE,"
					+ "A.PRICE,"
					+ "A.CARSHOP,"
					+ "A.INVOICE_NO,"
					+ "A.OIL_RATE,"
					+ "A.ISURANCE,"
					+ "A.RUN_MILE,"
					+ "C.EMP_CODE, "
					+ "A.DEP "
					+ "FROM AD_J_CARFILE A LEFT JOIN HR_C_DEPT B ON A.DEP=B.DEPT_CODE AND B.ENTERPRISE_CODE=? " 
					+ "LEFT JOIN HR_J_EMP_INFO C ON A.DRIVER=C.EMP_CODE AND C.ENTERPRISE_CODE=? "
					+ "WHERE A.IS_USE=? "
					+ "AND A.ENTERPRISE_CODE=? ";
			strSqlCount = "SELECT "
					+ "COUNT(A.ID) "
					+ "FROM AD_J_CARFILE A LEFT JOIN HR_C_DEPT B ON A.DEP=B.DEPT_CODE AND B.ENTERPRISE_CODE=? " 
					+ "LEFT JOIN HR_J_EMP_INFO C ON A.DRIVER=C.EMP_CODE AND C.ENTERPRISE_CODE=? "
					+ "WHERE A.IS_USE=? "
					+ "AND A.ENTERPRISE_CODE=? ";
			// 参数个数
			int intParamsCount = 4;
			// 是否具有开始时间
			if(strStartDate!=null && !strStartDate.equals("")){
				intParamsCount++;
			}
			// 是否具有截止时间
			if(strEndDate!=null && !strEndDate.equals("")){
				intParamsCount++;
			}
			// 是否具有部门编码
			if(strDepCode!=null && !strDepCode.equals("")){
				intParamsCount++;
			}
			// 参数数组
			Object[] params = new Object[intParamsCount];
			int i = 0;
			params[i++] = strEnterpriseCode;
			params[i++] = strEnterpriseCode;
			params[i++] = "Y";
			params[i++] = strEnterpriseCode;
			// 设置参数
			if(strStartDate!=null && !strStartDate.equals("")){
				strSql += "AND to_char(A.BUY_DATE, 'yyyy-mm-dd')>=? ";
				strSqlCount += "AND to_char(A.BUY_DATE, 'yyyy-mm-dd')>=? ";
				params[i++] = strStartDate;
			}
			if(strEndDate!=null && !strEndDate.equals("")){
				strSql += "AND to_char(A.BUY_DATE, 'yyyy-mm-dd')<=? ";
				strSqlCount += "AND to_char(A.BUY_DATE, 'yyyy-mm-dd')<=? ";
				params[i++] = strEndDate;
			}
			if(strDepCode!=null && !strDepCode.equals("")){
				strSql += "AND A.DEP LIKE ? ";
				strSqlCount += "AND A.DEP LIKE ? ";
				params[i++] = strDepCode + "%";
			}
			LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			// 执行查询
			Long lngTotalCount = Long.parseLong(bll.getSingal(strSqlCount, params).toString());
			List lst = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<CarFileMaintenInfo> lstCarFileMaintenInfo = new ArrayList<CarFileMaintenInfo>();
			if (lst != null) {
				Iterator it = lst.iterator();
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					CarFileMaintenInfo carFileMaintenInfo = new CarFileMaintenInfo();
					// 设置序号
					if (data[0] != null) {
						carFileMaintenInfo.setId(data[0].toString());
					}
					// 设置车牌号码
					if (data[1] != null) {
						carFileMaintenInfo.setCarNo(data[1].toString());
					}
					// 设置车名
					if (data[2] != null) {
						carFileMaintenInfo.setCarName(data[2].toString());
					}
					// 设置车种
					if (data[3] != null) {
						carFileMaintenInfo.setCarKind(data[3].toString());
					}
					// 设置车型
					if (data[4] != null) {
						carFileMaintenInfo.setCarType(data[4].toString());
					}
					// 设置车架
					if (data[5] != null) {
						carFileMaintenInfo.setCarFrame(data[5].toString());
					}
					// 设置发动机号码
					if (data[6] != null) {
						carFileMaintenInfo.setEngineNo(data[6].toString());
					}
					// 设置载人数
					if (data[7] != null) {
						carFileMaintenInfo.setLoadman(data[7].toString());
					}
					// 设置载重
					if (data[8] != null) {
						carFileMaintenInfo.setWeight(data[8].toString());
					}
					// 设置特殊设备
					if (data[9] != null) {
						carFileMaintenInfo.setEquip(data[9].toString());
					}
					// 设置使用情况
					if (data[10] != null) {
						carFileMaintenInfo.setUseStatus(data[10].toString());
					}
					// 设置部门名称
					if (data[11] != null) {
						carFileMaintenInfo.setDeptName(data[11].toString());
					}
					// 设置驾驶员
					if (data[12] != null) {
						carFileMaintenInfo.setName(data[12].toString());
					}
					// 设置行驶证
					if (data[13] != null) {
						carFileMaintenInfo.setRunLic(data[13].toString());
					}
					// 设置通行证
					if (data[14] != null) {
						carFileMaintenInfo.setRunAllLic(data[14].toString());
					}
					// 设置购买日期
					if (data[15] != null) {
						carFileMaintenInfo.setBuyDate(data[15].toString());
					}
					// 设置购买金额
					if (data[16] != null) {
						carFileMaintenInfo.setPrice(data[16].toString());
					}
					// 设置销售商家
					if (data[17] != null) {
						carFileMaintenInfo.setCarshop(data[17].toString());
					}
					// 设置发票号
					if (data[18] != null) {
						carFileMaintenInfo.setInvoiceNo(data[18].toString());
					}
					// 设置耗油率
					if (data[19] != null) {
						carFileMaintenInfo.setOilRate(data[19].toString());
					}
					// 设置保险费
					if (data[20] != null) {
						carFileMaintenInfo.setIsurance(data[20].toString());
					}
					// 设置行驶里程
					if (data[21] != null) {
						carFileMaintenInfo.setRunMile(data[21].toString());
					}
					// 设置人员编码
					if (data[22] != null) {
						carFileMaintenInfo.setEmpCode(data[22].toString());
					}
					// 设置部门编码
					if (data[23] != null) {
						carFileMaintenInfo.setDep(data[23].toString());
					}
					lstCarFileMaintenInfo.add(carFileMaintenInfo);
				}
			}
			// 设置结果集
			result.setList(lstCarFileMaintenInfo);
			// 设置行数
			result.setTotalCount(lngTotalCount);
			LogUtil.log("EJB:车辆档案查询结束", Level.INFO, null);
			// 返回结果
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:数据库操作失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

}
