package power.ejb.run.repair;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * @author drdu 20100507
 */
@Stateless
public class RunCRepairProjectFacade implements RunCRepairProjectFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(RunCRepairProject entity) {
		LogUtil.log("saving RunCRepairProject instance", Level.INFO, null);
		try {
			if (entity.getRepairProjectId() == null) {
				entity.setRepairProjectId(bll.getMaxId("RUN_C_REPAIR_PROJECT",
						"repair_project_id"));
			}
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCRepairProject update(RunCRepairProject entity) {
		LogUtil.log("updating RunCRepairProject instance", Level.INFO, null);
		try {
			RunCRepairProject result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCRepairProject findById(Long id) {
		LogUtil.log("finding RunCRepairProject instance with id: " + id,
				Level.INFO, null);
		try {
			RunCRepairProject instance = entityManager.find(
					RunCRepairProject.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findList(String strWhere,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from  RUN_C_REPAIR_PROJECT \n";
			if (strWhere != "") {
				sql = sql + " where  " + strWhere;
			}
			List list = bll.queryByNativeSQL(sql, RunCRepairProject.class,
					rowStartIdxAndCount);
			String sqlCount = "select count(*)　from RUN_C_REPAIR_PROJECT \n";
			if (strWhere != "") {
				sqlCount = sqlCount + " where  " + strWhere;
			}
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

	@SuppressWarnings("unchecked")
	public List<RunCRepairProject> findRepairTreeList(String fRepairId,
			String enterpriseCode) {
		String strWhere = "";
		if (fRepairId.equals("root")) {
			strWhere = "   f_project_id = 0  \n";
		} else {
			strWhere = " f_project_id='" + fRepairId + "' \n";
		}
		strWhere = strWhere + "and  enterprise_code='" + enterpriseCode + "'\n"
				+ "and is_use='Y' order by repair_project_id";
		PageObject result = findList(strWhere);
		return result.getList();

	}

	public boolean IfHasChild(Long repairId, String enterpriseCode) {
		boolean isSame = false;
		String strWhere = "";
		if (repairId.equals("root")) {
			strWhere = "   f_project_id = 0  \n";
		} else {
			strWhere = " f_project_id='" + repairId + "' \n";
		}
		strWhere = strWhere + "and  enterprise_code='" + enterpriseCode + "'\n"
				+ "and is_use='Y' order by repair_project_id";
		String sql = "select count(1)\n" + "  from RUN_C_REPAIR_PROJECT t\n"
				+ " where " + strWhere;
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

	public RunCRepairProject findByCode(String repairId, String enterpriseCode) {
		String strWhere = "  repair_project_id='" + repairId + "' \n"
				+ " and  enterprise_code='" + enterpriseCode + "' \n"
				+ " and is_use='Y'";
		PageObject result = findList(strWhere);
		if (result.getList() != null) {
			if (result.getList().size() > 0) {
				return (RunCRepairProject) result.getList().get(0);
			}
		}
		return null;
	}

	public Object findRepairProjectInfo(Long repairProjectId) {
		String sql = "select p.repair_project_id,\n"
				+ "       p.f_project_id,\n"
				+ "       (select r.repair_project_name\n"
				+ "          from RUN_C_REPAIR_PROJECT r\n"
				+ "         where r.repair_project_id = p.repair_project_id) fPorjectName,\n"
				+ "       p.repair_project_name,\n"
				+ "       p.working_charge,\n"
				+ "       getworkername(p.working_charge),\n"
				+ "       p.working_menbers,\n" + "       p.working_time,\n"
				+ "       p.acceptance_first,\n"
				+ "       getworkername(p.acceptance_first),\n"
				+ "       p.acceptance_second,\n"
				+ "       getworkername(p.acceptance_second),\n"
				+ "       p.acceptance_third,\n"
				+ "       getworkername(p.acceptance_third),\n"
				+ "       decode(p.acceptance_level, '2', '二级', '3', '三级')\n"
				+ "  from RUN_C_REPAIR_PROJECT p\n" + " where p.is_use = 'Y'\n"
				+ "   and p.repair_project_id = " + repairProjectId + "";

		return bll.getSingal(sql);
	}

	public Object findWorkerNameObject(String workingCharge) {
		String sql = "select getworkername(p.working_charge)\n"
				+ "  from RUN_C_REPAIR_PROJECT p\n"
				+ " where p.working_charge = '" + workingCharge + "'";
		return bll.getSingal(sql);
	}
}