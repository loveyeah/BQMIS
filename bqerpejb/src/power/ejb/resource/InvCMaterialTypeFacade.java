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
 * Facade for entity InvCMaterialType.
 * 
 * @see power.ejb.resource.InvCMaterialType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvCMaterialTypeFacade implements InvCMaterialTypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved InvCMaterialType entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvCMaterialType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvCMaterialType entity) {
		LogUtil.log("saving InvCMaterialType instance", Level.INFO, null);
		try {
			// 流水号的取得
			Long materialTypeId = bll.getMaxId("inv_c_material_type", "material_type_id");
			entity.setMaterialTypeId(materialTypeId);
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent InvCMaterialType entity.
	 * 
	 * @param entity
	 *            InvCMaterialType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvCMaterialType entity) {
		LogUtil.log("deleting InvCMaterialType instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(InvCMaterialType.class, entity
					.getMaterialTypeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved InvCMaterialType entity and return it or a
	 * copy of it to the sender. A copy of the InvCMaterialType entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            InvCMaterialType entity to update
	 * @return InvCMaterialType the persisted InvCMaterialType entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvCMaterialType update(InvCMaterialType entity) {
		LogUtil.log("updating InvCMaterialType instance", Level.INFO, null);
		try {
			InvCMaterialType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvCMaterialType findById(Long id) {
		LogUtil.log("finding InvCMaterialType instance with id: " + id,
				Level.INFO, null);
		try {
			InvCMaterialType instance = entityManager.find(
					InvCMaterialType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvCMaterialType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCMaterialType property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCMaterialType> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvCMaterialType> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding InvCMaterialType instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvCMaterialType model where model."
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
	 * Find all InvCMaterialType entities.
	 * 
	 * @return List<InvCMaterialType> all InvCMaterialType entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvCMaterialType> findAll() {
		LogUtil.log("finding all InvCMaterialType instances", Level.INFO, null);
		try {
			final String queryString = "select model from InvCMaterialType model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject getMaterialTypeList(String enterpriseCode, final int... rowStartIdxAndCount) {
		try{
			PageObject result = new PageObject();
			String sql = "select\n" + "    type_no,\n"
			     + "    type_name,\n"
			     + "    type_desc,\n"
			     + "    material_type_id\n"
			     + "from inv_c_material_type\n"
			     + "where is_use = 'Y'\n"
			     + " and enterprise_code = '" + enterpriseCode + "'\n"
			     + "order by type_no";
			
			String sqlCount = "select count('material_type_id') "				 
				 + " from inv_c_material_type t\n"
				 + "where is_use = 'Y'\n"
				 + " and enterprise_code = '" + enterpriseCode + "'\n";
			List list = bll.queryByNativeSQL(sql,rowStartIdxAndCount);
			List<InvCMaterialType> arrlist = new ArrayList<InvCMaterialType>();
			Iterator it = list.iterator();
			while (it.hasNext()) { 
				InvCMaterialType mType = new InvCMaterialType();
				Object[] data = (Object[]) it.next();
				mType.setTypeNo(data[0].toString());
				mType.setTypeName(data[1].toString());
				if(data[2] == null) {
					mType.setTypeDesc("");
				} else {
					mType.setTypeDesc(data[2].toString());
				}
				mType.setMaterialTypeId(Long.parseLong(data[3].toString()));
				arrlist.add(mType);
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
	 * 检查类型名称是否已经存在
	 */
	public boolean isTypeNameExist(String enterpriseCode, String typeName) {		
		try {
			boolean isExist = false;
			int count = 0;
			String sqlCount = "select count('material_type_id') from inv_c_material_type t\n"
				 + "where is_use = 'Y'\n"
			     + " and enterprise_code = '" + enterpriseCode + "'\n"
				 + "and type_name = '" + typeName + "'";
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
	 * 检查类型编码是否已经存在
	 */
	public boolean isTypeNoExist(String enterpriseCode, String typeNo) {		
		try {
			boolean isExist = false;
			int count = 0;
			String sqlCount = "select count('material_type_id') from inv_c_material_type t\n"
				 + "where is_use = 'Y'\n"
			     + " and enterprise_code = '" + enterpriseCode + "'\n"
				 + "and type_no = '" + typeNo + "'";
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