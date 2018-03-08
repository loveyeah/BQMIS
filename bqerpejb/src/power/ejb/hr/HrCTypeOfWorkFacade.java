package power.ejb.hr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.form.DrpCommBeanInfo;


/**
 * Facade for entity HrCTypeOfWork.
 * 
 * @see powereai.po.hr.HrCTypeOfWork
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCTypeOfWorkFacade implements HrCTypeOfWorkFacadeRemote {
	// property constants
	public static final String TYPE_OF_WORK_NAME = "typeOfWorkName";
	public static final String TYPE_OF_WORK_TYPE = "typeOfWorkType";
	public static final String IS_USE = "isUse";
	public static final String RETRIEVE_CODE = "retrieveCode";
	 // 工种类别
    private static final String TYPE_TYPE_1 = "1";
    private static final String TYPE_TYPE_2 = "2";
    private static final String TYPE_TYPE_3 = "3";
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved HrCTypeOfWork entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCTypeOfWork entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCTypeOfWork entity) throws CodeRepeatException{
		LogUtil.log("saving HrCTypeOfWork instance", Level.INFO, null);
		
		try {
			if(checkNameSameForAdd(entity.getTypeOfWorkName(),entity.getIsUse()))
			{
				throw new CodeRepeatException("工种名称已经存在!");
			} 
			entity.setTypeOfWorkId(bll.getMaxId("hr_c_type_of_work", "TYPE_OF_WORK_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	private boolean checkNameSameForAdd(String typeOfWorkName,String isUse)
	{
		String sql = "select count(1) from HR_C_TYPE_OF_WORK t where t.TYPE_OF_WORK_NAME=? and t.IS_USE=?";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{typeOfWorkName,isUse}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}
	private boolean checkNameSameForUpdate(String typeOfWorkName,String isUse,Long typeOfWorkId)
	{
		String sql = "select count(1) from HR_C_TYPE_OF_WORK t where t.TYPE_OF_WORK_NAME=? and t.IS_USE=? and t.type_of_work_id<>?";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{typeOfWorkName,isUse,typeOfWorkId}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}
	public static String convertoString(String inStr) throws NamingException{
		String sql= String.format(
				"select  fun_spellcode('"+inStr+"') from dual", inStr);
		NativeSqlHelperRemote dll = (NativeSqlHelperRemote) Ejb3Factory
				.getInstance().getFacadeRemote("NativeSqlHelper");
		String str=dll.getSingal(sql).toString();
		return str;
	}

	/**
	 * Delete a persistent HrCTypeOfWork entity.
	 * 
	 * @param entity
	 *            HrCTypeOfWork entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(String ids) {
		LogUtil.log("deleting HrCTypeOfWork instance", Level.INFO, null);
		
			String sql="update HR_C_TYPE_OF_WORK  set is_use='D' where TYPE_OF_WORK_ID  in ("+ids+")";
			bll.exeNativeSQL(sql);
	}

	/**
	 * Persist a previously saved HrCTypeOfWork entity and return it or a copy
	 * of it to the sender. A copy of the HrCTypeOfWork entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCTypeOfWork entity to update
	 * @return HrCTypeOfWork the persisted HrCTypeOfWork entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCTypeOfWork update(HrCTypeOfWork entity) throws CodeRepeatException{
		LogUtil.log("updating HrCTypeOfWork instance", Level.INFO, null);
		try {
			if(checkNameSameForUpdate(entity.getTypeOfWorkName(),entity.getIsUse(),entity.getTypeOfWorkId()))
			{
				throw new CodeRepeatException("工种名称已经存在!");
			} 
			HrCTypeOfWork result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCTypeOfWork findById(Long id) {
		LogUtil.log("finding HrCTypeOfWork instance with id: " + id,
				Level.INFO, null);
		try {
			HrCTypeOfWork instance = entityManager
					.find(HrCTypeOfWork.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCTypeOfWork entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCTypeOfWork property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<HrCTypeOfWork> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCTypeOfWork> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrCTypeOfWork instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCTypeOfWork model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrCTypeOfWork> findByTypeOfWorkName(Object typeOfWorkName,
			int... rowStartIdxAndCount) {
		return findByProperty(TYPE_OF_WORK_NAME, typeOfWorkName,
				rowStartIdxAndCount);
	}

	public List<HrCTypeOfWork> findByTypeOfWorkType(Object typeOfWorkType,
			int... rowStartIdxAndCount) {
		return findByProperty(TYPE_OF_WORK_TYPE, typeOfWorkType,
				rowStartIdxAndCount);
	}

	public List<HrCTypeOfWork> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	public List<HrCTypeOfWork> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount) {
		return findByProperty(RETRIEVE_CODE, retrieveCode, rowStartIdxAndCount);
	}

	/**
	 * Find all HrCTypeOfWork entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCTypeOfWork> all HrCTypeOfWork entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCTypeOfWork> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCTypeOfWork instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCTypeOfWork model order by model.typeOfWorkId";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * add by liuyi 091117
	 */
	public List<HrCTypeOfWork> getTypeOfWorkList(){
		String sql = "select t.* from hr_c_type_of_work t where t.is_use = 'U'";
		String sqlCount = "select count(*) from hr_c_type_of_work t where t.is_use = 'U'";
        if(!bll.getSingal(sqlCount).toString().equals(0))
        	return bll.queryByNativeSQL(sql,HrCTypeOfWork.class);
        else
        	return null;
	}
	
	 /**
	  * add by liuyi 091123
     * 获得工种信息
     * 
     * @param enterpriseCode
     *                企业编码
     * @param rowStartIdxAndCount
     * @return PageObject
     */
    public PageObject getWorkTypeList(String enterpriseCode,
	    int... rowStartIdxAndCount) {
	try {
	    PageObject result = new PageObject();
	    String strSql = "select " + "    A.TYPE_OF_WORK_ID, "
		    + "    A.TYPE_OF_WORK_NAME, " + "    A.TYPE_OF_WORK_TYPE, "
		    + "    A.RETRIEVE_CODE, " + "    A.ORDER_BY "
		    + "from HR_C_TYPE_OF_WORK A " + "where A.IS_USE = ? "
		    + " AND A.ENTERPRISE_CODE = ? "
		    + "order by A.TYPE_OF_WORK_ID";
	    String sqlCount = "select count(distinct A.TYPE_OF_WORK_ID)"
		    + "from HR_C_TYPE_OF_WORK A " + "where A.IS_USE = ? "
		    + " AND A.ENTERPRISE_CODE = ? "
		    + "order by A.TYPE_OF_WORK_ID";
	    // 查询参数数组
	    Object[] params = new Object[2];
	    params[0] = 'Y';
	    params[1] = enterpriseCode;

	    LogUtil.log("EJB:工种维护初始化开始。", Level.INFO, null);
	    LogUtil.log("SQL=" + strSql, Level.INFO, null);

	    List list = bll.queryByNativeSQL(strSql, params,
		    rowStartIdxAndCount);
	    List<HrCTypeOfWork> arrList = new ArrayList<HrCTypeOfWork>();
	    Iterator it = list.iterator();
	    while (it.hasNext()) {
		HrCTypeOfWork workTypeBeen = new HrCTypeOfWork();
		Object[] data = (Object[]) it.next();
		if (data[0] != null) {
		    workTypeBeen.setTypeOfWorkId(Long.parseLong(data[0]
			    .toString()));
		}
		if (data[1] != null) {
		    workTypeBeen.setTypeOfWorkName(data[1].toString());
		}
		if (data[2] != null) {
		    String typeCode = data[2].toString();
		    if (!TYPE_TYPE_1.equals(typeCode)
			    && !TYPE_TYPE_2.equals(typeCode)
			    && !TYPE_TYPE_3.equals(typeCode)) {
			typeCode = "";
		    }
		    workTypeBeen.setTypeOfWorkType(typeCode);
		}
		if (data[3] != null) {
		    workTypeBeen.setRetrieveCode(data[3].toString());
		}
		if (data[4] != null) {
			// modified by liuyi 091123 表中无该属性
//		    workTypeBeen.setOrderBy(Long.parseLong(data[4].toString()));
		}
		arrList.add(workTypeBeen);
	    }
	    // 按查询结果集设置返回结果
	    if (arrList.size() == 0) {
		// 设置查询结果集
		result.setList(null);
		// 设置行数
		result.setTotalCount(Long.parseLong("0"));
	    } else {
		result.setList(arrList);
		result.setTotalCount(Long.parseLong(bll.getSingal(sqlCount,
			params).toString()));
	    }
	    LogUtil.log("EJB:工种维护初始化结束。", Level.INFO, null);
	    return result;
	} catch (RuntimeException e) {
	    LogUtil.log("EJB:工种维护初始化失败。", Level.SEVERE, e);
	    throw e;
	}
    }
    
    
    /**
     * add by liuyi 091123
     * 查找所有工种编码
     * 
     * @param enterpriseCode
     *                企业编码
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageObject findAllWorkTypes(String enterpriseCode) {
	try {
	    String sql = "SELECT E.TYPE_OF_WORK_ID,E.TYPE_OF_WORK_NAME \n"
		    + "  FROM HR_C_TYPE_OF_WORK E \n"
		    // modified by liuyi 091125 数据库表属性问题
//		    + " WHERE E.IS_USE = 'Y'AND E.ENTERPRISE_CODE = '"
//		    + enterpriseCode + "'";
		    + " WHERE E.IS_USE = 'U' ";
	    LogUtil.log("所有工种编码id和名称开始。SQL=" + sql, Level.INFO, null);
	    List list = bll.queryByNativeSQL(sql);
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
	    LogUtil.log("查找所有工种编码id和名称结束。", Level.INFO, null);
	    return result;
	} catch (RuntimeException re) {
	    LogUtil.log("查找所有工种编码id和名称失败", Level.SEVERE, re);
	    throw re;
	}
    }
}