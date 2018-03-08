package power.ejb.opticket;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * @author slTang
 */
@Stateless
public class RunCOpStepcheckFacade implements RunCOpStepcheckFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(RunCOpStepcheck entity) throws CodeRepeatException{
		try {
			if (!this.checkNameForAdd(entity.getEnterpriseCode(), entity
					.getOperateTaskId(), entity.getStepCheckName())) {
				if (entity.getStepCheckId() == null) {
					entity.setStepCheckId(bll.getMaxId("RUN_C_OP_STEPCHECK","step_check_id"));
				}
				entity.setIsUse("Y");
				entity.setModifyDate(new Date());
				entityManager.persist(entity);
			} else {
				throw new CodeRepeatException("名称重复!");
			}
		} catch (RuntimeException re) {
			throw re;
		}
	}

	private boolean checkNameForAdd(String enterpriseCode, Long operateTaskId,
			String stepCheckName, Long... stepCheckId) {
		boolean isSame = true;
		String sql = "select count(1) from RUN_C_OP_STEPCHECK t where t.enterprise_code=? and t.step_check_name=? and t.operate_task_id=? and t.is_use='Y'";
		String whereSql = "";
		if (stepCheckId != null && stepCheckId.length > 0) {
			whereSql += " and t.step_check_id<>" + stepCheckId[0];
		}
		sql += whereSql;
		if (Integer.parseInt(bll.getSingal(sql,new Object[]{enterpriseCode,stepCheckName,operateTaskId}).toString()) > 0) {
			isSame = true;
		} else {
			isSame = false;
		}
		return isSame;
	}

	public void delete(RunCOpStepcheck entity) {
		try {
			entity.setIsUse("N");
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunCOpStepcheck update(RunCOpStepcheck entity) throws CodeRepeatException{
		try {
			if(!this.checkNameForAdd(entity.getEnterpriseCode(), entity.getOperateTaskId(), entity.getStepCheckName(),entity.getStepCheckId())){
				RunCOpStepcheck result = entityManager.merge(entity);
				return result;
			}else{
				throw new CodeRepeatException("名称重复!");
			}
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunCOpStepcheck findById(Long id) {
		LogUtil.log("finding RunCOpStepcheck instance with id: " + id,
				Level.INFO, null);
		try {
			RunCOpStepcheck instance = entityManager.find(
					RunCOpStepcheck.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<RunCOpStepcheck> findByTaskId(String enterpriseCode,
			Long operateTaskId) {
		String sql = "select t.* from RUN_C_OP_STEPCHECK t";
		String whereSql = " where t.is_use='Y'";
		if (enterpriseCode != null) {
			whereSql += " and t.enterprise_code='" + enterpriseCode + "'";
		}
		if (operateTaskId != null) {
			whereSql += " and t.operate_task_id=" + operateTaskId;
		}
		sql += whereSql+" order by t.display_no";
		return bll.queryByNativeSQL(sql, RunCOpStepcheck.class);
	}
	public boolean saveAllOperat(List<Map> addList,List<Map> updateList,String delStr,String currentMan,String enterpriseCod) throws CodeRepeatException{

		boolean operatStatus = false;
		final int maxSize=30;
		try{
			if (addList != null) {
				Long count = bll.getMaxId("RUN_C_OP_STEPCHECK", "step_check_id");
				int totalAdd = addList.size();
				for (int i=0;i<totalAdd;i++) {
					count++;
					Map map=addList.get(i);
					RunCOpStepcheck op=this.updateModel(map);
					if(op.getStepCheckId()==null){
						op.setStepCheckId(count);
						op.setEnterpriseCode(enterpriseCod);
						op.setModifyBy(currentMan);
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
					RunCOpStepcheck opUp=this.updateModel(mapUp);
					if(opUp.getStepCheckId()!=null){
						opUp.setEnterpriseCode(enterpriseCod);
						opUp.setModifyBy(currentMan);
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
					RunCOpStepcheck opDel=this.findById(Long.parseLong(ids[i]));
					this.delete(opDel);
					if(ids.length>maxSize){
						if((i+1)%maxSize==0){
							entityManager.flush();
						}						
					}	
				}
			}
			operatStatus=true;
		}catch(CodeRepeatException e){
			throw new CodeRepeatException("名称重复");
		}		
		return operatStatus;
	}
	private RunCOpStepcheck updateModel(Map map){
		RunCOpStepcheck model = new RunCOpStepcheck();
		Object stepCheckId = map.get("stepCheckId");
		Object operateTaskId = map.get("operateTaskId");
		Object stepCheckName = map.get("stepCheckName");
		Object displayNo = map.get("displayNo");
		Object memo = map.get("memo");
		Object checkStatus = map.get("checkStatus");
		if(stepCheckId==null || "".equals(stepCheckId.toString())){
			;
		}else{
			model=this.findById(Long.parseLong(stepCheckId.toString()));
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
		return model;
	}
}