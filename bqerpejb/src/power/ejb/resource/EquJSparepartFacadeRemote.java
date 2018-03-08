package power.ejb.resource;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for EquJSparepartFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJSparepartFacadeRemote {
    /**
     * Perform an initial save of a previously unsaved EquJSparepart entity. All
     * subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            EquJSparepart entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(EquJSparepart entity);

    /**
     * Delete a persistent EquJSparepart entity.
     * 
     * @param entity
     *            EquJSparepart entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(EquJSparepart entity);

    /**
     * Persist a previously saved EquJSparepart entity and return it or a copy
     * of it to the sender. A copy of the EquJSparepart entity parameter is
     * returned when the JPA persistence mechanism has not previously been
     * tracking the updated entity.
     * 
     * @param entity
     *            EquJSparepart entity to update
     * @return EquJSparepart the persisted EquJSparepart entity instance, may
     *         not be the same
     * @throws RuntimeException
     *             if the operation fails
     */
    public EquJSparepart update(EquJSparepart entity);

    public EquJSparepart findById(Long id);

    /**
     * Find all EquJSparepart entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the EquJSparepart property to query
     * @param value
     *            the property value to match
     * @return List<EquJSparepart> found by query
     */
    public List<EquJSparepart> findByProperty(String propertyName, Object value);

    public List<EquJSparepart> findByEquclassCode(Object equclassCode);

    public List<EquJSparepart> findByAttributeCode(Object attributeCode);

    public List<EquJSparepart> findByMaterialId(Object materialId);

    public List<EquJSparepart> findByQty(Object qty);

    public List<EquJSparepart> findByLastModifiedBy(Object lastModifiedBy);

    public List<EquJSparepart> findByEnterpriseCode(Object enterpriseCode);

    public List<EquJSparepart> findByIsUse(Object isUse);

    /**
     * Find all EquJSparepart entities.
     * 
     * @return List<EquJSparepart> all EquJSparepart entities
     */
    public List<EquJSparepart> findAll();
    
    public EquJSparepart findByMaterialIdAndAttributeCode(Long materialId,String attributeCode ,String enterpriseCode,String user);
}