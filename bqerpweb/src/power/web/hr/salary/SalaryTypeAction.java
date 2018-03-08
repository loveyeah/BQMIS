package power.web.hr.salary;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCSalaryType;
import power.ejb.hr.salary.HrCSalaryTypeFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SalaryTypeAction extends AbstractAction{
  private HrCSalaryTypeFacadeRemote remote;
  private HrCSalaryType salaryType;
  public HrCSalaryType getSalaryType() {
		return salaryType;
	}
	public void setSalaryType(HrCSalaryType salaryType) {
		this.salaryType = salaryType;
	}
  public SalaryTypeAction()
  {
	  remote=(HrCSalaryTypeFacadeRemote)factory.getFacadeRemote("HrCSalaryTypeFacade");
  }
  /**
   * 查询工资类别信息列表
   * @throws JSONException
   */
   public void findSalaryTypeList() throws JSONException
   {
	   String isBasicData = request.getParameter("isBasicData");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findAll(isBasicData, employee.getEnterpriseCode(),start,limit);
		} else {
			object = remote.findAll(isBasicData,employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
   }
   
   /**
    * 增加一条工资类别信息
    */
   public void addSalaryTypeInfo()
   {
	   salaryType.setEnterpriseCode(employee.getEnterpriseCode());
	   salaryType.setModifyBy(employee.getWorkerCode());
	   try {
		remote.save(salaryType);
		 write("{success:true,msg:'增加成功！'}");
	} catch (CodeRepeatException e) {
		write("{success:true,msg:'"+e.getMessage()+"'}");
	}
	  
   }
   
   /**
    * 修改一条工资类别信息
    */
   public void updateSalaryTypeInfo()
   {
	   HrCSalaryType model=remote.findById(salaryType.getSalaryTypeId());
	   model.setIsNeed(salaryType.getIsNeed());
	   model.setModifyBy(employee.getWorkerCode());
	   model.setSalaryTypeName(salaryType.getSalaryTypeName());
	   try {
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	} catch (CodeRepeatException e) {
		write("{success:true,msg:'"+e.getMessage()+"'}");
	}
	   
   }
   
   /**
    * 删除一条或多条工资类别信息
    */
   public void deleteSalaryTypeInfo()
   {
	   String ids=request.getParameter("ids");
		remote.delete(ids);
		write("{success:true,msg:'删除成功！'}");
   }
   
   

}
