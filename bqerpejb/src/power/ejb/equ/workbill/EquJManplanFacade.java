package power.ejb.equ.workbill;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.standardpackage.EquCStandardManplan;

import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquJManplan.
 * 
 * @see power.ejb.equ.workbill.EquJManplan
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJManplanFacade implements EquJManplanFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved EquJManplan entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJManplan entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */

	public boolean save(EquJManplan entity) {
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

	public boolean save(List<EquJManplan> addList,
			List<EquJManplan> updateList, String delIds) {
		try {
			if (addList != null && addList.size() > 0) {
				Long id = dll.getMaxId("EQU_J_MANPLAN", "ID");
				int i = 0;
				for (EquJManplan entity : addList) {
					entity.setId(id + (i++));
					this.save(entity);
				}
			}
			for (EquJManplan entity : updateList) {
				this.update(entity);
			}
			if (delIds != null && !delIds.trim().equals("")) {
				this.delete(delIds);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * Delete a persistent EquJManplan entity.
	 * 
	 * @param entity
	 *            EquJManplan entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */

	public boolean delete(Long id) {
		try {
			EquJManplan entity = new EquJManplan();
			entity = findById(id);
			entity.setIfUse("N");// 将记录加删除字段默认值设为N,不使用
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public boolean delete(String ids) {
		try {
			String sql = "UPDATE EQU_J_MANPLAN t\n"
					+ "   SET t.if_use = 'N'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved EquJManplan entity and return it or a copy of
	 * it to the sender. A copy of the EquJManplan entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            EquJManplan entity to update
	 * @return EquJManplan the persisted EquJManplan entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJManplan update(EquJManplan entity) {
		LogUtil.log("updating EquJManplan instance", Level.INFO, null);
		try {
			EquJManplan result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJManplan findById(Long id) {
		LogUtil.log("finding EquJManplan instance with id: " + id, Level.INFO,
				null);
		try {
			EquJManplan instance = entityManager.find(EquJManplan.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquJManplan entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJManplan property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<EquJManplan> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquJManplan> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding EquJManplan instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquJManplan model where model."
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

	/**
	 * Find all EquJManplan entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJManplan> all EquJManplan entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n" + "  	FROM EQU_J_MANPLAN t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n" + "		 AND t.OPERATION_STEP='"
					+ operationStep + "'\n" + "      AND t.enterprisecode = '"
					+ enterpriseCode + "'\n" + " ORDER BY t.orderby,\n"
					+ "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM EQU_J_MANPLAN t\n"
					+ " 		WHERE t.if_use = 'Y'\n" + "			  AND t.WO_CODE='"
					+ woCode + "'\n" + "		 	  AND t.OPERATION_STEP='"
					+ operationStep + "'\n" + "   		  AND t.enterprisecode = '"
					+ enterpriseCode + "'";

			List<EquJManplan> list = dll.queryByNativeSQL(sql,
					EquJManplan.class, rowStartIdxAndCount);

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	@SuppressWarnings("unchecked")
	public PageObject getEquCManplan(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n" + "  	FROM Equ_c_Standard_Manplan t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n" + "		 AND t.OPERATION_STEP='"
					+ operationStep + "'\n" + "      AND t.enterprisecode = '"
					+ enterpriseCode + "'\n" + " ORDER BY t.orderby,\n"
					+ "          t.id";

//			String sqlCount = "SELECT count(*)\n"
//					+ "  		 FROM Equ_c_Standard_Manplan t\n"
//					+ " 		WHERE t.if_use = 'Y'\n" + "			  AND t.WO_CODE='"
//					+ woCode + "'\n" + "		 	  AND t.OPERATION_STEP='"
//					+ operationStep + "'\n" + "   		  AND t.enterprisecode = '"
//					+ enterpriseCode + "'";

			List<EquCStandardManplan> list = entityManager.createNativeQuery(sql,
					EquCStandardManplan.class).getResultList();

			List<EquJManplan> jList = new ArrayList<EquJManplan>();
			
			int i;
		
			for(i=0;i<list.size();i++){
				
				EquJManplan data=new EquJManplan();	

				
				 data.setPlanFee(list.get(i).getPlanFee());
				 data.setPlanLaborCode(list.get(i).getPlanLaborCode());
				 // modified by liuyi 20100519 
//				 data.setPlanLaborHrs(Long.parseLong(list.get(i).getPlanLaborHrs().toString()));//modfy by wpzhu
				 data.setPlanLaborHrs(list.get(i).getPlanLaborHrs());
				 data.setPlanLaborQty(list.get(i).getPlanLaborQty());
				 data.setOperationStep(list.get(i).getOperationStep());
				 data.setEnterprisecode(enterpriseCode);
				 
				 jList.add(data);	
					
				
			}
		

			Long count=(long)(jList.size());
			result.setList(jList);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	

}