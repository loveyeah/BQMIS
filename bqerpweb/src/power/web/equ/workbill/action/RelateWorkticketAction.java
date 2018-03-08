package power.web.equ.workbill.action;

import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.equ.workbill.EquJWorktickets;
import power.ejb.equ.workbill.EquJWorkticketsFacadeRemote;
import power.ejb.equ.workbill.form.WorkticketInfo;
import power.ejb.workticket.business.RunJWorktickets;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.business.WorkticketManager;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;



public class RelateWorkticketAction extends AbstractAction{
	private EquJWorkticketsFacadeRemote remote;
	private RunJWorkticketsFacadeRemote runremote;
	private RunJWorktickets workticketBaseInfo;
	 private WorkticketManager workticketManager;
	public RelateWorkticketAction(){
		remote = (EquJWorkticketsFacadeRemote) factory
		.getFacadeRemote("EquJWorkticketsFacade");
		runremote = (RunJWorkticketsFacadeRemote)factory
		.getFacadeRemote("RunJWorkticketsFacade");
    }
	//根据工单编号查找对应所有工作票信息
	public void relateWorkticketAllList() throws JSONException{
		String woCode = request.getParameter("woCode");
		System.out.println("the wocode"+woCode);
		//String woCode = "11";
		List<WorkticketInfo> list = remote.getWorkticketListByWorkticketCode(woCode);
		if (list != null) {
			String Str = JSONUtil.serialize(list);
			//System.out.println(Str);
			write("{ root:" + Str + "}");
		} else {
			write("{root:[]}");
		}
	}
	//删除工单对应工作票关联关系
	public void deleteWorkticketRelation() throws JSONException{
		String woCode = request.getParameter("woCode");
		String workticketCode = request.getParameter("workticketCode");
		String[] ids = workticketCode.split(",");
		for(int i=0;i<ids.length;i++){
		        remote.deleteMutil(woCode, ids[i]);
		}
	}
	//删除工作票
	public void deleteWorkticket() throws JSONException{
		String woCode = request.getParameter("woCode");
		String workticketCode = request.getParameter("workticketCode");
		String entryId = request.getParameter("entryId");
		remote.deleteMutil(woCode, workticketCode);
		if(entryId != null){
			remote.delWorkticket(workticketCode, entryId);
		}
	}
	//给一个工单增加若干条工作票
	public void addWorktickets(){
		try{
			EquJWorktickets baseEntity = new EquJWorktickets();
			String str = request.getParameter("workticketsNo");
			String[] ids = str.split(",");
			String woCode = request.getParameter("woCode");
			for(int i=0;i<ids.length;i++){
				baseEntity.setWoCode(woCode);
				baseEntity.setWoticketCode(ids[i]);
				remote.save(baseEntity);
			}
			if (remote.save(baseEntity))
				write("{success:true,msg:'添加成功'}");
			else
				write("{success:false,msg:'添加失败'}");
		}catch(RuntimeException re){
			write("{success:false,msg:'添加失败'}");
		}
	}
	//生成一条非完整工作票记录
	public void createWorkticket(){
		workticketManager = (WorkticketManager) factory
                            .getFacadeRemote("WorkticketManagerImpl");
		RunJWorktickets workticketBaseInfo = new RunJWorktickets();
		String workticketTypeCode = request.getParameter("workticketTypeCode");
		String repairSpecailCode = request.getParameter("repairSpecailCode");
		String equAttributeCode = request.getParameter("equAttributeCode");
		String sourceId  = request.getParameter("sourceId");
		String chargeBy = request.getParameter("chargeBy");
		String chargeDept = request.getParameter("chargeDept");
		String permissionDept = request.getParameter("permissionDept");
		String locationName = request.getParameter("locationName");
		workticketBaseInfo.setWorkticketTypeCode(workticketTypeCode);
		workticketBaseInfo.setRepairSpecailCode(repairSpecailCode);
		workticketBaseInfo.setEnterpriseCode(Constants.ENTERPRISE_CODE);
		workticketBaseInfo.setEntryBy(employee.getWorkerCode());
		workticketBaseInfo.setEquAttributeCode(equAttributeCode);
		workticketBaseInfo.setSourceId(Long.parseLong(sourceId));
		workticketBaseInfo.setChargeBy(chargeBy);
		workticketBaseInfo.setChargeDept(chargeDept);
		workticketBaseInfo.setPermissionDept(permissionDept);
		workticketBaseInfo.setLocationName(locationName);
		workticketBaseInfo.setPlanStartDate(new Date());
		workticketBaseInfo.setPlanEndDate(new Date());
		workticketBaseInfo.setMemberCount(1l);
		String strEnterpriseChar = "";
		strEnterpriseChar = employee.getEnterpriseChar();
		RunJWorktickets entity = null;
		entity = workticketManager.createBaseWorkticket(workticketBaseInfo, strEnterpriseChar);
		if(entity != null){
			String strWorkticketNo = entity.getWorkticketNo(); 
			write("{success:true,msg:'增加成功！',workticketNo:'" + strWorkticketNo
                    + "'}");
		}else{
			write("{success:false}");
		}
	}
	//根据工作票编码查找其所有信息
	public void findWorkticketByWorkticketNo() throws JSONException{
		String workticketNo = request.getParameter("workticketNo");
		System.out.println("the workno"+workticketNo);
		List<power.ejb.workticket.form.WorkticketInfo> list = 
			     runremote.getWorkticketByWorkticketNo(workticketNo,employee.getEnterpriseCode());
		if(list != null&&!list.equals("")&&list.size()>0){ 
			write(JSONUtil.serialize(list.get(0)));
		}else{
			write("{list : []}");//modify by wpzhu
		}
	}
}
