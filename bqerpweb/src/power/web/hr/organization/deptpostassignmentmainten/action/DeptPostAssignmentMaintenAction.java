/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.organization.deptpostassignmentmainten.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.DataChangeException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.DeptPostAssignmentMaintenFacadeRemote;
import power.ejb.hr.HrJDepstationcorrespond;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 部门岗位对应维护Action
 * 
 * @author fangjihu
 * 
 */
@SuppressWarnings("serial")
public class DeptPostAssignmentMaintenAction extends AbstractAction{
	/** 部门Id */
	private Long deptId;
	/** 更新DB的数据 */
	private String data;
	/** 常量定义 */
	private static final String TWO = "2";
	private static final String ONE = "1";
	private static final String ZERO = "0";
	private static final String SAVE_SUCCESS = "{success:true,msg:'&nbsp&nbsp&nbsp保存成功！&nbsp&nbsp&nbsp'}";
	private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd HH:mm:ss";
	
	/** 远程接口 */
	protected DeptPostAssignmentMaintenFacadeRemote remote;
	public DeptPostAssignmentMaintenAction(){
		remote =(DeptPostAssignmentMaintenFacadeRemote)factory.getFacadeRemote("DeptPostAssignmentMaintenFacade");
		
	}
	/**
	 * 取得未分配的岗位信息
	 */
	public void getUnAssignMentPostList(){
		try{
			LogUtil.log("Action:取得未分配的岗位信息开始。", Level.INFO, null);
			PageObject obj = remote.findUnAssignMentPostList(employee.getEnterpriseCode(), this.getDeptId());
			write(JSONUtil.serialize(obj));
			LogUtil.log("Action:取得未分配的岗位信息开始结束。", Level.INFO, null);
			}catch (JSONException jsone) {
				LogUtil.log("Action:取得未分配的岗位信息开始失败。", Level.SEVERE, jsone);
				write(Constants.DATA_FAILURE);
			} catch (SQLException sqle) {
				LogUtil.log("Action:取得未分配的岗位信息开始失败。", Level.SEVERE, sqle);
				write(Constants.SQL_FAILURE);
			}
	}
	
	/**
	 * 取得已分配的岗位信息
	 */
	public void getAssignMentPostList(){
		try{
			LogUtil.log("Action:取得已分配的岗位信息开始。", Level.INFO, null);
			//modify by ming_lian 2010-08-11
			String stationName=request.getParameter("stationName")==null?"":request.getParameter("stationName");
			PageObject obj = remote.findAssignMentPostList(employee.getEnterpriseCode(), this.getDeptId(),stationName);
			write(JSONUtil.serialize(obj));
			LogUtil.log("Action:取得已分配的岗位信息开始结束。", Level.INFO, null);
			}catch (JSONException jsone) {
				LogUtil.log("Action:取得已分配的岗位信息开始失败。", Level.SEVERE, jsone);
				write(Constants.DATA_FAILURE);
			} catch (SQLException sqle) {
				LogUtil.log("Action:取得已分配的岗位信息开始失败。", Level.SEVERE, sqle);
				write(Constants.SQL_FAILURE);
			}
	}
	/**
	 * 保存DB操作
	 * @throws JSONException 
	 * @throws DataFormatException 
	 */
	@SuppressWarnings("unchecked")
	public void saveAssignmentStation() throws JSONException, DataFormatException{
		try{
		LogUtil.log("Action:处理保存操作开始。", Level.INFO, null);
		List<Map> dataMapList = (List<Map>) JSONUtil
		.deserialize(this.getData());
		updateDBOperate(dataMapList);
		write(SAVE_SUCCESS);
		LogUtil.log("Action:处理保存操作结束。", Level.INFO, null);
	} catch (DataChangeException e) {
		// 排他操作
		LogUtil.log("Action:保存DB操作失败", Level.SEVERE, e);
		write(Constants.DATA_USING);
	} catch (SQLException e) {
		LogUtil.log("Action:保存DB操作失败", Level.SEVERE, e);
		write(Constants.SQL_FAILURE);
	} catch (DataFormatException e) {
		LogUtil.log("Action:保存DB操作失败", Level.SEVERE, e);
		write(Constants.DATA_FAILURE);
	} catch (JSONException e) {
		LogUtil.log("Action:保存DB操作失败", Level.SEVERE, e);
		write(Constants.DATA_FAILURE);
	}catch (CodeRepeatException e) {
		LogUtil.log("Action:保存DB操作失败", Level.SEVERE, e);
		write(Constants.CODE_REPEAT);
	} catch (Exception e) {
		LogUtil.log("Action:保存DB操作失败", Level.SEVERE, e);
		write(Constants.SQL_FAILURE);
	}
	}
	/**
	 * db操作
	 * @param dataMapList
	 * @throws DataFormatException 
	 * @throws SQLException 
	 * @throws DataChangeException 
	 * @throws CodeRepeatException 
	 */
	@SuppressWarnings("unchecked")
	private void updateDBOperate(List<Map> dataMapList) throws DataFormatException, DataChangeException, SQLException, CodeRepeatException{
		Map map ;
		List<HrJDepstationcorrespond> updateDepstationcorrespondList = new ArrayList<HrJDepstationcorrespond>();
		List<HrJDepstationcorrespond> deleteDepstationcorrespondList = new ArrayList<HrJDepstationcorrespond>();
		List<HrJDepstationcorrespond> saveDepstationcorrespondList = new ArrayList<HrJDepstationcorrespond>();
		String flag="";
		for(int i =0;i<dataMapList.size();i++){
			map = dataMapList.get(i);
			flag = map.get("flag").toString();
			// 新规记录
			if(ONE.equals(flag)){
				saveDepstationcorrespondList.add(saveDepstationcorrespondBean(map));
			}else if(TWO.equals(flag)||ZERO.equals(flag)){
			// 更新记录	
				updateDepstationcorrespondList.add(updateDepstationcorrespondBean(map));
			}else{
			// 删除记录	
				deleteDepstationcorrespondList.add(updateDepstationcorrespondBean(map));
			}
		}
		remote.updateDBOperation(updateDepstationcorrespondList, deleteDepstationcorrespondList, saveDepstationcorrespondList);
	
	}
	
