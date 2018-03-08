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
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
/**
 * Facade for entity RunCLogWeather.
 * 
 * @see power.ejb.run.runlog.RunCLogWeather
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCLogWeatherFacade implements RunCLogWeatherFacadeRemote {
	
	public static final String IS_USE = "isUse";
	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunCLogWeather save(RunCLogWeather entity) throws CodeRepeatException {
		LogUtil.log("saving RunCLogWeather instance", Level.INFO, null);
		try {
			if(!this.CheckWeatherSame(entity.getEnterpriseCode(), entity.getWeatherCode()))
			{
			entity.setWeatherKeyId(bll.getMaxId("run_c_log_weather", "weather_key_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
			}
			else{
				throw new CodeRepeatException("编码不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
 
	public void delete(Long weatherId) throws CodeRepeatException
	{
		RunCLogWeather entity=this.findById(weatherId);
		if(entity!=null)
		{
			entity.setIsUse("N");
			this.update(entity);
		}
	}

	public RunCLogWeather update(RunCLogWeather entity) throws CodeRepeatException {
		LogUtil.log("updating RunCLogWeather instance", Level.INFO, null);
		try {
			if(!this.CheckWeatherSame(entity.getEnterpriseCode(), entity.getWeatherCode(), entity.getWeatherKeyId()))
			{
				RunCLogWeather result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}
			else
			{
				throw new CodeRepeatException("编不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCLogWeather findById(Long id) {
		LogUtil.log("finding RunCLogWeather instance with id: " + id,
				Level.INFO, null);
		try {
			RunCLogWeather instance = entityManager.find(RunCLogWeather.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RunCLogWeather> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCLogWeather instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCLogWeather model where model."
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

	@SuppressWarnings("unchecked")
	public List<RunCLogWeather> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCLogWeather instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCLogWeather model";
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
	 * 查询所有的可用的天气信息
	 * 
	 * @return List<EquCFailureType> all EquCFailureType entities
	 */
	public List<RunCLogWeather> findAllList() throws Exception{
		String strSql = 
						"select t.weather_key_id,\n" +
						"                   t.weather_code,\n" + 
						"                   t.weather_name,\n" + 
						"                   t.diaplay_no,\n" + 
						"                   t.is_use,\n" + 
						"                   t.enterprise_code\n" + 
						"             from run_c_log_weather t\n" + 
						"             where t.is_use='Y'";


		try{
			return bll.queryByNativeSQL(strSql);
		}
		catch(Exception se){
			LogUtil.log("find all failed", Level.SEVERE, se);
			throw se;
		}
	}
	
	/**
	 * 根据企业编码查询天气列表
	 * @param enterpriseCode
	 * @return
	 */
	public List<RunCLogWeather> findWeatherList(String enterpriseCode)
	{
		String sql = 
			"                           select t.*\n" +
			"                           from run_c_log_weather t\n" + 
			"                           where t.enterprise_code='"+enterpriseCode+"'\n" + 
			"                           and t.is_use = 'Y'";

		return bll.queryByNativeSQL(sql,RunCLogWeather.class);
		
	}
	
	public List<RunCLogWeather> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findWeather(String fuzzy,String enterpriseCode,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject(); 
			String sql=
				"select * from run_c_log_weather t\n" +
				"where (t.weather_name like '%"+fuzzy+"%'\n" + 
				"or t.weather_code like '%"+fuzzy+"%')\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y'\n"+
				"order by diaplay_no";

			List<RunCLogWeather> list=bll.queryByNativeSQL(sql, RunCLogWeather.class, rowStartIdxAndCount);
			String sqlCount=
				"select count(*) from run_c_log_weather t\n" +
				"where t.weather_name like '%"+fuzzy+"%'\n" + 
				"and t.weather_code like '%"+fuzzy+"%'\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y'\n"+
				"order by diaplay_no";

			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean CheckWeatherSame(String enterpriseCode,String weatherCode,Long... weatherId) 
	{ 
		boolean isSame = false;
		String sql =
			"select count(1)\n" +
			"      from run_c_log_weather t\n" + 
			"      where t.is_use = 'Y'\n" + 
			"      and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"      and t.weather_code = '"+weatherCode+"'";


	    if(weatherId !=null&& weatherId.length>0){
	    	sql += "  and t.weather_key_id <> " + weatherId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}

}