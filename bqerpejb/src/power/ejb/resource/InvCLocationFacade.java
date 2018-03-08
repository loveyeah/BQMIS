package power.ejb.resource;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity InvCLocation.
 *
 * @see power.ejb.resource.InvCLocation
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvCLocationFacade implements InvCLocationFacadeRemote {
	// property constants
	public static final String WHS_NO = "whsNo";
	public static final String LOCATION_NO = "locationNo";
	public static final String LOCATION_NAME = "locationName";
	public static final String LOCATION_DESC = "locationDesc";
	public static final String LOCATION_ZONE = "locationZone";
	public static final String IS_ALLOCATABLE_LOCATIONS = "isAllocatableLocations";
	public static final String IS_DEFAULT = "isDefault";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @throws CodeRepeatException
     */
    public InvCLocation save(InvCLocation entity) throws CodeRepeatException {
        LogUtil.log("saving InvCLocation instance", Level.INFO, null);
        try {

            // 检查库位编号和库位名称是否唯一
            if(checkLocationNameForAdd(entity.getWhsNo(), entity.getLocationName(),"", entity.getEnterpriseCode())){
                throw new CodeRepeatException("库位名称" + "已存在。请重新输入。");
            }
            if(checkLocationNoForAdd(entity.getWhsNo(), entity.getLocationNo(), "",entity.getEnterpriseCode())){
                throw new CodeRepeatException("库位号" + "已存在。请重新输入。");
            }
            if (entity.getLocationId() == null) {
                // 设定主键值
                entity.setLocationId(bll.getMaxId("INV_C_LOCATION",
                        "LOCATION_ID"));
            }
            // 设定修改时间
            entity.setLastModifiedDate(new java.util.Date());
            // 设定是否使用
            entity.setIsUse("Y");
            // 保存
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
            return entity;

        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }

    }

	/**
	 * Delete a persistent InvCLocation entity.
	 *
	 * @param entity
	 *            InvCLocation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvCLocation entity) {
		LogUtil.log("deleting InvCLocation instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(InvCLocation.class, entity
					.getLocationId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvCLocation entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the InvCLocation property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCLocation> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvCLocation> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding InvCLocation instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvCLocation model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<InvCLocation> findByWhsNo(Object whsNo) {
		return findByProperty(WHS_NO, whsNo);
	}

	public List<InvCLocation> findByLocationNo(Object locationNo) {
		return findByProperty(LOCATION_NO, locationNo);
	}

	public List<InvCLocation> findByLocationName(Object locationName) {
		return findByProperty(LOCATION_NAME, locationName);
	}

	public List<InvCLocation> findByLocationDesc(Object locationDesc) {
		return findByProperty(LOCATION_DESC, locationDesc);
	}

	public List<InvCLocation> findByLocationZone(Object locationZone) {
		return findByProperty(LOCATION_ZONE, locationZone);
	}

	public List<InvCLocation> findByIsAllocatableLocations(
			Object isAllocatableLocations) {
		return findByProperty(IS_ALLOCATABLE_LOCATIONS, isAllocatableLocations);
	}

	public List<InvCLocation> findByIsDefault(Object isDefault) {
		return findByProperty(IS_DEFAULT, isDefault);
	}

	public List<InvCLocation> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<InvCLocation> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<InvCLocation> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

/**
     * 删除一条记录
     *
     * @param locationId 库位流水号
     * @throws CodeRepeatException
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(Long locationId) throws CodeRepeatException {
        LogUtil.log("deleting InvCLocation instance", Level.INFO, null);
        try {
            InvCLocation entity=this.findById(locationId);
             if(entity!=null)
             {
                 // is_use设为N
                 entity.setIsUse("N");
                 this.update(entity);
                 LogUtil.log("delete successful", Level.INFO, null);
             }
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 删除对应一个仓库的所有记录
     *
     * @param whsNo 库位流水号
     * @param workerCode 登录者id
     * @throws CodeRepeatException
     * @throws RuntimeException
     *             when the operation fails
     */
    public void deleteByWhsNo(String whsNo, String workerCode) throws CodeRepeatException {
        LogUtil.log("deleting InvCLocation instance", Level.INFO, null);
        try {
            String sql=
                "update INV_C_LOCATION t\n" +
                "set t.is_use='N'\n" +
                ", t.last_modified_by='" + workerCode + "'\n" +
                ", t.last_modified_date= sysdate\n" +
                "where t.WHS_NO= '" + whsNo + "' and t.is_use='Y'";
            bll.exeNativeSQL(sql);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 修改一条记录
     *
     * @param entity  要修改的记录
     * @return InvCLocation  修改的记录
     * @throws CodeRepeatException
     */
    public InvCLocation update(InvCLocation entity) throws CodeRepeatException {
        LogUtil.log("updating InvCLocation instance", Level.INFO, null);
        try {

            // 检查库位编号和库位名称是否唯一
            if (checkLocationNameForAdd(entity.getWhsNo(), entity
                    .getLocationName(), String.valueOf(entity.getLocationId()), entity.getEnterpriseCode())) {
                throw new CodeRepeatException("库位名称"+ "已存在。请重新输入。");
            }
            if (checkLocationNoForAdd(entity.getWhsNo(),
                    entity.getLocationNo(), String.valueOf(entity.getLocationId()),  entity.getEnterpriseCode())) {
                throw new CodeRepeatException("库位号" +  "已存在。请重新输入。");
            }
            // 修改时间
            entity.setLastModifiedDate(new Date());
            InvCLocation result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("update failed", Level.SEVERE, re);
            throw new CodeRepeatException("修改失败");
        }
    }
    /**
     * 根据主键查找记录
     * @param locationId 库位流水号
     */
    public InvCLocation findById(Long locationId) {
        LogUtil.log("finding InvCLocation instance with id: " + locationId, Level.INFO,    null);
        try {
            InvCLocation instance = entityManager.find(InvCLocation.class, locationId);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 查询
     * @param enterpriseCode 企业编码
     * @param whsNo 仓库编号
     * @param rowStartIdxAndCount 分页
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
    public PageObject findAll(String enterpriseCode, String whsNo, final int... rowStartIdxAndCount) {
        LogUtil.log("finding all InvCLocation instances", Level.INFO, null);
        try {
            PageObject result = new PageObject();
            // 查询sql
            String sql=
                "select * from inv_c_location t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and t.whs_no='" + whsNo + "'\n" +
                "and t.is_use='Y' \n"+
                "order by t.location_id" ;
            List<InvCLocation> list=bll.queryByNativeSQL(sql, InvCLocation.class, rowStartIdxAndCount);
            String sqlCount=
                "select count(t.location_id) from inv_c_location t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and t.whs_no='" + whsNo + "'\n" +
                "and t.is_use='Y' \n"+
                "order by t.location_id" ;
            Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
            result.setList(list);
            result.setTotalCount(totalCount);
            return result;

        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 检查库位名称是否是唯一的
     * @param whsNo 仓库号
     * @param locationName 库位名称
     * @param enterpriseCode 企业编码
     * @return false 如果库位名称是唯一的
     */
    private boolean checkLocationNameForAdd(String whsNo, String locationName, String locationId,
            String enterpriseCode) {

        boolean isSame = false;
        String sql = "select count(t.location_id) from inv_c_location t\n"
                + "where t.location_name='" + locationName + "'\n"
                + "and t.enterprise_code='" + enterpriseCode + "'\n"
                + "and t.whs_no='" +whsNo + "'\n"
                + "and t.is_use='Y'";
        if(!"".equals(locationId)){
            sql += "\n and t.location_Id <> " + locationId ;
        }
        if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
            isSame = true;
        }
        return isSame;
    }
    /**
     * 检查库位编号是否是唯一的
     * @param whsNo 仓库号
     * @param locationId 仓库流水号
     * @param locationNo 库位编号
     * @param enterpriseCode 企业编码
     * @return false 如果库位编号是唯一的
     */
    private boolean checkLocationNoForAdd(String whsNo, String locationNo, String locationId,
            String enterpriseCode) {

        boolean isSame = false;
        String sql = "select count(t.location_id) from inv_c_location t\n"
                + "where t.location_no='" + locationNo + "'\n"
                + "and t.enterprise_code='" + enterpriseCode + "'\n"
                + "and t.whs_no='" +whsNo + "'\n"
                + "and t.is_use='Y'";
        if(!"".equals(locationId)){
            sql += "\n and t.location_Id <> " + locationId ;
        }
        if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
            isSame = true;
        }
        return isSame;
    }
	/**
	 * Find all InvCLocation entities.
	 *
	 * @return List<InvCLocation> all InvCLocation entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvCLocation> findAll() {
		LogUtil.log("finding all InvCLocation instances", Level.INFO, null);
		try {
			final String queryString = "select model from InvCLocation model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}