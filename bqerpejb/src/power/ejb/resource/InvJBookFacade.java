package power.ejb.resource;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity InvJBook.
 * 
 * @see power.ejb.resource.InvJBook
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvJBookFacade implements InvJBookFacadeRemote {
	// property constants
	public static final String BOOK_NO = "bookNo";
	public static final String BOOK_WHS = "bookWhs";
	public static final String BOOK_LOCATION = "bookLocation";
	public static final String BOOK_MATERIAL_CLASS = "bookMaterialClass";
	public static final String BOOK_MAN = "bookMan";
	public static final String BOOK_STATUS = "bookStatus";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**
     * 
     * @param initialBookNo 传入的单号格式 如"DP000000"
     * @param tabelName 表名
     * @param idColumnName 列名
     * @return 最大的单号
     */
    public String getMaxBookNo(String initialBookNo,String tabelName,String idColumnName ) {
        // 最大单号初始化为初始的单号
        StringBuffer maxBookNo = new StringBuffer(initialBookNo);
        // 数据库中当前的最大单号
        Long bookNo =bll.getMaxId(tabelName, idColumnName);
        String strTemp="0123456789"; 
        // j--数字出现的开始位置 
        int j = 0;
        // 从字符串后面开始检索，检索不是数字的位置
        for (int i=maxBookNo.length()-1; i >= 0; i--) { 
            j=strTemp.indexOf(maxBookNo.charAt(i));  
            // 如果不是数字，返回在该字符在字符串的位置
            if(j == -1) {
                j = i;
                 break;
             }
        }
        if(bookNo < Math.pow(10,(maxBookNo.length()-j-1))) {
            // 最大的数字
            String currentData = "";
            // 获取最大单号的数字部分
                currentData = String.valueOf(bookNo);
                // 如果当前单号的数字部分长度小于初始化时的数字部分长度
            if(currentData.length() < maxBookNo.length()-j-1) {
                maxBookNo.replace(maxBookNo.length()-currentData.length(), maxBookNo.length()+1, currentData);
            }else {
                // 如果当前单号的数字部分长度小于初始化时的数字部分长度
                maxBookNo.replace(j+1,maxBookNo.length()+1,currentData);
            }
    }else {
        maxBookNo =  maxBookNo.replace(j+1,maxBookNo.length()+1,String.valueOf(bookNo));
    }
        System.out.println(maxBookNo.toString());
        return maxBookNo.toString();
    }

	/**
	 * Perform an initial save of a previously unsaved InvJBook entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 /**
     *增加一条记录
     * @param entity 要增加的记录
     */
	public void save(InvJBook entity) {
		LogUtil.log("saving InvJBook instance", Level.INFO, null);
		try {
				entity.setBookId(bll.getMaxId(
					"INV_J_BOOK ", "BOOK_ID"));
                // 设定修改时间
                entity.setLastModifiedDate(new Date());
                entity.setBookDate(new Date());
                // 设定是否使用
                entity.setIsUse("Y");
                // 保存
                entityManager.persist(entity);
                LogUtil.log("save successful", Level.INFO, null);
                return;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent InvJBook entity.
	 * 
	 * @param entity
	 *            InvJBook entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJBook entity) {
		LogUtil.log("deleting InvJBook instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(InvJBook.class, entity
					.getBookId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved InvJBook entity and return it or a copy of it
	 * to the sender. A copy of the InvJBook entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity 要修改的记录
	 * @return InvJBook 修改的记录
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJBook update(InvJBook entity){
		LogUtil.log("updating InvJBook instance", Level.INFO, null);
		try {
			// 修改时间
            entity.setLastModifiedDate(new java.util.Date());
            InvJBook result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvJBook findById(Long id) {
		LogUtil.log("finding InvJBook instance with id: " + id, Level.INFO,
				null);
		try {
			InvJBook instance = entityManager.find(InvJBook.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvJBook entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJBook property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJBook> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvJBook> findByProperty(String propertyName, final Object value) {
		LogUtil.log("finding InvJBook instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvJBook model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<InvJBook> findByBookNo(Object bookNo) {
		return findByProperty(BOOK_NO, bookNo);
	}

	public List<InvJBook> findByBookWhs(Object bookWhs) {
		return findByProperty(BOOK_WHS, bookWhs);
	}

	public List<InvJBook> findByBookLocation(Object bookLocation) {
		return findByProperty(BOOK_LOCATION, bookLocation);
	}

	public List<InvJBook> findByBookMaterialClass(Object bookMaterialClass) {
		return findByProperty(BOOK_MATERIAL_CLASS, bookMaterialClass);
	}

	public List<InvJBook> findByBookMan(Object bookMan) {
		return findByProperty(BOOK_MAN, bookMan);
	}

	public List<InvJBook> findByBookStatus(Object bookStatus) {
		return findByProperty(BOOK_STATUS, bookStatus);
	}

	public List<InvJBook> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<InvJBook> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<InvJBook> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all InvJBook entities.
	 * 
	 * @return List<InvJBook> all InvJBook entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvJBook> findAll() {
		LogUtil.log("finding all InvJBook instances", Level.INFO, null);
		try {
			final String queryString = "select model from InvJBook model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<InvJBook> findBookNoList(String enterpriseCode)
	{
		String sql =
			"SELECT a.*\n" +
			"   FROM INV_J_BOOK a\n" + 
			"  where a.is_use = 'Y'\n" + 
			"    and a.enterprise_code = '"+enterpriseCode+"'";
		return bll.queryByNativeSQL(sql, InvJBook.class);
	}

}