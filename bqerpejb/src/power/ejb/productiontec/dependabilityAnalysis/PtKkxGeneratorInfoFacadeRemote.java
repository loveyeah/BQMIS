package power.ejb.productiontec.dependabilityAnalysis;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for PtKkxGeneratorInfoFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtKkxGeneratorInfoFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtKkxGeneratorInfo
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            PtKkxGeneratorInfo entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PtKkxGeneratorInfo save(PtKkxGeneratorInfo entity);

	/**
	 * Delete a persistent PtKkxGeneratorInfo entity.
	 * 
	 * @param entity
	 *            PtKkxGeneratorInfo entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtKkxGeneratorInfo entity);

	/**
	 * Persist a previously saved PtKkxGeneratorInfo entity and return it or a
	 * copy of it to the sender. A copy of the PtKkxGeneratorInfo entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            PtKkxGeneratorInfo entity to update
	 * @return PtKkxGeneratorInfo the persisted PtKkxGeneratorInfo entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtKkxGeneratorInfo update(PtKkxGeneratorInfo entity);

	public PtKkxGeneratorInfo findById(Long id);

	/**
	 * Find all PtKkxGeneratorInfo entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtKkxGeneratorInfo property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtKkxGeneratorInfo> found by query
	 */
	public List<PtKkxGeneratorInfo> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all PtKkxGeneratorInfo entities.
	 * 
	 * @return List<PtKkxGeneratorInfo> all PtKkxGeneratorInfo entities
	 */
//	public List<PtKkxGeneratorInfoForm> findInfoByBlockId(String blockId);
	public List<PtKkxGeneratorInfo> findInfoByBlockId(String blockId);
}