package power.web.equ.workbill.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;

import power.ejb.equ.workbill.EquJStandardService;
import power.ejb.equ.workbill.EquJStandardServiceFacadeRemote;
import power.ejb.run.runlog.shift.RunCUnitprofessionFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class EquJServAction extends AbstractAction {
	private EquJStandardServiceFacadeRemote remote;
	protected RunCUnitprofessionFacadeRemote dll;
	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public EquJServAction() {
		remote = (EquJStandardServiceFacadeRemote) factory
				.getFacadeRemote("EquJStandardServiceFacade");
		dll = (RunCUnitprofessionFacadeRemote) factory
				.getFacadeRemote("RunCUnitprofessionFacade");
	}

	// /**
	// * 取工种专业
	// *
	// */
	//
	// public void useprolist() throws Exception {
	// List<RunCUnitprofession> list = dll.findUnitPList(employee
	// .getEnterpriseCode());
	// String str = "[";
	// int i = 0;
	// for (RunCUnitprofession model : list) {
	// i++;
	// str += "[\"" + model.getSpecialityName() + "\",\""
	// + model.getSpecialityCode() + "\"]";
	// if (i < list.size()) {
	// str += ",";
	// }
	// }
	// str += "]";
	// write(str);
	// }

	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveEquJStandardService() {

		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			String woCode = request.getParameter("woCode");
			Object obj = JSONUtil.deserialize(str);

			List<EquJStandardService> addList = new ArrayList<EquJStandardService>();
			List<EquJStandardService> updateList = new ArrayList<EquJStandardService>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {

				String operationStep = null;
				String planServiceCode = null;
				String planServiceUnit = null;
				String planFee = null;
				String factServiceCode = null;
				String factServiceUnit = null;
				String factFee = null;
				String id = null;

			

				if (data.get("baseInfo.id") != null) {
					id = data.get("baseInfo.id").toString();
				}

				if (data.get("baseInfo.operationStep") != null) {
					operationStep = data.get("baseInfo.operationStep")
							.toString();
				}
				if (data.get("baseInfo.planServiceCode") != null) {
					planServiceCode = data.get("baseInfo.planServiceCode")
							.toString();
				}

				if (data.get("baseInfo.planServiceUnit") != null) {
					planServiceUnit = data.get("baseInfo.planServiceUnit")
							.toString();
				}

				if (data.get("baseInfo.planFee") != null) {
					planFee = data.get("baseInfo.planFee").toString();
				}

				if (data.get("baseInfo.factServiceCode") != null) {
					factServiceCode = data.get("baseInfo.factServiceCode")
							.toString();
				}

				if (data.get("baseInfo.factServiceUnit") != null) {
					factServiceUnit = data.get("baseInfo.factServiceUnit")
							.toString();
				}

				if (data.get("baseInfo.factFee") != null) {
					factFee = data.get("baseInfo.factFee").toString();
				}

				EquJStandardService model = new EquJStandardService();
				// 增加
				if (id == null) {
					model.setWoCode(woCode);
					model.setOperationStep(operationStep);

					model.setPlanServiceCode(planServiceCode);
					model.setPlanServiceUnit(planServiceUnit);

					if (planFee != null && !planFee.equals("")) {
						model.setPlanFee(Double.parseDouble(planFee));
					}

					model.setEnterprisecode(employee.getEnterpriseCode());

					addList.add(model);
				}
				// 修改
				else {
					model = remote.findById(Long.parseLong(id));

					if (planServiceCode != null && !planServiceCode.equals(""))
						model.setPlanServiceCode(planServiceCode);

					if (planServiceUnit != null && !planServiceUnit.equals(""))
						model.setPlanServiceUnit(planServiceUnit);

					if (planFee != null && !planFee.equals(""))
						model.setPlanFee(Double.parseDouble(planFee));

					if (factServiceCode != null && !factServiceCode.equals(""))
						model.setFactServiceCode(factServiceCode);

					if (factServiceUnit != null && !factServiceUnit.equals(""))
						model.setFactServiceUnit(factServiceUnit);

					if (factFee != null && !factFee.equals(""))
						model.setFactFee(Double.parseDouble(factFee));

					updateList.add(model);
				}
			}

			if ((addList.size() > 0 || updateList.size() > 0)
					|| (deleteIds != null && !deleteIds.trim().equals(""))) {
				remote.save(addList, updateList, deleteIds);
			}

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	/**
	 * 取列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getEquJStandardService() throws JSONException {
		String woCode = request.getParameter("woCode");
		String opCode = request.getParameter("opCode");
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), woCode,
				opCode, start, limit);
		write(JSONUtil.serialize(obj));
	}
	
	public void getEquCStandardService() throws JSONException {
		String woCode = request.getParameter("woCode");
		String opCode = request.getParameter("opCode");
		String objstart = request.getParameter("start");
		String objlimit = request.getParameter("limit");
		int start = 0;
		int limit = 18; 
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit")); 
		}  
		PageObject obj = remote.getEquCStandardService(employee.getEnterpriseCode(), woCode,
				opCode, start, limit);
		write(JSONUtil.serialize(obj));
	}

	

}
