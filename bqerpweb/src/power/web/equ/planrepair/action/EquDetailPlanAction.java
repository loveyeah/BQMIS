package power.web.equ.planrepair.action;

import java.io.File;
import java.text.ParseException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.planrepair.EquJPlanRepair;
import power.ejb.equ.planrepair.EquJPlanRepairFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

@SuppressWarnings("serial")
public class EquDetailPlanAction extends UploadFileAbstractAction {
	private EquJPlanRepairFacadeRemote remote;
	private EquJPlanRepair prepair;
	private File annex;
	private int start;
	private int limit;

	public EquDetailPlanAction() {
		remote = (EquJPlanRepairFacadeRemote) factory
				.getFacadeRemote("EquJPlanRepairFacade");
	}

	public void saveDetailPlan() {
		String filePath = request.getParameter("filePath");
		if (remote.judgeAddDetailNo(prepair.getDetailNo())) {
			if (!filePath.equals("")) {
				String result = filePath
						.substring(filePath.lastIndexOf("\\") + 1);
				String fileName = result.replaceAll(" ", "");
				String[] filetemp = fileName.split("\\.");
				if (filetemp[1].equals("txt")) {
					filetemp[1] = ".doc";
					fileName = filetemp[0] + filetemp[1];
				}
				String Temp = uploadFile(annex, fileName, "planrepair");
				prepair.setAnnex(Temp);
			}
			prepair.setEnterpriseCode(employee.getEnterpriseCode());
			remote.save(prepair);
		} else {
			write("{failure : true,msg:'编号重复!'}");
		}
	}

	public void updateDetailPlan() {
		String filePath = request.getParameter("filePath");
		if (remote.judgeUpdateDetailNo(prepair.getDetailNo(), prepair
				.getRepairDetailId().toString())) {
			EquJPlanRepair entity = remote
					.findById(prepair.getRepairDetailId());
			prepair.setIsUse(entity.getIsUse());
			prepair.setEnterpriseCode(entity.getEnterpriseCode());
			if (!filePath.equals("")) {
				if (entity.getAnnex() != null
						&& filePath.equals(entity.getAnnex().substring(
								entity.getAnnex().lastIndexOf("/") + 1))) {
					prepair.setAnnex(entity.getAnnex());
				} else {
					String result = filePath.substring(filePath
							.lastIndexOf("\\") + 1);
					String fileName = result.replaceAll(" ", "");
					String[] filetemp = fileName.split("\\.");
					if (filetemp[1].equals("txt")) {
						filetemp[1] = ".doc";
						fileName = filetemp[0] + filetemp[1];
					}
					String Temp = uploadFile(annex, fileName, "planrepair");
					prepair.setAnnex(Temp);
				}
			} else {
				prepair.setAnnex(entity.getAnnex());
			}
			remote.update(prepair);
		} else {
			write("{failure : true,msg:'编号重复!'}");
		}
	}

	public void delDetailPlan() {
		String repairDetailId = request.getParameter("repairDetailId");
		EquJPlanRepair entity = remote.findById(Long.parseLong(repairDetailId));
		entity.setIsUse("N");
		remote.update(entity);
	}

	public void getDetailPlanList() throws JSONException, ParseException {
		String repairId = request.getParameter("repairId");
		PageObject object = remote.getDetailPlanList(repairId, employee
				.getEnterpriseCode());
		if (object != null) {
			write(JSONUtil.serialize(object));
		} else {
			write("[]");
		}
	}

	public EquJPlanRepair getPrepair() {
		return prepair;
	}

	public void setPrepair(EquJPlanRepair prepair) {
		this.prepair = prepair;
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

	public File getAnnex() {
		return annex;
	}

	public void setAnnex(File annex) {
		this.annex = annex;
	}

}
