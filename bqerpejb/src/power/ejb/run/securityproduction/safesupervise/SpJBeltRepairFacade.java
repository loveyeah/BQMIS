package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
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
 * Facade for entity SpJBeltRepair.
 * 
 * @see power.ejb.run.securityproduction.SpJBeltRepair
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJBeltRepairFacade implements SpJBeltRepairFacadeRemote {
	// property constants
	public static final String TOOL_ID = "toolId";
	public static final String USE_TIME = "useTime";
	public static final String BELT_NUMBER = "beltNumber";
	public static final String REPAIR_RESULT = "repairResult";
	public static final String BELONG_DEP = "belongDep";
	public static final String REPAIR_BY = "repairBy";
	public static final String REPAIR_DEP = "repairDep";
	public static final String MEMO = "memo";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	
	public PageObject  getBeltRepair(String workCode,String beginTime,String endTime,String enterPriseCode,final  int... rowStartIdxAndCount)
	{
		PageObject  result=new PageObject();
		String sql=
			"select b.repair_id," +
			"t.tool_id," +
			"b.use_time," +
			"b.belong_dep," +
			"getdeptname( b.belong_dep)," +
			"b.belt_number," +
			"b.repair_result,\n" +
			"b.repair_by," +
			"getworkername( b.repair_by),\n" + 
			"b.repair_dep," +
			"getdeptname(b.repair_dep)," +
			"to_char(b.repair_begin,'yyyy-MM-dd')," +
			"to_char(b.repair_end,'yyyy-MM-dd')," +
			"to_char(b.next_time,'yyyy-MM-dd')," +
			"b.memo ," +
			"t.tool_name \n" + 
			"from SP_J_BELT_REPAIR  b ,SP_C_TOOLS  t\n" + 
			"where  b.tool_id=t.tool_id\n" +
			" and  b.is_use='Y'\n" + 
			"and b.enterprise_code='"+enterPriseCode+"'";
		if(workCode != null && !workCode.equals(""))
			sql += "and b.fill_by='"+workCode+"' "; 
		if(beginTime != null && !beginTime.equals(""))
			sql += "and to_char(b.repair_begin,'yyyy-mm-dd') >= '"+beginTime+"' "; 
		if(endTime != null && !endTime.equals(""))
			sql += "and to_char(b.repair_end,'yyyy-mm-dd') <= '"+endTime+"' "; 
		String sqlcount="select count(1) from ("+sql+")";
//		System.out.println("the sql"+sql);
		Long  count=Long.parseLong(bll.getSingal(sqlcount).toString());
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		result.setList(list);
		result.setTotalCount(count);
		return result;
		}
	public SpJBeltRepair addBeltRepair(SpJBeltRepair entity)
	{
		try {
			
			Long repairID=bll.getMaxId("SP_J_BELT_REPAIR","repair_id");
			entity.setRepairId(repairID);
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return entity;
	}
	public SpJBeltRepair updateBeltRepair(SpJBeltRepair entity)
	{
		try {
			SpJBeltRepair result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void deleteBeltRepair(String ids)
	{
		String sql="update SP_J_BELT_REPAIR  b " +
				"set b.is_use='N'" +
				" where b.repair_id  in ("+ids+") ";
		bll.exeNativeSQL(sql);
	}
	
	public void save(SpJBeltRepair entity) {
		LogUtil.log("saving SpJBeltRepair instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(SpJBeltRepair entity) {
		LogUtil.log("deleting SpJBeltRepair instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(SpJBeltRepair.class, entity
					.getRepairId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public SpJBeltRepair update(SpJBeltRepair entity) {
		LogUtil.log("updating SpJBeltRepair instance", Level.INFO, null);
		try {
			SpJBeltRepair result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJBeltRepair findById(Long id) {
		LogUtil.log("finding SpJBeltRepair instance with id: " + id,
				Level.INFO, null);
		try {
			SpJBeltRepair instance = entityManager
					.find(SpJBeltRepair.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<SpJBeltRepair> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all SpJBeltRepair instances", Level.INFO, null);
		try {
			final String queryString = "select model from SpJBeltRepair model";
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