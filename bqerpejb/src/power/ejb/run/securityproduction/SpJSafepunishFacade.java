package power.ejb.run.securityproduction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.run.securityproduction.form.SpJSafepunishForm;

/**
 * Facade for entity SpJSafepunish.
 * 
 * @see power.ejb.run.securityproduction.SpJSafepunish
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSafepunishFacade implements SpJSafepunishFacadeRemote {
	// property constants
	public static final String PUNISH_DEP = "punishDep";
	public static final String PUNISH_REASON = "punishReason";
	public static final String PUNISH_OPINION = "punishOpinion";
	public static final String APPROVAL_BY = "approvalBy";
	public static final String COMPLETE_BY = "completeBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	public boolean save(SpJSafepunish entity) {
		try {
			entity.setPunishId(dll.getMaxId("sp_j_safepunish", "PUNISH_ID"));
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			return false;
		}
	}

	public boolean delete(SpJSafepunish entity) {
		try {
			entity = entityManager.getReference(SpJSafepunish.class, entity
					.getPunishId());
			entityManager.remove(entity);
			String sql = "DELETE FROM sp_j_safepunish_details t\n"
					+ " WHERE t.PUNISH_ID=" + entity.getPunishId();
			dll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			return false;
		}
	}

	public boolean update(SpJSafepunish entity) {
		try {
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			return false;
		}
	}

	public SpJSafepunishForm findFormModel(Long id) {
		try {
			SpJSafepunishForm entity = new SpJSafepunishForm();
			String sql = "SELECT t.punish_id,\n"
					+ "       t.punish_dep,\n"
					+ "       GETDEPTNAME(t.punish_dep) punish_dep_name,\n"
					+ "       t.punish_reason,\n"
					+ "       t.punish_opinion,\n"
					+ "       t.punish_date,\n"
					+ "       to_char(t.punish_date, 'yyyy-mm-dd') punish_date_string,\n"
					+ "       t.approval_by,\n"
					+ "       GETWORKERNAME(t.approval_by) approval_by_name,\n"
					+ "       t.complete_by,\n"
					+ "       GETWORKERNAME(t.complete_by) complete_by_name\n"
					+ "  FROM sp_j_safepunish t\n" + " WHERE t.punish_id = "
					+ id;
			Object[] data = (Object[]) dll.getSingal(sql);
			SpJSafepunish model = new SpJSafepunish();

			model.setPunishId(id);
			if (data[1] != null)
				model.setPunishDep(data[1].toString());
			if (data[2] != null)
				entity.setPunishDepName(data[2].toString());
			if (data[3] != null)
				model.setPunishReason(data[3].toString());
			if (data[4] != null)
				model.setPunishOpinion(data[4].toString());
			if (data[5] != null)
				model.setPunishDate(java.sql.Date.valueOf(data[5].toString()));
			if (data[6] != null)
				entity.setPunishDateString(data[6].toString());
			if (data[7] != null)
				model.setApprovalBy(data[7].toString());
			if (data[8] != null)
				entity.setApprovalByName(data[8].toString());
			if (data[9] != null)
				model.setCompleteBy(data[9].toString());
			if (data[10] != null)
				entity.setCompleteByName(data[10].toString());
			entity.setPunishInfo(model);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJSafepunish findModel(Long id) {
		try {
			SpJSafepunish instance = entityManager
					.find(SpJSafepunish.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findByDate(String punishDate, String enterpriseCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.punish_id,\n"
					+ "       GETDEPTNAME(t.punish_dep) punish_dep_name,\n"
					+ "       to_char(t.punish_date, 'yyyy-mm-dd') punish_date_string\n"
					+ "  FROM sp_j_safepunish t\n"
					+ " WHERE to_char(t.punish_date, 'yyyy') = '" + punishDate
					+ "'\n" + "   AND t.enterprise_code = '" + enterpriseCode
					+ "'\n" + " ORDER BY t.punish_date,\n"
					+ "          t.punish_id";
			String sqlCount = "SELECT COUNT(1)\n"
					+ "  FROM sp_j_safepunish t\n"
					+ " WHERE to_char(t.punish_date, 'yyyy') = '" + punishDate
					+ "'\n" + "   AND t.enterprise_code = '" + enterpriseCode
					+ "'";

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				SpJSafepunishForm model = new SpJSafepunishForm();
				SpJSafepunish bodel = new SpJSafepunish();
				Object[] data = (Object[]) it.next();
				bodel.setPunishId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setPunishDepName(data[1].toString());
				if (data[2] != null)
					model.setPunishDateString(data[2].toString());
				model.setPunishInfo(bodel);
				arrlist.add(model);
			}
			result.setList(arrlist);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}
}