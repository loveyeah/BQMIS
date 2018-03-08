package power.web.equ.planrepair.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.planrepair.EquJPlanRepairMain;
import power.ejb.equ.planrepair.EquJPlanRepairMainFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquPlanRegisterAction extends AbstractAction {
	private EquJPlanRepairMainFacadeRemote remote;
	private EquJPlanRepairMain prMain;
	private int start;
	private int limit;

	public EquPlanRegisterAction() {
		remote = (EquJPlanRepairMainFacadeRemote) factory
				.getFacadeRemote("EquJPlanRepairMainFacade");
	}

	public void savePlanRepair() {
		if (remote.judgeAddPlanCode(prMain.getRepairCode())) {
			prMain.setEnterpriseCode(employee.getEnterpriseCode());
			EquJPlanRepairMain entity = remote.save(prMain);
			write("{success : true ,repairId :" + entity.getRepairId() + ",status : '初始'}");
		} else {
			write("{failure : true,msg:'编号重复!'}");
		}

	}

	public void updatePlanRepair() {
		String repairId = request.getParameter("repairId");
		EquJPlanRepairMain entity = remote.findById(Long.parseLong(repairId));
		if (remote.judgeUpdatePlanCode(repairId, prMain.getRepairCode())) {
			prMain.setRepairId(entity.getRepairId());
			prMain.setEnterpriseCode(entity.getEnterpriseCode());
			prMain.setIsUse(entity.getIsUse());
			prMain.setStatus(entity.getStatus());
			remote.update(prMain);
			write("{success : true ,repairId :" + entity.getRepairId() + ",status : '初始'}");
		} else {
			write("{failure : true,msg:'编号重复!'}");
		}
	}

	public void updatePlanStatus() {
		String repairId = request.getParameter("repairId");
		String status = request.getParameter("status");
		EquJPlanRepairMain entity = remote.findById(Long.parseLong(repairId));
		entity.setStatus(status);
		remote.update(entity);
	}

	public void delPlanRepair() {
		String repairId = request.getParameter("repairId");
		EquJPlanRepairMain entity = remote.findById(Long.parseLong(repairId));
		entity.setIsUse("N");
		remote.update(entity);
	}
//登记查询
	public void getPlanRepairList() throws JSONException {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		PageObject object = remote.getPlanRepairList(startDate, endDate, start,
				limit);
		if (object != null) {
			write(JSONUtil.serialize(object));
		} else {
			write("{list : [],totalCount : 0}");
		}

	}

	public void planRepairReport() {
		String repairId = request.getParameter("repairId");
		String nextRoles = request.getParameter("nextRoles");
		String approveText = request.getParameter("approveText");
		String actionId= request.getParameter("actionId");
		remote.reportPlanRepair(Long.parseLong(repairId), nextRoles, employee
				.getWorkerCode(),Long.parseLong(actionId), approveText);
		write("{success : true,msg:'上报成功!'}");
	}
//综合查询
	public void getAllPlanRepairList() throws JSONException{
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
	 PageObject object =	remote.getAllPlanRepairList(startDate, endDate, start,
				limit);
		if (object != null) {
			write(JSONUtil.serialize(object));
		} else {
			write("{list : [],totalCount : 0}");
		}
	}
	
	public EquJPlanRepairMain getPrMain() {
		return prMain;
	}

	public void setPrMain(EquJPlanRepairMain prMain) {
		this.prMain = prMain;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
