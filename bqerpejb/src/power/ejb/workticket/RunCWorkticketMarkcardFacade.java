package power.ejb.workticket;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity RunCWorkticketMarkcard.
 * 
 * @see power.ejb.workticket.RunCWorkticketMarkcard
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCWorkticketMarkcardFacade implements
		RunCWorkticketMarkcardFacadeRemote {
	// property constants
	public static final String MARKCARD_TYPE_ID = "markcardTypeId";
	public static final String MARKCARD_CODE = "markcardCode";
	public static final String IS_OTHER_USE = "isOtherUse";
	public static final String ORDER_BY = "orderBy";
	public static final String MODIFY_BY = "modifyBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	protected boolean checkcode(String markcardCode, String enterpriseCode,
			Long... markcardId) {
		boolean isSame = false;
		String sql = "select count(1) from run_c_workticket_markcard t\n"
				+ "where t.MARKCARD_CODE='" + markcardCode + "'\n"
				+ "and t.ENTERPRISE_CODE='" + enterpriseCode + "'\n"
				+ "and t.is_use='Y'";

		if (markcardId != null && markcardId.length > 0) {
			sql += "  and t.MARKCARD_ID <> " + markcardId[0];
		}
		if (Long.parseLong((dll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

	public RunCWorkticketMarkcard save(RunCWorkticketMarkcard entity)
			throws CodeRepeatException {
		try {
			if (!this.checkcode(entity.getMarkcardCode(), entity
					.getEnterpriseCode())) {
				if (entity.getMarkcardId() == null) {
					entity.setMarkcardId(dll.getMaxId(
							"run_c_workticket_markcard", "MARKCARD_ID"));
				}
				if (entity.getOrderBy() == null) {
					entity.setOrderBy(entity.getMarkcardId());
				}
				entity.setModifyDate(new java.util.Date());
				entity.setIsUse("Y");
				entityManager.persist(entity);
				return entity;
			} else {
				throw new CodeRepeatException("编码不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(Long markcardId) {
		String sql = "update run_c_workticket_markcard t\n"
				+ "set t.is_use='N'\n" + "where t.MARKCARD_ID = " + markcardId;
		dll.exeNativeSQL(sql);
	}

	public void deleteMulti(String markcardIds) {
		String sql = "update run_c_workticket_markcard t\n"
				+ "set t.is_use='N'\n" + "where t.MARKCARD_ID in("
				+ markcardIds + ")";
		dll.exeNativeSQL(sql);
	}

	public RunCWorkticketMarkcard update(RunCWorkticketMarkcard entity)
			throws CodeRepeatException {
		try {
			if (!this.checkcode(entity.getMarkcardCode(), entity
					.getEnterpriseCode(), entity.getMarkcardId())) {
				entity.setModifyDate(new java.util.Date());
				RunCWorkticketMarkcard result = entityManager.merge(entity);
				return result;
			} else {
				throw new CodeRepeatException("安全关键词名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCWorkticketMarkcard findById(Long markcardId) {
		try {
			RunCWorkticketMarkcard instance = entityManager.find(
					RunCWorkticketMarkcard.class, markcardId);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, Long markcardTypeID,
			String fuzzy, final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from run_c_workticket_markcard t \n"
					+ "where t.enterprise_code=? and t.MARKCARD_TYPE_ID=? \n"
					+ "and t.MARKCARD_CODE like ? and t.is_use='Y' \n"
					+ " order by t.ORDER_BY,t.MARKCARD_TYPE_ID";
			String sqlCount = "select count(1) from run_c_workticket_markcard t \n"
					+ "where t.enterprise_code=? and t.MARKCARD_TYPE_ID=? \n"
					+ " and t.MARKCARD_CODE like ? and t.is_use='Y'";
			List<RunCWorkticketMarkcard> list;
			Long totalCount;
			if (fuzzy == null && "".equals(fuzzy)) {
				fuzzy = "";
			}
			list = dll.queryByNativeSQL(sql, new Object[] { enterpriseCode,
					markcardTypeID, "%" + fuzzy + "%" },
					RunCWorkticketMarkcard.class, rowStartIdxAndCount);
			totalCount = Long.parseLong(dll.getSingal(
					sqlCount,
					new Object[] { enterpriseCode, markcardTypeID,
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