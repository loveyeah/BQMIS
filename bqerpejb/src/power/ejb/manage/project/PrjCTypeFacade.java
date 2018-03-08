package power.ejb.manage.project;



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
 * Facade for entity PrjCType.
 * 
 * @see power.ejb.manage.project.PrjCType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PrjCTypeFacade implements PrjCTypeFacadeRemote {
	// property constants


	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

    public int checkMarkCode(String markcode){
    	
    	
		int count=0;
		String sql = "select count(1) from PRJ_C_TYPE t where t.MARK_CODE='"
			+ markcode
			+"' and t.is_use='Y'"
			;
		count=Integer.parseInt(bll.getSingal(sql).toString());

			return count;

    	
    }


	public boolean getByPPrjtypeId(Long PContypeId){
		int count=0;
		String sql = "select count(1) from PRJ_C_TYPE t where t.PRJ_P_TYPE_ID="
			+ PContypeId
			+"and t.is_use='Y'"
			;
		count=Integer.parseInt(bll.getSingal(sql).toString());
		if(count!=0){
			return true;
		}
		else
			return false;
	}

	/**
	 * Perform an initial save of a previously unsaved PrjCType entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PrjCType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PrjCType save(PrjCType entity) {
		LogUtil.log("saving PrjCType instance", Level.INFO, null);
		try {
			if (entity.getPrjTypeId() == null) {
				entity.setPrjTypeId(bll
						.getMaxId("PRJ_C_TYPE", "PRJ_TYPE_ID"));
			}
			
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
					
			
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PrjCType entity.
	 * 
	 * @param entity
	 *            PrjCType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Long id) {
		LogUtil.log("deleting PrjCType instance", Level.INFO, null);
		try {
			PrjCType entity=this.findById(id);
			if(entity!=null)
			{
				entity.setIsUse("N");
				this.update(entity);
			}
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PrjCType entity and return it or a copy of it
	 * to the sender. A copy of the PrjCType entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PrjCType entity to update
	 * @return PrjCType the persisted PrjCType entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PrjCType update(PrjCType entity) {
		LogUtil.log("updating PrjCType instance", Level.INFO, null);
		try {
			PrjCType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PrjCType findById(Long id) {
		LogUtil.log("finding PrjCType instance with id: " + id, Level.INFO,
				null);
		try {
			PrjCType instance = entityManager.find(PrjCType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PrjCType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PrjCType property to query
	 * @param value
	 *            the property value to match
	 * @return List<PrjCType> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PrjCType> findByProperty(String propertyName, final Object value) {
		LogUtil.log("finding PrjCType instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PrjCType model where model."
					+ propertyName + "= :propertyValue"+" and model.isUse='Y'";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PrjCType> findByPPrjtypeId(Object prjPTypeId) {
		return findByProperty("prjPTypeId", prjPTypeId);
	}



	/**
	 * Find all PrjCType entities.
	 * 
	 * @return List<PrjCType> all PrjCType entities
	 */
	@SuppressWarnings("unchecked")
	public List<PrjCType> findAll() {
		LogUtil.log("finding all PrjCType instances", Level.INFO, null);
		try {
			final String queryString = "select model from PrjCType model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

/**
 * add by qxjiao 20100817
 */
	public String findPrjTypeById(long id) {
		String sql = "select prj_type_name from PRJ_C_TYPE where prj_type_id = "+id;
		String result =bll.getSingal(sql).toString();
		return result;
	}

}