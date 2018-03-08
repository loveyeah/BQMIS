package power.ejb.workticket;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class RunCWorkticketFlagFacade implements RunCWorkticketFlagFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll; 
	
	public RunCWorkticketFlag save(RunCWorkticketFlag entity) throws CodeRepeatException {
		LogUtil.log("saving RunCWorkticketFlag instance", Level.INFO, null);
		try {
			if(!this.checkFlagNameForAdd(entity.getFlagName(), entity.getEnterpriseCode())){
				if(entity.getFlagId()==null){
					entity.setFlagId(bll.getMaxId("run_c_workticket_flag", "flag_id"));
				}
				entity.setIsUse("Y");
				entity.setModifyDate(new Date());
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			}
			else{
				throw new CodeRepeatException("符号名称不能重复");
			}
			
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void deleteMulti(String flagIds) {	
		String sql=
			"update run_c_workticket_flag t\n" +
			"set t.is_use='N'\n" + 
			"where t.flag_id in("+flagIds+")";
        bll.exeNativeSQL(sql);
	}

	
	public RunCWorkticketFlag update(RunCWorkticketFlag entity) throws CodeRepeatException{
		LogUtil.log("updating RunCWorkticketFlag instance", Level.INFO, null);
		try {
			if(!this.checkFlagNameForAdd(entity.getFlagName(), entity.getEnterpriseCode(), entity.getFlagId())){
				entity.setModifyDate(new Date());
				RunCWorkticketFlag result = entityManager.merge(entity);
				
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			}
			else{
				throw new CodeRepeatException("符号名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public RunCWorkticketFlag findById(Long id) {
		LogUtil.log("finding RunCWorkticketFlag instance with id: " + id,
				Level.INFO, null);
		try {
			RunCWorkticketFlag instance = entityManager.find(
					RunCWorkticketFlag.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	 
	public boolean checkFlagNameForAdd(String flagName,String enterpriseCode,Long... flagId){
		
		boolean isSame = false;
		String sql=
			"select count(*) from RUN_C_WORKTICKET_FLAG t\n" +
			"where t.flag_name='"+flagName+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";
	    if(flagId !=null&& flagId.length>0){
	    	sql += "  and t.flag_id <> " + flagId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
	public List<RunCWorkticketFlag> findByNameOrId(String enterpriseCode,String flagLike) {
		if (flagLike == null && flagLike.length() < 1) {
			flagLike = "";
		}
		String sql = "select * from run_c_workticket_flag t where t.enterprise_code=? and t.flag_id||t.flag_name like ? and t.is_use='Y'";
		return bll.queryByNativeSQL(sql, new Object[]{enterpriseCode,"%"+flagLike+"%"},RunCWorkticketFlag.class);
		
	}
}