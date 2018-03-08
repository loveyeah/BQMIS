package power.web.hr.salary;

import java.text.ParseException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCSalaryType;
import power.ejb.monthaward.HrCMonthAward;
import power.ejb.monthaward.HrCMonthAwardFacadeRemote;
import power.ejb.monthaward.HrCMonthStandarddays;
import power.web.comm.AbstractAction;

public class monthAwardAction  extends AbstractAction{
	private  HrCMonthAwardFacadeRemote remoteA;
	private HrCMonthAward award;
	private  HrCMonthStandarddays  standardays;
	public monthAwardAction() {
		remoteA = (HrCMonthAwardFacadeRemote) factory.getFacadeRemote("HrCMonthAwardFacade");
		
	}
	public void getMonthAward() throws JSONException, ParseException{
	 String	 Time = request.getParameter("Time");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remoteA.findMonAward(Time,employee.getEnterpriseCode(), start, limit);
		} else {
			object = remoteA.findMonAward(Time,employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
		
		
	}
	public void saveMonthAward() throws CodeRepeatException, ParseException{
		String method = request.getParameter("method");	
		String effectStartTime = request.getParameter("effectStartTime");
		String effectEndTime = request.getParameter("effectEndTime");
	
		if(method.equals("add"))
			
		{
		
		    java.text.SimpleDateFormat format= new java.text.SimpleDateFormat("yyyy-MM");
			award.setEffectStartTime(format.parse(effectStartTime));
			award.setEffectEndTime(format.parse(effectEndTime));
			award.setEnterpriseCode(employee.getEnterpriseCode());
			award.setIsUse("Y");
			remoteA.save(award);
		    write("{success:true,msg:'增加成功！'}");
			
		}else if(method.equals("update"))	
		{
			 java.text.SimpleDateFormat format= new java.text.SimpleDateFormat("yyyy-MM");
			
			HrCMonthAward model=remoteA.findById(award.getMonthAwardId());
			   model.setEffectEndTime(format.parse(effectEndTime));
			   model.setEffectStartTime(format.parse(effectStartTime));
			   model.setMemo(award.getMemo());
			   remoteA.update(model);
			write("{success:true,msg:'修改成功！'}");
			
			
		}
		
		
		
	}
	public void delMonthAward(){
		  String ids=request.getParameter("ids");
			remoteA.deleteMonAward(ids);
			write("{success:true,msg:'删除成功！'}");
	   }
	public void getMonStandDays() throws ParseException, JSONException{
		 String	 equTime = request.getParameter("equTime");
		// System.out.println("the equtime is"+equTime);
			Object objstart = request.getParameter("start");
			Object objlimit = request.getParameter("limit");
			PageObject object = null;
			if (objstart != null && objlimit != null) {
				int start = Integer.parseInt(request.getParameter("start"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				object = remoteA.findMonAwardDays(equTime,employee.getEnterpriseCode(), start, limit);
			} else {
				object = remoteA.findMonAwardDays(equTime,employee.getEnterpriseCode());
			}
			String strOutput = "";
			if (object == null || object.getList() == null) {
				strOutput = "{\"list\":[],\"totalCount\":0}";
			} else {
				strOutput = JSONUtil.serialize(object);
			}
			write(strOutput);
	
	}
	public void saveMonthAwardDays() throws CodeRepeatException, ParseException{
		String method = request.getParameter("method");	
		String effectStartTime = request.getParameter("effectStartTime");
		String effectEndTime = request.getParameter("effectEndTime");
		if(method.equals("add"))
		{
			
		    java.text.SimpleDateFormat format= new java.text.SimpleDateFormat("yyyy-MM");
		    standardays.setEffectStartTime(format.parse(effectStartTime));
		    standardays.setEffectEndTime(format.parse(effectEndTime));
		    standardays.setEnterpriseCode(employee.getEnterpriseCode());
		    standardays.setIsUse("Y");
			remoteA.saveStandar(standardays);
		    write("{success:true,msg:'增加成功！'}");
			
		}else if(method.equals("update"))	
		{
			 java.text.SimpleDateFormat format= new java.text.SimpleDateFormat("yyyy-MM");
			
			 HrCMonthStandarddays model=remoteA.findByDaysId(standardays.getStandarddaysId());
			   model.setEffectEndTime(format.parse(effectEndTime));
			   model.setEffectStartTime(format.parse(effectStartTime));
			   model.setMemo(standardays.getMemo());
			   
			   model.setStandarddays(standardays.getStandarddays());//add by wpzhu 20100730
			   remoteA.updateAwardDays(model);
			write("{success:true,msg:'修改成功！'}");
			
			
		}
	}
	public void delMonthAwardDays(){
		  String ids=request.getParameter("ids");
			remoteA.deleteMonStandDays(ids);
			write("{success:true,msg:'删除成功！'}");
	   }
		
	
		
	
	public HrCMonthAward getAward() {
		return award;
	}
	public void setAward(HrCMonthAward award) {
		this.award = award;
	}
	public HrCMonthStandarddays getStandardays() {
		return standardays;
	}
	public void setStandardays(HrCMonthStandarddays standardays) {
		this.standardays = standardays;
	}
	
}