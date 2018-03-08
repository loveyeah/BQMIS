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
public class RunCWorkticketSourceFacade implements
		RunCWorkticketSourceFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunCWorkticketSource save(RunCWorkticketSource entity)
			throws CodeRepeatException { 
		try {
			if (!this.checkSourceNameForAdd(entity.getSourceName(), entity
					.getEnterpriseCode())) {
				if (entity.getSourceId() == null) {
					entity.setSourceId(bll.getMaxId("run_c_workticket_source",
							"source_id")); 
				}
				entity.setIsUse("Y");
				entity.setModifyDate(new Date());
				entityManager.persist(entity); 
				return entity;
			} else {
				throw new CodeRepeatException("工作票来源名称不能重复！");
			}

		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMutil(String sourceIds) {
		String sql = "update run_c_workticket_source t\n"
				+ "set t.is_use='N'\n" + "where t.source_id in(" + sourceIds
				+ ")";
		bll.exeNativeSQL(sql);
	}

	public RunCWorkticketSource update(RunCWorkticketSource entity)
			throws CodeRepeatException {
		LogUtil.log("updating RunCWorkticketSource instance", Level.INFO, null);
		try {
			if (!this.checkSourceNameForAdd(entity.getSourceName(), entity
					.getEnterpriseCode(), entity.getSourceId())) {
				entity.setModifyDate(new Date());
				RunCWorkticketSource result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("工作票来源名称不能重复！");
			}

		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCWorkticketSource findById(Long id) {
		LogUtil.log("finding RunCWorkticketSource instance with id: " + id,
				Level.INFO, null);
		try {
			RunCWorkticketSource instance = entityManager.find(
					RunCWorkticketSource.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	private boolean checkSourceNameForAdd(String sourceName,
			String enterpriseCode, Long... sourceId) {

		boolean isSame = false;
		String sql = "select count(*) from run_c_workticket_source t\n"
				+ "where t.source_name='" + sourceName + "'\n"
				+ "and t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.is_use='Y'";
		if (sourceId != null && sourceId.length > 0) {
			sql += "  and t.source_id <> " + sourceId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

	public List<RunCWorkticketSource> findByNameOrId(String enterpriseCode,
			String soureLike) {
		if (soureLike == null && "".equals(soureLike)) {
			soureLike = "";
		}
		String sql = "select * from run_c_workticket_source t where t.enterprise_code=? and  t.source_id||t.source_name like ? and t.is_use='Y' order by t.SOURCE_ID";
		return bll.queryByNativeSQL(sql,new Object[]{
				enterpriseCode,"%"+ soureLike + "%"}, 
				RunCWorkticketSource.class);
	}

}