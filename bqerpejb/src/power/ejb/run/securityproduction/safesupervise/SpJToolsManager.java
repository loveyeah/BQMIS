package power.ejb.run.securityproduction.safesupervise;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 电动工具和电气安全用具清册 操作接口
 * 
 * @author liuyi
 * 
 */
@Remote
public interface SpJToolsManager {
	/**
	 * 查询符合条件的电动工具和电气安全用具清册
	 * 
	 * @param toolName
	 *            名称
	 * @param toolModel
	 *            规格型号
	 * @param toolType
	 *            类别
	 * @param rowStartAndIdxCount
	 * @return
	 */
	public PageObject findToolsByCondi(String toolName, String toolModel,
			String toolType, String enterpriseCode, String fillBy,
			String isFiltrate, int... rowStartIdxAndCount);

	/**
	 * 保存 电动工具和电气安全用具清册
	 * 
	 * @param entity
	 *            保存对象
	 * @return
	 */
	String saveToolsEntity(SpCTools entity);

	/**
	 * 通过id查找工具
	 * 
	 * @param toolId
	 * @return
	 */
	SpCTools findToolById(Long toolId);

	/**
	 * 删除 电动工具和电气安全用具清册 多条删除
	 * 
	 * @param ids
	 *            删除ids
	 * @return
	 */
	String deleteToolsEntity(String ids);

	/**
	 * 查询 电气安全用具检修记录
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param toolCode
	 *            编号
	 * @param toolType
	 *            类别
	 * @param rowStartAndIdxCount
	 * @return
	 */
	PageObject findToolsRepairObject(String beginTime, String endTime,
			String toolCode, String toolType, String enterpriseCode,
			String isMaint, String fillBy, final int... rowStartIdxAndCount);

	/**
	 * 保存 电气安全用具检修记录
	 * 
	 * @param entity
	 * @return
	 */
	String saveRepairEntity(SpJToolsRepair entity);

	/**
	 * 通过id查找检修记录
	 * 
	 * @param repairId
	 * @return
	 */
	SpJToolsRepair findRepairById(Long repairId);

	/**
	 * 删除 电气安全用具检修记录
	 * 
	 * @param ids
	 * @return
	 */
	String deleteRepairEntity(String ids);
}