/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity HrCContractterm.
 * 
 * @see power.ejb.hr.HrCContractterm
 * @author zhouxu，jincong
 */
@Stateless
public class HrCContracttermFacade implements HrCContracttermFacadeRemote {
    // property constants
    public static final String CONTRACT_TERM = "contractTerm";
    public static final String CONTRACT_DISPLAY_NO = "contractDisplayNo";
    public static final String ENTERPRISE_CODE = "enterpriseCode";
    public static final String IS_USE = "isUse";
    public static final String LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String INSERTBY = "insertby";
    public int FIND_ALL_NOPAGE = -1;

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    public  Long  findcontractTermId(String contractterm)//add by wpzhu 20100613
    {
    	Long empId = null;
			String sql = "select t.contract_term_id   from hr_c_contractterm  t" +
					     " where t.is_use='Y'" +
					     " and t.contract_term='"+contractterm+"'";
			Object obj = bll.getSingal(sql);
			if(obj != null)
				empId = Long.parseLong(obj.toString());
			return empId;	
    }
    /**
     * Perform an initial save of a previously unsaved HrCContractterm entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            HrCContractterm entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrCContractterm entity) throws RuntimeException {
        LogUtil.log("EJB:合同有效期保存错误", Level.INFO, null);
        try {
            // 取得最大的id
            entity.setContractTermId(bll.getMaxId("HR_C_CONTRACTTERM", "CONTRACT_TERM_ID"));
            // 设置最后修改时间
            entity.setLastModifiedDate(new Date());
            entityManager.persist(entity);
            LogUtil.log("EJB:合同有效期保存结束", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("EJB:合同有效期保存错误", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Delete a persistent HrCContractterm entity.
     * 
     * @param entity
     *            HrCContractterm entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrCContractterm entity) {
        LogUtil.log("deleting HrCContractterm instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(HrCContractterm.class, entity.getContractTermId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Persist a previously saved HrCContractterm entity and return it or a copy
     * of it to the sender. A copy of the HrCContractterm entity parameter is
     * returned when the JPA persistence mechanism has not previously been
     * tracking the updated entity.
     * 
     * @param entity
     *            HrCContractterm entity to update
     * @return HrCContractterm the persisted HrCContractterm entity instance,
     *         may not be the same
     * @throws RuntimeException
     *             if the operation fails
     */
    public HrCContractterm update(HrCContractterm entity) throws RuntimeException {
        LogUtil.log("EJB:合同有效期更新开始", Level.INFO, null);
        try {
            // 设置最后修改时间
            entity.setLastModifiedDate(new Date());
            HrCContractterm result = entityManager.merge(entity);
            LogUtil.log("EJB:合同有效期更新结束", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("EJB:合同有效期更新失败", Level.SEVERE, re);
            throw re;
        }
    }

    public HrCContractterm findById(Long id) {
        LogUtil.log("EJB:合同有效期按id查询开始，id: " + id, Level.INFO, null);
        try {
            HrCContractterm instance = entityManager.find(HrCContractterm.class, id);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("EJB:合同有效期按id查询错误", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Find all HrCContractterm entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrCContractterm property to query
     * @param value
     *            the property value to match
     * @return List<HrCContractterm> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrCContractterm> findByProperty(String propertyName, final Object value) {
        LogUtil.log("finding HrCContractterm instance with property: " + propertyName + ", value: " + value,
                Level.INFO, null);
        try {
            final String queryString = "select model from HrCContractterm model where model." + propertyName
                    + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find by property name failed", Level.SEVERE, re);
            throw re;
        }
    }

    public List<HrCContractterm> findByContractTerm(Object contractTerm) {
        return findByProperty(CONTRACT_TERM, contractTerm);
    }

    public List<HrCContractterm> findByContractDisplayNo(Object contractDisplayNo) {
        return findByProperty(CONTRACT_DISPLAY_NO, contractDisplayNo);
    }

    public List<HrCContractterm> findByEnterpriseCode(Object enterpriseCode) {
        return findByProperty(ENTERPRISE_CODE, enterpriseCode);
    }

    public List<HrCContractterm> findByIsUse(Object isUse) {
        return findByProperty(IS_USE, isUse);
    }

    public List<HrCContractterm> findByLastModifiedBy(Object lastModifiedBy) {
        return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
    }

    public List<HrCContractterm> findByInsertby(Object insertby) {
        return findByProperty(INSERTBY, insertby);
    }

    /**
     * 查找所有记录
     * 
     * @param enterpriseCode
     *            企业编码
     * @param rowStartIdxAndCount
     *            动态参数(开始行数和查询行数)
     * @return PageObject 所有记录
     * @throws RuntimeException
     */
    @SuppressWarnings("unchecked")
    public PageObject findAll(String enterpriseCode, final int... rowStartIdxAndCount) throws RuntimeException {
        LogUtil.log("EJB:合同有效期查询开始", Level.INFO, null);
        try {
            PageObject object = new PageObject();
            // 查询sql
            String sql = "SELECT * " 
                + "FROM " 
                + "HR_C_CONTRACTTERM A " 
                + "WHERE " 
                + "A.IS_USE = ? AND "
                + "A.ENTERPRISE_CODE = ?" 
                + "ORDER BY " 
                + "A.CONTRACT_TERM_ID";
            String sqlCount = "SELECT COUNT(A.CONTRACT_TERM_ID) " + "FROM " + "HR_C_CONTRACTTERM A " + "WHERE "
                    + "A.IS_USE = ? AND " + "A.ENTERPRISE_CODE = ? " + "ORDER BY " + "A.contract_display_no";
            List<HrCContractterm> list;
            if (rowStartIdxAndCount[0] == FIND_ALL_NOPAGE && rowStartIdxAndCount[1] == FIND_ALL_NOPAGE) {
                list = bll.queryByNativeSQL(sql, new Object[] { "Y", enterpriseCode }, HrCContractterm.class);
            } else {
                list = bll.queryByNativeSQL(sql, new Object[] { "Y", enterpriseCode }, HrCContractterm.class,
                        rowStartIdxAndCount);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount, new Object[] { "Y", enterpriseCode }).toString());
            if (list == null) {
                list = new ArrayList<HrCContractterm>();
            }
            object.setList(list);
            object.setTotalCount(totalCount);
            LogUtil.log("EJB:合同有效期查询结束", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:合同有效期查询错误", Level.SEVERE, e);
            throw e;
        }
    }
}