package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdJOutQuestFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJOutQuestFacadeRemote {
	/**
	 * 增加一条签报申请对象
	 * 
	 * @param entity 签报申请对象
	 *            
	 * @throws SQLException 
	 * @throws RuntimeException
	 *            
	 */
	public void save(AdJOutQuest entity) throws SQLException ;

	/**
	 * Delete a persistent AdJOutQuest entity.
	 * 
	 * @param entity
	 *            AdJOutQuest entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJOutQuest entity);

	/**
	 * 更新签报申请表
	 * 
	 * @param entity  签报申请对象 
	 * @param strUpdateTime 上次修改时间
	 *            
	 * @throws DataChangeException 
	 * @throws SQLException 
	 * @throws RuntimeException
	 *           
	 */
	public void update(AdJOutQuest entity, String strUpdateTime) throws DataChangeException, SQLException ;

	public AdJOutQuest findById(Long id);

	/**
	 * Find all AdJOutQuest entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJOutQuest property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJOutQuest> found by query
	 */
	public List<AdJOutQuest> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJOutQuest entities.
	 * 
	 * @return List<AdJOutQuest> all AdJOutQuest entities
	 */
	public List<AdJOutQuest> findAll();
	
	/**
	 * 取得所有的申报申请
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject getOutQuest(String strEnterpriseCode, String strWorkCode,
			final int... rowStartIdxAndCount) throws SQLException;
	
	/**
	 * 根据人员编码取得部门名称
	 * @param strWorkCode 人员编码
	 * @return  部门名称
	 */public String getDeptNameById(String strWorkCode);
	 
	 /**
	  * 根据code得到人名
	  */
	 public String getReaderManName(String strReaderMan);
	 
	 /**
		 * 取得所有的申报申请
		 * @throws SQLException 
		 */
	 @SuppressWarnings("unchecked")
	 public PageObject getDirectorOutQuest(String strEnterpriseCode,
		String strWorkCode, final int... rowStartIdxAndCount)
		throws SQLException;
}