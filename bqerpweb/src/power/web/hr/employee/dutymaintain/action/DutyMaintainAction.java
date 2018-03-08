/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.web.hr.employee.dutymaintain.action;

import java.sql.SQLException;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 班组维护
 * @author chen shoujiang
 *  
 */
public class DutyMaintainAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	/** 部门remote**/
	private HrCDeptFacadeRemote hrCDeptFacadeRemote;
	/** 班组类别 */
	private static final String BANZU_SORT_1 = "1"; 
    /** 查询结果为null,传到前台的string */
    private static String STR_JSON_NULL = "{\"list\":[],\"totalCount\":0}";
    /**保存数据成功返回前台的String*/
    private static String STR_SAVE_SUCCESS = "{success:true,msg:'success'}";
	/** 部门ids*/
	private String ids;

	/**
	 * 构造函数
	 */
	public DutyMaintainAction() {
		hrCDeptFacadeRemote = (HrCDeptFacadeRemote) factory
				.getFacadeRemote("HrCDeptFacade");
	}
	
	/**
	 * 查询班组信息
	 * @throws JSONException
	 * @throws Exception
	 */
	public void getBanzuInfo() throws JSONException, Exception {
		try{
			LogUtil.log("Action:查询是否班组=='1'的班组信息开始", Level.INFO, null);
			// 分页信息
			PageObject obj = new PageObject();
				// 无分页信息时执行
			obj = hrCDeptFacadeRemote.getBanzuInfo(BANZU_SORT_1,employee.getEnterpriseCode());
			// 输出
			String strOutput = Constants.BLANK_STRING;
			//　要是查询结果不为空的话，就赋值
			if(obj.getList() != null && obj.getList().size() > 0) {
				strOutput = JSONUtil.serialize(obj);
			} else {
				// 否则设为空值
				strOutput = STR_JSON_NULL;
			}
			write(strOutput);
			LogUtil.log("Action:查询是否班组=='1'的班组信息结束", Level.INFO, null);
		} catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询是否班组=='1'的班组信息失败", Level.INFO, null);
		} catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:查询是否班组=='1'的班组信息失败", Level.INFO, null);
		}
	}
	
	/**
	 * 保存班组数据
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void updateDeptBanzuData() throws Exception {
		try {
			LogUtil.log("Action:班组数据更新开始", Level.INFO, null);
			// 更新数据
			hrCDeptFacadeRemote.updateDeptBanzu(ids, employee.getEnterpriseCode(),employee.getWorkerCode());
			write( STR_SAVE_SUCCESS);
			LogUtil.log("Action:班组数据更新结束", Level.INFO, null);
		} catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:班组数据更新失败", Level.INFO, null);
		}
	}
	
	/**
	 * 获取班组数据的部门id
	 * @return
	 */
	public String getIds() {
		return ids;
	}
	
	/**
	 * 设置班组数据的部门id
	 * @param ids
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}
}
