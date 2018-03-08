package power.web.hr.salary;

import java.text.ParseException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCBasisSalary;
import power.ejb.hr.salary.HrCBasisSalaryFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BasisSalaryAction extends AbstractAction{

	private HrCBasisSalaryFacadeRemote remote;
	private HrCBasisSalary basis;
	
	public BasisSalaryAction()
	{
		remote = (HrCBasisSalaryFacadeRemote)factory.getFacadeRemote("HrCBasisSalaryFacade");
	}
	
	/**
	 * 增加一条基本工资记录
	 * @throws ParseException
	 */
	public void addBasisSalary() throws ParseException
	{
		String effectStartTime = request.getParameter("effectStartTime");
		String effectEndTime = request.getParameter("effectEndTime");
		java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
		basis.setEffectStartTime(formatter.parse(effectStartTime));
		basis.setEffectEndTime(formatter.parse(effectEndTime));
		basis.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(basis);
		write("{success:true,msg:'增加成功！'}");
	}
	
	/**
	 * 修改一条基本工资记录
	 * @throws ParseException
	 */
	public void updateBasisSalary() throws ParseException
	{
		String effectStartTime = request.getParameter("effectStartTime");
		String effectEndTime = request.getParameter("effectEndTime");
		java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
		
		HrCBasisSalary model = remote.findById(basis.getBasisSalaryId());
		model.setBasisSalary(basis.getBasisSalary());
		model.setEffectEndTime(formatter.parse(effectEndTime));
		model.setEffectStartTime(formatter.parse(effectStartTime));
		model.setMemo(basis.getMemo());
		
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}
	
	/**
	 * 删除一条或多条基本工资记录
	 */
	public void deleteBasisSalary()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 查找所有基本工资记录列表
	 * @throws JSONException
	 */
	public void findBasisSalaryList() throws JSONException
	{
		String sDate = request.getParameter("sDate");
		
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findBaseSalaryList(sDate,employee.getEnterpriseCode(), start,limit);
		} else {
			object = remote.findBaseSalaryList(sDate,employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	
	public HrCBasisSalary getBasis() {
		return basis;
	}
	public void setBasis(HrCBasisSalary basis) {
		this.basis = basis;
	}
	
}
