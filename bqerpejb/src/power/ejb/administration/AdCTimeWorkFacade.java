/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;
// default package

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.FixedJobArrangeListBean;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdcTimeWork.
 * @see .AdcTimeWork
  * @author liugonglei
 */
@Stateless

public class AdCTimeWorkFacade  implements AdCTimeWorkFacadeRemote {

    @PersistenceContext private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
		/**
		 * 插入定期工作维护
	 Perform an initial save of a previously unsaved AdcTimeWork entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity AdcTimeWork entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(AdCTimeWork entity) throws SQLException{
    				LogUtil.log("saving AdcTimeWork instance", Level.INFO, null);
	        try {
            entityManager.persist(entity);
            			LogUtil.log("save successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("save failed", Level.SEVERE, re);
	            throw new SQLException();
        }
    }
    
    /**
	 Delete a persistent AdcTimeWork entity.
	  @param entity AdcTimeWork entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(AdCTimeWork entity) throws SQLException{
    				LogUtil.log("deleting AdcTimeWork instance", Level.INFO, null);
	        try {
        	entity = entityManager.getReference(AdCTimeWork.class, entity.getId());
            entityManager.remove(entity);
            			LogUtil.log("delete successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("delete failed", Level.SEVERE, re);
	            throw new SQLException();
        }
    }
    
    /**
     * 更新定期工作维护
	 Persist a previously saved AdcTimeWork entity and return it or a copy of it to the sender. 
	 A copy of the AdcTimeWork entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity AdcTimeWork entity to update
	 @return AdcTimeWork the persisted AdcTimeWork entity instance, may not be the same
     * @throws DataFormatException 
	 @throws RuntimeException if the operation fails
	 */
    public AdCTimeWork update(AdCTimeWork entity,Long lngUpdateTime) throws SQLException, DataFormatException{
    				LogUtil.log("updating AdcTimeWork instance", Level.INFO, null);
	        try {
			AdCTimeWork adcTimework = findById(entity.getId());
			Long lngNewUpdateTime = adcTimework.getUpdateTime().getTime();
			if (!lngNewUpdateTime.equals(lngUpdateTime)) {
				throw new DataFormatException();
			} else {
				entity.setUpdateTime(new Date());
				AdCTimeWork result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			}
		} catch (RuntimeException re) {
        				LogUtil.log("update failed", Level.SEVERE, re);
	            throw new SQLException();
        } catch (DataFormatException e) {
			throw e;
		}
    }
    /**
     * 检索定期工作维护
     */
    public AdCTimeWork findById( Long id) throws SQLException{
    				LogUtil.log("finding AdcTimeWork instance with id: " + id, Level.INFO, null);
	        try {
            AdCTimeWork instance = entityManager.find(AdCTimeWork.class, id);
            return instance;
        } catch (RuntimeException re) {
        				LogUtil.log("find failed", Level.SEVERE, re);
	            throw new SQLException();
        }
    }    
	
