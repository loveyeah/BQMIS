package power.ejb.manage.stat;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity BpCStatReport.
 * 
 * @see power.ejb.manage.stat.BpCStatReport
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCStatReportFacade implements BpCStatReportFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(BpCStatReport entity) throws CodeRepeatException {
		LogUtil.log("saving BpCStatReport instance", Level.INFO, null);
		try {
			if (!this.checkNameSame(entity.getReportName(), entity
					.getEnterpriseCode())) {
				entity.setReportCode(bll.getMaxId("BP_C_STAT_REPORT",
						"report_code"));
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
			} else {
				throw new CodeRepeatException("名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCStatReport update(BpCStatReport entity)
			throws CodeRepeatException {
		LogUtil.log("updating BpCStatReport instance", Level.INFO, null);
		try {
			if (!this.checkNameSame(entity.getReportName(), entity
					.getEnterpriseCode(), entity.getReportCode())) {
				BpCStatReport result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCStatReport findById(Long id) {
		LogUtil.log("finding BpCStatReport instance with id: " + id,
				Level.INFO, null);
		try {
			BpCStatReport instance = entityManager
					.find(BpCStatReport.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void modifyRecords(List<BpCStatReport> list, String delIds)
			throws CodeRepeatException {
		if (list != null) {
			for (BpCStatReport model : list) {
				if (model.getReportCode() != null) {
					this.update(model);
				} else {
					this.save(model);
				}
				entityManager.flush();
			}
		}
		if (delIds != null && !delIds.trim().equals("")) {
			String sql = "delete from BP_C_STAT_REPORT t where t.report_code  in("
					+ delIds + ")";
			bll.exeNativeSQL(sql);
			String sql1 = "delete from bp_c_stat_report_item t where t.report_code  in("
					+ delIds + ")";
			bll.exeNativeSQL(sql1);
		}

	}

	private boolean checkNameSame(String reportName, String enterpriseCode,
			Long... id) {
		String sql = "select count(*) from BP_C_STAT_REPORT t\n"
				+ "where t.report_name='" + reportName
				+ "' and t.enterprise_code='" + enterpriseCode + "'";

		if (id != null && id.length > 0) {
			sql += "  and t.report_code <>" + id[0];
		}
		int count = Integer.parseInt(bll.getSingal(sql).toString());
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String reportName, String enterpriseCode,String workerCode,
			String type, final int... rowStartIdxAndCount) {

		String sqlCount = "select count(*) from BP_C_STAT_REPORT t\n"
				+ "where t.report_name like '%" + reportName
				+ "%' and t.enterprise_code='" + enterpriseCode + "'";
		if(type!=null && !type.equals("")){
			sqlCount+=" and t.REPORT_TYPE ='"+type+"'"
				+"and (t.report_code || '_tj' in (select r.code from JXL_REPORTS_RIGHT r where r.worker_code='"+workerCode+"') or\n" +
				"      t.report_code || '_tj' not in (select r.code from JXL_REPORTS_RIGHT r)\n" + 
				" )";

		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		if (totalCount > 0) {
			PageObject obj = new PageObject();
			obj.setTotalCount(totalCount);
			String sql = "select * from BP_C_STAT_REPORT t\n"
					+ "where t.report_name like '%" + reportName
					+ "%' and t.enterprise_code='" + enterpriseCode + "'";
			if(type!=null && !type.equals("")){
				sql+=" and t.REPORT_TYPE ='"+type+"'"
					+"and (t.report_code || '_tj' in (select r.code from JXL_REPORTS_RIGHT r where r.worker_code='"+workerCode+"') or\n" +
					"      t.report_code || '_tj' not in (select r.code from JXL_REPORTS_RIGHT r)\n" + 
					" )";

			}
			List<BpCStatReport> list = bll.queryByNativeSQL(sql,
					BpCStatReport.class, rowStartIdxAndCount);
			obj.setList(list);
			return obj;
		}
		return null;
	}

}