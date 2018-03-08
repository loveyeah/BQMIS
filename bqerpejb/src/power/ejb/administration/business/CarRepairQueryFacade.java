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
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.CarRepairMXQueryInfo;
import power.ejb.administration.form.CarRepairQueryCarNoInfo;
import power.ejb.administration.form.CarRepairQueryInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * @author chaihao
 * 
 */
@Stateless
public class CarRepairQueryFacade implements CarRepairQueryFacadeRemote {

	@PersistenceContext
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 获取车牌号
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @return 结果
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getCarNo(String strEnterpriseCode) throws SQLException {
		LogUtil.log("EJB:检索车牌号开始", Level.INFO, null);
		try {
			// 查询SQL语句
			String strSql = "";
			// 需要返回的结果
			PageObject result = new PageObject();
            // 构造查询SQL语句
			strSql = "SELECT A.CAR_NO "
				+ "FROM AD_J_CARFILE A "
				+ "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE=?";
            // SQL语句参数
			Object[] params = new Object[2];
			params[0] = "Y";
			params[1] = strEnterpriseCode;
			LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			// 执行查询
			List lst = bll.queryByNativeSQL(strSql, params);
			List<CarRepairQueryCarNoInfo> lstCarRepairQueryCarNoInfo = new ArrayList<CarRepairQueryCarNoInfo>();
			// 追加空值
			CarRepairQueryCarNoInfo objNull = new CarRepairQueryCarNoInfo();
			objNull.setCarNo("");
			lstCarRepairQueryCarNoInfo.add(objNull);
			if (lst != null) {
				Iterator it = lst.iterator();
				while (it.hasNext()) {
					Object obj = (Object) it.next();
					CarRepairQueryCarNoInfo carRepairQueryCarNoInfo = new CarRepairQueryCarNoInfo();
					// 设置车牌号
					if (obj != null) {
						carRepairQueryCarNoInfo.setCarNo(obj.toString());
					}
					lstCarRepairQueryCarNoInfo.add(carRepairQueryCarNoInfo);
				}
			}
			// 设置结果集
			result.setList(lstCarRepairQueryCarNoInfo);
			LogUtil.log("EJB:检索车牌号结束", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:检索车牌号失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 车辆维修查询
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @param strStartDate 起始日期
	 * @param strEndDate 截止日期
	 * @param strManager 经办人
	 * @param strCarNo 车牌号
	 * @param strDcmStatus 单据状态
	 * @return 结果
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getCarRepair(String strEnterpriseCode, String strStartDate, String strEndDate,
			String strManager, String strCarNo, String strDcmStatus)
			throws SQLException {
		LogUtil.log("EJB:车辆维修查询开始", Level.INFO, null);
		try {
			// 查询SQL语句
			String strSql = "";
			// 查询行数SQL语句
			String strSqlCount = "";
			// 需要返回的结果
			PageObject result = new PageObject();
			// 构造查询SQL语句
			strSql = "SELECT " +
					"A.WH_ID," +
					"A.DCM_STATUS," +
					"A.CAR_NO," +
					"E.CAR_NAME," +
					"to_char(A.REPAIR_DATE,'yyyy-mm-dd')," +
					"D.PRO_NAME," +
					"D.HAVE_LISE," +
					"B.PRO_CODE," +
					"B.PRICE," +
					"B.REAL_PRICE," +
					"A.REAL_SUM," +
					"F.CP_NAME," +
					"A.DRIVE_MILE," +
					"C.CHS_NAME," +
					"A.REASON," +
					"A.MEMO " +
					"FROM AD_J_CARWH A LEFT JOIN AD_J_CARFILE E " +
					"ON A.CAR_NO=E.CAR_NO AND E.ENTERPRISE_CODE=? " +
					"LEFT JOIN AD_J_CARWH_MX B " +
					"ON A.WH_ID=B.WH_ID " +
					"AND B.IS_USE=? " +
					"LEFT JOIN HR_J_EMP_INFO C " +
					"ON A.MANAGER=C.EMP_CODE AND C.ENTERPRISE_CODE=? " +
					"LEFT JOIN AD_C_CARWH_PRO D " +
					"ON B.PRO_CODE=D.PRO_CODE AND D.ENTERPRISE_CODE=? " +
					"LEFT JOIN AD_C_CARMEND_WH F " +
					"ON A.CP_CODE=F.CP_CODE AND F.ENTERPRISE_CODE=? " +
					"WHERE A.IS_USE=? " +
					"AND A.ENTERPRISE_CODE=? " +
					"AND to_char(A.REPAIR_DATE,'yyyy-mm-dd')>=? " +
					"AND to_char(A.REPAIR_DATE,'yyyy-mm-dd')<=? ";
			// 构造查询行数SQL语句
			strSqlCount = "SELECT " +
					"COUNT(A.WH_ID) " +
					"FROM AD_J_CARWH A LEFT JOIN AD_J_CARFILE E " +
					"ON A.CAR_NO=E.CAR_NO AND E.ENTERPRISE_CODE=? " +
					"LEFT JOIN AD_J_CARWH_MX B " +
					"ON A.WH_ID=B.WH_ID " +
					"AND B.IS_USE=? " +
					"LEFT JOIN HR_J_EMP_INFO C " +
					"ON A.MANAGER=C.EMP_CODE AND C.ENTERPRISE_CODE=? " +
					"LEFT JOIN AD_C_CARWH_PRO D " +
					"ON B.PRO_CODE=D.PRO_CODE AND D.ENTERPRISE_CODE=? " +
					"LEFT JOIN AD_C_CARMEND_WH F " +
					"ON A.CP_CODE=F.CP_CODE AND F.ENTERPRISE_CODE=? " +
					"WHERE A.IS_USE=? " +
					"AND A.ENTERPRISE_CODE=? " +
					"AND to_char(A.REPAIR_DATE,'yyyy-mm-dd')>=? " +
					"AND to_char(A.REPAIR_DATE,'yyyy-mm-dd')<=? ";
			// SQL语句参数
			List lstParams = new ArrayList();
			lstParams.add(strEnterpriseCode);
			lstParams.add("Y");
            lstParams.add(strEnterpriseCode);
            lstParams.add(strEnterpriseCode);
            lstParams.add(strEnterpriseCode);
			lstParams.add("Y");
			lstParams.add(strEnterpriseCode);
            lstParams.add(strStartDate);
            lstParams.add(strEndDate);
            // 是否具有经办人
            if(isNotNull(strManager)){
            	strSql += "AND A.MANAGER=? ";
            	strSqlCount += "AND A.MANAGER=? ";
            	lstParams.add(strManager);
            }
            // 是否具有车牌号
            if(isNotNull(strCarNo)){
            	strSql += "AND A.CAR_NO=? ";
            	strSqlCount += "AND A.CAR_NO=? ";
            	lstParams.add(strCarNo);
            }
            // 是否具有单据状态
            if(isNotNull(strDcmStatus)){
            	strSql += "AND A.DCM_STATUS=? ";
            	strSqlCount += "AND A.DCM_STATUS=?";
            	lstParams.add(strDcmStatus);
            }
            // 按车辆维护序号排序
            strSql += "ORDER BY A.WH_ID";
            Object[] params = lstParams.toArray();
            LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			// 行数
			Long lngTotalCount=Long.parseLong(bll.getSingal(strSqlCount, params).toString());
			// 查询结果List
			List lst = bll.queryByNativeSQL(strSql, params);
			List<CarRepairQueryInfo> lstCarRepairQueryInfo = new ArrayList<CarRepairQueryInfo>();
			if (lst != null) {
				Iterator it = lst.iterator();
				String strWhId = "";
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					CarRepairQueryInfo carRepairQueryInfo = new CarRepairQueryInfo();
					// 设置隐藏维护序号
					if(null != data[0]){
						carRepairQueryInfo.setHdnWhId(data[0].toString());
					}
					// 维护序号相同，以下项目设为空
					if (null != data[0] && strWhId.equals(data[0].toString())) {
						// 设置车辆维护序号
						carRepairQueryInfo.setWhId("");
						// 设置单据状态
						carRepairQueryInfo.setDcmStatus("");
						// 设置车号
						carRepairQueryInfo.setCarNo("");
						// 设置车名
						carRepairQueryInfo.setCarName("");
						// 设置维修日期
						carRepairQueryInfo.setRepairDate("");
						// 设置单位编码
						carRepairQueryInfo.setCpName("");
						// 设置维修里程
						carRepairQueryInfo.setDriveMile("");
						// 设置经办人
						carRepairQueryInfo.setChsName("");
						// 设置支出事由
						carRepairQueryInfo.setReason("");
						// 设置备注
						carRepairQueryInfo.setMemo("");
						// 设置实际费用合计
						carRepairQueryInfo.setRealSum("");
					} else {
						if (null != data[0]) {
							carRepairQueryInfo.setWhId(data[0].toString());
						}
						// 设置单据状态
						if (null != data[1]) {
							carRepairQueryInfo.setDcmStatus(data[1].toString());
						}
						// 设置车牌号
						if (null != data[2]) {
							carRepairQueryInfo.setCarNo(data[2].toString());
						}
						// 设置车名
						if (null != data[3]) {
							carRepairQueryInfo.setCarName(data[3].toString());
						}
						// 设置维修日期
						if (null != data[4]) {
							carRepairQueryInfo.setRepairDate(data[4].toString());
						}
						// 设置实际费用合计
						if (null != data[10]) {
							carRepairQueryInfo.setRealSum(data[10].toString());
						}
						// 设置维修单位
						if (null != data[11]) {
							carRepairQueryInfo.setCpName(data[11].toString());
						}
						// 设置维修里程
						if (null != data[12]) {
							carRepairQueryInfo.setDriveMile(data[12].toString());
						}
						// 设置经办人
						if (null != data[13]) {
							carRepairQueryInfo.setChsName(data[13].toString());
						}
						// 设置支出事由
						if (null != data[14]) {
							carRepairQueryInfo.setReason(data[14].toString());
						}
						// 设置备注
						if (null != data[15]) {
							carRepairQueryInfo.setMemo(data[15].toString());
						}
					}
					// 设置项目名称
					if (null != data[5]) {
						carRepairQueryInfo.setProName(data[5].toString());
					}
					// 设置有无清单
					if (null != data[6]) {
						carRepairQueryInfo.setHaveLise(data[6].toString());
					}
					// 设置项目编码
					if (null != data[7]) {
						carRepairQueryInfo.setProCode(data[7].toString());
					}
					// 设置预算费用
					if (null != data[8]) {
						carRepairQueryInfo.setPrice(data[8].toString());
					}
					// 设置实际费用
					if (null != data[9]) {
						carRepairQueryInfo.setRealPrice(data[9].toString());
					}
					strWhId = data[0].toString();
					lstCarRepairQueryInfo.add(carRepairQueryInfo);
				}
			}
			// 设置查询结果集
			result.setList(lstCarRepairQueryInfo);
			// 设置行数
			result.setTotalCount(lngTotalCount);
			LogUtil.log("EJB:车辆维修查询结束。", Level.INFO, null);
			// 返回查询结果
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 车辆维修明细查询
	 * 
	 * @param strEnterpriseCode 企业代码
	 * @param strWhId 车辆维护序号
	 * @param strProCode 项目编码
	 * @param rowStartIdxAndCount 检索数据库附加参数
	 * @return 结果
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getCarRepairList(String strEnterpriseCode, String strWhId, String strProCode,
			int... rowStartIdxAndCount) throws SQLException {
		LogUtil.log("EJB:车辆维修明细查询开始", Level.INFO, null);
		try {
			// 查询SQL语句
			String strSql = "";
			// 查询行数SQL语句
			String strSqlCount = "";
			// 需要返回的结果
			PageObject result = new PageObject();
			// 构造查询SQL语句
			strSql = "SELECT " +
					"A.WH_ID," +
					"B.PRO_NAME," +
					"A.PART_NAME," +
					"A.UNIT," +
					"A.NUM," +
					"A.UNIT_PRICE," +
					"A.REAL_NUM," +
					"A.REAL_UNIT_PRICE," +
					"A.NOTE " +
					"FROM AD_J_CARWH_LIST A LEFT JOIN AD_C_CARWH_PRO B " +
					"ON A.PRO_CODE=B.PRO_CODE AND B.ENTERPRISE_CODE=? " +
					"WHERE A.IS_USE=? ";
			// 构造查询行数SQL语句
			strSqlCount = "SELECT " +
					"COUNT(A.WH_ID) " +
					"FROM AD_J_CARWH_LIST A LEFT JOIN AD_C_CARWH_PRO B " +
					"ON A.PRO_CODE=B.PRO_CODE AND B.ENTERPRISE_CODE=? " +
					"WHERE A.IS_USE=? ";
			List lstParams = new ArrayList();
			lstParams.add(strEnterpriseCode);
			lstParams.add("Y");
			// 是否具有车辆维护序号
            if(isNotNull(strWhId)){
            	strSql += "AND A.WH_ID=? ";
            	strSqlCount += "AND A.WH_ID=?";
            	lstParams.add(strWhId);
            }
            // 是否具有项目编码
            if(isNotNull(strProCode)){
            	strSql += "AND A.PRO_CODE=? ";
            	strSqlCount += "AND A.PRO_CODE=?";
            	lstParams.add(strProCode);
            }
            Object[] params = lstParams.toArray();
            LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			// 行数
			Long lngTotalCount=Long.parseLong(bll.getSingal(strSqlCount, params).toString());
			// 查询结果List
			List lst = bll.queryByNativeSQL(strSql, params);
			List<CarRepairMXQueryInfo> lstCarRepairMXQueryInfo = new ArrayList<CarRepairMXQueryInfo>();
			if (lst != null) {
				Iterator it = lst.iterator();
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					CarRepairMXQueryInfo carRepairMXQueryInfo = new CarRepairMXQueryInfo();
					// 设置序号
					if (null != data[0]) {
						carRepairMXQueryInfo.setWhId(data[0].toString());
					}
					// 设置项目名称
					if (null != data[1]) {
						carRepairMXQueryInfo.setProName(data[1].toString());
					}
					// 设置零件名称
					if (null != data[2]) {
						carRepairMXQueryInfo.setPartName(data[2].toString());
					}
					// 设置单位
					if (null != data[3]) {
						carRepairMXQueryInfo.setUnit(data[3].toString());
					}
					// 设置预算数量
					if (null != data[4]) {
						carRepairMXQueryInfo.setPreNum(data[4].toString());
					}
					// 设置预算单价
					if (null != data[5]) {
						carRepairMXQueryInfo.setPreUnitPrice(data[5].toString());
					}
					// 设置实际数量
					if (null != data[6]) {
						carRepairMXQueryInfo.setRealNum(data[6].toString());
					}
					// 设置实际单价
					if (null != data[7]) {
						carRepairMXQueryInfo.setRealUnitPrice(data[7].toString());
					}
					// 设置备注
					if (null != data[8]) {
						carRepairMXQueryInfo.setNote(data[8].toString());
					}
					lstCarRepairMXQueryInfo.add(carRepairMXQueryInfo);
				}
			}
			// 设置查询结果集
			result.setList(lstCarRepairMXQueryInfo);
			// 设置行数
			result.setTotalCount(lngTotalCount);
			LogUtil.log("EJB:车辆维修明细查询结束。", Level.INFO, null);
			// 返回查询结果
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	
	/**
	 * 判断字符串是否为空
	 * 
	 * @param str 字符串
	 * @return boolean
	 */
	private boolean isNotNull(String str){
		boolean flag = false;
		if(str != null && !str.equals("")){
			flag = true;
		}
		return flag;
	}


}
