package power.web.operateticket.maint.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ejb.opticket.RunCOpStepcheck;
import power.ejb.opticket.RunCOpStepcheckFacadeRemote;
import power.web.comm.AbstractAction;

public class CheckStepAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RunCOpStepcheckFacadeRemote remote;

	public CheckStepAction() {
		remote = (RunCOpStepcheckFacadeRemote) factory
				.getFacadeRemote("RunCOpStepcheckFacade");
	}

	public void updateStepChecklist() {
		String isAdd = request.getParameter("isAdd");
		String isUpdate = request.getParameter("isUpdate");
		String isDelete=request.getParameter("isDelete");
		List<Map> addList=this.toMapList(isAdd);
		List<Map> updateList=this.toMapList(isUpdate);
		boolean isSuccess=false;
		try {
			isSuccess=remote.saveAllOperat(addList, updateList, isDelete,employee.getWorkerCode(),employee.getEnterpriseCode());
			if(isSuccess){
				write("{success:true,msg:'操作成功！  '}");
			}
			else{
				write("{success:true,msg:'操作失败！  '}");
			}
//			this.addCheckStep(isAdd);
//			this.updateCheckStep(isUpdate);
//			this.deleteCheckStep(isDelete);
//			write("{success:true,msg:'操作成功！  '}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'操作失败:名称重复！  '}");
		}
	}
	private List<Map> toMapList(String str){
		List<Map> list=new ArrayList();
		if(str.length()>2){
			Object object=new Object();
			try {
				object=JSONUtil.deserialize(str);
				if(List.class.isAssignableFrom(object.getClass())){
					list=(List<Map>)object;
				}else{
					Map map=(Map)object;
					list.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			list=null;
		}
		return list;
	}
	private void addCheckStep(String isAdd) throws CodeRepeatException {
		List<RunCOpStepcheck> list = this.toArcArrayList(isAdd);
		for (int i = 0; i < list.size(); i++) {
			RunCOpStepcheck model = list.get(i);
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setModifyBy(employee.getWorkerCode());
			try {
				remote.save(model);
			} catch (CodeRepeatException e) {
				throw new CodeRepeatException("名称重复");
			}
		}
	}

	private void updateCheckStep(String isUpdate) throws CodeRepeatException {
		List<RunCOpStepcheck> list = this.toArcArrayList(isUpdate);
		for (int i = 0; i < list.size(); i++) {
			RunCOpStepcheck model = list.get(i);
			RunCOpStepcheck entity = remote.findById(model.getStepCheckId());
			entity.setCheckStatus(model.getCheckStatus());
			entity.setDisplayNo(model.getDisplayNo());
			entity.setMemo(model.getMemo());
			entity.setModifyBy(employee.getWorkerCode());
			entity.setModifyDate(new Date());
			entity.setOperateTaskId(model.getOperateTaskId());
			entity.setStepCheckName(model.getStepCheckName());
			try {
				remote.update(entity);
			} catch (CodeRepeatException e) {
				throw new CodeRepeatException("名称重复");
			}
		}
	}

	public void deleteCheckStep(String isDelete) {
//		String stepCheckIds = request.getParameter("stepCheckIds");
		String[] ids = isDelete.split(",");
		for (int i = 0; i < ids.length; i++) {
			RunCOpStepcheck entity = remote.findById(Long.parseLong(ids[i]));
			remote.delete(entity);
		}
	}

	public void findCheckStep() throws JSONException {
		String operateTaskId = request.getParameter("operateTaskId");
		List<RunCOpStepcheck> list = remote.findByTaskId(employee
				.getEnterpriseCode(), Long.parseLong(operateTaskId));
		String s = "";
		if (list != null && list.size() > 0) {
			s += "{total:" + list.size() + ",list:" + JSONUtil.serialize(list)
					+ "}";
		} else {
			s += "{total:0,list:[]}";
		}
		write(s);
	}

	private List<RunCOpStepcheck> toArcArrayList(String str) {
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
					RunCOpStepcheck data = this.setStepCheck(map);
					arrList.add(data);
				}
			} else {
				Map map = (Map) object;
				RunCOpStepcheck data = this.setStepCheck(map);
				arrList.add(data);
			}
		}
		return arrList;
	}

	private RunCOpStepcheck setStepCheck(Map map) {
		RunCOpStepcheck model = new RunCOpStepcheck();
		if (map != null) {
			Object stepCheckId = map.get("stepCheckId");
			Object operateTaskId = map.get("operateTaskId");
			Object stepCheckName = map.get("stepCheckName");
			Object displayNo = map.get("displayNo");
			Object memo = map.get("memo");
			Object checkStatus = map.get("checkStatus");
			if (stepCheckId != null && !stepCheckId.toString().equals("")) {
				model.setStepCheckId(Long.parseLong(stepCheckId.toString()));
			}
			if (operateTaskId != null) {
				model
						.setOperateTaskId(Long.parseLong(operateTaskId
								.toString()));
			}
			if (stepCheckName != null) {
				model.setStepCheckName(stepCheckName.toString());
			}
			if (displayNo != null) {
				model.setDisplayNo(Long.parseLong(displayNo.toString()));
			}
			if (memo != null) {
				model.setMemo(memo.toString());
			}
			if (checkStatus != null) {
				model.setCheckStatus(checkStatus.toString());
			}
		}
		return model;
	}

}
