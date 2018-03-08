package power.web.equ.workbill.action;

import power.ejb.system.SysCUlFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

public class EquJPEUserValidate extends AbstractAction {
	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 用户管理remote*/
	private SysCUlFacadeRemote userRemote;
	/**
	 * 构造函数
	 */
	public EquJPEUserValidate() {
		// 用户管理remote初始化
		userRemote = (SysCUlFacadeRemote) factory.getFacadeRemote("SysCUlFacade");
	}
	
	/**
	 * 获取默认工号
	 */
	public void getDefaultWorkerCode() {
		// 为空则返回
		if(employee == null){
			write(Constants.BLANK_STRING);
			return;
		}
		// 返回登录用户的工号
		write(employee.getWorkerCode());
	}
	/** 用户验证 */
	public void finishedRptCheckUser(){
			// 工号
			String workerCode = request.getParameter("workerCode");
			// 密码
			String loginPwd = request.getParameter("loginPwd");
			// TODO 用户check方法待确定 QA61
			boolean blnOK = userRemote.checkUserRight(Constants.ENTERPRISE_CODE, workerCode, loginPwd);
			write(String.valueOf(blnOK));
	}
}
