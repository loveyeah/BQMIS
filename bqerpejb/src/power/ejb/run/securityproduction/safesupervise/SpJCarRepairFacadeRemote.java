package power.ejb.run.securityproduction.safesupervise;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SpJCarRepairFacade.
 * 车辆维修记录
 * @author drdu 20100420
 */
@Remote
public interface SpJCarRepairFacadeRemote {

	/**
	 * 增加一条车辆维修记录
	 * @param entity
	 * @return
	 */
	public SpJCarRepair save(SpJCarRepair entity);

	/**
	 * 删除一条或多条车辆维修记录
	 * @param ids
	 */
	public void deleteMulti(String ids);
	/**
	 * 修改一条车辆维修记录
	 * @param entity
	 * @return
	 */
	public SpJCarRepair update(SpJCarRepair entity);

	/**
	 * 根据ID查找一条车辆维修记录的详细 
	 * @param id
	 * @return
	 */
	public SpJCarRepair findById(Long id);
	
	/**
	 * 根据维修时间段，车牌号，企业编码查询列表
	 * @param enterpriseCode
	 * @param sDate
	 * @param eDate
	 * @param carNo
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findCarRepairList(String enterpriseCode,String workCode,String sDate,String eDate,String carNo,final int... rowStartIdxAndCount);
}