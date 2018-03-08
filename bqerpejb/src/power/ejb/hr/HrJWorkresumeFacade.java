/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.ejb.hr;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * 工作简历登记 远程处理对象
 * 
 * @see power.ejb.hr.HrJWorkresume
 * @author huyou
 */
@Stateless
public class HrJWorkresumeFacade implements HrJWorkresumeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
    private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";
    
	/**
	 * 新增工作简历
	 * 
	 * @param entity 工作简历
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJWorkresume entity) throws SQLException {
		LogUtil.log("EJB:新增工作简历开始", Level.INFO, null);
		try {
			if (entity.getWorkresumeid() == null) {
				entity.setWorkresumeid(bll.getMaxId("HR_J_WORKRESUME", "WORKRESUMEID"));
			}
			entityManager.persist(entity);
			LogUtil.log("EJB:新增工作简历正常结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:新增工作简历异常结束", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent HrJWorkresume entity.
	 * 
	 * @param entity
	 *            HrJWorkresume entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJWorkresume entity) {
		LogUtil.log("deleting HrJWorkresume instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJWorkresume.class, entity
					.getWorkresumeid());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 修改工作简历
	 * 
	 * @param entity 工作简历
	 * @return HrJWorkresume 修改后的工作简历
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJWorkresume update(HrJWorkresume entity) throws SQLException, DataChangeException {
		LogUtil.log("EJB:修改工作简历开始", Level.INFO, null);

		// 得到数据库中的这个记录
		HrJWorkresume workResume = findById(entity.getWorkresumeid());

		// 排他
		if (!formatDate(workResume.getLastModifiedDate(), DATE_FORMAT_YYYYMMDD_TIME_SEC)
				.equals(formatDate(entity.getLastModifiedDate(), DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
			throw new DataChangeException("排它处理");
		}
		
		try {
			// 设置修改日期
			entity.setLastModifiedDate(new Date());
			HrJWorkresume result = entityManager.merge(entity);
			LogUtil.log("EJB:修改工作简历正常结束", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:修改工作简历异常结束", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	public HrJWorkresume findById(Long id) {
		LogUtil.log("finding HrJWorkresume instance with id: " + id,
				Level.INFO, null);
		try {
			HrJWorkresume instance = entityManager
					.find(HrJWorkresume.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJWorkresume entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJWorkresume property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJWorkresume> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJWorkresume> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJWorkresume instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJWorkresume model where model."
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
	 * Find all HrJWorkresume entities.
	 * 
	 * @return List<HrJWorkresume> all HrJWorkresume entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJWorkresume> findAll() {
		LogUtil.log("finding all HrJWorkresume instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJWorkresume model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 根据员工Id, 获得其工作简历基本信息
	 * @param argEmpId 员工Id
	 * @param argEnterpriseCode 企业编码
	 * @param rowStartIdxAndCount  动态参数(开始行数和查询行数)
	 * @return 工作简历基本信息
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWorksumeInfo(Long argEmpId, String argEnterpriseCode, final int ...rowStartIdxAndCount) {
		LogUtil.log("EJB:根据员工Id, 获得其工作简历基本信息开始", Level.INFO, null);
		
		try {
			StringBuilder sbd = new StringBuilder();
			sbd.append("SELECT A.WORKRESUMEID, ");
			sbd.append("A.START_DATE, ");
			sbd.append("A.END_DATE, ");
			sbd.append("A.UNIT, ");
			sbd.append("A.STATION_NAME, ");
			sbd.append("A.HEADSHIP_NAME, ");
			sbd.append("A.WITNESS, ");
			sbd.append("A.MEMO, ");
			sbd.append("A.INSERTBY, ");
			sbd.append("A.INSERTDATE, ");
			sbd.append("A.ENTERPRISE_CODE, ");
			sbd.append("A.IS_USE, ");
			sbd.append("A.LAST_MODIFIED_BY, ");
			sbd.append("A.LAST_MODIFIED_DATE, ");
			sbd.append("A.EMP_ID, ");
			sbd.append(" A.DEPT_NAME ");
			sbd.append("FROM HR_J_WORKRESUME A ");		
			sbd.append("WHERE A.IS_USE = ? ");
			sbd.append("AND A.EMP_ID = ? ");
			sbd.append("AND A.ENTERPRISE_CODE = ? ");		
			sbd.append("ORDER BY A.WORKRESUMEID ");		
			
			Object[] params = new Object[] {"Y", argEmpId, argEnterpriseCode};
			
			// 查询一条有参数sql语句
			List<HrJWorkresume> lstResult = bll.queryByNativeSQL(sbd.toString(),
					params, HrJWorkresume.class, rowStartIdxAndCount);
			String sqlCount = sbd.toString().replaceFirst("SELECT.*? FROM ", "SELECT COUNT(DISTINCT A.WORKRESUMEID) FROM ");
			// 查询符合条件的工作简历总数
			Object objCount = bll.getSingal(sqlCount, params);
			Long totalCount = 0L;
			if (objCount != null) {
				totalCount = Long.parseLong(objCount.toString());
			}
	        
			PageObject result = new PageObject();
			// 符合条件的工作简历信息
			result.setList(lstResult);
			// 符合条件的工作简历信息的总数
			result.setTotalCount(totalCount);

			LogUtil.log("EJB:根据员工Id, 获得其工作简历基本信息正常结束", Level.INFO, null);
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:根据员工Id, 获得其工作简历基本信息异常结束", Level.SEVERE, e);
			throw e;
		}
	}
	
	/**
	 * 查询一条有参数sql语句,返回查询结果 
	 * @param argSql SQL语句
	 * @param params 参数值数组
	 * @param argClass JavaBean class对象
	 * @param rowStartIdxAndCount  动态参数(开始行数和查询行数)
	 * @return 符合条件的List对象
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	@SuppressWarnings("unchecked")
	public List queryDescribeByNativeSQL(String argSql, Object[] params, Class<?> argClass,
			final int ...rowStartIdxAndCount) {
		// 查询一条有参数sql语句,返回查询结果
		List lstResult = bll.queryByNativeSQL(argSql, params, rowStartIdxAndCount);
		
		try {
			lstResult = getDescriptionList(lstResult, argSql, argClass);
		} catch (Exception e) {
			throw new RuntimeException("转换为泛型安全的List时出错!");
		}
		
		return lstResult;
	}

	/**
	 * 转换为泛型安全的List
	 * @param argList 装有对象数组的List
	 * @param argSql SQL语句
	 * @param argClass JavaBean class对象
	 * @return 泛型安全的List
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
	public List getDescriptionList(List argList, String argSql, Class<?> argClass) throws Exception {
    	List lstResult = new ArrayList();
    	if (argList != null ){
    		for (int intCnt = 0; intCnt < argList.size(); intCnt++) {
    			lstResult.add(getDescriptionObject((Object[]) argList.get(intCnt), argSql, argClass));
    		}
    	}
    	
    	return lstResult;
    }
    
    private Object getDescriptionObject(Object[] argObjects, String argSql, Class<?> argClass) throws Exception {
    	Matcher matcherSelect = Pattern.compile("(?<=SELECT\\s)(?:DISTINCT\\s)?(.+?(?=\\s+FROM\\s+))",
    			Pattern.CASE_INSENSITIVE).matcher(argSql);
    	Matcher matcherProp = Pattern.compile("(?:^|,)\\s*+([^,]+(?<=\\S))").matcher("");
    	Matcher matcherSpace = Pattern.compile(".*\\s(\\S+)").matcher("");
    	Matcher matcherSplit = Pattern.compile("\\.(\\S+)").matcher("");
    	
    	String strSelectProp = "";
    	if (matcherSelect.find()) {
    		strSelectProp = matcherSelect.group(1);
    	}
    	
    	Object result = null;
    	
    	int intPropCnt = 0;
    	matcherProp.reset(strSelectProp);
    	while(matcherProp.find()) {
    		String strPropName = matcherProp.group(1);
    		if (strPropName != null) {
    			while (strPropName.indexOf("(") > -1
    					&& strPropName.indexOf(")") < 0) {
    				// 如果有函数
    				if (matcherProp.find()) {
    					strPropName += matcherProp.group(1);
    				} else {
    					break;
    				}
    			}
    			matcherSpace.reset(strPropName);
    			if (matcherSpace.find()) {
    				strPropName = matcherSpace.group(1);
    			}
    			
    			matcherSplit.reset(strPropName);
    			if (matcherSplit.find()) {
    				strPropName = matcherSplit.group(1);
    			}
    			
    			strPropName = convertBeanPropName(strPropName);
    			if (result == null) {
    				result = argClass.newInstance();
    			}
    			
    			Field field = argClass.getDeclaredField(strPropName);
    			field.setAccessible(true);
    			field.set(result, getValueByType(argObjects[intPropCnt], field.getType()));
    		}
    		
    		intPropCnt++;
    	}
    	
    	return result;
    }

    /**
     * 根据类型得到该类型的值
     * @param argObject 值
     * @param argType 类型
     * @return 该类型的值
     */
    private Object getValueByType(Object argObject, Class<?> argType) {
    	if (argObject == null) {
    		return null;
    	}
        String strValue = argObject.toString();

        if (void.class == argType) {
            return null;
        } else if (Integer.class == argType) {
        	if (strValue.trim().length() < 1) {
        		return null;
            }
            return new Integer(strValue);
        } else if (Short.class == argType) {
        	if (strValue.trim().length() < 1) {
        		return null;
            }
        	return new Short(strValue);
        } else if (Long.class == argType) {
        	if (strValue.trim().length() < 1) {
        		return null;
            }
            return new Long(strValue);
        } else if (Double.class == argType) {
        	if (strValue.trim().length() < 1) {
        		return null;
            }
            return new Double(strValue);
        } else if (Float.class == argType) {
        	if (strValue.trim().length() < 1) {
        		return null;
            }
            return new Float(strValue);
        } else if (Boolean.class == argType) {
            return new Boolean(strValue);
        } else if (Byte.class == argType) {
            return new Byte(strValue);
        } else if (Character.class == argType) {
            char ch = '\0';
            if (strValue.trim().length() > 0) {
                ch = strValue.charAt(0);
            }
            return new Character(ch);
        } else if (Date.class == argType) {
        	return argObject;
        }

        return strValue;
    }

    private String convertBeanPropName(String argSqlPropName) {
    	String[] split = argSqlPropName.split("_");
    	
    	if (split == null || split.length < 1) {
    		return argSqlPropName.toLowerCase();
    	}
    	StringBuilder sdbResult = new StringBuilder();
    	sdbResult.append(split[0].toLowerCase());
    	for (int intCnt = 1; intCnt < split.length; intCnt++) {
    		sdbResult.append(getFirstUpperCase(split[intCnt].toLowerCase()));
    	}
    	
    	return sdbResult.toString();
    }

    private String getFirstUpperCase(String argString) {

        if (isNullOrEmpty(argString)) {
            return "";
        }
        if (argString.length() < 2) {
            return argString.toUpperCase();
        }

        char ch = argString.charAt(0);
        ch = Character.toUpperCase(ch);

        return ch + argString.substring(1);
    }

    private boolean isNullOrEmpty(CharSequence argCharSeq) {

        if ((argCharSeq == null) ||
                (argCharSeq.toString().trim().length() < 1)) {
            return true;
        }

        return false;
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

	public void deleteMutilEmpWorkresumeInfo(String ids) {
		if(ids != null)
		{
			String sql = "update hr_j_workresume t set t.is_use='N' where t.workresumeid in ("+ids+")";
			bll.exeNativeSQL(sql);
		}
		
	}

	public void importPersonnelFilesWorkResume(
			List<HrJWorkresume> workResumeList) {
		Long nextId = bll.getMaxId("HR_J_WORKRESUME", "workresumeid");
		for(HrJWorkresume  entity : workResumeList){
			String sql = 
				"select *\n" +
				"  from HR_J_WORKRESUME a\n" + 
				" where a.is_use = 'Y'\n" + 
				"   and a.emp_id = '"+entity.getEmpId()+"'\n" + 
				"   and a.unit = '"+entity.getUnit()+"'\n" + 
				"   and a.dept_name = '"+entity.getDeptName()+"'\n" + 
				"   and a.station_name = '"+entity.getStationName()+"'";
			List<HrJWorkresume> list = bll.queryByNativeSQL(sql, HrJWorkresume.class);
			if(list != null && list.size() > 0){
				HrJWorkresume updated = list.get(0);
				updated.setStartDate(entity.getStartDate());
				updated.setEndDate(entity.getEndDate());
				updated.setMemo(entity.getMemo());
				entityManager.merge(updated);
			}else{
				
				entity.setWorkresumeid(nextId++);
				entityManager.persist(entity);
			}

		}
		
	}
}