package power.ejb.run.repair;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunCRepairProjectFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCRepairProjectFacadeRemote {
	
	/**
	 * 新增一条检修项目维护
	 * @param entity
	 */
	public void save(RunCRepairProject entity);

	/**
	 * 修改一条检修项目维护
	 * @param entity
	 * @return
	 */
	public RunCRepairProject update(RunCRepairProject entity);

	/**
	 * 根据ID查找检修项目维护详细信息
	 * @param id
	 * @return
	 */
	public RunCRepairProject findById(Long id);

	/**
	 * 检修项目树列表记录
	 * @param fRepairProjectId
	 * @param enterpriseCode
	 * @return
	 */
	public List<RunCRepairProject> findRepairTreeList(String fRepairProjectId, String enterpriseCode);
	
	/**
	 * 根据ID查出记录详细
	 * @param repairProjectId
	 * @return
	 */
	public Object findRepairProjectInfo(Long repairProjectId);
	
	/**
	 * 判断是否孩子节点
	 * @param repairId
	 * @param enterpriseCode
	 * @return
	 */
	public boolean IfHasChild(Long repairId, String enterpriseCode);

	/**
	 * 根据ID查找详细
	 * @param boilerId
	 * @param enterpriseCode
	 * @return
	 */
	public RunCRepairProject findByCode(String repairId, String enterpriseCode);
	
	/**
	 * 根据workingCharge查找workingChargeName
	 * @param boilerId
	 * @param enterpriseCode
	 * @return
	 */
	public Object findWorkerNameObject(String workingCharge);
}