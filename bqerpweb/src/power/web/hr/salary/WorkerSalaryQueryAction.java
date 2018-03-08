package power.web.hr.salary;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.web.comm.AbstractAction;
import power.ejb.hr.salary.*;



public class WorkerSalaryQueryAction extends AbstractAction{
	private HrJSalaryWageFacadeRemote remote;
	private HrJSalaryWageFacade wage ;
	
	public WorkerSalaryQueryAction(){
		remote = (HrJSalaryWageFacadeRemote)factory.getFacadeRemote("HrJSalaryWageFacade");
	}
	 public void findWorkerSalaryByMonth()throws JSONException{
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.getSalary(startDate,endDate,start,limit);
			}
		else {
			object = remote.getSalary(startDate,endDate);
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
//		System.out.println(strOutput);
		write(strOutput);
		
	}
	 
	 
	 
	  //add by wpzhu 20100806----------------
	 public void getSalaryByMonth() throws JSONException
	 {
		 String month = request.getParameter("month");
		 String empId=request.getParameter("empId");
		 String flag=request.getParameter("flag");
		 
		 Long Id=0l;
		 if(empId!=null&&!"".equals(empId))
		 {
			 Id=Long.parseLong(empId);
		 }
			Object str = request.getParameter("start");
			Object lim = request.getParameter("limit");
			PageObject object = new PageObject();
			if (str != null && lim != null) {
				int start = Integer.parseInt(request.getParameter("start"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				object = remote.getSalaryByMonth(flag,Id,month, employee.getEnterpriseCode(), start,limit);
				}
			else {
				object = remote.getSalaryByMonth(flag,Id,month, employee.getEnterpriseCode());
			}
			String strOutput = "";
			if (object == null || object.getList() == null) {
				strOutput = "{\"list\":[],\"totalCount\":0}";
			} else {
				strOutput = JSONUtil.serialize(object);
			}
//			System.out.println("the stroutput"+strOutput);
			write(strOutput);
			
		 
	 }
	
	public HrJSalaryWageFacade getWage() {
		return wage;
	}
	public void setWage(HrJSalaryWageFacade wage) {
		this.wage = wage;
	}
}
