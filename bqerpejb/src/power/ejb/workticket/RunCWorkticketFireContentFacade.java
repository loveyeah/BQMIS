package power.ejb.workticket;

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
public class RunCWorkticketFireContentFacade implements
		RunCWorkticketFireContentFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunCWorkticketFireContent save(RunCWorkticketFireContent entity)
			throws CodeRepeatException {
		try {
			if (!this.checkFireContentNameForAdd(entity.getFirecontentName(),
					entity.getEnterpriseCode())) {
				if (entity.getFirecontentId() == null) {
					entity.setFirecontentId(bll.getMaxId(
							"RUN_C_WORKTICKET_FIRE_CONTENT", "firecontent_id"));
				}
				entity.setIsUse("Y");
				entity.setModifyDate(new java.util.Date());
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("动火票内容名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMutil(String firecontentIds) {
		String sql = "update RUN_C_WORKTICKET_FIRE_CONTENT t\n"
				+ "set t.is_use='N'\n" + "where t.firecontent_id in("
				+ firecontentIds + ")";
		bll.exeNativeSQL(sql);
	}

	public RunCWorkticketFireContent update(RunCWorkticketFireContent entity)
			throws CodeRepeatException {
		LogUtil.log("updating RunCWorkticketFireContent instance", Level.INFO,
				null);
		try {
			if (!this.checkFireContentNameForAdd(entity.getFirecontentName(),
					entity.getEnterpriseCode(), entity.getFirecontentId())) {
				RunCWorkticketFireContent result = entityManager.merge(entity);
				result.setModifyDate(new java.util.Date());
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("动火票内容名称不能重复!");
			}

		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCWorkticketFireContent findById(Long id) {
		LogUtil.log(
				"finding RunCWorkticketFireContent instance with id: " + id,
				Level.INFO, null);
		try {
			RunCWorkticketFireContent instance = entityManager.find(
					RunCWorkticketFireContent.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean checkFireContentNameForAdd(String firecontentName,
			String enterpriseCode, Long... firecontentId) {

		boolean isSame = false;
		String sql = "select count(*) from RUN_C_WORKTICKET_FIRE_CONTENT t\n"
				+ "where t.firecontent_name='" + firecontentName + "'\n"
				+ "and t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.is_use='Y'";
		if (firecontentId != null && firecontentId.length > 0) {
			sql += "  and t.firecontent_id <> " + firecontentId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;

	}

	public List<RunCWorkticketFireContent> findByNameOrId(
			String enterpriseCode, String contentLike) {
		if (contentLike == null && "".equals(contentLike)) {
			contentLike = "";
		}
		String sql = "select * from run_c_workticket_fire_content t where t.enterprise_code=? and t.firecontent_id||t.firecontent_name like ? and t.is_use='Y'";
		return bll.queryByNativeSQL(sql, new Object[]{ enterpriseCode,
				"%"+contentLike+"%" }, RunCWorkticketFireContent.class);

	}
}