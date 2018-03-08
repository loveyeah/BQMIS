/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.web.hr.ca.attendance.personattendancerights.action;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.HrCAttendancedep;
import power.ejb.hr.ca.HrCAttendancedepFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 个人考勤权限设置
 * 
 * @author chen shoujiang
 * 
 */
public class PersonAttendanceRightsAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	 /** 查询结果为null,传到前台的string */
    private static String STR_JSON_NULL = "{\"list\":[],\"totalCount\":0}";
	/**考勤登记人*/
	private String attendWriterId;
	/**考勤审核人*/
	private String attendCheckerId;
	private String id;
	/** 考勤部门维护remote */
	private HrCAttendancedepFacadeRemote hrcAttendancedepFacadeRemote;

	/**
	 * 构造函数
	 */
	public PersonAttendanceRightsAction() {
		// 考勤部门维护remote
		hrcAttendancedepFacadeRemote = (HrCAttendancedepFacadeRemote) factory
				.getFacadeRemote("HrCAttendancedepFacade");
	}

	/**
	 * 查询考勤登记人或考勤审核人是当前用户的数据
	 * 
	 * @throws JSONException 格式化
	 * @throws Exception 异常
	 */
	@SuppressWarnings("unchecked")
	public void getDataByWriterCheckerId() throws JSONException, Exception {
		try {
			LogUtil.log("Action:查询考勤登记人或考勤审核人是当前用户的数据开始", Level.INFO, null);
			// 分页信息
			PageObject obj = new PageObject();
			// 无分页信息时执行
			if (employee.getWorkerId() != null
					&& !employee.getWorkerId().equals(Constants.BLANK_STRING)) {
				obj = hrcAttendancedepFacadeRemote.findDataByWriterCheckerId(
						employee.getWorkerId(), employee.getEnterpriseCode());
			}
			// 输出
			String strOutput = Constants.BLANK_STRING;
			// 要是查询结果不为空的话，就赋值
			if (obj != null && obj.getList() != null
					&& obj.getList().size() > 0) {
				List<HrCAttendancedep> list = obj.getList();
				for(int j = 0 ; j < list.size(); j++){
					list.get(j).setLastModifiyBy(employee.getWorkerId().toString());
				}
				obj.setList(list);
				strOutput = JSONUtil.serialize(obj);
			} else {
				// 否则设为空值
				strOutput = STR_JSON_NULL;
			}
			write(strOutput);
			LogUtil.log("Action:查询考勤登记人或考勤审核人是当前用户的数据结束", Level.INFO, null);
		} catch (JSONException jsone) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询考勤登记人或考勤审核人是当前用户的数据失败", Level.SEVERE, null);
		}
	}

	
	/**
	 *修改考勤部门里考勤登记人和考勤审核人
	 * @throws DataFormatException 数据格式化异常
	 */
	public void modifyWriterChecker() {
		LogUtil.log("Action:修改考勤部门里考勤登记人和考勤审核人开始", Level.INFO, null);
		try{
			// 创建一个
			HrCAttendancedep entity = new HrCAttendancedep();
			// 通过id查找bean信息
			entity = hrcAttendancedepFacadeRemote.findById(Long.parseLong(id));
			// 考勤登记人
			if(checkNull(attendWriterId))
				entity.setAttendWriterId(Long.parseLong(attendWriterId));			
				// 考勤审核人
			if(checkNull(attendCheckerId))
				entity.setAttendCheckerId(Long.parseLong(attendCheckerId));
			// 设置最后修改人
			entity.setLastModifiyBy(employee.getWorkerCode());
			// 执行保存操作
			hrcAttendancedepFacadeRemote.update(entity);
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:修改考勤部门里考勤登记人和考勤审核人结束", Level.INFO, null); 
		} catch (SQLException jsone) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:修改考勤部门里考勤登记人和考勤审核人失败", Level.SEVERE, null);
		}
	}
	
	/**
	 * check字符串是否为空或者null
	 */
	private Boolean checkNull(String chaStr) {
		if(chaStr != null && !chaStr.equals(Constants.BLANK_STRING))
			return true;
		else 
			return false;
	}

	/**
	 * @return the attendWriterId
	 */
	public String getAttendWriterId() {
		return attendWriterId;
	}

	/**
	 * @param attendWriterId the attendWriterId to set
	 */
	public void setAttendWriterId(String attendWriterId) {
		this.attendWriterId = attendWriterId;
	}

	/**
	 * @return the attendCheckerId
	 */
	public String getAttendCheckerId() {
		return attendCheckerId;
	}

	/**
	 * @param attendCheckerId the attendCheckerId to set
	 */
	public void setAttendCheckerId(String attendCheckerId) {
		this.attendCheckerId = attendCheckerId;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
}
