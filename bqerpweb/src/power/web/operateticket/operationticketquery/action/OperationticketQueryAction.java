/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.operateticket.operationticketquery.action;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.opticket.RunCOpticketTask;
import power.ejb.opticket.RunCOpticketTaskFacadeRemote;
import power.ejb.opticket.RunJOpticket;
import power.ejb.opticket.RunJOpticketFacadeRemote;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 操作票查询Action
 * 
 * @author zhouxu
 * @version 1.0
 */
public class OperationticketQueryAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 定义操作票bean */
	private RunJOpticket opticket;
	/** 操作票内容接口 */
	protected RunJOpticketFacadeRemote opticketRemote;
	/** 定义操作票类型接口 */
	protected RunCOpticketTaskFacadeRemote opticketTaskRemote;
	/** 定义操作票专业接口 */
	protected RunCSpecialsFacadeRemote specialsRemote;
	/** 定义状态为3的常量 */
	protected String STATUS_THREE = "3";
	/** 定义状态为4的常量 */
	protected String STATUS_FOUR = "4";

	/** 构造函数 */
	public OperationticketQueryAction() {
		opticketRemote = (RunJOpticketFacadeRemote) factory
				.getFacadeRemote("RunJOpticketFacade");
		opticketTaskRemote = (RunCOpticketTaskFacadeRemote) factory
				.getFacadeRemote("RunCOpticketTaskFacade");
		specialsRemote = (RunCSpecialsFacadeRemote) factory
				.getFacadeRemote("RunCSpecialsFacade");
	}

	/**
	 * 查询操作票内容
	 * 
	 * @throws JSONException
	 */
	public void getOperationticket() throws JSONException {
		/** 获取企业编码 */
		String enterpriseCode = Constants.ENTERPRISE_CODE;
		/** 开始查询位置 */ 
		String _start = request.getParameter("start");
		String _limit = request.getParameter("limit");
		if(_start == null || "".equals(_start))
		{
			_start = "0";
			_limit = "18";
		}
		int start = Integer.parseInt(_start);
		/** 查询行数 */
		int limit = Integer.parseInt(_limit);
		/** 开始时间 */
		String date = request.getParameter("date");
		/** 结束时间 */
		String date2 = request.getParameter("date2");
		/** 操作票类型 */
		String opType = request.getParameter("opType");
		/** 操作票专业 */
		String specialCode = request.getParameter("specialCode");
		/** 操作票状态 */
		String opticketStatus = request.getParameter("opticketStatus");
		String isStandar = request.getParameter("isStandar");
		String optaskName = request.getParameter("optaskName");
		String optaskNo =request.getParameter("optaskNo");
		String newOrOld = request.getParameter("newOrOld");
		String createBy = request.getParameter("createBy");
		// 替换“所有”为 “”
		if (Constants.ALL_SELECT.equals(opticketStatus)) {
			opticketStatus = "";
		}
		PageObject obj = new PageObject();
		obj = opticketRemote.getOpticketList(enterpriseCode,newOrOld, date, date2,
				opType, specialCode, opticketStatus, isStandar, optaskName,createBy,optaskNo,
				start, limit);
		// 如果查询list返回结果为空，则替换为长度为0的list
		if (null == obj.getList()) {
			List list = new ArrayList();
			obj.setList(list);

		}
		String str = JSONUtil.serialize(obj);
		// 如果查询返回结果为空，则替换为如下返回结果
		if (Constants.BLANK_STRING.equals(str) || null == str) {
			str = "{\"list\":[],\"totalCount\":null}";
		}
		write(str);

	}

	/**
	 * 操作票存档
	 * 
	 * @throws CodeRepeatException
	 */
	public void updateOperationticket() throws CodeRepeatException {
		try {
			/** 定义从前台获取的操作票代码 */
			String codes = request.getParameter("opticket.opticketCode");
			/** 将获取的操作票代码分割为数组 */
			String[] code = codes.split(",");
			// 判断是否有指
			if (code.length > 0) {
				for (int i = 0; i < code.length; i++) {
					// 获取指定code的操作票bean
					opticket = opticketRemote.findById(code[i]);
					// 如果状态为4则改为5，并更新
					// if (STATUS_THREE.equals(opticket.getOpticketStatus())) {
					// opticket.setOpticketStatus(STATUS_FOUR);
					// opticketRemote.update(opticket);
					// }
					if ("4".equals(opticket.getOpticketStatus())) {
						opticket.setOpticketStatus("5");
						opticketRemote.update(opticket);
					}
				}
			}
			write(Constants.MODIFY_SUCCESS);
		} catch (Exception e) {
			// 返回错误
			write(Constants.MODIFY_FAILURE);
		}
	}

	/**
	 * 获取操作票类型
	 * 
	 * @throws JSONException
	 */
	public void getOperationticketType() throws JSONException {

		PageObject obj = new PageObject();
		/** 定义操作票类型list */
		List<RunCOpticketTask> list = opticketTaskRemote
				.findByOTaskCodeLength(Constants.ENTERPRISE_CODE);
		/** 定义”所有“这个list选项 */
		RunCOpticketTask opt = new RunCOpticketTask();
		// 显示为“所有”
		opt.setOperateTaskName(Constants.ALL_SELECT);
		// value为空值
		opt.setOperateTaskCode(Constants.BLANK_STRING);
		// 添加到list
		list.add(0, opt);
		// 添加到obj
		obj.setList(list);
		// 格式化数据
		String str = JSONUtil.serialize(obj);
		// 返回
		write(str);
	}

	/**
	 * 获取操作票专业
	 * 
	 * @throws JSONException
	 */
	public void getSpeType() throws JSONException {

		PageObject obj = new PageObject();
		/** 定义操作票专业+list */
		List<RunCSpecials> list = specialsRemote
				.findSpeList(Constants.ENTERPRISE_CODE);
		/** 定义”所有“这个list选项 */
		RunCSpecials spe = new RunCSpecials();
		// 显示为“所有”
		spe.setSpecialityName(Constants.ALL_SELECT);
		// value为空值
		spe.setSpecialityCode(Constants.BLANK_STRING);
		// 添加到list
		list.add(0, spe);
		// 添加到obj
		obj.setList(list);
		// 格式化数据
		String str = JSONUtil.serialize(obj);
		// 返回
		write(str);
	}

	public void getTreeNode() throws JSONException {
		List<TreeNode> treeList = new ArrayList();
		String operateTsakId = request.getParameter("node");
		RunCOpticketTaskFacadeRemote taskRemote = (RunCOpticketTaskFacadeRemote) factory
				.getFacadeRemote("RunCOpticketTaskFacade");
		List<RunCOpticketTask> list = taskRemote.findByParentOperateTaskId(
				employee.getEnterpriseCode(), Long.parseLong(operateTsakId));
		for (RunCOpticketTask model : list) {
			TreeNode node = new TreeNode();
			node.setId(String.valueOf(model.getOperateTaskId()));
			node.setText(model.getOperateTaskName());
			node.setCode(model.getOperateTaskCode());
			if (model.getOperateTaskCode().length() == 4) {
				node.setLeaf(true);
			} else {
				node.setLeaf(false);
			}
			treeList.add(node);
		}
		write(JSONUtil.serialize(treeList));
	}

}
