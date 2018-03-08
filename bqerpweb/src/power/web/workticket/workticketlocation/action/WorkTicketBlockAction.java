/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.web.workticket.workticketlocation.action;

import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCBlockFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
/**
 * 工作票区域维护Action
 * @author 黄维杰
 * @version 1.0
 */
public class WorkTicketBlockAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 远程服务 */
	protected EquCBlockFacadeRemote remote;
	
	/**
	 * 构造函数，初始化remote
	 */
	public WorkTicketBlockAction() {
		remote =  (EquCBlockFacadeRemote) factory
		.getFacadeRemote("EquCBlockFacade");
	}
	/**
	 * 获得所属系统
	 * @throws Exception
	 */
	public void getBelongSystem() throws Exception {
		PageObject  result= remote.findEquList(Constants.BLANK_STRING, Constants.ENTERPRISE_CODE);
		// 序列化为JSON对象的字符串形式
		String str = JSONUtil.serialize(result);
		// 以html方式输出字符串
		write(str);
	}
}
