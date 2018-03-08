package power.web.operateticket.finishWork.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ejb.opticket.RunCOpFinwork;
import power.ejb.opticket.RunCOpFinworkFacadeRemote;
import power.web.comm.AbstractAction;

public class OpticketFinishWorkAction  extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6636405552217619237L;
	private RunCOpFinworkFacadeRemote remote;
	public OpticketFinishWorkAction(){
		remote=(RunCOpFinworkFacadeRemote)factory.getFacadeRemote("RunCOpFinworkFacade");
	}
	public void updateOPfinWorkList(){
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
	private void addFinishWork(String isAdd) throws CodeRepeatException{
		List<RunCOpFinwork> list=this.toArcArrayList(isAdd);
		if(list!=null){
			for(int i=0;i<list.size();i++){
				RunCOpFinwork model=list.get(i);
				model.setEnterpriseCode(employee.getEnterpriseCode());
				model.setIsUse("Y");
				model.setModifyBy(employee.getWorkerCode());			
				try {
					remote.save(model);
				} catch (CodeRepeatException e) {
					throw new CodeRepeatException("名称重复");
				}
			}
		}
	}
	private void updateFinishWork(String isUpdate) throws CodeRepeatException{
		List<RunCOpFinwork> list=this.toArcArrayList(isUpdate);
		if(list!=null){
			for(int i=0;i<list.size();i++){
				RunCOpFinwork model=list.get(i);
				RunCOpFinwork entity=remote.findById(model.getFinishWorkId());
				entity.setFinishWorkName(model.getFinishWorkName());
				entity.setDisplayNo(model.getDisplayNo());
				entity.setMemo(model.getMemo());
				entity.setCheckStatus(model.getCheckStatus());
				try {
					remote.update(entity);
				} catch (CodeRepeatException e) {
					throw new CodeRepeatException("名称重复");
				}
				
			}
		}	
	}
	public void deleteFinishWork(String isDelete) throws CodeRepeatException{
//		String finishWorkIds=request.getParameter("finishWorkIds");
		String[] ids=isDelete.split(",");
		for(int i=0;i<ids.length;i++){
			RunCOpFinwork model=remote.findById(Long.parseLong(ids[i]));
			remote.delete(model);
		}
	}
	public void findFinishWork() throws JSONException{
		String operateTaskId=request.getParameter("operateTaskId");
		List<RunCOpFinwork> list=remote.findFinworkByTask(employee.getEnterpriseCode(), Long.parseLong(operateTaskId));
		String s="";
		if(list!=null && list.size()>0){
			s+="{total:"+list.size()+",list:"+JSONUtil.serialize(list)+"}";
		}else{
			s+="{total:0,list:[]}";
		}
		write(s);	
	}
	private List<RunCOpFinwork> toArcArrayList(String str){
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
					RunCOpFinwork data=this.setRunCOpFinwork(map);
					arrList.add(data);
				}
			}else{
				Map map=(Map)object;
				RunCOpFinwork data=this.setRunCOpFinwork(map);
				arrList.add(data);
			}
		}
		return arrList;
	}
	private RunCOpFinwork setRunCOpFinwork(Map map){
		RunCOpFinwork model=new RunCOpFinwork();
		if(map!=null){
			Object finishWorkId=map.get("finishWorkId");
			Object finishWorkName=map.get("finishWorkName");
			Object operateTaskId=map.get("operateTaskId");
			Object displayNo=map.get("displayNo");
			Object checkStatus=map.get("checkStatus");
			Object memo=map.get("memo");
			if(finishWorkId!=null && !finishWorkId.toString().equals("")){
				model.setFinishWorkId(Long.parseLong(finishWorkId.toString()));
			}
			if(operateTaskId!=null){
				model.setOperateTaskId(Long.parseLong(operateTaskId.toString()));
			}
			if(finishWorkName!=null){
				model.setFinishWorkName(finishWorkName.toString());
			}
			if(displayNo!=null){
				model.setDisplayNo(Long.parseLong(displayNo.toString()));
			}
			if(checkStatus!=null){
				model.setCheckStatus(checkStatus.toString());
			}
			if(memo!=null){
				model.setMemo(memo.toString());
			}
		}
		return model;
	}
}
