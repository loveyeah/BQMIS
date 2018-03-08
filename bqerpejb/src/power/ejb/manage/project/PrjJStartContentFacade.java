package power.ejb.manage.project;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.run.powernotice.form.PowerNoticeInfo;
import power.ejb.run.powernotice.RunJPowerNoticeApprove;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.reward.HrJMonthRewardDetail;
import power.ejb.manage.project.PrjJStartContent;
/**
 * Facade for entity PrjJStartContent.
 * 
 * @see power.ejb.manage.project.PrjJStartContent
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PrjJStartContentFacade implements PrjJStartContentFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved PrjJStartContent entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PrjJStartContent entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PageObject findOperationList(String e,String s,int d,int... rowStartIdxAndCount){
		PageObject	result = new PageObject();
		String sql="select t.reoprt_content_id,\n"+
		"t.reoprt_content\n"+
		"from PRJ_J_START_CONTENT t\n"+ 
        "where t.report_id='"+d+"' and t.content_type='"+s+"' and t.is_use='Y'";
		String sqlCount=
			"select count(1)\n" +
			"from PRJ_J_START_CONTENT t\n"+
			"where t.report_id='"+d+"' and t.content_type='"+s+"' and t.is_use='Y'";
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setTotalCount(totalCount);
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		//List arrlist = new ArrayList();
		result.setList(list);
		return result;
	}
		
	
	public void save(PrjJStartContent entity) {
		LogUtil.log("saving PrjJStartContent instance", Level.INFO, null);
		try {
			entity.setReoprtContentId(bll.getMaxId("PRJ_J_START_CONTENT", "REOPRT_CONTENT_ID"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void saveOrUpdateDetailList(List<PrjJStartContent> addList, List<PrjJStartContent> updateList) {
		if (addList != null && addList.size() > 0) {
			for (PrjJStartContent entity : addList) {
				this.save(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (PrjJStartContent entity : updateList) {
				this.update(entity);
			}
		}
	}
	/**
	 * Delete a persistent PrjJStartContent entity.
	 * 
	 * @param entity
	 *            PrjJStartContent entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	/*public void delete(PrjJStartContent entity) {
		LogUtil.log("deleting PrjJStartContent instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PrjJStartContent.class, entity
					.getReoprtContentId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}*/
	public void delete(String detailIds) {
		LogUtil.log("deleting PrjJStartContent instance", Level.INFO, null);
		try {
			System.out.println("喝酒");
			String deleteDetailSql = "update PRJ_J_START_CONTENT t set t.is_use = 'N' where t.REOPRT_CONTENT_ID in("+detailIds+")";
			bll.exeNativeSQL(deleteDetailSql);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PrjJStartContent entity and return it or a
	 * copy of it to the sender. A copy of the PrjJStartContent entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PrjJStartContent entity to update
	 * @return PrjJStartContent the persisted PrjJStartContent entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PrjJStartContent update(PrjJStartContent entity) {
		LogUtil.log("updating PrjJStartContent instance", Level.INFO, null);
		try {
			PrjJStartContent result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PrjJStartContent findById(Long id) {
		LogUtil.log("finding PrjJStartContent instance with id: " + id,
				Level.INFO, null);
		try {
			PrjJStartContent instance = entityManager.find(
					PrjJStartContent.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PrjJStartContent entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PrjJStartContent property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<PrjJStartContent> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PrjJStartContent> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PrjJStartContent instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PrjJStartContent model where model."
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
	 * Find all PrjJStartContent entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PrjJStartContent> all PrjJStartContent entities
	 */
	@SuppressWarnings("unchecked")
	public List<PrjJStartContent> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PrjJStartContent instances", Level.INFO, null);
		try {
			final String queryString = "select model from PrjJStartContent model";
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