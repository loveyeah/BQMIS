/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.menusystem.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.business.MenuSystemFacadeRemote;
import power.ejb.administration.comm.ADCommonFacadeRemote;
import power.ejb.administration.form.MenuSystemInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.ExportXsl;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 订餐信息查询
 * 
 * @author zhaomingjian
 * 
 */
public class MenuSystemAction extends AbstractAction {
	/** serial id */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	/** 导出excel文件名前缀 */
	private static final String STR_FILE_NAME = "订餐信息查询";
	// 远程EJB接口
	MenuSystemFacadeRemote remote = null;
	// 通用EJB接口
	ADCommonFacadeRemote remoteComm = null;

	/** 查询时间 */
	private String strDate = "";
	/** 人员类别 */
	private String strManType = "";
	/** 工作类型 */
	private String strMenuType = "";

	/** 起始查询行 */
	private int start = 0;
	/** 查询限制行 */
	private int limit = 0;

	/** 无参构造函数 */
	public MenuSystemAction() {
		// 调用EJB远程接口获取MenuSystemFacade实例
		remote = (MenuSystemFacadeRemote) factory
				.getFacadeRemote("MenuSystemFacade");
		// 调用EJB远程接口获取ADCommonFacade实例
		remoteComm = (ADCommonFacadeRemote) factory
				.getFacadeRemote("ADCommonFacade");

	}

