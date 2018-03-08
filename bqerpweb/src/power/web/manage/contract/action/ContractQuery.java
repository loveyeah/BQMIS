package power.web.manage.contract.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJContractInfoFacade;
import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;
import power.ejb.manage.contract.business.ConJModifyFacadeRemote;
import power.web.comm.AbstractAction;
/**
 *功能：查询所有合同
 * @author qxjiao
 *
 */
public class ContractQuery extends AbstractAction {
	private ConJContractInfoFacadeRemote remote ;
	
	public ContractQuery(){
		remote = (ConJContractInfoFacadeRemote) factory.getFacadeRemote("ConJContractInfoFacade");
	}
	
	public void findContractList(){
		String con_year = request.getParameter("year");
		String contract_name = request.getParameter("contract_name");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject obj = remote.findContactList(con_year, contract_name, Integer.parseInt(start), Integer.parseInt(limit));
		System.out.println(obj.getList().size());
		try {
			this.write(JSONUtil.serialize(obj));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ConJContractInfoFacadeRemote getRemote() {
		return remote;
	}
	public void setRemote(ConJContractInfoFacadeRemote remote) {
		this.remote = remote;
	}
	
}
