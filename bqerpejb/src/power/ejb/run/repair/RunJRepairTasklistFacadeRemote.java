package power.ejb.run.repair;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJRepairTasklistFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJRepairTasklistFacadeRemote {
	
	/**查询所有检修任务单信息
	 * @return
	 */
	public PageObject getRepairTask(String year,String enterpirseCode);
	
	/**保存所有修改和增加的数据
	 * @param addList
	 * @param updateList
	 */
	public  void saveRepairTask(List<RunJRepairTasklist>  addList,List<RunJRepairTasklist> updateList)  throws CodeRepeatException;
	
	/**删除多条检修任务单信息
	 * @param ids
	 */
	public void delRepairTask(String ids);
	/**保存一条检修任务单信息
	 * @param entity
	 */
	public void save(RunJRepairTasklist entity);

	
	/**删除一条检修任务单信息
	 * @param entity
	 */
	public void delete(RunJRepairTasklist entity);
//	public  boolean  isHas(String tasklistName,String  enterpirseCode);
	
	/**修改检修任务单信息
	 * @param entity
	 * @return
	 */
	public RunJRepairTasklist update(RunJRepairTasklist entity);

	/**通过id找到此条检修任务单信息
	 * @param id
	 * @return
	 */
	public RunJRepairTasklist findById(Long id);
	
	/**
	 * 检修单选择页面的查询方法
	 * @param year
	 * @param sepeciality
	 * @param enterpirseCode
	 * @return
	 */
	public PageObject getRepairTaskSelect(String year,String sepeciality,String enterpirseCode);

	
}