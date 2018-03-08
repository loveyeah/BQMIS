package power.web.workticket.register.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketDangerFacadeRemote;
import power.ejb.workticket.RunCWorkticketDangerType;
import power.ejb.workticket.RunCWorkticketDangerTypeFacadeRemote;
import power.ejb.workticket.business.RunJWorkticketDanger;
import power.ejb.workticket.business.RunJWorkticketDangerFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class WorkticketDangerAction extends AbstractAction{
	private RunCWorkticketDangerTypeFacadeRemote dangerTypeRemote;
	private RunJWorkticketDangerFacadeRemote dangerRemote;
	private RunJWorkticketDanger danger;
	private String dangerContentId;
	
	
	public WorkticketDangerAction()
	{
		dangerTypeRemote=(RunCWorkticketDangerTypeFacadeRemote)factory.getFacadeRemote("RunCWorkticketDangerTypeFacade");
		dangerRemote=(RunJWorkticketDangerFacadeRemote)factory.getFacadeRemote("RunJWorkticketDangerFacade");
	}
	
	/**
	 * 危险点类型下拉框
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void CoDataDangerType() throws JSONException
	{
		 PageObject obj=new  PageObject();
		 String workticketType=request.getParameter("workticketType");
		 obj=dangerTypeRemote.findAll(employee.getEnterpriseCode(), workticketType, "");
		 RunCWorkticketDangerType model=new RunCWorkticketDangerType();
		 model.setDangerTypeName("所有");
		 model.setDangerTypeId(0l);
		 obj.getList().add(0, model);
		 String str=JSONUtil.serialize(obj);
		 write(str);
		
	}
	
	public void findDangerListForSelect() throws JSONException
	{
		RunCWorkticketDangerFacadeRemote remote=(RunCWorkticketDangerFacadeRemote)factory.getFacadeRemote("RunCWorkticketDangerFacade");
		PageObject obj=new  PageObject();
		String workticketTypeCode=request.getParameter("workticketTypeCode");
		String dangerName=request.getParameter("fuzzy");
		String dangerType="";
		if(!request.getParameter("dangerTypeId").equals("0"))
		{
			dangerType=request.getParameter("dangerTypeId");
		}
//		Object objstart=request.getParameter("start");
//	    Object objlimit=request.getParameter("limit");
//	    if(objstart!=null&&objlimit!=null)
//	    {
//	        int start = Integer.parseInt(request.getParameter("start"));
//			int limit = Integer.parseInt(request.getParameter("limit"));
//	        obj=remote.findListForSelect(employee.getEnterpriseCode(), workticketTypeCode, dangerType, dangerName, start,limit);
//	    }
//	    else
//	    {
	    	obj=remote.findListForSelect(employee.getEnterpriseCode(), workticketTypeCode, dangerType, dangerName);
//	    }
	    String str=JSONUtil.serialize(obj);
		 write(str);
	}
	
	/**
	 * 危险点列表
	 * @throws JSONException
	 */
	public void findDangerList() throws JSONException
	{
		String workticketNo=request.getParameter("workticketNo");
		 PageObject obj=new  PageObject();
		 obj=dangerRemote.findDangerList(workticketNo);
		 String str=JSONUtil.serialize(obj);
		 write(str);
		
	}
	
	public void findDangerControlList() throws JSONException
	{
		String dangerId=request.getParameter("dangerId");
		 PageObject obj=new  PageObject();
		 Object objstart=request.getParameter("start");
		    Object objlimit=request.getParameter("limit");
		    if(objstart!=null&&objlimit!=null)
		    {
		        int start = Integer.parseInt(request.getParameter("start"));
				int limit = Integer.parseInt(request.getParameter("limit"));
		        obj=dangerRemote.findDangerControlList(dangerId,start,limit);
		    }
		    else
		    {
		    	obj=dangerRemote.findDangerControlList(dangerId);
		    }
		    String str=JSONUtil.serialize(obj);
			 write(str);   
	}
	
	public void deleteDanger()
	{
		String ids=request.getParameter("ids");
		dangerRemote.deleteDangerMulti(ids);
		String	str = "{success: true,msg:'删除成功！'}";
	    write(str);
		
	}
	
	//delete by fyyang 090328
//	public void deleteControl()
//	{
//		String ids=request.getParameter("ids");
//		dangerRemote.deleteControlMulti(ids);
//		String	str = "{success: true,msg:'删除成功！'}";
//	    write(str);
//	}
	
	@SuppressWarnings("unchecked")
	public void saveControl() throws JSONException
	{ 
		try {
			String str = request.getParameter("data");
			String deleteIds = request.getParameter("deleteIds");
			Object obj = JSONUtil.deserialize(str);
			List<Map> list = (List<Map>) obj;
			List<RunJWorkticketDanger> addList = new ArrayList<RunJWorkticketDanger>();
			List<RunJWorkticketDanger> updateList = new ArrayList<RunJWorkticketDanger>();
			for (Map data : list) {
				String id = data.get("dangerId").toString();
				String name = data.get("dangerName").toString();
				String orderby = data.get("orderBy").toString();
				String workticketNo = data.get("workticketNo").toString();
				String pId = data.get("pId").toString();
				String isRun = data.get("isRun").toString();
				RunJWorkticketDanger model = new RunJWorkticketDanger();
				// 增加
				if (id.equals("")) {
					model.setDangerName(name);
					if (!orderby.equals("undefined") && !orderby.equals("")) {
						model.setOrderBy(Long.parseLong(orderby));
					}
					model.setWorkticketNo(workticketNo);
					model.setPDangerId(Long.parseLong(pId));
					model.setEnterpriseCode(employee.getEnterpriseCode());
					model.setModifyBy(employee.getWorkerCode());
					model.setIsRunadd(isRun);
					addList.add(model); 
				} else {
					model = dangerRemote.findById(Long.parseLong(id));
					model.setDangerName(name);
					if (!orderby.equals("undefined") && !orderby.equals("")) {
						model.setOrderBy(Long.parseLong(orderby));
					}
					model.setModifyBy(employee.getWorkerCode());
					updateList.add(model); 
				}
			}
			if ((addList.size() > 0 || updateList.size() > 0)
					|| (deleteIds != null && !deleteIds.trim().equals(""))) {
				dangerRemote.save(addList, updateList, deleteIds);
			}
			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}
	
	public void saveDanger()
	{
		String	str = "";
	   
		if(dangerContentId.equals("自动生成"))
		{
			try {
				 danger.setWorkticketNo(request.getParameter("workticketNo"));
				 danger.setIsRunadd(request.getParameter("isRun"));
				danger.setEnterpriseCode(employee.getEnterpriseCode());
				danger.setModifyBy(employee.getWorkerCode());
				danger.setPDangerId(0l);
				danger=dangerRemote.save(danger);
				str = "{success: true,id:'"+danger.getDangerContentId()+"',msg:'增加成功！'}";
			} catch (CodeRepeatException e) {
				str = "{success: true,msg:'"+e.getMessage()+"'}";
			}
		}
		else
		{
			RunJWorkticketDanger model=dangerRemote.findById(Long.parseLong(dangerContentId));
		    model.setDangerName(danger.getDangerName());
		    model.setOrderBy(danger.getOrderBy());
		    model.setModifyBy(employee.getWorkerCode());
		    try {
				dangerRemote.update(model);
				str = "{success: true,id:"+model.getDangerContentId()+",msg:'修改成功！'}";
			} catch (CodeRepeatException e) {
				str = "{success: true,id:"+model.getDangerContentId()+",msg:'"+e.getMessage()+"'}";
			}
		}
		 write(str);
	}

	public RunJWorkticketDanger getDanger() {
		return danger;
	}

	public void setDanger(RunJWorkticketDanger danger) {
		this.danger = danger;
	}

	public String getDangerContentId() {
		return dangerContentId;
	}

	public void setDangerContentId(String dangerContentId) {
		this.dangerContentId = dangerContentId;
	}
	

}
