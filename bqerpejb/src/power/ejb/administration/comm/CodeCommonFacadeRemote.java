package power.ejb.administration.comm;

import java.sql.SQLException;

import javax.ejb.Remote;

/**编码生成处理
 * @author 谭晓苏
 */
@Remote
public interface CodeCommonFacadeRemote {
	
	/**
	 * 工作项目编码生成
	 * 定期工作画面向DB保存时，工作项目编码生成
	 * 
	 * @return String 生成的工作项目编码
	 * 
	 */
	public String getWorkItemCode() throws SQLException ;
	
	/**
	 * 菜谱编码生成
	 * 菜谱维护画面向DB保存时，菜谱编码生成
	 * 
	 * @return String 生成的菜谱编码
	 * @throws SQLException  
	 */
	public String getCMenuCode() throws SQLException ;
	
	/**
	 * 子工作类别编码生成
	 * 定期工作类别维护画面面向DB保存时，子工作类别编码生成
	 * 
	 * @param String workTypeCode 工作类别编码
	 * 
	 * @return String 生成的子工作类别编码
	 * @throws SQLException  
	 */
	public String getSubWorkTypeCode(String workTypeCode) throws SQLException ;
	
	/**
	 * 值别编码生成
	 * 值别维护画面向DB保存时，值别编码生成。
	 * 
	 * @return String 生成的值别编码
	 * @throws SQLException  
	 * 
	 */
	public String getDutyCode() throws SQLException ;
	
	/**
	 * 车辆维修单位编码生成
	 * 车辆维修单位维护画面向DB保存时，单位编码生成
	 * 
	 * @return String 生成的车辆维修单位编码
	 * @throws SQLException  
	 * 
	 */
	public String getCarWHCode() throws SQLException ;
	
	/**
	 * 会议申请单号编码生成
	 * 会议申请上报画面向DB保存时，会议申请单号编码生成
	 * 
	 * @param String applyUserID 申请人ID
	 * 
	 * @return String 生成的会议申请单号编码
	 * @throws SQLException  
	 */
	public String getMeetAplNoCode(String applyUserID) throws SQLException ;
	
	/**
	 * 接待申请单号编码生成
	 * 接待申请上报画面向DB保存时，接待申请单号编码生成
	 * 
	 * @param String applyUserID 申请人ID
	 * 
	 * @return String 生成的接待申请单号编码
	 * @throws SQLException  
	 */
	public String getReciveAplNoCode(String applyUserID) throws SQLException ;

	/**
	 * 签报申请单号生成
	 * 
	 * @param String appType 申请类别
	 *                内部申请 I , 董事会申请D
	 * @param String userID 登录用户ID
	 * 
	 * @return String 生成的签报申请单号
	 * @throws SQLException  
	 */
	public String getReportAppNoCode(String appType, String userID) throws SQLException ;

	/**
	 * 签报编号编码生成
	 * 
	 * @param String appType 申请类别
	 *                内部申请 I , 董事会申请D
	 * @param String appUserDeptCode 申请人部门编码
	 * 
	 * @return String 生成的签报编号编码
	 * @throws SQLException  
	 */
	public String getReportNoCode(String appType, String appUserDeptCode) throws SQLException ;

	/**
	 * 车辆维修申请单号生成
	 * 
	 * @param String userID 登录用户ID
	 * 
	 * @return String 生成的车辆维修申请单号
	 * @throws SQLException  
	 */
	public String getCarMaintenanceAppNo(String userID) throws SQLException ;

	/**
	 * 车辆申请单号生成
	 * 
	 * @param String userID 登录用户ID
	 * 
	 * @return String 生成的车辆申请单号
	 * @throws SQLException  
	 */
	public String getCarAppNo(String userID) throws SQLException ;
	
	/**
	 * 维修项目编码生成
	 *  
	 * @param String strFeeType 费用类别编码
	 * 
	 * @return String 生成的维修项目编码
	 * @throws SQLException
	 */
	public String getMaintenanceItemCode(String strFeeType) throws SQLException ;
}
