/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.workticket.flag.action;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketFlag;
import power.ejb.workticket.RunCWorkticketFlagFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 标点符号维护Action
 * 
 * @author pbao
 * @version 1.0
 */
public class FlagMaintenanceAction extends AbstractAction {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/** 标点符号远程对象 */
	private RunCWorkticketFlagFacadeRemote remote;
	/** 标点符号 */
	private RunCWorkticketFlag flag;
	/** 标点符号Id */
	private String flagId;

	/**
	 * 取得标点符号
	 * 
	 * @return 标点符号
	 */
	public RunCWorkticketFlag getFlag() {
		return flag;
	}

	/**
	 * 设置标点符号
	 * 
	 * @param flag
	 *            标点符号
	 */
	public void setFlag(RunCWorkticketFlag flag) {
		this.flag = flag;
	}

	/**
	 * @return 标点符号Id
	 */
	public String getFlagId() {
		return flagId;
	}

	/**
	 * @param flagId
	 *            标点符号Id
	 */
	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}

	/**
	 * 构造函数
	 */
	public FlagMaintenanceAction() {
		remote = (RunCWorkticketFlagFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketFlagFacade");
	}

	/**
	 * 获得标点符号列表
	 * 
	 * @throws JSONException
	 */
	public void getFlagList() throws JSONException {
		String fuzzy = "";
		// 取得查询参数
		Object myobj = request.getParameter("fuzzy");
		if (myobj != null) {
			fuzzy = myobj.toString();
		}
		// 查询标点符号列表
		List<RunCWorkticketFlag> workTicketFlagList = new ArrayList<RunCWorkticketFlag>();
		workTicketFlagList = remote.findByNameOrId(Constants.ENTERPRISE_CODE,
				fuzzy);
		PageObject obj = new PageObject();
		obj.setList(workTicketFlagList);

		// 序列化为JSON对象的字符串形式
		String str = JSONUtil.serialize(obj);
		// 以html方式输出字符串
		write(str);
	}

	/**
	 * 增加标点符号
	 * 
	 * @throws CodeRepeatException
	 */
	public void addFlag() throws CodeRepeatException {
		// 企业编码
		flag.setEnterpriseCode(Constants.ENTERPRISE_CODE);
		// 标点符号Id
		flag.setFlagId(null);
		// 添加人
		flag.setModifyBy(employee.getWorkerCode());
		try {
			// 增加一条标点符号记录
			remote.save(flag);
			// 显示成功信息
			write(Constants.ADD_SUCCESS);
		} catch (CodeRepeatException ce) {
			// 显示错误信息
			write(Constants.ADD_FAILURE);
			throw ce;
		} finally {
			flag.setFlagId(new Long(-1));
        }
	}

	/**
	 * 修改标点符号
	 * 
	 * @throws CodeRepeatException
	 */
	public void updateFlag() throws CodeRepeatException {
		// 查询标点符号记录
		RunCWorkticketFlag model = remote.findById(Long.parseLong(flagId));
		// 名称
		model.setFlagName(flag.getFlagName());
		// 显示顺序
		model.setOrderBy(flag.getOrderBy());
		// 修改人
		model.setModifyBy(employee.getWorkerCode());
		try {
			// 修改这条标点符号记录
			remote.update(model);
			write(Constants.MODIFY_SUCCESS);
		} catch (CodeRepeatException ce) {
			write(Constants.MODIFY_FAILURE);
			throw ce;
		}
	}

	/**
	 * 删除标点符号
	 */
	public void deleteFlag() {
		// 取得参数-标点符号Id集
		String ids = request.getParameter("ids");
			if(ids != null && ids.trim().length() > 0) {
			// 删除标点符号
			remote.deleteMulti(ids);
			// 显示删除成功信息
			write(Constants.DELETE_SUCCESS);
		}
	}
}
