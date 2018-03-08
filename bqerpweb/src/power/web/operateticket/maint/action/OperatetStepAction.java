package power.web.operateticket.maint.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ejb.opticket.RunCOpticketstep;
import power.ejb.opticket.RunCOpticketstepFacadeRemote;
import power.web.comm.AbstractAction;

public class OperatetStepAction  extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RunCOpticketstepFacadeRemote remote;
	public OperatetStepAction(){
		remote=(RunCOpticketstepFacadeRemote)factory.getFacadeRemote("RunCOpticketstepFacade");
	}
	public void updateOperateStepList(){
		String isAdd=request.getParameter("isAdd");
		String isUpdate=request.getParameter("isUpdate");
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
//		try {
//			this.addOpticketStep(isAdd);
//			this.updateOpticketStep(isUpdate);
//			this.deleteOpticketStep(isDelete);
//			write("{success:true,msg:'操作成功！  '}");
//		} catch (CodeRepeatException e) {
//			write("{success:true,msg:'操作失败:名称重复！  '}");
//		}		
//	}
	public void addOpticketStep(String isAdd) throws CodeRepeatException{
		List<RunCOpticketstep> list=this.toarrayList(isAdd);
		for(int i=0;i<list.size();i++){
			RunCOpticketstep model=list.get(i);
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setModifyBy(employee.getWorkerCode());
			model.setModifyDate(new Date());
			try {
				remote.save(model);
			} catch (CodeRepeatException e) {
				throw new CodeRepeatException("名称重复");
			}
			
		}
	}
	public void updateOpticketStep(String isUpdate) throws CodeRepeatException{
		List<RunCOpticketstep> list=this.toarrayList(isUpdate);
		for(int i=0;i<list.size();i++){
			RunCOpticketstep model=list.get(i);
			RunCOpticketstep entity=remote.findById(model.getOperateStepId());
			entity.setDisplayNo(model.getDisplayNo());
			entity.setMemo(model.getMemo());
			entity.setModifyBy(employee.getWorkerCode());
			entity.setModifyDate(new Date());
			entity.setOperateStepName(model.getOperateStepName());
			try {
				remote.update(entity);
			} catch (CodeRepeatException e) {
				throw new CodeRepeatException("名称重复");
			}
				
		}
	}
	public void deleteOpticketStep(String isDelete){
//		String operateStepIds=request.getParameter("OperateStepId");
		String[] ids=isDelete.split(",");
		for(int i=0;i<ids.length;i++){
			RunCOpticketstep entity=remote.findById(Long.parseLong(ids[i]));
			remote.delete(entity);
		}
	}
	public void findOpticketStep() throws JSONException{
		String operateTaskId=request.getParameter("operateTaskId");
		List<RunCOpticketstep> list=remote.findFinworkByTask(employee.getEnterpriseCode(), Long.parseLong(operateTaskId));
		String s="";
		if(list!=null && list.size()>0){
			s+="{total:"+list.size()+",list:"+JSONUtil.serialize(list)+"}";
		}else{
			s+="{total:0,list:[]}";
		}
		write(s);	
	}
	private List<RunCOpticketstep> toarrayList(String str){
		Object object=new Object();
		try {
			object = JSONUtil.deserialize(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List arrList=new ArrayList();
		if(object!=null){
			if(List.class.isAssignableFrom(object.getClass())){
				List list=(List)object;
				int intLen = list.size();
				for(int i=0;i<intLen;i++){
					Map map=(Map)list.get(i);
					RunCOpticketstep data=this.setOpStep(map);
					arrList.add(data);
				}
			}else{
				Map map=(Map)object;
				RunCOpticketstep data=this.setOpStep(map);
				arrList.add(data);
			}
		}
		return arrList;
	}
	private RunCOpticketstep setOpStep(Map map){
		RunCOpticketstep model=new RunCOpticketstep();
		if(map!=null){
			Object operateStepId=map.get("operateStepId");
			Object operateTaskId=map.get("operateTaskId");
			Object operateStepName=map.get("operateStepName");
			Object displayNo=map.get("displayNo");
			Object memo=map.get("memo");
			if(operateStepId!=null && !operateStepId.toString().equals("")){
				model.setOperateStepId(Long.parseLong(operateStepId.toString()));
			}
			if(operateTaskId!=null){
				model.setOperateTaskId(Long.parseLong(operateTaskId.toString()));
			}
			if(operateStepName!=null){
				model.setOperateStepName(operateStepName.toString());
			}
			if(displayNo!=null){
				model.setDisplayNo(Long.parseLong(displayNo.toString()));
			}
			if(memo!=null){
				model.setMemo(memo.toString());
			}
		}
		return model;
	}
}
