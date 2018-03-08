package power.web.manage.stat.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCStatReport;
import power.ejb.manage.stat.BpCStatReportFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 未使用
 * 
 * @author fyyang
 * 
 */
@SuppressWarnings("serial")
public class BpStatReportAction extends AbstractAction {

	private BpCStatReportFacadeRemote remote;

	public BpStatReportAction() {
		remote = (BpCStatReportFacadeRemote) factory
				.getFacadeRemote("BpCStatReportFacade");
	}

	/**
	 * 
	 * @throws JSONException
	 */
	public void findStatReportList() throws JSONException {
		Object objReportName = request.getParameter("reportName");
		String type = request.getParameter("type");
		type = type == null ? "" : type;
		String reportName = "";
		if (objReportName != null) {
			reportName = objReportName.toString();
		}
		Object objStart = request.getParameter("start");
		/** 查询行数 */
		Object objLimit = request.getParameter("limit");
		PageObject result = new PageObject();
		if (objStart != null && objLimit != null) {
			result = remote.findAll(reportName, employee.getEnterpriseCode(),employee.getWorkerCode(),
					type, Integer.parseInt(objStart.toString()), Integer
							.parseInt(objLimit.toString()));
		} else {
		    result  = remote
					.findAll(reportName, employee.getEnterpriseCode(), employee.getWorkerCode(),type);
		}

		String str = "{\"list\":[],\"totalCount\":}";
		if (result != null) {
			str = JSONUtil.serialize(result);
		} 
		write(str); 
	}

	public void modifyRecords() throws JSONException {
		String addOrUpdateRecords = request.getParameter("addOrUpdateRecords");
		String delIds = request.getParameter("deleteRecords");
		if (addOrUpdateRecords != null) {
			List<BpCStatReport> list = this.parseStatReport(addOrUpdateRecords);
			try {
				remote.modifyRecords(list, delIds);
				write("{success:true,msg:'保存成功！'}");
			} catch (CodeRepeatException e) {
				write("{success:true,msg:'" + e.getMessage() + "'}");
			}
		}

	}

	@SuppressWarnings("unchecked")
	private List<BpCStatReport> parseStatReport(String records)
			throws JSONException {
		List<BpCStatReport> result = new ArrayList<BpCStatReport>();
		Object object = JSONUtil.deserialize(records);
		List list1 = (List) object;
		int intLen = list1.size();
		for (int i = 0; i < intLen; i++) {
			Map map = (Map) list1.get(i);
			BpCStatReport m = this.parseStatReportModal(map);
			result.add(m);
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	private BpCStatReport parseStatReportModal(Map map) {
		BpCStatReport model = new BpCStatReport();
		Object id = map.get("reportCode");
		Object reportName = map.get("reportName");
		Object reportType = map.get("reportType");
		if (id != null && !id.equals("")) {
			model.setReportCode(Long.parseLong(id.toString()));

		}
		if (reportName != null) {
			model.setReportName(reportName.toString());

		}
		if (reportType != null) {
			model.setReportType(reportType.toString());

		}
		model.setEnterpriseCode(employee.getEnterpriseCode());
		return model;
	}
}
