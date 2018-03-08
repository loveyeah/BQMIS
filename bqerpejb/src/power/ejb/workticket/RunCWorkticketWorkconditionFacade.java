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

/**
 * Facade for entity RunCWorkticketWorkcondition.
 * 
 * @see power.ejb.workticket.RunCWorkticketWorkcondition
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCWorkticketWorkconditionFacade implements
		RunCWorkticketWorkconditionFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunCWorkticketWorkcondition save(RunCWorkticketWorkcondition entity)
			throws CodeRepeatException {
		LogUtil.log("saving RunCWorkticketWorkcondition instance", Level.INFO,
				null);
		try {
			if (!this.checkConditionNameForAdd(entity.getConditionName(), entity
					.getEnterpriseCode())) {
				if (entity.getConditionId() == null) {
					entity.setConditionId(bll.getMaxId(
							"run_c_workticket_workcondition", "condition_id"));
				}
				entity.setIsUse("Y");
				entity.setModifyDate(new Date());
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("工作条件名称不能重复！");
			}

		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMutil(String conditionIds) {
		String sql = "update run_c_workticket_workcondition t\n"
				+ "set t.is_use='N'\n" + "where t.condition_id in("
				+ conditionIds + ")";
		bll.exeNativeSQL(sql);
	}

	public RunCWorkticketWorkcondition update(RunCWorkticketWorkcondition entity)
			throws CodeRepeatException {
		LogUtil.log("updating RunCWorkticketWorkcondition instance",
				Level.INFO, null);
		try {
			if (!this.checkConditionNameForAdd(entity.getConditionName(), entity
					.getEnterpriseCode(), entity.getConditionId())) {
				entity.setModifyDate(new Date());
				RunCWorkticketWorkcondition result = entityManager
						.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("工作条件名称不能重复！");
			}

		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCWorkticketWorkcondition findById(Long id) {
		LogUtil.log("finding RunCWorkticketWorkcondition instance with id: "
				+ id, Level.INFO, null);
		try {
			RunCWorkticketWorkcondition instance = entityManager.find(
					RunCWorkticketWorkcondition.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}


	public boolean checkConditionNameForAdd(String conditionName,
			String enterpriseCode, Long... conditionId) {

		boolean isSame = false;
		String sql = "select count(*) from run_c_workticket_workcondition t\n"
				+ "where t.condition_name='" + conditionName + "'\n"
				+ "and t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.is_use='Y'";
		if (conditionId != null && conditionId.length > 0) {
			sql += "  and t.condition_id <> " + conditionId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}
	public List<RunCWorkticketWorkcondition> findByNameOrId(String enterpriseCode,String conditionLike){
		if (conditionLike == null && conditionLike.length() < 1) {
			conditionLike = "";
		}
		String sql = "select * from run_c_workticket_workcondition t where t.enterprise_code=? and t.condition_id||t.condition_name like ? and t.is_use='Y'";
		return bll.queryByNativeSQL(sql, new Object[]{enterpriseCode,"%"+conditionLike+"%"},RunCWorkticketWorkcondition.class);
		
	}

}