package power.ejb.manage.plan.trainplan;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.trainplan.form.BpJTrainingSumForm;
import power.ejb.manage.plan.trainplan.form.BpJTrainingTypeForm;
import power.ejb.manage.plan.trainplan.form.BpTrainPlanApproveForm;

@Remote
public interface TrainPlanManager {
	// 类别
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public boolean addTrainPlanType(BpCTrainingType entity)
			throws CodeRepeatException;

	/**
	 * 
	 * @param ids
	 * @return
	 */
	public boolean delTrainPlanType(String ids);

	/**
	 * 
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getTrainPlanList(String enterpriseCode,
			final int... rowStartIdxAndCount);

	// public Long getMaxId(BpCTrainingType entity);
	public boolean updateTrainPlanType(Long id, BpCTrainingType entity);

	public boolean checkinput(BpCTrainingType entity);

	public PageObject findAllTypeList(String fuzzy, String enterpriseCode,
			final int... rowStartIdxAndCount);

	// 填写明细
	/**
	 * 新增培训计划信息
	 * 
	 * @param entity
	 * @param planTime
	 * @param planDept
	 * @return
	 */
//	public BpJTrainingDetail addTrainPlanApplyDetail(BpJTrainingDetail entity,
//			String planTime, String planDept) throws ParseException, CodeRepeatException;
	
//	public void save(List<BpJTrainingDetail> addList, List<BpJTrainingDetail> updateList,
//			String deleteId,String planTime,String planDept,String enterpriseCode)throws ParseException, CodeRepeatException;
	// modified by liuyi 20100427 培训计划填写时，需要填写人过滤
	public void save(List<BpJTrainingDetail> addList, List<BpJTrainingDetail> updateList,
			String deleteId,String planTime,String planDept,String enterpriseCode,String workerCode)throws ParseException, CodeRepeatException;

	/**
	 * 修改
	 * 
	 * @param entity
	 * @return
	 */
//	public BpJTrainingDetail updateTrainPlanApplyDetail(BpJTrainingDetail entity);

	/**
	 * 
	 * @param ids
	 * @return
	 */
	public BpJTrainingDetail delTrainPlanApplyDetail(String ids);

	/**
	 * modified by liuyi 20100427 isApply:1,部门培训计划申请，一个管理部门可以有多个主记录
	 * 根据‘部门’‘时间’‘计划类别’三个查询条件 找到满足要求的TRAINING_DETAIL_ID
	 * 
	 * @param planTime 时间
	 * @param planDept 部门
	 * @param planType 计划类别
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	public PageObject findTrainPlanApplyList(String approve,String entryIds,String planType, String planDate,
			String planDept, String enterpriseCode,String isApply,String workerCode, int... rowStartIdxAndCount)
			throws Exception;
    
	
	public PageObject findPlanGatherQueryList(String approve,String entryIds,String planType, String planDate,
			String planDept, String enterpriseCode,String isGatherQuery, int... rowStartIdxAndCount)
			throws Exception;
	
	/**
	 *  add by liuyi 20100427 
	 * 部门培训计划查询 只为该查询页面用 
	 * @param planTime
	 * @param planType
	 * @param planDept
	 * @param enterpriseCode
	 * @return
	 */
	PageObject getDeptTrainPlanQueryList(String planTime,String planType,String planDept,String enterpriseCode, int... rowStartIdxAndCount);
	/**
	 * 通过id 取一条详细记录
	 * 
	 * @param id
	 * @return
	 */
	public BpJTrainingDetail findByDetailId(Long id);

	// 操作主表
	/**
	 * 培训计划主表
	 * 
	 * @param mainId
	 * @return
	 */
	public BpJTrainingMain findById(Long mainId);

	/**
	 * 修改主表
	 * 
	 * @param entity
	 * @return
	 */
	public BpJTrainingMain update(BpJTrainingMain entity);

	public PageObject findAllList(String strWhere,
			final int... rowStartIdxAndCount);

	/**
	 * 审批取主记录一条记录
	 * 
	 * @param planTime 计划时间
	 * @param planDept 计划部门
	 * @return
	 */
	public BpJTrainingTypeForm getBpTrainMainList(String planTime,
			String planDept);

	// 汇总
	/**
	 * 培训计划汇总表
	 * 
	 * @param mainId
	 * @return
	 */
	public BpJTrainingGather findByGatherId(Long gatherId);
	
	/**
	 * 修改汇总表
	 * 
	 * @param entity
	 * @return
	 */
	public BpJTrainingGather update(BpJTrainingGather entity);
	
