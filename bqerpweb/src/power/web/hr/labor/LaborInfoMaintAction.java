package power.web.hr.labor;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.labor.HrCLaborClass;
import power.ejb.hr.labor.HrCLaborMaterial;
import power.ejb.hr.labor.LaborInfoMaint;
import power.ejb.hr.salary.HrCSalaryPoint;
import power.web.comm.AbstractAction;

public class LaborInfoMaintAction extends AbstractAction{
	private static final String String = null;
	private LaborInfoMaint maintRemote;
	private HrCLaborClass laborClass;
	private HrCLaborMaterial laborMaterial;
	private String ids;
	private int start;
	private int limit;
	public LaborInfoMaintAction()
	{
		maintRemote=(LaborInfoMaint)factory.getFacadeRemote("LaborInfoMaintImpl");
	}
	
	/**
	 * 劳保分类信息维护
	 */
	public void saveLaborClassInfo()
	{  
		boolean existFlag=false;
			existFlag=maintRemote.checkClassInput(laborClass);
			if(existFlag==true){
				write("{success:true,existFlag:"+existFlag+"}");
			}
			else{
		laborClass.setEnterpriseCode(employee.getEnterpriseCode());
		maintRemote.saveLaborClass(laborClass);
		write("{success:true,msg:'增加成功！'}");
	  }
	}
	
    public void updateLaborClassInfo() throws CodeRepeatException
    {
          boolean existFlag=false;
    		existFlag=maintRemote.checkClassInput(laborClass);
    		if(existFlag==true){
    			write("{success:true,existFlag:"+existFlag+"}");
    		}
    		else{
    	HrCLaborClass entity = maintRemote.findByClassId(laborClass.getLaborClassId());
    	laborClass.setIsUse(entity.getIsUse());
    	laborClass.setEnterpriseCode(entity.getEnterpriseCode());
		maintRemote.updateLaborClass(laborClass);
		write("{success:true,msg:'修改成功！'}");
      }
    }
    
    public void deleteLaborClassInfo()
    {
    	
    	String ids=request.getParameter("classIds");
    	boolean existFlag=false;
    	existFlag=maintRemote.checkDelete(ids);
    	if(existFlag==true){
    		write("{success:true,existFlag:"+existFlag+"}");
    	}else{
		maintRemote.deleteLaborClass(ids);
		write("{success:true,msg:'删除成功！'}");
      }
    }
    
    public void findLaborClassList() throws JSONException
    {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String codeOrName=request.getParameter("codeOrName");
		PageObject pg = null;
		if(start != null && limit != null&&start.equals("null"))
			pg = maintRemote.findLaborClassList(codeOrName,employee.getEnterpriseCode(), Integer.parseInt(start),
					Integer.parseInt(limit));
		else 
			pg = maintRemote.findLaborClassList(codeOrName,employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
    
    /**
     * 劳保用品信息维护
     */
    public void saveLaborMaterialInfo()
    {  
    boolean existFlag=false;
	existFlag=maintRemote.checkMaterialInput(laborMaterial);
	if(existFlag==true){
		write("{success:true,existFlag:"+existFlag+"}");
	}
	else{
    	laborMaterial.setEnterpriseCode(employee.getEnterpriseCode());
		maintRemote.saveLaborMaterial(laborMaterial);
		write("{success:true,msg:'增加成功！'}");
	}
    }
    
    public void updateLaborMaterialInfo()
    {
     boolean existFlag=false;
    	existFlag=maintRemote.checkMaterialInput(laborMaterial);
    	if(existFlag==true){
    	write("{success:true,existFlag:"+existFlag+"}");
    	}
    	else{      
    	HrCLaborMaterial entity = maintRemote.findByMaterialId(laborMaterial.getLaborMaterialId());
    	laborMaterial.setIsUse("Y");
    	laborMaterial.setEnterpriseCode(entity.getEnterpriseCode());
		maintRemote.updateLaborMaterial(laborMaterial);
		write("{success:true,msg:'修改成功！'}");
    	}
	
    }
    
    public void deleteLaborMaterialInfo()
    {
    	String ids=request.getParameter("materialIds");
		maintRemote.deleteLaborMaterial(ids);
		write("{success:true,msg:'删除成功！'}");
    }
    
    public void findLaborMaterialList() throws JSONException
    {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String laborMaterialName=request.getParameter("laborMaterialName");
		String laborClass=request.getParameter("laborClass");
		String recieveKind=request.getParameter("recieveKind");
		PageObject pg = null;
		if(start != null && limit != null&&!start.equals(""))
			pg = maintRemote.findLaborMaterialList(laborMaterialName,laborClass,recieveKind,employee.getEnterpriseCode(), Integer.parseInt(start),
					Integer.parseInt(limit));
		else 
			pg = maintRemote.findLaborMaterialList(laborMaterialName,laborClass,recieveKind,employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	
    }

	

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public HrCLaborClass getLaborClass() {
		return laborClass;
	}

	public void setLaborClass(HrCLaborClass laborClass) {
		this.laborClass = laborClass;
	}

	public HrCLaborMaterial getLaborMaterial() {
		return laborMaterial;
	}

	public void setLaborMaterial(HrCLaborMaterial laborMaterial) {
		this.laborMaterial = laborMaterial;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}


}
