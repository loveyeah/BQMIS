package power.web.productiontec.dependability.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxCFjcode;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxCFjcodeFacadeRemote;
import power.web.comm.AbstractAction;

public class FjCodeMaintAction extends AbstractAction{
	private PtKkxCFjcodeFacadeRemote  codeRemote;
	private PtKkxCFjcode code;
	public PtKkxCFjcode getCode() {
		return code;
	}
	public void setCode(PtKkxCFjcode code) {
		this.code = code;
	}
	public FjCodeMaintAction()
	{
		codeRemote=(PtKkxCFjcodeFacadeRemote)factory.getFacadeRemote("PtKkxCFjcodeFacade");
	}
	
	public void findFjCodeList() throws JSONException
	{
	
		String fjName = request.getParameter("fjName");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			
			object = codeRemote.findAll(fjName,employee.getEnterpriseCode(), start, limit);
		} else {
			object = codeRemote.findAll(fjName,employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
			
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	public void addFjCodeInfo()
	{
		code.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			codeRemote.save(code);
			write("{success:true,msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	public void updateFjCodeInfo()
	{
		PtKkxCFjcode model=codeRemote.findById(code.getFjCode());
		model.setFjName(code.getFjName());
		model.setOrderBy(code.getOrderBy());
		codeRemote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}
	
	public void deleteFjCodeInfo()
	{
		String ids=request.getParameter("ids");
		codeRemote.delete(ids);
		write("{success:true,msg:'删除成功！'}");
	}

}