	/**
	 * 汇总
	 * @param planTime 计划时间
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	public PageObject BpPlanGatherList(String planTime, String enterpriseCode,
			int... rowStartIdxAndCount);
	
	/**
	 * 汇总按钮
	 * @param 主表ids
	 */
	public void trainPlanGather(String ids, String planTime,String gatherBy,String enterpriseCode);
	
	/**
	 * 汇总后查询
	 * @param planTime
	 * @return
	 */
	public PageObject trainPlanGatherAfter(String approve,String entryIds,String planTime,String enterpriseCode,int... rowStartIdxAndCount);

	/**
	 * 审批查询
	 * @param planTime
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject trainPlanGatherAproveList(String planTime,String entityIds,String enterpriseCode,int... rowStartIdxAndCount);
	
	/**
	 * 部门培训计划执行回填信息保存
	 * add by drdu 091210
	 * @param updateList
	 */
	@SuppressWarnings("unchecked")
	public void update(List<Map> list,Long mainId,String workerCode,String month) throws ParseException;
	
	/**
	 *  在完成情况回填汇总页面，点查询按钮时先执行此方法
	 * 对培训计划完成情况统计BP_J_TRAINING_SUM表增加记录
	 * add by drdu 20100611
	 * @param month
	 */
	public void saveTrainingSumRecord(String month);
	
	/**
	 * 部门培训计划执行回填列表
	 * add by drdu 091210
	 * @param enterpriseCode
	 * @param planDept
	 * @param planDate
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findTrainPlanBackfillList(String enterpriseCode,String planDept,String planDate,String report_By,String flag,String entryIds, int... rowStartIdxAndCount);

	/**
	 * 取回填状态
	 * @param planTime
	 * @param planDept
	 * @param report_By
	 * @return
	 */
	public BpJTrainingTypeForm getBpTrainMainBackList(String planTime,
			String planDept);
	
	/**
	 * 培训计划执行回填汇总列表
	 * add by drdu 091212
	 * @param enterpriseCode
	 * @param type
	 * @param flag
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findPlanBackGatherList(String enterpriseCode,String month,String deptName,String flag, int... rowStartIdxAndCount);
	
	/**
	 * 批量修改回填汇总记录
	 * add by drdu 091214
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void updateBackGather(List<Map> list,String month,Long trainingMainId);
	
	/**
	 * 取培训回填汇总表中的工作流状态
	 * add by drdu 091215
	 * @param planTime
	 * @return
	 */
	public BpJTrainingSumApproval getBpTrainBackGatherList(String planTime);
	
	/**
	 * add by liuyi 091215
	 * @param month
	 * @param enterpriseCode
	 * @return
	 */
	public String getCompleteDescription(String month);

	/**
	 * add by liuyi 20100427 获取计划汇总的工作流
	 * @param month
	 * @return
	 */
	public String getPlanGatherDesc(String month);
	
	/**
	 * 取汇总审批批记录
	 * month
	 */
	public List<BpTrainPlanApproveForm> getGatherApprovelist(String month);
	
	/**
	 * 回填汇总上报动作前的保存方法
	 * @param month
	 * @param trainingMainId
	 */
	public void addSumApproveRecord(String month,Long trainingMainId);
	
	/**
	 * 执行回填汇总上报动作前的保存方法前获取的值
	 * @param month
	 * @return
	 */
	public BpJTrainingSumForm getTrainingSumIdForAdd(String month);
	
	/**
	 * 根据月份取回填汇总部门
	 * @param month
	 * @return
	 */
	public List findTraingDept(String month);
	
	/**
	 * 培训计划回填汇总页面新加“退回”按钮的方法
	 * add by drdu 20100104
	 * @param month
	 * @param deptCode
	 */
	public void backGatherReturnSelectMethod(String month,String deptCode);
	
	/**
	 * 培训计划汇总修改保存方法
	 * add by drdu 20100105
	 * @param list
	 * @param month
	 * @param trainingDetailId
	 */
	public void updateDeptGather(List<Map> list);
	
	/**
	 * add by liuyi 
	 * 查询部门表中为一级部门的数据
	 */
	PageObject getManageDeptList(String enterpriseCode, int... rowStartIdxAndCount);
	
	/**
	 * 判断一部门在某一月份下是否能审批
	 * add by liuyi 20100429 
	 * @param planDept
	 * @param planTime
	 * @param enterpriseCode
	 * @return
	 */
	public String judgeDeptCanApprove(String planDept, String planTime,
			String enterpriseCode,String flag);
	//	String judgeDeptCanApprove(String planDept,String planTime,String enterpriseCode);
}
