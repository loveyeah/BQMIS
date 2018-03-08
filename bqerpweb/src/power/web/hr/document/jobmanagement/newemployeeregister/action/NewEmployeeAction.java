package power.web.hr.document.jobmanagement.newemployeeregister.action;

import java.util.Date;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrCTechnologyTitlesType;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.HrJNewemployee;
import power.ejb.hr.HrJNewemployeeFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.ListRange;

@SuppressWarnings("serial")
public class NewEmployeeAction extends AbstractAction {

	protected HrJNewemployeeFacadeRemote remote;
	protected HrJEmpInfoFacadeRemote empRemote;
	private HrJNewemployee newEmp;

	public NewEmployeeAction() {
		remote = (HrJNewemployeeFacadeRemote) factory
				.getFacadeRemote("HrJNewemployeeFacade");
		empRemote = (HrJEmpInfoFacadeRemote)factory.getFacadeRemote("HrJEmpInfoFacade");
	}

	public void editNewEmployeeRecord() {
		String strAddFlag = request.getParameter("isAdd");
		String empId = request.getParameter("empId");
		String empType = request.getParameter("empType");
		String empTime = request.getParameter("empTime");
		if (Boolean.parseBoolean(strAddFlag)) {
			newEmp.setEmpId(Long.parseLong(empId));
			newEmp.setEnterpriseCode(employee.getEnterpriseCode());
			newEmp.setRegisterDate(new Date());//add by sychen 20100717
			newEmp.setEmpType(empType);//add by sychen 20100721
			newEmp.setEmpTime(empTime);//add by sychen 20100721
			
			remote.save(newEmp);
			
			HrJEmpInfo empInfo = empRemote.findById(Long.parseLong(empId));
			empInfo.setEmpId(newEmp.getEmpId());
			empInfo.setDeptId(newEmp.getNewDeptid());
			empInfo.setCheckStationGrade(newEmp.getCheckStationGrade());
			empInfo.setStationId(newEmp.getStationId());
			empInfo.setMissionDate(newEmp.getMissionDate());
			empRemote.update(empInfo);
			
			write("{success:true,id:'" + newEmp.getNewEmpid()+ "',msg:'增加成功！'}");
		} else {
			HrJNewemployee model = remote.findById(newEmp.getNewEmpid());
			model.setAdvicenoteNo(newEmp.getAdvicenoteNo());
			model.setCheckStationGrade(newEmp.getCheckStationGrade());
			model.setEmpId(newEmp.getEmpId());
			model.setMemo(newEmp.getMemo());
			model.setMissionDate(newEmp.getMissionDate());
			model.setNewDeptid(newEmp.getNewDeptid());
			model.setSalaryPoint(newEmp.getSalaryPoint());
			model.setStartsalaryDate(newEmp.getStartsalaryDate());
			model.setStationId(newEmp.getStationId());
			model.setRegisterDate(new Date());//add by sychen 20100717
			model.setEmpType(empType);//add by sychen 20100721
			model.setEmpTime(empTime);//add by sychen 20100721
			model.setPrintDate(newEmp.getPrintDate());//add by sychen 20100721

			remote.update(model);
			
			HrJEmpInfo empInfo = empRemote.findById(Long.parseLong(empId));
			empInfo.setEmpId(newEmp.getEmpId());
			empInfo.setDeptId(newEmp.getNewDeptid());
			empInfo.setCheckStationGrade(newEmp.getCheckStationGrade());
			empInfo.setStationId(newEmp.getStationId());
			empInfo.setMissionDate(newEmp.getMissionDate());
			empRemote.update(empInfo);
			
			write("{success:true,msg:'修改成功！'}");
		}
	}

	public void deleteNewEmployeeRecord() {
		String ids = request.getParameter("ids");
		remote.deleteMult(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public void findNewEmployeeList() throws JSONException {
		String year = request.getParameter("year");
		String advicenoteNo = request.getParameter("advicenoteNo");
		String dept = request.getParameter("dept");

		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findNewEmployeeList(employee.getEnterpriseCode(),
					year, advicenoteNo, dept, start, limit);
		} else {
			obj = remote.findNewEmployeeList(employee.getEnterpriseCode(),
					year, advicenoteNo, dept);
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	
	 /**
     * 新进员工通知单号自增
     * add by drdu 20100629
     */
	public void getAdvicenoteNo() {
		//update by sychen 20100717
		String sqlstr = String.format("select nvl(max(advicenote_no)+1,1) from hr_j_newemployee t where t.is_use='Y'");
//		String sqlstr = String.format("select nvl(max(advicenote_no)+1,1) from hr_j_newemployee");

		try {
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory.getInstance().getFacadeRemote("NativeSqlHelper");
			Object ob = bll.getSingal(sqlstr);
			String advicenoteNo = (ob.toString());
			ListRange<HrJNewemployee> newlist = new ListRange<HrJNewemployee>();
			newlist.setMessage(advicenoteNo);
			newlist.setSuccess(true);
			String jsonstr = JSONUtil.serialize(newlist);
			write(jsonstr);
		} catch (NumberFormatException e) {
			LogUtil.log("SYS_C_WEBPAGE err", Level.INFO, e);
		} catch (Exception e) {
			LogUtil.log("SYS_C_WEBPAGE err", Level.INFO, e);
		}
	}

	public HrJNewemployee getNewEmp() {
		return newEmp;
	}

	public void setNewEmp(HrJNewemployee newEmp) {
		this.newEmp = newEmp;
	}
}
