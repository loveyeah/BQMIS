package power.web.operateticket.maint.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ejb.opticket.RunCOpMeasures;
import power.ejb.opticket.RunCOpMeasuresFacadeRemote;
import power.web.comm.AbstractAction;

public class DangerMeasureAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RunCOpMeasuresFacadeRemote remote;

	public DangerMeasureAction() {
		remote = (RunCOpMeasuresFacadeRemote) factory
				.getFacadeRemote("RunCOpMeasuresFacade");
	}

	public void updateConMeasureList() {
		String isAdd = request.getParameter("isAdd");
		String isUpdate = request.getParameter("isUpdate");
		String isDelete = request.getParameter("isDelete");
		List<Map> addList = this.toMapList(isAdd);
		List<Map> updateList = this.toMapList(isUpdate);
		boolean isSuccess = false;
		try {
			isSuccess = remote.saveAllOperat(addList, updateList, isDelete,
					employee.getWorkerCode(), employee.getEnterpriseCode());
			if (isSuccess) {
				write("{success:true,msg:'操作成功！  '}");
			} else {
				write("{success:true,msg:'操作失败！  '}");
			}
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'操作失败:名称重复！  '}");
		}
	}

	private List<Map> toMapList(String str) {
		List<Map> list = new ArrayList();
		if (str.length() > 2) {
			Object object = new Object();
			try {
				object = JSONUtil.deserialize(str);
				if (List.class.isAssignableFrom(object.getClass())) {
					list = (List<Map>) object;
				} else {
					Map map = (Map) object;
					list.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			list = null;
		}
		return list;
	}

	// try {
	// this.addDangerMeasure(isAdd);
	// this.updateDangerMeasure(isUpdate);
	// this.deleteDangerMeasure(isDelete);
	// write("{success:true,msg:'操作成功！ '}");
	// } catch (CodeRepeatException e) {
	// write("{success:true,msg:'操作失败:名称重复！ '}");
	// }
	//
	// }

	private void addDangerMeasure(String isAdd) throws CodeRepeatException {
		List<RunCOpMeasures> list = this.toarrayList(isAdd);
		for (int i = 0; i < list.size(); i++) {
			RunCOpMeasures model = list.get(i);
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setModifyBy(employee.getWorkerCode());
			try {
				remote.save(model);
			} catch (CodeRepeatException e) {
				throw new CodeRepeatException("名称重复");
			}
		}
	}

	private void updateDangerMeasure(String isUpdate)
			throws CodeRepeatException {
		List<RunCOpMeasures> list = this.toarrayList(isUpdate);
		for (int i = 0; i < list.size(); i++) {
			RunCOpMeasures model = list.get(i);
			RunCOpMeasures entity = remote.findById(model.getDangerId());
			entity.setDangerName(model.getDangerName());
			entity.setDisplayNo(model.getDisplayNo());
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			entity.setMeasureContent(model.getMeasureContent());
			entity.setMemo(model.getMemo());
			entity.setModifyBy(employee.getWorkerCode());
			entity.setModifyDate(new Date());
			try {
				remote.update(entity);
			} catch (CodeRepeatException e) {
				throw new CodeRepeatException("名称重复");
			}
		}
	}

	public void deleteDangerMeasure(String isDelete) {
		// String dangerIds = request.getParameter("dangerIds");
		String[] ids = isDelete.split(",");
		for (int i = 0; i < ids.length; i++) {
			RunCOpMeasures model = remote.findById(Long.parseLong(ids[i]));
			remote.delete(model);
		}
	}

	public void findDangerMeasure() throws JSONException {
		String operateTaskId = request.getParameter("operateTaskId");
		List<RunCOpMeasures> list = remote.findByTaskId(employee
				.getEnterpriseCode(), employee.getWorkerCode(), Long
				.parseLong(operateTaskId));
		String s = "";
		if (list != null && list.size() > 0) {
			s += "{total:" + list.size() + ",list:" + JSONUtil.serialize(list)
					+ "}";
		} else {
			s += "{total:0,list:[]}";
		}
		write(s);
	}

	private List<RunCOpMeasures> toarrayList(String str) {
		Object object = new Object();
		try {
			object = JSONUtil.deserialize(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List arrList = new ArrayList();
		if (object != null) {
			if (List.class.isAssignableFrom(object.getClass())) {
				List list = (List) object;
				int intLen = list.size();
				for (int i = 0; i < intLen; i++) {
					Map map = (Map) list.get(i);
					RunCOpMeasures data = this.setMeasures(map);
					arrList.add(data);
				}
			} else {
				Map map = (Map) object;
				RunCOpMeasures data = this.setMeasures(map);
				arrList.add(data);
			}
		}
		return arrList;
	}

	private RunCOpMeasures setMeasures(Map map) {
		RunCOpMeasures model = new RunCOpMeasures();
		if (map != null) {
			Object dangerId = map.get("dangerId");
			Object operateTaskId = map.get("operateTaskId");
			Object dangerName = map.get("dangerName");
			Object measureContent = map.get("measureContent");
			Object displayNo = map.get("displayNo");
			Object memo = map.get("memo");
			if (dangerId != null && !dangerId.toString().equals("")) {
				model.setDangerId(Long.parseLong(dangerId.toString()));
			}
			if (operateTaskId != null) {
				model
						.setOperateTaskId(Long.parseLong(operateTaskId
								.toString()));
			}
			if (dangerName != null) {
				model.setDangerName(dangerName.toString());
			}
			if (measureContent != null) {
				model.setMeasureContent(measureContent.toString());
			}
			if (displayNo != null) {
				model.setDisplayNo(Long.parseLong(displayNo.toString()));
			}
			if (memo != null) {
				model.setMemo(memo.toString());
			}
		}
		return model;
	}
}
