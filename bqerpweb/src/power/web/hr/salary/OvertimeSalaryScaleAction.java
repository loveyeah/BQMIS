package power.web.hr.salary;

import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCOvertimeSalaryScale;
import power.ejb.hr.salary.HrCOvertimeSalaryScaleFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class OvertimeSalaryScaleAction extends AbstractAction{

	private HrCOvertimeSalaryScaleFacadeRemote remote;
	private HrCOvertimeSalaryScale scale;
	
	public OvertimeSalaryScaleAction()
	{
		remote = (HrCOvertimeSalaryScaleFacadeRemote)factory.getFacadeRemote("HrCOvertimeSalaryScaleFacade");
	}
	
	/**
	 * 批量保存
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void saveRecord() throws JSONException
	{
		String str = request.getParameter("isUpdate");
		
		Object obj = JSONUtil.deserialize(str);
		 
		if (str!=null && !str.trim().equals(""))
		{
			remote.saveOvertimeSalaryRecord((List<Map>) obj);
		} 
		write("{success: true,msg:'保存成功！'}");
	}

	/**
	 * 重置方法
	 */
	public void resetReocord()
	{
		String ids = request.getParameter("ids");
		remote.deleteAndAddRecord(ids, employee.getEnterpriseCode());
		write("{success:true,msg:'操作成功！'}");
	}
	
	/**
	 * 查找符合条件的加班工资系数记录
	 * @throws JSONException
	 */
	public void findSalaryScaleList() throws JSONException
	{
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findSalaryScaleList(employee.getEnterpriseCode(), start,limit);
		} else {
			object = remote.findSalaryScaleList(employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	
	public HrCOvertimeSalaryScale getScale() {
		return scale;
	}
	public void setScale(HrCOvertimeSalaryScale scale) {
		this.scale = scale;
	}
}
