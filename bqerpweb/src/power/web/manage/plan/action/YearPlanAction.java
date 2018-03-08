package power.web.manage.plan.action;

import java.io.File;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.web.comm.UploadFileAbstractAction;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.BpJYearPlan;
import power.ejb.manage.plan.BpJYearPlanFacadeRemote;



@SuppressWarnings("serial")
public class   YearPlanAction  extends  UploadFileAbstractAction
{
	
	protected  BpJYearPlanFacadeRemote  remote ;
	@SuppressWarnings("unused")
	private   BpJYearPlan  yearPlan  ;
	private File  contentPath ;
	public YearPlanAction()
	{
		remote = (BpJYearPlanFacadeRemote) factory
		.getFacadeRemote("BpJYearPlanFacade");
	}
	@SuppressWarnings("unchecked")
	public void  findYearPlan() throws JSONException
	{
		String year = request.getParameter("Year");
		Object  start = request.getParameter("start");
		Object limit = request.getParameter("limit");
		int start1=0;
		int limit1=18;
		if(start!=null&&limit!=null)
		{
			int start2 = Integer.parseInt(start.toString());
			int limit2 = Integer.parseInt(limit.toString());
			PageObject result=remote.getYearPlan(year,employee.getEnterpriseCode(),start2,limit2);
			write(JSONUtil.serialize(result));
		}else 
		{
			PageObject result=remote.getYearPlan(year,employee.getEnterpriseCode(),start1,limit1);
			write(JSONUtil.serialize(result));
		}
		
		
	}
	public void addOrUpdateYearPlan() {
		String method= request.getParameter("method");
		if (method.equals("add")) {
			addYearPlan();
		} else {
			updateYearPlan();
		}

	}

	public void addYearPlan() {
		String filePath = request.getParameter("filePath");
		String year = request.getParameter("year");
		java.text.SimpleDateFormat form= new java.text.SimpleDateFormat("yyyy");
		
		if (!filePath.equals("")) {
			String result = filePath.substring(filePath.lastIndexOf("\\") + 1);
			String fileName = result.replaceAll(" ", "");
			String[] filetemp = fileName.split("\\.");
			if (filetemp[1].equals("txt")) {
				filetemp[1] = ".doc";
				fileName = filetemp[0] + filetemp[1];
			}
			String Temp = uploadFile(contentPath, fileName, "rundoc");
			yearPlan.setContentPath(Temp);
		}
		yearPlan.setStrYear(year);
		yearPlan.setEntryBy(employee.getWorkerCode());
		yearPlan.setEntryDate(new Date());
		yearPlan.setEnterpriseCode(employee.getEnterpriseCode());
		yearPlan.setIsUse("Y");
		remote.save(yearPlan);
		write("{success:true,msg:'增加成功！'}");
	}

	
	public void updateYearPlan() {
		String filePath = request.getParameter("filePath");
		BpJYearPlan model = new BpJYearPlan();
		model = remote.findById(Long.parseLong(yearPlan.getYearPlanId().toString()));
		if (!filePath.equals("")) {
			if (!(model.getContentPath()!=null&& filePath.equals(model.getContentPath()))) {
				String result = filePath
						.substring(filePath.lastIndexOf("\\") + 1);
				String fileName = result.replaceAll(" ", "");
				String[] filetemp = fileName.split("\\.");
				if (filetemp[1].equals("txt")) {
					filetemp[1] = ".doc";
					fileName = filetemp[0] + filetemp[1];
				}
				String Path = uploadFile(contentPath, fileName, "rundoc");
				model.setContentPath(Path);
			}
		}
		model.setTitle(yearPlan.getTitle());
		model.setMemo(yearPlan.getMemo());
		
		remote.update(model) ;
		write("{success:true,msg:'修改成功！'}");
		
	}

	
	public void delYearPlan()
	{
		String ids = request.getParameter("ids");
		remote.deleteYearPlan(ids);
		write("{success:true,msg:'删除成功！'}");
	
	}
	
	
	
	public BpJYearPlan getYearPlan() {
		return yearPlan;
	}
	public void setYearPlan(BpJYearPlan yearPlan) {
		this.yearPlan = yearPlan;
	}
	public File getContentPath() {
		return contentPath;
	}
	public void setContentPath(File contentPath) {
		this.contentPath = contentPath;
	}
	
	
	}