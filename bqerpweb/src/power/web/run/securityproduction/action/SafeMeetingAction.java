package power.web.run.securityproduction.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpJMonthSafeMeeting;
import power.ejb.run.securityproduction.SpJMonthSafeMeetingFacadeRemote;
import power.ejb.run.securityproduction.SpJSafemeetingAbsence;
import power.ejb.run.securityproduction.SpJSafemeetingAbsenceFacadeRemote;
import power.ejb.run.securityproduction.SpJSafemeetingAttend;
import power.ejb.run.securityproduction.SpJSafemeetingAttendFacadeRemote;
import power.ejb.run.securityproduction.form.SpJMonthSafeMeetingFrom;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SafeMeetingAction extends AbstractAction {

	protected SpJMonthSafeMeetingFacadeRemote remote;
	protected SpJSafemeetingAttendFacadeRemote atnremote;
	protected SpJSafemeetingAbsenceFacadeRemote absremote;
	private SpJMonthSafeMeeting meetingInfo;
	private String meetingDate;
	private String method;

	public SafeMeetingAction() {
		remote = (SpJMonthSafeMeetingFacadeRemote) factory
				.getFacadeRemote("SpJMonthSafeMeetingFacade");
		atnremote = (SpJSafemeetingAttendFacadeRemote) factory
				.getFacadeRemote("SpJSafemeetingAttendFacade");
		absremote = (SpJSafemeetingAbsenceFacadeRemote) factory
				.getFacadeRemote("SpJSafemeetingAbsenceFacade");
	}

	public void getSafeMeetingList() throws JSONException {
		String meetingDate = request.getParameter("meetingDate");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		obj = remote.findByDate(meetingDate, employee.getEnterpriseCode(),
				start, limit);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	public void getSafeMeeting() throws JSONException {
		String meetingId = request.getParameter("meetingId");
		SpJMonthSafeMeetingFrom model = new SpJMonthSafeMeetingFrom();
		if (meetingId != null)
			model = remote.findFromModel(Long.parseLong(meetingId));
		String str = JSONUtil.serialize(model);
		write(str);
	}

	private Date str2date(String s) throws java.text.ParseException {
		Date datea = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date date = sdf.parse(s);
		datea = date;
		return datea;
	}

	public void saveSafeMeeting() throws java.text.ParseException {
		if (("add").equals(method)) {
			meetingInfo.setEnterpriseCode(employee.getEnterpriseCode());
			meetingInfo.setMeetingDate(str2date(meetingDate));
			if (remote.save(meetingInfo))
				write("{success:true,msg:'保 存 成 功 ！'}");
			else
				write("{success:false,msg:'保 存 失 败 ！'}");
		} else if (("update").equals(method)) {
			SpJMonthSafeMeeting model = new SpJMonthSafeMeeting();
			model = remote.findModel(meetingInfo.getMeetingId());
			meetingInfo.setMeetingDate(str2date(meetingDate));
			meetingInfo.setEnterpriseCode(model.getEnterpriseCode());
			if (remote.update(meetingInfo))
				write("{success:true,msg:'更 新 成 功 ！'}");
			else
				write("{success:false,msg:'更 新 失 败 ！'}");
		} else {
			write("{success:false,msg:'未指定的操作 ！'}");
		}
	}

	public void deleteSafeMeeting() {
		if (remote.delete(meetingInfo))
			write("{success:true,msg:'删 除 成 功 ！'}");
		else
			write("{success:false,msg:'删 除 失 败 ！'}");
	}

	public void getSafeMeetingAtnList() throws JSONException {
		String meetingId = request.getParameter("meetingId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		obj = atnremote.getAtnList(meetingId, employee.getEnterpriseCode(),
				start, limit);
		write(JSONUtil.serialize(obj));
	}

	@SuppressWarnings("unchecked")
	public void saveSafeMeetingAtnList() throws JSONException {
		try {
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			String meetingId = request.getParameter("meetingId");
			Object obj = JSONUtil.deserialize(str);
			boolean ste = true;
			List<SpJSafemeetingAttend> addList = new ArrayList<SpJSafemeetingAttend>();
			List<SpJSafemeetingAttend> updateList = new ArrayList<SpJSafemeetingAttend>();
			List<Map> list = (List<Map>) obj;

			target1: for (Map data : list) {
				if (data.get("atnInfo.workerCode") != null) {
					SpJSafemeetingAttend model = new SpJSafemeetingAttend();
					if (data.get("atnInfo.attendId") != null
							&& !("").equals(data.get("atnInfo.attendId")
									.toString()))
						model.setAttendId(Long.parseLong(data.get(
								"atnInfo.attendId").toString()));
					if (meetingId != null)
						model.setMeetingId(Long.parseLong(meetingId));
					if (data.get("atnInfo.workerCode") != null)
						model.setWorkerCode(data.get("atnInfo.workerCode")
								.toString());
					if (data.get("atnInfo.depCode") != null)
						model
								.setDepCode(data.get("atnInfo.depCode")
										.toString());
					if (data.get("atnInfo.enterpriseCode") != null)
						model.setEnterpriseCode(data.get(
								"atnInfo.enterpriseCode").toString());
					else
						model.setEnterpriseCode(employee.getEnterpriseCode());
					if (model.getAttendId() == null)
						addList.add(model);
					else
						updateList.add(model);
				} else {
					ste = false;
					break target1;
				}
			}
			if (ste) {
				atnremote.saveAtnList(addList, updateList, deleteIds);
				write("{success:true,msg:'保 存 成 功 ！'}");
			} else
				write("{success:false,msg:'人 员 不 可 为 空 ！'}");
		} catch (RuntimeException e) {
			write("{success:false,msg:'保 存 失 败 ！'}");
		}
	}

	public void getSafeMeetingAbsList() throws JSONException {
		String meetingId = request.getParameter("meetingId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		obj = absremote.getAbsList(meetingId, employee.getEnterpriseCode(),
				start, limit);
		write(JSONUtil.serialize(obj));
	}

	@SuppressWarnings("unchecked")
	public void saveSafeMeetingAbsList() throws JSONException {
		try {
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			String meetingId = request.getParameter("meetingId");
			Object obj = JSONUtil.deserialize(str);
			boolean ste = true;
			List<SpJSafemeetingAbsence> addList = new ArrayList<SpJSafemeetingAbsence>();
			List<SpJSafemeetingAbsence> updateList = new ArrayList<SpJSafemeetingAbsence>();
			List<Map> list = (List<Map>) obj;
			target1: for (Map data : list) {
				if (data.get("absInfo.workerCode") != null
						&& !("").equals(data.get("absInfo.workerCode"))) {
					SpJSafemeetingAbsence model = new SpJSafemeetingAbsence();
					if (data.get("absInfo.absenceId") != null
							&& !("").equals(data.get("absInfo.absenceId")
									.toString()))
						model.setAbsenceId(Long.parseLong(data.get(
								"absInfo.absenceId").toString()));
					if (meetingId != null)
						model.setMeetingId(Long.parseLong(meetingId));
					if (data.get("absInfo.workerCode") != null)
						model.setWorkerCode(data.get("absInfo.workerCode")
								.toString());
					if (data.get("absInfo.depCode") != null)
						model
								.setDepCode(data.get("absInfo.depCode")
										.toString());
					if (data.get("absInfo.reason") != null)
						model.setReason(data.get("absInfo.reason").toString());
					if (data.get("absInfo.makeupRecord") != null)
						model.setMakeupRecord(data.get("absInfo.makeupRecord")
								.toString());
					if (data.get("absInfo.enterpriseCode") != null)
						model.setEnterpriseCode(data.get(
								"absInfo.enterpriseCode").toString());
					else
						model.setEnterpriseCode(employee.getEnterpriseCode());
					if (model.getAbsenceId() == null)
						addList.add(model);
					else
						updateList.add(model);
				} else {
					ste = false;
					break target1;
				}
			}
			if (ste) {
				absremote.saveAbsList(addList, updateList, deleteIds);
				write("{success:true,msg:'保 存 成 功 ！'}");
			} else
				write("{success:false,msg:'人 员 不 可 为 空 ！'}");
		} catch (RuntimeException e) {
			write("{success:false,msg:'保 存 失 败 ！'}");
		}
	}

	public SpJMonthSafeMeeting getMeetingInfo() {
		return meetingInfo;
	}

	public void setMeetingInfo(SpJMonthSafeMeeting meetingInfo) {
		this.meetingInfo = meetingInfo;
	}

	public String getMeetingDate() {
		return meetingDate;
	}

	public void setMeetingDate(String meetingDate) {
		this.meetingDate = meetingDate;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
