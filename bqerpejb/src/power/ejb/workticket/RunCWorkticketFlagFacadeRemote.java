package power.ejb.workticket;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * 符号管理
 */
@Remote
public interface RunCWorkticketFlagFacadeRemote {
	/**
	 * 增加符号
	 * @param RunCWorkticketFlag entity
	 * 符号对象
	 * @return  RunCWorkticketFlag 
	 * 符号对象
	 */
	public RunCWorkticketFlag save(RunCWorkticketFlag entity) throws CodeRepeatException;

	/**
	 * 删除符号
	 * @param String  flagIds
	 * 符号id集(如:1,2,3)
	 */
	public void deleteMulti(String flagIds);

	/**
	 * 修改符号
	 * @param RunCWorkticketFlag entity
	 * 符号对象
	 * @return  RunCWorkticketFlag
	 * 符号对象 
	 * 
	 */
	public RunCWorkticketFlag update(RunCWorkticketFlag entity) throws CodeRepeatException;
	/**
	 * 根据id查询
	 * @param Long id
	 * 符号id
	 * @return  RunCWorkticketFlag 
	 * 符号对象 
	 */
	public RunCWorkticketFlag findById(Long id);

	
	 
	/**
	 * 根据名称或者Id模糊符号列表
	 * @param String flagLike
	 * 名称或者Id
	 * @param String enterpriseCode
	 * 企业编码
	 * @return List<RunCWorkticketFlag>
	 * 符号列表
	 */
	public List<RunCWorkticketFlag> findByNameOrId(String enterpriseCode,String flagLike);
}