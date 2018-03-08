package power.ejb.resource;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for MrpCPlanOriginalFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface MrpCPlanOriginalFacadeRemote {
    /**
     * Perform an initial save of a previously unsaved MrpCPlanOriginal entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            MrpCPlanOriginal entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(MrpCPlanOriginal entity);

    /**
     * Delete a persistent MrpCPlanOriginal entity.
     * 
     * @param entity
     *            MrpCPlanOriginal entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(MrpCPlanOriginal entity);

    /**
     * Persist a previously saved MrpCPlanOriginal entity and return it or a
     * copy of it to the sender. A copy of the MrpCPlanOriginal entity parameter
     * is returned when the JPA persistence mechanism has not previously been
     * tracking the updated entity.
     * 
     * @param entity
     *            MrpCPlanOriginal entity to update
     * @return MrpCPlanOriginal the persisted MrpCPlanOriginal entity instance,
     *         may not be the same
     * @throws RuntimeException
     *             if the operation fails
     */
    public MrpCPlanOriginal update(MrpCPlanOriginal entity);

    public MrpCPlanOriginal findById(Long id);

    /**
     * Find all MrpCPlanOriginal entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the MrpCPlanOriginal property to query
     * @param value
     *            the property value to match
     * @return List<MrpCPlanOriginal> found by query
     */
    public List<MrpCPlanOriginal> findByProperty(String propertyName, Object value);

    public List<MrpCPlanOriginal> findByParentId(Long parentId,String enterpriseCode);

    public List<MrpCPlanOriginal> findByPlanOriginalDesc(Object planOriginalDesc);

    public List<MrpCPlanOriginal> findByOriginalType(Object originalType);

    public List<MrpCPlanOriginal> findByIsUse(Object isUse);

    public List<MrpCPlanOriginal> findByEnterpriseCode(Object enterpriseCode);

    public List<MrpCPlanOriginal> findByLastModifiedBy(Object lastModifiedBy);

    /**
     * Find all MrpCPlanOriginal entities.
     * 
     * @return List<MrpCPlanOriginal> all MrpCPlanOriginal entities
     */
    public List<MrpCPlanOriginal> findAll();
}