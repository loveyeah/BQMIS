/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;
// default package

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for AdCTimeWorkDFacade.
 * @author liugonglei
 */
@Remote

public interface AdCTimeWorkDFacadeRemote {
		/**
		 * 保存定期工作明细
	 Perform an initial save of a previously unsaved AdCTimeWorkD entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity AdCTimeWorkD entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(AdCTimeWorkD entity)throws SQLException;
    /**
     * 
	 Delete a persistent AdCTimeWorkD entity.
	  @param entity AdCTimeWorkD entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(AdCTimeWorkD entity)throws SQLException;
   /**
    * 更新定期工作明细
	 Persist a previously saved AdCTimeWorkD entity and return it or a copy of it to the sender. 
	 A copy of the AdCTimeWorkD entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity AdCTimeWorkD entity to update
	 @return AdCTimeWorkD the persisted AdCTimeWorkD entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
	public AdCTimeWorkD update(AdCTimeWorkD entity)throws SQLException;
    /**
     * 检索工作明细
     * @param id
     * @return
     * @throws SQLException
     */
	public AdCTimeWorkD findById( Long id)throws SQLException;
	/**
	 * Find all AdCTimeWorkD entities.
	  	  @return List<AdCTimeWorkD> all AdCTimeWorkD entities
	 */
	public List<AdCTimeWorkD> findAll(
		)throws SQLException;	
}