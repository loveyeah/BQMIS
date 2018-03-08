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
import power.ejb.run.securityproduction.form.SpJSafepunishDetailsForm;

/**
 * Facade for entity SpJSafepunishDetails.
 * 
 * @see power.ejb.run.securityproduction.SpJSafepunishDetails
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSafepunishDetailsFacade implements
		SpJSafepunishDetailsFacadeRemote {
	// property constants
	public static final String PUNISH_ID = "punishId";
	public static final String PUNISH_MAN = "punishMan";
	public static final String PUNISH_TYPE = "punishType";
	public static final String PUNISH_MONEY = "punishMoney";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	protected boolean save(SpJSafepunishDetails entity) {
		try {
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			return false;
		}
	}

	protected boolean update(SpJSafepunishDetails entity) {
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
			String sql = "DELETE FROM SP_J_SAFEPUNISH_DETAILS t\n"
					+ " WHERE t.PUNISH_DETAILS_ID IN (" + ids + ")";
			dll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public boolean savePunishManList(List<SpJSafepunishDetails> addList,
			List<SpJSafepunishDetails> updateList, String delIds) {

		try {
			if (addList != null && addList.size() > 0) {
				Long id = dll.getMaxId("SP_J_SAFEPUNISH_DETAILS",
						"PUNISH_DETAILS_ID");
				int i = 0;
				for (SpJSafepunishDetails entity : addList) {
					entity.setPunishDetailsId(id + (i++));
					this.save(entity);
				}
			}
			for (SpJSafepunishDetails entity : updateList) {
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
	public PageObject getPunishManList(String punishId, String enterpriseCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.punish_details_id,\n"
					+ "       t.punish_id,\n"
					+ "       t.punish_man,\n"
					+ "       GETWORKERNAME(t.punish_man) punish_man_name,\n"
					+ "       t.punish_type,\n"
					+ "       t.punish_money,\n"
					+ "       GETDEPTNAME(GETDEPTBYWORKCODE(t.punish_man)) punish_man_dep\n"
					+ "  FROM sp_j_safepunish_details t\n"
					+ " WHERE t.punish_id = " + punishId + "\n"
					+ "   AND t.enterprise_code = '" + enterpriseCode
					+ "' order by t.punish_details_id";
			String sqlCount = "SELECT COUNT(1)\n"
					+ "  FROM sp_j_safepunish_details t\n"
					+ " WHERE t.punish_id = " + punishId + "\n"
					+ "   AND t.enterprise_code = '" + enterpriseCode + "'";

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				SpJSafepunishDetailsForm model = new SpJSafepunishDetailsForm();
				SpJSafepunishDetails bodel = new SpJSafepunishDetails();
				Object[] data = (Object[]) it.next();
				bodel.setPunishDetailsId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					bodel.setPunishId(Long.parseLong(data[1].toString()));
				if (data[2] != null)
					bodel.setPunishMan(data[2].toString());
				if (data[3] != null)
					model.setPunishManName(data[3].toString());
				if (data[4] != null)
					bodel.setPunishType(data[4].toString());
				if (data[5] != null)
					bodel
							.setPunishMoney(Double.parseDouble(data[5]
									.toString()));
				if (data[6] != null)
					model.setPunishManDep(data[6].toString());
				model.setPunishDetailsInfo(bodel);
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