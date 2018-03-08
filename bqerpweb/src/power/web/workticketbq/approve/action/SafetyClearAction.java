package power.web.workticketbq.approve.action;

import java.util.HashMap;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class SafetyClearAction extends AbstractAction{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 运行专业，检修专业远程对象 */
	private RunCSpecialsFacadeRemote runCSpecialsRemote;

	/** 查询远程对象 */
	private RunJWorkticketsFacadeRemote securityMeasureRemote;

	/** 根据类型查找专业列表 */
	private static final String SPECIALITY_TYPE_ONE = "1";
	private static final String SPECIALITY_TYPE_TWO = "2";

	/** 给前台combobox添加所有选项 */
	private static final String ALL_TYPE = "所有";

	/** 工作票状态 */
	private static final String WORKTICKET_TYPE = "8";
	/** 定义null */
	private static final String NULL = "null";

	/**
	 * 构造函数
	 */
	public SafetyClearAction() {
		runCSpecialsRemote = (RunCSpecialsFacadeRemote) factory
				.getFacadeRemote("RunCSpecialsFacade");
		securityMeasureRemote = (RunJWorkticketsFacadeRemote) factory
				.getFacadeRemote("RunJWorkticketsFacade");
	}

	/**
	 * 获取运行专业
	 * 
	 * @throws JSONException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void getRunSpecialList() throws JSONException {
		String enterpriseCode = Constants.ENTERPRISE_CODE;
		PageObject obj = new PageObject();
		obj.setList(runCSpecialsRemote.findByType(SPECIALITY_TYPE_ONE,
				enterpriseCode));
		RunCSpecials runC = new RunCSpecials();
		runC.setSpecialityName(ALL_TYPE);
		obj.getList().add(0, runC);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 获取检修专业
	 * 
	 * @throws JSONException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void getHaulSpecialList() throws JSONException {
		String enterpriseCode = Constants.ENTERPRISE_CODE;
		PageObject obj = new PageObject();
		obj.setList(runCSpecialsRemote.findByType(SPECIALITY_TYPE_TWO,
				enterpriseCode));
		RunCSpecials runC = new RunCSpecials();
		runC.setSpecialityName(ALL_TYPE);
		obj.getList().add(0, runC);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 查询，页面加载
	 * modify by fyyang 090109
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getSecurityMeasureList() throws JSONException {
		String enterpriseCode = Constants.ENTERPRISE_CODE;
		// 计划开始时间
		String strSdate = "";
		// 计划结束时间
		String strEdate = "";
		// 工作票种类
		String strTypeCode = "";
		// 工作票状态 (固定值: 8 )
		String workticketStausId = WORKTICKET_TYPE;
		// 安措拆除状态
		String strSafetyExeStatusId = "";
		// 运行专业
		String permissionDept = "";
		// 检修专业
		String strRepairSpecailCode = "";
		// 所属机组和系统
		String strEquAttributeCode = "";
		// 工作票号
		String fuzzy = "";
		// 获取前台数据
		Object sd = request.getParameter("startD");
		Object ed = request.getParameter("endD");
		Object tc = request.getParameter("typeC");
		Object sc = request.getParameter("stateC");
		Object rc = request.getParameter("runC");
		Object hc = request.getParameter("haulC");
		Object syc = request.getParameter("systemC");
		Object fu = request.getParameter("fuzzy");
		// 取得查询条件: 开始行
		Object objstart = request.getParameter("start");
		// 取得查询条件：结束行
		Object objlimit = request.getParameter("limit");
		if (sd != null)
			strSdate = sd.toString();
		if (ed != null)
			strEdate = ed.toString();
		if (tc != null)
			strTypeCode = tc.toString();
		if (ed != null)
			strEdate = ed.toString();
		if (sc != null)
			strSafetyExeStatusId = sc.toString();
		if (rc != null)
			permissionDept = rc.toString();
		if (hc != null)
			strRepairSpecailCode = hc.toString();
		if (syc != null)
			strEquAttributeCode = syc.toString();
		if (fu != null)
			fuzzy = fu.toString();
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = securityMeasureRemote.findSecurityMeasureForBreakOutList(
					enterpriseCode, strSdate, strEdate, strTypeCode,
					workticketStausId, permissionDept, strRepairSpecailCode,
					strEquAttributeCode, fuzzy,strSafetyExeStatusId,start, limit);
		} else
			obj = securityMeasureRemote.findSecurityMeasureForBreakOutList(
					enterpriseCode, strSdate, strEdate, strTypeCode,
					workticketStausId, permissionDept, strRepairSpecailCode,
					strEquAttributeCode, fuzzy,strSafetyExeStatusId);
		String str = JSONUtil.serialize(obj);
		if (NULL.equals(str)) {
			str = "{\"list\":[],\"totalCount\":null}";
		}
		write(str);
	}

	/**
	 * 安措拆除详细列表加载
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getSecurityDetailList() throws JSONException {
		String enterpriseCode = Constants.ENTERPRISE_CODE;
		PageObject obj = new PageObject();
		String workticketNo = "";
		Object wN = request.getParameter("workticketNo");
		if (wN != null)
			workticketNo = wN.toString();
		obj.setList(securityMeasureRemote.findSecurityMeasureForBreakOutByNo(
				enterpriseCode, workticketNo));
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 安措未拆除原因列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getNotSecurityList() throws JSONException {
		PageObject obj = new PageObject();
		String workticketNo = "";
		Object wN = request.getParameter("workticketNo");
		if (wN != null)
			workticketNo = wN.toString();
		obj.setList(securityMeasureRemote.findWorkticketHisList(workticketNo));
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 安措拆除保存
	 * 
	 * @throws JSONException
	 * 
	 */
	public void saveSafeSecurityReason() throws JSONException {
		// 索引存放id , 值存放是否选中(CLA表示选中记录行，EXE表示未选中)
		HashMap<String, String> map = new HashMap<String, String>();
		// 安措拆除人
		String workticketNo = request.getParameter("workticketNo");
		String reasonText = request.getParameter("reasonText");
		String workerCode = request.getParameter("workerCode");
		String ids = request.getParameter("ids");
		String isBreakOuts = request.getParameter("isBreakOuts");
		String[] idsA = ids.split(",");
		String[] isBreakOutsA = isBreakOuts.split(",");
		for (int i = 0; i < idsA.length; i++) {
			map.put(idsA[i], isBreakOutsA[i]);
		}

		securityMeasureRemote.breakOutSecurityMeasure(workticketNo, map,
				reasonText, workerCode);
		write("{success:true,msg:'&nbsp&nbsp&nbsp拆除成功！&nbsp&nbsp&nbsp'}");
	}

}
