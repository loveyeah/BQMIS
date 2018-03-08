package power.ejb.resource;

import java.util.List;

import javax.ejb.Remote;

/**
 * 采购订单与需求计划关联处理远程接口
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PurJPlanOrderFacadeRemote {

    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @return entity 增加后记录
     */
	public PurJPlanOrder save(PurJPlanOrder entity);

	/**
	 * 增加一条记录(多次增加时用)
	 *
	 * @param entity 要增加的记录
	 * @param argId 上次增加记录的流水号
	 * @return Long 增加后记录的流水号
	 */
	public Long save(PurJPlanOrder entity, Long argId);
	
	/**
	 * 删除一条记录
	 *
	 * @param entity 采购订单与需求计划关联对象
	 * @throws RuntimeException when the operation fails
	 */
	public void delete(PurJPlanOrder entity);

    /**
     * 修改记录
     *
     * @param entity 要修改的记录
     * @return entity 修改后记录
     */
	public PurJPlanOrder update(PurJPlanOrder entity);

	/**
	 * 通过采购订单与需求计划关联流水号得到采购订单与需求计划关联
	 * 
	 * @param id 采购订单与需求计划关联流水号
     * @return PurJPlanOrder 采购订单与需求计划关联
	 */
	public PurJPlanOrder findById(Long id);

	public List<PurJPlanOrder> findByRequirementDetailId(
			Object requirementDetailId);

	public List<PurJPlanOrder> findByPurNo(Object purNo);

	public List<PurJPlanOrder> findByPurOrderDetailsId(Object purOrderDetailsId);

	/**
	 * 查找所有的采购订单与需求计划关联
	 * 
	 * @return List<PurJPlanOrder> 采购订单与需求计划关联
	 */
	public List<PurJPlanOrder> findAll();
}