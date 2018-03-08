package power.ejb.workticket.business;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity RunJWorkticketMap.
 * 
 * @see power.ejb.workticket.business.RunJWorkticketMap
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJWorkticketMapFacade implements RunJWorkticketMapFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public void save(RunJWorkticketMap entity) {
		LogUtil.log("saving RunJWorkticketMap instance", Level.INFO, null);
		try {
			if(entity.getId()==null)
			{
				entity.setId(bll.getMaxId("RUN_J_WORKTICKET_MAP", "id"));
			
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(RunJWorkticketMap entity) {
		LogUtil.log("deleting RunJWorkticketMap instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunJWorkticketMap.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public RunJWorkticketMap update(RunJWorkticketMap entity) {
		LogUtil.log("updating RunJWorkticketMap instance", Level.INFO, null);
		try {
			RunJWorkticketMap result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJWorkticketMap findById(Long id) {
		LogUtil.log("finding RunJWorkticketMap instance with id: " + id,
				Level.INFO, null);
		try {
			RunJWorkticketMap instance = entityManager.find(
					RunJWorkticketMap.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<RunJWorkticketMap> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunJWorkticketMap instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunJWorkticketMap model where model."
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
	public List<RunJWorkticketMap> findAll(final int... rowStartIdxAndCount) {
		LogUtil
				.log("finding all RunJWorkticketMap instances", Level.INFO,
						null);
		try {
			final String queryString = "select model from RunJWorkticketMap model";
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
	
	public RunJWorkticketMap findByWorkticketNo(String workticketNo)
	{
		String sql=
			"select * from RUN_J_WORKTICKET_MAP a\n" +
			" where a.workticket_no='"+workticketNo+"'";
		List<RunJWorkticketMap> list=bll.queryByNativeSQL(sql,RunJWorkticketMap.class);
		if(list!=null)
		{
			if(list.size()>0)
			{
				return (RunJWorkticketMap)list.get(0);
			}
		}
		return null;

	}

}