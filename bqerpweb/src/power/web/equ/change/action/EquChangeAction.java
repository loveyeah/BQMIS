package power.web.equ.change.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCEquipments;
import power.ejb.equ.base.EquCEquipmentsFacadeRemote;
import power.ejb.equ.change.EquCChangesourceFacadeRemote;
import power.ejb.equ.change.EquJChangphoto;
import power.ejb.equ.change.EquJChangphotoFacadeRemote;
import power.ejb.equ.change.EquJEquchang;
import power.ejb.equ.change.EquJEquchangBackfill;
import power.ejb.equ.change.EquJEquchangFacadeRemote;
import power.ejb.hr.labor.HrJLaborTempe;

import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.UploadFileAbstractAction;
@SuppressWarnings("serial")
public class EquChangeAction extends UploadFileAbstractAction{

	private EquJEquchangFacadeRemote remote;
	private EquJEquchang change;
	private String equChangeId;
	private File annex;
	
	public EquJEquchang getChange() {
		return change;
	}
	public void setChange(EquJEquchang change) {
		this.change = change;
	}
	public String getEquChangeId() {
		return equChangeId;
	}
	public void setEquChangeId(String equChangeId) {
		this.equChangeId = equChangeId;
	}
	
	public EquChangeAction()
	{
		remote=(EquJEquchangFacadeRemote)factory.getFacadeRemote("EquJEquchangFacade");
	}
	
	public void findSpecialList() throws JSONException
	{
		RunCSpecialsFacadeRemote specialRemote=(RunCSpecialsFacadeRemote)factory.getFacadeRemote("RunCSpecialsFacade");
		List<RunCSpecials> list=specialRemote.findByType("1", employee.getEnterpriseCode());
		 String str=JSONUtil.serialize(list);
		write(str);
	
	}
	
	public void findChangeModel() throws JSONException
	{
		EquCEquipmentsFacadeRemote equremote=(EquCEquipmentsFacadeRemote)factory.getFacadeRemote("EquCEquipmentsFacade");
		EquCEquipments equmodel=new EquCEquipments();
		Long id=Long.parseLong(request.getParameter("id"));
		EquJEquchang model=remote.findById(id);
		String str=JSONUtil.serialize(model);
		String oldEquName="";
		String newEquName="";
		String deptName="";
		String specialityName="";
		String sourceName="";
		String changeTypeName="";
		String applyManName="";//add by kzhang 20100921 
		if(model.getEquNewCode()!=null&&!model.getEquNewCode().equals(""))
		{
			equmodel=equremote.findByCode(model.getEquNewCode(), employee.getEnterpriseCode());
			if(equmodel!=null)
			{
				newEquName=equmodel.getEquName();
			}
		}
		if(model.getEquOldCode()!=null&&!model.getEquOldCode().equals(""))
		{
			equmodel=equremote.findByCode(model.getEquOldCode(), employee.getEnterpriseCode());
			if(equmodel!=null)
			{
				oldEquName=equmodel.getEquName();
			}
		}
		if (model.getDeptCode() != null&&!model.getDeptCode().equals("")) {
			String deptCode=model.getDeptCode();
			String deptCodes[]=deptCode.split(",");
			for(int i=0;i<deptCodes.length;i++)
			{
				deptName+=remote.getEquChangeDeptName(deptCodes[i])+",";
		    }
			deptName=deptName.substring(0,deptName.lastIndexOf(","));
			
		}
		if (model.getSpecialCode() != null&&!model.getSpecialCode().equals("")) {
			    String specialityCode=model.getSpecialCode();
				specialityName+=remote.getEquSpecialName(specialityCode);
		}
		
		if (model.getChangeType() != null&&!model.getChangeType().equals("")) {
		    String changeTypeCode=model.getChangeType();
		    if(changeTypeCode.equals("1"))
		    	changeTypeName="安装";
		    else if(changeTypeCode.equals("2"))
		    	changeTypeName="改装";
		    else if(changeTypeCode.equals("3"))
		    	changeTypeName="拆除";
		    else if(changeTypeCode.equals("4"))
		    	changeTypeName="改造";
	}
		if (model.getSourceCode() != null&&!model.getSourceCode().equals("")) {
		    String sourceCode=model.getSourceCode();
		    sourceName+=remote.getEquChangeSourceName(sourceCode,employee.getEnterpriseCode());
	}
		if (model.getApplyMan()!=null&&!model.getApplyMan().equals("")) {
			applyManName+=remote.getApplyManName(model.getApplyMan());
		}
		
		 write("{success: true,oldname:'"+oldEquName+"',newname:'"+newEquName+"',deptName:'"+deptName+"'," +
			 		"specialityName:'"+specialityName+"',changeTypeName:'"+changeTypeName+"',sourceName:'"+sourceName+"',applyManName:'"+applyManName+"',data:"+str+"}");
	}
	
