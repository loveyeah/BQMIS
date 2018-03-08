package power.ejb.workticket;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**工作条件管理
 * 
 */
@Remote
public interface RunCWorkticketWorkconditionFacadeRemote {
	/**增加工作条件
	 *@param RunCWorkticketWorkcondition entity
	 *工作条件对象
	 *@return RunCWorkticketWorkcondition
	 *工作条件对象
	 */
	public RunCWorkticketWorkcondition save(RunCWorkticketWorkcondition entity)throws CodeRepeatException;

	/**删除工作条件
	 * @param String conditionIds
	 * 工作条件id集(如:1,2,3)
	 */
	public void deleteMutil(String conditionIds);

	/**修改工作条件
	 *@param  RunCWorkticketWorkcondition entity
	 *工作条件对象
	 *@return RunCWorkticketWorkcondition
	 *工作条件对象
	 */
	public RunCWorkticketWorkcondition update(RunCWorkticketWorkcondition entity)throws CodeRepeatException;
	/**根据Id查询工作条件
	 *@param  Long conditionId
	 *工作条件id
	 *@return RunCWorkticketWorkcondition
	 *工作条件对象
	 */
	public RunCWorkticketWorkcondition findById(Long id);
	/**
	 * 根据名称或者Id模糊工作条件列表
	 * @param String conditionLike
	 * 名称或者Id
	 * @param String enterpriseCode
	 * 企业编码
	 * @return List<RunCWorkticketWorkcondition> 
	 * 工作条件列表
	 */
	public List<RunCWorkticketWorkcondition> findByNameOrId(String enterpriseCode,String conditionLike);
}