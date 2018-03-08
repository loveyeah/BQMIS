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
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJMeet.
 * 
 * @see power.ejb.administration.AdJMeet
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJMeetFacade implements AdJMeetFacadeRemote {
	// property constants
	public static final String MEET_ID = "meetId";
	public static final String APPLY_MAN = "applyMan";
	public static final String MEET_NAME = "meetName";
	public static final String MEET_PLACE = "meetPlace";
	public static final String ROOM_NEED = "roomNeed";
	public static final String CIG_NAME = "cigName";
	public static final String CIG_PRICE = "cigPrice";
	public static final String CIG_NUM = "cigNum";
	public static final String WINE_NAME = "wineName";
	public static final String WINE_PRICE = "winePrice";
	public static final String WINE_NUM = "wineNum";
	public static final String TF_NUM = "tfNum";
	public static final String TF_THING = "tfThing";
	public static final String DJ_NUM = "djNum";
	public static final String DJ_THING = "djThing";
	public static final String BJ_NUM = "bjNum";
	public static final String BJ_THING = "bjThing";
	public static final String DINNER_NUM = "dinnerNum";
	public static final String DINNER_BZ = "dinnerBz";
	public static final String BUDPAY_INALL = "budpayInall";
	public static final String REALPAY_INALL = "realpayInall";
	public static final String MEET_OTHER = "meetOther";
	public static final String DCM_STATUS = "dcmStatus";
	public static final String WORK_FLOW_NO = "workFlowNo";
	public static final String IS_USE = "isUse";
	public static final String UPDATE_USER = "updateUser";
	
	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
    private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";
    
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName ="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved AdJMeet entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJMeet entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJMeet entity) throws SQLException {
		LogUtil.log("Ejb:新增会务申请上报单开始", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("Ejb:新增会务申请上报单结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("Ejb:新增会务申请上报单失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent AdJMeet entity.
	 * 
	 * @param entity
	 *            AdJMeet entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJMeet entity) {
		LogUtil.log("deleting AdJMeet instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJMeet.class, entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
 
	/**
	 * Persist a previously saved AdJMeet entity and return it or a copy of it
	 * to the sender. A copy of the AdJMeet entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJMeet entity to update
	 * @return AdJMeet the persisted AdJMeet entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJMeet update(AdJMeet entity) throws SQLException, DataChangeException {
		LogUtil.log("Ejb:更新会务申请上报单开始", Level.INFO, null);
		// 排他
		
		// 得到数据库中的这个记录
		AdJMeet meet = myFindById(entity.getMeetId());

		// 排他
		if (!formatDate(meet.getUpdateTime(), DATE_FORMAT_YYYYMMDD_TIME_SEC)
				.equals(formatDate(entity.getUpdateTime(), DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
			throw new DataChangeException("排它处理");
		}
		
		try {
			// 更新时间
			entity.setUpdateTime(new Date());
			AdJMeet result = entityManager.merge(entity);
			LogUtil.log("Ejb:更新会务申请上报单结束", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("Ejb:更新会务申请上报单失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	public AdJMeet findById(Long id) {
		LogUtil
				.log("finding AdJMeet instance with id: " + id, Level.INFO,
						null);
		try {
			AdJMeet instance = entityManager.find(AdJMeet.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJMeet entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJMeet property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJMeet> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJMeet> findByProperty(String propertyName, final Object value) {
		LogUtil.log("finding AdJMeet instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJMeet model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<AdJMeet> findByMeetId(Object meetId) {
		return findByProperty(MEET_ID, meetId);
	}

	public List<AdJMeet> findByApplyMan(Object applyMan) {
		return findByProperty(APPLY_MAN, applyMan);
	}

	public List<AdJMeet> findByMeetName(Object meetName) {
		return findByProperty(MEET_NAME, meetName);
	}

	public List<AdJMeet> findByMeetPlace(Object meetPlace) {
		return findByProperty(MEET_PLACE, meetPlace);
	}

	public List<AdJMeet> findByRoomNeed(Object roomNeed) {
		return findByProperty(ROOM_NEED, roomNeed);
	}

	public List<AdJMeet> findByCigName(Object cigName) {
		return findByProperty(CIG_NAME, cigName);
	}

	public List<AdJMeet> findByCigPrice(Object cigPrice) {
		return findByProperty(CIG_PRICE, cigPrice);
	}

	public List<AdJMeet> findByCigNum(Object cigNum) {
		return findByProperty(CIG_NUM, cigNum);
	}

	public List<AdJMeet> findByWineName(Object wineName) {
		return findByProperty(WINE_NAME, wineName);
	}

	public List<AdJMeet> findByWinePrice(Object winePrice) {
		return findByProperty(WINE_PRICE, winePrice);
	}

	public List<AdJMeet> findByWineNum(Object wineNum) {
		return findByProperty(WINE_NUM, wineNum);
	}

	public List<AdJMeet> findByTfNum(Object tfNum) {
		return findByProperty(TF_NUM, tfNum);
	}

	public List<AdJMeet> findByTfThing(Object tfThing) {
		return findByProperty(TF_THING, tfThing);
	}

	public List<AdJMeet> findByDjNum(Object djNum) {
		return findByProperty(DJ_NUM, djNum);
	}

	public List<AdJMeet> findByDjThing(Object djThing) {
		return findByProperty(DJ_THING, djThing);
	}

	public List<AdJMeet> findByBjNum(Object bjNum) {
		return findByProperty(BJ_NUM, bjNum);
	}

	public List<AdJMeet> findByBjThing(Object bjThing) {
		return findByProperty(BJ_THING, bjThing);
	}

	public List<AdJMeet> findByDinnerNum(Object dinnerNum) {
		return findByProperty(DINNER_NUM, dinnerNum);
	}

	public List<AdJMeet> findByDinnerBz(Object dinnerBz) {
		return findByProperty(DINNER_BZ, dinnerBz);
	}

	public List<AdJMeet> findByBudpayInall(Object budpayInall) {
		return findByProperty(BUDPAY_INALL, budpayInall);
	}

	public List<AdJMeet> findByRealpayInall(Object realpayInall) {
		return findByProperty(REALPAY_INALL, realpayInall);
	}

	public List<AdJMeet> findByMeetOther(Object meetOther) {
		return findByProperty(MEET_OTHER, meetOther);
	}

	public List<AdJMeet> findByDcmStatus(Object dcmStatus) {
		return findByProperty(DCM_STATUS, dcmStatus);
	}

	public List<AdJMeet> findByWorkFlowNo(Object workFlowNo) {
		return findByProperty(WORK_FLOW_NO, workFlowNo);
	}

	public List<AdJMeet> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<AdJMeet> findByUpdateUser(Object updateUser) {
		return findByProperty(UPDATE_USER, updateUser);
	}

	/**
	 * Find all AdJMeet entities.
	 * 
	 * @return List<AdJMeet> all AdJMeet entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJMeet> findAll() {
		LogUtil.log("finding all AdJMeet instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJMeet model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 根据会务单id查找记录
	 * @param meetId 会务单meetId
	 * @return AdJMeet 会务单实体
	 */
	@SuppressWarnings("unchecked")
	public AdJMeet myFindById(String meetId) {
		LogUtil.log("EJB:根据会务单id查找记录开始", Level.INFO, null);
		try {
			String sql = 
			"SELECT " + 
			   "* " +
			"FROM " +
			   "AD_J_MEET A " +
			"WHERE " +
			   "A.MEET_ID = ? AND " +
			   "A.IS_USE = 'Y'";
			List<AdJMeet> list = bll.queryByNativeSQL(sql, new Object[]{meetId}, 
					AdJMeet.class);
			if (null != list) {
				if (list.size() > 0) {
					LogUtil.log("EJB:根据会务单id查找记录结束", Level.INFO, null);
					return list.get(0);
				}
			}
			LogUtil.log("EJB:根据会务单id查找记录结束", Level.INFO, null);
			return null;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:根据会务单id查找记录失败", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 根据会务单id查找记录的最后更新时间
	 * @param meetId 会务单meetId
	 * @return String 会务单的最后更新时间
	 */
	@SuppressWarnings("unchecked")
	public String myFindTimeById(String meetId) {
		LogUtil.log("EJB:根据会务单id查找记录的最后更新时间开始", Level.INFO, null);
		try {
			String sql = 
			"SELECT " + 
			   "TO_CHAR(A.UPDATE_TIME, 'yyyy-mm-dd hh24:mi:ss') " +
			"FROM " +
			   "AD_J_MEET A " +
			"WHERE " +
			   "A.MEET_ID = ? AND " +
			   "A.IS_USE = 'Y'";
			Object obj = bll.getSingal(sql, new Object[]{meetId});
			if (obj != null) {
				String time = obj.toString();
				LogUtil.log("EJB:根据会务单id查找记录的最后更新时间结束", Level.INFO, null);
				return time;
			}
			LogUtil.log("EJB:根据会务单id查找记录的最后更新时间结束", Level.INFO, null);
			return null;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:根据会务单id查找记录的最后更新时间失败", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 得到会务表里的最大流水号（用于插入数据）
	 * @return Long id 会务单的最大流水号
	 */
	public Long myGetMaxId() {
		LogUtil.log("geting max Id", Level.INFO, null);
		try {
				return bll.getMaxId("AD_J_MEET", "ID");
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