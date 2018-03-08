/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.dutyjournal.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.DataChangeException;
import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJDutyman;
import power.ejb.administration.AdJDutymanFacadeRemote;
import power.ejb.administration.AdJOnduty;
import power.ejb.administration.AdJOndutyFacadeRemote;
import power.ejb.administration.comm.ADCommonFacadeRemote;
import power.ejb.administration.comm.ComAdCRight;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 定期工作管理Action
 * 
 * @author sufeiyu
 * 
 */
@SuppressWarnings("serial")
public class DutyJournalAction extends AbstractAction {
	/** 获得当前用户ID* */
	private Employee employee;
	/** 获得值班日志entity* */
	AdJOnduty onduty;
	/** 获得值班人员entity* */
	AdJDutyman dutyman;
	/** 值班日志实现接口* */
	AdJOndutyFacadeRemote dremote;
	/** 值班人员实现接口* */
	AdJDutymanFacadeRemote premote;
	/** 用户权限取得处理远程对象 */
	private ADCommonFacadeRemote adcremote;
	/** 上次修改时间* */
	public String strUpdateTime;
	/** 值班记事ID* */
	public Long lngNoteId;
	/** 值班人员ID* */
	public Long lngPersonId;
	/** 开始页* */
	public Long start;
	/** 页容量* */
	public Long limit;
	/** 登记时间* */
	public String strRegTime;
	/** 值班时间* */
	public String strDutytime;

	public DutyJournalAction() {
		dremote = (AdJOndutyFacadeRemote) factory
				.getFacadeRemote("AdJOndutyFacade");
		premote = (AdJDutymanFacadeRemote) factory
				.getFacadeRemote("AdJDutymanFacade");
		adcremote = (ADCommonFacadeRemote) factory
				.getFacadeRemote("ADCommonFacade");
	}

	/**
	 * 取得记事数据
	 * 
	 * @throws JSONException
	 */
	public void getRecord() throws JSONException {
		LogUtil.log("Action：取得记事数据开始", Level.INFO, null);

		try {
			employee = (Employee) session.getAttribute("employee");
			String strEnterpriseCode = employee.getEnterpriseCode();
			// 取得工作类别
			String strUserRight = "";
			PageObject pobjUserRight = null;
			try {
				pobjUserRight = adcremote.getUserRight(employee
						.getWorkerCode(), strEnterpriseCode);
			} catch (SQLException e) {
				write(Constants.SQL_FAILURE);
			}
			if (pobjUserRight != null) {
			if (pobjUserRight.getList().size() > 0) {
				ComAdCRight objTemp = (ComAdCRight) pobjUserRight.getList().get(
						0);
				strUserRight = objTemp.getWorktypeCode();
			}}

			// 传给前台的字符串
			String strRecord = "";
			PageObject objResult = new PageObject();

			// 分页显示
			int intStart = Integer.parseInt(start.toString());
			int intLimit = Integer.parseInt(limit.toString());
			objResult = dremote.getRecord(strEnterpriseCode, strUserRight, intStart, intLimit);

			if (objResult.getTotalCount() > 0) {
				strRecord = JSONUtil.serialize(objResult);
			} else {
				strRecord = "{\"list\":[],\"totalCount\":null}";
			}
			write(strRecord);
			LogUtil.log("Action：取得记事数据正常结束", Level.INFO, null);
		} catch (NumberFormatException e) {
			LogUtil.log("Action：取得记事数据异常结束", Level.SEVERE, e);
		}

	}

	/**
	 * 删除一条记事数据
	 */
	public void deleteRecord() {
		LogUtil.log("Action：删除一条记事数据开始", Level.INFO, null);

		try {
			employee = (Employee) session.getAttribute("employee");
			String strEmployee = employee.getWorkerCode();

			// 删除记事信息
			try {
				try {
					dremote.delete(strEmployee, lngNoteId, strUpdateTime);
				} catch (SQLException e) {
					write(Constants.SQL_FAILURE);
				}
			} catch (DataChangeException e) {
				write(Constants.DATA_USING);
			}
			// 显示删除成功信息
			write(Constants.DELETE_SUCCESS);

			LogUtil.log("Action：删除一条记事数据正常结束", Level.INFO, null);
		} catch (RuntimeException e) {
			LogUtil.log("Action：删除一条记事数据异常结束", Level.SEVERE, e);
		}
	}

