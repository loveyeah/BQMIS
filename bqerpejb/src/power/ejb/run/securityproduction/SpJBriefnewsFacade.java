package power.ejb.run.securityproduction;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.run.securityproduction.form.BriefNewsForm;

/**
 * Facade for entity SpJBriefnews.
 * 
 * @see power.ejb.run.securityproduction.SpJBriefnews
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJBriefnewsFacade implements SpJBriefnewsFacadeRemote {
	// property constants
	public static final String ISSUE = "issue";
	public static final String CONTENT = "content";
	public static final String COMMON_BY = "commonBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved SpJBriefnews entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SpJBriefnews entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public SpJBriefnews save(SpJBriefnews entity) {
		try {
			if(entity.getBriefnewsId()== null){
				entity.setBriefnewsId(bll.getMaxId("SP_J_BRIEFNEWS", "BRIEFNEWS_ID"));	
			}
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent SpJBriefnews entity.
	 * 
	 * @param entity
	 *            SpJBriefnews entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SpJBriefnews entity) {
		try {
			entity = entityManager.getReference(SpJBriefnews.class, entity
					.getBriefnewsId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved SpJBriefnews entity and return it or a copy of
	 * it to the sender. A copy of the SpJBriefnews entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            SpJBriefnews entity to update
	 * @return SpJBriefnews the persisted SpJBriefnews entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SpJBriefnews update(SpJBriefnews entity) {
		try {
			SpJBriefnews result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJBriefnews findById(Long id) {
		try {
			SpJBriefnews instance = entityManager.find(SpJBriefnews.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all SpJBriefnews entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SpJBriefnews property to query
	 * @param value
	 *            the property value to match
	 * @return List<SpJBriefnews> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<SpJBriefnews> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding SpJBriefnews instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from SpJBriefnews model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<SpJBriefnews> findByIssue(Object issue) {
		return findByProperty(ISSUE, issue);
	}

	public List<SpJBriefnews> findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}

	public List<SpJBriefnews> findByCommonBy(Object commonBy) {
		return findByProperty(COMMON_BY, commonBy);
	}

	public List<SpJBriefnews> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all SpJBriefnews entities.
	 * 
	 * @return List<SpJBriefnews> all SpJBriefnews entities
	 */
	@SuppressWarnings("unchecked")
	public List<BriefNewsForm> findAll(String month) {
		try {
			final String queryString = 	"select t.briefnews_id,\n" +
				"t.issue,\n" + 
				"t.content,\n" + 
				"t.common_by,\n" + 
				"getworkername(t.common_by),\n" + 
				"to_char(t.month,'yyyy-MM') month ,\n" + 
				"to_char(t.common_date,'yyyy-MM-dd') commondate\n" + 
				"\n" + 
				"from sp_j_briefnews t\n" + 
				"where to_char(t.month,'yyyy-MM') like '"+month+"%'";
			List list = bll.queryByNativeSQL(queryString);
			List<BriefNewsForm> arraylist = new ArrayList<BriefNewsForm>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				SpJBriefnews model = new SpJBriefnews();
				BriefNewsForm brForm = new BriefNewsForm();
				if (data[0] != null) {
					model.setBriefnewsId(Long.parseLong(data[0].toString()));
				}
				if(data[1] != null)
					model.setIssue(Long.parseLong(data[1].toString()));
				if(data[2] != null)
					model.setContent(data[2].toString());
				if(data[3] != null)
					model.setCommonBy(data[3].toString());
				if(data[4] != null)
					brForm.setWorkName(data[4].toString());
				if(data[5] != null)
					brForm.setMonth(data[5].toString());
				if(data[6] != null)
					brForm.setCommondate(data[6].toString());
				brForm.setBriefnews(model);
				arraylist.add(brForm);
			}
			return arraylist;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}