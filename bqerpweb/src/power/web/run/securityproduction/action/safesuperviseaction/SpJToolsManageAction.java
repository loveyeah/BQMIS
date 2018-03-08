package power.web.run.securityproduction.action.safesuperviseaction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.safesupervise.SpCTools;
import power.ejb.run.securityproduction.safesupervise.SpJToolsManager;
import power.ejb.run.securityproduction.safesupervise.SpJToolsRepair;
import power.web.comm.AbstractAction;

/**
 * 
 * @author liuyi
 * 
 */
@SuppressWarnings("serial")
public class SpJToolsManageAction extends AbstractAction {
	private SpJToolsManager remote;
	private SpCTools tool;
	private SpJToolsRepair repair;

	public SpJToolsManageAction() {
		remote = (SpJToolsManager) factory
				.getFacadeRemote("SpJToolsManagerImpl");
	}

	/**
	 * 查询符合条件的电动工具和电气安全用具清册
	 * 
	 * @param toolName
	 *            名称
	 * @param toolModel
	 *            规格型号
	 * @param toolType
	 *            类别
	 * @param rowStartAndIdxCount
	 */
	public void findToolsByCondi() throws JSONException {
		String toolName = request.getParameter("toolName");
		String toolModel = request.getParameter("toolModel");
		String toolType = request.getParameter("toolType");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String isFiltrate = request.getParameter("isFiltrate");// 填写人过滤
		PageObject pg = null;
		if (start != null && limit != null)
			pg = remote.findToolsByCondi(toolName, toolModel, toolType,
					employee.getEnterpriseCode(), employee.getWorkerCode(),
					isFiltrate, Integer.parseInt(start), Integer
							.parseInt(limit));
		else
			pg = remote.findToolsByCondi(toolName, toolModel, toolType,
					employee.getEnterpriseCode(), employee.getWorkerCode(),
					isFiltrate);
		write(JSONUtil.serialize(pg));

	}

	/**
	 * 保存 电动工具和电气安全用具清册
	 * 
	 * @param entity
	 *            保存对象
	 */
	public void saveToolsEntity() {
		String str = "";
		if (tool.getToolId() == null) {
			tool.setChargeBy(employee.getWorkerCode());
			tool.setIsUse("Y");
			tool.setEnterpriseCode(employee.getEnterpriseCode());
			str = remote.saveToolsEntity(tool);
		} else {
			SpCTools entity = remote.findToolById(tool.getToolId());
			entity.setToolCode(tool.getToolCode());
			entity.setToolName(tool.getToolName());
			entity.setToolType(tool.getToolType());
			entity.setToolModel(tool.getToolModel());
			entity.setFactoryDate(tool.getFactoryDate());
			entity.setMemo(tool.getMemo());
			if (tool.getChargeBy()!=null&&!tool.getChargeBy().equals("")) {
				entity.setChargeBy(tool.getChargeBy());
			}
			str = remote.saveToolsEntity(entity);
		}
		write("{success:true,msg:'" + str + "'}");
	}

	/**
	 * 删除 电动工具和电气安全用具清册 多条删除
	 * 
	 * @param ids
	 *            删除ids
	 */
	public void deleteToolsEntity() {
		String ids = request.getParameter("ids");
		String str = remote.deleteToolsEntity(ids);
		write("{success:true,msg:'" + str + "'}");
	}

	/**
	 * 查询 电气安全用具检修记录
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param toolCode
	 *            编号
	 * @param toolType
	 *            类别
	 * @param rowStartAndIdxCount
	 */
	public void findToolsRepairObject() throws JSONException {
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String toolCode = request.getParameter("toolCode");
		String toolType = request.getParameter("toolType");
		String isMaint = request.getParameter("isMaint");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = null;
		if (start != null && limit != null)
			pg = remote.findToolsRepairObject(beginTime, endTime, toolCode,
					toolType, employee.getEnterpriseCode(), isMaint, employee
							.getWorkerCode(), Integer.parseInt(start), Integer
							.parseInt(limit));
		else
			pg = remote.findToolsRepairObject(beginTime, endTime, toolCode,
					toolType, employee.getEnterpriseCode(), isMaint, employee
							.getWorkerCode());
		write(JSONUtil.serialize(pg));
	}

	/**
	 * 保存 电气安全用具检修记录
	 * 
	 * @param entity
	 */
	public void saveRepairEntity() {
		String str = "";
		if (repair.getRepairId() == null) {
			repair.setFillBy(employee.getWorkerCode());
			repair.setIsUse("Y");
			repair.setEnterpriseCode(employee.getEnterpriseCode());
			str = remote.saveRepairEntity(repair);
		} else {
			SpJToolsRepair entity = remote.findRepairById(repair.getRepairId());
			entity.setToolId(repair.getToolId());
			entity.setBelongDep(repair.getBelongDep());
			entity.setRepairResult(repair.getRepairResult());
			entity.setRepairBegin(repair.getRepairBegin());
			entity.setRepairEnd(repair.getRepairEnd());
			entity.setRepairBy(repair.getRepairBy());
			entity.setRepairDep(repair.getRepairDep());
			entity.setNextTime(repair.getNextTime());
			entity.setMemo(repair.getMemo());
			repair.setFillBy(employee.getWorkerCode());
			str = remote.saveRepairEntity(entity);
		}
		write("{success:true,msg:'" + str + "'}");
	}

	/**
	 * 删除 电气安全用具检修记录
	 * 
	 * @param ids
	 */
	public void deleteRepairEntity() {
		String ids = request.getParameter("ids");
		write("{success:true,msg:'" + remote.deleteRepairEntity(ids) + "'}");
	}

	public SpCTools getTool() {
		return tool;
	}

	public void setTool(SpCTools tool) {
		this.tool = tool;
	}

	public SpJToolsRepair getRepair() {
		return repair;
	}

	public void setRepair(SpJToolsRepair repair) {
		this.repair = repair;
	}
}