	/**
	 * Find all AdcTimeWork entities.
	  	  @return List<AdcTimeWork> all AdcTimeWork entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdCTimeWork> findAll(
		) throws SQLException{
					LogUtil.log("finding all AdcTimeWork instances", Level.INFO, null);
			try {
			final String queryString = "select model from AdcTimeWork model";
								Query query = entityManager.createQuery(queryString);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find all failed", Level.SEVERE, re);
				throw new SQLException();
		}
	}
	

	/**
	 * 从类别编码查询定期工作维护
	 *
	 * @param 
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return AdJRoomPrice 定期工作维护
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByWorkType(String strWorkTypeCode, String strSubWorkTypeCode, 
			String strEnterpriseCode, final int... rowStartIdxAndCount) throws ParseException {
		LogUtil.log("EJB:查询定期工作维护实例开始", Level.INFO, null);

		PageObject pobj = new PageObject();
		String sql;
		int N;
		if (strSubWorkTypeCode != null && !"".equals(strSubWorkTypeCode)) {
			N = 4;
		} else {
			N = 3;
		}
		Object[] params = new Object[N];
		params[0] = "Y";
		params[1] = strWorkTypeCode;
		if (strSubWorkTypeCode != null && !"".equals(strSubWorkTypeCode)) {
			sql = "SELECT WORKITEM_NAME, " +
			"DECODE(WORKRANGE_TYPE, '0', '没有设置', '1', '每日', '2', '隔日', '3','每周', '4', '隔周', '5', '每月', '6', '隔月', '7', '隔N天') , " +
			"TO_CHAR(START_TIME, 'yyyy-mm-dd hh24:mi') AS SDATE, " +
			"DECODE(TO_CHAR(START_TIME, 'D'),'1','星期日','2','星期一','3','星期二'," +
			"'4','星期三','5','星期四','6','星期五','7','星期六') AS WEEK, " +
			"WORK_EXPLAIN, " +
			"DECODE(IF_WEEKEND,'Y','是','N','否') " +
			"FROM AD_C_TIMEWORK " +
			"WHERE IS_USE = ? " +
			"AND WORKTYPE_CODE = ? " +
			"AND SUB_WORKTYPE_CODE =? " +
			"AND ENTERPRISE_CODE = ? " +
			"ORDER BY  SDATE";
			params[2] = strSubWorkTypeCode;
			params[3] = strEnterpriseCode;
		} else {
			sql = "SELECT WORKITEM_NAME, " +
			"DECODE(WORKRANGE_TYPE, '0', '没有设置', '1', '每日', '2', '隔日', '3','每周', '4', '隔周', '5', '每月', '6', '隔月', '7', '隔N天') , " +
			"TO_CHAR(START_TIME, 'yyyy-mm-dd hh24:mi') AS SDATE, " +
			"DECODE(TO_CHAR(START_TIME, 'D'),'1','星期日','2','星期一','3','星期二'," +
			"'4','星期三','5','星期四','6','星期五','7','星期六') AS WEEK, " +
			"WORK_EXPLAIN, " +
			"DECODE(IF_WEEKEND,'Y','是','N','否') " +
			"FROM AD_C_TIMEWORK " +
			"WHERE IS_USE = ? " +
			"AND WORKTYPE_CODE = ? " +
			"AND ENTERPRISE_CODE = ? " +
			"ORDER BY  SDATE";
			params[2] = strEnterpriseCode;
		}
		List<FixedJobArrangeListBean> list = bll.queryByNativeSQL(sql, params);
		List<FixedJobArrangeListBean> lstAdC= new ArrayList<FixedJobArrangeListBean>();
		if (list != null) {
			Iterator it = list.iterator();
			while (it.hasNext()) { 
				Object[] data = (Object[]) it.next();
				FixedJobArrangeListBean fixedJobArrange = new FixedJobArrangeListBean();
				// 工作项目名称
				if (data[0] != null) {
					fixedJobArrange.setWorkitemName(data[0].toString());
				}
				// 周期类别
				if (data[1] != null) {
					fixedJobArrange.setWorkrangeType(data[1].toString());
				}
				// 开始时间
				if (data[2] != null) {
					fixedJobArrange.setStartTime(data[2].toString());
				}
				// 星期
				if (data[3] != null) {
					fixedJobArrange.setStartWeek(data[3].toString());
				}
				// 工作说明
				if (data[4] != null) {
					fixedJobArrange.setWorkExplain(data[4].toString());
				}
				// 节假日是否工作
				if (data[5] != null) {
					fixedJobArrange.setIfWeekend(data[5].toString());
				}
				lstAdC.add(fixedJobArrange);
			}
		}
		if(lstAdC.size()>0)
		{
			// 符合条件的定期工作维护
			pobj.setList(lstAdC);
			// 符合条件的定期工作维护的总数 
			pobj.setTotalCount(Long.parseLong(lstAdC.size() + ""));
		}	
		LogUtil.log("EJB:查询定期工作维护实例结束", Level.INFO, null);
		return pobj;
		
	}
	
}