package power.web.workticketbq.approve.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.business.BqWorkticketApprove;
import power.ejb.workticket.business.RunJWorkticketMeasure;
import power.ejb.workticket.business.RunJWorkticketMeasureData;
import power.ejb.workticket.business.RunJWorkticketMeasureDataFacadeRemote;
import power.web.comm.AbstractAction;

public class FireMeasureApproveAction extends AbstractAction{

	private BqWorkticketApprove approve;
	private RunJWorkticketMeasure measure;
	private RunJWorkticketMeasureDataFacadeRemote measureRemote;
	
	public FireMeasureApproveAction()
	{
		approve=(BqWorkticketApprove)factory.getFacadeRemote("BqWorkticketApproveImpl");
		measureRemote=(RunJWorkticketMeasureDataFacadeRemote)factory.getFacadeRemote("RunJWorkticketMeasureDataFacade");
	}
	
	public void fireMeasureApprove()
	{
		String  eventIdentify=request.getParameter("eventIdentify");
		String workticketNo=request.getParameter("workticketNo");
		String actionId=request.getParameter("actionId");
		String responseDate=request.getParameter("responseDate");
		String nextRoles=request.getParameter("nextRoles");
		String workerCode=request.getParameter("workerCode");
		String approveText=request.getParameter("approveText");
		measure.setWorkticketNo(workticketNo);
		measure.setEnterpriseCode(employee.getEnterpriseCode());
		approve.fireMeasureApprove(measure, approveText, workerCode, Long.parseLong(actionId), responseDate, nextRoles, eventIdentify);
		 write("{success:true,msg:'审批成功！'}");
	}
	
	/**
	 * 获得测量数据列表
	 * @throws JSONException 
	 */
	public void findMeasureList() throws JSONException
	{
		String  workticketNo=request.getParameter("workticketNo");
		  Object objstart=request.getParameter("start");
		    Object objlimit=request.getParameter("limit");
		    PageObject obj=new  PageObject();
		    if(objstart!=null&&objlimit!=null)
		    {
		        int start = Integer.parseInt(request.getParameter("start"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				obj=measureRemote.findAllByWorkticketNo(workticketNo, start,limit);
		    }
		    else
		    {
		    	obj=measureRemote.findAllByWorkticketNo(workticketNo);
		    }
		    String str=JSONUtil.serialize(obj);
			write(str);
	}
	
	/**
	 * 保存测量数据
	 * @throws JSONException 
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public void saveMeasureInfo() throws JSONException, ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str=request.getParameter("data");
		String workticketNo=request.getParameter("workticketNo");
		   Object obj = JSONUtil.deserialize(str);
		   List<Map> list = (List<Map>)obj;
		   for(Map data : list)
		   {
			   String measureDataId = ((Map) ((Map) data)).get("measureDataId").toString();
			   String measureData = ((Map) ((Map) data)).get("measureData").toString();
			   String measureDate = ((Map) ((Map) data)).get("measureDate").toString();
			   String measureMan = ((Map) ((Map) data)).get("measureMan").toString();
			  // String workticketNo=((Map) ((Map) data)).get("workticketNo").toString();
			   RunJWorkticketMeasureData model=new RunJWorkticketMeasureData();
			   if(measureDataId!=null&&!measureDataId.equals(""))
			   {
				    model=measureRemote.findById(Long.parseLong(measureDataId));
			   }
			   if(measureData!=null&&!measureData.equals(""))
			   {
				   model.setMeasureData(measureData);
			   }
			   if(measureDate!=null&&!measureDate.equals(""))
			   {
				   model.setMeasureDate(df.parse(measureDate));
			   }
			   if(measureMan!=null&&!measureMan.equals(""))
			   {
				   model.setMeasureMan(measureMan);
			   }
			  model.setWorkticketNo(workticketNo);
			  
			  if(measureDataId!=null&&!measureDataId.equals(""))
			   {
				  measureRemote.update(model);
			   }
			  else
			  {
				  measureRemote.save(model);
				 
			  }
		   }
		   write("{success:true,msg:'保存成功！'}");
	}
	
	public void deleteMeasureInfo()
	{
		String ids=request.getParameter("ids");
		measureRemote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
		
	}

	public RunJWorkticketMeasure getMeasure() {
		return measure;
	}

	public void setMeasure(RunJWorkticketMeasure measure) {
		this.measure = measure;
	}
}
