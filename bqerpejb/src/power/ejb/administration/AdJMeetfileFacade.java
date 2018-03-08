package power.ejb.administration;

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
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJMeetfile.
 * 
 * @see power.ejb.administration.AdJMeetfile
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJMeetfileFacade implements AdJMeetfileFacadeRemote {
	// property constants
	public static final String MEET_ID = "meetId";
	public static final String FILE_TYPE = "fileType";
	public static final String FILE_KIND = "fileKind";
	public static final String FILE_NAME = "fileName";
	public static final String IS_USE = "isUse";
	public static final String UPDATE_USER = "updateUser";

	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
    private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";
    
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName ="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved AdJMeetfile entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJMeetfile entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJMeetfile entity) throws SQLException {
		LogUtil.log("Ejb:文件的上传操作开始", Level.INFO, null);
		try {
			if (entity.getId() == null) {
				entity.setId(bll.getMaxId("AD_J_MEETFILE",
						"ID"));
			}
			entityManager.persist(entity);
			LogUtil.log("Ejb:文件的上传操作结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("Ejb:文件的上传操作失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent AdJMeetfile entity.
	 * 
	 * @param entity
	 *            AdJMeetfile entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJMeetfile entity) {
		LogUtil.log("deleting AdJMeetfile instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJMeetfile.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved AdJMeetfile entity and return it or a copy of
	 * it to the sender. A copy of the AdJMeetfile entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJMeetfile entity to update
	 * @return AdJMeetfile the persisted AdJMeetfile entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJMeetfile update(AdJMeetfile entity) throws SQLException, DataChangeException {
		LogUtil.log("Ejb:文件的更新操作开始", Level.INFO, null);

		// 得到数据库中的这个记录
		AdJMeetfile meetFile = findById(entity.getId());

		// 排他
		if (!formatDate(meetFile.getUpdateTime(), DATE_FORMAT_YYYYMMDD_TIME_SEC)
				.equals(formatDate(entity.getUpdateTime(), DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
			throw new DataChangeException("排它处理");
		}
		
		try {
			// 设置修改日期
			entity.setUpdateTime(new Date());
			AdJMeetfile result = entityManager.merge(entity);
			LogUtil.log("Ejb:文件的更新操作结束", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("Ejb:文件的更新操作失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	public AdJMeetfile findById(Long id) {
		LogUtil.log("finding AdJMeetfile instance with id: " + id, Level.INFO,
				null);
		try {
			AdJMeetfile instance = entityManager.find(AdJMeetfile.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJMeetfile entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJMeetfile property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJMeetfile> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJMeetfile> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJMeetfile instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJMeetfile model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<AdJMeetfile> findByMeetId(Object meetId) {
		return findByProperty(MEET_ID, meetId);
	}

	public List<AdJMeetfile> findByFileType(Object fileType) {
		return findByProperty(FILE_TYPE, fileType);
	}

	public List<AdJMeetfile> findByFileKind(Object fileKind) {
		return findByProperty(FILE_KIND, fileKind);
	}

	public List<AdJMeetfile> findByFileName(Object fileName) {
		return findByProperty(FILE_NAME, fileName);
	}

	public List<AdJMeetfile> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<AdJMeetfile> findByUpdateUser(Object updateUser) {
		return findByProperty(UPDATE_USER, updateUser);
	}

	/**
	 * Find all AdJMeetfile entities.
	 * 
	 * @return List<AdJMeetfile> all AdJMeetfile entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJMeetfile> findAll() {
		LogUtil.log("finding all AdJMeetfile instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJMeetfile model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * find by id 且 is_use为"Y"
	 * @param id 附件ID
	 * @return AdJMeetfile 附件信息
	 */
	@SuppressWarnings("unchecked")
	public AdJMeetfile myFindById(Long id) {
		LogUtil.log("EJB:根据ID查找会务附件开始", Level.INFO, null);
		try {
			String sql = 
			"SELECT " + 
			   "* " +
			"FROM " +
			   "AD_J_MEETFILE A " +
			"WHERE " +
			   "A.ID = ? AND " +
			   "A.IS_USE = 'Y'";
			List<AdJMeetfile> list = bll.queryByNativeSQL(sql, new Object[]{id}, 
					AdJMeetfile.class);
			if (null != list) {
				if (list.size() > 0) {
					LogUtil.log("EJB:根据ID查找会务附件结束", Level.INFO, null);
					return list.get(0);
				}
			}
			LogUtil.log("EJB:根据ID查找会务附件结束", Level.INFO, null);
			return null;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:根据ID查找会务附件失败", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * find by 会务id 且 is_use为"Y"
	 * @param meetIdid 会务ID
	 * @return List<AdJMeetfile> 会务信息List
	 */
	@SuppressWarnings("unchecked")
	public PageObject myFindByMeetId(String meetId, final int... rowStartIdxAndCount) {
		LogUtil.log("EJB:查找会务附件开始", Level.INFO, null);
		try {
			String sql = 
			"SELECT " + 
			   "* " +
			"FROM " +
			   "AD_J_MEETFILE A " +
			"WHERE " +
			   "A.MEET_ID = ? AND " +
			   "A.IS_USE = 'Y'";
			List<AdJMeetfile> list = null;
			if (meetId == null || meetId.length() < 1) {
				sql = "SELECT * FROM AD_J_MEETFILE A WHERE A.MEET_ID IS NULL AND A.IS_USE = 'Y'";
				// 查询一条无参数sql语句
				list = bll.queryByNativeSQL(sql, AdJMeetfile.class, rowStartIdxAndCount);
			} else {
				list = bll.queryByNativeSQL(sql, 
						new Object[]{meetId}, AdJMeetfile.class, rowStartIdxAndCount);
			}
			LogUtil.log("sql=" + sql, Level.INFO, null);
			
			sql = "SELECT count(A.ID) " + 
				"FROM " + 
				"	AD_J_MEETFILE A " + 
				"WHERE " +
				   "A.MEET_ID = ? AND " +
				   "A.IS_USE = 'Y'";
			Object obj = null;
			if (meetId == null || meetId.length() < 1) {
				sql = "SELECT count(A.ID) FROM AD_J_MEETFILE A WHERE A.MEET_ID IS NULL AND A.IS_USE = 'Y'";
				// 查询一条无参数sql语句
				obj = bll.getSingal(sql);
			} else {
				obj = bll.getSingal(sql, new Object[]{meetId});
			}
			LogUtil.log("sql=" + sql, Level.INFO, null);
			if (null != list && null != obj) {
				if (list.size() > 0) {
					PageObject pobj = new PageObject();
					pobj.setList(list);
					pobj.setTotalCount(Long.parseLong(obj.toString()));
					LogUtil.log("EJB:查找会务附件结束", Level.INFO, null);
					return pobj;
				}
			}
			LogUtil.log("EJB:查找会务附件结束", Level.INFO, null);
			return null;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:查找会务附件失败", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 得到会务附件表里的最大流水号（用于插入数据）
	 * @return Long id 会务附件的最大流水号
	 */
	public Long myGetMaxId() {
		LogUtil.log("geting max Id", Level.INFO, null);
		try {
				return bll.getMaxId("AD_J_MEETFILE", "ID");
		} catch (RuntimeException re) {
			LogUtil.log("get max Id fail", Level.SEVERE, re);
			throw re;
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
   
}