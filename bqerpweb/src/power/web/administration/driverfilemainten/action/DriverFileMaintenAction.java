/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.web.administration.driverfilemainten.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJDriverfile;
import power.ejb.administration.AdJDriverfileFacadeRemote;
import power.ejb.administration.business.DriverFileMaintenFacadeRemote;
import power.ejb.administration.form.DriverFileMaintenInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.ExportXsl;

/**
 * @author chaihao
 * 
 */
public class DriverFileMaintenAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 导出文件名前缀 */
	private static final String STR_FILE_NAME = "司机档案";
	/** 读取位置 */
	private int start = 0;
	/** 读取记录数 */
	private int limit = 0;
	/** 人员编码 */
	private String strWorkerCode = "";
	/** 部门编码 */
	private String strDepCode = "";
	/** 办照时间 */
	private String strLicence = "";
	/** 司机实体 */
	private AdJDriverfile driverFile;
	/** 司机档案维护远程对象 */
	DriverFileMaintenFacadeRemote remote;
	/** 司机档案处理远程对象 */
	AdJDriverfileFacadeRemote adfremote;

	/**
	 * 构造函数
	 */
	public DriverFileMaintenAction() {
		remote = (DriverFileMaintenFacadeRemote) factory
				.getFacadeRemote("DriverFileMaintenFacade");
		adfremote = (AdJDriverfileFacadeRemote) factory
				.getFacadeRemote("AdJDriverfileFacade");
	}
	
	/**
	 * 司机档案查询
	 *
	 */
	public void driverFileQuery() {
		LogUtil.log("Action:司机档案查询开始", Level.INFO, null);
		try {
			PageObject pob = remote.findDriverFile(employee.getEnterpriseCode(), strDepCode, strWorkerCode,
					strLicence, start, limit);
			request.getSession().setAttribute("pageObjDriverFile", pob);
			if (pob.getTotalCount() != 0) {
				String strRes = JSONUtil.serialize(pob);
				write(strRes);
			} else {
				write("{totalCount:0,list:[]}");
			}
			LogUtil.log("Action:司机档案查询结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		} catch (JSONException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:格式化数据失败", Level.SEVERE, e);
		}
	}

	/**
	 * 增加司机档案
	 */
	public void addDriverFile() {
		LogUtil.log("Action:新增司机档案开始", Level.INFO, null);
		try {
			// 司机档案是否重复检查
			if(!adfremote.checkDriver(driverFile.getDriverCode(), employee.getEnterpriseCode())){
				write(Constants.ADD_DRIVERCODE_FAILURE);
				return;
			}
			// 设置修改人
			driverFile.setUpdateUser(employee.getWorkerCode());
			// 设置修改时间
			driverFile.setUpdateTime(new Date());
			// 设置是否使用
			driverFile.setIsUse("Y");
			// 设置企业代码
			driverFile.setEnterpriseCode(employee.getEnterpriseCode());
			// 数据库更新
			adfremote.save(driverFile);
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:新增司机档案结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 修改司机档案
	 * @throws Exception 
	 */
	public void modifyDriverFile() {
		LogUtil.log("Action:修改司机档案开始", Level.INFO, null);
		try {
			// 按序号查找司机档案
			AdJDriverfile model = adfremote.findById(driverFile.getId());
			// 设置驾照类型
			model.setLicence(driverFile.getLicence());
			// 设置驾照号码
			model.setLicenceNo(driverFile.getLicenceNo());
			// 设置办照时间
			model.setLicenceDate(driverFile.getLicenceDate());
			// 设置年检时间
			model.setCheckDate(driverFile.getCheckDate());
			// 设置手机号码
			model.setMobileNo(driverFile.getMobileNo());
			// 设置家庭电话
			model.setTelNo(driverFile.getTelNo());
			// 设置家庭住址
			model.setHomeAddr(driverFile.getHomeAddr());
			// 设置通讯地址
			model.setComAddr(driverFile.getComAddr());
			// 设置修改人
			model.setUpdateUser(employee.getWorkerCode());
			// 设置修改时间
			model.setUpdateTime(new Date());
			// 更新
			adfremote.update(model);
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:修改司机档案结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 删除司机档案
	 */
	public void deleteDriverFile() {
		LogUtil.log("Action:删除司机档案开始", Level.INFO, null);
		try {
			// 按序号查找
			AdJDriverfile model = adfremote.findById(driverFile.getId());
			// 设置是否使用
			model.setIsUse("N");
			// 设置修改人
			model.setUpdateUser(employee.getWorkerCode());
			// 设置修改时间
			model.setUpdateTime(new Date());
			// 更新
			adfremote.update(model);
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除司机档案结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 查找人员性别年龄
	 */
	@SuppressWarnings("unchecked")
	public void driverFileEmployeeQuery() {
		LogUtil.log("Action:人员基本信息查询开始", Level.INFO, null);
		try {
			// 按人员编码查找人员基本信息
			PageObject pob = remote.findByWorkCode(strWorkerCode);
			if (pob.getList() != null && pob.getList().size() > 0) {
				write("{success:true,sex:'" + pob.getList().get(0)
						+ "',age:'" + pob.getList().get(1) + "'}");
			} else {
				write("{success:false}");
			}
			LogUtil.log("Action:人员基本信息查询结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 导出文件
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void exportDriverFile() throws Exception {

		try {
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
			lstHeader.add("姓名");
			lstHeader.add("性别");
			lstHeader.add("年龄");
			lstHeader.add("所在部门");
			lstHeader.add("驾照类型");
			lstHeader.add("驾照号码");
			lstHeader.add("办照日期");
			lstHeader.add("年检日期");
			lstHeader.add("手机号码");
			lstHeader.add("家庭电话");
			lstHeader.add("家庭住址");
			lstHeader.add("通讯住址");
			exsl.setLstTitle(lstHeader);

			// excel文件中的一行
			List<String> lstRow = null;
			// excel文件中的所有行集合
			List lstRowSet = new ArrayList();
			// excel文件单行实体
			DriverFileMaintenInfo tempDriverInfo = null;
			// 获得需要导出的excel文件内容
			PageObject pageObj = (PageObject) request.getSession()
					.getAttribute("pageObjDriverFile");
			for (int i = 0; i < pageObj.getList().size(); i++) {
				lstRow = new ArrayList();
				tempDriverInfo = (DriverFileMaintenInfo) pageObj.getList().get(
						i);
				// 设置行号
				lstRow.add(i + 1 + "");
				// 设置姓名
				if (tempDriverInfo.getName() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempDriverInfo.getName());
				}
				// 设置性别
				if (tempDriverInfo.getSex() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempDriverInfo.getSex());
				}
				// 设置年龄
				if (tempDriverInfo.getAges() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempDriverInfo.getAges());
				}
				// 设置所在部门
				if (tempDriverInfo.getDepName() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempDriverInfo.getDepName());
				}
				// 设置驾照类型
				if (tempDriverInfo.getLicence() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempDriverInfo.getLicence());
				}
				// 设置驾照号码
				if (tempDriverInfo.getLicenceNo() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempDriverInfo.getLicenceNo());
				}
				// 设置办照时间
				if (tempDriverInfo.getLicenceDate() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempDriverInfo.getLicenceDate());
				}
				// 设置年检时间
				if (tempDriverInfo.getCheckDate() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempDriverInfo.getCheckDate());
				}
				// 设置手机号码
				if (tempDriverInfo.getMobileNo() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempDriverInfo.getMobileNo());
				}
				// 设置家庭电话
				if (tempDriverInfo.getTelNo() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempDriverInfo.getTelNo());
				}
				// 设置家庭住址
				if (tempDriverInfo.getHomeAddr() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempDriverInfo.getHomeAddr());
				}
				// 设置通讯地址
				if (tempDriverInfo.getComAddr() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempDriverInfo.getComAddr());
				}
				lstRowSet.add(lstRow);
			}
			// 设置所有行内容
			exsl.setLstRow(lstRowSet);
			// 创建导出excel文件
			exsl.createXsl();
			LogUtil.log("Action:导出文件结束", Level.INFO, null);
		} catch (IOException e) {
			write(Constants.IO_FAILURE);
			LogUtil.log("Action:导出文件IO异常", Level.SEVERE, e);
		}
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the driverFile
	 */
	public AdJDriverfile getDriverFile() {
		return driverFile;
	}

	/**
	 * @param driverFile
	 *            the driverFile to set
	 */
	public void setDriverFile(AdJDriverfile driverFile) {
		this.driverFile = driverFile;
	}

	/**
	 * @return the strWorkerCode
	 */
	public String getStrWorkerCode() {
		return strWorkerCode;
	}

	/**
	 * @param strWorkerCode the strWorkerCode to set
	 */
	public void setStrWorkerCode(String strWorkerCode) {
		this.strWorkerCode = strWorkerCode;
	}

	/**
	 * @return the strDepCode
	 */
	public String getStrDepCode() {
		return strDepCode;
	}

	/**
	 * @param strDepCode the strDepCode to set
	 */
	public void setStrDepCode(String strDepCode) {
		this.strDepCode = strDepCode;
	}

	/**
	 * @return the strLicence
	 */
	public String getStrLicence() {
		return strLicence;
	}

	/**
	 * @param strLicence the strLicence to set
	 */
	public void setStrLicence(String strLicence) {
		this.strLicence = strLicence;
	}

}
