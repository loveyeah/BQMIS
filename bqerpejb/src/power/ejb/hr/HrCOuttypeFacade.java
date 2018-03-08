package power.ejb.hr;

import java.util.ArrayList;
import java.util.Iterator;
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
import power.ejb.hr.form.DrpCommBeanInfo;

/**
 * Facade for entity HrCOuttype.
 *
 * @see power.ejb.hr.HrCOuttype
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCOuttypeFacade implements HrCOuttypeFacadeRemote {
    // property constants
    public static final String OUT_TYPE_TYPE = "outTypeType";
    public static final String ENTERPRISE_CODE = "enterpriseCode";
    public static final String IS_USE = "Y";
    public static final String LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String INSERTBY = "insertby";
    public static final String ORDER_BY = "orderBy";

    @PersistenceContext
    private EntityManager entityManager;
    @EJB (beanName="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**
     * Perform an initial save of a previously unsaved HrCOuttype entity. All
     * subsequent persist actions of this entity should use the #update()
     * method.
     *
     * @param entity
     *            HrCOuttype entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrCOuttype entity) {
        LogUtil.log("saving HrCOuttype instance", Level.INFO, null);
        try {
            //采番处理
            entity.setOutTypeId(bll.getMaxId("HR_C_OUTTYPE", "OUT_TYPE_ID"));
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Delete a persistent HrCOuttype entity.
     *
     * @param entity
     *            HrCOuttype entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrCOuttype entity) {
        LogUtil.log("deleting HrCOuttype instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(HrCOuttype.class, entity
                    .getOutTypeId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Persist a previously saved HrCOuttype entity and return it or a copy of
     * it to the sender. A copy of the HrCOuttype entity parameter is returned
     * when the JPA persistence mechanism has not previously been tracking the
     * updated entity.
     *
     * @param entity
     *            HrCOuttype entity to update
     * @return HrCOuttype the persisted HrCOuttype entity instance, may not be
     *         the same
     * @throws RuntimeException
     *             if the operation fails
     */
    public HrCOuttype update(HrCOuttype entity) {
        LogUtil.log("updating HrCOuttype instance", Level.INFO, null);
        try {
            HrCOuttype result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public HrCOuttype findById(Long id) {
        LogUtil.log("finding HrCOuttype instance with id: " + id, Level.INFO,
                null);
        try {
            HrCOuttype instance = entityManager.find(HrCOuttype.class, id);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Find all HrCOuttype entities with a specific property value.
     *
     * @param propertyName
     *            the name of the HrCOuttype property to query
     * @param value
     *            the property value to match
     * @return List<HrCOuttype> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrCOuttype> findByProperty(String propertyName,
            final Object value) {
        LogUtil.log("finding HrCOuttype instance with property: "
                + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from HrCOuttype model where model."
                    + propertyName + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find by property name failed", Level.SEVERE, re);
            throw re;
        }
    }

    public List<HrCOuttype> findByOutTypeType(Object outTypeType) {
        return findByProperty(OUT_TYPE_TYPE, outTypeType);
    }

    public List<HrCOuttype> findByEnterpriseCode(Object enterpriseCode) {
        return findByProperty(ENTERPRISE_CODE, enterpriseCode);
    }

    public List<HrCOuttype> findByIsUse(Object isUse) {
        return findByProperty(IS_USE, isUse);
    }

    public List<HrCOuttype> findByLastModifiedBy(Object lastModifiedBy) {
        return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
    }

    public List<HrCOuttype> findByInsertby(Object insertby) {
        return findByProperty(INSERTBY, insertby);
    }

    public List<HrCOuttype> findByOrderBy(Object orderBy) {
        return findByProperty(ORDER_BY, orderBy);
    }

    /**
     * Find all HrCOuttype entities.
     *
     * @return List<HrCOuttype> all HrCOuttype entities
     */
    @SuppressWarnings("unchecked")
    public List<HrCOuttype> findAll() {
        LogUtil.log("finding all HrCOuttype instances", Level.INFO, null);
        try {
            final String queryString = "select model from HrCOuttype model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 离职类别维护查询
     *
     * @param
     *         isUse,enterprisCode,rowStartIdxAndCount
     * @return
     *         List<HrCOuttype>
     */
    public List<HrCOuttype> getOutTypeList(String isUse , String enterprisCode,int... rowStartIdxAndCount){
        try {
            String sql = "select t.* from  HR_C_OUTTYPE t where t.IS_USE=? "
                +"and t.ENTERPRISE_CODE=? order by t.order_by";
            Object[] params = new Object[2];
            params[0] = isUse;
            params[1]=enterprisCode;
            List<HrCOuttype> list = bll.queryByNativeSQL(sql,params,HrCOuttype.class,rowStartIdxAndCount);
            return list;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 按Id，企业编码，和是否使用字段查询该条记录是否存在
     *
     */
    public Boolean findByIdIsuseCode(Long Id ,String isUse , String enterprisCode){
        try {
            String sql = "select t.* from  HR_C_OUTTYPE t where t.IS_USE=? and t.OUT_TYPE_ID=? "
                +"and t.ENTERPRISE_CODE=?";
            Object[] params = new Object[3];
            params[0] = isUse;
            params[1] = Id;
            params[2]=enterprisCode;
            List<HrCOuttype> list = bll.queryByNativeSQL(sql,params,HrCOuttype.class);
            if (list.size()==0)return false;
            return true;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }
	/**
	 * 查找所有离职类别
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllOuttypes(String enterpriseCode){
		try{
			String sql = "SELECT O.OUT_TYPE_ID,O.OUT_TYPE_TYPE " +
					"  FROM HR_C_OUTTYPE O" +
					"  WHERE O.IS_USE = ?  AND O.ENTERPRISE_CODE = ?";
			LogUtil.log("所有离职类别id和名称开始。", Level.INFO, null);
			List list = bll.queryByNativeSQL(sql,new Object[]{"Y",enterpriseCode});
			List<DrpCommBeanInfo> arraylist = new ArrayList<DrpCommBeanInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DrpCommBeanInfo model = new DrpCommBeanInfo();
				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setText(data[1].toString());
				}
				arraylist.add(model);
			}
			PageObject result = new PageObject();
			result.setList(arraylist);
			LogUtil.log("查找所有离职类别id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有离职类别id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}

}