	public void findChangeList() throws JSONException, ParseException
	{
		String changeNo="";
		String changeTitle="";
		Object objchangeNo=request.getParameter("changeNo");
		Object objchangeTitle=request.getParameter("changeTitle");
		String flag=request.getParameter("flag");
		String entryIds = "";
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();

		entryIds = workflowService.getAvailableWorkflow(
				new String[] { "bqEquChange" }, employee.getWorkerCode());
		if(objchangeNo!=null)
		{
			changeNo=objchangeNo.toString();
		}
		if(objchangeTitle!=null)
		{
			changeTitle=objchangeTitle.toString();
		}
	    String enterpriseCode=employee.getEnterpriseCode();
	    Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    PageObject obj=new  PageObject();
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			 obj=remote.findChangeList(employee.getDeptCode(),changeNo, changeTitle,flag,entryIds, enterpriseCode, start,limit);
	    }
	    else
	    {
	    	obj=remote.findChangeList(employee.getDeptCode(),changeNo, changeTitle, flag,entryIds,enterpriseCode);
	    }

		 String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	
	public void addChange()
	{
		change.setEnterpriseCode(employee.getEnterpriseCode());
		change.setApplyMan(employee.getWorkerCode());
		 int i=remote.save(change);
			if(i != -1)
			{
				write("{success:true,id:"+i+",msg:'增加成功！'}");
			}
			else
			{
				write("{success:true,msg:'增加失败:编码重复！'}");
			}
	}
	
	public void saveThingAndImage() throws IOException
	{
		String filePath=request.getParameter("front");
		String frontThing=request.getParameter("frontThing");
		String backThing=request.getParameter("backThing");
		
		EquJEquchang model = null;
		String changeId = request.getParameter("changeId") ;
		if(changeId!=null&&!"".equals(changeId)){
			Long id=Long.parseLong(changeId);
			model =remote.findById(id);
		}else{
			model = new EquJEquchang();
		}
		
		if(frontThing!=null &&!frontThing.equals("")){
			model.setFrontThing(frontThing);
		}
		if(backThing!=null &&!backThing.equals("")){
			model.setBackThing(backThing);
		}
	    if(!filePath.equals(""))
	    {
				String result = filePath.substring(filePath
						.lastIndexOf("\\") + 1);
				String fileName = result.replaceAll(" ", "");
				String[] filetemp = fileName.split("\\.");
				if (filetemp[1].equals("txt")) {
					filetemp[1] = ".doc";
					fileName = filetemp[0] + filetemp[1];
				}
				
				String Temp = uploadFile(annex, fileName, "equChange");
				System.out.println("上传的文件名："+Temp);
				model.setAnnex(Temp);
			
	    }
	    if(changeId!=null&&!"".equals(changeId)){
	    	remote.update(model);
	    	write("{success:true,msg:'保存成功！',changeId:'none'}");
	    	
	    }else{
	    	model.setEnterpriseCode(employee.getEnterpriseCode());
	    	model.setApplyMan(employee.getWorkerCode());
	    	int id = remote.save(model);
	    	write("{success:true,msg:'保存成功！',changeId:'"+id+"'}");
	    }
	}
	
	public void saveImage(String changeNo,String frontfilename,String backfilename) throws IOException
	{
		EquJChangphotoFacadeRemote photoremote=(EquJChangphotoFacadeRemote)factory.getFacadeRemote("EquJChangphotoFacade");
		
		EquJChangphoto model=photoremote.findByChangeNo(changeNo, employee.getEnterpriseCode());
		boolean check=true;
		if(model==null)
		{
			model=new EquJChangphoto();
			check=true;
		}
		else
		{
			check=false;
		}
	
		model.setEquChangeNo(changeNo);
	    model.setEnterpriseCode(employee.getEnterpriseCode());
	    if(!frontfilename.equals(""))
	    {
		java.io.FileInputStream frontfis = new java.io.FileInputStream(frontfilename);
		byte[] frontdata = new byte[(int)frontfis.available()];
		frontfis.read(frontdata);
		model.setOldPhoto(frontdata);
	    }
	    /*if(!("").equals(backfilename))
	    {
		java.io.FileInputStream backfis = new java.io.FileInputStream(backfilename);
		byte[] backdata = new byte[(int)backfis.available()];
		backfis.read(backdata);
		model.setNewPhoto(backdata);
	    }*/
	    if(check)
	    {
		photoremote.save(model);
	    }
	    else
	    {
	    photoremote.update(model);
	    }
	}
	
