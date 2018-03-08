package power.web.operateticket.register.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


import power.ear.comm.CodeRepeatException;
import power.ejb.opticket.bussiness.RunJOpStepcheck;
import power.ejb.opticket.bussiness.RunJOpStepcheckFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class BefCheckStepAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RunJOpStepcheckFacadeRemote remote;

	public BefCheckStepAction() {
		remote = (RunJOpStepcheckFacadeRemote) factory
				.getFacadeRemote("RunJOpStepcheckFacade");
	}
	public void updateBefCheckStepList(){
		String isAdd=request.getParameter("isAdd");
		String isUpdate=request.getParameter("isUpdate");
		String isDelete=request.getParameter("isDelete");
		List<Map> addList=this.toMapList(isAdd);
		List<Map> updateList=this.toMapList(isUpdate);
		boolean isSuccess=remote.saveAllOperat(addList, updateList, isDelete,employee.getWorkerCode(),employee.getEnterpriseCode());
		if(isSuccess){
			write("{success:true,msg:'操作成功！  '}");
		}
		else{
			write("{success:true,msg:'操作失败！  '}");
		}
//		if(isAdd.length()>2)
//		this.addCheckStep(isAdd);
//		if(isUpdate.length()>2)
//		this.updateCheckStep(isUpdate);
//		if(isDelete.length()>1)
//			this.deleteCheckStep(isDelete);
	}
	private void addCheckStep(String isAdd) {
		List<RunJOpStepcheck> list = this.toArcArrayList(isAdd);
		for (int i = 0; i < list.size(); i++) {
			RunJOpStepcheck model = list.get(i);
			model.setCheckMan(employee.getWorkerCode());
			try {
				remote.save(model);
			} catch (CodeRepeatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	private void updateCheckStep(String isUpdate) {
		List<RunJOpStepcheck> list = this.toArcArrayList(isUpdate);
		for (int i = 0; i < list.size(); i++) {
			RunJOpStepcheck model = list.get(i);
			RunJOpStepcheck entity = remote.findById(model.getStepCheckId());
			entity.setCheckStatus(model.getCheckStatus());
			entity.setDisplayNo(model.getDisplayNo());
			entity.setMemo(model.getMemo());
			entity.setStepCheckName(model.getStepCheckName());
			entity.setCheckMan(employee.getWorkerCode());
			entity.setRunAddFlag(model.getRunAddFlag());
			entity.setOpticketCode(model.getOpticketCode());
			try {
				remote.update(entity);
			} catch (CodeRepeatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void deleteCheckStep(String isDelete) {
		String[] ids = isDelete.split(",");
		for (int i = 0; i < ids.length; i++) {
			RunJOpStepcheck entity = remote.findById(Long.parseLong(ids[i]));
			remote.delete(entity);
		}
	}

	
	private List<RunJOpStepcheck> toArcArrayList(String str){
		Object object=new Object();
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
					RunJOpStepcheck data = this.setStepCheck(map);
					arrList.add(data);
				}
			} else {
				Map map = (Map) object;
				RunJOpStepcheck data = this.setStepCheck(map);
				arrList.add(data);
			}
		}
		return arrList;
	}

	private RunJOpStepcheck setStepCheck(Map map) {
		RunJOpStepcheck model = new RunJOpStepcheck();
		if (map != null) {
			Object opticketCode = map.get("opticketCode");
			Object runAddFlag = map.get("runAddFlag");
			Object stepCheckId = map.get("stepCheckId");
			Object stepCheckName = map.get("stepCheckName");
			Object displayNo = map.get("displayNo");
			Object memo = map.get("memo");
			Object checkStatus = map.get("checkStatus");
			if (stepCheckId!=null && !stepCheckId.toString().equals("") ) {
				model.setStepCheckId(Long.parseLong(stepCheckId.toString()));
			}
			if (stepCheckName!=null) {
				model.setStepCheckName(stepCheckName.toString());
			}
			if (displayNo!=null) {
				model.setDisplayNo(Long.parseLong(displayNo.toString()));
			}
			if (memo!=null) {
				model.setMemo(memo.toString());
			}
			if (opticketCode!=null) {
				model.setOpticketCode(opticketCode.toString());
			}
			if (runAddFlag!=null) {
				model.setRunAddFlag(runAddFlag.toString());
			}
			if (checkStatus!=null) {
				model.setCheckStatus(checkStatus.toString());
			}
		}
		return model;
	}
}
