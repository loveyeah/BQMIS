/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;
// default package


import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdcTimeWorkFacade.
 * @author liugonglei
 * 
 */
@Remote

public interface AdCTimeWorkFacadeRemote {
		/**
		 * 插入定期工作维护
	 Perform an initial save of a previously unsaved AdcTimeWork entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity AdcTimeWork entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(AdCTimeWork entity)throws SQLException;
    /**
	 Delete a persistent AdcTimeWork entity.
	  @param entity AdcTimeWork entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(AdCTimeWork entity)throws SQLException;
   /**
    * 更新定期工作维护
	 Persist a previously saved AdcTimeWork entity and return it or a copy of it to the sender. 
	 A copy of the AdcTimeWork entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity AdcTimeWork entity to update
	 @return AdcTimeWork the persisted AdcTimeWork entity instance, may not be the same
   * @throws DataFormatException 
	 @throws RuntimeException if the operation fails
	 */
	public AdCTimeWork update(AdCTimeWork entity,Long lngUpdateTime)throws SQLException, DataFormatException;
	/**
	 * 检索定期工作维护
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public AdCTimeWork findById( Long id)throws SQLException;
	/**
	 * Find all AdcTimeWork entities.
	  	  @return List<AdcTimeWork> all AdcTimeWork entities
	 */
	public List<AdCTimeWork> findAll(
		)throws SQLException;	
	

	/**
	 * 从类别编码查询定期工作维护
	 *
	 * @param 
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return AdJRoomPrice 定期工作维护
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByWorkType(String strWorkTypeCode, String strSubWorkTypeCode,
			String strEnterpriseCode, final int... rowStartIdxAndCount) throws ParseException;
}