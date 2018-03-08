package power.ejb.equ.workbill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.workbill.form.WorkticketInfo;
import power.ejb.hr.LogUtil;
import power.ejb.workticket.business.RunJWorktickets;

/**
 * Facade for entity EquJWorktickets.
 * 
 * @see power.ejb.equ.workbill.EquJWorktickets
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJWorkticketsFacade implements EquJWorkticketsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	//public void save(EquJWorktickets entity) {
		//try {
			//entityManager.persist(entity);
		//}/ catch (RuntimeException re) {
			//throw re;
		//}
	//}

	public void delete(EquJWorktickets entity) {
		try {
			entity = entityManager.getReference(EquJWorktickets.class, entity
					.getReId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EquJWorktickets update(EquJWorktickets entity) {
		try {
			EquJWorktickets result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EquJWorktickets findById(Long id) {
		try {
			EquJWorktickets instance = entityManager.find(
					EquJWorktickets.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<EquJWorktickets> findBywoCode(String woCode) {
		try {
			String sql = "select t.* from EQU_J_WORKTICKETS t where t.WO_CODE='"
					+ woCode + "'";
			List<EquJWorktickets> list = bll.queryByNativeSQL(sql,
					EquJWorktickets.class);
			return list;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void deleteMutil(String woCode,String woticketCodes){
		try{
			String strWhere="";
			if(woCode!=null && !"".equals(woCode)){
				strWhere+=" where WO_CODE='"+woCode+"'";
			}
			if(woticketCodes!=null)
				//if(strWhere.trim().length()>0)
				strWhere+=" and WOTICKET_CODE = '"+woticketCodes+"'";
			//}
			String sql="delete from EQU_J_WORKTICKETS ";
			sql+=strWhere;
			bll.exeNativeSQL(sql);
		}catch(RuntimeException re){
			throw re;
		}
	}

	public String findAllWorkTicketCodeBywoCode(String woCode){
		try {
			String sql = "select t.* from EQU_J_WORKTICKETS t where t.WO_CODE='"
					+ woCode + "'";
			List<EquJWorktickets> list = bll.queryByNativeSQL(sql,EquJWorktickets.class);
			String sbf = new String();
			sbf ="";
			if(list!=null){
				for(int i=0;i<list.size();i++){
					EquJWorktickets ob = (EquJWorktickets) list.get(i);
					if(i==0){
						sbf +="'"+ob.getWoticketCode()+"'";
					}else{
						if(ob.getWoticketCode()==null){
							sbf +="";
						}
						sbf +=","+"'"+ob.getWoticketCode()+"'";
					}
				}
			}
			return sbf;
		} catch (RuntimeException re) {
			throw re;
		}
		
	}
	public List<WorkticketInfo> getWorkticketListByWorkticketCode(String woCode){
		String wCode = "";
		wCode = this.findAllWorkTicketCodeBywoCode(woCode);
		if(wCode.length()==0||wCode.equals("")||wCode == null){
			return null;
		}else{
		String sql = 
					"select t.workticket_no,\n" +
					"t.workticket_staus_id,\n" + 
					"GETWTSTATUSNAME(t.workticket_staus_id) statusname,\n" + 
					"t.workticket_content,\n" + 
					"t.location_name,\n" + 
					"t.charge_by,getworkername(t.charge_by) chargebyname,\n" + 
					"t.charge_dept,getdeptname(t.charge_dept) deptname,\n" + 
					"t.members,\n" + 
					"t.work_flow_no,\n" + 
					"to_char(t.plan_start_date, 'yyyy-MM-dd hh24:mi:ss') planstartdate,\n" + 
					"to_char(t.plan_end_date, 'yyyy-MM-dd hh24:mi:ss') planenddate,\n" + 
					"t.SOURCE_ID,getwtsourcename(t.SOURCE_ID) sourcename\n" + 
					"from run_j_worktickets t\n" + 
					"where t.workticket_no\n" + 
					"in("+wCode+") and t.is_use = 'Y'";
		List list = bll.queryByNativeSQL(sql);
		List<WorkticketInfo> arraylist = new ArrayList<WorkticketInfo>();
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			Object[] data = (Object[])it.next();
			WorkticketInfo model = new WorkticketInfo();
			RunJWorktickets basemodel = new RunJWorktickets();
			if(data[0] != null)
				basemodel.setWorkticketNo(data[0].toString());
			if(data[1] != null)
				basemodel.setWorkticketStausId(Long.parseLong(data[1].toString()));
			if(data[2] != null)
				model.setStatusName(data[2].toString());
			if(data[3] != null){
				if(data[4] != null){
					model.setWorkticketContentAndLocationName(data[3].toString()+","+data[4].toString());
				}else{
					model.setWorkticketContentAndLocationName(data[3].toString());
				}
			}else{
				if(data[4] != null){
					model.setWorkticketContentAndLocationName(data[4].toString());
				}else{
					model.setWorkticketContentAndLocationName("");
				}
			}
			if(data[5] != null)
				basemodel.setChargeBy(data[5].toString());
			if(data[6] != null)
				model.setChargeByName(data[6].toString());
			if(data[7] != null)
				basemodel.setChargeDept(data[7].toString());
			if(data[8] != null)
				model.setDeptName(data[8].toString());
			if(data[9] != null)
				basemodel.setMembers(data[9].toString());
			if(data[10] !=null)
				basemodel.setWorkFlowNo(Long.parseLong(data[10].toString()));
			if(data[11] != null)
				model.setPlanStartDate(data[11].toString());
			if(data[12] != null)
				model.setPlanEndDate(data[12].toString());
			if(data[13] != null)
				basemodel.setSourceId(Long.parseLong(data[13].toString()));
			if(data[14] != null)
				model.setSourceName(data[14].toString());
			model.setModel(basemodel);
			arraylist.add(model);
		}
        return arraylist;
	  }
	}
	public boolean checkSame(String workticketNo){
		String sqlCount = "select count(*) from EQU_J_WORKTICKETS t where t.WOTICKET_CODE = '"+workticketNo+"'";
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		//System.out.println(count);
		if(count > 0)
			return false;
		else
			return true;
	}
	public boolean save(EquJWorktickets entity){
		try{
			if(checkSame(entity.getWoticketCode())){
				entity.setReId(bll.getMaxId("EQU_J_WORKTICKETS", "RE_ID"));
				String sql = "insert into EQU_J_WORKTICKETS (re_id,wo_code,woticket_code) values ("+entity.getReId()+",'"+entity.getWoCode()+"','"+entity.getWoticketCode()+"')";
				bll.exeNativeSQL(sql);
			}
			return true;
		}catch(RuntimeException re){
			return false;
		}
	}
	public void delWorkticket(String busiNo,String entryId){
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory.getInstance().getFacadeRemote("NativeSqlHelper");  
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		sb.append("update run_j_worktickets t set t.is_use='N' where t.workticket_no='"+busiNo+"';");
		if(entryId != null && !"".equals(entryId.trim()))
		{
			sb.append(getWfSql(Long.parseLong(entryId)));
		}
		sb.append("commit;\n");
		sb.append("end;\n");
		bll.exeNativeSQL(sb.toString());
	}
	public  String getWfSql(Long workflowNo){ 	 
		String wfSql =  "delete from wf_c_currentstep a where a.entry_id="+workflowNo+";\n" +
				"delete from wf_j_rrs_cr b where b.entry_id="+workflowNo+";\n" + 
				"delete from wf_j_rrs_cp c where c.entry_id="+workflowNo+";\n" + 
				"insert into wf_j_historyoperation(id,entry_id,step_id,step_name,action_id,action_name,caller,caller_name,opinion,opinion_time)\n" + 
				"values((select nvl(max(id),0)+1 from wf_j_historyoperation),"+workflowNo+",-1,' ',-1,' ','999999','管理员','管理员删除',sysdate);" ;
		 
		return wfSql;
	}
	//public void deleteRelateWorkbill(String woCode){
		//try{
			//String sql ="delete from EQU_J_WORKTICKETS where wo_code = '"+woCode+"'";
		//}catch(RuntimeException re){
			//throw re;
		//}
			
	//}
}