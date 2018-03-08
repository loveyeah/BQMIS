package power.web.message.action;

import java.util.ArrayList;
import java.util.List;

import power.ejb.basedata.BaseDataManager;
import power.ejb.message.JljfCObject;
import power.ejb.message.JljfCObjectFacadeRemote;
import power.ejb.message.bussiness.HrJCompanyWorker;
import power.ejb.message.bussiness.HrJCompanyWorkerFacadeRemote;
import power.ejb.message.form.WorkerInfo;
import power.ejb.system.SysCUl;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class CustomerContactAction extends AbstractAction {
	private HrJCompanyWorkerFacadeRemote remote;
	private JljfCObjectFacadeRemote compRemote;

	public CustomerContactAction() {
		compRemote = (JljfCObjectFacadeRemote) factory
				.getFacadeRemote("JljfCObjectFacade");
		remote = (HrJCompanyWorkerFacadeRemote) factory
				.getFacadeRemote("HrJCompanyWorkerFacade");
	}

	// 根据模糊查询查找客户公司列表
	public void findByFuzzy() throws Exception {
		String fuzzy = request.getParameter("likename");
		if (fuzzy == null && fuzzy.length() < 1) {
			fuzzy = "";
		}
		List<JljfCObject> list = compRemote.findByFuzzy(fuzzy);
		if (list != null) {
			write("{data:{list:" + JSONUtil.serialize(list) + "}}");
		} else {
			write("{data:{list:[],total:[0]}}");
		}
	}

	// 根据联系人code删除
	public void deleteContacter() throws Exception {
		String workerCode = request.getParameter("workerCode");
		String[] ids = workerCode.split(",");
		for (int i = 0; i < ids.length; i++) {
			HrJCompanyWorker model = remote.findAllByWorkerCode(ids[i]);
			if (model != null) {
				remote.delete(model);
			}
		}
	}

	// 根据客户公司code查找联系人列表
	public void findContacterByCode() throws Exception {
		String zbbmtxCode = request.getParameter("zbbmtxCode");
		List<WorkerInfo> list = null;
		if ("管理员".equals(zbbmtxCode)) {
			BaseDataManager bdm = (BaseDataManager) factory
					.getFacadeRemote("BaseDataManagerImpl");
			List<SysCUl> ul = bdm.getAdminWorker(employee.getEnterpriseCode());
			if (ul != null && ul.size() > 0) {
				list = new ArrayList<WorkerInfo>();
				for (SysCUl u : ul) {
					WorkerInfo tmp = new WorkerInfo();
					tmp.setWorkerCode(u.getWorkerCode());
					tmp.setWorkerName(u.getWorkerName());
					list.add(tmp);
				}
			}
		} else {
			list = remote.findByZbbmtxCode(zbbmtxCode);
		}
		if (list != null) {
			String retStr = JSONUtil.serialize(list);
			write("{success : true,data:{ list:" + retStr + "}}");
		} else
			write("{success : true,data:{totalCount:0,list:[]}");
	}

	// 根据客户公司code给相应的公司增加联系人
	public void addContacterInCustomer() {
		try {
			String companyCode = request.getParameter("companyCode");
			String workerCodes = request.getParameter("workerCodes");
			if ((companyCode != null && !companyCode.equals(""))
					&& (workerCodes != null && !workerCodes.equals(""))) {
				remote.addWorkersToCompany(employee.getEnterpriseCode(),
						companyCode, workerCodes);
			}
			write("{success:true}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success:fasle,msg:'" + exc.getMessage() + "'}");
		}
	}
}
