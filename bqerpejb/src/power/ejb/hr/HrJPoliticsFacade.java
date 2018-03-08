/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.ejb.hr;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.DataChangeException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 政治面貌登记 远程处理对象
 * 
 * @see power.ejb.hr.HrJPolitics
 * @author wangjunjie
 */
@Stateless
public class HrJPoliticsFacade implements HrJPoliticsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB (beanName="HrJWorkresumeFacade")
	protected HrJWorkresumeFacadeRemote remote;

	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
	private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";
	/** 是否最新 */
	private static final String IF_NEW_MARK = "Y";
	/**
	 * 新增政治面貌
	 * 
	 * @param entity
	 *            政治面貌
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJPolitics entity) throws SQLException {
		LogUtil.log("EJB:新增政治面貌开始", Level.INFO, null);
		try {
			if (entity.getPoliticsid()== null) {
				entity.setPoliticsid(bll.getMaxId("HR_J_POLITICS",
						"POLITICSID"));
			}
			entityManager.persist(entity);
			LogUtil.log("EJB:新增政治面貌正常结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:新增政治面貌异常结束", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent HrJPolitics entity.
	 * 
	 * @param entity
	 *            HrJPolitics entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJPolitics entity) {
		LogUtil.log("deleting HrJPolitics instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJPolitics.class, entity
					.getPoliticsid());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 修改政治面貌
	 * 
	 * @param entity
	 *            政治面貌
	 * @return HrJPolitics 修改后的政治面貌
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJPolitics update(HrJPolitics entity)
			throws SQLException, DataChangeException {
		LogUtil.log("EJB:修改政治面貌开始", Level.INFO, null);

		// 得到数据库中的这个记录
		HrJPolitics politics = findById(entity.getPoliticsid());

		// 排他
		if (!formatDate(politics.getLastModifiedDate(),
				DATE_FORMAT_YYYYMMDD_TIME_SEC).equals(
				formatDate(entity.getLastModifiedDate(),
						DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
			throw new DataChangeException("排它处理");
		}

		try {
			// 设置修改日期
			entity.setLastModifiedDate(new Date());
			HrJPolitics result = entityManager.merge(entity);
			LogUtil.log("EJB:修改政治面貌正常结束", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:修改政治面貌异常结束", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	public HrJPolitics findById(Long id) {
		LogUtil.log("finding HrJPolitics instance with id: " + id,
				Level.INFO, null);
		try {
			HrJPolitics instance = entityManager.find(
					HrJPolitics.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJPolitics entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJPolitics property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJPolitics> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJPolitics> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJPolitics instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJPolitics model where model."
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
	 * Find all HrJPolitics entities.
	 * 
	 * @return List<HrJPolitics> all HrJPolitics entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJPolitics> findAll() {
		LogUtil.log("finding all HrJPolitics instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJPolitics model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据员工Id, 获得其政治面貌基本信息
	 * 
	 * @param argEmpId
	 *            员工Id
	 * @param argEnterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            动态参数(开始行数和查询行数)
	 * @return 政治面貌基本信息
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	@SuppressWarnings("unchecked")
	public PageObject getPoliticsInfo(Long argEmpId, String argEnterpriseCode, final int ...rowStartIdxAndCount) {
		LogUtil.log("EJB:根据员工Id, 获得其政治面貌基本信息开始", Level.INFO, null);
		
		try {
			StringBuilder sbd = new StringBuilder();
			sbd.append("	SELECT					");										
			sbd.append("			A.JOIN_DATE,	");				
			sbd.append("			A.BELONG_UNIT,	");				
			sbd.append("			A.EXIT_DATE, ");					
			sbd.append("			A.INTRODUCER,	 ");				
			sbd.append("			A.JOIN_UNIT,	");				
			sbd.append("			A.INTRODUCER_UNIT, ");					
			sbd.append("			A.JOIN_PLACE,	 ");				
			sbd.append("			A.MEMO,			");		
			sbd.append("			A.POLITICS_ID,	");				
			sbd.append("			B.POLITICS_NAME,	");				
			sbd.append("			A.IF_NEW_MARK AS IS_NEW_MARK,	");				
			sbd.append("			to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS LAST_MODIFIED_DATE,	");				
			sbd.append("			A.POLITICSID		");	
			sbd.append("    FROM   						");										
			sbd.append("			HR_J_POLITICS A	");		
			sbd.append("	left join	HR_C_POLITICS	B		");							
			sbd.append("						on A.POLITICS_ID = B.POLITICS_ID AND B.IS_USE = ? AND B.ENTERPRISE_CODE = ? ");						
			sbd.append("WHERE A.IS_USE = ? ");
			sbd.append("AND A.EMP_ID = ? ");
			sbd.append("AND A.ENTERPRISE_CODE = ? ");
			sbd.append("ORDER BY A.POLITICSID ");
			
			Object[] params = new Object[] {"Y", argEnterpriseCode, "Y", argEmpId, argEnterpriseCode};											
			
			// 查询一条有参数sql语句
			List<HrJPoliticsBean> lstResult = remote.queryDescribeByNativeSQL(sbd.toString(),
					params, HrJPoliticsBean.class, rowStartIdxAndCount);
			String sqlCount = sbd.toString().replaceFirst("SELECT.*? FROM ", "SELECT COUNT(DISTINCT A.POLITICSID) FROM ");
			// 查询符合条件的政治面貌总数
			Object objCount = bll.getSingal(sqlCount, params);
			Long totalCount = 0L;
			if (objCount != null) {
				totalCount = Long.parseLong(objCount.toString());
			}
	        
			PageObject result = new PageObject();
			// 符合条件的政治面貌信息
			result.setList(lstResult);
			// 符合条件的政治面貌信息的总数
			result.setTotalCount(totalCount);

			LogUtil.log("EJB:根据员工Id, 获得其政治面貌基本信息正常结束", Level.INFO, null);
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:根据员工Id, 获得其政治面貌基本信息异常结束", Level.SEVERE, e);
			throw e;
		}
	}
	
	/**
	 * 根据员工Id, 获得其当前政治面貌基本信息
	 * 
	 * @param argEmpId
	 *            员工Id
	 * @param argEnterpriseCode
	 *            企业编码	
	 * @return HrJPoliticsBean
	 * 			  当前政治面貌基本信息 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	@SuppressWarnings("unchecked")
	public HrJPoliticsBean getNowPoliticsInfo(Long argEmpId, String argEnterpriseCode) {
		LogUtil.log("EJB:根据员工Id, 获得其当前政治面貌基本信息开始", Level.INFO, null);
		
		try {
			StringBuilder sbd = new StringBuilder();
			sbd.append("	select					");										
			sbd.append("			A.JOIN_DATE,	");				
			sbd.append("			A.BELONG_UNIT,	");				
			sbd.append("			A.EXIT_DATE, ");					
			sbd.append("			A.INTRODUCER,	 ");				
			sbd.append("			A.JOIN_UNIT,	");				
			sbd.append("			A.INTRODUCER_UNIT, ");					
			sbd.append("			A.JOIN_PLACE,	 ");				
			sbd.append("			A.MEMO,			");		
			sbd.append("			B.POLITICS_NAME,	");				
			sbd.append("			A.IF_NEW_MARK,	");				
			sbd.append("			A.POLITICSID		");	
			sbd.append("	from							");										
			sbd.append("			HR_J_POLITICS A	");		
			sbd.append("	left join	HR_C_POLITICS	B		");							
			sbd.append("						on A.POLITICS_ID = B.POLITICS_ID	");						
			sbd.append("WHERE A.IS_USE = 'Y' ");
			sbd.append("AND A.IF_NEW_MARK = ? ");
			sbd.append("AND A.EMP_ID = ? ");
			sbd.append("AND A.ENTERPRISE_CODE = ? ");
			
			// 打印SQL语句
			LogUtil.log("EJB:sql=" + sbd.toString(), Level.INFO, null);
			Object[] params = new Object[] {IF_NEW_MARK,argEmpId, argEnterpriseCode};											
			
			// 查询一条有参数sql语句
			List<HrJPoliticsBean> lstResult = remote.queryDescribeByNativeSQL(sbd.toString(),
					params, HrJPoliticsBean.class);
			HrJPoliticsBean politicsInfo=null;
			if(lstResult!=null&&lstResult.size()>0)
			{
				politicsInfo=lstResult.get(0);
			}
			LogUtil.log("EJB:根据员工Id, 获得其当前政治面貌基本信息正常结束", Level.INFO, null);
			return politicsInfo;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:根据员工Id, 获得其当前政治面貌基本信息异常结束", Level.SEVERE, e);
			throw e;
		}
	}

    /**
     * 根据日期和形式返回日期字符串
     * @param argDate 日期
     * @param argFormat 日期形式字符串
     * @return 日期字符串
     */
    private String formatDate(Date argDate, String argFormat) {
        if (argDate == null) {
            return "";
        }
        
        // 日期形式
        SimpleDateFormat sdfFrom = null;
        // 返回字符串
        String strResult = null;

        try {
            sdfFrom = new SimpleDateFormat(argFormat);
            // 格式化日期
            strResult = sdfFrom.format(argDate).toString();
        } catch (Exception e) {
            strResult = "";
        } finally {
            sdfFrom = null;
        }

        return strResult;
    }
    
    public Long findPoliticsIdByName(String name, String enterpriseCode) {
		Long politicsId = null;
		String sql = 
			"select a.politics_id\n" +
			"  from hr_c_politics a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.politics_name = '"+name+"'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			politicsId = Long.parseLong(obj.toString());

		return politicsId;
	}
}