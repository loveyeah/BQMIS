package power.ejb.administration;

import java.sql.SQLException;
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
 * Facade for entity AdJTimework.
 * 
 * @see power.ejb.administration.AdJTimework
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJTimeworkFacade implements AdJTimeworkFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 定期工作登记表插入数据
	 * 
	 * @param entity
	 *            定期工作登记表
	 * @throws RuntimeException
	 * @author daichunlin
	 */
	public void save(AdJTimework entity) {
		LogUtil.log("EJB:向定期工作登记表里插入数据开始", Level.INFO, null);
		try {
			// 序号的取得
			Long id = bll.getMaxId("AD_J_TIMEWORK", "id");
			entity.setId(id);
			// 工作日期设定
			entity.setWorkDate(new Date());
			// 更新时间设定
			entity.setUpdateTime(new Date());
			entityManager.persist(entity);
			LogUtil.log("EJB:向定期工作登记表里插入数据结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:向定期工作登记表里插入数据失败", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 定期工作登记表删除数据
	 * 
	 * @param lngId
	 *            序号
	 * @param strEmployee
	 *            更新者
	 * @throws RuntimeException
	 */
	public void delete(String strEmployee, Long lngId, String strUpdateTime)
			throws DataChangeException, SQLException {
		LogUtil.log("EJB:从定期工作登记表里删除数据开始", Level.INFO, null);
		try {
			AdJTimework objOld = new AdJTimework();
			objOld = this.findById(lngId);
			
			String strLastmodifiedDate = objOld.getUpdateTime().toString()
					.substring(0, 19);
			if (strUpdateTime.equals(strLastmodifiedDate)) {
				String strSql = "UPDATE AD_J_TIMEWORK T " 
					       + "SET T.UPDATE_USER = ?"
						   + ", T.UPDATE_TIME = SYSDATE "
						   + ", T.IS_USE = ? " 
						   + "WHERE T.ID = ?"; 
				Object[] params = new Object[3];
				params[0] = strEmployee;
				params[1] = "N";
				params[2] = lngId;
				LogUtil.log("EJB：从定期工作登记表里删除数据开始 ", Level.INFO, null);
						
				bll.exeNativeSQL(strSql, params);
				LogUtil.log("EJB：从定期工作登记表里删除数据结束", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}			
		} catch (RuntimeException re) {
			LogUtil.log("EJB:从定期工作登记表里删除数据失败", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	/**
	 * 定期工作登记表更新数据
	 * 
	 * @param entity
	 * @return AdJTimework
	 * @throws RuntimeException
	 */
	public AdJTimework update(AdJTimework entity) {
		LogUtil.log("EJB:定期工作登记表更新数据开始", Level.INFO, null);
		try {
			// 更新时间
			entity.setUpdateTime(new Date());
			AdJTimework result = entityManager.merge(entity);
			LogUtil.log("EJB:定期工作登记表更新数据结束", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:定期工作登记表更新数据失败", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 定期工作登记表更新数据
	 * 
	 * @param entity
	 * @return AdJTimework
	 * @throws RuntimeException
	 */	
	public void update(AdJTimework entity, String strLastmodifyTime)
			throws DataChangeException, SQLException {
		LogUtil.log("EJB:定期工作登记表更新数据开始", Level.INFO, null);
		try {
			AdJTimework objOld = new AdJTimework();
			objOld = this.findById(entity.getId());
			String strUpdateTime = objOld.getUpdateTime().toString().substring(
					0, 19);
			if (strLastmodifyTime.equals(strUpdateTime)) {
				entity.setUpdateTime(new Date());
				entityManager.merge(entity);
				LogUtil.log("EJB：定期工作登记表更新数据正常结束", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}
		} catch (RuntimeException re) {
			LogUtil.log("EJB:定期工作登记表更新数据失败", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	public AdJTimework findById(Long id) {
		LogUtil.log("finding AdJTimework instance with id: " + id, Level.INFO,
				null);
		try {
			AdJTimework instance = entityManager.find(AdJTimework.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJTimework entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJTimework property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJTimework> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJTimework> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJTimework instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJTimework model where model."
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
	 * Find all AdJTimework entities.
	 * 
	 * @return List<AdJTimework> all AdJTimework entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJTimework> findAll() {
		LogUtil.log("finding all AdJTimework instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJTimework model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}


}