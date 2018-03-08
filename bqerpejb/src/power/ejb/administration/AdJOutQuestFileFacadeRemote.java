package power.ejb.administration;

import java.sql.SQLException;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.QuestFileInfo;

/**
 * Remote interface for AdJOutQuestFileFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJOutQuestFileFacadeRemote {
	/**
	 * 增加一条签报申请附件对象
	 * 
	 * @param entity 签报申请附件对象
	 *            
	 * @throws SQLException 
	 * @throws RuntimeException
	 *            
	 */
	public void save(QuestFileInfo entity) throws SQLException;



	/**
	 * 更新签报申请附件表
	 * 
	 * @param entity  签报申请附件对象 
	 * @param strUpdateTime 上次修改时间
	 *            
	 * @throws DataChangeException 
	 * @throws SQLException 
	 * @throws RuntimeException
	 *           
	 */
	public void update(QuestFileInfo entity, String strUpdateTime) throws DataChangeException, SQLException;

	public PageObject findById(String strApply) throws SQLException;
	
	/**
	 *  查询所有申请附件
	 * @param strApplyId 申请单号
	 * @return 所有申请附件
	 */
	 public PageObject getAllQuestFile(String strApplyId, final int... rowStartIdxAndCount) throws SQLException;
	 
	 public QuestFileInfo findByPhyId(Long lngFileId) throws SQLException;

	
}