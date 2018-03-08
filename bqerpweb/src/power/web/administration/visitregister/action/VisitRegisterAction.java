/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.visitregister.action;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import bsh.ParseException;

import power.ear.comm.DataChangeException;
import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJManpass;
import power.ejb.administration.AdJManpassFacadeRemote;
import power.ejb.administration.business.VisitRegisterFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 来访人员登记action
 * 
 * @author daichunlin
 * @version 1.0
 */
public class VisitRegisterAction extends AbstractAction {
	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 来访人员登记表entity */
	private AdJManpass adJManpass;
	/** 来访人员登记remote */
	private VisitRegisterFacadeRemote typeRemote;
	/** 来访人员登记表remote */
	private AdJManpassFacadeRemote adjRemote;
	/** 画面参数开始页 */
	public int start;
	/** 画面参数页面最大值 */
	public int limit;
	/** 画面参数序号 */
	public Long id;
	/** 画面参数bumen */
	public String deptCd;

	/**
	 * @return 序号
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param 序号
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return 画面参数开始页
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param 画面参数开始页
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return 画面参数页面最大值
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param 画面参数页面最大值
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 构造函数
	 */
	public VisitRegisterAction() {
		typeRemote = (VisitRegisterFacadeRemote) factory
				.getFacadeRemote("VisitRegisterFacade");
		adjRemote = (AdJManpassFacadeRemote) factory
				.getFacadeRemote("AdJManpassFacade");
	}

