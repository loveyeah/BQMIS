package power.ejb.administration;
// default package

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdCTimeWorkD.
 * @see .AdCTimeWorkD
  * @author MyEclipse Persistence Tools 
 */
@Stateless

public class AdCTimeWorkDFacade  implements AdCTimeWorkDFacadeRemote {
    @PersistenceContext private EntityManager entityManager;
	
		/**
		 * 保存定期工作明细
	 Perform an initial save of a previously unsaved AdCTimeWorkD entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity AdCTimeWorkD entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(AdCTimeWorkD entity) throws SQLException{
    				LogUtil.log("saving AdCTimeWorkD instance", Level.INFO, null);
	        try {
            entityManager.persist(entity);
            			LogUtil.log("save successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("save failed", Level.SEVERE, re);
	            throw new SQLException();
        }
    }
    
    /**
	 Delete a persistent AdCTimeWorkD entity.
	  @param entity AdCTimeWorkD entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(AdCTimeWorkD entity) throws SQLException{
    				LogUtil.log("deleting AdCTimeWorkD instance", Level.INFO, null);
	        try {
        	entity = entityManager.getReference(AdCTimeWorkD.class, entity.getId());
            entityManager.remove(entity);
            			LogUtil.log("delete successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("delete failed", Level.SEVERE, re);
	            throw new SQLException();
        }
    }
    
    /**
     * 更新定期工作明细
	 Persist a previously saved AdCTimeWorkD entity and return it or a copy of it to the sender. 
	 A copy of the AdCTimeWorkD entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity AdCTimeWorkD entity to update
	 @return AdCTimeWorkD the persisted AdCTimeWorkD entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
    public AdCTimeWorkD update(AdCTimeWorkD entity) throws SQLException{
    				LogUtil.log("updating AdCTimeWorkD instance", Level.INFO, null);
	        try {
            AdCTimeWorkD result = entityManager.merge(entity);
            			LogUtil.log("update successful", Level.INFO, null);
	            return result;
        } catch (RuntimeException re) {
        				LogUtil.log("update failed", Level.SEVERE, re);
	            throw new SQLException();
        }
    }
    /**
     * 检索工作明细
     * params id
     */
    public AdCTimeWorkD findById( Long id) throws SQLException{
    				LogUtil.log("finding AdCTimeWorkD instance with id: " + id, Level.INFO, null);
	        try {
            AdCTimeWorkD instance = entityManager.find(AdCTimeWorkD.class, id);
            return instance;
        } catch (RuntimeException re) {
        				LogUtil.log("find failed", Level.SEVERE, re);
	            throw new SQLException();
        }
    }    
    
	/**
	 * Find all AdCTimeWorkD entities.
	  	  @return List<AdCTimeWorkD> all AdCTimeWorkD entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdCTimeWorkD> findAll(
		) throws SQLException{
					LogUtil.log("finding all AdCTimeWorkD instances", Level.INFO, null);
			try {
			final String queryString = "select model from AdCTimeWorkD model";
								Query query = entityManager.createQuery(queryString);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find all failed", Level.SEVERE, re);
				throw new SQLException();
		}
	}
	
}