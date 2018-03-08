package power.ejb.manage.plan.itemplan;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote
public interface EcomonicItemPlanManager 
{
	/**
	 * 取得所有的维护主题
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 */
	public PageObject getAllTopicRecord(String enterpriseCode, int... rowStartIdxAndCount);
	
	/**
	 * 批量修改维护主题数据
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	public void saveModifiedTopic(List<BpCItemplanTopic> addList,List<BpCItemplanTopic> updateList,String ids);
	
	/**
	 * 通过主题id取得该主题下的所有指标,只为维护用
	 * @param topic
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getItemByTopic(Long topic,String enterpriseCode, int... rowStartIdxAndCount);
	
	/**
	 * 批量修改维护指标数据
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	public void saveModifiedItem(List<BpCItemplanEcoItem> addList,List<BpCItemplanEcoItem> updateList,String ids);
	
	/**
	 * 查询全厂，分部门，计划，实际值，共通方法
	 *  * 返回的数组的取值顺序 0：主题ID，1：主题名称，2：主题显示顺序，3：经济指标ID，4：指标名称，5：指标别名，
	 * 6：单位Id，7：单位名称，8：分类，9：指标显示顺序，10：指标计划主ID，11：月份，12：计划工作流序号，
	 * 13：计划工作流状态，14：完成情况工作流序号，15：完成情况工作流状态，16：指标计划明细ID，
	 * 17：#11#12计划值，18：#1#2计划值，19：#11#12完成情况，20：#1#2完成情况
	 * @param isInsertData  分部门 插入最近数据  modified by liuyi 20100607
	 * @param topic
	 * @param month
	 * * @param planStatus 计划状态 reported:已上报，approved:已审批
	 * @param realStatus 实际值状态 reported:已上报，approved:已审批
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return 
	 */
//	public PageObject findItemByCondition(Long topic,String month,String planStatus, String realStatus,String enterpriseCode, int... rowStartIdxAndCount);
	public PageObject findItemByCondition(String isInsertData,Long topic,String month,String planStatus, String realStatus,String enterpriseCode, int... rowStartIdxAndCount);
	
	/**
	 * 批量保存全厂指标计划数据
	 * @param basic 主表数据
	 * @param addList 明细增加数据
	 * @param updateList 明细修改数据
	 */
	public void saveWholeItemPlanAndActual(BpJItemplanPlantMain basic,List<BpJItemplanPlantDetail> addList,List<BpJItemplanPlantDetail> updateList);
	
	/**
	 * 批量保存各部门指标计划
	 * @param basic 主表数据
	 * @param addList 明细增加数据
	 * @param updateList 明细修改数据
	 */
	
	public void savePartItemPlanAndActual(BpJItemplanDepMain basic,List<BpJItemplanDepDetail> addList,List<BpJItemplanDepDetail> updateList);
	
	/**
	 * 通过id找到全厂指标中的对象
	 * @param plantId
	 * @return
	 */
	public BpJItemplanPlantMain findWholeObject(Long plantId);
	
	/**
	 * 通过id找到分部门指标中的对象
	 * @param depId
	 * @return
	 */
	public BpJItemplanDepMain findPartObject(Long depId);
	
	/**
	 * 判断是否有部门填写、完成情况审批权限
	 * @param planId
	 * @param entityIds1
	 * @param entityIds2
	 * @return
	 */
	public List judgeApprovePlan(String planId,String entityIds1,String entityIds2);
	
	/**
	 * 判断是否有部门填写、完成情况审批权限(实际)
	 * @param planId
	 * @param entityIds1
	 * @param entityIds2
	 * @return
	 */
	public List judgeApproveFact(String planId,String entityIds1,String entityIds2);
	
}