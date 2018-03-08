package power.web.equ.base.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquLibraryManage;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

@SuppressWarnings("serial")
public class EquLibraryAction extends AbstractAction {
	protected EquLibraryManage remote;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	@SuppressWarnings("unchecked")
	public EquLibraryAction() {
		remote = (EquLibraryManage) factory
				.getFacadeRemote("EquLibraryManageImpl");
	}
	
   public  void  EquAction(){
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String fuzzy = request.getParameter("fuzzy");
		String fuzzy1 = request.getParameter("fuzzy1");
		PageObject pat = null;
		if (start != null && limit != null && !start.equals(""))
			pat= remote.find(fuzzy,fuzzy1,employee.getEnterpriseCode(),Integer.parseInt(start), Integer.parseInt(limit));
		else
			pat = remote.find(fuzzy,fuzzy1, employee.getEnterpriseCode());
		try {
			
			write(JSONUtil.serialize(pat));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	   
	   
   }
   //add by ypan 20100919
	public void findEquLibrary() throws JSONException
	{
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    String modelName=request.getParameter("modelName");
	    String materialName=request.getParameter("materialName");
	    PageObject obj=new  PageObject();
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findQueryList(employee.getEnterpriseCode(),materialName, modelName, start,limit);
	    }
	    else
	    {
	    	obj=remote.findQueryList(employee.getEnterpriseCode(),materialName,modelName);
	    }
	    String str=JSONUtil.serialize(obj);
		write(str);
	}

}