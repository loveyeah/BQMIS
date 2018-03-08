package power.ejb.workticket;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**工作票来源管理
 * 
 */
@Remote
public interface RunCWorkticketSourceFacadeRemote {
	/**增加工作票来源
	 *@param RunCWorkticketSource 工作票来源实体
	 *@return RunCWorkticketSource 工作票来源实体
	 */
	public RunCWorkticketSource save(RunCWorkticketSource entity) throws CodeRepeatException;

	/**删除
	 * @param String sourceIds
	 * 工作票来源ID集(如:1,2,3)
	 */
	public void deleteMutil(String sourceIds);

	/**修改
	 * @param RunCWorkticketSource 工作票来源实体
	 *  @return RunCWorkticketSource    工作票来源实体      
	 */
	public RunCWorkticketSource update(RunCWorkticketSource entity) throws CodeRepeatException;
	/**根据id查询
	 * @param long id  工作票来源ID
	 * @return RunCWorkticketSource   工作票来源实体          
	 */
	public RunCWorkticketSource findById(Long id); 
	/**
	 * 根据名称或者Id模糊工作票来源列表
	 * @param String soureLike 名称或者Id关键字 
	 * @param String enterpriseCode 企业编码
	 * @return List<RunCWorkticketSource> 工作票来源列表
	 * 
	 */
	public List<RunCWorkticketSource> findByNameOrId(String enterpriseCode,String soureLike);
}