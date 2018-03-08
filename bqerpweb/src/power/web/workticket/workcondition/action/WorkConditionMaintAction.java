package power.web.workticket.workcondition.action;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketWorkcondition;
import power.ejb.workticket.RunCWorkticketWorkconditionFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 工作条件维护Action
 * 
 * @author wangjunjie
 * 
 */
public class WorkConditionMaintAction extends AbstractAction {
	/** 工作条件处理远程对象 */
	private RunCWorkticketWorkconditionFacadeRemote remote;
	/** 工作条件 */
	private RunCWorkticketWorkcondition workCondition;
	
	/**
	 * 构造函数
	 */
	public WorkConditionMaintAction() {
		// 取得工作条件处理远程对象
		remote = (RunCWorkticketWorkconditionFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketWorkconditionFacade");
	}

	/**
	 * 取得工作条件
	 */
	public RunCWorkticketWorkcondition getWorkCondition() {
		return workCondition;
	}

	/**
	 * 设置工作条件
	 * 
	 * @param argWorkCondition
	 *            工作条件
	 */
	public void setWorkCondition(RunCWorkticketWorkcondition argWorkCondition) {
		workCondition = argWorkCondition;
	}

	/**
	 * 获得工作条件列表
	 * 
	 * @throws JSONException
	 */
	public void getWorkConditionList() throws JSONException {
		String strFuzzy = "";
		// 取得查询参数
		Object objSearch = request.getParameter("fuzzy");
		if (objSearch != null) {
			strFuzzy = objSearch.toString();
		}
		String strEnterpriseCode = Constants.ENTERPRISE_CODE;
		PageObject pobjInfo = new PageObject();
		// 查询工作条件信息列表
		pobjInfo.setList(remote.findByNameOrId(strEnterpriseCode, strFuzzy));

		// 序列化为JSON对象的字符串形式
		String strInfo = JSONUtil.serialize(pobjInfo);
		// 以HTML方式输出字符串
		write(strInfo);
	}

	/**
	 * 增加工作条件
	 * 
	 * @throws CodeRepeatException
	 */
	public void addWorkCondition() throws CodeRepeatException {
		workCondition.setEnterpriseCode(Constants.ENTERPRISE_CODE);
		workCondition.setConditionId(null);
		workCondition.setModifyBy(employee.getWorkerCode());
		try {
			// 增加一条工作条件记录
			remote.save(workCondition);

			write(Constants.ADD_SUCCESS);
		} catch (CodeRepeatException e) {
			write(Constants.ADD_FAILURE);
			throw e;
		} finally {
			workCondition.setConditionId(new Long(-1));
		}
	}

	/**
	 * 修改工作条件
	 * 
	 * @throws CodeRepeatException
	 */
	public void updateWorkCondition() throws CodeRepeatException {
		// 查找这条工作条件记录
		RunCWorkticketWorkcondition model = remote.findById(workCondition
				.getConditionId());
		// 工作条件名称
		model.setConditionName(workCondition.getConditionName());
		model.setModifyBy(employee.getWorkerCode());
		// 显示顺序
		model.setOrderBy(workCondition.getOrderBy());

		try {
			// 修改这条工作条件记录
			remote.update(model);

			write(Constants.MODIFY_SUCCESS);
		} catch (CodeRepeatException e) {
			write(Constants.MODIFY_FAILURE);
			throw e;
		}
	}

	/**
	 * 删除工作条件
	 */
	public void deleteWorkCondition() {
		// 从请求中获得删除的ID
		String strId = request.getParameter("ids");		
		if (strId != null && strId.trim().length() > 0) {
			// 删除工作条件记录
			remote.deleteMutil(strId);
		}
		write(Constants.DELETE_SUCCESS);
	}
}
