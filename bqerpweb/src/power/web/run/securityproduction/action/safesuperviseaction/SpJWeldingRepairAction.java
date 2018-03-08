package power.web.run.securityproduction.action.safesuperviseaction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.safesupervise.SpJWeldingRepair;
import power.ejb.run.securityproduction.safesupervise.SpJWeldingRepairFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SpJWeldingRepairAction extends AbstractAction {

	protected SpJWeldingRepairFacadeRemote remote;
	private SpJWeldingRepair welding;

	public SpJWeldingRepairAction() {
		remote = (SpJWeldingRepairFacadeRemote) factory
				.getFacadeRemote("SpJWeldingRepairFacade");
	}

	public void findWeldingList() throws JSONException {
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String toolCode = request.getParameter("toolCode");
		String toolType = request.getParameter("toolType");
		String isMaint = request.getParameter("isMaint");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject tl = new PageObject();
		if (start != null && limit != null) {
			tl = remote.getWelding(beginTime, endTime, toolCode, toolType,
					employee.getEnterpriseCode(), isMaint, employee
							.getWorkerCode(), Integer.parseInt(start), Integer
							.parseInt(limit));
		} else {
			tl = remote.getWelding(beginTime, endTime, toolCode, toolType,
					employee.getEnterpriseCode(), isMaint, employee
							.getWorkerCode());
		}
		if (tl != null) {
			write(JSONUtil.serialize(tl));
		} else {
			write("{totalCount : 0,list :[]}");
		}
	}

	public void deleteWelding() {
		String ids = request.getParameter("ids");
		String str = remote.deleteWeldinglist(ids);
		write("{success:true,msg:'" + str + "'}");
	}

	public void saveWelding() {
		if (welding.getRepairId() == null) {
			welding.setFillBy(employee.getWorkerCode());
			welding.setIsUse("Y");
			welding.setEnterpriseCode(employee.getEnterpriseCode());
			remote.save(welding);
		} else {
			SpJWeldingRepair entity = remote.findById(welding.getRepairId());
			entity.setToolId(welding.getToolId());
			entity.setBelongDep(welding.getBelongDep());
			entity.setMadeDate(welding.getMadeDate());
			entity.setInsulation(welding.getInsulation());
			entity.setResistance(welding.getResistance());
			entity.setOutsideCheck(welding.getOutsideCheck());
			entity.setRepairResult(welding.getRepairResult());
			entity.setRepairBy(welding.getRepairBy());
			entity.setRepairBegin(welding.getRepairBegin());
			entity.setRepairEnd(welding.getRepairEnd());
			entity.setNextTime(welding.getNextTime());
			entity.setMemo(welding.getMemo());
			remote.update(entity);
		}
	}

	public SpJWeldingRepair getWelding() {
		return welding;
	}

	public void setWelding(SpJWeldingRepair welding) {
		this.welding = welding;
	}

}