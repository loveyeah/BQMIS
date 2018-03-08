package power.ejb.resource;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.resource.form.MRPArriveCangoDetailInfo;

/**
 * Remote interface for PurJArrivalDetailsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PurJArrivalDetailsFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PurJArrivalDetails
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            PurJArrivalDetails entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PurJArrivalDetails entity);

	/**
	 * Delete a persistent PurJArrivalDetails entity.
	 * 
	 * @param entity
	 *            PurJArrivalDetails entity to delete
	 * @throws CodeRepeatException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PurJArrivalDetails entity) throws CodeRepeatException;

	/**
	 * 删除一条记录.
	 * 
	 * @param ardid 流水号
	 * @throws CodeRepeatException
	 */
	public void deleteMuti(Long ardid) throws CodeRepeatException;

	/**
	 * 通过采购单编号得到采购单明细
	 *
	 * @param purNo 采购单编号
	 * @param argSupplier 供应商编号
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 采购单明细
	 */
	@SuppressWarnings("unchecked")
	public double findByMifNo(String mifNo, final int... rowStartIdxAndCount) ;

	/**
	 * 查询到货登记明细bean
	 * 
	 * @param mifNo 到货单号
	 * 
	 * @throws CodeRepeatException
	 */
	public PageObject findByArrivalNo(String mifNo, String enterpriseCode) throws CodeRepeatException;

	/**
	 * Persist a previously saved PurJArrivalDetails entity and return it or a
	 * copy of it to the sender. A copy of the PurJArrivalDetails entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            PurJArrivalDetails entity to update
	 * @return PurJArrivalDetails the persisted PurJArrivalDetails entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PurJArrivalDetails update(PurJArrivalDetails entity);

	public PurJArrivalDetails findById(Long id);

	/**
	 * Find all PurJArrivalDetails entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PurJArrivalDetails property to query
	 * @param value
	 *            the property value to match
	 * @return List<PurJArrivalDetails> found by query
	 */
	public List<PurJArrivalDetails> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all PurJArrivalDetails entities.
	 * 
	 * @return List<PurJArrivalDetails> all PurJArrivalDetails entities
	 */
	public List<PurJArrivalDetails> findAll();

	/**
	 * 单据编号自动采番
	 *
	 * @param purNo 采购单编号
	 * @param argSupplier 供应商编号
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 采购单明细
	 */
	public String  findBy(final int... rowStartIdxAndCount);
	/**
	 * 到货单信息查询
	 *
	 * @param mifNo 到货单号
	 * @param enterpriseCode 企业编码
     * @return ReceiveGoodsBean 到货单明细
	 */
	public ReceiveGoodsBean findForReceiveGoods(String mifNo,String enterpriseCode);
	
	/**
     * 根据需求计划明细ID查询领料单信息
     * @param requirementDetailId
     * @return List<MRPArriveCangoDetailInfo>
     * add By ywliu 09/05/05
     */
	public List<MRPArriveCangoDetailInfo> findForReceiveGoodsByDetailID(Long requirementDetailId);
}