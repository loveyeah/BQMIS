package power.ejb.administration.comm;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote
public interface ADCommonFacadeRemote {
	
	/**
	 * 根据当前用户的工作类别编码取得对应的所有值别。
	 * 
	 * @param String strWorkTypeCode 当前用户的工作类别编码
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	public PageObject getDutyTypeInfoList(String strWorkTypeCode, String strEnterPriseCode) throws SQLException;
	
	/**
	 * 证件类别编码表数据检索
	 * 
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	public PageObject getPaperInfoList(String strEnterPriseCode) throws SQLException;	
	
	/**
	 * 根据类别编码，检索此类别下的所有子类别编码和子类别名称
	 * 
	 * @param String strWorkTypeCode 类别编码
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	public PageObject getWorkTypeInfoList(String strWorkTypeCode, String strEnterPriseCode) throws SQLException;

	/**
	 * 检索车辆档案表
	 * 
	 * @param int rowStartIdxAndCount 分页
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	public PageObject getCarInfoList(String strEnterPriseCode, final int... rowStartIdxAndCount) throws SQLException;
	
	/**
	 * 根据司机档案，检索出司机编码和姓名
	 * 
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	public PageObject getDriverInfoList(String strEnterPriseCode) throws SQLException;
	
	/**
	 * 根据部门编码，检索出该部门内的人员
	 * 
	 * @param String strDeptCode 部门编码
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	public PageObject getWorkerByDept(String strDeptCode, String strEnterPriseCode) throws SQLException;
	
	/**
	 * 根据用户ID，在权限设置表里取得用户对应的权限设置。
	 * 
	 * @param String strUserID 人员编码
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	public PageObject getUserRight(String strUserID, String strEnterPriseCode) throws SQLException;
	
	/**
	 * 检索所有的计量单位名称和计量单位标志
	 * 
	 * @throws SQLException
	 */
	public PageObject getUnit() throws SQLException;
	
	/**
	 * 检索所有菜谱类别
	 * 
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	public PageObject getAllCMenuType(String strEnterPriseCode) throws SQLException;
	
	/**
	 * 取得抄送人员的code值和姓名
	 * 
	 * @param String strEnterPriseCode 企业代码
	 * @throws SQLException
	 */
	public PageObject getCCUserIDAndName(String strEnterPriseCode) throws SQLException;
}