	/**
	 * 增加一条记事记录
	 */
	public void addRecord() {
		LogUtil.log("Action：增加一条记事记录开始", Level.INFO, null);

		
			try {
				employee = (Employee) session.getAttribute("employee");
				String strEmployee = employee.getWorkerCode();
				String strEnterpriseCode = employee.getEnterpriseCode();
				/** 取得工作类别* */
				String strUserRight = "";
				PageObject pobjUserRight = null;
				pobjUserRight = adcremote.getUserRight(strEmployee, employee.getEnterpriseCode());
				if (pobjUserRight != null) {
				if (pobjUserRight.getList().size() > 0) {
					ComAdCRight objTemp = (ComAdCRight) pobjUserRight.getList().get(
							0);
					strUserRight = objTemp.getWorktypeCode();
				}}
				
				// 登记人
				onduty.setCrtUser(strEmployee);
				// 更新人
				onduty.setUpdateUser(strEmployee);
				// 是否使用
				onduty.setIsUse("Y");
				// 登记时间
				if (strRegTime != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date dteRegTime = sdf.parse(strRegTime);
					onduty.setRegTime(dteRegTime);
				}
				// ID
				onduty.setId(null);
				// 工作类别
				onduty.setWorktypeCode(strUserRight);
				// 企业代码
				onduty.setEnterpriseCode(strEnterpriseCode);
				dremote.save(onduty);
				// 显示成功信息
				write(Constants.ADD_SUCCESS);
				LogUtil.log("Action：增加一条记事记录正常结束", Level.INFO, null);
			} catch (SQLException e) {
				LogUtil.log("Action：增加一条记事记录异常结束", Level.SEVERE, e);
				write(Constants.SQL_FAILURE);
			} catch (ParseException e) {
				LogUtil.log("Action：增加一条记事记录异常结束", Level.SEVERE, e);
				write(Constants.DATA_FAILURE);
			}
	}

	/**
	 * 修改一条记事记录
	 */
	public void updateRecord() {
		LogUtil.log("Action：修改一条记事记录开始", Level.INFO, null);

		try {
				employee = (Employee) session.getAttribute("employee");
				String strEmployee = employee.getWorkerCode();
				String strEnterpriseCode = employee.getEnterpriseCode();
				
				AdJOnduty objOld = new AdJOnduty();
				objOld = dremote.findById(onduty.getId());
				
				// 登记人
				onduty.setCrtUser(objOld.getCrtUser());
				// 更新人
				onduty.setUpdateUser(strEmployee);
				// 是否使用
				onduty.setIsUse("Y");
				// 登记时间
				if (strRegTime != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date dteRegTime = sdf.parse(strRegTime);
					onduty.setRegTime(dteRegTime);
				}
				// 企业代码
				onduty.setEnterpriseCode(strEnterpriseCode);
				// 更新
				dremote.update(onduty, strUpdateTime);
				// 显示成功信息
				write(Constants.ADD_SUCCESS);
				
				LogUtil.log("Action：修改一条记事记录正常结束", Level.INFO, null);
			} catch (ParseException e) {
				LogUtil.log("Action：修改一条记事记录异常结束", Level.SEVERE, e);
				write(Constants.DATA_FAILURE);
			} catch (DataChangeException e) {
				LogUtil.log("Action：修改一条记事记录异常结束", Level.SEVERE, e);
				write(Constants.DATA_USING);
			} catch (SQLException e) {
				LogUtil.log("Action：修改一条记事记录异常结束", Level.SEVERE, e);
				write(Constants.SQL_FAILURE);
			}
	}

