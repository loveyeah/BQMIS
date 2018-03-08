/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.workticket.query.action;

import java.util.ArrayList;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.opticket.RunJOpticketFacadeRemote;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.ejb.workticket.RunCWorkticketType;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.form.WorkticketInfo;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 工作票终结预警查询Action
 * 
 * @author jincong
 * @version 1.0
 */
public class WorkticketEndWarnQueryAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 工作票类型远程接口 */
	private RunCWorkticketTypeFacadeRemote runCTypeRemote;
	/** 运行专业远程接口 */
	private RunCSpecialsFacadeRemote runCSpecialsRemote;
	/** 工作票查询远程接口 */
	private RunJWorkticketsFacadeRemote runJRemote;

	/**
	 * 构造函数
	 */
	public WorkticketEndWarnQueryAction() {
		// 远程处理对象的取得
		runCTypeRemote = (RunCWorkticketTypeFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketTypeFacade");
		runCSpecialsRemote = (RunCSpecialsFacadeRemote) factory
				.getFacadeRemote("RunCSpecialsFacade");
		runJRemote = (RunJWorkticketsFacadeRemote) factory
				.getFacadeRemote("RunJWorkticketsFacade");
	}

	/**
	 * 取得工作票类型
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getEndWarnWorkticketType() throws JSONException {
		// 根据企业编码取得所有工作票类型
		PageObject object = runCTypeRemote.findAll(Constants.ENTERPRISE_CODE);
		// 查询结果为null
		if (object == null) {
			object.setList(new ArrayList<RunCWorkticketType>());
		}
		// 添加"所有"选项
		RunCWorkticketType runCWorkticketType = new RunCWorkticketType();
		// 种类为"所有"
		runCWorkticketType.setWorkticketTypeName(Constants.ALL_SELECT);
		// 编码为空
		// runCWorkticketType.setWorkticketTypeCode(Constants.BLANK_STRING);
		runCWorkticketType.setWorkticketTypeCode("%");
		// 添加
		object.getList().add(0, runCWorkticketType);
		write(JSONUtil.serialize(object));
	}

	/**
	 * 取得运行专业和检修专业
	 * 
	 * @throws JSONException
	 */
	public void getEndWarnRunSpeciality() throws JSONException {
		// 取得专业类型
		// 1: 运行专业
		// 2: 检修专业
		String strType = request.getParameter("specialityType");
		// 根据专业类型,取得数据
		List<RunCSpecials> list = runCSpecialsRemote.findByType(strType,
				Constants.ENTERPRISE_CODE);
		// 查询结果为null
		if (list == null) {
			list = new ArrayList<RunCSpecials>();
		}
		// 添加"所有"选项
		RunCSpecials runCSpeclals = new RunCSpecials();
		// 种类为"所有"
		runCSpeclals.setSpecialityName(Constants.ALL_SELECT);
		// 编码为空
		// runCSpeclals.setSpecialityCode(Constants.BLANK_STRING);
		runCSpeclals.setSpecialityCode("%");
		// 添加
		list.add(0, runCSpeclals);
		// 转化为PageObject
		PageObject object = new PageObject();
		// 设置list
		object.setList(list);
		// 设置长度
		object.setTotalCount(new Long(list.size()));
		write(JSONUtil.serialize(object));
	}

	/**
	 * 取得工作票
	 * 
	 * @throws JSONException
	 */
	public void getEndWarnWorkticketList() throws JSONException {
		// 取得查询参数: 工作票种类
		String workticketTypeCode = request.getParameter("workticketTypeCode");
		// 取得查询参数: 运行专业
		String runSpeciality = request.getParameter("runSpeciality");
		// 取得查询参数: 检修专业
		String repairSpeciality = request.getParameter("repairSpeciality");

		// 根据查询条件，取得工作票
		List<WorkticketInfo> list = runJRemote.workticketEndWarn(
				Constants.ENTERPRISE_CODE, workticketTypeCode, runSpeciality,
				repairSpeciality);
		// 查询结果为null
		if (list == null) {
			list = new ArrayList<WorkticketInfo>();
		}
		// 转化为PageObject
		PageObject object = new PageObject();
		// 设置list
		object.setList(list);
		// 设置长度
		object.setTotalCount(new Long(list.size()));
		write(JSONUtil.serialize(object));
	}

	// add by sltang
	public void getOptickectCode() {
		RunJOpticketFacadeRemote opremote = (RunJOpticketFacadeRemote) factory
				.getFacadeRemote("RunJOpticketFacade");
		String code="";
		String wCode=request.getParameter("workticketNo");
		code=opremote.findByWorktickectNo(wCode);
//		write("{success:true,code:"+code+"}");
		write(code);
	}
}
