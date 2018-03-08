package power.web.run.securityproduction.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpJSafetraining;
import power.ejb.run.securityproduction.SpJSafetrainingAttend;
import power.ejb.run.securityproduction.SpJSafetrainingAttendFacadeRemote;
import power.ejb.run.securityproduction.SpJSafetrainingFacadeRemote;
import power.web.comm.AbstractAction;

public class SpJSafetrainingAction extends AbstractAction {

	private SpJSafetrainingFacadeRemote remote;
	private SpJSafetrainingAttendFacadeRemote attendRemote;

	private int start;
	private int limit;
	private SpJSafetraining baseInfo;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public SpJSafetrainingAction() {
		remote = (SpJSafetrainingFacadeRemote) factory
				.getFacadeRemote("SpJSafetrainingFacade");
		attendRemote = (SpJSafetrainingAttendFacadeRemote) factory
				.getFacadeRemote("SpJSafetrainingAttendFacade");
	}

	public void saveSpJSafetraining() throws Exception {
		String trainingTime = request.getParameter("trainingTime");
		System.out.println(trainingTime.toString());
		if (baseInfo.getTrainingId() == null) {
			baseInfo.setTrainingTime(this.str2date(trainingTime));
			baseInfo.setEnterpriseCode(employee.getEnterpriseCode());
			if(remote.save(baseInfo))
				write("{success:true,msg:'保 存 成 功 ！'}");
			else
				write("{success:false,msg:'保 存 失 败 ！'}");
			

		} else {
			SpJSafetraining model = new SpJSafetraining();
			model = remote.findById(baseInfo.getTrainingId());
			baseInfo.setTrainingTime(this.str2date(trainingTime));
			baseInfo.setEnterpriseCode(employee.getEnterpriseCode());
			if(remote.update(baseInfo))
			    write("{success:true,msg:'更 新 成 功 ！'}");
			else
				write("{success:false,msg:'更 新 失 败 ！'}");
		}

	}

	public void deleteSpJSafetraining() throws Exception {
		 String trainingId=request.getParameter("trainingId");
		 
		 if(remote.delete(remote.findById(Long.parseLong(trainingId)))){
			 attendRemote.deleteById(Long.parseLong(trainingId));
		 }

	}

	@SuppressWarnings("unchecked")
	public void saveSpJSafetrainingAttend() throws Exception {
		try {
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);
			List<SpJSafetrainingAttend> addList = new ArrayList<SpJSafetrainingAttend>();
			List<SpJSafetrainingAttend> updateList = new ArrayList<SpJSafetrainingAttend>();
			List<Map> list = (List<Map>) obj;
			for (Map data : list) {

				String attendId = null;
				String trainingId = null;

				String attendCode = null;

				if (data.get("atnInfo.attendId") != null) {
					attendId = data.get("atnInfo.attendId").toString();
				}

				if (data.get("atnInfo.trainingId") != null) {
					trainingId = data.get("atnInfo.trainingId").toString();
				}

				if (data.get("atnInfo.attendCode") != null) {
					attendCode = data.get("atnInfo.attendCode").toString();
				}

				SpJSafetrainingAttend model = new SpJSafetrainingAttend();

				// 增加
				if (attendId == null || "".equals(attendId)) {

					if (trainingId != null && !trainingId.equals(""))
						model.setTrainingId(Long.parseLong(trainingId));
					model.setAttendCode(attendCode);

					model.setEnterpriseCode(employee.getEnterpriseCode());

					addList.add(model);
				} else {
					model = attendRemote.findById(Long.parseLong(attendId));

					if (trainingId != null && !trainingId.equals(""))

						model.setAttendCode(attendCode);

					updateList.add(model);
				}

			}

			if (addList.size() > 0)
				attendRemote.save(addList);

			if (updateList.size() > 0)

				attendRemote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))

				attendRemote.delete(deleteIds);

			write("{success: true,msg:'保存成功！'}");

		} catch (Exception exc) {
			write("{success: false,msg:'操作失败'}");
			throw exc;

		}
	}
	private Date str2date(String s) throws java.text.ParseException {
		Date datea = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(s);
		datea = date;
		return datea;
	}

	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */

	public void getSpJSafetrainingList() throws JSONException {
		
		String argFuzzy = request.getParameter("argFuzzy");
		PageObject obj = remote.findAll(argFuzzy,employee.getEnterpriseCode(), start,
				limit);

		write(JSONUtil.serialize(obj));
	}

	public void getSpJSafetrainingAttendList() throws JSONException {
		String trainingId = request.getParameter("trainingId");
		PageObject obj = attendRemote.findAll(trainingId, employee
				.getEnterpriseCode(), start, limit);

		write(JSONUtil.serialize(obj));
	}

	// ******************************************get/set变量方法******************************************

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	public SpJSafetraining getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(SpJSafetraining baseInfo) {
		this.baseInfo = baseInfo;
	}
}
