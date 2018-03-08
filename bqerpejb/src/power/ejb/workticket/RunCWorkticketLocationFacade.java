package power.ejb.workticket;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ejb.hr.LogUtil;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity RunCWorkticketLocation.
 * 
 * @see power.ejb.workticket.RunCWorkticketLocation
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCWorkticketLocationFacade implements
		RunCWorkticketLocationFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public boolean checkSafeCodeForAdd(String locationName,
			String enterpriseCode, Long... locationId) {
		boolean isSame = false;
		String sql = "select count(*) from RUN_C_WORKTICKET_LOCATION t\n"
				+ "where t.location_name='" + locationName + "'\n"
				+ "and t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.is_use='Y'";

		if (locationId != null && locationId.length > 0) {
			sql += "  and t.location_id <> " + locationId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

	public RunCWorkticketLocation save(RunCWorkticketLocation entity)
			throws CodeRepeatException {
		try {
			if (!this.checkSafeCodeForAdd(entity.getLocationName(), entity
					.getEnterpriseCode())) {
				if (entity.getLocationId() == null) {
					entity.setLocationId(bll.getMaxId(
							"RUN_C_WORKTICKET_LOCATION", "LOCATION_ID"));
				}
				entity.setModifyDate(new java.util.Date());
				entity.setIsUse("Y");
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("工作票区域名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(Long locationId) throws CodeRepeatException {
		RunCWorkticketLocation entity = this.findById(locationId);
		if (entity != null) {
			entity.setIsUse("N");
			this.update(entity);
		}
	}

	public void deleteMulti(String locationIds) {
		String sql = "update RUN_C_WORKTICKET_LOCATION t\n"
				+ "set t.is_use='N'\n" + "where t.location_id in("
				+ locationIds + ")";
		bll.exeNativeSQL(sql);

	}

	public RunCWorkticketLocation update(RunCWorkticketLocation entity)
			throws CodeRepeatException {
		try {
			if (!this.checkSafeCodeForAdd(entity.getLocationName(), entity
					.getEnterpriseCode(), entity.getLocationId())) {
				entity.setModifyDate(new java.util.Date());
				RunCWorkticketLocation result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("工作票区域名称不能重复!");
			}

		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCWorkticketLocation findById(Long id) {
		try {
			RunCWorkticketLocation instance = entityManager.find(
					RunCWorkticketLocation.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String blockCode,
			String fuzzy, final int... rowStartIdxAndCount)
			throws ParseException {
		try {
			PageObject result = new PageObject();
			if (fuzzy == null && "".equals(fuzzy)) {
				fuzzy = "";
			}
			String sql = "select t.* from RUN_C_WORKTICKET_LOCATION t\n"
					+ "where t.enterprise_code=?\n" + "and t.block_code=?\n"
					+ "and t.LOCATION_NAME like ? \n" + " and t.is_use='Y'"
					+ "order by t.ORDER_BY";
			List<RunCWorkticketLocation> list = bll
					.queryByNativeSQL(sql, new Object[] { enterpriseCode,
							blockCode, "%" + fuzzy + "%" },
							RunCWorkticketLocation.class, rowStartIdxAndCount);
			String sqlCount = "select count(1) from RUN_C_WORKTICKET_LOCATION t\n"
					+ "where t.enterprise_code=?\n"
					+ "and t.block_code=?\n"
					+ "and t.LOCATION_NAME like ? \n" + "and t.is_use='Y'";
			Long totalCount = Long.parseLong(bll
					.getSingal(
							sqlCount,
							new Object[] { enterpriseCode, blockCode,
									"%" + fuzzy + "%" }).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
}