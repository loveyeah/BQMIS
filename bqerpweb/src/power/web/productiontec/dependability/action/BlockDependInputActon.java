package power.web.productiontec.dependability.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxJSjlr;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxJSjlrFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BlockDependInputActon extends AbstractAction {
	private PtKkxJSjlrFacadeRemote remote;

	public BlockDependInputActon() {
		remote = (PtKkxJSjlrFacadeRemote) factory
				.getFacadeRemote("PtKkxJSjlrFacade");

	}

	@SuppressWarnings("unchecked")
	public void saveDependInput() throws JSONException, ParseException {
		String str = request.getParameter("isUpdate");
		String deleteId = request.getParameter("isDelete");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<PtKkxJSjlr> addList = new ArrayList<PtKkxJSjlr>();
		List<PtKkxJSjlr> updateList = new ArrayList<PtKkxJSjlr>();
		for (Map data : list) {
			String sjlrId = null;
			String blockCode = null;
			String jzztId = null;
			String startDate = null;
			String endDate = null;
			String keepTime = null;
			String reduceExert = null;
			String stopTimes = null;
			String successTimes = null;
			String failureTimes = null;
			String repairMandays = null;
			String repairCost = null;
			String stopReason = null;
			if (data.get("sjlrId") != null)
				sjlrId = data.get("sjlrId").toString();
			if (data.get("blockCode") != null)
				blockCode = data.get("blockCode").toString();
			if (data.get("jzztId") != null)
				jzztId = data.get("jzztId").toString();
			if (data.get("startDate") != null)
				startDate = data.get("startDate").toString();
			if (data.get("endDate") != null)
				endDate = data.get("endDate").toString();
			if (data.get("keepTime") != null)
				keepTime = data.get("keepTime").toString();
			if (data.get("reduceExert") != null)
				reduceExert = data.get("reduceExert").toString();
			if (data.get("stopTimes") != null)
				stopTimes = data.get("stopTimes").toString();
			if (data.get("successTimes") != null)
				successTimes = data.get("successTimes").toString();
			if (data.get("failureTimes") != null)
				failureTimes = data.get("failureTimes").toString();
			if (data.get("repairMandays") != null)
				repairMandays = data.get("repairMandays").toString();
			if (data.get("repairCost") != null)
				repairCost = data.get("repairCost").toString();
			if (data.get("stopReason") != null)
				stopReason = data.get("stopReason").toString();

			PtKkxJSjlr model = new PtKkxJSjlr();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (sjlrId == null) {
				model.setBlockCode(blockCode);
				if(jzztId != null && !jzztId.equals(""))
				model.setJzztId(Long.parseLong(jzztId));
				model.setStartDate(format.parse(startDate));
				model.setEndDate(format.parse(endDate));
				if (keepTime != null && !keepTime.equals(""))
					model.setKeepTime(Double.valueOf(keepTime));
				if (reduceExert != null && !reduceExert.equals(""))
					model.setReduceExert(Double.valueOf(reduceExert));
				if (stopTimes != null && !stopTimes.equals(""))
					model.setStopTimes(Long.parseLong(stopTimes));
				if (successTimes != null && !successTimes.equals(""))
					model.setSuccessTimes(Long.parseLong(successTimes));
				if (failureTimes != null && !failureTimes.equals(""))
					model.setFailureTimes(Long.parseLong(failureTimes));
				if (repairMandays != null && !repairMandays.equals(""))
					model.setRepairMandays(Double.valueOf(repairMandays));
				if (repairCost != null && !repairCost.equals(""))
					model.setRepairCost(Double.valueOf(repairCost));
				model.setStopReason(stopReason);
				model.setEnterpriseCode(employee.getEnterpriseCode());
				addList.add(model);
			} else {
				model = remote.findById(Long.parseLong(sjlrId));
				model.setBlockCode(blockCode);
				if(jzztId != null && !jzztId.equals(""))
				model.setJzztId(Long.parseLong(jzztId));
				model.setStartDate(format.parse(startDate));
				model.setEndDate(format.parse(endDate));
				model.setStartDate(format.parse(startDate.replaceAll("T"," ")));
				model.setEndDate(format.parse(endDate.replaceAll("T", " ")));
				if (keepTime != null && !keepTime.equals(""))
					model.setKeepTime(Double.valueOf(keepTime));
				if (reduceExert != null && !reduceExert.equals(""))
					model.setReduceExert(Double.valueOf(reduceExert));
				if (stopTimes != null && !stopTimes.equals(""))
					model.setStopTimes(Long.parseLong(stopTimes));
				if (successTimes != null && !successTimes.equals(""))
					model.setSuccessTimes(Long.parseLong(successTimes));
				if (failureTimes != null && !failureTimes.equals(""))
					model.setFailureTimes(Long.parseLong(failureTimes));
				if (repairMandays != null && !repairMandays.equals(""))
					model.setRepairMandays(Double.valueOf(repairMandays));
				if (repairCost != null && !repairCost.equals(""))
					model.setRepairCost(Double.valueOf(repairCost));
				model.setStopReason(stopReason);
				updateList.add(model);
			}

		}
		if (addList.size() > 0 || updateList.size() > 0
				|| deleteId.length() > 0)
			remote.save(addList, updateList, deleteId);
	}

	public void getBlockDependList() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject object = remote.findAll(employee.getEnterpriseCode(),
				Integer.parseInt(start), Integer.parseInt(limit));
		write(JSONUtil.serialize(object));
	}
}
