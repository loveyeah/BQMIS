package power.web.run.securityproduction.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpJSafepunish;
import power.ejb.run.securityproduction.SpJSafepunishDetails;
import power.ejb.run.securityproduction.SpJSafepunishDetailsFacadeRemote;
import power.ejb.run.securityproduction.SpJSafepunishFacadeRemote;
import power.ejb.run.securityproduction.form.SpJSafepunishForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SafePunishAction extends AbstractAction {
	protected SpJSafepunishFacadeRemote remote;
	protected SpJSafepunishDetailsFacadeRemote dremote;
	private SpJSafepunish punishInfo;
	private String punishDate;
	private String method;

	public SafePunishAction() {
		remote = (SpJSafepunishFacadeRemote) factory
				.getFacadeRemote("SpJSafepunishFacade");
		dremote = (SpJSafepunishDetailsFacadeRemote) factory
				.getFacadeRemote("SpJSafepunishDetailsFacade");
	}

	public void getSafePunishList() throws JSONException {
		String punishDate = request.getParameter("punishDate");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		obj = remote.findByDate(punishDate, employee.getEnterpriseCode(),
				start, limit);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	public void getSafePunish() throws JSONException {
		String punishId = request.getParameter("punishId");
		SpJSafepunishForm model = new SpJSafepunishForm();
		if (punishId != null)
			model = remote.findFormModel(Long.parseLong(punishId));
		String str = JSONUtil.serialize(model);
		write(str);
	}

	private Date str2date(String s) throws java.text.ParseException {
		Date datea = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(s);
		datea = date;
		return datea;
	}

	public void saveSafePunish() throws java.text.ParseException {
		if (("add").equals(method)) {
			punishInfo.setEnterpriseCode(employee.getEnterpriseCode());
			punishInfo.setPunishDate(str2date(punishDate));
			if (remote.save(punishInfo))
				write("{success:true,msg:'保 存 成 功 ！'}");
			else
				write("{success:false,msg:'保 存 失 败 ！'}");
		} else if (("update").equals(method)) {
			SpJSafepunish model = new SpJSafepunish();
			model = remote.findModel(punishInfo.getPunishId());
			punishInfo.setPunishDate(str2date(punishDate));
			punishInfo.setEnterpriseCode(model.getEnterpriseCode());
			if (remote.update(punishInfo))
				write("{success:true,msg:'更 新 成 功 ！'}");
			else
				write("{success:false,msg:'更 新 失 败 ！'}");
		} else {
			write("{success:false,msg:'未指定的操作 ！'}");
		}
	}

	public void deleteSafePunish() {
		if (remote.delete(punishInfo))
			write("{success:true,msg:'删 除 成 功 ！'}");
		else
			write("{success:false,msg:'删 除 失 败 ！'}");
	}

	public void getSafePunishDetailsList() throws JSONException {
		String punishId = request.getParameter("punishId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		obj = dremote.getPunishManList(punishId, employee.getEnterpriseCode(),
				start, limit);
		write(JSONUtil.serialize(obj));
	}

	@SuppressWarnings("unchecked")
	public void saveSafePunishDetailsList() throws JSONException {
		try {
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			String punishId = request.getParameter("punishId");
			Object obj = JSONUtil.deserialize(str);
			boolean ste = true;
			List<SpJSafepunishDetails> addList = new ArrayList<SpJSafepunishDetails>();
			List<SpJSafepunishDetails> updateList = new ArrayList<SpJSafepunishDetails>();
			List<Map> list = (List<Map>) obj;
			target1: for (Map data : list) {
				if (data.get("punishDetailsInfo.punishMan") != null
						&& !("")
								.equals(data.get("punishDetailsInfo.punishMan"))) {
					SpJSafepunishDetails model = new SpJSafepunishDetails();
					if (data.get("punishDetailsInfo.punishDetailsId") != null
							&& !("").equals(data.get(
									"punishDetailsInfo.punishDetailsId")
									.toString()))
						model.setPunishDetailsId(Long
								.parseLong(data.get(
										"punishDetailsInfo.punishDetailsId")
										.toString()));
					if (punishId != null)
						model.setPunishId(Long.parseLong(punishId));
					if (data.get("punishDetailsInfo.punishMan") != null)
						model.setPunishMan(data.get(
								"punishDetailsInfo.punishMan").toString());
					if (data.get("punishDetailsInfo.punishType") != null)
						model.setPunishType(data.get(
								"punishDetailsInfo.punishType").toString());
					if (data.get("punishDetailsInfo.punishMoney") != null
							&& !("")
									.equals(data.get(
											"punishDetailsInfo.punishMoney")
											.toString()))
						model.setPunishMoney(Double.parseDouble(data.get(
								"punishDetailsInfo.punishMoney").toString()));
					if (data.get("punishDetailsInfo.enterpriseCode") != null)
						model.setEnterpriseCode(data.get(
								"punishDetailsInfo.enterpriseCode").toString());
					else
						model.setEnterpriseCode(employee.getEnterpriseCode());
					if (model.getPunishDetailsId() == null)
						addList.add(model);
					else
						updateList.add(model);
				} else {
					ste = false;
					break target1;
				}
			}
			if (ste) {
				dremote.savePunishManList(addList, updateList, deleteIds);
				write("{success:true,msg:'保 存 成 功 ！'}");
			} else
				write("{success:false,msg:'人 员 不 可 为 空 ！'}");
		} catch (RuntimeException e) {
			write("{success:false,msg:'保 存 失 败 ！'}");
		}
	}

	public SpJSafepunish getPunishInfo() {
		return punishInfo;
	}

	public void setPunishInfo(SpJSafepunish punishInfo) {
		this.punishInfo = punishInfo;
	}

	public String getPunishDate() {
		return punishDate;
	}

	public void setPunishDate(String punishDate) {
		this.punishDate = punishDate;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
