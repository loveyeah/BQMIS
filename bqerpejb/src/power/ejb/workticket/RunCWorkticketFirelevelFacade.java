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
 * Facade for entity RunCWorkticketFirelevel.
 * 
 * @see power.ejb.workticket.RunCWorkticketFirelevel
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCWorkticketFirelevelFacade implements
		RunCWorkticketFirelevelFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunCWorkticketFirelevel save(RunCWorkticketFirelevel entity)
			throws CodeRepeatException {
		LogUtil
				.log("saving RunCWorkticketFirelevel instance", Level.INFO,
						null);
		try {
			if (!this.checkFireLevelNameForAdd(entity.getFirelevelName(), entity
					.getEnterpriseCode())) {
				if (entity.getFirelevelId() == null) {
					entity.setFirelevelId(bll.getMaxId(
							"run_c_workticket_firelevel", "firelevel_id"));
				}
				entity.setIsUse("Y");
				entity.setModifyDate(new Date());
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("动火票级别名称不能重复！");
			}

		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String firelevelIds) {
		String sql = "update run_c_workticket_firelevel t\n"
				+ "set t.is_use='N'\n" + "where t.firelevel_id in("
				+ firelevelIds + ")";
		bll.exeNativeSQL(sql);
	}

	public RunCWorkticketFirelevel update(RunCWorkticketFirelevel entity)
			throws CodeRepeatException {
		LogUtil.log("updating RunCWorkticketFirelevel instance", Level.INFO,
				null);
		try {
			if (!this.checkFireLevelNameForAdd(entity.getFirelevelName(), entity
					.getEnterpriseCode(), entity.getFirelevelId())) {
				entity.setModifyDate(new Date());
				RunCWorkticketFirelevel result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("动火票级别名称不能重复！");
			}

		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCWorkticketFirelevel findById(Long id) {
		LogUtil.log("finding RunCWorkticketFirelevel instance with id: " + id,
				Level.INFO, null);
		try {
			RunCWorkticketFirelevel instance = entityManager.find(
					RunCWorkticketFirelevel.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean checkFireLevelNameForAdd(String firelevelName,
			String enterpriseCode, Long... firelevelId) {

		boolean isSame = false;
		String sql = "select count(*) from run_c_workticket_firelevel t\n"
				+ "where t.firelevel_name='" + firelevelName + "'\n"
				+ "and t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.is_use='Y'";
		if (firelevelId != null && firelevelId.length > 0) {
			sql += "  and t.firelevel_id <> " + firelevelId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

	public List<RunCWorkticketFirelevel> findByNameOrId(String enterpriseCode,String levelLike) {
		if (levelLike == null && levelLike.length() < 1) {
			levelLike = "";
		}
		String sql = "select * from run_c_workticket_firelevel t where t.enterprise_code=? and t.firelevel_id||t.firelevel_name like ? and t.is_use='Y'";
		return bll.queryByNativeSQL(sql, new Object[]{enterpriseCode,"%"+levelLike+"%"}, RunCWorkticketFirelevel.class);
		
	}
}