package power.web.run.securityproduction.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpJDiseases;
import power.ejb.run.securityproduction.SpJDiseasesFacadeRemote;
import power.ejb.run.securityproduction.form.EmployeeInfo;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SpDiseasesAction extends AbstractAction{

	private SpJDiseases disease;
	protected SpJDiseasesFacadeRemote remote;
	private EmployeeInfo emp;
	
	public SpDiseasesAction()
	{
		remote = (SpJDiseasesFacadeRemote)factory.getFacadeRemote("SpJDiseasesFacade");
	}
	/**
	 * 增加一条职业病检查信息
	 */
	public void addWorkDiseasesInfo()
	{
		disease.setEnterpriseCode(employee.getEnterpriseCode());
		disease = remote.save(disease);
		write("{success:true,id:'"+disease.getMedicalId()+"',msg:'增加成功！'}");
	}
	
	/**
	 *修改一条职业病检查信息
	 */
	public void updateWorkDiseasesInfo()
	{
		SpJDiseases model = remote.findById(disease.getMedicalId());
		model.setWorkerCode(disease.getWorkerCode());
		model.setContactHarm(disease.getContactHarm());
		model.setDepCode(disease.getDepCode());
		model.setContactYear(disease.getContactYear());
		model.setContent(disease.getContent());
		model.setCheckResult(disease.getCheckResult());
		model.setHospital(disease.getHospital());
		model.setMemo(disease.getMemo());
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}
	/**
	 * 删除一条或者多条职业病信息
	 */
	public void deleteWorkDiseasesInfo()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 根据姓名和企业编码查找职业病信息列表
	 * @throws JSONException 
	 */
	public void findWorkDiseasesList() throws JSONException {
		String workName = request.getParameter("workerName");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) 
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findAll(workName, employee.getEnterpriseCode(), start,limit);
		}
		else {
			obj = remote.findAll(workName, employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 根据员工编号取得详细信息
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getInfoByWorkCode() throws JSONException {
		String workCode = request.getParameter("workCode");
		EmployeeInfo list = remote.getEmpInfoDetail(workCode);
		
		String str = JSONUtil.serialize(list);
		write(str);
	}
	
	
	public SpJDiseases getDisease() {
		return disease;
	}

	public void setDisease(SpJDiseases disease) {
		this.disease = disease;
	}
	public EmployeeInfo getEmp() {
		return emp;
	}
	public void setEmp(EmployeeInfo emp) {
		this.emp = emp;
	}
	
}
