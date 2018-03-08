package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity HrCStationmovetype.
 * 
 * @see power.ejb.hr.HrCStationmovetype
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCStationmovetypeFacade implements HrCStationmovetypeFacadeRemote {

    @PersistenceContext
    private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 是否使用 */
	private static final String IS_USE_Y = "Y";
	/** 空字符串 */
	public static final String BLANK_STRING = "";

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
    public void save(HrCStationmovetype entity) {
	LogUtil.log("saving HrCStationmovetype instance", Level.INFO, null);
	try {
	    entityManager.persist(entity);
	    LogUtil.log("save successful", Level.INFO, null);
	} catch (RuntimeException re) {
	    LogUtil.log("save failed", Level.SEVERE, re);
	    throw re;
	}
    }

    /**
     * Delete a persistent HrCStationmovetype entity.
     * 
     * @param entity
     *                HrCStationmovetype entity to delete
     * @throws RuntimeException
     *                 when the operation fails
     */
    public void delete(HrCStationmovetype entity) {
	LogUtil.log("deleting HrCStationmovetype instance", Level.INFO, null);
	try {
	    entity = entityManager.getReference(HrCStationmovetype.class,
		    entity.getStationMoveTypeId());
	    entityManager.remove(entity);
	    LogUtil.log("delete successful", Level.INFO, null);
	} catch (RuntimeException re) {
	    LogUtil.log("delete failed", Level.SEVERE, re);
	    throw re;
	}
    }

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
    public HrCStationmovetype update(HrCStationmovetype entity) {
	LogUtil.log("updating HrCStationmovetype instance", Level.INFO, null);
	try {
	    HrCStationmovetype result = entityManager.merge(entity);
	    LogUtil.log("update successful", Level.INFO, null);
	    return result;
	} catch (RuntimeException re) {
	    LogUtil.log("update failed", Level.SEVERE, re);
	    throw re;
	}
    }

    public HrCStationmovetype findById(Long id) {
	LogUtil.log("finding HrCStationmovetype instance with id: " + id,
		Level.INFO, null);
	try {
	    HrCStationmovetype instance = entityManager.find(
		    HrCStationmovetype.class, id);
	    return instance;
	} catch (RuntimeException re) {
	    LogUtil.log("find failed", Level.SEVERE, re);
	    throw re;
	}
    }

    /**
     * Find all HrCStationmovetype entities with a specific property value.
     * 
     * @param propertyName
     *                the name of the HrCStationmovetype property to query
     * @param value
     *                the property value to match
     * @return List<HrCStationmovetype> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrCStationmovetype> findByProperty(String propertyName,
	    final Object value) {
	LogUtil.log("finding HrCStationmovetype instance with property: "
		+ propertyName + ", value: " + value, Level.INFO, null);
	try {
	    final String queryString = "select model from HrCStationmovetype model where model."
		    + propertyName + "= :propertyValue";
	    Query query = entityManager.createQuery(queryString);
	    query.setParameter("propertyValue", value);
	    return query.getResultList();
	} catch (RuntimeException re) {
	    LogUtil.log("find by property name failed", Level.SEVERE, re);
	    throw re;
	}
    }

    /**
     * Find all HrCStationmovetype entities.
     * 
     * @return List<HrCStationmovetype> all HrCStationmovetype entities
     */
    @SuppressWarnings("unchecked")
    public List<HrCStationmovetype> findAll() {
	LogUtil.log("finding all HrCStationmovetype instances", Level.INFO,
		null);
	try {
	    final String queryString = "select model from HrCStationmovetype model";
	    Query query = entityManager.createQuery(queryString);
	    return query.getResultList();
	} catch (RuntimeException re) {
	    LogUtil.log("find all failed", Level.SEVERE, re);
	    throw re;
	}
    }
    
    /**
     * 查询所有的岗位调动类别维护信息
     * @param enterpriseCode 企业编码
     * @return
     * @throws SQLException
     */
	@SuppressWarnings("unchecked")
	public PageObject getStationRemove(String enterpriseCode) throws SQLException {
		LogUtil.log("查询调动类别维护信息开始: ",Level.INFO, null);
        try {
        	PageObject result = new PageObject();
        	Object[] params = new Object[2];
			params[0] = enterpriseCode;
			params[1] = IS_USE_Y;
//			params[2] = '1';
            // 查询sql
            String sql=
                "select  * from HR_C_STATIONMOVETYPE  t\n" +
                "where  t.enterprise_code= ? \n" +
                "and t.is_use= ? \n" 
//                + " and t.STATION_MOVE_TYPE_ID = ? "
                + " order by t.STATION_MOVE_TYPE_ID";
            // 打印sql文
			LogUtil.log("sql 文："+sql, Level.INFO, null);
            // 执行查询
            List<HrCStationmovetype> list = bll.queryByNativeSQL(sql,params, HrCStationmovetype.class);
            // 查询sql
            String sqlCount =
            	"select count(t.STATION_MOVE_TYPE_ID) from HR_C_STATIONMOVETYPE  t\n" +
                "where  t.enterprise_code= ? \n" +
                "and t.is_use= ? \n" 
//                + " and t.STATION_MOVE_TYPE_ID = ? "
                + " order by t.STATION_MOVE_TYPE_ID";;
            // 执行查询
            Long totalCount = Long
				.parseLong(bll.getSingal(sqlCount,params).toString());
            // 设置PageObject
            result.setList(list);
            result.setTotalCount(totalCount);
            // 返回
            LogUtil.log("查询调动类别维护信息结束: ",Level.INFO, null);
            return result;
        }catch(RuntimeException e){
        	LogUtil.log("查询调动类别维护信息失败: ",Level.INFO, e);
			throw new SQLException(e.getMessage());
		}
	}

}