	public void updateChange() 
	{
		String id = request.getParameter("changeId");
		Long changeId = Long.parseLong(id);
		EquJEquchang model=remote.findById(changeId);
		//model.setApplyDate();
		model.setApplyDate(change.getApplyDate());
		model.setApplyMan(employee.getWorkerCode());
		model.setAssetnum(change.getAssetnum());
		model.setChangePlanDate(change.getChangePlanDate());
		model.setChangeReason(change.getChangeReason());
		model.setChangeTitle(change.getChangeTitle());
		model.setEquName(change.getEquName());
		model.setEquChangeNo(change.getEquChangeNo());
		
		if(change.getChangeType()!=null&&!change.getChangeType().equals("")){
			model.setChangeType(change.getChangeType());
		}
		
		if(change.getSourceCode()!=null&&!change.getSourceCode().equals("")){
			model.setSourceCode(change.getSourceCode());
		}
		if(change.getEquNewCode()!=null&&!change.getEquNewCode().equals("")&&!change.getEquNewCode().equals("null")){
			model.setEquNewCode(change.getEquNewCode());
		}
		if(change.getEquOldCode()!=null&&!change.getEquOldCode().equals("")&&!change.getEquOldCode().equals("null")){
			model.setEquOldCode(change.getEquOldCode());
		}
		if(change.getSpecialCode()!=null&&!change.getSpecialCode().equals("")){
			model.setSpecialCode(change.getSpecialCode());
		}
		if(change.getDeptCode()!=null&&!change.getDeptCode().equals("")){
			model.setDeptCode(change.getDeptCode());
		}
		remote.update(model);
		write("{success:true,id:"+model.getEquChangeId()+",msg:'修改成功！'}");
		
	}
	
	public void deleteChange()
	{
		EquJChangphotoFacadeRemote photoremote=(EquJChangphotoFacadeRemote)factory.getFacadeRemote("EquJChangphotoFacade");
		 String ids= request.getParameter("ids");
	     String [] changeids= ids.split(",");
			for(int i=0;i<changeids.length;i++)
			{
				if(!changeids[i].equals(""))
				{
					EquJEquchang model=remote.findById(Long.parseLong(changeids[i]));
					if(model!=null)
					{
						photoremote.deleteByChangeNo(model.getEquChangeNo(),employee.getEnterpriseCode());
					}
					remote.delete(Long.parseLong(changeids[i]));
				}
			}
			
			write("{success:true,msg:'删除成功！'}");
	}
	
	
	public void showphoto() throws Exception{
      EquJChangphotoFacadeRemote photoremote=(EquJChangphotoFacadeRemote)factory.getFacadeRemote("EquJChangphotoFacade");
      String changeNo=request.getParameter("changeNo");
      String method=request.getParameter("method");
		EquJChangphoto model=photoremote.findByChangeNo(changeNo, employee.getEnterpriseCode());
		

		  if(model!=null)
		  {
			  
			  if(method.equals("old"))
			  {
		     byte[] data=model.getOldPhoto();
		     if(data==null||data.length==0){
		    	 model=photoremote.findByChangeNo("HX20101013-01", employee.getEnterpriseCode());
				  data=model.getOldPhoto();
				     response.setContentType("image/jpeg");    
		             OutputStream outs = response.getOutputStream();   
		             for (int i = 0; i < data.length; i++) {   
		      	          outs.write(data[i]);//输出到页面   
		      	           }  
		     }else{
			     response.setContentType("image/jpeg");    
	             OutputStream outs = response.getOutputStream();   
	             for (int i = 0; i < data.length; i++) {   
	      	          outs.write(data[i]);//输出到页面   
	      	           }  

		     }
			  }
			  else
			  {
				  byte[] data=model.getNewPhoto();
				  if(data==null||data.length==0){
				    	 model=photoremote.findByChangeNo("HX20101013-01", employee.getEnterpriseCode());
						  data=model.getOldPhoto();
						     response.setContentType("image/jpeg");    
				             OutputStream outs = response.getOutputStream();   
				             for (int i = 0; i < data.length; i++) {   
				      	          outs.write(data[i]);//输出到页面   
				      	           }  
				     }else{
					     response.setContentType("image/jpeg");    
			             OutputStream outs = response.getOutputStream();   
			             for (int i = 0; i < data.length; i++) {   
			      	          outs.write(data[i]);//输出到页面   
			      	           }  
				     }

			  }
			  
		  }else{
			  model=photoremote.findByChangeNo("HX20101013-01", employee.getEnterpriseCode());
			  byte[] data=model.getOldPhoto();
			     response.setContentType("image/jpeg");    
	             OutputStream outs = response.getOutputStream();   
	             for (int i = 0; i < data.length; i++) {   
	      	          outs.write(data[i]);//输出到页面   
	      	           }  
		  }
	}
	