	/**
	 * 
	 * @throws JSONException
	 */
	public void getMenuSystemInfo() throws JSONException {
		// Action log start
		LogUtil.log("Action:  订餐信息查询开始", Level.INFO, null);
		try {
             //取得企业编码 
			String strEnterpriseCode = employee.getEnterpriseCode();
			// 调用远程方法获取PageObject实例
			PageObject result = remote.getMenuSystemInfo(strDate,
					strManType, strMenuType,strEnterpriseCode);
			request.getSession().setAttribute("menuSystemInfo", result);
			// 转换为字符串形式
			String strPageObject = null;
			if (result.getTotalCount() <= 0) {
				strPageObject = "{\"list\":[],\"totalCount\":null}";
			} else {
				strPageObject = JSONUtil.serialize(result);
			}
			LogUtil.log("Action:  订餐信息查询正常结束", Level.INFO, null);
			write(strPageObject);
		} catch (SQLException sqle) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:  订餐信息查询失败", Level.SEVERE, sqle);
		} catch (JSONException jsone) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:  订餐信息查询失败", Level.SEVERE, jsone);
		}
	}

	/**
	 * 统计信息
	 * 
	 * @throws JSONException
	 */
	public void getMenuSystemSubInfo() throws JSONException {
		// Action log start
		LogUtil.log("Action:  订餐信息统计查询开始", Level.INFO, null);
		try {
            //取得用户企业编码
			String strEnterpriseCode = employee.getEnterpriseCode();
			// 调用远程方法获取PageObject实例
			PageObject result = (PageObject) remote.getMenuSystemSubInfo(
					strDate, strManType, strMenuType,strEnterpriseCode);

			// 转换为字符串形式
			String strPageObject = null;
			if (result.getTotalCount() <= 0) {
				strPageObject = "{\"list\":[],\"totalCount\":null}";
			} else {
				strPageObject = JSONUtil.serialize(result);
			}
			LogUtil.log("Action:订餐信息统计查询正常结束", Level.INFO, null);
			write(strPageObject);
		} catch (SQLException sqle) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:订餐信息统计查询失败", Level.SEVERE, sqle);
		} catch (JSONException jsone) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:订餐信息统计查询失败", Level.SEVERE, jsone);
		}
	}

	@SuppressWarnings("unchecked")
	public void exportFile() throws Exception {
		LogUtil.log("Action:订餐信息文件导出开始", Level.INFO, null);
		// 操作excel文件对象
		ExportXsl exsl = new ExportXsl();
		// 设置response
		exsl.setResponse(response);
		// 当前时间作为文件名以部分
		Date dte = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate = sdf.format(dte);
		// 设定文件名
		exsl.setStrFileName(STR_FILE_NAME + strDate);

		// excel文件每列标题头
		List<String> lstHeader = new ArrayList<String>();
		lstHeader.add("行号");
		// xsTan 删除开始 2009-1-23
//		lstHeader.add("用餐时间");
		// xsTan 删除结束 2009-1-23
		lstHeader.add("订餐人");
		lstHeader.add("所属部门");
		lstHeader.add("填单日期");
		lstHeader.add("就餐地点");
		lstHeader.add("菜谱名称");
		lstHeader.add("菜谱类别");
		lstHeader.add("份数");
		lstHeader.add("单价(元)");
		lstHeader.add("合计(元)");
		lstHeader.add("备注");
		// 设置标题行
		exsl.setLstTitle(lstHeader);
		// excel文件中的一行
		List<String> lstRow = null;
		// excel文件中的所有行集合
		List lstRowSet = new ArrayList();
		// excel文件单行实体
		MenuSystemInfo tempMenuSystemInfo = null;
		//总计
		Double dblTotal = 0.0;
		// 获得需要导出的excel文件内容
		PageObject pageObj = (PageObject) request.getSession().getAttribute(
				"menuSystemInfo");
		for (int i = 0; i < pageObj.getList().size(); i++) {
			lstRow = new ArrayList();
			tempMenuSystemInfo = (MenuSystemInfo) pageObj.getList().get(i);
			// 设置行号
			lstRow.add(i + 1 + "");
			//设置人员
			if (tempMenuSystemInfo.getName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempMenuSystemInfo.getName());
			}
			// 设置所属单位
			if (tempMenuSystemInfo.getDeptName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempMenuSystemInfo.getDeptName());
			}
			// 设置填单日期
			if (tempMenuSystemInfo.getInsertDate() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempMenuSystemInfo.getInsertDate());
			}
			// 设置地址
			if (tempMenuSystemInfo.getPlace() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempMenuSystemInfo.getPlace());
			}
			// 设置菜单名
			if (tempMenuSystemInfo.getMenuName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempMenuSystemInfo.getMenuName());
			}
			// 设置菜单类型名
			if (tempMenuSystemInfo.getMenuTypeName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempMenuSystemInfo.getMenuTypeName());
			}
			// 设置份数
			if (tempMenuSystemInfo.getMenuAmount() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempMenuSystemInfo.getMenuAmount());
			}
			// 设置单价
			if (tempMenuSystemInfo.getMenuPrice() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempMenuSystemInfo.getMenuPrice());
			}
			// 设置合计
			if (tempMenuSystemInfo.getMenuTotal() == null) {
				lstRow.add("");
			} else {
				dblTotal += Double.parseDouble(tempMenuSystemInfo.getMenuTotal());
				lstRow.add(tempMenuSystemInfo.getMenuTotal());
			}
			//设置备注
			if (tempMenuSystemInfo.getMemo() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempMenuSystemInfo.getMemo());
			}
			lstRowSet.add(lstRow);
		}
		//最后一行
		lstRow = new ArrayList();
		lstRow.add(0,"合计");
		lstRow.add(1,"");
		lstRow.add(2,"");
		lstRow.add(3,"");
		lstRow.add(4,"");
		lstRow.add(5,"");
		lstRow.add(6,"");
		lstRow.add(7,"");
		lstRow.add(8,"");
		lstRow.add(9,String.valueOf(dblTotal));
		//添加最后一行
		lstRowSet.add(lstRow);
		// 设置所有行内容
		
		exsl.setLstRow(lstRowSet);
		// 创建导出excel文件
		exsl.createXsl();
		LogUtil.log("Action:导出文件结束", Level.INFO, null);
	}

	/**
	 * 
	 * @return 查询时间
	 */
	public String getStrDate() {
		return strDate;
	}

	/**
	 * 
	 * @param strDate
	 *            查询时间
	 */
	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	/**
	 * 
	 * @return 人员类别
	 */
	public String getStrManType() {
		return strManType;
	}

	/**
	 * 
	 * @param strManType
	 *            人员类别
	 */
	public void setStrManType(String strManType) {
		this.strManType = strManType;
	}

	/**
	 * 
	 * @return 用餐类别
	 */
	public String getStrMenuType() {
		return strMenuType;
	}

	/**
	 * 
	 * @param strMenuType
	 *            用餐类别
	 */
	public void setStrMenuType(String strMenuType) {
		this.strMenuType = strMenuType;
	}

	/**
	 * 
	 * @return 开始行
	 */
	public int getStart() {
		return start;
	}

	/**
	 * 
	 * @param start
	 *            开始行
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * 
	 * @return 限制查询行
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * 
	 * @param limit
	 *            限制查询行
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
