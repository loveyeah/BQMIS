/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.administration.restaurantplan.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.DataChangeException;
import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdCMenuWhFacadeRemote;
import power.ejb.administration.AdJRestaurantPlan;
import power.ejb.administration.AdJRestaurantPlanFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 餐厅计划维护Action
 * @author sufeiyu
 *
 */

@SuppressWarnings("serial")
public class RestaurantPlanAction extends AbstractAction {
	/** 获得当前用户ID* */
	Employee employee;
	/** 获得餐厅计划entity* */
	AdJRestaurantPlan restaurant;
	/** 获得餐厅计划实现接口* */
	AdJRestaurantPlanFacadeRemote rremote;
	/** 获得餐厅计划实现接口* */
	AdCMenuWhFacadeRemote mremote;
	/** 获得计划日期* */
	public java.sql.Date dtePlanDate;
	/** 获得菜谱类别* */
	public String strMenutypeCode;
	/** 获得菜谱ID* */
	public Long lngId;
	/** 获得上次修改时间* */
	public String strUpdateTime;
	/** 获得要增加的数据* */
	public String strAdd;
	/** 获得要更新的数据* */
	public String strUpdate;
	/** *画面参数开始页 */
	public Long start;
	/** 画面参数页面最大值* */
	public Long limit;

	/**
	 * 构造函数
	 */
	public RestaurantPlanAction() {
		rremote = (AdJRestaurantPlanFacadeRemote) factory
				.getFacadeRemote("AdJRestaurantPlanFacade");
		mremote = (AdCMenuWhFacadeRemote) factory
		.getFacadeRemote("AdCMenuWhFacade");
	}
	
	/**
	 * 取得全部餐厅计划 
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	public void getRestaurantPlan() throws JSONException {
		PageObject objResult = null;
		// 写出的数据
		String strRecord = "";

		LogUtil.log("Action:取得全部餐厅计划开始", Level.INFO, null);

		try {
			employee = (Employee) session.getAttribute("employee");
			String strEnterpriseCode = employee.getEnterpriseCode();
			// 计划日期
			String strPlanDate = "";
			if (dtePlanDate != null) {
				strPlanDate = dtePlanDate.toString();
			}

			// 取得全部餐厅计划
			@SuppressWarnings("unused")
			int intStart = Integer.parseInt(String.valueOf(start));
			@SuppressWarnings("unused")
			int intLimit = Integer.parseInt(String.valueOf(limit));
			objResult = rremote.getRestaurantPlan(strEnterpriseCode, strPlanDate);

			// 编码转换
			if (objResult.getTotalCount() > 0) {
				strRecord = JSONUtil.serialize(objResult);
			} else {
				strRecord = "{\"list\":[],\"totalCount\":null}";
			}
			write(strRecord);
			LogUtil.log("Action:取得全部餐厅计划正常结束", Level.INFO, null);
		} catch (NumberFormatException e) {
			LogUtil.log("Action:取得全部餐厅计划异常结束", Level.SEVERE, null);
		}
	}
	
	/**
	 * 取得全部菜谱信息
	 * 
	 * @throws JSONException
	 */
	public void getAllMenu() throws JSONException {
		employee = (Employee) session.getAttribute("employee");
		// 取得计划日期
		String strMenutypeCode1 = "";
		// 写出的数据
		String strRecord = "";
		PageObject objResult = new PageObject();

		if (strMenutypeCode != null) {
			strMenutypeCode1 = strMenutypeCode.toString();
		}

		int intStart = Integer.parseInt(String.valueOf(start));
		int intLimit = Integer.parseInt(String.valueOf(limit));
		objResult = mremote.getAllMenu(strMenutypeCode1, intStart, intLimit);

		// 字符转换
		if (objResult.getTotalCount() > 0) {
			strRecord = JSONUtil.serialize(objResult);
		} else {
			strRecord = "{\"list\":[],\"totalCount\":null}";
		}
		write(strRecord);
	}
	
