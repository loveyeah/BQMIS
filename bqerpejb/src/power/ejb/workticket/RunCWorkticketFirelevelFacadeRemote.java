package power.ejb.workticket;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
/**
 * 动火票级别管理
 */

@Remote
public interface RunCWorkticketFirelevelFacadeRemote {
	/**
	 *增加动火票级别
	 *@param  RunCWorkticketFirelevel entity
	 *动火票级别对象
	 *@return  RunCWorkticketFirelevel 
	 *动火票级别对象
	 */
	public RunCWorkticketFirelevel save(RunCWorkticketFirelevel entity) throws CodeRepeatException ;

	/**
	 * 删除动火票级别
	 * @param String firelevelIds
	 * 动火票级别ID集(如:1,2,3)
	 */
	public void deleteMulti(String firelevelIds);

	/**
	 * 修改动火票级别
	 * @param RunCWorkticketFirelevel entity
	 * 动火票级别对象
	 * @return  RunCWorkticketFirelevel
	 * 动火票级别对象
	 */
	public RunCWorkticketFirelevel update(RunCWorkticketFirelevel entity) throws CodeRepeatException;
	/**
	 * 根据id查询
	 * @param Long id
	 * 动火票级别id
	 * @return  RunCWorkticketFirelevel
	 * 动火票级别对象
	 */
	public RunCWorkticketFirelevel findById(Long id);
	/**
	 * 根据名称或者Id模糊查找动火票级别列表
	 * @param String levelLike
	 * 名称或者Id
	 * @param String enterpriseCode
	 * 企业编码
	 * @return List<RunCWorkticketFirelevel>
	 * 动火票级别列表
	 */
	public List<RunCWorkticketFirelevel> findByNameOrId(String enterpriseCode,String levelLike);
}