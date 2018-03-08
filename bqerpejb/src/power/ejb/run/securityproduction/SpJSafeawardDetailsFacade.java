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
import power.ejb.run.securityproduction.form.SpJSafeawardDetailsForm;

/**
 * Facade for entity SpJSafeawardDetails.
 * 
 * @see power.ejb.run.securityproduction.SpJSafeawardDetails
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSafeawardDetailsFacade implements
		SpJSafeawardDetailsFacadeRemote {
	// property constants
	public static final String SAFEAWARD_ID = "safeawardId";
	public static final String ENCOURAGE_MAN = "encourageMan";
	public static final String ENCOURAGE_WAY = "encourageWay";
	public static final String ENCOURAGE_MONEY = "encourageMoney";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;

	protected boolean save(SpJSafeawardDetails entity) {
		try {
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			return false;
		}
	}

	protected boolean update(SpJSafeawardDetails entity) {
		try {
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			return false;
		}
	}

	protected boolean delete(String ids) {
		try {
			String sql = "DELETE FROM sp_j_safeaward_details t\n"
					+ " WHERE t.award_details_id IN (" + ids + ")";
			dll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public boolean saveAwardManList(List<SpJSafeawardDetails> addList,
			List<SpJSafeawardDetails> updateList, String delIds) {
		try {
			if (addList != null && addList.size() > 0) {
				Long id = dll.getMaxId("sp_j_safeaward_details",
						"AWARD_DETAILS_ID");
				int i = 0;
				for (SpJSafeawardDetails entity : addList) {
					entity.setAwardDetailsId(id + (i++));
					this.save(entity);
				}
			}
			for (SpJSafeawardDetails entity : updateList) {
				this.update(entity);
			}
			if (delIds != null && !delIds.trim().equals("")) {
				this.delete(delIds);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getAwardManList(String awardId, String enterpriseCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.award_details_id,\n"
					+ "       t.safeaward_id,\n"
					+ "       t.encourage_man,\n"
					+ "       GETWORKERNAME(t.encourage_man) encourage_man_name,\n"
					+ "       t.encourage_way,\n"
					+ "       t.encourage_money,\n"
					+ "		  GETDEPTNAME(GETDEPTBYWORKCODE(t.encourage_man)) encourage_man_dep"
					+ "  FROM sp_j_safeaward_details t\n"
					+ " WHERE t.safeaward_id = " + awardId + "\n"
					+ "   AND t.enterprise_code = '" + enterpriseCode
					+ "' order by t.award_details_id";
			String sqlCount = "SELECT COUNT(1)\n"
					+ "  FROM sp_j_safeaward_details t\n"
					+ " WHERE t.safeaward_id = " + awardId + "\n"
					+ "   AND t.enterprise_code = '" + enterpriseCode + "'";

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				SpJSafeawardDetailsForm model = new SpJSafeawardDetailsForm();
				SpJSafeawardDetails bodel = new SpJSafeawardDetails();
				Object[] data = (Object[]) it.next();
				bodel.setAwardDetailsId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					bodel.setSafeawardId(Long.parseLong(data[1].toString()));
				if (data[2] != null)
					bodel.setEncourageMan(data[2].toString());
				if (data[3] != null)
					model.setEncourageManName(data[3].toString());
				if (data[4] != null)
					bodel.setEncourageWay(data[4].toString());
				if (data[5] != null)
					bodel.setEncourageMoney(Double.parseDouble(data[5]
							.toString()));
				if (data[6] != null)
					model.setEncourageManDep(data[6].toString());
				model.setAwardDetailsInfo(bodel);
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