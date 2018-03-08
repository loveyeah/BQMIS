package power.web.equ.standardpackage.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;

import power.ejb.equ.standardpackage.EquCStandardService;
import power.ejb.equ.standardpackage.EquCStandardServiceFacadeRemote;

import power.ejb.run.runlog.shift.RunCUnitprofessionFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquCStandardServAction extends AbstractAction {
	private EquCStandardServiceFacadeRemote remote;
	protected RunCUnitprofessionFacadeRemote dll;
	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public EquCStandardServAction() {
		remote = (EquCStandardServiceFacadeRemote) factory
				.getFacadeRemote("EquCStandardServiceFacade");
		dll = (RunCUnitprofessionFacadeRemote) factory
				.getFacadeRemote("RunCUnitprofessionFacade");
	}

//	/**
//	 * 取工种专业
//	 * 
//	 */
//
//	public void useprolist() throws Exception {
//		List<RunCUnitprofession> list = dll.findUnitPList(employee
//				.getEnterpriseCode());
//		String str = "[";
//		int i = 0;
//		for (RunCUnitprofession model : list) {
//			i++;
//			str += "[\"" + model.getSpecialityName() + "\",\""
//					+ model.getSpecialityCode() + "\"]";
//			if (i < list.size()) {
//				str += ",";
//			}
//		}
//		str += "]";
//		write(str);
//	}

	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveEquCStandardServplan() {

		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<EquCStandardService> addList = new ArrayList<EquCStandardService>();
			List<EquCStandardService> updateList = new ArrayList<EquCStandardService>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {

				String operationStep = null;
				String planServiceCode = null;
				String planServiceUnit = null;
				String planFee = null;
				String id = null;

				String woCode = data.get("baseInfo.woCode").toString();

				if (data.get("baseInfo.id") != null) {
					id = data.get("baseInfo.id").toString();
				}

				if (data.get("baseInfo.operationStep") != null) {
					operationStep = data.get("baseInfo.operationStep").toString();
				}
				if (data.get("baseInfo.planServiceCode") != null) {
					planServiceCode = data.get("baseInfo.planServiceCode").toString();
				}

				if (data.get("baseInfo.planServiceUnit") != null) {
					planServiceUnit = data.get("baseInfo.planServiceUnit").toString();
				}

				if (data.get("baseInfo.planFee") != null) {
					planFee = data.get("baseInfo.planFee").toString();
				}

				EquCStandardService model = new EquCStandardService();
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
				} else {
					model = remote.findById(Long.parseLong(id));

					model.setPlanServiceCode(planServiceCode);
					model.setPlanServiceUnit(planServiceUnit);

					if (planFee != null && !planFee.equals("")) {
						model.setPlanFee(Double.parseDouble(planFee));
					}

					updateList.add(model);
				}
			}

			if (addList.size() > 0)

				remote.save(addList);

			if (updateList.size() > 0)

				remote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))

				remote.delete(deleteIds);

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
	public void getEquCStandardServplanList() throws JSONException {
		String woCode = request.getParameter("woCode");
		String opCode = request.getParameter("opCode");
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), woCode, opCode, start, limit);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
