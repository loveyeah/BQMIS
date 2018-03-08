/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity InvCMaterialStatus.
 * 
 * @see power.ejb.resource.InvCMaterialStatus
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvCMaterialStatusFacade implements InvCMaterialStatusFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved InvCMaterialStatus
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            InvCMaterialStatus entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvCMaterialStatus entity) {
		LogUtil.log("saving InvCMaterialStatus instance", Level.INFO, null);
		try {
			// 流水号的取得
			Long materialStatusId = bll.getMaxId("inv_c_material_status", "material_status_id");
			entity.setMaterialStatusId(materialStatusId);
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent InvCMaterialStatus entity.
	 * 
	 * @param entity
	 *            InvCMaterialStatus entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvCMaterialStatus entity) {
		LogUtil.log("deleting InvCMaterialStatus instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(InvCMaterialStatus.class,
					entity.getMaterialStatusId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved InvCMaterialStatus entity and return it or a
	 * copy of it to the sender. A copy of the InvCMaterialStatus entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            InvCMaterialStatus entity to update
	 * @return InvCMaterialStatus the persisted InvCMaterialStatus entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvCMaterialStatus update(InvCMaterialStatus entity) {
		LogUtil.log("updating InvCMaterialStatus instance", Level.INFO, null);
		try {
			InvCMaterialStatus result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvCMaterialStatus findById(Long id) {
		LogUtil.log("finding InvCMaterialStatus instance with id: " + id,
				Level.INFO, null);
		try {
			InvCMaterialStatus instance = entityManager.find(
					InvCMaterialStatus.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvCMaterialStatus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCMaterialStatus property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCMaterialStatus> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvCMaterialStatus> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding InvCMaterialStatus instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvCMaterialStatus model where model."
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
	 * Find all InvCMaterialStatus entities.
	 * 
	 * @return List<InvCMaterialStatus> all InvCMaterialStatus entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvCMaterialStatus> findAll() {
		LogUtil.log("finding all InvCMaterialStatus instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from InvCMaterialStatus model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 获取查询物料状态码列表
	 */
	public PageObject getMaterialStatusList(String enterpriseCode, final int... rowStartIdxAndCount) {
		try{
			PageObject result = new PageObject();
			String sql = "select\n" + "    status_no,\n"
			     + "    status_name,\n"
			     + "    status_desc,\n"
			     + "    material_status_id\n"
			     + "from inv_c_material_status\n"
			     + "where is_use = 'Y'\n"
			     + " and enterprise_code = '" + enterpriseCode + "'\n"
			     + "order by status_no";
			
			String sqlCount = "select count('material_status_id')"
				 + "  from inv_c_material_status t\n"
				 + "where is_use = 'Y'\n"
				 + " and enterprise_code = '" + enterpriseCode + "'\n" ;
			List list = bll.queryByNativeSQL(sql,rowStartIdxAndCount);
			List<InvCMaterialStatus> arrlist = new ArrayList<InvCMaterialStatus>();
			Iterator it = list.iterator();
			while (it.hasNext()) { 
				InvCMaterialStatus mStatus = new InvCMaterialStatus();
				Object[] data = (Object[]) it.next();
				mStatus.setStatusNo(data[0].toString());
				mStatus.setStatusName(data[1].toString());
				if(data[2] == null) {
					mStatus.setStatusDesc("");
				} else {
					mStatus.setStatusDesc(data[2].toString());
				}
				mStatus.setMaterialStatusId(Long.parseLong(data[3].toString()));
				arrlist.add(mStatus);
			}
			if(arrlist.size()>0)
			{
				result.setList(arrlist);
				result.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
			}		
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}
	/**
	 * 检查状态码名称是否已经存在
	 */
	public boolean isStatusNameExist(String enterpriseCode, String statusName) {		
		try {
			boolean isExist = false;
			int count = 0;
			String sqlCount = "select count('material_status_id') from inv_c_material_status t\n"
				 + "where is_use = 'Y'\n"
			     + " and enterprise_code = '" + enterpriseCode + "'\n"
				 + "and status_name = '" + statusName + "'";
			count = Integer.parseInt(bll.getSingal(sqlCount).toString());
			if(count > 0) {
				isExist = true;
			}
			return isExist;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}		
	}

	/**
	 * 检查状态码编码是否已经存在
	 */
	public boolean isStatusNoExist(String enterpriseCode, String statusNo) {		
		try {
			boolean isExist = false;
			int count = 0;
			String sqlCount = "select count('material_status_id') from inv_c_material_status t\n"
				 + "where is_use = 'Y'\n"
			     + " and enterprise_code = '" + enterpriseCode + "'\n"
				 + "and status_no = '" + statusNo + "'";
			count = Integer.parseInt(bll.getSingal(sqlCount).toString());
			if(count > 0) {
				isExist = true;
			}
			return isExist;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}		
	}
}