	/**
	 * 页面加载
	 * 
	 * @throws JSONException
	 * @throws ParseException
	 */
	public void getVisitInfoList() {
		LogUtil.log("Action:来访人员登记页面加载开始", Level.INFO, null);
		try {
			// 企业code			
			String strEnterpriseCode = employee.getEnterpriseCode();
			PageObject obj = typeRemote.getVisitInfoList(strEnterpriseCode,start, limit);
			// 查询结果为null,设置页面显示
			if (obj.getTotalCount() < 0) {
				String str = "{\"list\":[],\"totalCount\":0}";
				write(str);
			} else {
				String str = JSONUtil.serialize(obj);
				write(str);
			}
			LogUtil.log("Action:来访人员登记登记页面加载结束", Level.INFO, null);

		} catch (JSONException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:来访人员登记登记页面加载失败。", Level.SEVERE, e);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:来访人员登记登记页面加载失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 增加信息
	 */
	public void addVisitData() {
		LogUtil.log("Action:来访人员登记表增加正常开始", Level.INFO, null);
		try {
			employee = (Employee) session.getAttribute("employee");
			// 添加人
			adJManpass.setUpdateUser(employee.getWorkerCode().toString());
			// 是否使用
			adJManpass.setIsUse("Y");
			String dteInsertdate = request.getParameter("insertdate");
			String dteInDate = request.getParameter("inDate");
			String dteOutDate = request.getParameter("outDate");
			DateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date dateIns;
			Date dateIn;
			Date dateOut;
			if (dteInsertdate != null && !"".equals(dteInsertdate)) {
				dateIns = simpleDate.parse(dteInsertdate);
				adJManpass.setInsertdate(dateIns);
			}
			if (dteInDate != null && !"".equals(dteInDate)) {
				dateIn = simpleDate.parse(dteInDate);
				adJManpass.setInDate(dateIn);
			}
			if (dteOutDate != null && !"".equals(dteOutDate)) {
				dateOut = simpleDate.parse(dteOutDate);
				adJManpass.setOutDate(dateOut);
			}
			// 企业code			
			String strEnterpriseCode = employee.getEnterpriseCode();
			adJManpass.setEnterpriseCode(strEnterpriseCode);
			// 增加一条记录
			adjRemote.save(adJManpass);
			// 显示成功信息
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:来访人员登记表增加正常结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:来访人员登记表增加失败", Level.SEVERE, e);
		} catch (java.text.ParseException pe) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:来访人员登记表增加失败", Level.SEVERE, pe);
		}
	}

	/**
	 * 修改信息
	 * 
	 * @throws ParseException
	 */
	public void updateVisitData() {
		LogUtil.log("Action:修改来访人员登记表信息开始", Level.INFO, null);
		try {
			employee = (Employee) session.getAttribute("employee");
			AdJManpass visitRegisterEntity = new AdJManpass();
			visitRegisterEntity = adjRemote.findById(adJManpass.getId());
			// 更新者
			visitRegisterEntity.setUpdateUser(employee.getWorkerCode()
					.toString());

			String dteInsertdate = request.getParameter("insertdate");
			String dteInDate = request.getParameter("inDate");
			String dteOutDate = request.getParameter("outDate");
			DateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date dateIns;
			Date dateIn;
			Date dateOut;
			if (dteInsertdate != null && !"".equals(dteInsertdate)) {
				dateIns = simpleDate.parse(dteInsertdate);
				adJManpass.setInsertdate(dateIns);
			}
			if (dteInDate != null && !"".equals(dteInDate)) {
				dateIn = simpleDate.parse(dteInDate);
				adJManpass.setInDate(dateIn);
			}
			if (dteOutDate != null && !"".equals(dteOutDate)) {
				dateOut = simpleDate.parse(dteOutDate);
				adJManpass.setOutDate(dateOut);
			}
			// 来访人
			visitRegisterEntity.setInsertby(adJManpass.getInsertby());
			// 被访人部门
			visitRegisterEntity.setVisiteddep(adJManpass.getVisiteddep());
			// 来访时间
			visitRegisterEntity.setInsertdate(adJManpass.getInsertdate());
			// 被访人
			visitRegisterEntity.setVisitedman(adJManpass.getVisitedman());
			// 证件类别
			visitRegisterEntity.setPapertypeCd(adJManpass.getPapertypeCd());
			// 进厂时间
			visitRegisterEntity.setInDate(adJManpass.getInDate());
			// 证件号
			visitRegisterEntity.setPaperId(adJManpass.getPaperId());
			// 出厂时间
			visitRegisterEntity.setOutDate(adJManpass.getOutDate());
			// 来访人单位
			visitRegisterEntity.setFirm(adJManpass.getFirm());
			// 值班人
			visitRegisterEntity.setOnduty(adJManpass.getOnduty());
			// 备注
			visitRegisterEntity.setNote(adJManpass.getNote());

			// 前台取得时间
			Date lastmodified = adJManpass.getUpdateTime();
			DateFormat formatLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String lastmodifiedDate = formatLong.format(lastmodified);
			// 修改一条记录
			adjRemote.update(visitRegisterEntity, lastmodifiedDate);
			// 显示成功信息
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:修改来访人员登记表信息结束", Level.INFO, null);
		} catch (SQLException sqle) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:修改来访人员登记表信息失败", Level.SEVERE, sqle);
		} catch (DataChangeException ue) {
			write(Constants.DATA_USING);
			LogUtil.log("Action:修改来访人员登记表信息失败", Level.SEVERE, ue);
		} catch (java.text.ParseException pe) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:修改来访人员登记表信息失败", Level.SEVERE, pe);
		}

	}

	/**
	 * 删除信息
	 * 
	 * @throws ParseException
	 */
	public void deleteVisitData() throws ParseException {
		LogUtil.log("Action:删除来访人员登记表正常开始", Level.INFO, null);
		try {
			employee = (Employee) session.getAttribute("employee");
			String strEmployee = employee.getWorkerCode().toString();
			// 取得序号
			Long lngId = Long.parseLong(request.getParameter("id").toString());

			// 上次修改时间
			String strUpdateTime = request.getParameter("updateTime")
					.substring(0, 10)
					+ " " + request.getParameter("updateTime").substring(11);

			// 删除记事信息

			adjRemote.delete(strEmployee, lngId, strUpdateTime);
			// 显示删除成功信息
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除来访人员登记表正常结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:删除来访人员登记失败", Level.SEVERE, e);
		} catch (DataChangeException ue) {
			write(Constants.DATA_USING);
			LogUtil.log("Action:删除来访人员登记失败", Level.SEVERE, ue);
		}
	}

	/**
	 * @return 来访人员登记表
	 */
	public AdJManpass getAdJManpass() {
		return adJManpass;
	}

	/**
	 * @param 来访人员登记表
	 */
	public void setAdJManpass(AdJManpass adJManpass) {
		this.adJManpass = adJManpass;
	}

	/**
	 * @return the deptCd
	 */
	public String getDeptCd() {
		return deptCd;
	}

	/**
	 * @param deptCd
	 *            the deptCd to set
	 */
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}

}