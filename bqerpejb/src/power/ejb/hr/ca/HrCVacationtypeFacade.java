/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
 * Facade for entity HrCVacationtype.
 * 
 * @see power.ejb.hr.HrCVacationtype
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCVacationtypeFacade implements HrCVacationtypeFacadeRemote {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	/**是否使用*/
	private String IS_USE_Y = "Y";

    /**
     * Perform an initial save of a previously unsaved HrCVacationtype entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            HrCVacationtype entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrCVacationtype entity) {
        LogUtil.log("saving HrCVacationtype instance", Level.INFO, null);
        try {
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Delete a persistent HrCVacationtype entity.
     * 
     * @param entity
     *            HrCVacationtype entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrCVacationtype entity) {
        LogUtil.log("deleting HrCVacationtype instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(HrCVacationtype.class, entity
                    .getVacationTypeId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Persist a previously saved HrCVacationtype entity and return it or a copy
     * of it to the sender. A copy of the HrCVacationtype entity parameter is
     * returned when the JPA persistence mechanism has not previously been
     * tracking the updated entity.
     * 
     * @param entity
     *            HrCVacationtype entity to update
     * @return HrCVacationtype the persisted HrCVacationtype entity instance,
     *         may not be the same
     * @throws RuntimeException
     *             if the operation fails
     */
    public HrCVacationtype update(HrCVacationtype entity) {
        LogUtil.log("updating HrCVacationtype instance", Level.INFO, null);
        try {
            HrCVacationtype result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public HrCVacationtype findById(Long id) {
        LogUtil.log("finding HrCVacationtype instance with id: " + id,
                Level.INFO, null);
        try {
            HrCVacationtype instance = entityManager.find(
                    HrCVacationtype.class, id);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Find all HrCVacationtype entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrCVacationtype property to query
     * @param value
     *            the property value to match
     * @return List<HrCVacationtype> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrCVacationtype> findByProperty(String propertyName,
            final Object value) {
        LogUtil.log("finding HrCVacationtype instance with property: "
                + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from HrCVacationtype model where model."
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
     * Find all HrCVacationtype entities.
     * 
     * @return List<HrCVacationtype> all HrCVacationtype entities
     */
    @SuppressWarnings("unchecked")
    public List<HrCVacationtype> findAll() {
        LogUtil.log("finding all HrCVacationtype instances", Level.INFO, null);
        try {
            final String queryString = "select model from HrCVacationtype model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 查询所有假别维护信息
     * @param enterpriseCode 企业编码
     * @param rowStartIdxAndCount 分页
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
    public PageObject findAllVacation(String enterpriseCode, final int... rowStartIdxAndCount) throws SQLException{
        LogUtil.log("Action:假别维护查询开始。", Level.INFO, null);
        try {
            PageObject result = new PageObject();
            // 查询sql
            StringBuffer sql = new StringBuffer().append("select R.VACATION_TYPE_ID,R.VACATION_TYPE,R.IF_CYCLE,R.IF_WEEKEND,R.VACATION_MARK,R.LAST_MODIFIY_BY," +
            		"R.LAST_MODIFIY_DATE,R.IS_USE,R.ENTERPRISE_CODE \n ").append(
                    " FROM HR_C_VACATIONTYPE R \n").append(
                    "where  R.enterprise_code= ? \n").append(
                    "and R.is_use= ? \n").append("order by R.VACATION_TYPE_ID");
            LogUtil.log("SQL=" + sql.toString(), Level.INFO, null);
            List<HrCVacationtype> list=bll.queryByNativeSQL(sql.toString(),new Object[]{enterpriseCode,IS_USE_Y}, HrCVacationtype.class, rowStartIdxAndCount);
            StringBuffer sqlCount = new StringBuffer().append(
                    "select COUNT(R.VACATION_TYPE_ID) \n").append(
                    " FROM HR_C_VACATIONTYPE R \n").append(
                    "where  R.enterprise_code= ? \n").append(
                    "and R.is_use=? \n").append("order by R.VACATION_TYPE_ID");
            Long totalCount=Long.parseLong(bll.getSingal(sqlCount.toString(),new Object[]{enterpriseCode,IS_USE_Y}).toString());
            result.setList(list);
            result.setTotalCount(totalCount);
            LogUtil.log("Action:假别维护查询结束。", Level.INFO, null);
            return result;

        } catch (RuntimeException re) {
            LogUtil.log("Action:假别维护查询失败。", Level.SEVERE, re);
            throw new SQLException();
        }
    }
    
    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @throws SQLException
     * @return HrCVacationtype  查询结果
     */
    public HrCVacationtype addVacation(HrCVacationtype entity)throws SQLException{
        LogUtil.log("EJB:新增假别维护开始", Level.INFO, null);
        try {
            if (entity.getVacationTypeId() == null) {
                // 设定主键值
                entity.setVacationTypeId(bll.getMaxId("HR_C_VACATIONTYPE",
                        "VACATION_TYPE_ID"));
            }
            // 设定修改时间
            entity.setLastModifiyDate(new java.util.Date());
            // 设定是否使用
            entity.setIsUse(IS_USE_Y);
            // 保存
            entityManager.persist(entity);
            LogUtil.log("EJB:新增假别维护结束", Level.INFO, null);
            return entity;

        } catch (RuntimeException re) {
            LogUtil.log("EJB:新增假别维护失败", Level.SEVERE, re);
            throw new SQLException();
        }
    }
    
    /**
     * 修改一条记录
     *
     * @param entity  要修改的记录
     * @return InvCLocation  修改的记录
     * @throws CodeRepeatException
     */
    public HrCVacationtype updateVacation(HrCVacationtype entity)throws SQLException {
        LogUtil.log("EBJ:修改假别维护开始", Level.INFO, null);
        try {
            // 修改时间
            entity.setLastModifiyDate(new Date());
            HrCVacationtype result = entityManager.merge(entity);
            LogUtil.log("EBJ：修改假别维护结束", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("EBJ：修改假别维护失败", Level.SEVERE, re);
            throw new SQLException();
        }
    }
    
    /**
     * 删除一条信息
     *
     * @param 假别ID
     * @param workerCode 登录者id
     * @throws RuntimeException
     * @return void            
     */
    public void deleteByVacationTypeId(HrCVacationtype entity) throws SQLException{
        LogUtil.log("删除假别维护开始", Level.INFO, null);
        try {
            // 设置最后修改时间
            entity.setLastModifiyDate(new Date());
            this.update(entity);
            LogUtil.log("删除假别维护结束", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("删除假别维护失败", Level.SEVERE, re);
            throw new SQLException();
        }
    }

	/**
	 * 查询请假类别信息
	 * @param strEnterpriseCode 企业代码
	 * @return PageObject 请假类别信息
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findVacationTypeCmb(String strEnterpriseCode) throws SQLException {
		LogUtil.log("EJB:请假类别信息查询正常开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		try {
			// 查询sql
			StringBuffer sbd = new StringBuffer();
			// SELECT文
			sbd.append("SELECT ");
			sbd.append("A.VACATION_TYPE_ID , ");
			sbd.append("A.VACATION_TYPE, ");
			sbd.append("A.IF_WEEKEND ");
			int intLength = sbd.length();

			// FROM文
			sbd.append("FROM HR_C_VACATIONTYPE A ");
			sbd.append("WHERE ");
			sbd.append("A.ENTERPRISE_CODE = ? ");
			sbd.append("AND A.IS_USE = ? ");

			// 查询参数数组
			Object[] params = new Object[2];
			int i =0;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;

			// 记录数
			String sqlCount = "SELECT " +
					" COUNT(A.VACATION_TYPE_ID) " +sbd.substring(intLength, sbd.length());
			List<HrCVacationtype> list = bll.queryByNativeSQL(sbd.toString(), params);
			LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
					.toString());
			LogUtil.log("EJB: SQL =" + sqlCount, Level.INFO, null);
			List<HrCVacationtype> arrList = new ArrayList<HrCVacationtype>();
			if (list !=null) {
				Iterator it = list.iterator();
				while(it.hasNext()) {
					HrCVacationtype hrCVacationtype = new HrCVacationtype();
					Object[] data = (Object[]) it.next();
					// 请假类别id
					if(data[0] != null) {
						hrCVacationtype.setVacationTypeId(Long.parseLong(data[0].toString()));
					}
					// 请假类别
					if (data[1] != null) {
						hrCVacationtype.setVacationType(data[1].toString());
					}
					// 是否包括周末
					if (data[2] != null) {
						hrCVacationtype.setIfWeekend(data[2].toString());
					}
					arrList.add(hrCVacationtype);
				}
			}
			pobj.setList(arrList);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:请假类别信息查询正常结束", Level.INFO, null);
		} catch (RuntimeException e) {
			LogUtil.log("EJB:请假类别信息查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
		return pobj;
	}
}