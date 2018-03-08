package power.ejb.workticket;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**工作票压板维护
 * 
 */
@Remote
public interface RunCWorkticketPressboardFacadeRemote {
	/**增加压板
	 * @param RunCWorkticketPressboard entity 压板实体
	 * @return RunCWorkticketPressboard 压板对象
	 */
	public RunCWorkticketPressboard save(RunCWorkticketPressboard entity) throws CodeRepeatException;

	/**物理删除压板
	 *@param RunCWorkticketPressboard entity 压板实体
	 *@return void 
	 */
	public void delete(RunCWorkticketPressboard entity) throws CodeRepeatException;

	/**更新压板
	 *@param RunCWorkticketPressboard entity 压板实体
	 * @return RunCWorkticketPressboard 压板对象
	 */
	public RunCWorkticketPressboard update(RunCWorkticketPressboard entity) throws CodeRepeatException;

	public RunCWorkticketPressboard findById(Long id);
	/**根据父压板id查找其子压板id
	 * @param Long parentPressboardId 父压板ID
	 * @return List<RunCWorkticketPressboard>
	 */
	public List<RunCWorkticketPressboard> findByParentPressboardId(Long parentPressboardId);
}