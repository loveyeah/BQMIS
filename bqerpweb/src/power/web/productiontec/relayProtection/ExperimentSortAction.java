package power.web.productiontec.relayProtection;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.relayProtection.PtJdbhCSylbwh;
import power.ejb.productiontec.relayProtection.PtJdbhCSylbwhFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 实验类别维护
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class ExperimentSortAction extends AbstractAction{

	private PtJdbhCSylbwhFacadeRemote sortRemote;
	private PtJdbhCSylbwh sort;
	public PtJdbhCSylbwh getSort() {
		return sort;
	}
	public void setSort(PtJdbhCSylbwh sort) {
		this.sort = sort;
	}
	public ExperimentSortAction()
	{
		sortRemote=(PtJdbhCSylbwhFacadeRemote)factory.getFacadeRemote("PtJdbhCSylbwhFacade");
	}
	
	public void findExperimentSortList() throws JSONException
	{
		Object objstart = request.getParameter("start");
		
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object =sortRemote.findAll(employee.getEnterpriseCode(), start,limit);
		} else {
			
			object = sortRemote.findAll(employee.getEnterpriseCode());
		}
		
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	public void addExperimentSortInfo()
	{
		sort.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			sortRemote.save(sort);
		} catch (CodeRepeatException e) {
			write("{success : true,msg :'"+e.getMessage()+"'}");
		}
		write("{success : true,msg :'增加成功！'}");
	}
	public void updateExperimentSortInfo()
	{
		PtJdbhCSylbwh model=sortRemote.findById(sort.getSylbId());
		model.setDisplayNo(sort.getDisplayNo());
		model.setSylbName(sort.getSylbName());
		try {
			sortRemote.update(model);
		} catch (CodeRepeatException e) {
			write("{success : true,msg :'"+e.getMessage()+"'}");
		}
		write("{success : true,msg :'修改成功！'}");
	}
	
	public void deleteExperimentSortInfo()
	{
		String ids=request.getParameter("ids");
		sortRemote.deleteMulti(ids);
		write("{success : true,msg :'删除成功！'}");
	}
	
}
