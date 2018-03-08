package power.web.administration.carusemanagement.action;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

//import bsh.ParseException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJApplycar;
import power.ejb.administration.AdJApplycarFacadeRemote;
import power.ejb.administration.AdJCarfile;
import power.ejb.administration.AdJCarfileFacadeRemote;
import power.ejb.administration.form.CarApplyInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

public class CarUseManagementAction extends AbstractAction {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 查询，保存，修改数据远程对象 */
	private AdJApplycarFacadeRemote carAppRemote;
	/** 车辆档案远程接口 */
	private AdJCarfileFacadeRemote carFileRemote;
	/** 车辆管理entity */
	private CarApplyInfo entity;
	/** 分页start */
	private int start;
	/** 分页limit */
	private int limit;
	/** 开始日期 */
	private String strStartDate;
	/** 结束日期 */
	private String strEndDate;
	/** 申请人编码 */
	private String strWorkerCode;
	/** 用车部门编码 */
	private String strDepCode;
	/** 司机编码 */
	private String strDriverCode;
	/** 出车状态 */
	private String strdrpCarStatus;
	/** 用车申请表更新时间 */
	private Long updateTime;
	/** 用车申请表ID */
	private Long carAppIdValue;
	 
	/**
	 * @return the updateTime
	 */
	public Long getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the carAppIdValue
	 */
	public Long getCarAppIdValue() {
		return carAppIdValue;
	}

	/**
	 * @param carAppIdValue the carAppIdValue to set
	 */
	public void setCarAppIdValue(Long carAppIdValue) {
		this.carAppIdValue = carAppIdValue;
	}

	/**
	 * 构造函数
	 */
	public CarUseManagementAction() {
		carAppRemote = (AdJApplycarFacadeRemote) factory
				.getFacadeRemote("AdJApplycarFacade");
		carFileRemote = (AdJCarfileFacadeRemote) factory
				.getFacadeRemote("AdJCarfileFacade");
	}

