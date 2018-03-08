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
import javax.xml.rpc.holders.LongHolder;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.salary.form.PolicyForm;

/**
 * 运行岗位倾斜政策维护
 * @author liuyi 090927
 */
@Stateless
public class HrCSalaryPolicyFacade implements HrCSalaryPolicyFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 *新增运行岗位倾斜政策维护数据
	 */
	public HrCSalaryPolicy save(HrCSalaryPolicy entity) {
		LogUtil.log("saving HrCSalaryPolicy instance", Level.INFO, null);
		try {
			String sql = "select count(*) from HR_C_SALARY_POLICY a \n"
				+ "where a.is_use='Y' \n"
				+ "and a.station_id=" + entity.getStationId();
			
			Long flag = Long.parseLong(bll.getSingal(sql).toString());
			if(flag > 0)
				return null;
			entity.setPolicyId(bll.getMaxId("HR_C_SALARY_POLICY", "policy_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}		
	}

	/**
	 * 删除一条运行岗位倾斜政策维护数据
	 */
	public void delete(HrCSalaryPolicy entity) {
		LogUtil.log("deleting HrCSalaryPolicy instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCSalaryPolicy.class, entity
					.getPolicyId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条或多条运行岗位倾斜政策维护数据
	 */
	public void delete(String ids)
	{
		String sql = "update HR_C_SALARY_POLICY a set a.is_use='N' \n"
			+ "where a.policy_id in(" + ids + ") \n";
		bll.exeNativeSQL(sql);
	}
	/**
	 * 更新一条运行岗位倾斜政策维护数据
	 */
	public HrCSalaryPolicy update(HrCSalaryPolicy entity) {
		LogUtil.log("updating HrCSalaryPolicy instance", Level.INFO, null);
		try {
			String sql = "select count(*) from HR_C_SALARY_POLICY a \n"
				+ "where a.is_use='Y' \n"
				+ "and a.station_id=" + entity.getStationId()
				+ "and a.policy_id <>" + entity.getPolicyId();
			Long flag = Long.parseLong(bll.getSingal(sql).toString());
			if(flag > 0)
				return null;
			HrCSalaryPolicy result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCSalaryPolicy findById(Long id) {
		LogUtil.log("finding HrCSalaryPolicy instance with id: " + id,
				Level.INFO, null);
		try {
			HrCSalaryPolicy instance = entityManager.find(
					HrCSalaryPolicy.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}


	/**
	 * 查询所有运行岗位倾斜政策维护数据
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String stationName,String enterpriseCode,final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCSalaryPolicy instances", Level.INFO, null);
		try {
			PageObject pg = new PageObject();
			String sql = "select a.policy_id, \n"
				+ "a.station_id, \n"
				+ "a.increase_range, \n"
				+ "a.memo, \n"
				+ "b.station_name \n"
				+ "from HR_C_SALARY_POLICY a,hr_c_station b \n"
				+ "where a.is_use='Y' \n"
				+ "and b.is_use='Y' \n" //update by sychen 20100901
//				+ "and b.is_use='U' \n" 
				+ "and a.enterprise_code='" + enterpriseCode + "' \n"
				+ "and b.enterprise_code='" + enterpriseCode + "' \n"
				+ "and a.station_id=b.station_id \n";
			if(stationName != null && !stationName.equals(""))
				sql += " and b.station_name like '%" + stationName + "%' \n";
			String sqlCount = "select count(*) from (" + sql + ") \n";
			List<PolicyForm> arrlist = new ArrayList<PolicyForm>();
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			if(list != null && list.size() > 0)
			{
				Iterator it = list.iterator();
				while(it.hasNext())
				{
					PolicyForm form = new PolicyForm();
					Object[] ob = (Object[])it.next();
					if(ob[0] != null)
						form.setPolicyId(Long.parseLong(ob[0].toString()));
					if(ob[1] != null)
						form.setStationId(Long.parseLong(ob[1].toString()));
					if(ob[2] != null)
						form.setIncreaseRange(Double.parseDouble(ob[2].toString()));
					if(ob[3] != null)
						form.setMemo(ob[3].toString());
					if(ob[4] != null)
						form.setStationName(ob[4].toString());
					arrlist.add(form);
				}
			}
			pg.setList(arrlist);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setTotalCount(totalCount);
			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}