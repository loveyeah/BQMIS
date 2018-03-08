package power.web.run.runlog.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.runlog.RunCLogWeather;
import power.ejb.run.runlog.RunCLogWeatherFacadeRemote;
import power.web.comm.AbstractAction;

public class RunLogWeatherAction extends AbstractAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RunCLogWeatherFacadeRemote remote;
	private RunCLogWeather weather;

	//构造函数
	public RunLogWeatherAction()
	{
		remote = (RunCLogWeatherFacadeRemote)factory.getFacadeRemote("RunCLogWeatherFacade");
	}
	
	public void addWeather()
	{
		weather.setIsUse("Y");
		weather.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			remote.save(weather);
			write("{success:true,msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			 write("{success:true,msg:'"+e.getMessage()+"'}"); 
		}
	}
	
	public void updateWeather()
	{
		RunCLogWeather model = remote.findById(weather.getWeatherKeyId());
		model.setWeatherCode(weather.getWeatherCode());
		model.setWeatherName(weather.getWeatherName());
		model.setDiaplayNo(weather.getDiaplayNo());
		model.setIsUse("Y");
		 try {
				remote.update(model);
				write("{success:true,msg:'修改成功！'}");
			} catch (CodeRepeatException e) {
				write("{success:true,msg:'"+e.getMessage()+"'}"); 
			}
	}

	public void deleteWeather() throws Exception
	{
		RunCLogWeather model = remote.findById(weather.getWeatherKeyId());
		model.setIsUse("N");
		remote.update(model);
		write("{success:true}");
	}
	
	public void findWeatherList() throws JSONException
	{
		String fuzzy="";
		Object myobj=request.getParameter("fuzzy");
		if(myobj!=null)
		{
			fuzzy=myobj.toString();
		}
	    String enterpriseCode=employee.getEnterpriseCode();
	    Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    PageObject obj=new  PageObject();
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			 obj=remote.findWeather(fuzzy, enterpriseCode,start,limit);
	    }
	    else
	    {
	    	obj=remote.findWeather(fuzzy, enterpriseCode);
	    }
	    
		String str=JSONUtil.serialize(obj);
		write(str);
	}

	public RunCLogWeather getWeather() {
		return weather;
	}

	public void setWeather(RunCLogWeather weather) {
		this.weather = weather;
	}

	
}
