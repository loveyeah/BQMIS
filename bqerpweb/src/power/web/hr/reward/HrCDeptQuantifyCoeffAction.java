package power.web.hr.reward;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.reward.HrCDeptQuantifyCoefficient;
import power.ejb.hr.reward.HrCDeptQuantifyCoefficientFacadeRemote;
import power.ejb.hr.reward.HrCTechnicianStandards;
import power.ejb.hr.reward.HrCTechnicianStandardsFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class HrCDeptQuantifyCoeffAction extends AbstractAction {
	private HrCDeptQuantifyCoefficientFacadeRemote remote;

	public HrCDeptQuantifyCoeffAction() {
		remote = (HrCDeptQuantifyCoefficientFacadeRemote) factory
				.getFacadeRemote("HrCDeptQuantifyCoefficientFacade");
	}

	public void getDeptQuantifyList() throws JSONException {
		String monthDate = request.getParameter("monthDate");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.getDeptQuantifyList(monthDate, employee
					.getEnterpriseCode(), start, limit);
		} else {
			obj = remote.getDeptQuantifyList(monthDate, employee
					.getEnterpriseCode());
		}
		if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list :[]}");
		}

	}

	public void deleteDeptQuantifyList() {
		String ids = request.getParameter("ids");
		remote.deleteDeptQuantifyList(ids);
		write("{success:true,msg:'删除成功！'}");

	}

	@SuppressWarnings("unchecked")
	public void saveDeptQuantifyList() throws JSONException {
		String str = request.getParameter("isUpdate");
		String monthDate = request.getParameter("monthDate");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<HrCDeptQuantifyCoefficient> addList = null;
		List<HrCDeptQuantifyCoefficient> updateList = null;
		if (list != null && list.size() > 0) {
			addList = new ArrayList<HrCDeptQuantifyCoefficient>();
			updateList = new ArrayList<HrCDeptQuantifyCoefficient>();
			for (Map data : list) {

				String coefficientId = null;
				String deptId = null;
				String quantifyCoefficient = null;

				if (data.get("coefficientId") != null
						&& !"".equals(data.get("coefficientId")))
					coefficientId = data.get("coefficientId").toString();
				if (data.get("deptId") != null
						&& !"".equals(data.get("deptId")))
					deptId = data.get("deptId").toString();
				if (data.get("quantifyCoefficient") != null
						&& !"".equals(data.get("quantifyCoefficient")))
					quantifyCoefficient = data.get("quantifyCoefficient")
							.toString();

				HrCDeptQuantifyCoefficient model = new HrCDeptQuantifyCoefficient();
				if (coefficientId == null) {
					if (deptId != null) {
						model.setDeptId(Long.parseLong(deptId));
					}
					if (quantifyCoefficient != null) {
						model.setQuantifyCoefficient(Double
								.parseDouble(quantifyCoefficient));
					}
					model.setCoefficientMonth(monthDate);
					model.setEnterpriseCode(employee.getEnterpriseCode());
					model.setIsUse("Y");
					addList.add(model);
				} else {
					model = remote.findById(Long.parseLong(coefficientId));
					if (deptId != null) {
						model.setDeptId(Long.parseLong(deptId));
					}
					if (quantifyCoefficient != null) {
						model.setQuantifyCoefficient(Double
								.parseDouble(quantifyCoefficient));
					}
					model.setCoefficientMonth(monthDate);
					model.setEnterpriseCode(employee.getEnterpriseCode());
					model.setIsUse("Y");
					updateList.add(model);
				}
			}
		}
		remote.saveDeptQuantifyList(addList, updateList);
		write("{success:true,msg:'操作成功！'}");

	}
}
