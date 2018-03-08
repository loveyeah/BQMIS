/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/

package power.web.workticket.markcardmaint.action;

import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCMarkcardTypeFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 标识牌类型取得Action
 * @author jincong
 *
 */
public class RunCMarkcardAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/** 标识牌类型处理远程对象 */
	protected RunCMarkcardTypeFacadeRemote remote;

	public RunCMarkcardAction() {
		// 取得标识牌类型处理远程对象
		remote = (RunCMarkcardTypeFacadeRemote) factory
				.getFacadeRemote("RunCMarkcardTypeFacade");
	}

	/**
	 * 获得所有标识牌类型
	 * @throws JSONException 
	 */
	public void getMarkcard() throws JSONException {
		// 根据企业编码取出所有标识牌类型
		PageObject object = remote.findAll(Constants.ENTERPRISE_CODE, Constants.ALL_DATA);
		String string = JSONUtil.serialize(object);
		write(string);
	}
}
