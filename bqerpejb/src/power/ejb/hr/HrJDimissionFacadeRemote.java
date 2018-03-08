package power.ejb.hr;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;

/**
 * Remote interface for HrJDimissionFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJDimissionFacadeRemote {
    /**
     * Perform an initial save of a previously unsaved HrJDimission entity. All
     * subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *                HrJDimission entity to persist
     * @throws RuntimeException
     *                 when the operation fails
     */
    public void save(HrJDimission entity);

    /**
     * Delete a persistent HrJDimission entity.
     * 
     * @param entity
     *                HrJDimission entity to delete
     * @throws RuntimeException
     *                 when the operation fails
     */
    public void delete(HrJDimission entity);

    /**
     * Persist a previously saved HrJDimission entity and return it or a copy of
     * it to the sender. A copy of the HrJDimission entity parameter is returned
     * when the JPA persistence mechanism has not previously been tracking the
     * updated entity.
     * 
     * @param entity
     *                HrJDimission entity to update
     * @return HrJDimission the persisted HrJDimission entity instance, may not
     *         be the same
     * @throws RuntimeException
     *                 if the operation fails
     */
    public HrJDimission update(HrJDimission entity);

    /**
     * 更新离职员工登记信息(含排他)
     * 
     * @param entity
     *                实体信息
     * @param lastModifiedDate
     *                画面端的上次修改时间
     * @return
     * @throws DataChangeException
     */
    public HrJDimission update(HrJDimission entity, String lastModifiedDate)
	    throws DataChangeException;

    public HrJDimission findById(Long id);

    /**
     * Find all HrJDimission entities with a specific property value.
     * 
     * @param propertyName
     *                the name of the HrJDimission property to query
     * @param value
     *                the property value to match
     * @return List<HrJDimission> found by query
     */
    public List<HrJDimission> findByProperty(String propertyName, Object value);

    /**
     * Find all HrJDimission entities.
     * 
     * @return List<HrJDimission> all HrJDimission entities
     */
    public List<HrJDimission> findAll();
}