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
import power.ejb.administration.form.VisitRegisterInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJManpass.
 * 
 * @see power.ejb.administration.AdJManpass
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJManpassFacade implements AdJManpassFacadeRemote {
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
	 * @throws SQLException 
	 */
	public void save(AdJManpass entity) throws SQLException {
		LogUtil.log("EJB:向来访人员登记表里插入数据开始", Level.INFO, null);
		try {
			// 序号的取得
			Long id = bll.getMaxId("AD_J_MANPASS", "id");
			entity.setId(id);		
			// 更新时间设定
			entity.setUpdateTime(new Date());
			entityManager.persist(entity);
			LogUtil.log("EJB:向来访人员登记表里插入数据结束", Level.INFO, null);
		} catch (Exception re) {
			LogUtil.log("EJB:向来访人员登记表里插入数据失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent AdJManpass entity.
	 * 
	 * @param entity
	 *            AdJManpass entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJManpass entity) {
		LogUtil.log("deleting AdJManpass instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJManpass.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}


	/**
	 * 来访人员登记修改
	 * @param entity 来访人员登记表	
	 * @param strLastmodifyTime 排他时间
	 */
	public void update(AdJManpass entity, String strLastmodifyTime)
			throws DataChangeException, SQLException {
		LogUtil.log("EJB:来访人员登记表更新数据开始", Level.INFO, null);
		try {
			AdJManpass objOld = new AdJManpass();
			objOld = this.findById(entity.getId());
			// 取得时间			
			String strLastmodifiedDate = objOld.getUpdateTime().toString()
			.substring(0, 19);
			if (strLastmodifyTime.equals(strLastmodifiedDate)) {
				entity.setUpdateTime(new Date());
				entityManager.merge(entity);
				LogUtil.log("EJB：来访人员登记表更新数据正常结束", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}
		} catch (DataChangeException ue) {
			LogUtil.log("EJB:来访人员登记表更新数据失败", Level.SEVERE, ue);
			throw ue;
		} catch (Exception e) {
			LogUtil.log("EJB:来访人员登记表更新数据失败", Level.SEVERE, e);
			throw new SQLException("");
		}
	}

	public AdJManpass findById(Long id) {
		LogUtil.log("finding AdJManpass instance with id: " + id, Level.INFO,
				null);
		try {
			AdJManpass instance = entityManager.find(AdJManpass.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJManpass entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJManpass property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJManpass> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJManpass> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJManpass instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJManpass model where model."
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
	 * Find all AdJManpass entities.
	 * 
	 * @return List<AdJManpass> all AdJManpass entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJManpass> findAll() {
		LogUtil.log("finding all AdJManpass instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJManpass model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 删除数据
	 * 
	 * @param lngId
	 *            序号
	 * @param strEmployee
	 *            更新者
	 * @param strUpdateTime
	 *            更新时间
	 * @throws RuntimeException
	 */
	public void delete(String strEmployee, Long lngId, String strUpdateTime)
			throws DataChangeException, SQLException {
		LogUtil.log("EJB:删除来访人员登记表数据开始", Level.INFO, null);
		try {
			AdJManpass objOld = new AdJManpass();
			objOld = this.findById(lngId);
			// 取得时间
			String strLastmodifiedDate = objOld.getUpdateTime().toString()
					.substring(0, 19);
			if (strUpdateTime.equals(strLastmodifiedDate)) {
				String strSql = "UPDATE AD_J_MANPASS T " 
					       + "SET T.UPDATE_USER = ?"
						   + ", T.UPDATE_TIME = SYSDATE "
						   + ", T.IS_USE = ? " 
						   + "WHERE T.ID = ?"; 
				Object[] params = new Object[3];
				params[0] = strEmployee;
				params[1] = "N";
				params[2] = lngId;		  
				LogUtil.log("EJB：删除来访人员登记表数据开始 " , Level.INFO, null);	
				LogUtil.log("EJB：SQL= " + strSql, Level.INFO, null);	
				bll.exeNativeSQL(strSql, params);
				LogUtil.log("EJB：删除来访人员登记表数据结束", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}			
		} catch (DataChangeException ue) {
			LogUtil.log("EJB:删除来访人员登记表数据失败", Level.SEVERE, ue);
			throw ue;
		} catch (Exception e) {
			LogUtil.log("EJB:删除来访人员登记表数据失败", Level.SEVERE, e);
			throw new SQLException("");
		}
	}

}