package power.ejb.equ.workbill;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for EquJWoerFacade.
 * 
 * @author slTang
 */
@Remote
public interface EquJWoerFacadeRemote {
	/**
	 * 增加
	 * 
	 * @param entity
	 *            EquJWoer entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquJWoer entity);

	/**
	 * 删除
	 * 
	 * @param entity
	 *            EquJWoer entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquJWoer entity);

	/**
	 * 更新
	 * 
	 * @param entity
	 *            EquJWoer entity to update
	 * @return EquJWoer the persisted EquJWoer entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJWoer update(EquJWoer entity);

	public EquJWoer findById(Long id);

	/**
	 * 根据工单编号查找
	 * 
	 * @param woCode
	 * @param enterprisecode
	 */
	public List<EquJWoer> findByWoCode(String woCode, String enterprisecode);
}