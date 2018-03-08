package power.ejb.resource;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.resource.form.MrpJPlanRequirementDetailInfo;

/**
 * 物料需求计划明细处理远程接口
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface MrpJPlanRequirementDetailFacadeRemote {

    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @return entity 增加后记录
     */
	public MrpJPlanRequirementDetail save(MrpJPlanRequirementDetail entity);

	/**
	 * 删除一条记录
	 *
	 * @param entity 采购订单与需求计划关联对象
	 * @throws RuntimeException when the operation fails
	 */
	public void delete(MrpJPlanRequirementDetail entity);

    /**
     * 修改记录
     *
     * @param entity 要修改的记录
     * @return entity 修改后记录
     */
	public MrpJPlanRequirementDetail update(MrpJPlanRequirementDetail entity);

	public MrpJPlanRequirementDetail findById(Long id,String enterpriseCode);

	/**
	 * Find all MrpJPlanRequirementDetail entities with a specific property
	 * value.
	 * 
	 * @param propertyName
	 *            the name of the MrpJPlanRequirementDetail property to query
	 * @param value
	 *            the property value to match
	 * @return List<MrpJPlanRequirementDetail> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<MrpJPlanRequirementDetail> findByProperty(String enterpriseCode,String propertyName,
			final Object value);
	/**
	 * Find all MrpJPlanRequirementDetail entities.
	 * 
	 * @return List<MrpJPlanRequirementDetail> all MrpJPlanRequirementDetail
	 *         entities
	 */
	public List<MrpJPlanRequirementDetail> findAll();
	
	/**
	 * 查询登录人需求的已入库的物资（用于首页提示信息）
	 * add by fyyang 091029
	 * @param workCode
	 * @param enterpriseCode
	 * @return
	 */
	public List<MrpJPlanRequirementDetailInfo> findMaterialByWorkCode(String workCode,String enterpriseCode);
	
	/**
	 * 查询该汇总对应的需求物资明细
	 * add by fyyang 091105
	 * @param gatherId
	 * @return
	 */
	public List<MrpJPlanRequirementDetailInfo> getMaterialDetailByGatherId(Long gatherId);
	
	/**
	 * 根据领料单号或采购单号查询其对应的需求计划单信息
	 * add  by fyyang 20100318
	 * @param orderNo
	 * @param flag 1--采购单 2--领料单
	 * @return
	 */
	public List<MrpJPlanRequirementDetailInfo> getMaterialDetailByPurOrIssue(String orderNo,String flag);
	
	/**
	 * 查询登录人申请的已退回或作废的单据及物资（勇于首页提示信息）
	 * add by fyyang 20100407
	 * @param workCode
	 * @param enterpriseCode
	 * @return
	 */
	public List<MrpJPlanRequirementDetailInfo> findBackOrCancelInfoByWorkCode(String workCode,String enterpriseCode);
}