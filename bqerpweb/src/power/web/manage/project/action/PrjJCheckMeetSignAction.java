package power.web.manage.project.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.BpJPlanRepairDetail;
import power.ejb.manage.project.PrjJCheckMeetSign;
import power.ejb.manage.project.PrjJCheckMeetSignFacadeRemote;
import power.ejb.manage.project.PrjJEquUse;
import power.ejb.manage.project.PrjJEquUseFacadeRemote;
import power.web.comm.AbstractAction;

public class PrjJCheckMeetSignAction extends AbstractAction {
	private  PrjJCheckMeetSignFacadeRemote  remote;
	private  PrjJEquUseFacadeRemote  remoteB;
	private  PrjJCheckMeetSign  prjMeetSign;
	private  PrjJEquUse  equUse;
	

	public PrjJCheckMeetSignAction() {
		
		remote = (PrjJCheckMeetSignFacadeRemote) factory.getFacadeRemote("PrjJCheckMeetSignFacade");
		remoteB = (PrjJEquUseFacadeRemote) factory.getFacadeRemote("PrjJEquUseFacade");
		
		
	}
	public  void getPrjCheckMeetSign() throws JSONException
	{
		PageObject result=new PageObject();
		String conName=request.getParameter("conName");
		String contractorName=request.getParameter("contractorName");
		String flag=request.getParameter("flag");
		
		Object str= request.getParameter("start");
		Object lim= request.getParameter("limit");
		int start=0;
		int limit=0;
		
		if(str!=null&&lim!=null)
		{
			start=Integer.parseInt(str.toString());
			limit=Integer.parseInt(lim.toString());
		result=remote.getPrjCheckMeetSign(conName,contractorName,employee.getEnterpriseCode(),employee.getWorkerCode(),flag,start,limit );
		}else
		{
			result=remote.getPrjCheckMeetSign(conName,contractorName,employee.getEnterpriseCode(),employee.getWorkerCode(),flag );
		}
		
		if (result != null) {
			write(JSONUtil.serialize(result));
			
		} else {
			write("{totalCount : 0,list :[]}");
		}
	}
	public  void  addPrjCheckMeetSign()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		
		String projectNo = remote.findProjectNo(employee.getDeptName(), format.format(new Date()).substring(2,6));
		if(prjMeetSign.getReportCode()==null||"".equals(prjMeetSign.getReportCode()))
		{
			prjMeetSign.setReportCode(projectNo);
			
			
		}else
		{
			prjMeetSign.setReportCode(prjMeetSign.getReportCode());
		}
		prjMeetSign.setApplyBy(employee.getWorkerCode());
		prjMeetSign.setApplyDate(new Date());
		prjMeetSign.setIsUse("Y");
		prjMeetSign.setEnterpriseCode(employee.getEnterpriseCode());
		Long id=remote.save(prjMeetSign);
		
