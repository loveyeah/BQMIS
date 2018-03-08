package power.web.operateticket.register.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.CodeRepeatException;
import power.ejb.opticket.bussiness.RunJOpFinwork;
import power.ejb.opticket.bussiness.RunJOpFinworkFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class AftFinWorkAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6636405552217619237L;
	private RunJOpFinworkFacadeRemote remote;

	public AftFinWorkAction() {
		remote = (RunJOpFinworkFacadeRemote) factory
				.getFacadeRemote("RunJOpFinworkFacade");
	}

	public void updateFinishWorkList() {
		String isAdd = request.getParameter("isAdd");
		String isUpdate = request.getParameter("isUpdate");
		String isDelete = request.getParameter("isDelete");
		List<Map> addList=this.toMapList(isAdd);
		List<Map> updateList=this.toMapList(isUpdate);
		boolean isSuccess=remote.saveAllOperat(addList, updateList, isDelete,employee.getWorkerCode(),employee.getEnterpriseCode());
		if(isSuccess){
			write("{success:true,msg:'操作成功！  '}");
		}
		else{
			write("{success:true,msg:'操作失败！  '}");
		}
//		if (isAdd.length() > 2)
//			this.addFinishWork(isAdd);
//		if (isUpdate.length() > 2)
//			this.updateFinishWork(isUpdate);
//		if (isDelete.length() > 1)
//			this.deleteFinishWork(isDelete);
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

	private void addFinishWork(String isAdd) {
		List<RunJOpFinwork> list = this.toArcArrayList(isAdd);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				RunJOpFinwork model = list.get(i);
				model.setCheckMan(employee.getWorkerCode());
				try {
					remote.save(model);
				} catch (CodeRepeatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void updateFinishWork(String isUpdate) {
		List<RunJOpFinwork> list = this.toArcArrayList(isUpdate);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				RunJOpFinwork model = list.get(i);
				RunJOpFinwork entity = remote.findById(model.getFinishWorkId());
				entity.setFinishWorkName(model.getFinishWorkName());
				entity.setDisplayNo(model.getDisplayNo());
				entity.setMemo(model.getMemo());
				entity.setCheckStatus(model.getCheckStatus());
				entity.setCheckMan(employee.getWorkerCode());
				entity.setOpticketCode(model.getOpticketCode());
				entity.setRunAddFlag(model.getRunAddFlag());
				try {
					remote.update(entity);
				} catch (CodeRepeatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void deleteFinishWork(String isDelete) {
		String[] ids = isDelete.split(",");
		for (int i = 0; i < ids.length; i++) {
			RunJOpFinwork model = remote.findById(Long.parseLong(ids[i]));
			remote.delete(model);
		}
	}

	private List<RunJOpFinwork> toArcArrayList(String str) {
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
					RunJOpFinwork data = this.setRunCOpFinwork(map);
					arrList.add(data);
				}
			} else {
				Map map = (Map) object;
				RunJOpFinwork data = this.setRunCOpFinwork(map);
				arrList.add(data);
			}
		}
		return arrList;
	}

	private RunJOpFinwork setRunCOpFinwork(Map map) {
		RunJOpFinwork model = new RunJOpFinwork();
		if (map != null) {
			Object opticketCode = map.get("opticketCode");
			Object runAddFlag = map.get("runAddFlag");
			Object finishWorkId = map.get("finishWorkId");
			Object finishWorkName = map.get("finishWorkName");
			Object displayNo = map.get("displayNo");
			Object checkStatus = map.get("checkStatus");
			Object memo = map.get("memo");
			if (finishWorkId != null && !finishWorkId.toString().equals("")) {
				model.setFinishWorkId(Long.parseLong(finishWorkId.toString()));
			}
			if (finishWorkName != null) {
				model.setFinishWorkName(finishWorkName.toString());
			}
			if (displayNo != null && !displayNo.toString().equals("")) {
				model.setDisplayNo(Long.parseLong(displayNo.toString()));
			}
			if (checkStatus != null) {
				model.setCheckStatus(checkStatus.toString());
			}
			if (memo != null) {
				model.setMemo(memo.toString());
			}
			if (opticketCode != null) {
				model.setOpticketCode(opticketCode.toString());
			}
			if (runAddFlag != null) {
				model.setRunAddFlag(runAddFlag.toString());
			}
		}
		return model;
	}
}
