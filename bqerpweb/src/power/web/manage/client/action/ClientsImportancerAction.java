package power.web.manage.client.action;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.client.ConCClientsImportance;
import power.ejb.manage.client.ConCClientsImportanceFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 合作伙伴重要程度维护
 * @author fyyang 090615
 *
 */
public class ClientsImportancerAction extends AbstractAction {
	
	private ConCClientsImportanceFacadeRemote importanceRemote;
	private ConCClientsImportance imp;

	public ClientsImportancerAction() {
		importanceRemote = (ConCClientsImportanceFacadeRemote)factory.getFacadeRemote("ConCClientsImportanceFacade");
	}
	
	/**
	 * 根据输入条件查询合作伙伴重要性信息
	 */
	public void getClientsImportanceList() throws JSONException {
		//取得模糊查询条件
		String importanceName = request.getParameter("fuzzytext");
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
			object = importanceRemote.findAll(importanceName,employee.getEnterpriseCode(), start, limit);
		} else {
			// 查询
			object = importanceRemote.findAll(importanceName,employee.getEnterpriseCode());
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
	 * 增加一条合作伙伴重要性信息
	 */
	public void addClientsImportanceInfo()
	{
		imp.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			importanceRemote.save(imp);
			write("{success:true,msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 修改一条合作伙伴重要性信息
	 */
	public void updateClientsImportanceInfo()
	{
		ConCClientsImportance model=importanceRemote.findById(imp.getImportanceId());
		model.setImportanceName(imp.getImportanceName());
		model.setMemo(imp.getMemo());
		try {
			importanceRemote.update(model);
			write("{success:true,msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 删除一条或多条合作伙伴重要性信息
	 */
	public void deleteClientsImportanceInfo()
	{
		String ids=request.getParameter("ids");
		importanceRemote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public ConCClientsImportance getImp() {
		return imp;
	}

	public void setImp(ConCClientsImportance imp) {
		this.imp = imp;
	}

}
