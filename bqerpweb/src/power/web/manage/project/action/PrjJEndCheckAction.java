package power.web.manage.project.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.project.PrjJEndCheck;
import power.ejb.manage.project.PrjJEndCheckFacadeRemote;


import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class PrjJEndCheckAction extends AbstractAction {
	
	private PrjJEndCheck temp;
	private PrjJEndCheckFacadeRemote remote;

	public PrjJEndCheckAction() {
		{
			remote = (PrjJEndCheckFacadeRemote)factory.getFacadeRemote("PrjJEndCheckFacade");		
		}
	}
	
	//增加
	public void addPrjEndCheck() throws ParseException
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		//PrjJEndCheck model = remote.findById(temp.getCheckId());
		String projectNo = remote.findProjectNo(employee.getDeptId().toString(), format.format(new Date()).substring(2,6));
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		temp.setEntryDate(new Date());
		temp.setEntryBy(employee.getWorkerCode());
		if(temp.getReportCode()==null||"".equals(temp.getReportCode())){
			temp.setReportCode(projectNo);
		}else{
			temp.setReportCode(temp.getReportCode());
		}
		temp = remote.save(temp);
		if(temp == null)
		{
			write("{failure:true,msg:'此类型的验证书已存在！'}");
		}
		else
		write("{success:true,id:'"+temp.getCheckId()+"',msg:'增加成功！'}");
	}
	
	//修改
	public void updatePrjEndCheck() throws ParseException
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		String projectNo = remote.findProjectNo(employee.getDeptId().toString(), format.format(new Date()).substring(2,6));
		PrjJEndCheck model = remote.findById(temp.getCheckId());
		model.setConId(temp.getConId());
		model.setConName(temp.getConName());
		model.setContractorId(temp.getContractorId());
		model.setContractorName(temp.getContractorName());
		model.setPrjLocation(temp.getPrjLocation());
		model.setProjectPrice(temp.getProjectPrice());
		if(temp.getReportCode()==null||"".equals(temp.getReportCode())){
			model.setReportCode(projectNo);
		}else{
			model.setReportCode(temp.getReportCode());
		}
		model.setStartDate(temp.getStartDate());
		model.setEndDate(temp.getEndDate());
		model.setCheckDate(temp.getCheckDate());
		model.setQuantities(temp.getQuantities());
		model.setEntryDate(new Date());
		model.setEntryBy(employee.getWorkerCode());
		model.setPrjId(temp.getPrjId());
		
		temp = remote.update(model);
		if(temp == null)
		{
			write("{failure:true,msg:'此类型的验证书已存在！'}");
		}
		else
		write("{success:true,msg:'修改成功！'}");
	}
	
	//删除
	public void deletePrjEndCheck()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	//工程交工竣工验收证书列表查询
	public void findPrjEndCheckList() throws JSONException, ParseException
	{	
		PageObject obj=new PageObject();
		
		String conName = request.getParameter("conName")==null?"":request.getParameter("conName");
		String contractorName = request.getParameter("contractorName")==null?"":request.getParameter("contractorName");
		String flag = request.getParameter("flag");
		
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	   
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findPrjEndCheckList(employee.getWorkerCode(),employee.getEnterpriseCode(), conName, contractorName,flag, start,limit);
	    }
	    else
	    {
	    	obj=remote.findPrjEndCheckList(employee.getWorkerCode(),employee.getEnterpriseCode(), conName, contractorName,flag);
	    }
	    String str=JSONUtil.serialize(obj);
	    System.out.println(str);
		write(str);
		
	}
	
	public void addPrjEndCheckAdditional(){
		System.out.println("checkId================"+request.getParameter("checkId"));
		String checkId = request.getParameter("checkId");
		String projectContent = request.getParameter("projectContent");
		String endInfo = request.getParameter("endInfo");
		String checkText = request.getParameter("checkText");
		String qualityAssess = request.getParameter("qualityAssess");
		String constructChargeBy = request.getParameter("constructChargeBy");
		String contractorChargeBy = request.getParameter("contractorChargeBy");
		
		PrjJEndCheck model = remote.findById(Long.parseLong(checkId));
		model.setProjectContent(projectContent);
		model.setEndInfo(endInfo);
		model.setCheckText(checkText);
		model.setQualityAssess(qualityAssess);
		model.setConstructChargeBy(constructChargeBy);
		model.setContractorChargeBy(contractorChargeBy);
		model.setBackEntryBy(employee.getWorkerName());
		model.setBackEnrtyDate(new Date());
		model.setIsBackEntry("Y");
		
		temp = remote.update(model);
		
	}
	

	public PrjJEndCheck getTemp() {
		return temp;
	}

	public void setTemp(PrjJEndCheck temp) {
		this.temp = temp;
	}
	
	

}
