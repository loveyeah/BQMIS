package power.web.run.securityproduction.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpJSafeaward;
import power.ejb.run.securityproduction.SpJSafeawardDetails;
import power.ejb.run.securityproduction.SpJSafeawardDetailsFacadeRemote;
import power.ejb.run.securityproduction.SpJSafeawardFacadeRemote;
import power.ejb.run.securityproduction.form.SpJSafeawardForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SafeAwardAction extends AbstractAction {
	protected SpJSafeawardFacadeRemote remote;
	protected SpJSafeawardDetailsFacadeRemote dremote;
	private SpJSafeaward awardInfo;
	private String awardDate;
	private String method;

	public SafeAwardAction() {
		remote = (SpJSafeawardFacadeRemote) factory
				.getFacadeRemote("SpJSafeawardFacade");
		dremote = (SpJSafeawardDetailsFacadeRemote) factory
				.getFacadeRemote("SpJSafeawardDetailsFacade");
	}

	public void getSafeAwardList() throws JSONException {
		String awardDate = request.getParameter("awardDate");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		obj = remote.findByDate(awardDate, employee.getEnterpriseCode(), start,
				limit);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	public void getSafeAward() throws JSONException {
		String awardId = request.getParameter("awardId");
		SpJSafeawardForm model = new SpJSafeawardForm();
		if (awardId != null)
			model = remote.findFormModel(Long.parseLong(awardId));
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

	public void saveSafeAward() throws java.text.ParseException {
		if (("add").equals(method)) {
			awardInfo.setEnterpriseCode(employee.getEnterpriseCode());
			awardInfo.setEncourageDate(str2date(awardDate));
			if (remote.save(awardInfo))
				write("{success:true,msg:'保 存 成 功 ！'}");
			else
				write("{success:false,msg:'保 存 失 败 ！'}");
		} else if (("update").equals(method)) {
			SpJSafeaward model = new SpJSafeaward();
			model = remote.findModel(awardInfo.getAwardId());
			awardInfo.setEncourageDate(str2date(awardDate));
			awardInfo.setEnterpriseCode(model.getEnterpriseCode());
			if (remote.update(awardInfo))
				write("{success:true,msg:'更 新 成 功 ！'}");
			else
				write("{success:false,msg:'更 新 失 败 ！'}");
		} else {
			write("{success:false,msg:'未指定的操作 ！'}");
		}
	}

	public void deleteSafeAward() {
		if (remote.delete(awardInfo))
			write("{success:true,msg:'删 除 成 功 ！'}");
		else
			write("{success:false,msg:'删 除 失 败 ！'}");
	}

	public void getSafeAwardDetailsList() throws JSONException {
		String awardId = request.getParameter("awardId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		obj = dremote.getAwardManList(awardId, employee.getEnterpriseCode(),
				start, limit);
		write(JSONUtil.serialize(obj));
	}

	@SuppressWarnings("unchecked")
	public void saveSafeAwardDetailsList() throws JSONException {
		try {
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			String awardId = request.getParameter("awardId");
			Object obj = JSONUtil.deserialize(str);
			boolean ste = true;
			List<SpJSafeawardDetails> addList = new ArrayList<SpJSafeawardDetails>();
			List<SpJSafeawardDetails> updateList = new ArrayList<SpJSafeawardDetails>();
			List<Map> list = (List<Map>) obj;
			target1: for (Map data : list) {
				if (data.get("awardDetailsInfo.encourageMan") != null
						&& !("").equals(data
								.get("awardDetailsInfo.encourageMan"))) {
					SpJSafeawardDetails model = new SpJSafeawardDetails();
					if (data.get("awardDetailsInfo.awardDetailsId") != null
							&& !("").equals(data.get(
									"awardDetailsInfo.awardDetailsId")
									.toString()))
						model.setAwardDetailsId(Long.parseLong(data.get(
								"awardDetailsInfo.awardDetailsId").toString()));
					if (awardId != null)
						model.setSafeawardId(Long.parseLong(awardId));
					if (data.get("awardDetailsInfo.encourageMan") != null)
						model.setEncourageMan(data.get(
								"awardDetailsInfo.encourageMan").toString());
					if (data.get("awardDetailsInfo.encourageWay") != null)
						model.setEncourageWay(data.get(
								"awardDetailsInfo.encourageWay").toString());
					if (data.get("awardDetailsInfo.encourageMoney") != null
							&& !("").equals(data.get(
									"awardDetailsInfo.encourageMoney")
									.toString()))
						model.setEncourageMoney(Double.parseDouble(data.get(
								"awardDetailsInfo.encourageMoney").toString()));
					if (data.get("awardDetailsInfo.enterpriseCode") != null)
						model.setEnterpriseCode(data.get(
								"awardDetailsInfo.enterpriseCode").toString());
					else
						model.setEnterpriseCode(employee.getEnterpriseCode());
					if (model.getAwardDetailsId() == null)
						addList.add(model);
					else
						updateList.add(model);
				} else {
					{
						ste = false;
						break target1;
					}
				}
			}
			if (ste) {
				dremote.saveAwardManList(addList, updateList, deleteIds);
				write("{success:true,msg:'保 存 成 功 ！'}");
			} else
				write("{success:false,msg:'人 员 不 可 为 空 ！'}");
		} catch (RuntimeException e) {
			write("{success:false,msg:'保 存 失 败 ！'}");
		}
	}

	public SpJSafeaward getAwardInfo() {
		return awardInfo;
	}

	public void setAwardInfo(SpJSafeaward awardInfo) {
		this.awardInfo = awardInfo;
	}

	public String getAwardDate() {
		return awardDate;
	}

	public void setAwardDate(String awardDate) {
		this.awardDate = awardDate;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