	 public void getEquChangeNo() {
			String special = request.getParameter("special");
			
			String equChangeNo = "";
			//专业
		     
			Calendar time =  Calendar.getInstance();
			//得到月份
			String formatMonth = null;
			int intMonth = time.get(Calendar.MONTH) + 1;
			if(intMonth < 10){
				formatMonth = "0" + intMonth;
			}else{
				formatMonth = intMonth + "";
			}
			//得到日
			String formatDay = null;
			int intDay = time.get(Calendar.DAY_OF_MONTH) + 1;
			if(time.get(Calendar.DAY_OF_MONTH) < 10){
				formatDay = "0" + time.get(Calendar.DAY_OF_MONTH);
			}else{
				formatDay = time.get(Calendar.DAY_OF_MONTH) + "";
			}
			equChangeNo= remote.getMajorNo(special) + time.get(Calendar.YEAR) + 
			formatMonth + formatDay;
		
			//得到流水号
			int maxId = remote.getMaxNo(equChangeNo,employee.getEnterpriseCode());
			
			String no = null;
			if(maxId < 10){
				no = "0" + (maxId);
			}
			equChangeNo = equChangeNo + '-'+ no;
			
			write(equChangeNo);
		 }
	 
		public void reportEquChange() {

			String equChangeId = request.getParameter("equChangeId");
			String approveText = request.getParameter("approveText");
			String nextRoles = request.getParameter("nextRoles");
			String workflowType = request.getParameter("workflowType");
			String actionId = request.getParameter("actionId");

			remote.reportEquChange(Long.parseLong(equChangeId), Long
					.parseLong(actionId), employee.getWorkerCode(), approveText,
					nextRoles, "", workflowType);
			
			write("{success:true,msg:'上报成功！'}");

		}	
		
		public void approveEquChange() {
			try {
				String equChangeId = request.getParameter("equChangeId");
				String workflowType = request.getParameter("workflowType");
				String actionId = request.getParameter("actionId");
				String eventIdentify = request.getParameter("eventIdentify");
				String approveText = request.getParameter("approveText");
				String nextRoles = request.getParameter("nextRoles");

				EquJEquchang obj = remote.approveEquChange(equChangeId,workflowType, employee.getWorkerCode(), actionId,
						eventIdentify, approveText, nextRoles,employee.getEnterpriseCode());
						write("{success:true,msg:'操作成功！',obj:"
								+ JSONUtil.serialize(obj) + "}");

			} catch (Exception e) {
				write("{failure:true,msg:'" + e.getMessage() + "'}");
			}
		}
		
		
		public void saveBackFill(){
			String equChangeNum = request.getParameter("equChangeNum");
			String dept = request.getParameter("equChangeNum");
			String backEntryBy = request.getParameter("backEntryBy");
			String executeTime = request.getParameter("executeTime");
			String executeStatus = request.getParameter("executeStatus");
			EquJEquchangBackfill model = new EquJEquchangBackfill();
			SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
			Date exeDate = null;
			try {
				exeDate = format.parse(executeTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			model.setEquChangeNo(equChangeNum);
			model.setDeptCode(dept);
			model.setExeDate(exeDate);
			model.setBackfillBy(backEntryBy);
			model.setExeSituation(executeStatus);
			model.setIsBackfill("Y");
			model.setIsUse("Y");
			model.setEnterpriseCode(employee.getEnterpriseCode());
			remote.saveEquJEquchangBackfill(model);
			
			this.write("{success:true,status:'保存成功！'}");
		}
		
		public void findBackFillModel(){
			String changeNo = request.getParameter("changeNo");
			EquJEquchangBackfill model = null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				model = remote.findEquJEquchangBackfill(changeNo);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(model!=null){
				 write("{success: true,status:'data',changeNo:'"+model.getEquChangeNo()+"',dept:'"+model.getDeptCode()+"',backEntry:'"+model.getBackfillBy()+"'," +
					 		"executeTime:'"+format.format(model.getExeDate())+"',executeStatus:'"+model.getExeSituation()+"'}");
			}else{
				write("{success:true,status:'null'}");
			}
			
		}
		/**
		 * 设备异动信息查询
		 * By 设备编码
		 * @throws JSONException 
		 */
		public void findChangeListByEquCode() throws JSONException{
			String equCode=request.getParameter("equCode");
			String objstart=request.getParameter("start");
		    String objlimit=request.getParameter("limit");
		    PageObject page=new PageObject();
		    if (objstart!=null&&!objstart.equals("")&&objlimit!=null&&!objlimit.equals("")) {
				Integer start=Integer.parseInt(objstart);
				Integer limit=Integer.parseInt(objlimit);
				page=remote.findChangeListByEquCode(equCode, employee.getEnterpriseCode(), start,limit);
			}else{
				page=remote.findChangeListByEquCode(equCode, employee.getEnterpriseCode());
			}
		    write(JSONUtil.serialize(page));
		}
		public File getAnnex() {
			return annex;
		}
		public void setAnnex(File annex) {
			this.annex = annex;
		}	
	
}
