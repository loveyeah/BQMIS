package power.web.operateticket.register.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ear.comm.CodeRepeatException;
import power.ejb.opticket.bussiness.RunJOpMeasures;
import power.ejb.opticket.bussiness.RunJOpMeasuresFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class OpDangerMeasureAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RunJOpMeasuresFacadeRemote remote;

	public OpDangerMeasureAction() {
		remote = (RunJOpMeasuresFacadeRemote) factory
				.getFacadeRemote("RunJOpMeasuresFacade");
	}

	public void updateDangerMeasureList() {
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
//			this.addDangerMeasure(isAdd);
//		if (isUpdate.length() > 2)
//			this.updateDangerMeasure(isUpdate);
//		if (isDelete.length() > 1)
//			this.deleteDangerMeasure(isDelete);
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
	private void addDangerMeasure(String isAdd) {
		List<RunJOpMeasures> list = this.toarrayList(isAdd);
		for (int i = 0; i < list.size(); i++) {
			RunJOpMeasures model = list.get(i);
			try {
				remote.save(model);
			} catch (CodeRepeatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void updateDangerMeasure(String isUpdate) {
		List<RunJOpMeasures> list = this.toarrayList(isUpdate);
		for (int i = 0; i < list.size(); i++) {
			RunJOpMeasures model = list.get(i);
			RunJOpMeasures entity = remote.findById(model.getDangerId());
			entity.setDangerName(model.getDangerName());
			entity.setDisplayNo(model.getDisplayNo());
			entity.setMeasureContent(model.getMeasureContent());
			entity.setMemo(model.getMemo());
			entity.setRunAddFlag(model.getRunAddFlag());
			entity.setMeasureContent(model.getMeasureContent());
			try {
				remote.update(entity);
			} catch (CodeRepeatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void deleteDangerMeasure(String isDelete) {
		String[] ids = isDelete.split(",");
		for (int i = 0; i < ids.length; i++) {
			RunJOpMeasures model = remote.findById(Long.parseLong(ids[i]));
			remote.delete(model);
		}
	}

	private List<RunJOpMeasures> toarrayList(String str) {
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
					RunJOpMeasures data = this.setMeasures(map);
					arrList.add(data);
				}
			} else {
				Map map = (Map) object;
				RunJOpMeasures data = this.setMeasures(map);
				arrList.add(data);
			}
		}
		return arrList;
	}

	private RunJOpMeasures setMeasures(Map map) {
		RunJOpMeasures model = new RunJOpMeasures();
		if (map != null) {
			Object opticketCode = map.get("opticketCode");
			Object runAddFlag = map.get("runAddFlag");
			Object dangerId = map.get("dangerId");
			Object dangerName = map.get("dangerName");
			Object measureContent = map.get("measureContent");
			Object displayNo = map.get("displayNo");
			Object memo = map.get("memo");
			if (dangerId != null && !dangerId.toString().equals("")) {
				model.setDangerId(Long.parseLong(dangerId.toString()));
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
