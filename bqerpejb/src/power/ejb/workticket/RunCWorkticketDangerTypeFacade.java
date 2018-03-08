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
 * Facade for entity RunCWorkticketDangerType.
 * 
 * @see power.ejb.workticket.RunCWorkticketDangerType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCWorkticketDangerTypeFacade implements
		RunCWorkticketDangerTypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunCWorkticketDangerType save(RunCWorkticketDangerType entity)
			throws CodeRepeatException {

		try {
			if (!this.checkDangerTypeName(entity.getDangerTypeName(), entity
					.getWorkticketTypeCode())) {
				if (entity.getDangerTypeId() == null) {
					entity.setDangerTypeId(bll.getMaxId(
							"run_c_workticket_danger_type", "danger_type_id"));
				}
				entity.setModifyDate(new java.util.Date());
				entity.setIsUse("Y");
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("危险点类型名称和工作票类型不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(Long dangerTypeId) throws CodeRepeatException {

		try {
			RunCWorkticketDangerType entity = this.findById(dangerTypeId);
			if (entity != null) {
				entity.setIsUse("N");
				this.update(entity);
			}
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String dangerTypeIds) {
		String sql = "update  run_c_workticket_danger_type a\n"
				+ "set a.is_use='N' \n" + "where a.danger_type_id in ("
				+ dangerTypeIds + ")";
		bll.exeNativeSQL(sql);
	}

	public RunCWorkticketDangerType update(RunCWorkticketDangerType entity)
			throws CodeRepeatException {
		try {
			if (!checkDangerTypeName(entity.getDangerTypeName(), entity
					.getWorkticketTypeCode(), entity.getDangerTypeId())) {
				entity.setModifyDate(new java.util.Date());
				RunCWorkticketDangerType result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("危险点类型名称和工作票类型不能重复!");
			}

		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCWorkticketDangerType findById(Long dangerTypeId) {
		LogUtil.log("finding RunCWorkticketDangerType instance with id: "
				+ dangerTypeId, Level.INFO, null);
		try {
			RunCWorkticketDangerType instance = entityManager.find(
					RunCWorkticketDangerType.class, dangerTypeId);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String workticketTypeCode,
			String dangerTypeName, int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCWorkticketDangerType instances",
				Level.INFO, null);
		try {
			if (workticketTypeCode.equals("")) {
				workticketTypeCode = "%";
			}
			PageObject result = new PageObject();
			String sql = "select t.danger_type_id,t.danger_type_name,t.workticket_type_code,t.order_by,t.enterprise_code,nvl(GETWORKERNAME(t.modify_by),t.modify_by) modify_by,t.modify_date,t.is_use from run_c_workticket_danger_type t\n"
					+ "where t.danger_type_name like '%"
					+ dangerTypeName
					+ "%'\n"
					+ "and t.workticket_type_code like'"
					+ workticketTypeCode
					+ "'\n"
					+ "and t.is_use='Y' and t.enterprise_code='"
					+ enterpriseCode + "' " + "order by t.danger_type_id";
			List<RunCWorkticketDangerType> list = bll.queryByNativeSQL(sql,
					RunCWorkticketDangerType.class, rowStartIdxAndCount);
			String sqlCount = "select count(*) from run_c_workticket_danger_type t\n"
					+ "where t.danger_type_name like '%"
					+ dangerTypeName
					+ "%'\n"
					+ "and t.workticket_type_code like'"
					+ workticketTypeCode
					+ "'\n"
					+ "and t.is_use='Y' and t.enterprise_code='"
					+ enterpriseCode + "'";
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean checkDangerTypeName(String dangerTypeName,
			String workticketTypeCode, Long... dangerTypeId) {
		boolean isSame = false;
		String sql = "select count(*) from run_c_workticket_danger_type t\n"
				+ "where t.danger_type_name = '" + dangerTypeName + "'\n"
				+ "and t.is_use = 'Y'\n" + "and t.workticket_type_code = '"
				+ workticketTypeCode + "'";

		if (dangerTypeId != null && dangerTypeId.length > 0) {
			sql += "  and t.danger_type_id <> " + dangerTypeId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

}