	/**
	 * 查询车辆信息
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getCarApplyBy() throws JSONException {
		try {
			// 需要在画面上追加隐藏控件存放车辆档案的更新时间
			LogUtil.log("Action:查询车辆信息开始。", Level.INFO, null);
			String enterpriseCode = employee.getEnterpriseCode();
			PageObject obj = carAppRemote.findCarApplyBy(strStartDate,
					strEndDate, strDepCode, strWorkerCode, strDriverCode,
					strdrpCarStatus,enterpriseCode, start, limit);

			if (obj != null) {
				String retStr = JSONUtil.serialize(obj);
				write(retStr);
			} else {
				write("{\"list\":[],\"totalCount\":null}");
			}
			LogUtil.log("Action:查询车辆信息结束。", Level.INFO, null);
		} catch (SQLException e) {
			// 显示失败
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:查询车辆信息失败", Level.SEVERE, null);
		} catch (JSONException je) {
			// 显示失败
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询车辆信息失败", Level.SEVERE, null);
		}
	}

	/**
	 * 删除车辆申请
	 */
	public void delCarApp() {
		LogUtil.log("Action:删除车辆申请开始。", Level.INFO, null);
		try {
			// 取得登录用户ID
			String strUserID = employee.getWorkerCode();
			// 删除车辆申请
			carAppRemote.delete(carAppIdValue, strUserID, updateTime);
			LogUtil.log("Action:删除车辆申请结束。", Level.INFO, null);
			write(Constants.DELETE_SUCCESS);
		} catch (SQLException sqle) {
			LogUtil.log("Action:删除车辆申请失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		} catch (DataChangeException de) {
			LogUtil.log("Action:删除车辆申请失败。", Level.SEVERE, de);
			write(Constants.DATA_USING);
		}
	}

	/**
	 * 更新车辆申请和车辆档案
	 */
	public void updateCarAppCarFile() {
		LogUtil.log("Action:保存操作开始", Level.INFO, null);
		// 取得登录用户ID
		String strUserID = employee.getWorkerCode();
		// 时间格式转换
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 新建事务
			UserTransaction tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			try {
				tx.begin();
				// 更新车辆申请
				LogUtil.log("Action:更新车辆申请开始", Level.INFO, null);
				// 取得原数据
				AdJApplycar objCarApp = carAppRemote.findById(entity.getId());
				// 发车时间
				Date dateStartTime = new Date();
		        dateStartTime = format.parse(entity.getStartTime());
				objCarApp.setStartTime(dateStartTime);
				// 发车里程
				objCarApp.setGoMile(Double.parseDouble(entity.getGoMile()));
				// 司机
				objCarApp.setDriver(entity.getDriverCode());
				// 车牌号
				objCarApp.setCarNo(entity.getCarNo());
				// 收车时间
				if ((entity.getEndTime() == null)
						|| ("".equals(entity.getEndTime().toString().trim()))){
					objCarApp.setEndTime(null);
				}else{
					Date dateEndTime = new Date();
			        dateEndTime = format.parse(entity.getEndTime());	         
			        objCarApp.setEndTime(dateEndTime);
				}		
				// 收车里程
				if ((entity.getComeMile() == null)
						|| ("".equals(entity.getComeMile().toString().trim()))){
					objCarApp.setComeMile(null);
				}else{
					objCarApp.setComeMile(Double.parseDouble(entity.getComeMile()));
				}	
				
				// 油费
				if ((entity.getUseOil() == null)
						|| ("".equals(entity.getUseOil().toString().trim()))){
					objCarApp.setUseOil(null);
				}else{
					objCarApp.setUseOil(Double.parseDouble(entity.getUseOil()));
				}				
				// 路桥费
				if ((entity.getLqPay() == null)
						|| ("".equals(entity.getLqPay().toString().trim()))){
					objCarApp.setLqPay(null);
				}else{
					objCarApp.setLqPay(Double.parseDouble(entity.getLqPay()));
				}
				// 行车里程
				if ((entity.getDistance() == null)
						|| ("".equals(entity.getDistance().toString().trim()))){
					objCarApp.setDistance(null);
				}else{
					objCarApp.setDistance(Double.parseDouble(entity.getDistance()));
				}		
				// 更新者
				objCarApp.setUpdateUser(strUserID);
				// 更新车辆申请表
				carAppRemote.update(objCarApp, entity.getUpdateTime());
				LogUtil.log("Action:更新车辆申请结束", Level.INFO, null);

				// 更新车辆档案
				LogUtil.log("Action:更新车辆档案开始", Level.INFO, null);
				// 取得原数据
				AdJCarfile objCarFile = carFileRemote.findById(entity
						.getCarId());
				// 车辆档案.修改人 = 当前用户
				objCarFile.setUpdateUser(strUserID);
				// 设置更新值
				if ((entity.getEndTime() == null)
						|| ("".equals(entity.getEndTime().toString().trim()))) {
				// 如果画面.收车时间为空
				// 车辆档案.使用情况 = ‘Y’(使用中)
					objCarFile.setUseStatus("Y");
				} else {
					// 如果画面.收车时间不为空
					// 车辆档案.使用情况 = ‘N’（未使用）
					objCarFile.setUseStatus("N");
					objCarFile.setRunMile(Double.parseDouble(entity.getComeMile()));
				}
				carFileRemote.update(objCarFile, entity.getCarFileUpdateTime());
				// 提交事务
				tx.commit();
				write(Constants.MODIFY_SUCCESS);
				LogUtil.log("Action:保存操作结束", Level.INFO, null);
			} catch (SQLException sqle) {
				// 事务回滚
				tx.rollback();
				write(Constants.SQL_FAILURE);
				LogUtil.log("Action:保存操作失败", Level.SEVERE, sqle);
			} catch (DataChangeException de) {
				// 事务回滚
				tx.rollback();
				write(Constants.DATA_USING);
				LogUtil.log("Action:保存操作失败", Level.SEVERE, de);
			} catch (ParseException pe) {
				// 事务回滚
				tx.rollback();
				write(Constants.DATA_FAILURE);
				LogUtil.log("Action:保存操作失败", Level.SEVERE, pe);
			}
		} catch (Exception e) {
			LogUtil.log("Action:保存操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * @return 车辆管理entity
	 */
	public CarApplyInfo getEntity() {
		return entity;
	}

	/**
	 * @param 车辆管理entity
	 */
	public void setEntity(CarApplyInfo entity) {
		this.entity = entity;
	}

	/**
	 * @return 分页START
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param 分页START
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return 分页LIMIT
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param 分页LIMIT
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return 开始日期
	 */
	public String getStrStartDate() {
		return strStartDate;
	}

	/**
	 * @param 开始日期
	 */
	public void setStrStartDate(String strStartDate) {
		this.strStartDate = strStartDate;
	}

	/**
	 * @return 结束日期
	 */
	public String getStrEndDate() {
		return strEndDate;
	}

	/**
	 * @param 结束日期
	 */
	public void setStrEndDate(String strEndDate) {
		this.strEndDate = strEndDate;
	}

	/**
	 * @return 申请人编码
	 */
	public String getStrWorkerCode() {
		return strWorkerCode;
	}

	/**
	 * @param 申请人编码
	 */
	public void setStrWorkerCode(String strWorkerCode) {
		this.strWorkerCode = strWorkerCode;
	}

	/**
	 * @return 用车部门编码
	 */
	public String getStrDepCode() {
		return strDepCode;
	}

	/**
	 * @param 用车部门编码
	 */
	public void setStrDepCode(String strDepCode) {
		this.strDepCode = strDepCode;
	}

	/**
	 * @return 司机编码
	 */
	public String getStrDriverCode() {
		return strDriverCode;
	}

	/**
	 * @param 司机编码
	 */
	public void setStrDriverCode(String strDriverCode) {
		this.strDriverCode = strDriverCode;
	}

	/**
	 * @return 出车状态
	 */
	public String getStrdrpCarStatus() {
		return strdrpCarStatus;
	}

	/**
	 * @param 出车状态
	 */
	public void setStrdrpCarStatus(String strdrpCarStatus) {
		this.strdrpCarStatus = strdrpCarStatus;
	}
}