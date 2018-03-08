package power.ejb.equ.standardpackage;

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
 * Facade for entity EquCStandardService.
 * 
 * @see power.ejb.equ.standardpackage.EquCStandardService
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCStandardServiceFacade implements
		EquCStandardServiceFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved EquCStandardService
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardService entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquCStandardService entity) {
		LogUtil.log("saving EquCStandardService instance", Level.INFO, null);
		try {
			if (entity.getOrderby() == null)
				entity.setOrderby(entity.getId());// 设置排序号
			entity.setIfUse("Y");// 将记录加删除字段默认值设为Y,使用
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void save(List<EquCStandardService> addList){
		if (addList != null && addList.size() > 0) {
			Long id = dll.getMaxId("EQU_C_STANDARD_SERVICE", "ID");// 取最大ID,主键
			int i = 0;
			for (EquCStandardService entity : addList) {
				entity.setId(id + (i++));
				this.save(entity);
			}
		}
	}

	/**
	 * Delete a persistent EquCStandardService entity.
	 * 
	 * @param entity
	 *            EquCStandardService entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCStandardService entity) {
		LogUtil.log("deleting EquCStandardService instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquCStandardService.class,
					entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public boolean delete(String ids) {
		try {
			String sql = "UPDATE EQU_C_STANDARD_SERVICE t\n"
					+ "   SET t.if_use = 'N'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved EquCStandardService entity and return it or a
	 * copy of it to the sender. A copy of the EquCStandardService entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardService entity to update
	 * @return EquCStandardService the persisted EquCStandardService entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardService update(EquCStandardService entity) {
		LogUtil.log("updating EquCStandardService instance", Level.INFO, null);
		try {
			EquCStandardService result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void update(List<EquCStandardService> updateList){
		int i;
		for(i=0;i<updateList.size();i++){
			update(updateList.get(i));
		}
		
	}

	public EquCStandardService findById(Long id) {
		LogUtil.log("finding EquCStandardService instance with id: " + id,
				Level.INFO, null);
		try {
			EquCStandardService instance = entityManager.find(
					EquCStandardService.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquCStandardService entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardService property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardService> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardService> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding EquCStandardService instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquCStandardService model where model."
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
	 * Find all EquCStandardService entities.
	 * 
	 * @return List<EquCStandardService> all EquCStandardService entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.*,b.name replaceName\n"
					+ "  	FROM EQU_C_STANDARD_SERVICE t,EQU_C_WO_SERVICE b \n"
					+ "    WHERE t.if_use = 'Y'\n"
					+ "   and b.is_use = 'Y' \n"
					+ "		 AND t.WO_CODE='"
					+ woCode
					+ "'\n"
					+ "		 AND t.OPERATION_STEP='"
					+ operationStep
					+ "'\n"
					+ "      AND t.enterprisecode = '"
					+ enterpriseCode
					+ "'\n"
					+ "   AND b.enterprise_code = '" + enterpriseCode + "' \n"
					+ " and t.plan_service_code=b.code \n"
					+ " ORDER BY t.orderby,\n"
					+ "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM EQU_C_STANDARD_SERVICE t\n"
					+ " 		WHERE t.if_use = 'Y'\n" + "			  AND t.WO_CODE='"
					+ woCode + "'\n" + "		 	  AND t.OPERATION_STEP='"
					+ operationStep + "'\n" + "   		  AND t.enterprisecode = '"
					+ enterpriseCode + "'";



			
			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				EquCStandardService baseInfo = new EquCStandardService();
				EquCStandardServiceAdd omodel = new EquCStandardServiceAdd();
				Object[] data = (Object[]) it.next();

				if (data[0] != null) {
					baseInfo.setId(Long.parseLong(data[0].toString()));
				}
				;
				if (data[1] != null) {
					baseInfo.setWoCode(data[1].toString());
				}
				;
				if (data[2] != null) {
					baseInfo.setOperationStep(data[2].toString());
				}
				;
				if (data[3] != null) {
					baseInfo.setPlanServiceCode(data[3].toString());
				}
				;
				if (data[4] != null) {
					baseInfo.setPlanServiceUnit(data[4].toString());
				}
				;
				if (data[5] != null) {
					baseInfo.setPlanFee(Double.parseDouble(data[5].toString()));
				}
				;
		
				if (data[9] != null) {
					omodel.setServName(data[9].toString());
				}
				;
				omodel.setBaseInfo(baseInfo);
				arrlist.add(omodel);
			}

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(arrlist);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

}