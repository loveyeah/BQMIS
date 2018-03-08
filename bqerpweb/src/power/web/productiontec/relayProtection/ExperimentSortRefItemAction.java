package power.web.productiontec.relayProtection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.relayProtection.PtJdbhCLbyxmdyFacadeRemote;
import power.ejb.productiontec.relayProtection.form.PtCLbyxmdyForm;
import power.web.comm.AbstractAction;

/**
 * 类别与项目对应
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class ExperimentSortRefItemAction extends AbstractAction{

	private  PtJdbhCLbyxmdyFacadeRemote refRemote;
	public ExperimentSortRefItemAction(){
		refRemote=(PtJdbhCLbyxmdyFacadeRemote)factory.getFacadeRemote("PtJdbhCLbyxmdyFacade");
	}
	
	public void findExperimentSortRefItemList() throws JSONException
	{
		String sortId=request.getParameter("sortId");
        Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object =refRemote.findAll(employee.getEnterpriseCode(), Long.parseLong(sortId), start,limit);
		} else {
			
			object = refRemote.findAll(employee.getEnterpriseCode(), Long.parseLong(sortId));
		}
		
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	public void saveExperimentSortRefItemInfo() throws NumberFormatException, JSONException
	{
		String sortId=request.getParameter("sortId");
		String selectIds=request.getParameter("selectIds");
		String noselectIds=request.getParameter("noselectIds");
		refRemote.saveRecords(Long.parseLong(sortId), selectIds, noselectIds, employee.getEnterpriseCode());
		write("{success:true}");

	}
	

}
