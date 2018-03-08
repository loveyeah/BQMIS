package power.ejb.item;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity ItemJData.
 * 
 * @see power.ejb.item.ItemJData
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ItemJDataFacade implements ItemJDataFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * modify by fyyang 20100920
	 * @param entity
	 *            ItemJData entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public List GetElectricList(String enterpriseCode,String machineNo)
	{
		NativeSqlHelperRemote bll;
		bll = (NativeSqlHelperRemote)Ejb3Factory.getInstance().getFacadeRemote("NativeSqlHelper");
		String sql = 
//			"select month,sum(monthView.data_value) elecvalue\n" +
//			"from (select t.data_value,t.data_date datadate,t.enterprise_code,\n" + 
//			"t.machine_no,t.item_code,to_char(t.data_date,'YYYY-MM') month from item_j_data t) monthView\n" + 
//			"where to_char(sysdate,'YYYY') = to_char(monthView.datadate,'YYYY') group by monthView.month";
			
//			"select b.strmonth, a.data_value\n" +
//			"          from (select to_char(t.data_date, 'yyyy-MM') strmonth,\n" + 
//			"                       max(t.data_date) strdate\n" + 
//			"                  from bp_j_stat_ytz t\n" + 
//			"                 where to_char(t.data_date, 'yyyy') =\n" + 
//			"                       to_char(sysdate, 'yyyy')\n" + 
//			"                   and t.item_code = 'zfdl(d)'\n" + 
//			"                 group by to_char(t.data_date, 'yyyy-MM')) b,\n" + 
//			"               bp_j_stat_ytz a\n" + 
//			"         where a.item_code = 'zfdl(d)'\n" + 
//			"           and a.data_date = b.strdate";
			"select t1.strmonth, nvl(t2.data_value, 0)\n" +
			"  from (select to_char(sysdate, 'yyyy') || '-01' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate, 'yyyy') || '-02' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate, 'yyyy') || '-03' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate, 'yyyy') || '-04' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate, 'yyyy') || '-05' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate, 'yyyy') || '-06' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate, 'yyyy') || '-07' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate, 'yyyy') || '-08' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate, 'yyyy') || '-09' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate, 'yyyy') || '-10' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate, 'yyyy') || '-11' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate, 'yyyy') || '-12' as strmonth from dual) t1,\n" + 
			"\n" + 
			"       (select b.strmonth, a.data_value\n" + 
			"          from (select to_char(t.data_date, 'yyyy-MM') strmonth,\n" + 
			"                       max(t.data_date) strdate\n" + 
			"                  from bp_j_stat_ytz t\n" + 
			"                 where to_char(t.data_date, 'yyyy') =\n" + 
			"                       to_char(sysdate, 'yyyy')\n" + 
			"                   and t.item_code = 'zfdl(d)'\n" + 
			"                 group by to_char(t.data_date, 'yyyy-MM')) b,\n" + 
			"               bp_j_stat_ytz a\n" + 
			"         where a.item_code = 'zfdl(d)'\n" + 
			"           and a.data_date = b.strdate) t2\n" + 
			" where t1.strmonth = t2.strmonth(+)\n" + 
			" order by t1.strmonth";
		return bll.queryByNativeSQL(sql);
	}
	public List GetPreElectricList(String enterpriseCode,String machineNo)
	{
		NativeSqlHelperRemote bll;
		bll = (NativeSqlHelperRemote)Ejb3Factory.getInstance().getFacadeRemote("NativeSqlHelper");
		String sql = 
//			"select month,sum(monthView.data_value) elecvalue\n" +
//			"from (select t.data_value,t.data_date datadate,t.enterprise_code,\n" + 
//			"t.machine_no,t.item_code,to_char(t.data_date,'YYYY-MM') month from item_j_data t) monthView\n" + 
//			"where to_char(add_months(sysdate,-12),'YYYY') = to_char(monthView.datadate,'YYYY')\n" + 
//			"group by monthView.month";


			"select t1.strmonth, nvl(t2.data_value, 0)\n" +
			"  from (select to_char(sysdate - interval '1' year,'yyyy') || '-01' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate - interval '1' year,'yyyy') || '-02' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate - interval '1' year,'yyyy') || '-03' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate - interval '1' year,'yyyy') || '-04' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate - interval '1' year,'yyyy') || '-05' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate - interval '1' year,'yyyy') || '-06' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate - interval '1' year,'yyyy') || '-07' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate - interval '1' year,'yyyy') || '-08' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate - interval '1' year,'yyyy') || '-09' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate - interval '1' year,'yyyy') || '-10' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate - interval '1' year,'yyyy') || '-11' as strmonth\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select to_char(sysdate - interval '1' year,'yyyy') || '-12' as strmonth from dual) t1,\n" + 
			"\n" + 
			"       (select b.strmonth, a.data_value\n" + 
			"          from (select to_char(t.data_date, 'yyyy-MM') strmonth,\n" + 
			"                       max(t.data_date) strdate\n" + 
			"                  from bp_j_stat_ytz t\n" + 
			"                 where to_char(t.data_date, 'yyyy') =\n" + 
			"                       to_char(sysdate - interval '1' year,'yyyy')\n" + 
			"                   and t.item_code = 'zfdl(d)'\n" + 
			"                 group by to_char(t.data_date, 'yyyy-MM')) b,\n" + 
			"               bp_j_stat_ytz a\n" + 
			"         where a.item_code = 'zfdl(d)'\n" + 
			"           and a.data_date = b.strdate) t2\n" + 
			" where t1.strmonth = t2.strmonth(+)\n" + 
			" order by t1.strmonth";

//			"select b.strmonth, a.data_value\n" +
//			"          from (select to_char(t.data_date, 'yyyy-MM') strmonth,\n" + 
//			"                       max(t.data_date) strdate\n" + 
//			"                  from bp_j_stat_ytz t\n" + 
//			"                 where to_char(t.data_date, 'yyyy') =\n" + 
//			"                       to_char(sysdate - interval '1' year,'yyyy')\n" + 
//			"                   and t.item_code = 'zfdl(d)'\n" + 
//			"                 group by to_char(t.data_date, 'yyyy-MM')) b,\n" + 
//			"               bp_j_stat_ytz a\n" + 
//			"         where a.item_code = 'zfdl(d)'\n" + 
//			"           and a.data_date = b.strdate";


		return bll.queryByNativeSQL(sql);
	}
	public List GetElectricListOnUnit(String enterpriseCode,String flag)
	{
		//modify by fyyang 20100920
		NativeSqlHelperRemote bll;
		bll = (NativeSqlHelperRemote)Ejb3Factory.getInstance().getFacadeRemote("NativeSqlHelper");
		String sql = 
//			"select t.machine_no,sum(t.data_value) from item_j_data t\n" +
//			"where to_char(t.data_date,'YYYY') = to_char(sysdate,'YYYY')\n" + 
//			"and t.machine_no <> 0\n" + 
//			"group by t.machine_no order by t.machine_no";


			"select decode(b.item_code,\n" +
			"              '#11fdl(d)',\n" + 
			"              '#11机组',\n" + 
			"              '#12fdl(d)',\n" + 
			"              '#12机组',\n" + 
			"              '#1fdl(d)',\n" + 
			"              '#1机组',\n" + 
			"              '#2fdl(d)',\n" + 
			"              '#2机组',\n" + 
			"              ''),\n" + 
			"       b.data_value\n" + 
			"  from (select t.item_code, max(t.data_date) data_date\n" + 
			"          from bp_j_stat_ntz t\n" + 
			"         where t.item_code in\n" + 
			"               ('#11fdl(d)', '#12fdl(d)', '#1fdl(d)', '#2fdl(d)')\n" + 
			"           and to_char(t.data_date, 'yyyy') = to_char(sysdate,'yyyy')\n" + 
			"         group by t.item_code) a,\n" + 
			"       bp_j_stat_ntz b\n" + 
			" where b.item_code = a.item_code\n" + 
			"   and b.data_date = a.data_date\n" + 
			"   and b.data_date = a.data_date\n" + 
			" order by b.item_code asc";


		return bll.queryByNativeSQL(sql);
	}
	public List GetElectricUnit(String enterpriseCode,String machineNo)
	{
		NativeSqlHelperRemote bll;
		bll = (NativeSqlHelperRemote)Ejb3Factory.getInstance().getFacadeRemote("NativeSqlHelper");
		String sql = 
//			"select distinct t.machine_no from item_j_data t\n" +
//			"where t.machine_no <> 0 order by t.machine_no";		

			"select decode(t.item_code,\n" +
			"              '#11fdl(d)',\n" + 
			"              '#11机组',\n" + 
			"              '#12fdl(d)',\n" + 
			"              '#12机组',\n" + 
			"              '#1fdl(d)',\n" + 
			"              '#1机组',\n" + 
			"              '#2fdl(d)',\n" + 
			"              '#2机组',\n" + 
			"              ''),\n" + 
			"       max(t.data_value)\n" + 
			"  from bp_j_stat_ytz t\n" + 
			" where t.item_code in ('#11fdl(d)', '#12fdl(d)', '#1fdl(d)', '#2fdl(d)')\n" + 
			"   and to_char(t.data_date, 'yyyy-MM') = to_char(sysdate, 'yyyy-MM')\n" + 
			" group by t.item_code\n" + 
			" order by t.item_code asc";

		return bll.queryByNativeSQL(sql);
	}
	public void save(ItemJData entity) {
		LogUtil.log("saving ItemJData instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent ItemJData entity.
	 * 
	 * @param entity
	 *            ItemJData entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(ItemJData entity) {
		LogUtil.log("deleting ItemJData instance", Level.INFO, null);
		try {
			entity = entityManager
					.getReference(ItemJData.class, entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved ItemJData entity and return it or a copy of it
	 * to the sender. A copy of the ItemJData entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            ItemJData entity to update
	 * @return ItemJData the persisted ItemJData entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public ItemJData update(ItemJData entity) {
		LogUtil.log("updating ItemJData instance", Level.INFO, null);
		try {
			ItemJData result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ItemJData findById(ItemJDataId id) {
		LogUtil.log("finding ItemJData instance with id: " + id, Level.INFO,
				null);
		try {
			ItemJData instance = entityManager.find(ItemJData.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all ItemJData entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ItemJData property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<ItemJData> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<ItemJData> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding ItemJData instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from ItemJData model where model."
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

	/**
	 * Find all ItemJData entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ItemJData> all ItemJData entities
	 */
	@SuppressWarnings("unchecked")
	public List<ItemJData> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all ItemJData instances", Level.INFO, null);
		try {
			final String queryString = "select model from ItemJData model";
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

}