	/**
	 * 新规用的bean设值
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private HrJDepstationcorrespond saveDepstationcorrespondBean(Map map){
		HrJDepstationcorrespond bean = new HrJDepstationcorrespond();
		// 部门ID
		bean.setDeptId(Long.parseLong(map.get("deptId").toString()));
		// 标准人数
		bean.setStandardPersonNum(Long.parseLong(map.get("standardPersonNum").toString()));
		// 记录日期
		bean.setInsertdate(new Date());
		// 记录人
		 bean.setInsertby(employee.getWorkerCode());
		// 企业编码
		bean.setEnterpriseCode(employee.getEnterpriseCode());
		// 是否使用
		bean.setIsUse(Constants.IS_USE_Y);
		// 上次修改人
		bean.setLastModifiedBy(employee.getWorkerCode());
		// 上次修改日期
		bean.setLastModifiedDate(new Date());
		// 岗位ID
		bean.setStationId(Long.parseLong(map.get("stationId").toString()));
		// 是否领导岗位
		bean.setIsLead(map.get("isLead").toString());
		return bean;
	}
	
	@SuppressWarnings("unchecked")
	private HrJDepstationcorrespond updateDepstationcorrespondBean(Map map) throws DataFormatException{
		HrJDepstationcorrespond bean = new HrJDepstationcorrespond();
		bean.setDepstationcorrespondid(Long.parseLong(map.get("depstationcorrespondid").toString()));
		bean.setLastModifiedBy(employee.getWorkerCode());
		// 标准人数
		bean.setStandardPersonNum(Long.parseLong(map.get("standardPersonNum").toString()));
		// 是否领导岗位
		bean.setIsLead(map.get("isLead").toString());
		// 上次修改日期
		String updateTime = map.get("lastModifiedDate").toString();
		bean.setLastModifiedDate(formatStringToDate(updateTime, DATE_FORMAT_YYYYMMDD));
		return bean;
		
	}
	/**
	 * 根据日期日期字符串和形式返回日期
	 * 
	 * @param argDateStr
	 *            日期字符串
	 * @param argFormat
	 *            日期形式字符串
	 * @return 日期
	 */
	private Date formatStringToDate(String argDateStr, String argFormat)
			throws DataFormatException {
		if (argDateStr == null || argDateStr.trim().length() < 1) {
			return null;
		}
		try {
		SimpleDateFormat sdfFrom = new SimpleDateFormat(argFormat);
		// 格式化日期
		Date dtresult = sdfFrom.parse(argDateStr);
		return dtresult;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 部门id取得
	 * @return
	 */
	public Long getDeptId() {
		return deptId;
	}
	/**
	 * 部门id设值
	 * @param deptId
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	
}