		write("{success:true,msg:'增加成功!',id:'"+id+"'}");
		
		
	}
	public  void  updatePrjCheckMeetSign()
	{
	
		PrjJCheckMeetSign  model=remote.findById(prjMeetSign.getCheckSignId());
		//------------补充录入--------------
	  String flag=request.getParameter("flag");
		
		if(flag!=null&&!"".equals(flag))
		{
			model.setAuditCheckBy(prjMeetSign.getAuditCheckBy());
			model.setAuditCheckDate(prjMeetSign.getAuditCheckDate());
			model.setAuditCheckText(prjMeetSign.getAuditCheckText());
			model.setEquCheckBy(prjMeetSign.getEquCheckBy());
			model.setEquCheckDate(prjMeetSign.getEquCheckDate());
			model.setEquCheckText(prjMeetSign.getEquCheckText());
			model.setFinanceCheckBy(prjMeetSign.getFinanceCheckBy());
			model.setFinanceCheckDate(prjMeetSign.getFinanceCheckDate());
			model.setFinanceCheckText(prjMeetSign.getFinanceCheckText());
			
			model.setSafetyCheckBy(prjMeetSign.getSafetyCheckBy());
			model.setSafetyCheckDate(prjMeetSign.getSafetyCheckDate());
			model.setSafetyCheckText(prjMeetSign.getSafetyCheckText());
			model.setManageCheckBy(prjMeetSign.getManageCheckBy());
			model.setManageCheckDate(prjMeetSign.getManageCheckDate());
			model.setManageCheckText(prjMeetSign.getManageCheckText());
			model.setIsBackEntry("Y");
			model.setBackEnrtyDate(new Date());
			model.setBackEntryBy(employee.getWorkerCode());
		}else
		{//填写页面录入
			model.setConId(prjMeetSign.getConId());
			model.setConName(prjMeetSign.getConName());
			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
			
			String projectNo = remote.findProjectNo(employee.getDeptName(), format.format(new Date()).substring(2,6));
			if(prjMeetSign.getReportCode()==null||"".equals(prjMeetSign.getReportCode()))
			{
				model.setReportCode(projectNo);
				
				
			}else
			{
				model.setReportCode(prjMeetSign.getReportCode());
			}
			model.setBudgetCost(prjMeetSign.getBudgetCost());
			model.setContractorId(prjMeetSign.getContractorId());
			model.setContractorName(prjMeetSign.getContractorName());
			model.setOwner(prjMeetSign.getOwner());
			model.setStartDate(prjMeetSign.getStartDate());
			model.setEndDate(prjMeetSign.getEndDate());
			model.setChangeInfo(prjMeetSign.getChangeInfo());
			model.setDevolveOnInfo(prjMeetSign.getDevolveOnInfo());
			model.setMemo(prjMeetSign.getMemo());
			model.setPrjId(prjMeetSign.getPrjId());
			model.setApplyBy(employee.getWorkerCode());
			model.setApplyDate(new Date());
			
		}
		
		//------------------------------
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	};
	public  void  delPrjCheckMeetSign()
	{
		String ids=request.getParameter("checkSignId");
		remote.delCheckMeetSign(ids);
		write("{success:true,msg:'删除成功！'}");
		
	}
	
	//----------设备使用部门表的操作------------
	
	public  void getEquDept() throws JSONException
	{
		PageObject result=new PageObject();
		String id=request.getParameter("checkSignId");
		Long ID=0L;
		if(id!=null&&!"".equals(id))
		{
			ID=Long.parseLong(id);
		}
		result=remoteB.getEquDept(ID);
		if (result != null) {
//			System.out.println("the result"+JSONUtil.serialize(result));
			write(JSONUtil.serialize(result));
			
		} else {
			write("{totalCount : 0,list :[]}");
		}
		
		
		
	}
	public  void  saveEquDept() throws JSONException, java.text.ParseException
	{
		 String   isCheck=request.getParameter("isCheck");
         Object   checkSignId=request.getParameter("checkSignId");
        
		String str = request.getParameter("isUpdate");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<PrjJEquUse> addList = null;
		List<PrjJEquUse> updateList = null;
		if (list != null && list.size() > 0) {
			addList = new ArrayList<PrjJEquUse>();
			updateList = new ArrayList<PrjJEquUse>();
			for (Map data : list) {
				
				String id = null;
				String deptId = null;
				String checkText=null;
				String signBy=null;
				String signDate=null;

				if (data.get("id") != null&&!"".equals(data.get("id")))
					id = data.get("id").toString();


				if (data.get("deptId") != null&&!"".equals(data.get("deptId")))
					deptId = data.get("deptId").toString();
				if(isCheck!=null&&!"".equals(isCheck)&&("Y".equals(isCheck)))
				{
					if (data.get("checkText") != null&&!"".equals(data.get("checkText")))
						checkText = data.get("checkText").toString();

					if (data.get("signBy") != null&&!"".equals(data.get("signBy")))
						signBy = data.get("signBy").toString();
					if (data.get("signDate") != null&&!"".equals(data.get("signDate")))
						signDate = data.get("signDate").toString();
					
				}
				
				PrjJEquUse model = new PrjJEquUse();
				if (id == null) {

					if (checkSignId != null)
						model.setCheckSignId(Long.parseLong(checkSignId.toString()));

					if (deptId != null)
						model.setDeptId(Long.parseLong(deptId));
					model.setEntryBy(employee.getWorkerCode());
					model.setEntryDate(new Date());
					model.setIsUse("Y");
					model.setEnterpriseCode(employee.getEnterpriseCode());
					addList.add(model);
				} else {
					model = remoteB.findById(Long.parseLong(id));
					if(isCheck!=null&&!"".equals(isCheck))
					{
						if(checkText!=null)
						model.setCheckText(checkText);
						if(signBy!=null)
						model.setSignBy(signBy);
						if(signDate!=null)
						{
						SimpleDateFormat  df=new SimpleDateFormat("yyyy-MM-dd");
						model.setSignDate(df.parse(signDate));	
						}
						updateList.add(model);
						
					}else
					{
					
					if (checkSignId != null)
						model.setCheckSignId(Long.parseLong(checkSignId.toString()));

					if (deptId != null)
					model.setDeptId(Long.parseLong(deptId));
					model.setEntryBy(employee.getWorkerCode());
					model.setEntryDate(new Date());
					model.setIsUse("Y");
					model.setEnterpriseCode(employee.getEnterpriseCode());
					model.setEnterpriseCode(employee.getEnterpriseCode());
					updateList.add(model);
				  }
				}
			}
		}
		
			remoteB.saveEquDept(addList, updateList);
			
			write("{success:true,msg:'操作成功！'}");

		
	
	}
	
	public  void  delEquDept()
	{
		String ids=request.getParameter("ids");
		remoteB.delEquUse(ids);
		write("{success:true,msg:'删除成功！'}");
		
	}
	
	//-------------------------------
	public  void  getPrjCheckMeetSignById() throws JSONException
	{
		String checkSignId=request.getParameter("checkSignId");
		PageObject   model=remote.findCheckSignById(Long.parseLong(checkSignId),employee.getEnterpriseCode());
//		System.out.print("the model"+JSONUtil.serialize(model));
		write(JSONUtil.serialize(model));
		
		
		
	}
	//-----------------------------
	public PrjJCheckMeetSign getPrjMeetSign() {
		return prjMeetSign;
	}
	public void setPrjMeetSign(PrjJCheckMeetSign prjMeetSign) {
		this.prjMeetSign = prjMeetSign;
	}
	public PrjJEquUse getEquUse() {
		return equUse;
	}
	public void setEquUse(PrjJEquUse equUse) {
		this.equUse = equUse;
	}
	
	
	//-----------------------------------------

}
