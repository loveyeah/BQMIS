package power.web.hr.reward;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.reward.HrCTechnicianStandards;
import power.ejb.hr.reward.HrCTechnicianStandardsFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class HrCTechnicianStandardsAction extends AbstractAction {
	private HrCTechnicianStandardsFacadeRemote remote;

	public HrCTechnicianStandardsAction() {
		remote = (HrCTechnicianStandardsFacadeRemote) factory
				.getFacadeRemote("HrCTechnicianStandardsFacade");
	}

	public void getTechnicianList() throws JSONException {
		PageObject obj = new PageObject();
		obj = remote.getTechnicianList(employee.getEnterpriseCode());
		if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list :[]}");
		}

	}

	public void deleteTechicianList() {
		String ids = request.getParameter("ids");
		remote.deleteTechicianList(ids);
		write("{success:true,msg:'删除成功！'}");

	}

	@SuppressWarnings("unchecked")
	public void saveTechicianList() throws java.text.ParseException, JSONException {
		String str = request.getParameter("isUpdate");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<HrCTechnicianStandards> addList = null;
		List<HrCTechnicianStandards> updateList = null;
		if (list != null && list.size() > 0) {
			addList = new ArrayList<HrCTechnicianStandards>();
			updateList = new ArrayList<HrCTechnicianStandards>();
			for (Map data : list) {

				String techId = null;
				String techStandard = null;
				String isEmploy = null;
				String effectStartTime = null;
				String effectEndTime = null;
				String memo = null;

				if (data.get("techId") != null
						&& !"".equals(data.get("techId")))
					techId = data.get("techId").toString();
				if (data.get("techStandard") != null
						&& !"".equals(data.get("techStandard")))
					techStandard = data.get("techStandard").toString();
				if (data.get("isEmploy") != null
						&& !"".equals(data.get("isEmploy")))
					isEmploy = data.get("isEmploy").toString();
				if (data.get("effectStartTime") != null
						&& !"".equals(data.get("effectStartTime")))
					effectStartTime = data.get("effectStartTime").toString();
				if (data.get("effectEndTime") != null
						&& !"".equals(data.get("effectEndTime")))
					effectEndTime = data.get("effectEndTime").toString();
				if (data.get("memo") != null && !"".equals(data.get("memo")))
					memo = data.get("memo").toString();

				HrCTechnicianStandards model = new HrCTechnicianStandards();
				if (techId == null) {
					if (techStandard != null) {
						model.setTechStandard(Double.parseDouble(techStandard));
					}
					if (isEmploy != null) {
						model.setIsEmploy(isEmploy);
					}
					if (effectStartTime != null) {
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						model.setEffectStartTime(sdf.parse(effectStartTime));
					}
					if (effectEndTime != null) {
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						model.setEffectEndTime(sdf.parse(effectEndTime));
					}
					if (memo != null)
						model.setMemo(memo);

					model.setEnterpriseCode(employee.getEnterpriseCode());
					model.setIsUse("Y");
					addList.add(model);
				} else {
					model = remote.findById(Long.parseLong(techId));
					if (techStandard != null) {
						model.setTechStandard(Double.parseDouble(techStandard));
					}
					if (isEmploy != null) {
						model.setIsEmploy(isEmploy);
					}
					if (effectStartTime != null) {
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						model.setEffectStartTime(sdf.parse(effectStartTime));
					}
					if (effectEndTime != null) {
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						model.setEffectEndTime(sdf.parse(effectEndTime));
					}
					if (memo != null)
						model.setMemo(memo);

					model.setEnterpriseCode(employee.getEnterpriseCode());
					model.setIsUse("Y");
					updateList.add(model);
				}
			}
		}

		remote.saveTechicianList(addList, updateList);
		write("{success:true,msg:'操作成功！'}");

	}
}
