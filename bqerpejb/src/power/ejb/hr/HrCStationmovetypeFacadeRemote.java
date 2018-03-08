package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCStationmovetypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCStationmovetypeFacadeRemote {
    /**
     * Perform an initial save of a previously unsaved HrCStationmovetype
     * entity. All subsequent persist actions of this entity should use the
     * #update() method.
     * 
     * @param entity
     *                HrCStationmovetype entity to persist
     * @throws RuntimeException
     *                 when the operation fails
     */
    public void save(HrCStationmovetype entity);

    /**
     * Delete a persistent HrCStationmovetype entity.
     * 
     * @param entity
     *                HrCStationmovetype entity to delete
     * @throws RuntimeException
     *                 when the operation fails
     */
    public void delete(HrCStationmovetype entity);

    /**
     * Persist a previously saved HrCStationmovetype entity and return it or a
     * copy of it to the sender. A copy of the HrCStationmovetype entity
     * parameter is returned when the JPA persistence mechanism has not
     * previously been tracking the updated entity.
     * 
     * @param entity
     *                HrCStationmovetype entity to update
     * @return HrCStationmovetype the persisted HrCStationmovetype entity
     *         instance, may not be the same
     * @throws RuntimeException
     *                 if the operation fails
     */
    public HrCStationmovetype update(HrCStationmovetype entity);

    public HrCStationmovetype findById(Long id);

    /**
     * Find all HrCStationmovetype entities with a specific property value.
     * 
     * @param propertyName
     *                the name of the HrCStationmovetype property to query
     * @param value
     *                the property value to match
     * @return List<HrCStationmovetype> found by query
     */
    public List<HrCStationmovetype> findByProperty(String propertyName,
	    Object value);

    /**
     * Find all HrCStationmovetype entities.
     * 
     * @return List<HrCStationmovetype> all HrCStationmovetype entities
     */
    public List<HrCStationmovetype> findAll();
    /**
     * 查询所有的岗位调动类别维护信息
     * @param enterpriseCode 企业编码
     * @return
     * @throws SQLException
     */
	@SuppressWarnings("unchecked")
	public PageObject getStationRemove(String enterpriseCode) throws SQLException;
}