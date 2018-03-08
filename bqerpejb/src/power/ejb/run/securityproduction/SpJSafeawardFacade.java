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
import power.ejb.run.securityproduction.form.SpJSafeawardForm;

/**
 * Facade for entity SpJSafeaward.
 * 
 * @see power.ejb.run.securityproduction.SpJSafeaward
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSafeawardFacade implements SpJSafeawardFacadeRemote {
	// property constants
	public static final String ENCOURAGE_ITEM = "encourageItem";
	public static final String ENCOURAGE_REASON = "encourageReason";
	public static final String APPROVAL_BY = "approvalBy";
	public static final String COMPLETE_BY = "completeBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	public boolean save(SpJSafeaward entity) {
		try {
			entity.setAwardId(dll.getMaxId("SP_J_SAFEAWARD", "AWARD_ID"));
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			return false;
		}
	}

	public boolean delete(SpJSafeaward entity) {
		try {
			entity = entityManager.getReference(SpJSafeaward.class, entity
					.getAwardId());
			entityManager.remove(entity);
			String sql = "DELETE FROM sp_j_safeaward_details t\n"
					+ " WHERE t.safeaward_id=" + entity.getAwardId();
			dll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			return false;
		}
	}

	public boolean update(SpJSafeaward entity) {
		try {
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			return false;
		}
	}

	public SpJSafeawardForm findFormModel(Long id) {
		try {
			SpJSafeawardForm entity = new SpJSafeawardForm();
			String sql = "SELECT t.award_id,\n"
					+ "       t.encourage_item,\n"
					+ "       t.encourage_reason,\n"
					+ "       t.encourage_date,\n"
					+ "       to_char(t.encourage_date, 'yyyy-mm-dd') encourage_date_string,\n"
					+ "       t.approval_by,\n"
					+ "       GETWORKERNAME(t.approval_by) approval_by_name,\n"
					+ "       t.complete_by,\n"
					+ "       GETWORKERNAME(t.complete_by) complete_by_name\n"
					+ "  FROM sp_j_safeaward t\n" + " WHERE t.award_id = " + id;
			Object[] data = (Object[]) dll.getSingal(sql);
			SpJSafeaward model = new SpJSafeaward();

			model.setAwardId(id);
			if (data[1] != null)
				model.setEncourageItem(data[1].toString());
			if (data[2] != null)
				model.setEncourageReason(data[2].toString());
			if (data[3] != null)
				model.setEncourageDate(java.sql.Date
						.valueOf(data[3].toString()));
			if (data[4] != null)
				entity.setEncourageDateString(data[4].toString());
			if (data[5] != null)
				model.setApprovalBy(data[5].toString());
			if (data[6] != null)
				entity.setApprovalByName(data[6].toString());
			if (data[7] != null)
				model.setCompleteBy(data[7].toString());
			if (data[8] != null)
				entity.setCompleteByName(data[8].toString());
			entity.setAwardInfo(model);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJSafeaward findModel(Long id) {
		try {
			SpJSafeaward instance = entityManager.find(SpJSafeaward.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findByDate(String encourageDate, String enterpriseCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.award_id,\n"
					+ "       t.encourage_item,\n"
					+ "       to_char(t.encourage_date, 'yyyy-mm-dd') encourage_date_string\n"
					+ "  FROM sp_j_safeaward t\n"
					+ " WHERE to_char(t.encourage_date, 'yyyy') = '"
					+ encourageDate + "'\n" + "   AND t.enterprise_code = '"
					+ enterpriseCode + "'\n" + " ORDER BY t.encourage_date,\n"
					+ "          t.award_id";
			String sqlCount = "SELECT COUNT(1)\n" + "  FROM sp_j_safeaward t\n"
					+ " WHERE to_char(t.encourage_date, 'yyyy') = '"
					+ encourageDate + "'\n" + "   AND t.enterprise_code = '"
					+ enterpriseCode + "'";

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				SpJSafeawardForm model = new SpJSafeawardForm();
				SpJSafeaward bodel = new SpJSafeaward();
				Object[] data = (Object[]) it.next();
				bodel.setAwardId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					bodel.setEncourageItem(data[1].toString());
				if (data[2] != null)
					model.setEncourageDateString(data[2].toString());
				model.setAwardInfo(bodel);
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