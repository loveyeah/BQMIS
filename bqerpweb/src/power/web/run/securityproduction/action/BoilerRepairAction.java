package power.web.run.securityproduction.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpBoilRepairFacadeRemote;
import power.ejb.run.securityproduction.SpCBoiler;
import power.ejb.run.securityproduction.SpCBoilerFacadeRemote;
import power.ejb.run.securityproduction.SpJBoilerRepair;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BoilerRepairAction extends AbstractAction {
	private SpBoilRepairFacadeRemote remote;
	protected SpCBoilerFacadeRemote boiler;

	public BoilerRepairAction() {
		remote = (SpBoilRepairFacadeRemote) factory
				.getFacadeRemote("SpBoilRepairFacade");

		boiler = (SpCBoilerFacadeRemote) factory
				.getFacadeRemote("SpCBoilerFacade");

	}

	/**
	 * @throws JSONException
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	@SuppressWarnings("unchecked")
	public void saveBoilRepair() throws JSONException, ParseException,
			java.text.ParseException {

		String blockName = request.getParameter("blockName");
		String type = request.getParameter("type");
		String str = request.getParameter("isUpdate");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<SpJBoilerRepair> addList = null;
		List<SpJBoilerRepair> updateList = null;
		Object DateFormat;
		if (list != null && list.size() > 0) {
			addList = new ArrayList<SpJBoilerRepair>();
			updateList = new ArrayList<SpJBoilerRepair>();
			for (Map data : list) {
				String boilRepairID = null;
				String boilerId = null;
				String taskSource = null;
				String repairRecord = null;
				String repairBy = null;
				String repairTime = null;
				String boilerName = null;
				String boilerType = null;
				if (data.get("boilerRepairId") != null)
					boilRepairID = data.get("boilerRepairId").toString();
				if (data.get("boilerId") != null)
					boilerId = data.get("boilerId").toString();
				if (data.get("taskSource") != null)
					taskSource = data.get("taskSource").toString();
				if (data.get("repairRecord") != null)
					repairRecord = data.get("repairRecord").toString();
				if (data.get("repairTime") != null)
					repairTime = data.get("repairTime").toString();
				if (data.get("repairBy") != null)
					repairBy = data.get("repairBy").toString();
				if (data.get("boilerName") != null)
					boilerName = data.get("boilerName").toString();
				if (data.get("boilerType") != null)
					boilerType = data.get("boilerType").toString();
				SpJBoilerRepair model = new SpJBoilerRepair();
				// 设备检修记录boilrepair表

				if (boilRepairID == null || boilRepairID.equals("")) {
					if (taskSource != null && !taskSource.equals("")) {
						model.setTaskSource(taskSource);
					}
					if (repairRecord != null && !repairRecord.equals(""))
						model.setRepairRecord(repairRecord);
					if (repairBy != null && !repairBy.equals(""))
						model.setRepairBy(repairBy);
					if (repairTime != null) {
						DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
						model.setRepairTime(f.parse(repairTime));
					}
					//add by ltong
					model.setFillBy(employee.getWorkerCode());
					model.setFillTime(new Date());
					model.setEnterpriseCode(employee.getEnterpriseCode());
					addList.add(model);
				} else {
					model = remote.findById(Long.parseLong(boilRepairID));
					if (taskSource != null && !taskSource.equals(""))
						model.setTaskSource(taskSource);
					if (repairRecord != null && !repairRecord.equals(""))
						model.setRepairRecord(repairRecord);
					if (repairBy != null && !repairBy.equals(""))
						model.setRepairBy(repairBy);

					if (repairTime != null && !repairTime.equals("")) {
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						model.setRepairTime(sdf.parse(repairTime));

					}
					model.setEnterpriseCode(employee.getEnterpriseCode());
					//add by ltong
					model.setFillBy(employee.getWorkerCode());
					model.setFillTime(new Date());
					updateList.add(model);
				}

			}
		}
		remote.saveBoilRepair(addList, updateList,
				employee.getEnterpriseCode(), blockName, type);
		write("{success:true,msg:'操作成功！'}");

	}

	public void deleteBoilRepair() {
		String ids = request.getParameter("ids");
		remote.delBoilRepair(ids);

	}

	public void getBoilRepair() throws JSONException {
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		//add by ltong
		String isMaint = request.getParameter("isMaint");
		PageObject obj = new PageObject();
		obj = remote.getBoilRepair(employee.getEnterpriseCode(), startTime,
				endTime, isMaint, employee.getWorkerCode());

		if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list :[]}");
		}

	}

	public void getBoilerEquList() throws JSONException {
		String sDate = request.getParameter("sDate");
		String eDate = request.getParameter("eDate");
		String boilerId = request.getParameter("boilerId");
		Long _boilerId = (boilerId == null || "".equals(boilerId)) ? 0L : Long
				.parseLong(boilerId);
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		obj = remote.getBoilerEquList(employee.getEnterpriseCode(), _boilerId,
				sDate, eDate, start, limit);
		if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list :[]}");
		}

	}

}