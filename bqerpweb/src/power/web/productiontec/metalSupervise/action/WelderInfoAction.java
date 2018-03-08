package power.web.productiontec.metalSupervise.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.metalSupervise.PtJsjdJHgjbxx;
import power.ejb.productiontec.metalSupervise.PtJsjdJHgjbxxFacadeRemote;
import power.ejb.productiontec.metalSupervise.PtJsjdJHgjnkhb;
import power.ejb.productiontec.metalSupervise.PtJsjdJHgjnkhbFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class WelderInfoAction extends AbstractAction {
	private PtJsjdJHgjbxx model;
	private PtJsjdJHgjbxxFacadeRemote jbxxRemote;
	private PtJsjdJHgjnkhbFacadeRemote jnkhbRemote;
	private int start;
	private int limit;

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

	public WelderInfoAction() {
		jbxxRemote = (PtJsjdJHgjbxxFacadeRemote) factory
				.getFacadeRemote("PtJsjdJHgjbxxFacade");
		jnkhbRemote = (PtJsjdJHgjnkhbFacadeRemote) factory
				.getFacadeRemote("PtJsjdJHgjnkhbFacade");
	}

	public void saveJbxx() {
		model.setEnterpriseCode(employee.getEnterpriseCode());
		jbxxRemote.save(model);
		write("{success:true}");
	}

	public void updateJbxx() {
		PtJsjdJHgjbxx entity = jbxxRemote.findById(model.getWeldId());
		entity.setWeldAge(model.getWeldAge());
		entity.setWeldCode(model.getWeldCode());
		entity.setWeldWorkDate(model.getWeldWorkDate());
		entity.setWorkerCode(model.getWorkerCode());
		entity.setWeldLevel(model.getWeldLevel());//add by drdu 091106
		jbxxRemote.update(entity);
		write("{success:true}");
	}

	public void deleteJbxx() {
		String idString = request.getParameter("ids");
		jbxxRemote.delete(idString);
	}

	public void findJbxxList() throws JSONException {
		String fuzzyString = request.getParameter("argFuzzy");
		PageObject object = jbxxRemote.getPtJsjdJHgjbxxList(fuzzyString,
				employee.getEnterpriseCode(), start, limit);
		write(JSONUtil.serialize(object));
	}

	public void findJnkhbListByweldId() throws JSONException {
		String weldId = request.getParameter("weldId");
		List<PtJsjdJHgjnkhb> list = jnkhbRemote.findByWeldId(Long
				.parseLong(weldId));
		write(JSONUtil.serialize(list));
	}

	@SuppressWarnings("unchecked")
	public void saveJnkhb() throws Exception {
		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<PtJsjdJHgjnkhb> addList = new ArrayList<PtJsjdJHgjnkhb>();
			List<PtJsjdJHgjnkhb> updateList = new ArrayList<PtJsjdJHgjnkhb>();

			List<Map> list = (List<Map>) obj;

			for (Map data : list) {

				String hgjnkhId = null;
				String weldId = null;
				Date examDate = null;
				Date fetchDate = null;
				String checkUnit = null;
				String sendUnit = null;
				String cardCode = null;
				String steelCode = null;
				Date nextCheckDate = null;

				if (data.get("hgjnkhId") != null) {
					hgjnkhId = data.get("hgjnkhId").toString();
				}
				if (data.get("weldId") != null) {
					weldId = data.get("weldId").toString();
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if (data.get("examDate") != null) {
					examDate = sdf.parse(data.get("examDate").toString());
				}

				if (data.get("fetchDate") != null) {
					fetchDate = sdf.parse(data.get("fetchDate").toString());
				}

				if (data.get("checkUnit") != null) {
					checkUnit = data.get("checkUnit").toString();
				}
				if (data.get("sendUnit") != null) {
					sendUnit = data.get("sendUnit").toString();
				}
				if (data.get("cardCode") != null) {
					cardCode = data.get("cardCode").toString();
				}
				if (data.get("steelCode") != null) {
					steelCode = data.get("steelCode").toString();
				}

				if (data.get("nextCheckDate") != null) {
					nextCheckDate = sdf.parse(data.get("nextCheckDate")
							.toString());
				}

				PtJsjdJHgjnkhb model = new PtJsjdJHgjnkhb();

				// 增加
				if (hgjnkhId == null) {

					model.setCardCode(cardCode);
					model.setCheckUnit(checkUnit);
					model.setExamDate(examDate);
					model.setFetchDate(fetchDate);
					model.setNextCheckDate(nextCheckDate);
					model.setSendUnit(sendUnit);
					model.setSteelCode(steelCode);
					model.setWeldId(Long.parseLong(weldId));
					model.setEnterpriseCode(employee.getEnterpriseCode());
					addList.add(model);

				} else {
					model = jnkhbRemote.findById(Long.parseLong(hgjnkhId));

					model.setCardCode(cardCode);
					model.setCheckUnit(checkUnit);
					model.setExamDate(examDate);
					model.setFetchDate(fetchDate);
					model.setNextCheckDate(nextCheckDate);
					model.setSendUnit(sendUnit);
					model.setSteelCode(steelCode);
					model.setWeldId(Long.parseLong(weldId));

					updateList.add(model);

				}

			}

			if (addList.size() > 0 || updateList.size() > 0
					|| (deleteIds != null && !deleteIds.trim().equals("")))
				jnkhbRemote.save(addList, updateList, deleteIds);
			write("{success:true,msg:'保存成功！'}");

		} catch (Exception exc) {
			write("{success:false,msg:'操作失败'}");
			throw exc;

		}
	}

	public PtJsjdJHgjbxx getModel() {
		return model;
	}

	public void setModel(PtJsjdJHgjbxx model) {
		this.model = model;
	}

}