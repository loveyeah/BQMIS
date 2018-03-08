/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.workticket.safetyitemmaint.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketType;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 工作票安全项目措施维护Action
 * @author zhangqi
 */
public class WorkticketTypeAction extends AbstractAction {
	/**远程服务*/
	protected RunCWorkticketTypeFacadeRemote remote;
	
	/**
	 * 构造函数，初始化remote
	 */
	public WorkticketTypeAction() {
		remote =  (RunCWorkticketTypeFacadeRemote) factory
		.getFacadeRemote("RunCWorkticketTypeFacade");
	}
	
	/**
	 * 获得工作票类型
	 */
	public void getWorkticketType() throws Exception {		
			PageObject obj = new PageObject();
			obj = remote.findAll(Constants.ENTERPRISE_CODE);
		    // 记录
			String str=JSONUtil.serialize(obj);
			write(str);
	}	
}
