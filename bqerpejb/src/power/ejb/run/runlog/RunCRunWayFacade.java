package power.ejb.run.runlog;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity RunCRunWay.
 * 
 * @see power.ejb.run.runlog.RunCRunWay
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCRunWayFacade implements RunCRunWayFacadeRemote {

	public static final String IS_USE = "isUse";
	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public RunCRunWay save(RunCRunWay entity) throws CodeRepeatException {
		try{
		if(!this.CheckRunWayCodeSame(entity.getEnterpriseCode(), entity.getRunWayCode()))
		{
			entity.setRunKeyId(bll.getMaxId("RUN_C_RUN_WAY", "run_key_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			return entity;
		}
		else
		{
			throw new CodeRepeatException("编码不能重复!");
		}
		}catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCRunWay update(RunCRunWay entity) throws CodeRepeatException{
		try{
		if(!this.CheckRunWayCodeSame(entity.getEnterpriseCode(), entity.getRunWayCode()))
		{
			RunCRunWay result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			entityManager.persist(result);
			return result;
		}
		else
		{
			throw new CodeRepeatException("编码不能重复!");
		}
		}catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(RunCRunWay entity) {
		LogUtil.log("deleting RunCRunWay instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCRunWay.class, entity
					.getRunKeyId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String runwayIds)
	{
		String sql="update run_c_run_way w\n" +
			"     set w.is_use = 'N'\n" + 
			"     where w.run_key_id in("+runwayIds+")";
		bll.exeNativeSQL(sql);
	}
	
	public RunCRunWay findById(Long id) {
		LogUtil.log("finding RunCRunWay instance with id: " + id, Level.INFO,
				null);
		try {
			RunCRunWay instance = entityManager.find(RunCRunWay.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RunCRunWay> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCRunWay instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCRunWay model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
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
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	public List<RunCRunWay> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}
	
	@SuppressWarnings("unchecked")
	public List<RunCRunWay> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCRunWay instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCRunWay model";
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
	
	/**
	 * 判断运行方式编码是否重复
	 */
	public boolean CheckRunWayCodeSame(String enterpriseCode,String runwayCode) 
	{ 
		String sql =
					"select count(1)\n" +
					"from run_c_run_way t\n" + 
					"where t.enterprise_code = '"+enterpriseCode+"'\n" + 
					"and t.run_way_code = '"+runwayCode+"'\n" + 
					"and t.is_use = 'Y'";

		if(Long.parseLong(bll.getSingal(sql).toString())>0)
		{
			return true;
		}
		return false;
	}
	/**
	 * 根据企业编码查询运行方式列表
	 */
	public List<RunCRunWay> findAllList(String enterpriseCode){
		String strSql = 
						"select *\n" +
						"from run_c_run_way t\n" + 
						"where t.enterprise_code = '"+enterpriseCode+"'\n" + 
						"and t.is_use = 'Y'\n" + 
						"order by t.diaplay_no";

		
			return bll.queryByNativeSQL(strSql, RunCRunWay.class);
	
	}
	
}