	/**
	 * 取得人员数据
	 * 
	 * @throws JSONException
	 */
	public void getPerson() throws JSONException {
		LogUtil.log("Action：取得人员数据开始", Level.INFO, null);

		try {
			employee = (Employee) session.getAttribute("employee");
			String strEnterpriseCode = employee.getEnterpriseCode();
			// 取得工作类别
			String strUserRight = "";
			PageObject pobjUserRight = null;
			try {
				pobjUserRight = adcremote.getUserRight(employee
						.getWorkerCode(), employee.getEnterpriseCode());
			} catch (SQLException e) {
				write(Constants.SQL_FAILURE);
			}
			if (pobjUserRight != null) {
			if (pobjUserRight.getList().size() > 0) {
				ComAdCRight objTemp = (ComAdCRight) pobjUserRight.getList().get(
						0);
				strUserRight = objTemp.getWorktypeCode();
			}}
			// 传给前台的字符串
			String strRecord = "";

			PageObject objResult = new PageObject();

			// 分页显示

			int intStart = Integer.parseInt(start.toString());
			int intLimit = Integer.parseInt(limit.toString());
			objResult = premote.getPerson(strEnterpriseCode, strUserRight, intStart, intLimit);

			if (objResult.getTotalCount() > 0) {
				strRecord = JSONUtil.serialize(objResult);
			} else {
				strRecord = "{\"list\":[],\"totalCount\":null}";
			}
			write(strRecord);
			LogUtil.log("Action：取得人员数据正常结束", Level.INFO, null);
		} catch (NumberFormatException e) {
			LogUtil.log("Action：取得人员数据异常结束", Level.SEVERE, e);
		}
	}

	/**
	 * 增加一条人员记录
	 */
	public void addPersons() {
		LogUtil.log("Action：增加一条人员记录开始", Level.INFO, null);

		    try {
				employee = (Employee) session.getAttribute("employee");
				String strEmployee = employee.getWorkerCode();
				String strEnterpriseCode = employee.getEnterpriseCode();
				/** 取得工作类别* */
				String strUserRight = "";
				PageObject pobjUserRight = null;
				pobjUserRight = adcremote.getUserRight(strEmployee, strEnterpriseCode);
				if (pobjUserRight != null) {
					if (pobjUserRight.getList().size() > 0) {
						ComAdCRight objTemp = (ComAdCRight) pobjUserRight.getList()
								.get(0);
						strUserRight = objTemp.getWorktypeCode();
					}}
				
				// 登记人
				dutyman.setCrtUser(strEmployee);
				// 更新人
				dutyman.setUpdateUser(strEmployee);
				// 是否使用
				dutyman.setIsUse("Y");
				// 值班时间
				if (strDutytime != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date dteDutytime = sdf.parse(strDutytime);
					dutyman.setDutytime(dteDutytime);
				}
				// ID
				dutyman.setId(null);
				// 工作类别
				dutyman.setWorktypeCode(strUserRight);
				// 企业代码
				dutyman.setEnterpriseCode(strEnterpriseCode);
				try {
					premote.save(dutyman);
				} catch (SQLException e) {
					write(Constants.SQL_FAILURE);
				}
				// 显示成功信息
				write(Constants.ADD_SUCCESS);
				LogUtil.log("Action：增加一条人员记录正常结束", Level.INFO, null);
			} catch (SQLException e) {
				LogUtil.log("Action：增加一条人员记录异常结束", Level.SEVERE, e);
				write(Constants.SQL_FAILURE);
			} catch (ParseException e) {
				LogUtil.log("Action：增加一条人员记录异常结束", Level.SEVERE, e);
				write(Constants.DATA_FAILURE);
			}
	}

	/**
	 * 删除一条人员数据
	 */
	public void deletePerson() {
		LogUtil.log("Action：删除一条人员数据开始", Level.INFO, null);

		try {
			employee = (Employee) session.getAttribute("employee");
			String strEmployee = employee.getWorkerCode();

			// 删除记事信息
			try {
				try {
					premote.delete(strEmployee, lngPersonId, strUpdateTime);
				} catch (SQLException e) {
					write(Constants.SQL_FAILURE);
				}
			} catch (DataChangeException e) {
				write(Constants.DATA_USING);
			}
			// 显示删除成功信息
			write(Constants.DELETE_SUCCESS);

			LogUtil.log("Action：删除一条人员数据正常结束", Level.INFO, null);
		} catch (RuntimeException e) {
			LogUtil.log("Action：删除一条人员数据异常结束", Level.SEVERE, e);
		}
	}

