package power.web.manage.stat.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.comm.TreeNode;
import power.ejb.manage.stat.ZbCReport;
import power.ejb.manage.stat.ZbCReportFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ZbReportAction extends AbstractAction{

	protected ZbCReportFacadeRemote remote;
	private ZbCReport report;
	
	public ZbReportAction()
	{
		remote = (ZbCReportFacadeRemote)factory.getFacadeRemote("ZbCReportFacade");
	}
	
	public void addAndUpdateReport()
	{
		String method = request.getParameter("method");
		if(method.equals("add"))
		{
			String faReprotCode = request.getParameter("faReprotCode");
			String reportCode = request.getParameter("reportCode");
			
			report.setFaReprotCode(faReprotCode);
			
			if(faReprotCode != null&&!faReprotCode.equals("0"))
			{
				report.setReportCode(faReprotCode+"-"+reportCode);
			}
			else{
				report.setReportCode(reportCode);
			}
			report.setLastModifierBy(employee.getWorkerCode());
			report.setEnterpriseCode(employee.getEnterpriseCode());
			remote.save(report);
			write("{success:true,msg:'增加成功！'}");
		}else{
			ZbCReport model = remote.findById(report.getReportId());
			model.setLastModifierBy(employee.getWorkerCode());
			model.setLastModifierDate(new Date());
			model.setReportCode(report.getReportCode());
			model.setReportName(report.getReportName());
			model.setReportType(report.getReportType());
			
			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		}
	}
	
	public void deleteReport()
	{
		ZbCReport modelReport = new ZbCReport();
		String code = "";
		code = request.getParameter("id");
		if (!remote.IfHasChild(code, employee.getEnterpriseCode())) {
			modelReport = remote.findByCode(code, employee.getEnterpriseCode());
			modelReport.setIsUse("N");
			remote.update(modelReport);
			write("{success:true,msg:'删除成功！'}");
		} else {
			write("{success:false,msg:'该节点有子节点，不能删除！'}");
		}
	}
	
	public void getReportTreeInfo() throws JSONException {
		String code = "";
		code = request.getParameter("id");
		Object o = remote.findReportInfo(code);
		write(JSONUtil.serialize(o));
	}

	@SuppressWarnings("unchecked")
	public void getReportTreeList() throws JSONException
	{
		String fReportCode = request.getParameter("id");
		List<TreeNode> arrayList = new ArrayList();
		List<ZbCReport> list = remote.findReportTreeList(fReportCode, employee.getEnterpriseCode());		
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("[");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				ZbCReport report = list.get(i);
				TreeNode model = new TreeNode();
				boolean isLeaf = !remote.IfHasChild(report.getReportCode(), employee.getEnterpriseCode());
				model.setId(report.getReportCode());
				model.setText(report.getReportName());
				model.setLeaf(isLeaf);
				model.setCode(report.getReportId().toString());
				model.setHref(report.getFaReprotCode());
				arrayList.add(model);
			}
		}
		if (JSONStr.length() > 1) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
		JSONStr.append("]");
			write(JSONUtil.serialize(arrayList));
	}
	
	
	public ZbCReport getReport() {
		return report;
	}

	public void setReport(ZbCReport report) {
		this.report = report;
	}
}
