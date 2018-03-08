package power.web.operateticket.register.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ejb.opticket.RunJOpticketstep;
import power.ejb.opticket.RunJOpticketstepFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class OpContentAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RunJOpticketstepFacadeRemote remote;

	public OpContentAction() {
		remote = (RunJOpticketstepFacadeRemote) factory
				.getFacadeRemote("RunJOpticketstepFacade");
	}

	public void updateOPContentList() {
		String isAdd = request.getParameter("isAdd");
		String isUpdate = request.getParameter("isUpdate");
		String isDelete = request.getParameter("isDelete");
		List<Map> addList=this.toMapList(isAdd);
		List<Map> updateList=this.toMapList(isUpdate);
		boolean isSuccess=remote.saveAllOperat(addList, updateList, isDelete,null,employee.getEnterpriseCode());
		if(isSuccess){
			write("{success:true,msg:'操作成功！  '}");
		}
		else{
			write("{success:true,msg:'操作失败！  '}");
		}
//		if (isAdd.length() > 2)
//			this.addOpticketStep(isAdd);
//		if (isUpdate.length() > 2)
//			this.updateOpticketStep(isUpdate);
//		if (isDelete.length() > 1)
//			this.deleteOpticketStep(isDelete);
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

	private void addOpticketStep(String isAdd) {
		List<RunJOpticketstep> list = this.toarrayList(isAdd);
		for (int i = 0; i < list.size(); i++) {
			RunJOpticketstep model = list.get(i);
			remote.save(model);
		}
	}

	private void updateOpticketStep(String isUpdate) {
		List<RunJOpticketstep> list = this.toarrayList(isUpdate);
		for (int i = 0; i < list.size(); i++) {
			RunJOpticketstep model = list.get(i);
			RunJOpticketstep entity = remote.findById(model.getOperateStepId());
			entity.setDisplayNo(model.getDisplayNo());
			entity.setMemo(model.getMemo());
			entity.setExecStatus(model.getExecStatus());
			entity.setFinishTime(model.getFinishTime());
			entity.setRunAddFlag(model.getRunAddFlag());
			entity.setOperateStepName(model.getOperateStepName());
			remote.update(entity);
		}
	}

	public void deleteOpticketStep(String isDelete) {
		String[] ids = isDelete.split(",");
		for (int i = 0; i < ids.length; i++) {
			RunJOpticketstep entity = remote.findById(Long.parseLong(ids[i]));
			remote.delete(entity);
		}
	}

	private List<RunJOpticketstep> toarrayList(String str) {
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
					RunJOpticketstep data = this.setOpContent(map);
					arrList.add(data);
				}
			} else {
				Map map = (Map) object;
				RunJOpticketstep data = this.setOpContent(map);
				arrList.add(data);
			}
		}
		return arrList;
	}

	private RunJOpticketstep setOpContent(Map map) {
		RunJOpticketstep model = new RunJOpticketstep();
		if (map != null) {
			Object operateStepId = map.get("operateStepId");
			Object opticketCode = map.get("opticketCode");
			Object finishTime = map.get("finishTime");
			Object runAddFlag = map.get("runAddFlag");
			Object execStatus = map.get("execStatus");
			Object operateStepName = map.get("operateStepName");
			Object displayNo = map.get("displayNo");
			Object memo = map.get("memo");
			if (operateStepId != null && !operateStepId.toString().equals("")) {
				model
						.setOperateStepId(Long.parseLong(operateStepId
								.toString()));
			}
			if (operateStepName != null) {
				model.setOperateStepName(operateStepName.toString());
			}
			if (opticketCode != null) {
				model.setOpticketCode(opticketCode.toString());
			}
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			if (finishTime != null && !finishTime.toString().equals("")) {
				try {
					model.setFinishTime(format.parse(finishTime.toString()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (runAddFlag != null) {
				model.setRunAddFlag(runAddFlag.toString());
			}
			if (execStatus != null) {
				if (execStatus.toString().equals("true")
						|| execStatus.toString().equals("1")) {
					model.setExecStatus("1");
				} else if (execStatus.toString().equals("false")
						|| execStatus.toString().equals("0")) {
					model.setExecStatus("0");
				}
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

	public void selectAllItems() {
		String ischeck = request.getParameter("ischeck");
		String opticketCode = request.getParameter("opticketCode");
		List<RunJOpticketstep> list = remote.findByOperateCode(opticketCode);
		List arraylist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			RunJOpticketstep entity = list.get(i);
			if (ischeck.equals("true")) {
				entity.setExecStatus("1");
			} else {
				entity.setExecStatus("0");
			}
			arraylist.add(entity);
		}
		try {
			write("{\"list\":" + JSONUtil.serialize(arraylist) + "}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			write("{list,data:[]}");
			e.printStackTrace();
		}
	}

}
