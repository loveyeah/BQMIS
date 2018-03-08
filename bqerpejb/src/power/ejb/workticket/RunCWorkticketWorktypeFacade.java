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

@Stateless
public class RunCWorkticketWorktypeFacade implements
		RunCWorkticketWorktypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public boolean checkSafeCodeForAdd(String workticketType,String worktypeName,
			String enterpriseCode, Long... worktypeId) {
		boolean isSame = false;
		String sql = "select count(*) from RUN_C_WORKTICKET_WORKTYPE t\n"
				+ "where t.worktype_name='" + worktypeName + "'\n"
				+ "and t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.workticket_type_code='" + workticketType + "'\n" 
				+ "and t.is_use='Y'";
		if (worktypeId != null && worktypeId.length > 0) {
			sql += "  and t.Worktype_Id <> " + worktypeId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

	public RunCWorkticketWorktype save(RunCWorkticketWorktype entity)
			throws CodeRepeatException {
		try {
			if (!this.checkSafeCodeForAdd(entity.getWorkticketTypeCode(),entity.getworktypeName(), entity
					.getEnterpriseCode())) {
				if (entity.getworktypeId() == null) {
					entity.setworktypeId(bll.getMaxId(
							"RUN_C_WORKTICKET_WORKTYPE", "worktype_id"));
				}
				entity.setModifyDate(new java.util.Date());
				entity.setIsUse("Y");
				entityManager.persist(entity);
				return entity;
			} else {
				throw new CodeRepeatException("工作类型名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(Long worktypeId) throws CodeRepeatException {
		RunCWorkticketWorktype entity = this.findById(worktypeId);
		if (entity != null) {
			entity.setIsUse("N");
			this.update(entity);
		}
	}

	public void deleteMulti(String worktypeIds) {
		String sql = "update RUN_C_WORKTICKET_WORKTYPE t\n"
				+ "set t.is_use='N'\n" + "where t.WORKTYPE_ID in("
				+ worktypeIds + ")";
		bll.exeNativeSQL(sql);

	}

	public RunCWorkticketWorktype update(RunCWorkticketWorktype entity)
			throws CodeRepeatException {
		try {
			if (!this.checkSafeCodeForAdd(entity.getWorkticketTypeCode(),entity.getworktypeName(), entity
					.getEnterpriseCode(), entity.getworktypeId())) {
				entity.setModifyDate(new java.util.Date());
				RunCWorkticketWorktype result = entityManager.merge(entity);
				return result;
			} else {
				throw new CodeRepeatException("工作名称不能重复!");
			}

		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCWorkticketWorktype findById(Long id) {
		try {
			RunCWorkticketWorktype instance = entityManager.find(
					RunCWorkticketWorktype.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String fuzzy,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from RUN_C_WORKTICKET_WORKTYPE t\n"
					+ "where  t.enterprise_code='" + enterpriseCode + "'\n"
					+ "and t.is_use='Y' and t.WORKTYPE_NAME like '%" + fuzzy
					+ "%'";
			List<RunCWorkticketWorktype> list = bll.queryByNativeSQL(sql,
					RunCWorkticketWorktype.class, rowStartIdxAndCount);
			String sqlCount = "select count(*) from RUN_C_WORKTICKET_WORKTYPE t\n"
					+ "where  t.enterprise_code='"
					+ enterpriseCode
					+ "'\n"
					+ "and t.is_use='Y' and t.WORKTYPE_NAME like '%"
					+ fuzzy
					+ "%'";
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

	public List<RunCWorkticketWorktype> findByWorkticketTypeCodeAndName(
			String enterpriseCode, String workticketTypeCode, String fuzzy) {
		String sql = "select * from run_c_workticket_worktype t where t.enterprise_code=? and t.workticket_type_code=? and t.worktype_name like ? and t.is_use='Y'";
		return bll.queryByNativeSQL(sql, new Object[] { enterpriseCode,
				workticketTypeCode, "%"+fuzzy+"%" },RunCWorkticketWorktype.class);
	}

	public List<RunCWorkticketWorktype> findByWorkticketTypeCode(
			String enterpriseCode, String workticketTypeCode) {
		String sql = "select * from run_c_workticket_worktype p where p.enterprise_code=? and (p.workticket_type_code=? or p.workticket_type_code='C') and p.is_use='Y'";
		return bll.queryByNativeSQL(sql, new Object[]{enterpriseCode,workticketTypeCode},RunCWorkticketWorktype.class);
	}

}