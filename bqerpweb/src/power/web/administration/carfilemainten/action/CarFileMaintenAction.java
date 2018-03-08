/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.web.administration.carfilemainten.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJCarfile;
import power.ejb.administration.AdJCarfileFacadeRemote;
import power.ejb.administration.business.CarFileMaintenFacadeRemote;
import power.ejb.administration.form.CarFileMaintenInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.ExportXsl;

/**
 * @author chaihao
 * 
 */
public class CarFileMaintenAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 导出文件名前缀 */
	private static final String STR_FILE_NAME = "车辆档案";
	/** 读取位置 */
	private int start = 0;
	/** 导出记录数 */
	private int limit = 0;
	/** 查询开始日期 */
	private String strStartDate = "";
	/** 查询结束日期 */
	private String strEndDate = "";
	/** 部门编码 */
	private String strDepCode = "";
	/** 车辆档案实体 */
	private AdJCarfile carFile;
	/** 车辆档案维护远程对象 */
	CarFileMaintenFacadeRemote remote;
	/** 车辆档案远程对象 */
	AdJCarfileFacadeRemote adcremote;

	/**
	 * 构造函数
	 */
	public CarFileMaintenAction() {
		remote = (CarFileMaintenFacadeRemote) factory
				.getFacadeRemote("CarFileMaintenFacade");
		adcremote = (AdJCarfileFacadeRemote)factory.getFacadeRemote("AdJCarfileFacade");
	}

	/**
	 * 车辆档案查询
	 */
	public void carFileQuery(){
		LogUtil.log("Action:车辆档案查询开始", Level.INFO, null);
		try{
			// 按指定属性查找
			PageObject pob = remote.findCarFile(employee.getEnterpriseCode(), strStartDate, strEndDate, strDepCode, start, limit);
			request.getSession().setAttribute("pageObjCarFile", pob);
			if(pob.getTotalCount() != 0){
				String strRes = JSONUtil.serialize(pob);
				write(strRes);
			} else {
				write("{totalCount:0,list:[]}");
			}
			LogUtil.log("Action:车辆档案查询结束", Level.INFO, null);
		} catch (SQLException e){
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		} catch (JSONException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:格式化数据失败", Level.SEVERE, e);
		}
	}

	/**
	 * 新增车辆档案
	 */
	public void addCarFile() {
		LogUtil.log("Action:新增车辆档案开始", Level.INFO, null);
		try {
			employee =(Employee) session.getAttribute("employee");
			// 车辆档案是否重复检查
			if(!adcremote.checkCar(carFile.getCarNo(), employee.getEnterpriseCode())){
				write(Constants.ADD_CARNO_FAILURE);
				return;
			}
			// 设置使用情况
			carFile.setUseStatus("N");
			// 设置修改人
			carFile.setUpdateUser(employee.getWorkerCode());
			// 设置修改时间
			carFile.setUpdateTime(new Date());
			// 设置是否使用标志
			carFile.setIsUse("Y");
			// 设置企业代码
			carFile.setEnterpriseCode(employee.getEnterpriseCode());
			adcremote.save(carFile);
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:新增车辆档案结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 修改车辆档案
	 */
	public void modifyCarFile() {
		LogUtil.log("Action:修改车辆档案开始", Level.INFO, null);
		try {
			employee =(Employee) session.getAttribute("employee");
			// 按序号查找车辆档案
			AdJCarfile model = adcremote.findById(carFile.getId());
			// 设置车名
			model.setCarName(carFile.getCarName());
			// 设置车种
			model.setCarKind(carFile.getCarKind());
			// 设置车型
			model.setCarType(carFile.getCarType());
			// 设置车架
			model.setCarFrame(carFile.getCarFrame());
			// 设置发动机号码
			model.setEngineNo(carFile.getEngineNo());
			// 设置载人数
			model.setLoadman(carFile.getLoadman());
			// 设置载重
			model.setWeight(carFile.getWeight());
			// 设置特殊设备
			model.setEquip(carFile.getEquip());
			// 设置部门编码
			model.setDep(carFile.getDep());
			// 设置驾驶员
			model.setDriver(carFile.getDriver());
			// 设置行驶证
			model.setRunLic(carFile.getRunLic());
			// 设置通行证
			model.setRunAllLic(carFile.getRunAllLic());
			// 设置购买日期
			model.setBuyDate(carFile.getBuyDate());
			// 设置购买金额
			model.setPrice(carFile.getPrice());
			// 设置销售商家
			model.setCarshop(carFile.getCarshop());
			// 设置发票号
			model.setInvoiceNo(carFile.getInvoiceNo());
			// 设置耗油率
			model.setOilRate(carFile.getOilRate());
			// 设置保险费
			model.setIsurance(carFile.getIsurance());
			// 设置行驶里程
			model.setRunMile(carFile.getRunMile());
			// 设置修改人
			model.setUpdateUser(employee.getWorkerCode());
			// 设置修改时间
			model.setUpdateTime(new Date());
			// 设置是否使用
			model.setIsUse("Y");
			adcremote.update(model);
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:修改车辆档案结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 删除车辆档案
	 */
	public void deleteCarFile() {
		LogUtil.log("Action:删除车辆档案开始", Level.INFO, null);
		try {
			employee =(Employee) session.getAttribute("employee");
			// 按序号查找车辆档案
			AdJCarfile model = adcremote.findById(carFile.getId());
			// 设置是否使用
			model.setIsUse("N");
			// 设置修改人
			model.setUpdateUser(employee.getWorkerCode());
			// 设置修改时间
			model.setUpdateTime(new Date());
			adcremote.update(model);
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除车辆档案结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}
	@SuppressWarnings("unchecked")
	public void exportCarFile() throws Exception{

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
			lstHeader.add("车名");
			lstHeader.add("车种");
			lstHeader.add("车型");
			lstHeader.add("车牌号");
			lstHeader.add("车架");
			lstHeader.add("发动机号码");
			lstHeader.add("载人数(人)");
			lstHeader.add("载重量(吨)");
			lstHeader.add("特殊设备");
			lstHeader.add("使用情况");
			lstHeader.add("部门名称");
			lstHeader.add("驾驶员");
			lstHeader.add("行驶证");
			lstHeader.add("通行证");
			lstHeader.add("购买日期");
			lstHeader.add("购买金额(万元)");
			lstHeader.add("销售商家");
			lstHeader.add("发票号");
			lstHeader.add("耗油率");
			lstHeader.add("保险费");
			lstHeader.add("行车里程(公里)");
			exsl.setLstTitle(lstHeader);

			// excel文件中的一行
			List<String> lstRow = null;
			// excel文件中的所有行集合
			List lstRowSet = new ArrayList();
			// excel文件单行实体
			CarFileMaintenInfo tempCarInfo = null;
			// 获得需要导出的excel文件内容
			PageObject pageObj = (PageObject) request.getSession()
					.getAttribute("pageObjCarFile");
			for (int i = 0; i < pageObj.getList().size(); i++) {
				lstRow = new ArrayList();
				tempCarInfo = (CarFileMaintenInfo) pageObj.getList().get(i);
				// 设置行号
				lstRow.add(i + 1 + "");
				// 设置车名
				if (tempCarInfo.getCarName() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getCarName());
				}
				// 设置车种
				if (tempCarInfo.getCarKind() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getCarKind());
				}
				// 设置车型
				if (tempCarInfo.getCarType() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getCarType());
				}
				// 设置车牌号
				if (tempCarInfo.getCarNo() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getCarNo());
				}
				// 设置车架
				if (tempCarInfo.getCarFrame() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getCarFrame());
				}
				// 设置发动机号码
				if (tempCarInfo.getEngineNo() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getEngineNo());
				}
				// 设置在人数
				if (tempCarInfo.getLoadman() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getLoadman());
				}
				// 设置载重
				if (tempCarInfo.getWeight() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getWeight());
				}
				// 设置特殊设备
				if (tempCarInfo.getEquip() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getEquip());
				}
				// 设置使用情况
				if (tempCarInfo.getUseStatus() == null) {
					lstRow.add("");
				} else {
					if (tempCarInfo.getUseStatus().equals("Y")) {
						lstRow.add("使用");
					} else if (tempCarInfo.getUseStatus().equals("N")) {
						lstRow.add("空闲");
					} else {
						lstRow.add("");
					}
				}
				// 设置所在部门
				if (tempCarInfo.getDeptName() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getDeptName());
				}
				// 设置驾驶员
				if (tempCarInfo.getName() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getName());
				}
				// 设置行驶证
				if (tempCarInfo.getRunLic() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getRunLic());
				}
				// 设置通行证
				if (tempCarInfo.getRunAllLic() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getRunAllLic());
				}
				// 设置购买日期
				if (tempCarInfo.getBuyDate() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getBuyDate());
				}
				// 设置购买金额
				if (tempCarInfo.getPrice() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getPrice());
				}
				// 设置销售商家
				if (tempCarInfo.getCarshop() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getCarshop());
				}
				// 设置发票号
				if (tempCarInfo.getInvoiceNo() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getInvoiceNo());
				}
				// 设置耗油率
				if (tempCarInfo.getOilRate() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getOilRate());
				}
				// 设置保险费
				if (tempCarInfo.getIsurance() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getIsurance());
				}
				// 设置行车里程
				if (tempCarInfo.getRunMile() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarInfo.getRunMile());
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
	 * @return the carFile
	 */
	public AdJCarfile getCarFile() {
		return carFile;
	}

	/**
	 * @param carFile the carFile to set
	 */
	public void setCarFile(AdJCarfile carFile) {
		this.carFile = carFile;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
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
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the strStartDate
	 */
	public String getStrStartDate() {
		return strStartDate;
	}

	/**
	 * @param strStartDate the strStartDate to set
	 */
	public void setStrStartDate(String strStartDate) {
		this.strStartDate = strStartDate;
	}

	/**
	 * @return the strEndDate
	 */
	public String getStrEndDate() {
		return strEndDate;
	}

	/**
	 * @param strEndDate the strEndDate to set
	 */
	public void setStrEndDate(String strEndDate) {
		this.strEndDate = strEndDate;
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

}
