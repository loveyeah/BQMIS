package power.ejb.resource;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for InvJLotFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvJLotFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved InvJLot entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvJLot entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvJLot entity);

	/**
	 * Delete a persistent InvJLot entity.
	 * 
	 * @param entity
	 *            InvJLot entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJLot entity);

	/**
	 * Persist a previously saved InvJLot entity and return it or a copy of it
	 * to the sender. A copy of the InvJLot entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            InvJLot entity to update
	 * @return InvJLot the persisted InvJLot entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJLot update(InvJLot entity);

	public InvJLot findById(Long id);

	/**
	 * Find all InvJLot entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJLot property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJLot> found by query
	 */
	public List<InvJLot> findByProperty(String propertyName, Object value);

	public List<InvJLot> findByMaterialId(Object materialId);

	public List<InvJLot> findByLotNo(Object lotNo);

	public List<InvJLot> findByWhsNo(Object whsNo);

	public List<InvJLot> findByLocationNo(Object locationNo);

	public List<InvJLot> findByMonthAmount(Object monthAmount);

	public List<InvJLot> findByMonthCost(Object monthCost);

	public List<InvJLot> findByYearAmount(Object yearAmount);

	public List<InvJLot> findByYearCost(Object yearCost);

	public List<InvJLot> findByOpenBalance(Object openBalance);

	public List<InvJLot> findByReceipt(Object receipt);

	public List<InvJLot> findByAdjust(Object adjust);

	public List<InvJLot> findByIssue(Object issue);

	public List<InvJLot> findByReserved(Object reserved);

	public List<InvJLot> findByLastModifiedBy(Object lastModifiedBy);

	public List<InvJLot> findByEnterpriseCode(Object enterpriseCode);

	public List<InvJLot> findByIsUse(Object isUse);

	/**
	 * Find all InvJLot entities.
	 * 
	 * @return List<InvJLot> all InvJLot entities
	 */
	public List<InvJLot> findAll();
	
	/**
	 * 由批号，仓库编码，库位编码和物料编码查询库位物料记录
	 * @param enterpriseCode 企业编码
	 * @param lotNo 批号
	 * @param whsNo 仓库编码
	 * @param locationNo 库位编码
	 * @param materialId 物料流水号
	 * @return
	 */
	public List<InvJLot> findByLWHLM(String enterpriseCode, String lotNo, String whsNo, String locationNo, Long materialId);
	
	/**
	 * 更新批号记录相关信息
	 * @param entityInvJTransactionHis 新增事务历史表记录
	 * @param entityInInvJWarehouse 调入仓库的库存物料记录
	 * @param entityInOutInvJWarehouse  调出仓库的库存物料记录
	 * @param entityInInvJLocatione 调入仓库的库位物料记录
	 * @param entityOutInvJLocatione 调出库位的库位物料记录
	 * @param entityInInvJLot 调入仓库的批号记录表的记录
	 * @param entityOutInvJLot 调出仓库的批号记录表的记录
	 * @return
	 */
	public void updateInvLotRelatedInfor(InvJTransactionHis entityInvJTransactionHis,
			InvJWarehouse entityInInvJWarehouse, InvJWarehouse entityInOutInvJWarehouse, 
			InvJLocation entityInInvJLocatione, InvJLocation entityOutInvJLocatione, 
			InvJLot entityInInvJLot,InvJLot entityOutInvJLot);
}