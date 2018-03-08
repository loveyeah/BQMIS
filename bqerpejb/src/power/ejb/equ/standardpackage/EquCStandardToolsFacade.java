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
 * Facade for entity EquCStandardTools.
 * 
 * @see power.ejb.equ.standardpackage.EquCStandardTools
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCStandardToolsFacade implements EquCStandardToolsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved EquCStandardTools entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCStandardTools entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardTools entity) {
		
		try {

			if (entity.getOrderby() == null)
				entity.setOrderby(entity.getId());// 设置排序号
			  
			entity.setIfUse("Y");// 将记录加删除字段默认值设为Y,使用
			entityManager.persist(entity);
			return true;
			
		} catch (RuntimeException re) {
		
		    return false;
		}
	}
	public void save(List<EquCStandardTools> addList){
		
		if (addList != null && addList.size() > 0) {
			Long id = dll.getMaxId("EQU_C_STANDARD_TOOLS", "ID");// 取最大ID,主键
			int i = 0;
			for (EquCStandardTools entity : addList) {
				entity.setId(id + (i++));
				this.save(entity);
			}
		}
	}
	/**
	 * Delete a persistent EquCStandardTools entity.
	 * 
	 * @param entity
	 *            EquCStandardTools entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
//	public boolean delete(Long id) {
//		
//		try {
//			EquCStandardTools entity=new EquCStandardTools();
//			entity = findById(id);
//			entity.setIfUse("N");// 将记录加删除字段默认值设为N,不使用
//			LogUtil.log("delete successful", Level.INFO, null);
//			return true;
//		} catch (RuntimeException re) {
//			LogUtil.log("delete failed", Level.SEVERE, re);
//			return false;
//		}
//	}
	
	public boolean delete(String ids) {
		

		try {
			String sql = "UPDATE EQU_C_STANDARD_TOOLS t\n"
					+ "   SET t.if_use = 'N'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			return false;
		}
	}

	/**
	 * Persist a previously saved EquCStandardTools entity and return it or a
	 * copy of it to the sender. A copy of the EquCStandardTools entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardTools entity to update
	 * @return EquCStandardTools the persisted EquCStandardTools entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardTools update(EquCStandardTools entity) {
		LogUtil.log("updating EquCStandardTools instance", Level.INFO, null);
		try {
			EquCStandardTools result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void update(List<EquCStandardTools> updateList){
		int i;
		for(i=0;i<updateList.size();i++){
			update(updateList.get(i));
		}
		
	}
	public EquCStandardTools findById(Long id) {
		LogUtil.log("finding EquCStandardTools instance with id: " + id,
				Level.INFO, null);
		try {
			EquCStandardTools instance = entityManager.find(
					EquCStandardTools.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquCStandardTools entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardTools property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardTools> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardTools> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding EquCStandardTools instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquCStandardTools model where model."
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
	 * Find all EquCStandardTools entities.
	 * 
	 * @return List<EquCStandardTools> all EquCStandardTools entities
	 */

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.*, b.name \n"
					+ " FROM EQU_C_STANDARD_TOOLS t,EQU_C_WO_TOOLS b \n"
					+ "    WHERE t.if_use = 'Y'\n" 
					+ " and b.is_use = 'Y' \n"
					+ "		 AND t.WO_CODE='"
					+ woCode + "'\n" 
					+ "		 AND t.OPERATION_STEP='"
					+ operationStep + "'\n"
					+ "      AND t.enterprisecode = '"
					+ enterpriseCode + "'\n"
					+ " and b.enterprise_code='" + enterpriseCode + "' \n"
					+ " and t.plan_tool_code=b.code \n"
					+ " ORDER BY t.orderby,\n"
					+ "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM EQU_C_STANDARD_TOOLS t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n" + "		 AND t.OPERATION_STEP='"
					+ operationStep + "'\n" + "		 AND t.enterprisecode = '"
					+ enterpriseCode + "'";
			
			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				EquCStandardTools baseInfo = new EquCStandardTools();
				EquCStandardToolsAdd omodel = new EquCStandardToolsAdd();
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
					baseInfo.setPlanToolCode(data[3].toString());
				}
				;
				if (data[4] != null) {
					baseInfo.setPlanLocationId(data[4].toString());
				}
				;
				if (data[5] != null) {
					baseInfo.setPlanToolQty(Long.parseLong(data[5].toString()));
				}
				;
				if (data[6] != null) {
					baseInfo.setPlanToolHrs(Double.parseDouble(data[6].toString()));
				}
				;
				if (data[8] != null) {
					baseInfo.setPlanToolPrice(Double.parseDouble(data[8].toString()));
				}
				;
				if (data[9] != null) {
					baseInfo.setPlanToolDescription(data[9].toString());
				}
				;
				if (data[13] != null) {
					omodel.setToolsName(data[13].toString());
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