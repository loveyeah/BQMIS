package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.QuestReaderInfo;

/**
 * Remote interface for AdJOutQuestReaderFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJOutQuestReaderFacadeRemote {
	/**
	 * 增加一条签报申请抄送人对象
	 * 
	 * @param entity 签报申请抄送人对象
	 *            
	 * @throws SQLException 
	 * @throws RuntimeException
	 *            
	 */
	public void save(QuestReaderInfo entity) throws SQLException;

	/**
	 * Delete a persistent AdJOutQuestReader entity.
	 * 
	 * @param entity
	 *            AdJOutQuestReader entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJOutQuestReader entity);

	/**
	 * 更新签报申请抄送人表
	 * 
	 * @param entity  签报申请抄送人对象 
	 * @param strUpdateTime 上次修改时间
	 *            
	 * @throws DataChangeException 
	 * @throws SQLException 
	 * @throws RuntimeException
	 *           
	 */
	public void update(QuestReaderInfo entity, String strUpdateTime) throws DataChangeException, SQLException;

	public PageObject findById(String strApply) throws SQLException;

	/**
	 * Find all AdJOutQuestReader entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJOutQuestReader property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJOutQuestReader> found by query
	 */
	public List<AdJOutQuestReader> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all AdJOutQuestReader entities.
	 * 
	 * @return List<AdJOutQuestReader> all AdJOutQuestReader entities
	 */
	public List<AdJOutQuestReader> findAll();
	
	public QuestReaderInfo findByPhyId(Long lngReaderId) throws SQLException;
	
	/**
	 *  取得全部签报申请抄送人
	 *  @param applyId 申请单号
	 *  
	 *  @return 所有抄送人
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAllQuestReader(String lngApplyId, final int... rowStartIdxAndCount);
}