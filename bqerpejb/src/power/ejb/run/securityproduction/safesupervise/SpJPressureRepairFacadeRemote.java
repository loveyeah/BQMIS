package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SpJPressureRepairFacade.
 * 
 * @author sychen 20100423
 */
@Remote
public interface SpJPressureRepairFacadeRemote {

	/**
	 * 锅炉或压力容器、附件检修记录
	 * 
	 * @param nextTime
	 * @param type
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findSpJPressureRepairList(String nextTime, String type,
			String enterpriseCode, String queryName, String startTime,
			String endTime, String fillBy, String isMaint,
			int... rowStartIdxAndCount);

	/**
	 * 通过主键查找锅炉或压力容器、附件检修记录信息
	 * 
	 * @param id
	 * @return
	 */
	public SpJPressureRepair findById(Long id);

	/**
	 * 增加锅炉或压力容器、附件检修记录信息
	 * 
	 * @param entity
	 * @return
	 */
	public SpJPressureRepair saveSpJPressureRepair(SpJPressureRepair entity);

	/**
	 * 修改锅炉或压力容器、附件检修记录信息
	 * 
	 * @param entity
	 * @return
	 */
	public SpJPressureRepair updateSpJPressureRepair(SpJPressureRepair entity);

	/**
	 * 删除锅炉或压力容器、附件检修记录信息
	 * 
	 * @param ids
	 */
	public void deleteSpJPressureRepair(String ids);
	
	/**
	 * 压力容器信息
	 * 
	 * @param ids
	 */
	public PageObject findptYlrqJDjList();

}