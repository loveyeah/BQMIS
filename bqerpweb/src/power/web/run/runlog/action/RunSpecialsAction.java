package power.web.run.runlog.action;

import java.util.List;

import power.ejb.equ.base.EquCEquipments;
import power.ejb.equ.base.EquCLocation;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.ejb.run.runlog.shift.RunCUnitprofession;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;


public class RunSpecialsAction extends AbstractAction{

	private String SpecialityId;
	private static final long serialVersionUID = 1L;
	private RunCSpecialsFacadeRemote remote;
	
	
//	private Long node;
	private RunCSpecials spe; 
	private String catalogStr;
	private String code;

	/**
	 * 构造函数
	 */

	public RunSpecialsAction()
	{
		remote = (RunCSpecialsFacadeRemote)factory.getFacadeRemote("RunCSpecialsFacade");
	}
	
	public void addRunSpecials()
	{
		if(remote.existsByCode(spe.getSpecialityCode(), employee.getEnterpriseCode())==false)
	    {
		spe.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(spe);        
		write("{success:true}");
		}
		else write("{failure:true}");
	}
	
	public void updateRunSpecials()
	{
		RunCSpecials model = remote.findById(Long.parseLong(SpecialityId));
		model.setSpecialityCode(spe.getSpecialityCode());
		model.setSpecialityName(spe.getSpecialityName());
		model.setSpecialityType(spe.getSpecialityType());
		model.setDisplayNo(spe.getDisplayNo());
		if(!remote.existsByCode(spe.getSpecialityCode(), employee.getEnterpriseCode(), model.getSpecialityId()))
	    {
		remote.update(model);
		write("{success:true}");
	    }
		else write("{failure:true}");
	}
	
	public void deleteRunSpecials()
	{
		RunCSpecials model = remote.findById(Long.parseLong(SpecialityId));
		model.setIsUse("N");
		remote.update(model);
		write("{success:true}");	
	}
	
	public void findRunSpecials() throws JSONException
	{
//		RunCSpecials model = remote.findById(node); 
//		write(JSONUtil.serialize(model));
	   
		String code=request.getParameter("id");
	    StringBuffer JSONStr = new StringBuffer();
	    JSONStr.append("[");
		List<RunCSpecials> list= remote.getListByParent(code, employee.getEnterpriseCode());
		
	    if(list!=null)
	    {
	         for (int i = 0; i < list.size(); i++)
	         {
	        	 RunCSpecials spe= list.get(i);
	    	     boolean isLeaf = remote.isParentNode(spe.getSpecialityCode(), employee.getEnterpriseCode());
	    	     String icon=isLeaf?"file":"folder";
	    	     JSONStr.append("{id:'"+spe.getSpecialityCode()+"',text:'"+spe.getSpecialityName()+"',leaf:"+isLeaf+",iconCls:'"+icon+"'},");
	         }
	     	if (JSONStr.length() > 1) {
				JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
			}
	         
	    }
	    JSONStr.append("]");
	    write(JSONStr.toString());
		
//	       String str=" [{id:'1',text:'设备',leaf:false}]";
//	       write(str);
	}
		

	public void findRunSpecialsByCode() throws JSONException
	{
		RunCSpecials model=remote.findByCode(code,employee.getEnterpriseCode() );
		String str=JSONUtil.serialize(model);
		write(str);
	}

	public void findSpecialityList() throws JSONException{
		List<RunCSpecials> list=remote.findSpeList(employee.getEnterpriseCode());
		String str = JSONUtil.serialize(list);
		write(str);
	}
	
	public String getSpecialityId() {
		return SpecialityId;
	}

	public void setSpecialityId(String specialityId) {
		SpecialityId = specialityId;
	}

	public RunCSpecials getSpe() {
		return spe;
	}

	public void setSpe(RunCSpecials spe) {
		this.spe = spe;
	}

	public String getCatalogStr() {
		return catalogStr;
	}

	public void setCatalogStr(String catalogStr) {
		this.catalogStr = catalogStr;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	} 
}