	/**
	 * 修改一条人员记录
	 */
	public void updatePerson() {
		LogUtil.log("Action：修改一条人员记录开始", Level.INFO, null);

		
			try {
				employee = (Employee) session.getAttribute("employee");
				String strEmployee = employee.getWorkerCode();
				String strEnterpriseCode = employee.getEnterpriseCode();
				
				AdJDutyman objOld = new AdJDutyman();
				objOld = premote.findById(dutyman.getId());

				// 登记人
				dutyman.setCrtUser(objOld.getCrtUser());
				// 更新人
				dutyman.setUpdateUser(strEmployee);
				// 是否使用
				dutyman.setIsUse("Y");
				// 值班时间
				if (strDutytime != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date dteDutytime = sdf.parse(strDutytime);
					dutyman.setDutytime(dteDutytime);
				}
				// 企业代码
				dutyman.setEnterpriseCode(strEnterpriseCode);
				// 更新
				premote.update(dutyman, strUpdateTime);
				// 显示成功信息
				write(Constants.ADD_SUCCESS);

				LogUtil.log("Action：修改一条人员记录正常结束", Level.INFO, null);
			} catch (ParseException e) {
				LogUtil.log("Action：修改一条人员记录异常结束", Level.SEVERE, e);
				write(Constants.DATA_FAILURE);
			} catch (DataChangeException e) {
				LogUtil.log("Action：修改一条人员记录异常结束", Level.SEVERE, e);
				write(Constants.DATA_USING);
			} catch (SQLException e) {
				LogUtil.log("Action：修改一条人员记录异常结束", Level.SEVERE, e);
				write(Constants.SQL_FAILURE);
			}
	}

	

	/**
	 * @return 值班日志entity
	 */
	public AdJOnduty getOnduty() {
		return onduty;
	}

	/**
	 * @param 值班日志entity
	 */
	public void setOnduty(AdJOnduty onduty) {
		this.onduty = onduty;
	}

	/**
	 * @return 值班人员entity
	 */
	public AdJDutyman getDutyman() {
		return dutyman;
	}

	/**
	 * @param 值班人员entity
	 */
	public void setDutyman(AdJDutyman dutyman) {
		this.dutyman = dutyman;
	}

	/**
	 * @return 上次修改时间
	 */
	public String getStrUpdateTime() {
		return strUpdateTime;
	}

	/**
	 * @param 上次修改时间
	 */
	public void setStrUpdateTime(String strUpdateTime) {
		this.strUpdateTime = strUpdateTime;
	}

	/**
	 * @return 值班记事ID
	 */
	public Long getLngNoteId() {
		return lngNoteId;
	}

	/**
	 * @param 值班记事ID
	 */
	public void setLngNoteId(Long lngNoteId) {
		this.lngNoteId = lngNoteId;
	}

	/**
	 * @return 值班人员ID
	 */
	public Long getLngPersonId() {
		return lngPersonId;
	}

	/**
	 * @param 值班人员ID
	 */
	public void setLngPersonId(Long lngPersonId) {
		this.lngPersonId = lngPersonId;
	}

	/**
	 * @return 开始页
	 */
	public Long getStart() {
		return start;
	}

	/**
	 * @param 开始页
	 */
	public void setStart(Long start) {
		this.start = start;
	}

	/**
	 * @return 页容量
	 */
	public Long getLimit() {
		return limit;
	}

	/**
	 * @param 页容量
	 */
	public void setLimit(Long limit) {
		this.limit = limit;
	}

	/**
	 * @return 登记时间
	 */
	public String getStrRegTime() {
		return strRegTime;
	}

	/**
	 * @param 登记时间
	 */
	public void setStrRegTime(String strRegTime) {
		this.strRegTime = strRegTime;
	}

	/**
	 * @return 值班时间
	 */
	public String getStrDutytime() {
		return strDutytime;
	}

	/**
	 * @param 值班时间
	 */
	public void setStrDutytime(String strDutytime) {
		this.strDutytime = strDutytime;
	}
}
