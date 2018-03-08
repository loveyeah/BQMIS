/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.workticket.safetyitemmaint.action;

import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCMarkcardType;
import power.ejb.workticket.RunCMarkcardTypeFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 工作票安全项目措施维护Action
 * @author zhangqi
 */
public class RunCMarkcardTypeAction extends AbstractAction {
	/**去所有挂牌内容*/
	private static final String MARKCARD_TYPE_NAME = "%";
	/**挂牌内容服务*/
	protected RunCMarkcardTypeFacadeRemote remote;
	
	/**
	 * 构造函数
	 */
	public RunCMarkcardTypeAction() {
		// 服务初始化
		remote = (RunCMarkcardTypeFacadeRemote) factory
				.getFacadeRemote("RunCMarkcardTypeFacade");
	}
	
	/**
	 * 获得所有的挂牌内容
	 * 
	 * @throws Exception
	 */
	public void getMarkcardType() throws Exception{
		PageObject result = remote.findAll(Constants.ENTERPRISE_CODE, MARKCARD_TYPE_NAME);
		List<RunCMarkcardType> types=   result.getList();
		 StringBuilder sb = new StringBuilder();
			int i = 0;
			for (RunCMarkcardType model : types) {
				i++;
				sb.append( "['").append( model.getMarkcardTypeName() )
					.append( "','").append(model.getMarkcardTypeId()).append("']");
				if (i < types.size()) {
					sb.append( ",");
				}
			}
		write(sb.toString());		
	}
}
