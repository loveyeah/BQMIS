package power.web.birt.action;

import java.util.ArrayList;
import java.util.List;

import power.ear.comm.Employee;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.hr.ca.employeeLeaveBean;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.resource.business.IssueManage;

public class IssueDetails {
	
	public static Ejb3Factory factory;
	
	private IssueManage issuemanager;

	static {
		factory = Ejb3Factory.getInstance();
	}
	
	public IssueDetails() {
		issuemanager = (IssueManage) factory.getFacadeRemote("IssueManageImpl");
	}
	
	// modified by liuyi 20100128 
	@SuppressWarnings("unchecked")
//	public List getIssueDetailsMaterialInfo(String issueNo , String whsName,String fillDate,String flag)
	public List getIssueDetailsMaterialInfo(String issueNo , String whsName,String fillDate,String flag,String gdFlag,String materialId)
	{
		// gdFlag = 1 表示固定资产类
		//modify by drdu 091120
		//flag==1表示出库补打印，flag==""表示出库库打印
		List list = new ArrayList();
		if(flag ==null||flag =="")
		{
//		list = issuemanager.getIssueDetailsMaterialInfo(issueNo, whsName);
			list = issuemanager.getIssueDetailsMaterialInfo(issueNo, whsName,gdFlag,materialId);
		}else if(flag.equals("1"))
		{
//			list = issuemanager.getAfterPrintIssueInfo(issueNo, fillDate,whsName);
			list = issuemanager.getAfterPrintIssueInfo(issueNo, fillDate,whsName,gdFlag,materialId);
		}
		return list;
	}
//领料单审批记录 add by bjxu
	public List<ConApproveForm> getApproveList(String issueNo) {
		return issuemanager.getApproveList(issueNo);
	}
		
}
