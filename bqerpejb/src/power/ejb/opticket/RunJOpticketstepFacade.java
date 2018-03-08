package power.ejb.opticket;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.opticket.bussiness.RunJOpFinwork;
import power.ejb.opticket.form.OpstepInfo;

/**
 * Facade for entity RunJOpticketstep.
 * 
 * @see power.ejb.opticket.RunJOpticketstep
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJOpticketstepFacade implements RunJOpticketstepFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunJOpticketstep save(RunJOpticketstep entity) {
		LogUtil.log("saving RunJOpticketstep instance", Level.INFO, null);
		try {
			if (entity.getOperateStepId() == null) {
				entity.setOperateStepId(bll.getMaxId("RUN_J_OPTICKETSTEP",
						"operate_step_id"));
			}
			entity.setRunAddFlag("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(RunJOpticketstep entity) {
		try {
			entity = entityManager.getReference(RunJOpticketstep.class, entity
					.getOperateStepId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}

	}

	public RunJOpticketstep update(RunJOpticketstep entity) {
		try {
			RunJOpticketstep result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunJOpticketstep findById(Long id) {
		LogUtil.log("finding RunJOpticketstep instance with id: " + id,
				Level.INFO, null);
		try {
			RunJOpticketstep instance = entityManager.find(
					RunJOpticketstep.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<RunJOpticketstep> findByOperateCode(String opticketCode) {
		String sql = "select t.* from Run_j_Opticketstep t where t.opticket_code=?  order by t.display_no,t.operate_step_id";
		return bll.queryByNativeSQL(sql, new Object[] { opticketCode },
				RunJOpticketstep.class);

	}
	
	public List<OpstepInfo> findByOpCode(String opticketCode){
		String sql=
			"select t.operate_step_id,\n" +
			"       t.opticket_code,\n" + 
			"       t.operate_step_name,\n" + 
			"       t.run_add_flag,\n" + 
			"       to_char(t.finish_time,'yyyy-MM-dd HH24:MI:SS'),\n" + 
			"       t.exec_man,\n" + 
			"       t.exec_status,\n" + 
			"       t.display_no,\n" + 
			"       t.is_main,\n" + 
			"       t.memo,\n" + 
			"       t.pro_man,\n" + 
			"       getworkername(t.exec_man),\n" + 
			"       getworkername(t.pro_man)\n" + 
			"  from Run_j_Opticketstep t\n" + 
			" where t.opticket_code = ?\n" + 
			" order by t.display_no, t.operate_step_id";
		List list=	bll.queryByNativeSQL(sql, new Object[] { opticketCode });
		List<OpstepInfo> arr=new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			Object[] ob=(Object[])it.next();
			OpstepInfo model=new OpstepInfo();
			if(ob[0]!=null){
				model.setOperateStepId(Long.parseLong(ob[0].toString()));
			}
			if(ob[1]!=null){
				model.setOpticketCode(ob[1].toString());
			}
			if(ob[2]!=null){
				model.setOperateStepName(ob[2].toString());
			}
			if(ob[3]!=null)
				model.setRunAddFlag(ob[3].toString());
			if(ob[4]!=null)
				model.setFinishTime(ob[4].toString().substring(11,16));
			if(ob[5]!=null)
				model.setExecMan(ob[5].toString());
			if(ob[6]!=null)
				model.setExecStatus(ob[6].toString());
			if(ob[7]!=null)
				model.setDisplayNo(Long.parseLong(ob[7].toString()));
			if(ob[8]!=null)
				model.setIsMain(ob[8].toString());
			if(ob[9]!=null)
				model.setMemo(ob[9].toString());
			if(ob[10]!=null)
				model.setProMan(ob[10].toString());
			if(ob[11]!=null)
				model.setExecName(ob[11].toString());
			if(ob[12]!=null)
				model.setProName(ob[12].toString());
			arr.add(model);
		}
		return arr;
	}

	public boolean saveAllOperat(List<Map> addList, List<Map> updateList,
			String delStr,String currentMan,String enterpriseCod) {
		boolean operatStatus = false;
		final int maxSize=30;
		try{
			if (addList != null) {
				Long count = bll.getMaxId("RUN_J_OPTICKETSTEP", "operate_step_id");
				int totalAdd = addList.size();
				for (int i=0;i<totalAdd;i++) {
					count++;
					Map map=addList.get(i);
					RunJOpticketstep op=this.updateModel(map);
					if(op.getOperateStepId()==null){
						if(op.getExecMan()==null)
							op.setExecMan(currentMan);
						op.setOperateStepId(count);
						this.save(op);
						if(totalAdd>maxSize){
							if((i+1)%maxSize==0){
								entityManager.flush();
							}						
						}					
					}
				}
			}
			if(updateList!=null){
				int totalUpdate=updateList.size();
				for(int i=0;i<totalUpdate;i++){
					Map mapUp=updateList.get(i);
					RunJOpticketstep opUp=this.updateModel(mapUp);
					if(opUp.getOperateStepId()!=null){
						if(opUp.getExecMan()==null)
							opUp.setExecMan(currentMan);
						this.update(opUp);
						if(totalUpdate>maxSize){
							if((i+1)%maxSize==0){
								entityManager.flush();
							}						
						}	
					}
				}
			}
			if(delStr.length()>1){
				String[] ids=delStr.split(",");
				for(int i=0;i<ids.length;i++){
					RunJOpticketstep opDel=this.findById(Long.parseLong(ids[i]));
					this.delete(opDel);
					if(ids.length>maxSize){
						if((i+1)%maxSize==0){
							entityManager.flush();
						}						
					}	
				}
			}
			operatStatus=true;
		}catch(Exception e){			
			e.printStackTrace();
			operatStatus=false;
			return operatStatus;
		}
		
		return operatStatus;
	}

	private RunJOpticketstep updateModel(Map map) {
		RunJOpticketstep model = new RunJOpticketstep();
		Object operateStepId = map.get("operateStepId");
		Object opticketCode = map.get("opticketCode");
		Object finishTime = map.get("finishTime");
		Object runAddFlag = map.get("runAddFlag");
		Object execStatus = map.get("execStatus");
		Object operateStepName = map.get("operateStepName");
		Object displayNo = map.get("displayNo");
		Object memo = map.get("memo");
		Object execMan=map.get("execMan");
		Object proMan=map.get("proMan");
		// 增加
		if (operateStepId == null || "".equals(operateStepId.toString())) {
			;
		}
		// 修改
		else {
			model = this.findById(Long
					.parseLong(operateStepId.toString()));
		}
		if (operateStepName != null) {
			model.setOperateStepName(operateStepName.toString());
		}
		if (opticketCode != null) {
			model.setOpticketCode(opticketCode.toString());
		}
		DateFormat format = new SimpleDateFormat("HH:mm");
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
		if(execMan!=null){
			model.setExecMan(execMan.toString());
		}
		if(proMan!=null){
			model.setProMan(proMan.toString());
		}
		return model;
	}
}