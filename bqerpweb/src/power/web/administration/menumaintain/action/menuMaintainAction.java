/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.menumaintain.action;

import java.sql.SQLException;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdCMenuWh;
import power.ejb.administration.AdCMenuWhFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.ejb.administration.comm.CodeCommonFacadeRemote;

/**
 * 菜谱维护
 * @author li chensheng
 * @version 1.0
 */
public class menuMaintainAction extends AbstractAction{
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 查询，保存，修改数据远程对象 */
	private AdCMenuWhFacadeRemote   menuRemote;
	private CodeCommonFacadeRemote codeCommon;
	/** 分页 START */
	private int start;
	/** 分页 LIMIT */
	private int limit;
	/** 菜谱类别码 */
	private String menuTypeValue = "";
	/** 流水号 */
	private Long menuIdValue;
	/** 菜谱维护entity */
	private AdCMenuWh menuWH;
	/** 更新时间 */
	private static final String UPDATE_TIME = "updateTime";
	/**
	 * 构造函数
	 */
	public menuMaintainAction(){
		menuRemote = (AdCMenuWhFacadeRemote) factory
		          .getFacadeRemote("AdCMenuWhFacade");
		codeCommon = (CodeCommonFacadeRemote)factory
                  .getFacadeRemote("CodeCommonFacade");
	}
	/**
	 * @return 菜谱维护entity
	 */
	public AdCMenuWh getMenuWH() {
		return menuWH;
	}
	/**
	 * @param 菜谱维护entity
	 */
	public void setMenuWH(AdCMenuWh argMenuWH) {
		menuWH = argMenuWH;
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
	 * @return 菜谱类别码
	 */
	public String getMenuTypeValue() {
		return menuTypeValue;
	}
	/**
	 * @param 菜谱类别码
	 */
	public void setMenuTypeValue(String menuTypeValue) {
		this.menuTypeValue = menuTypeValue;
	}
	/**
	 * @return 流水号
	 */
	public Long getMenuIdValue() {
		return menuIdValue;
	}
	/**
	 * @param 流水号
	 */
	public void setMenuIdValue(Long menuIdValue) {
		this.menuIdValue = menuIdValue;
	}
	/**
	 *  查询菜谱维护信息
	 * 
	 * @throws JSONException
	 * 
	 */
	public void searchMenu() throws JSONException, Exception {
		LogUtil.log("Action:查询菜谱维护信息开始。", Level.INFO, null);	
		try{
			// 取得企业代码
			String enterpriseCode = employee.getEnterpriseCode();
			PageObject  obj = menuRemote.findMenu(menuTypeValue,enterpriseCode,start,limit);
			String str ="";
			// 如果查询返回结果为空，则替换为如下返回结果
	        if (obj.getTotalCount()<=0) {
	            str = "{\"list\":[],\"totalCount\":null}";
	        }else{
	        	//序列化PageObject类型对象,转化为字符串类型
	             str = JSONUtil.serialize(obj);           	
	        }
	        LogUtil.log("Action:查询菜谱维护信息结束。", Level.INFO, null);
			write(str);
		}catch (SQLException se){
			//显示失败
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:查询菜谱维护信息失败。", Level.SEVERE , null);
		}catch (JSONException jse){
			// 显示失败
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询菜谱维护信息失败。", Level.SEVERE, null);
		}
	}
	/**
	 *  删除一条菜谱维护信息
	 * 
	 * @throws JSONException
	 * 
	 */
	public void deleteMenu()throws JSONException, Exception {
		LogUtil.log("Action:删除菜谱维护信息开始。", Level.INFO, null);
		try {
			String lastmodifiedDate = request.getParameter(UPDATE_TIME);
			menuRemote.delete(menuIdValue,employee.getWorkerCode(),lastmodifiedDate);
			//删除成功
			write(Constants.DELETE_SUCCESS);
		} catch (DataChangeException e) {
			// 显示数据正在使用
			write(Constants.DATA_USING);
		} catch (SQLException e) {
			// 显示失败
			write(Constants.SQL_FAILURE);
		}		
		LogUtil.log("Action:删除菜谱维护信息结束。", Level.INFO, null);
	}
	/**
	 * 增加菜谱维护信息
	 */
	public void addMenu() {
		LogUtil.log("Action:增加菜谱维护信息开始。", Level.INFO, null);
		// 添加菜谱类别
		menuWH.setMenutypeCode(menuTypeValue);
		try {
			//共通取得菜谱编码 
			String menuCode;
			menuCode = codeCommon.getCMenuCode();
			//添加菜谱编码 
			menuWH.setMenuCode(menuCode);
		} catch (SQLException e) {
		    // 显示失败
			write(Constants.SQL_FAILURE);
		}
		// 增加企业代码
		menuWH.setEnterpriseCode(employee.getEnterpriseCode());
		// 是否使用
		menuWH.setIsUse("Y");
		// 添加修改人
		menuWH.setUpdateUser(employee.getWorkerCode());
		try {
			// 增加一条记录
			menuRemote.save(menuWH);
			// 显示成功信息
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:增加菜谱维护信息结束。", Level.INFO, null);	
		} catch (Exception ce) {
			LogUtil.log("Action:增加菜谱维护信息失败。", Level.SEVERE, ce);
		}
	}
	/**
	 *  修改菜谱维护信息
	 * @param
	 * 
	 * @throws JSONException
	 * 
	 */
	public void updateMenu() throws JSONException, Exception {
		LogUtil.log("Action:修改菜谱维护信息开始。", Level.INFO, null);
		try {
			// 取得entity
			AdCMenuWh adCMenuWhEntity = menuRemote.findById(menuWH.getId());
			// 添加菜谱类别
			adCMenuWhEntity.setMenutypeCode(menuTypeValue);
			// 更新菜谱名称
			adCMenuWhEntity.setMenuName(menuWH.getMenuName());
			// 更新配料说明
			adCMenuWhEntity.setMenuMemo(menuWH.getMenuMemo());
			// 更新价格
			adCMenuWhEntity.setMenuPrice(menuWH.getMenuPrice());
			// 更新检索码
			adCMenuWhEntity.setRetrieveCode(menuWH.getRetrieveCode());
			// 设置更新者
			adCMenuWhEntity.setUpdateUser(employee.getWorkerCode());
			// 传入前台的更新时间用于排他 
			adCMenuWhEntity.setUpdateTime(menuWH.getUpdateTime());
			// 修改一条记录
			menuRemote.update(adCMenuWhEntity);
			// 显示成功信息
			write(Constants.MODIFY_SUCCESS);
		} catch (DataChangeException e) {
			// 显示数据正在使用
			write(Constants.DATA_USING);
		} catch (SQLException e) {
			// 显示失败
			write(Constants.SQL_FAILURE);
		}
		LogUtil.log("Action:修改菜谱维护信息结束。", Level.INFO, null);				
	}
	



}
