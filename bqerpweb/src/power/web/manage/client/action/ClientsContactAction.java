package power.web.manage.client.action;

import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.client.ConJClientsContact;
import power.ejb.manage.client.ConJClientsContactFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

@SuppressWarnings("serial")
public class ClientsContactAction extends AbstractAction {
	
	private ConJClientsContactFacadeRemote clientsContactRemote;
	
	private ConJClientsContact base;
	
	public ClientsContactAction() {
		clientsContactRemote = (ConJClientsContactFacadeRemote)factory.getFacadeRemote("ConJClientsContactFacade");
	}
	
	/**
	 * 保存一条合作伙伴联系人信息
	 */
	public void saveClientsContact() {
		base.setEnterpriseCode(employee.getEnterpriseCode());
		base.setLastModifiedBy(employee.getWorkerCode());
		clientsContactRemote.save(base); 
		write(Constants.ADD_SUCCESS);	
	}
	
	/**
	 * 修改一条合作伙伴联系人信息
	 */
	public void updateClientsContact() {
		String contactId = request.getParameter("contactId");
		base.setContactId(Long.parseLong(contactId));
		base.setEnterpriseCode(employee.getEnterpriseCode());
		base.setLastModifiedBy(employee.getWorkerCode());
		base.setLastModifiedDate(new Date());
		clientsContactRemote.update(base); 
		write(Constants.MODIFY_SUCCESS);
		
	}
	
	/**
	 * 删除合作伙伴联系人信息
	 */
	public void daleteClientsContact() {
		String contactId = request.getParameter("contactId");
		int exeRow = clientsContactRemote.delete(contactId);
		if(exeRow >0) {
			write(Constants.DELETE_SUCCESS);
		} else {
			write(Constants.DELETE_FAILURE);
		}
	}
	
	/**
	 * 查询合作伙伴联系人信息
	 * @return
	 */
	public void getClientsContactList() throws JSONException {
		String contactNameOrclientName = request.getParameter("fuzzyText");
		String clientId = request.getParameter("cliendId"); 
		// 取得查询条件: 开始行
		Object objstart = request.getParameter("start");
		// 取得查询条件：结束行
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		// 判断是否为null
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			// 查询
			object = clientsContactRemote.findAll(contactNameOrclientName, employee.getEnterpriseCode(), clientId, start, limit);
		} else {
			// 查询
			object = clientsContactRemote.findAll(contactNameOrclientName, employee.getEnterpriseCode(), clientId);
		}
		// 输出结果
		String strOutput = "";
		// 查询结果为null
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
			// 不为null
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	/**
	 * @return the clientsContactRemote
	 */
	public ConJClientsContactFacadeRemote getClientsContactRemote() {
		return clientsContactRemote;
	}

	/**
	 * @param clientsContactRemote the clientsContactRemote to set
	 */
	public void setClientsContactRemote(
			ConJClientsContactFacadeRemote clientsContactRemote) {
		this.clientsContactRemote = clientsContactRemote;
	}

	/**
	 * @return the base
	 */
	public ConJClientsContact getBase() {
		return base;
	}

	/**
	 * @param base the base to set
	 */
	public void setBase(ConJClientsContact base) {
		this.base = base;
	}
	
	

}
