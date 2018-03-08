package power.web.hr.salary;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCSalaryLevel;
import power.ejb.hr.salary.HrCSalaryLevelFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SalaryLevelAction  extends AbstractAction{

	private HrCSalaryLevelFacadeRemote remote;
	private HrCSalaryLevel level;
	
	public SalaryLevelAction()
	{
		remote = (HrCSalaryLevelFacadeRemote)factory.getFacadeRemote("HrCSalaryLevelFacade");
	}

	/**
	 * 增加一条岗位薪级记录
	 */
	public void addSalaryLevel()
	{
		level.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			remote.save(level);
			write("{success:true,msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 修改一条岗位薪级记录信息
	 */
	public void updateSalaryLevel()
	{
		HrCSalaryLevel model = remote.findById(level.getSalaryLevel());
		model.setSalaryLevelName(level.getSalaryLevelName());
		try {
			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 删除一条或多条岗位薪级记录
	 */
	public void deleteSalaryLevel()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 查找岗位薪级列表信息
	 * @throws JSONException
	 */
	public void findSalaryLevelList() throws JSONException
	{
		String salaryLevelName = request.getParameter("fuzzy");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findSalaryLevelList(employee.getEnterpriseCode(), salaryLevelName, start,limit);
		} else {
			object = remote.findSalaryLevelList(employee.getEnterpriseCode(), salaryLevelName);
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	public HrCSalaryLevel getLevel() {
		return level;
	}

	public void setLevel(HrCSalaryLevel level) {
		this.level = level;
	}
}