	/**
	 * 删除一条餐厅计划
	 */
	public void deletePlan() {
		LogUtil.log("Action：删除一条餐厅计划数据开始", Level.INFO, null);

		try {
			employee = (Employee) session.getAttribute("employee");
			String strEmployee = employee.getWorkerCode();

			// 删除记事信息

			rremote.delete(strEmployee, lngId, strUpdateTime);
			// 显示删除成功信息
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action：删除一条餐厅计划数据正常结束", Level.INFO, null);
		} catch (SQLException e) {
			LogUtil.log("Action：删除一条餐厅计划数据异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		} catch (DataChangeException e) {
			LogUtil.log("Action：删除一条餐厅计划数据异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
		} catch (RuntimeException e) {
			LogUtil.log("Action：删除一条餐厅计划数据异常结束", Level.SEVERE, e);
		}
	}
	
	/**
	 * 处理后台传过来的数据
	 * 
	 * @throws JSONException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public void dealData() throws JSONException, ParseException {
		LogUtil.log("Action：更新餐厅计划数据开始", Level.INFO, null);
		try {
			// 取得用户ID
			employee = (Employee) session.getAttribute("employee");
			String strEmployee = employee.getWorkerCode();
			String strEnterpriseCode = employee.getEnterpriseCode();
			
			UserTransaction tx = (UserTransaction) new InitialContext()
            .lookup("java:comp/UserTransaction");
			
			// 事务处理起点
    		tx.begin();
			
			
			try {
				// 转换从前台获取的数据
				Object objUpdate = JSONUtil.deserialize(strUpdate); 
				SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
				Date dtePlanDate = null;
				if (objUpdate != null) {
					Map temMap = null;
					AdJRestaurantPlan objAddData = new AdJRestaurantPlan();
					if (List.class.isAssignableFrom(objUpdate.getClass())) {
						List lstObj = (List) objUpdate;
						for (int i = 0; i < lstObj.size(); i++) {
							temMap = (Map) (lstObj.get(i));
							// 设置entity
							objAddData = new AdJRestaurantPlan();
							// ID
							objAddData.setId(Long.parseLong(temMap.get("id")
									.toString()));
							// 是否使用
							objAddData.setIsUse("Y");
							// 备注
							objAddData.setMemo((String) temMap.get("memo"));
							// 菜谱编码
							objAddData.setMemuCode((String) temMap.get("menuCode"));
							// 价格
							if ((temMap.get("menuPrice") != null)
									&& (!(temMap.get("menuPrice").toString()
											.trim().equals("")))) {
								objAddData.setMenuPrice(Double
										.parseDouble(temMap.get("menuPrice")
												.toString()));
							}
							// 用餐类别
							objAddData.setMenuType((String) temMap.get("menuType"));
							// 日期
							dtePlanDate = dateFm.parse((String) temMap
									.get("planDate"));
							objAddData.setPlanDate(dtePlanDate);
							// 修改人
							objAddData.setUpdateUser(strEmployee);
							String strLastmodifyTime = (String) temMap
									.get("updateTime");
							// 企业代码
							objAddData.setEnterpriseCode(strEnterpriseCode);
							// 更新数据

							rremote.update(objAddData, strLastmodifyTime);
						}
					}
				}

					// 转换从前台获取的数据
					Object objAdd = JSONUtil.deserialize(strAdd);
					if (objAdd != null) {
						Map temMap = null;
						AdJRestaurantPlan objAddData = new AdJRestaurantPlan();
						if (List.class.isAssignableFrom(objAdd.getClass())) {
							List lstObj = (List) objAdd;
							@SuppressWarnings("unused")
							String strPrice = "";
							Long lngMaxId = rremote.getMaxId();
							for (int i = 0; i < lstObj.size();lngMaxId++, i++) {
								temMap = (Map) (lstObj.get(i));
								// 设置entity
								objAddData = new AdJRestaurantPlan();
								objAddData.setId(lngMaxId);
								// 是否使用
								objAddData.setIsUse("Y");
								// 备注
								objAddData.setMemo((String) temMap.get("memo"));
								// 菜谱编码
								objAddData.setMemuCode((String) temMap.get("menuCode"));
								// 价格
								if ((temMap.get("menuPrice") != null)
										&& (!(temMap.get("menuPrice").toString()
												.trim().equals("")))) {
									objAddData.setMenuPrice(Double
											.parseDouble(temMap.get("menuPrice")
													.toString()));
								}
								// 用餐类别
								objAddData.setMenuType((String) temMap.get("menuType"));
								// 日期
								dtePlanDate = dateFm.parse((String) temMap
										.get("planDate"));
								objAddData.setPlanDate(dtePlanDate);
								// 修改人
								objAddData.setUpdateUser(strEmployee);
								// 企业代码
								objAddData.setEnterpriseCode(strEnterpriseCode);
								// 增加数据
								rremote.save(objAddData);
								
							}
						}
					}
					tx.commit();
					write(Constants.ADD_SUCCESS);
					LogUtil.log("Action：更新餐厅计划数据正常结束", Level.INFO, null);
			} catch (DataChangeException e) {
				LogUtil.log("Action：更新餐厅计划数据异常结束", Level.SEVERE, e);
				tx.rollback();
				write(Constants.DATA_USING);
			} catch (SQLException e) {
				LogUtil.log("Action：更新餐厅计划数据异常结束", Level.SEVERE, e);
				tx.rollback();
				write(Constants.SQL_FAILURE);
			}
		} catch (Exception e) {
			LogUtil.log("Action：更新餐厅计划数据异常结束", Level.SEVERE, e);
		} 
}

	/**
	 * @return 餐厅计划entity
	 */
	public AdJRestaurantPlan getRestaurant() {
		return restaurant;
	}

	/**
	 * @param 餐厅计划entity
	 */
	public void setRestaurant(AdJRestaurantPlan restaurant) {
		this.restaurant = restaurant;
	}

	/**
	 * @return 计划日期
	 */
	public java.sql.Date getDtePlanDate() {
		return dtePlanDate;
	}

	/**
	 * @param 计划日期
	 */
	public void setDtePlanDate(java.sql.Date dtePlanDate) {
		this.dtePlanDate = dtePlanDate;
	}

	/**
	 * @return 菜谱类别
	 */
	public String getStrMenutypeCode() {
		return strMenutypeCode;
	}

	/**
	 * @param 菜谱类别
	 */
	public void setStrMenutypeCode(String strMenutypeCode) {
		this.strMenutypeCode = strMenutypeCode;
	}

	/**
	 * @return 画面参数开始页
	 */
	public Long getStart() {
		return start;
	}

	/**
	 * @param 画面参数开始页
	 */
	public void setStart(Long start) {
		this.start = start;
	}

	/**
	 * @return 画面参数页面最大值
	 */
	public Long getLimit() {
		return limit;
	}

	/**
	 * @param 画面参数页面最大值
	 */
	public void setLimit(Long limit) {
		this.limit = limit;
	}

	/**
	 * @return the lngId
	 */
	public Long getLngId() {
		return lngId;
	}

	/**
	 * @param lngId the lngId to set
	 */
	public void setLngId(Long lngId) {
		this.lngId = lngId;
	}

	/**
	 * @return the strUpdateTime
	 */
	public String getStrUpdateTime() {
		return strUpdateTime;
	}

	/**
	 * @param strUpdateTime the strUpdateTime to set
	 */
	public void setStrUpdateTime(String strUpdateTime) {
		this.strUpdateTime = strUpdateTime;
	}

	/**
	 * @return the strAdd
	 */
	public String getStrAdd() {
		return strAdd;
	}

	/**
	 * @param strAdd the strAdd to set
	 */
	public void setStrAdd(String strAdd) {
		this.strAdd = strAdd;
	}

	/**
	 * @return the strUpdate
	 */
	public String getStrUpdate() {
		return strUpdate;
	}

	/**
	 * @param strUpdate the strUpdate to set
	 */
	public void setStrUpdate(String strUpdate) {
		this.strUpdate = strUpdate;
	}

}
