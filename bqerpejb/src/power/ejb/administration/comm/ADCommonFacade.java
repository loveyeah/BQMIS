/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.ejb.administration.comm;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJCarfile;
import power.ejb.administration.AdJCarfileFacadeRemote;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 编码生成处理
 * 
 * @author 谭晓苏
 */
@Stateless
public class ADCommonFacade implements ADCommonFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	// 添加车辆档案接口 
	@EJB(beanName = "AdJCarfileFacade")
	AdJCarfileFacadeRemote adjfileremote;
	
	// 日期格式
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	// 使用标志
	private static final String USE_FLG = "Y";
	
	/**
	 * 根据当前用户的工作类别编码取得对应的所有值别。
	 * 
	 * @param String strWorkTypeCode 当前用户的工作类别编码
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getDutyTypeInfoList(String strWorkTypeCode, String strEnterPriseCode) throws SQLException {
		try {
			PageObject result = new PageObject();
			String strSql = "";
			// 取得系统时间
			DateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			String sysDate = sdf.format(new Date());
			// SQL文
			strSql = "SELECT "
				+ "DUTY_TYPE, "
				+ "DUTY_TYPE_NAME "
				+ "FROM "
				+ "AD_C_DUTYTYPE "
				+ "WHERE "
				+ "IS_USE = ? AND  "
				+ "WORKTYPE_CODE = ? AND "
				+ "ENTERPRISE_CODE = ? AND "
				+ "START_TIME <= TO_DATE('" + sysDate + "', 'YYYY-MM-DD') AND "
				+ "END_TIME >= TO_DATE('" + sysDate + "', 'YYYY-MM-DD')";

			// 查询参数数组
			Object[] params = new Object[3];
			params[0] = USE_FLG;
			params[1] = strWorkTypeCode;
			params[2] = strEnterPriseCode;
			
	        LogUtil.log("EJB:取得所有值别开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
	        
            List list = bll.queryByNativeSQL(strSql, params);
            List<ComAdCDutytype> lstResult = new ArrayList<ComAdCDutytype>();
            for (int i = 0;i < list.size();i++){
            	ComAdCDutytype temp = new ComAdCDutytype();
            	Object[] data = (Object[])list.get(i);
            	// 值别
            	if (null != data[0]){
            		temp.setDutyType(data[0].toString());
            	}
            	// 值别名称
            	if (null != data[1]){
            		temp.setDutyTypeName(data[1].toString());
            	}
            	lstResult.add(temp);
            }
            // 追加空值
            ComAdCDutytype objNull = new ComAdCDutytype();
            objNull.setDutyType("");
            objNull.setDutyTypeName("");
            lstResult.add(0, objNull);
            
            result.setList(lstResult);
            result.setTotalCount((long)lstResult.size());
	        LogUtil.log("EJB:取得所有值别结束。", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:取得所有值别失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	
	/**
	 * 证件类别编码表数据检索
	 *  
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getPaperInfoList(String strEnterPriseCode) throws SQLException {

		try {
			PageObject result = new PageObject();
			String strSql = "";
			
			strSql = "SELECT "
				+ "PAPERTYPE_CODE, "
				+ "PAPERTYPE_NAME "
				+ "FROM "
				+ "AD_C_PAPER "
				+ "WHERE "
				+ "ENTERPRISE_CODE = ? AND "
				+ "IS_USE = ? ";

			// 查询参数数组
			Object[] params = new Object[2];
			params[0] = strEnterPriseCode;
			params[1] = USE_FLG;
			
	        LogUtil.log("EJB:取得所有证件类别开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);

            List list=bll.queryByNativeSQL(strSql, params);
            List<ComAdCPaper> lstResult = new ArrayList<ComAdCPaper>();
            for (int i = 0;i < list.size();i++){
            	ComAdCPaper temp = new ComAdCPaper();
            	Object[] data = (Object[])list.get(i);
            	// 类别编码
            	if (null != data[0]){
            		temp.setPapertypeCode(data[0].toString());
            	}
            	// 类别名称
            	if (null != data[1]){
            		temp.setPapertypeName(data[1].toString());
            	}
            	lstResult.add(temp);
            }
            // 追加空值
            ComAdCPaper objNull = new ComAdCPaper();
            objNull.setPapertypeName("");
            objNull.setPapertypeCode("");
            lstResult.add(0, objNull);

            result.setList(lstResult);
            result.setTotalCount((long)lstResult.size());
	        LogUtil.log("EJB:取得所有证件类别结束。", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:取得所有证件类别失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 根据类别编码，检索此类别下的所有子类别编码和子类别名称
	 * 
	 * @param String strWorkTypeCode 类别编码
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWorkTypeInfoList(String strWorkTypeCode, String strEnterPriseCode) throws SQLException {
		try {
			PageObject result = new PageObject();
			String strSql = "";

			strSql = "SELECT "
				+ "SUB_WORKTYPE_CODE, "
				+ "SUB_WORKTYPE_NAME "
				+ "FROM "
				+ "AD_C_WORKTYPE "
				+ "WHERE "
				+ "IS_USE = ? AND  "
				+ "ENTERPRISE_CODE = ? AND "
				+ "WORKTYPE_CODE = ? ";

			// 查询参数数组
			Object[] params = new Object[3];
			params[0] = USE_FLG;
			params[1] = strEnterPriseCode;
			params[2] = strWorkTypeCode;
			
	        LogUtil.log("EJB:取得子类别编码和子类别名称开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);

            List list=bll.queryByNativeSQL(strSql, params);
            List<ComAdCWorktype> lstResult = new ArrayList<ComAdCWorktype>();
            for (int i = 0;i < list.size();i++){
            	ComAdCWorktype temp = new ComAdCWorktype();
            	Object[] data = (Object[])list.get(i);
            	// 子类别编码
            	if (null != data[0]){
            		temp.setSubWorktypeCode(data[0].toString());
            	}
            	// 子类别名称
            	if (null != data[1]){
            		temp.setSubWorktypeName(data[1].toString());
            	}
            	lstResult.add(temp);
            }
            // 追加空值
            ComAdCWorktype objNull = new ComAdCWorktype();
            objNull.setSubWorktypeCode("");
            objNull.setSubWorktypeName("");
            lstResult.add(0, objNull);

            result.setList(lstResult);
            result.setTotalCount((long)lstResult.size());
	        LogUtil.log("EJB:取得子类别编码和子类别名称结束。", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:取得子类别编码和子类别名称失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	
	}

	/**
	 * 检索车辆档案表
	 * 
	 * @param int rowStartIdxAndCount 分页
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getCarInfoList(String strEnterPriseCode, final int... rowStartIdxAndCount) throws SQLException {
		try {
			PageObject result = new PageObject();
			String strSql = "";
			String strSqlCount = "";
			
			strSql = "SELECT "
				+ "A.CAR_NO, "
				+ "A.CAR_NAME, "
				+ "A.CAR_KIND, "
				+ "A.CAR_TYPE, "
				+ "A.RUN_MILE, "
				+ "A.DRIVER, "
				+ "A.ID, "
				+ "A.UPDATE_TIME, "
				+ "B.CHS_NAME "
				+ "FROM "
				+ "AD_J_CARFILE A LEFT JOIN HR_J_EMP_INFO B ON B.EMP_CODE = A.DRIVER AND B.ENTERPRISE_CODE = ?"
				+ "WHERE "
				+ "A.IS_USE = ? AND "
				+ "A.ENTERPRISE_CODE = ? AND "
				+ "A.USE_STATUS = ? ";
			strSqlCount = "SELECT "
				+ "COUNT(A.ID) "
				+ "FROM "
				+ "AD_J_CARFILE A "
				+ "WHERE "
				+ "A.IS_USE = ? AND  "
				+ "A.ENTERPRISE_CODE = ? AND "
				+ "A.USE_STATUS = ? ";

			// Sql查询参数数组
			Object[] paramsSql = new Object[4];
			paramsSql[0] = strEnterPriseCode;
			paramsSql[1] = USE_FLG;
			paramsSql[2] = strEnterPriseCode;
			paramsSql[3] = "N";

			// SqlCount查询参数数组
			Object[] paramsSqlCount = new Object[3];
			paramsSqlCount[0] = USE_FLG;
			paramsSqlCount[1] = strEnterPriseCode;
			paramsSqlCount[2] = "N";
			
	        LogUtil.log("EJB:检索车辆档案表开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
	        LogUtil.log("EJB:SQLCOUNT=" + strSqlCount, Level.INFO, null);

            List list=bll.queryByNativeSQL(strSql, paramsSql, rowStartIdxAndCount);
            List<ComAdJCarfile> lstResult = new ArrayList<ComAdJCarfile>();
            for (int i = 0;i < list.size();i++){
            	ComAdJCarfile temp = new ComAdJCarfile();
            	Object[] data = (Object[])list.get(i);
            	// 车牌号
            	if (null != data[0]){
            		temp.setCarNo(data[0].toString());
            	}
            	// 车名
            	if (null != data[1]){
            		temp.setCarName(data[1].toString());
            	}
            	// 车种
            	if (null != data[2]){
            		temp.setCarKind(data[2].toString());
            	}
            	// 车型
            	if (null != data[3]){
            		temp.setCarType(data[3].toString());
            	}
            	// 行车里程
            	if (null != data[4]){
            		temp.setRunMile(Double.parseDouble(data[4].toString()));
            	}
            	// 司机编码
            	if (null != data[5]){
            		temp.setDriver(data[5].toString());
            	}
            	// ID
            	temp.setId(data[6].toString());
            	// 更新时间
            	if (null != data[7]){
            		//修改更新时间为long 
            		AdJCarfile entity = adjfileremote.findById(new Long(data[6].toString()));
            		temp.setUpdateTime(entity.getUpdateTime().getTime());
            	}
            	// 司机名
            	if (null != data[8]){
            		temp.setDriverName(data[8].toString());
            	}
            	
            	lstResult.add(temp);
            }
            
            Long lngCount = Long.parseLong(bll.getSingal(strSqlCount, paramsSqlCount).toString());
            result.setList(lstResult);
            result.setTotalCount(lngCount);

	        LogUtil.log("EJB:检索车辆档案表结束。", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:检索车辆档案表失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 根据司机档案，检索出司机编码和姓名
	 * 
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getDriverInfoList(String strEnterPriseCode) throws SQLException {
		try {
			PageObject result = new PageObject();
			String strSql = "";
			
			strSql = "SELECT "
				+ "AD_J_DRIVERFILE.DRIVER_CODE, "
				+ "HR_J_EMP_INFO.CHS_NAME "
				+ "FROM "
				+ "AD_J_DRIVERFILE, "
				+ "HR_J_EMP_INFO "
				+ "WHERE "
				+ "AD_J_DRIVERFILE.IS_USE = ? AND "
				// modify by liuyi 090909 15:00 表中午is_use属性
//				+ "HR_J_EMP_INFO.IS_USE = ? AND "
				+ "AD_J_DRIVERFILE.ENTERPRISE_CODE = ? AND "
				+ "HR_J_EMP_INFO.ENTERPRISE_CODE = ? AND "
				+ "HR_J_EMP_INFO.EMP_CODE = AD_J_DRIVERFILE.DRIVER_CODE ";
			
			// 查询参数数组
			// modify by liuyi 090909 15:02
//			Object[] params = new Object[4];
			Object[] params = new Object[3];
			params[0] = USE_FLG;
//			params[1] = USE_FLG;
			params[1] = strEnterPriseCode;
			params[2] = strEnterPriseCode;
			
	        LogUtil.log("EJB:检索司机编码和姓名开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);

            List list=bll.queryByNativeSQL(strSql, params);
            List<ComAdJDriverfile> lstResult = new ArrayList<ComAdJDriverfile>();
            for (int i = 0;i < list.size();i++){
            	ComAdJDriverfile temp = new ComAdJDriverfile();
            	Object[] data = (Object[])list.get(i);
            	// 司机编号
            	if (null != data[0]){
            		temp.setDriverCode(data[0].toString());
            	}
            	// 姓名
            	if (null != data[1]){
            		temp.setDriverName(data[1].toString());
            	}
            	lstResult.add(temp);
            }
            // 追加空值
            ComAdJDriverfile objNull = new ComAdJDriverfile();
            objNull.setDriverCode("");
            objNull.setDriverName("");
            lstResult.add(0, objNull);
            
            result.setList(lstResult);
            result.setTotalCount((long)lstResult.size());
	        LogUtil.log("EJB:检索司机编码和姓名结束。", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:检索司机编码和姓名失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 根据部门编码，检索出该部门内的人员
	 * 
	 * @param String strDeptCode 部门编码
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWorkerByDept(String strDeptCode, String strEnterPriseCode) throws SQLException {
		try {
			PageObject result = new PageObject();
			String strSql = "";
			
			strSql = "SELECT "
				+ "HR_J_EMP_INFO.EMP_CODE,  "
				+ "HR_J_EMP_INFO.CHS_NAME  "
				+ "FROM "
				+ "HR_J_EMP_INFO, "
				+ "HR_C_DEPT "
				+ "WHERE  "
				//+ "HR_J_EMP_INFO.IS_USE = ? AND  "//modify by drdu 090909
				+ "HR_C_DEPT.IS_USE = 'U' AND  "
				+ "HR_C_DEPT.DEPT_ID = HR_J_EMP_INFO.DEPT_ID AND "
				+ "HR_J_EMP_INFO.ENTERPRISE_CODE = ? AND "
				+ "HR_C_DEPT.ENTERPRISE_CODE = ? AND "
			    + "HR_C_DEPT.DEPT_CODE LIKE ? ";
			
			// 查询参数数组
			Object[] params = new Object[3];
			//modify by drdu 090909
			//params[0] = USE_FLG;
			//params[1] = USE_FLG;
			params[0] = strEnterPriseCode;
			params[1] = strEnterPriseCode;
			params[2] = strDeptCode + "%";
			
	        LogUtil.log("EJB:检索人员编码和姓名开始。", Level.INFO, null);
	        LogUtil.log("SQL=" + strSql, Level.INFO, null);

            List list=bll.queryByNativeSQL(strSql, params);
            List<WorkerEmployeeInform> lstResult = new ArrayList<WorkerEmployeeInform>();
            for (int i = 0;i < list.size();i++){
            	WorkerEmployeeInform temp = new WorkerEmployeeInform();
            	Object[] data = (Object[])list.get(i);
            	// 人员基本信息表.人员编码
            	if (null != data[0]){
            		temp.setWorkerCode(data[0].toString());
            	}
            	// 人员基本信息表.姓名
            	if (null != data[1]){
            		temp.setName(data[1].toString());
            	}
            	lstResult.add(temp);
            }
            // 追加空值
            WorkerEmployeeInform objNull = new WorkerEmployeeInform("", "");
            lstResult.add(0, objNull);
            result.setList(lstResult);
	        LogUtil.log("EJB:检索人员编码和姓名结束。", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:检索人员编码和姓名失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	
	/**
	 * 根据用户ID，在权限设置表里取得用户对应的权限设置。
	 * 
	 * @param String strUserID 人员编码
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getUserRight(String strUserID, String strEnterPriseCode) throws SQLException {
		try {
			PageObject result = new PageObject();
			String strSql = "";
			strSql = "SELECT "
				+ "AD_C_RIGHT.USER_CODE,  "
				+ "AD_C_RIGHT.WORKTYPE_CODE, "
				+ "AD_C_WORKTYPE.WORKTYPE_NAME "
				+ "FROM "
				+ "AD_C_WORKTYPE,"
				+ "AD_C_RIGHT "
				+ "WHERE "
				+ "AD_C_RIGHT.IS_USE = ? "
				+ "AND AD_C_WORKTYPE.IS_USE = ? "
				+ "AND AD_C_WORKTYPE.WORKTYPE_CODE = AD_C_RIGHT.WORKTYPE_CODE "
				+ "AND AD_C_WORKTYPE.ENTERPRISE_CODE = ? "
				+ "AND AD_C_RIGHT.ENTERPRISE_CODE = ? "
				+ "AND AD_C_RIGHT.USER_CODE = ? ";
			// 查询参数数组
			Object[] params = new Object[5];
			params[0] = USE_FLG;
			params[1] = USE_FLG;
			params[2] = strEnterPriseCode;
			params[3] = strEnterPriseCode;
			params[4] = strUserID;
			
	        LogUtil.log("EJB:取得用户对应的权限设置开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);

            List list=bll.queryByNativeSQL(strSql, params);
            
            List<ComAdCRight> lstResult = new ArrayList<ComAdCRight>();
            for (int i = 0;i < list.size();i++){
            	ComAdCRight temp = new ComAdCRight();
            	Object[] data = (Object[])list.get(i);
            	// 人员编码
            	if (null != data[0]){
            		temp.setUserCode(data[0].toString());
            	}
            	// 工作类别编码
            	if (null != data[1]){
            		temp.setWorktypeCode(data[1].toString());
            	}
            	// 工作类别名称
            	if (null != data[2]){
            		temp.setWorktypeName(data[2].toString());
            	}
            	lstResult.add(temp);
            }
            result.setList(lstResult);
            result.setTotalCount((long)lstResult.size());
	        LogUtil.log("EJB:取得用户对应的权限设置结束。", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:取得用户对应的权限设置失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	
	/**
	 * 检索所有的计量单位名称和计量单位标志
	 * 
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getUnit() throws SQLException {
		try {
			PageObject result = new PageObject();
			String strSql = "";
			
			strSql = "SELECT "
				+ "UNIT_ID, "
				+ "UNIT_NAME "
				+ "FROM "
				+ "BP_C_MEASURE_UNIT "
				+ "WHERE "
				+ "IS_USED = ? ";
			// 查询参数数组
			Object[] params = new Object[1];
			params[0] = USE_FLG;
			
	        LogUtil.log("EJB:取得所有计量单位名称和计量单位标志开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);

            List list=bll.queryByNativeSQL(strSql, params);
            List<ComBpCMeasureUnit> lstResult = new ArrayList<ComBpCMeasureUnit>();
            for (int i = 0;i < list.size();i++){
            	ComBpCMeasureUnit temp = new ComBpCMeasureUnit();
            	Object[] data = (Object[])list.get(i);
            	// 计量单位编码维护.计量单位标识
            	if (null != data[0]){
            		temp.setStrUnitID(data[0].toString());
            	}
            	// 计量单位编码维护.计量单位名称
            	if (null != data[1]){
            		temp.setStrUnitName(data[1].toString());
            	}
            	lstResult.add(temp);
            }
            // 追加空值
            ComBpCMeasureUnit objNull = new ComBpCMeasureUnit();
            objNull.setStrUnitID("");
            objNull.setStrUnitName("");
            lstResult.add(0, objNull);

            result.setList(lstResult);
            result.setTotalCount((long)lstResult.size());
	        LogUtil.log("EJB:取得所有计量单位名称和计量单位标志结束。", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:取得所有计量单位名称和计量单位标志失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	
	/**
	 * 检索所有菜谱类别
	 * 
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAllCMenuType(String strEnterPriseCode) throws SQLException {
		try {
			PageObject result = new PageObject();
			String strSql = "";
			
			strSql = "SELECT "
				+ "MENUTYPE_CODE, "
				+ "MENUTYPE_NAME "
				+ "FROM "
				+ "AD_C_MENU_TYPE "
				+ "WHERE "
				+ "IS_USE = ? "
				+ "AND ENTERPRISE_CODE = ? ";
			
			// 查询参数数组
			Object[] params = new Object[2];
			params[0] = USE_FLG;
			params[1] = strEnterPriseCode;
			
	        LogUtil.log("EJB:取得所有菜谱类别开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);

            List list=bll.queryByNativeSQL(strSql, params);
            List<ComAdCMenuType> lstResult = new ArrayList<ComAdCMenuType>();
            for (int i = 0;i < list.size();i++){
            	ComAdCMenuType temp = new ComAdCMenuType();
            	Object[] data = (Object[])list.get(i);
            	// 菜谱类别表.类别编码
            	if (null != data[0]){
            		temp.setStrMenuTypeCode(data[0].toString());
            	}
            	// 菜谱类别表.类别名称
            	if (null != data[1]){
            		temp.setStrMenuTypeName(data[1].toString());
            	}
            	lstResult.add(temp);
            }

            result.setList(lstResult);
            result.setTotalCount((long)lstResult.size());
	        LogUtil.log("EJB:取得所有菜谱类别结束。", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:取得所有菜谱类别失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	
	/**
	 * 取得抄送人员的code值和姓名
	 * 
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getCCUserIDAndName(String strEnterPriseCode) throws SQLException {
		try {
			PageObject result = new PageObject();
			String strSql = "";
			
			strSql = "SELECT "
				+ "HR_J_EMP_INFO.EMP_CODE,  "
				+ "HR_J_EMP_INFO.CHS_NAME  "
				+ "FROM "
				+ "HR_J_EMP_INFO "
				+ "WHERE "
				+ "HR_J_EMP_INFO.IS_USE = ? "
				+ "AND HR_J_EMP_INFO.ENTERPRISE_CODE = ? ";
			
			// 查询参数数组
			Object[] params = new Object[2];
			params[0] = USE_FLG;
			params[1] = strEnterPriseCode;
			
	        LogUtil.log("EJB:取得抄送人员的code值和姓名开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);

            List list=bll.queryByNativeSQL(strSql, params);
            List<WorkerEmployeeInform> lstResult = new ArrayList<WorkerEmployeeInform>();
            for (int i = 0;i < list.size();i++){
            	WorkerEmployeeInform temp = new WorkerEmployeeInform();
            	Object[] data = (Object[])list.get(i);
            	// 人员基本信息表.人员编码
            	if (null != data[0]){
            		temp.setWorkerCode(data[0].toString());
            	}
            	// 人员编码表.姓名
            	if (null != data[1]){
            		temp.setName(data[1].toString());
            	}
            	lstResult.add(temp);
            }
            result.setList(lstResult);
            result.setTotalCount((long)lstResult.size());
	        LogUtil.log("EJB:取得抄送人员的code值和姓名结束。", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:取得抄送人员的code值和姓名失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}
}
