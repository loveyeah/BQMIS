package power.ejb.equ.workbill;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquJOtmaFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJOtmaFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquJOtma entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJOtma entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquJOtma entity);

	/**
	 * Delete a persistent EquJOtma entity.
	 * 
	 * @param entity
	 *            EquJOtma entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquJOtma entity);

	/**
	 * Persist a previously saved EquJOtma entity and return it or a copy of it
	 * to the sender. A copy of the EquJOtma entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            EquJOtma entity to update
	 * @return EquJOtma the persisted EquJOtma entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJOtma update(EquJOtma entity);

	public EquJOtma findById(Long id);
	public void delMateriel(String matCodes,String enterprisecode);
	
/**
 * 根据工单号查询查物资编码
 * @param woCode
 * @return List<EquJOtma>
 */
	public List<EquJOtma> findBywoCode(String woCode);
	
	public void deleteMulti(String woCode,String matCodes,String enterprisecode);
	
	/**
	 * 根据工单号，查询物料列表
	 * @param woCode 工单号
	 * @return List<Materiel> 
	 */
	
	public  PageObject relateMaterielList(String woCode);
	/**
	 * 根据工单号，企业编码删除工单关联领料单关系
	 * @param woCode
	 * @param enterprisecode
	 */
	public void deleteWorkbillMaterialRelation(String woCode,String enterprisecode);
}