package power.ejb.run.runlog;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity RunCEquRunstatus.
 * 
 * @see power.ejb.run.runlog.RunCEquRunstatus
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCEquRunstatusFacade implements RunCEquRunstatusFacadeRemote {
	public static final String IS_USE = "isUse";
	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public int save(RunCEquRunstatus entity) {
		if (!CheckEquStatusSame(entity.getEnterpriseCode(), entity.getEqustatusId(),entity.getRunEquId())) {

			if (entity.getRunstatusId() == null) {
				entity.setRunstatusId(bll.getMaxId("RUN_C_EQU_RUNSTATUS","runstatus_id"));
			}
			entityManager.persist(entity);
			return Integer.parseInt(entity.getRunstatusId().toString());
		} else {
			return -1;
		}

	}

	public void delete(RunCEquRunstatus entity) {
		LogUtil.log("deleting RunCEquRunstatus instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCEquRunstatus.class, entity
					.getRunstatusId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCEquRunstatus update(RunCEquRunstatus entity) {
		LogUtil.log("updating RunCEquRunstatus instance", Level.INFO, null);
		try {
			RunCEquRunstatus result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCEquRunstatus findById(Long id) {
		LogUtil.log("finding RunCEquRunstatus instance with id: " + id,
				Level.INFO, null);
		try {
			RunCEquRunstatus instance = entityManager.find(
					RunCEquRunstatus.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<RunCEquRunstatus> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCEquRunstatus instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCEquRunstatus model where model."
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
	public List<RunCEquRunstatus> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCEquRunstatus instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCEquRunstatus model";
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

	
	
	
	public List<RunCEquRunstatus> findByIsUse(Object isUse,int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}
	
	/**
	 * 以设备状态ID为条件进行的查询
	 */
	public List findRunStatusList(String run_equ_id ,String enterpriseCode) 
	{
		String sql = 
			"select s.runstatus_id,\n" +
			"       r.run_equ_id,\n" + 
			"       t.equstatus_id,\n" + 
			"       t.status_name,\n" + 
			"       t.color_value,\n" + 
			"       t.status_desc,\n" + 
			"       s.is_use,\n"+
			"       s.enterprise_code\n" + 
			"from RUN_C_SHIFT_EQU r,RUN_C_EQUSTATUS t,RUN_C_EQU_RUNSTATUS s\n" + 
			"where s.run_equ_id = r.run_equ_id\n" + 
			"and s.equstatus_id = t.equstatus_id\n" + 
			"and s.enterprise_code='"+enterpriseCode+"'\n" + 
			"and s.is_use='Y' and r.run_equ_id = '"+run_equ_id+"'\n" + 
			"order by s.equstatus_id";
	

		return bll.queryByNativeSQL(sql);
		
	}
	
	/**
	 * 根据运行方式下设备ID获取某设备除了实时状态外的其它状态列表
	 * @param runEquId
	 * @param statusId
	 * @param enterpriseCode
	 * @return
	 */
	public List GetListExcept(Long runEquId, Long statusId, String enterpriseCode)
	{
		String sql =
			"		 select p.equstatus_id,\n" +
			"          q.status_name\n" + 
			"          from run_c_equ_runstatus p,run_c_equstatus q\n" + 
			"          where p.equstatus_id = q.equstatus_id\n" + 
			"          and p.run_equ_id = "+runEquId+"\n" +
			"          and p.equstatus_id !="+statusId+"\n" +
			"          and p.enterprise_code = q.enterprise_code\n" + 
			"          and p.enterprise_code = '"+enterpriseCode+"'\n" +
			"          and p.is_use = q.is_use\n" + 
			"          and p.is_use = 'Y'";
		
		return bll.queryByNativeSQL(sql);
	}
		
		/**
		 * 判断状态编码是否重复
		 */
		public boolean CheckEquStatusSame(String enterpriseCode,Long equstatusId,Long runequId) 
		{ 
			String sql =
				"select count(1)\n" +
				"from run_c_equ_runstatus t\n" + 
				"where t.equstatus_id ="+equstatusId+"\n" +
				"and t.run_equ_id = "+runequId+"\n" +
				"and t.enterprise_code = '"+enterpriseCode+"'\n" + 
				"and t.is_use ='Y'";

			if(Long.parseLong(bll.getSingal(sql).toString())>0)
			{
				return true;
			}
			return false;
		}
}