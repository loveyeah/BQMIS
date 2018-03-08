package power.ejb.run.repair;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.BpJPlanRepairDetail;

/**
 * Remote interface for RunCRepairTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */

@Remote
public interface RunCRepairTypeFacadeRemote {
	public PageObject getRepairType(String enterPriseCode);
	/**保存检修类别
	 * @param entity
	 */
	public void save(RunCRepairType entity);
	
	/**保存一条或者多条检修记录
	 * @param addList
	 * @param updateList
	 * @throws ParseException
	 */
	public  void saveRepairType(List<RunCRepairType> addList, List<RunCRepairType> updateList) throws CodeRepeatException;
	

	
	/**删除一条检修类别记录
	 * @param entity
	 */
	public void delete(RunCRepairType entity);

	
	/**修改一条检修类别记录
	 * @param entity
	 * @return
	 */
	public RunCRepairType update(RunCRepairType entity);

	/**通过指定的id找到此条检修记录
	 * @param id
	 * @return
	 */
	public RunCRepairType findById(Long id);

	
}