package power.ejb.opticket;

import java.util.List;
import javax.ejb.Remote;

/**操作任务维护
 *
 */
@Remote
public interface RunCOpticketTaskFacadeRemote {
	/**
	 * 新增操作任务
	 * @param RunCOpticketTask entity 操作任务实体
	 * @return RunCOpticketTask 操作任务对象
	 */
	public RunCOpticketTask save(RunCOpticketTask entity);

	/**
	 * 删除一条操作任务，调用update方法置isUse为N
	 * @param RunCOpticketTask entity 操作任务实体
	 * @return void 
	 */
	public void delete(RunCOpticketTask entity);

	/**
	 *更新一条操作任务
	 *@param RunCOpticketTask entity 操作任务实体
	 *@return RunCOpticketTask 操作任务对象
	 */
	public RunCOpticketTask update(RunCOpticketTask entity);

	public RunCOpticketTask findById(Long id);

	/**
	 * 查找所有有效操作任务
	 * @param String enterpriseCode 企业编码
	 * @return List<RunCOpticketTask> 操作任务列表
	 */
	public List<RunCOpticketTask> findAll(String enterpriseCode);
	

	/**
	 * 根据父操作任务ID查找所有操作任务列表
	 * @param String enterpriseCode 企业编码
	 * @param Long parentOperateTaskId 父操作任务ID
	 * @return List<RunCOpticketTask> 操作任务列表
	 */
	public List<RunCOpticketTask> findByParentOperateTaskId(String enterpriseCode,Long parentOperateTaskId);
	/**
	 * 查询操作任务编码为4位的操作任务列表
	 * @param String enterpriseCode 企业编码
	 * @return List<RunCOpticketTask> 操作任务列表
	 */
	public List<RunCOpticketTask> findByOTaskCodeLength(String enterpriseCode);
	/**
	 * 由操作操作票生成操作步骤
	 * @param ticketNos 标准操作票号
	 * @param enterpriseCode 企业编码
	 * @param exeBy 执行人
	 */
	public void addByStandTickets(String ticketNos,Long parentId,String enterpriseCode,String exeBy);
	
	public void copyTicket(Long sourceId,Long destId);
	public RunCOpticketTask saveCopyTicket(RunCOpticketTask entity);
 }