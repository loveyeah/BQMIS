package power.ejb.run.securityproduction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.budget.CbmJBudgetItemFacadeRemote;
import power.ejb.run.securityproduction.form.CheckupForm;

/**
 * 25项反措动态检查表
 * 
 * @author liuyi 090917
 */
@Stateless
public class SpJAntiAccidentCheckupFacade implements
		SpJAntiAccidentCheckupFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	protected CbmJBudgetItemFacadeRemote itemRemote;
	WorkflowService service;
	
	/**
	 * 新增一条25项反措动态检查表记录
	 */
	public SpJAntiAccidentCheckup save(SpJAntiAccidentCheckup entity) {
		LogUtil.log("saving SpJAntiAccidentCheckup instance", Level.INFO, null);
		try {
			String sql = "select count(*) \n"
				+ "from SP_J_ANTI_ACCIDENT_CHECKUP a \n"
				+ "where a.is_use='Y' \n"
				+ "and a.enterprise_code='" + entity.getEnterpriseCode() + "' \n"
				+ "and a.season='" + entity.getSeason() + "' \n"
				+ "and a.measure_code='" + entity.getMeasureCode() + "' \n";
			if(Long.parseLong(bll.getSingal(sql).toString()) > 0)
			{
				return null;
			}
			entity.setCheckupId(bll.getMaxId("SP_J_ANTI_ACCIDENT_CHECKUP",
					"CHECKUP_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条25项反措动态检查表记录
	 */
	public void delete(SpJAntiAccidentCheckup entity) {
		LogUtil.log("deleting SpJAntiAccidentCheckup instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(SpJAntiAccidentCheckup.class,
					entity.getCheckupId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条或多条25项反措动态检查表记录
	 */
	public void delete(String ids) {
		String sql = "update SP_J_ANTI_ACCIDENT_CHECKUP a set a.is_use = 'N' \n"
				+ "where a.CHECKUP_ID in (" + ids + ") ";
		String sqlAmend = "update SP_J_ANTI_ACCIDENT_AMEND a set a.is_use = 'N' \n"
			+ "where a.CHECKUP_ID in (" + ids + ") \n";
		bll.exeNativeSQL(sqlAmend);
		bll.exeNativeSQL(sql);
	}

	/**
	 * 更新一条25项反措动态检查表记录
	 */
	public SpJAntiAccidentCheckup update(SpJAntiAccidentCheckup entity) {
		LogUtil.log("updating SpJAntiAccidentCheckup instance", Level.INFO,
				null);
		try {
			String sql = "select count(*) \n"
				+ "from SP_J_ANTI_ACCIDENT_CHECKUP a \n"
				+ "where a.is_use='Y' \n"
				+ "and a.enterprise_code='" + entity.getEnterpriseCode() + "' \n"
				+ "and a.season='" + entity.getSeason() + "' \n"
				+ "and a.checkup_id <>" + entity.getCheckupId()
				+ "and a.measure_code='" + entity.getMeasureCode() + "' \n";
			if(Long.parseLong(bll.getSingal(sql).toString()) > 0)
			{
				return null;
			}
			SpJAntiAccidentCheckup result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJAntiAccidentCheckup findById(Long id) {
		LogUtil.log("finding SpJAntiAccidentCheckup instance with id: " + id,
				Level.INFO, null);
		try {
			SpJAntiAccidentCheckup instance = entityManager.find(
					SpJAntiAccidentCheckup.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<SpJAntiAccidentCheckup> findAll(
			final int... rowStartIdxAndCount) {
		LogUtil.log("finding all SpJAntiAccidentCheckup instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from SpJAntiAccidentCheckup model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getCheckupList(String status, String time,
			String measureCode,String specialCode,String checkBy, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.checkup_id, \n" + "a.measure_code, \n"
				+ "a.is_problem, \n" + "a.check_by, \n" + "a.approve_by, \n"
				+ "a.approve_text, \n" + "a.approve_status, \n"
				+ "a.season, \n" + "a.modify_by, \n"
				+ "getworkername(a.check_by), \n"
				+ "getworkcontent(a.approve_by), \n"
				+ "getworkername(a.modify_by), \n"
				+ "to_char(a.check_date,'yyyy-MM-dd'), \n"
				+ "to_char(a.approve_date,'yyyy-MM-dd'), \n"
				+ "to_char(a.modify_date,'yyyy-MM-dd'), \n"
				+ "b.MEASURE_NAME, \n"
				+ "b.special_code, \n"
				+ "getspecialname(b.special_code) \n"
				+ "from SP_J_ANTI_ACCIDENT_CHECKUP a, SP_J_ANTI_ACCIDENT b \n"
				+ "where a.is_use='Y' \n" + "and b.is_use='Y' \n"
				+ "and a.enterprise_code='" + enterpriseCode + "' \n"
				+ "and b.enterprise_code='" + enterpriseCode + "' \n"
				+ "and a.measure_code=b.measure_code \n";
		if (status != null && status.equals("03"))
			sql += "and a.approve_status in ('0','3') \n";
		else if (status != null && !status.equals(""))
			sql += "and a.approve_status ='" + status + "' ";
		if (time != null && !time.equals(""))
			sql += "and a.season='" + time + "' \n";
		if (measureCode != null && !measureCode.equals(""))
			sql += "and a.measure_code like '" + measureCode + "%' \n";
		if(specialCode != null && !specialCode.equals(""))
			sql += " and b.special_code='" + specialCode + "' \n";
		
		if(checkBy != null && !checkBy.equals(""))
			sql += " and a.check_by='" + checkBy + "' \n";
		String sqlCount = "select count(*) from (" + sql + ") ";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<CheckupForm> arrlist = new ArrayList<CheckupForm>();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				CheckupForm form = new CheckupForm();
				SpJAntiAccidentCheckup check = new SpJAntiAccidentCheckup();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					check.setCheckupId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					check.setMeasureCode(data[1].toString());
				if (data[2] != null)
					check.setIsProblem(data[2].toString());
				if (data[3] != null)
					check.setCheckBy(data[3].toString());
				if (data[4] != null)
					check.setApproveBy(data[4].toString());
				if (data[5] != null)
					check.setApproveText(data[5].toString());
				if (data[6] != null)
					check.setApproveStatus(data[6].toString());
				if (data[7] != null)
					check.setSeason(data[7].toString());
				if (data[8] != null)
					check.setModifyBy(data[8].toString());
				if (data[9] != null)
					form.setCheckName(data[9].toString());
				if (data[10] != null)
					form.setApproveName(data[10].toString());
				if (data[11] != null)
					form.setModifyName(data[11].toString());
				if (data[12] != null)
					form.setCheckDate(data[12].toString());
				if (data[13] != null)
					form.setApproveDate(data[13].toString());
				if (data[14] != null)
					form.setModifyDate(data[14].toString());
				if (data[15] != null)
					form.setMeasureName(data[15].toString());
				if(data[16] != null)
					form.setSpecialCode(data[16].toString());
				if(data[17] != null)
					form.setSpecialName(data[17].toString());
				form.setCheck(check);
				arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

}