/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.leaderaudit.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJUserMenu;
import power.ejb.administration.AdJUserMenuFacadeRemote;
import power.ejb.administration.business.LeaderAuditInfoFacadeRemote;
import power.ejb.administration.form.LeaderAuditInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.*;

/**
 * 值长审核Action
 * @author zhengzhipeng
 * @version 1.0
 */

public class LeaderAuditAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
 
	/** 值长审核远程接口 */
	private LeaderAuditInfoFacadeRemote remote;
	/** 用户点菜（退回，审核）远程接口 */
	private AdJUserMenuFacadeRemote userMenuRemote;
	/** 导出excel文件名前缀 */
	private static final String STR_FILE_NAME = "值长审核";
    /** 查询参数：订餐日期 */
	private String menuDate;
	/** 查询参数：订餐类别 */
	private String menuType;
	/** 退出、审核处理参数：订单号 */
	private String menuId;
	/** 退出、审核处理参数：修改时间 */
	private String updateTime;
	/**
	 * 构造函数
	 */
	public LeaderAuditAction() {
		// 远程处理对象的取得
		remote = (LeaderAuditInfoFacadeRemote) factory.getFacadeRemote("LeaderAuditInfoFacade");
		userMenuRemote = (AdJUserMenuFacadeRemote) factory.getFacadeRemote("AdJUserMenuFacade");
	}
	/**
	 * 查询
	 */
	public void getLeaderAuditInfoList() {
		try {
			LogUtil.log("Action:值长审核查询开始", Level.INFO, null);
			// 根据查询条件，取得相应信息
			PageObject object = remote.getLeaderAuditInfo(menuDate, menuType, employee.getEnterpriseCode());
			// 供导出
			request.getSession().setAttribute("pageObjLeaderAuditInfo", object);
			// 查询结果为null,设置页面显示
			if (object.getList() != null) {
				String str = JSONUtil.serialize(object);
				write(str);
			} else {
				String str = "{\"list\":[],\"totalCount\":0,msg:''}";
				write(str);
			}
			LogUtil.log("Action:值长审核查询结束", Level.INFO, null);
		} catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:接待审批查询失败", Level.SEVERE, jsone);
        } catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:接待审批查询失败。", Level.SEVERE, sqle);
        }
	}
	/**
	 * 导出文件
	 * 
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void exportLeaderAuditInfoFile() throws Exception {

		LogUtil.log("Action:导出文件开始", Level.INFO, null);
		// 操作excel文件对象
		ExportXsl exsl = new ExportXsl();
		// 设置response
		exsl.setResponse(response);
		// 当前时间作为文件名以部分
		Date dte = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String strDate = sdf.format(dte);
		// 设定文件名
		exsl.setStrFileName(STR_FILE_NAME + strDate);
		// excel文件每列标题头
		List<String> lstHeader = new ArrayList<String>();
		lstHeader.add("行号");
		lstHeader.add("订单号");
		lstHeader.add("订餐类别");
		lstHeader.add("订餐人");
		lstHeader.add("所属部门");
		lstHeader.add("填单日期");
		lstHeader.add("就餐地点");
		lstHeader.add("菜谱名称");
		lstHeader.add("菜谱类别");
		lstHeader.add("份数");
		lstHeader.add("单价(元)");
		lstHeader.add("备注");
		exsl.setLstTitle(lstHeader);

		// excel文件中的一行
		List<String> lstRow = null;
		// excel文件中的所有行集合
		List lstRowSet = new ArrayList();
		// excel文件单行实体
		LeaderAuditInfo tempInfo = null;
		// 获得需要导出的excel文件内容
		PageObject pageObj = (PageObject) request.getSession().getAttribute("pageObjLeaderAuditInfo");

		int intCnt = pageObj.getList().size();
		for (int i = 0; i < intCnt; i++) {
			lstRow = new ArrayList();
			tempInfo = (LeaderAuditInfo) pageObj.getList().get(i);
			// 设置行号
			lstRow.add(i + 1 + "");
			// 设置订单号
			if (tempInfo.getMenuId() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getMenuId());
			}
			// 设置订餐类别
			if (tempInfo.getMenuType() == null) {
				lstRow.add("");
			} else {
				lstRow.add(menuTypeFormat(tempInfo.getMenuType()));
			}
			// 设置订餐人
			if (tempInfo.getWorkerName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getWorkerName());
			}
			// 设置所属部门
			if (tempInfo.getDepName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getDepName());
			}
			// 设置填单日期
			if (tempInfo.getInsertDate() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getInsertDate());
			}
			// 设置就餐地点
			if (tempInfo.getPlace() == null) {
				lstRow.add("");
			} else {
				lstRow.add(placeFormat(tempInfo.getPlace()));
			}
			// 设置菜谱名称
			if (tempInfo.getMenuName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getMenuName());
			}
			// 设置菜谱类别
			if (tempInfo.getMenuTypeName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getMenuTypeName());
			}
			// 设置份数
			if (tempInfo.getMenuAmount() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getMenuAmount());
			}
			// 设置单价
			if (tempInfo.getMenuPrice() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getMenuPrice());
			}
			// 设置备注
			if (tempInfo.getMemo() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getMemo());
			}
			lstRowSet.add(lstRow);
		}
		// 设置所有行内容
		exsl.setLstRow(lstRowSet);
		// 创建导出excel文件
		exsl.createXsl();
		LogUtil.log("Action:导出文件结束", Level.INFO, null);
	}
	/**
	 * 退回处理
	 */
	public void doBackRecord(){
		updateRecord(true);
	}
	/**
	 * 审核处理
	 */
	public void doAuditRecord(){
		updateRecord(false);
	}
	/**
	 * 审核处理,退回处理
	 * 
	 * @param isBack 是否退回
	 */
	private void updateRecord(boolean isBack){
		// 通过订单号点菜表实体
		Long id = Long.parseLong(menuId);
		AdJUserMenu newEntity = userMenuRemote.findById(id);
		
		// 设置本次修改人
		String updateUser = employee.getWorkerCode();
		newEntity.setUpdateUser(updateUser);
		// 设置本次修改时间
		Date newUpdateTime = new Date();
		newEntity.setUpdateTime(newUpdateTime);
		
		// 设置订餐状态
		if(isBack){
			// 退回
		    newEntity.setMenuInfo(CodeConstants.ORDER_STATUS_1);
		}else {
			// 审核
			newEntity.setMenuInfo(CodeConstants.ORDER_STATUS_3);
		}
		
		// 更新保存
		try{
		    userMenuRemote.backUpdate(newEntity, updateTime);
		    write("{success:true}");
		} catch (DataChangeException e) {
			LogUtil.log("Action:退回处理失败。", Level.SEVERE, null);
			// 排他操作
			write(Constants.DATA_USING);
		} catch (SQLException e) {
			LogUtil.log("Action:退回处理失败。", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		} catch (Exception e) {
			LogUtil.log("Action:退回处理失败。", Level.SEVERE, e);
		}
	}
	/**
     * 订餐类型名称化
     */
	private String menuTypeFormat(String value){
		String newValue = "";
    	if(CodeConstants.MEAL_TYPE_1.equals(value)){
    		newValue = "早餐";
    	}else if(CodeConstants.MEAL_TYPE_2.equals(value)){
    		newValue = "中餐";
    	}else if(CodeConstants.MEAL_TYPE_3.equals(value)){
    		newValue = "晚餐";
    	}else if(CodeConstants.MEAL_TYPE_4.equals(value)){
    		newValue = "宵夜";
    	}
    	return newValue;
    }
	/**
     * 就餐地点名称化
     */
	private String placeFormat(String value){
		String newValue = "";
    	if(CodeConstants.MEAL_PLACE_1.equals(value)){
    		newValue = "餐厅";
    	}else if(CodeConstants.MEAL_PLACE_2.equals(value)){
    		newValue = "工作地";
    	}
    	return newValue;
    }
	/**
	 * @return 订餐日期
	 */
	public String getMenuDate() {
		return menuDate;
	}
	/**
	 * @param 订餐日期
	 */
	public void setMenuDate(String menuDate) {
		this.menuDate = menuDate;
	}
	/**
	 * @return 订餐类别
	 */
	public String getMenuType() {
		return menuType;
	}
	/**
	 * @param 订餐类别
	 */
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	/**
	 * @return 订单号
	 */
	public String getMenuId() {
		return menuId;
	}
	/**
	 * @param 订单号
	 */
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	/**
	 * @return 修改时间
	 */
	public String getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param 修改时间
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
