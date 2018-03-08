package power.ejb.hr;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

@Stateless
public class HrCStationFacade implements HrCStationFacadeRemote {
	// property constants
	public static final String STATION_TYPE_ID = "stationTypeId";
	public static final String STATION_CODE = "stationCode";
	public static final String STATION_NAME = "stationName";
	public static final String STATION_DUTY = "stationDuty";
	public static final String IS_USE = "isUse";
	public static final String MEMO = "memo";
	public static final String RETRIEVE_CODE = "retrieveCode";
	public static final String ORDEY_BY = "ordeyBy";
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	public void save(HrCStation entity) throws CodeRepeatException {
		try {
			if(this.checkNameSame(entity.getStationName()))
			{
				throw new CodeRepeatException("此岗位名称已经存在，请重新输入！");
			} 
			if (entity.getStationId() == null) {
				entity.setStationId(bll.getMaxId("hr_c_station", "station_id"));
			}
			if (entity.getOrdeyBy() == null) {
				entity.setOrdeyBy(bll.getMaxId("hr_c_station", "ordey_by"));
			} 
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 未使用
	 * @param stationCode
	 * @return
	 */
	private boolean checkCodeSameForAdd(String stationCode)
	{
		String sql = "select count(1) from hr_c_station t where t.station_code=?";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{stationCode}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}
	
	/**
	 * add by fyyang 081212
	 * @param stationName
	 * @param stationId
	 * @return
	 */
	private boolean checkNameSame(String stationName,Long ... stationId)
	{
		String sql=
			"select count(1) from HR_C_STATION t\n" +
			"where t.station_name='"+stationName+"'";
		  if(stationId !=null&& stationId.length>0){
		    	sql += "  and t.station_id <> " + stationId[0];
		    } 
		    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
			{
		    	return true;
			}
		    else
		    {
		    	return false;
		    }
	}
	/**
	 * 未使用
	 * @param stationId
	 * @param stationCode
	 * @return
	 */
	private boolean checkCodeSameForUpdate(Long stationId,String stationCode)
	{
		String sql = "select count(1) from hr_c_station t where t.station_code=? and t.station_id <> ?";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{stationCode,stationId}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}

	/**
	 * 批量删除岗位
	 * 
	 * @param ids
	 *            岗位ID
	 */
	public void deletes(String ids) {
		String sql = "delete from hr_c_station t where t.station_id in(" + ids
				+ ")";
		bll.exeNativeSQL(sql);
	}

	/**
	 * 查询岗位
	 */
	@SuppressWarnings("unchecked")
	public PageObject GetStationList( String stationName,final int... rowStartIdxAndCount) {
		String sql = 
			"select a.station_id,\n" +
			"       nvl(b.station_type_name, a.station_type_id),\n" + 
			"       a.station_code,\n" + 
			"       a.station_name,\n" + 
			"       a.station_duty,\n" + 
			"       a.is_use,\n" + 
			"       a.retrieve_code,\n" + 
			"       a.memo,\n" + 
			"       a.work_kind,\n" + 
			"       a.station_level_id,\n" + 
			"       (select t.station_level_name\n" + 
			"          from hr_c_station_level t\n" + 
			"         where t.station_level_id = a.station_level_id\n" + 
			"           and t.is_use = 'Y')\n" + //update by sychen 20100831
//			"           and t.is_use = 'U')\n" + 
			"  from hr_c_station a, hr_c_station_type b\n" + 
			" where a.station_type_id = b.station_type_id(+)\n";
		if(stationName!=null&&!stationName.equals(""))
		{
			sql+=" and a.station_name like '%"+stationName.trim()+"%'";
		}
		sql +=" order by a.station_name";

		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		if(list!=null && list.size()>0)
		{
			PageObject result = new PageObject();
			result.setList(list);
			String sqlcount = "select count(*) from hr_c_station a";
			if(stationName!=null&&!stationName.equals(""))
			{
				sqlcount+=" where a.station_name like '%"+stationName.trim()+"%'";
			}
			result.setTotalCount(Long.parseLong(bll.getSingal(sqlcount).toString())); 
			return result;
		} 
		return null;
	} 
	/**
	 * 查询岗位
	 */
	@SuppressWarnings("unchecked")
	public List<HrCStation> GetAllStationList() {
		String sql = "select * \n"
				+ "  from hr_c_station \n"
				+ "  where  is_use='Y' order by ordey_by"; //update by sychen 20100831
//		        + "  where  is_use='U' order by ordey_by"; 
		return  bll.queryByNativeSQL(sql,HrCStation.class); 
	} 
	public void delete(HrCStation entity) {
		try {
			entity = entityManager.getReference(HrCStation.class, entity
					.getStationId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCStation update(HrCStation entity) throws CodeRepeatException { 
		try {
			if(this.checkNameSame(entity.getStationName(), entity.getStationId()))
			{
				throw new CodeRepeatException("此岗位名称已经存在，请重新输入！");
			} 
			HrCStation result = entityManager.merge(entity); 
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCStation findById(Long id) {
		LogUtil.log("finding HrCStation instance with id: " + id, Level.INFO,
				null);
		try {
			HrCStation instance = entityManager.find(HrCStation.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<HrCStation> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrCStation instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCStation model where model."
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
	public List<HrCStation> findStationListByFuzzy(String fuzzy)
	{
		String sql="select t.*\n" +
			"  from hr_c_station t\n" + 
			" where t.is_use = 'Y'\n" + //update by sychen 20100831
//			" where t.is_use = 'U'\n" + 
			"   and t.station_name like '%"+fuzzy+"%'\n" + 
			" order by t.ordey_by";
		return bll.queryByNativeSQL(sql, HrCStation.class);
	}

	public Long getStationIdByName(String name) {
		Long stationId = null;
		String sql = "select t.station_id from hr_c_station t where t.station_name='"+name+"'" +
				" and t.is_use='Y'"; //update by sychen 20100831
       //		" and t.is_use='U'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			stationId = Long.parseLong(obj.toString());
		return stationId;
	}
}