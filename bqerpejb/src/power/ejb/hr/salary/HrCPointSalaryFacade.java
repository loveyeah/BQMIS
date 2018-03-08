package power.ejb.hr.salary;

import java.util.ArrayList;
import java.util.Iterator;
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
import power.ejb.hr.salary.form.PointSalaryForm;

/**
 *  岗位薪点工资维护
 * @author liuyi 090927
 */
@Stateless
public class HrCPointSalaryFacade implements HrCPointSalaryFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增一条岗位薪点工资维护记录
	 */
	public void save(HrCPointSalary entity) {
		LogUtil.log("saving HrCPointSalary instance", Level.INFO, null);
		try {
			entity.setPointSalaryId(bll.getMaxId("HR_C_POINT_SALARY", "POINT_SALARY_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条岗位薪点工资维护记录
	 */
	public void delete(HrCPointSalary entity) {
		LogUtil.log("deleting HrCPointSalary instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCPointSalary.class, entity
					.getPointSalaryId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	

	/**
	 * 删除一条或多条岗位薪点工资维护记录
	 */
	public void delete(String ids)
	{
		String sql = "update HR_C_POINT_SALARY a set a.is_use = 'N' \n"
			+ "where a.POINT_SALARY_ID in(" + ids +") \n";
		bll.exeNativeSQL(sql);
	}

	/**
	 *  更新一条岗位薪点工资维护记录
	 */
	public HrCPointSalary update(HrCPointSalary entity) {
		LogUtil.log("updating HrCPointSalary instance", Level.INFO, null);
		try {
			HrCPointSalary result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCPointSalary findById(Long id) {
		LogUtil.log("finding HrCPointSalary instance with id: " + id,
				Level.INFO, null);
		try {
			HrCPointSalary instance = entityManager.find(HrCPointSalary.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(Long checkGrade,String enterpriseCode,final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCPointSalary instances", Level.INFO, null);
		try {
			PageObject pg = new PageObject();
			String sql = "select a.point_salary_id, \n"
				+ "a.check_station_grade, \n"
				+ "b.salary_level, \n"
				+ "a.job_point, \n"
				+ "a.memo, \n"
				+ "b.salary_level_name \n"
				+ "from HR_C_POINT_SALARY a,HR_C_SALARY_LEVEL b \n"
				+ "where b.is_use='Y' \n"
				+ "and b.salary_level=a.salary_level(+) \n"
				+ "and a.is_use(+) = 'Y' \n"
				+ "and a.enterprise_code(+)='" +enterpriseCode + "' \n"
				+ "and b.enterprise_code='" + enterpriseCode + "' \n";
			if(checkGrade != null)
				sql += " and a.check_station_grade(+)=" + checkGrade;
			String sqlCount = "select count(*) from (" + sql + ") \n";
			List<PointSalaryForm> arrlist = new ArrayList<PointSalaryForm>();
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			if(list != null && list.size() > 0)
			{
				Iterator it = list.iterator();
				while(it.hasNext())
				{
					PointSalaryForm form = new PointSalaryForm();
					Object[] data = (Object[])it.next();
					if(data[0] != null)
						form.setPointSalaryId(Long.parseLong(data[0].toString()));
					if(data[1] != null)
						form.setCheckStationGrade(Long.parseLong(data[1].toString()));
					if(data[2] != null)
						form.setSalaryLevel(Long.parseLong(data[2].toString()));
					if(data[3] != null)
						form.setJobPoint(Double.parseDouble(data[3].toString()));
					if(data[4] != null)
						form.setMemo(data[4].toString());
					if(data[5] != null)
						form.setLevelName(data[5].toString());
					arrlist.add(form);
				}
			}
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void savePointSalaryRec(List<HrCPointSalary> addList,
			List<HrCPointSalary> updateList) {
		if(addList != null && addList.size() > 0)
		{
			for(HrCPointSalary ps : addList)
			{
				this.save(ps);
				entityManager.flush();
			}				
		}
		if(updateList != null && updateList.size() > 0)
		{
			for(HrCPointSalary ps : updateList)
			{
				this.update(ps);
			}
		}
	}

}