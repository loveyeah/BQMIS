package power.ejb.equ.workbill;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.standardpackage.EquCStandardOrderstep;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquJOrderstep.
 * 
 * @see power.ejb.equ.workbill.EquJOrderstep
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJOrderstepFacade implements EquJOrderstepFacadeRemote {
	// property constants


	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved EquJOrderstep entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJOrderstep entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquJOrderstep entity,int i) {
		LogUtil.log("saving EquJOrderstep instance", Level.INFO, null);
		try {
			SimpleDateFormat codeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			Date codevalue =new Date();
			String j=((Integer) i).toString();
			if(i<10){
				j="00"+j;
			}
			else if(i>=10&&i<100)
			{
				j+="0"+j;
			}
			
			
			if(entity.getOperationStep()==null||entity.getOperationStep()=="")	
				
			entity.setOperationStep("OS" + codeFormat.format(codevalue)+j);// 按时间设置code
			entity.setIfUse("Y");// 将记录加删除字段默认值设为Y,使用
			if (entity.getOrderby() == null)// 如果没有指定排序号,就将主键值放入排序号字段
				entity.setOrderby(entity.getId());
			entityManager.persist(entity);
//			return true;
		} catch (RuntimeException re) {
			throw re;
//			return false;
		}
	}
	public void save(List<EquJOrderstep> addList,
			List<EquJOrderstep> updateList, String delIds) {
		try {
			if (addList != null && addList.size() > 0) {
				Long id = dll.getMaxId("EQU_J_ORDERSTEP", "ID");
				int i = 0;
				for (EquJOrderstep entity : addList) {
					entity.setId(id + (i++));
					this.save(entity,i);
				}
			}
			
			if (updateList != null && updateList.size() > 0) {
			for (EquJOrderstep entity : updateList) {
				this.update(entity);
			}
			}
			
			if (delIds != null && !delIds.trim().equals("")) {
				this.delete(delIds);
			}
//			return true;
		} catch (RuntimeException re) {
			throw re;
//			return false;
		}
	}

	/**
	 * Delete a persistent EquJOrderstep entity.
	 * 
	 * @param entity
	 *            EquJOrderstep entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids) {
		LogUtil.log("deleting EquJOrderstep instance", Level.INFO, null);
		try {
			String sql = "UPDATE EQU_J_ORDERSTEP t\n"
					+ "   SET t.if_use = 'N'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		
		} catch (RuntimeException re) {
		
			return false;
		
		}
		
	}

	/**
	 * Persist a previously saved EquJOrderstep entity and return it or a copy
	 * of it to the sender. A copy of the EquJOrderstep entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquJOrderstep entity to update
	 * @return EquJOrderstep the persisted EquJOrderstep entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJOrderstep update(EquJOrderstep entity) {
		LogUtil.log("updating EquJOrderstep instance", Level.INFO, null);
		try {
			EquJOrderstep result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJOrderstep findById(Long id) {
		LogUtil.log("finding EquJOrderstep instance with id: " + id,
				Level.INFO, null);
		try {
			EquJOrderstep instance = entityManager
					.find(EquJOrderstep.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquJOrderstep entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJOrderstep property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquJOrderstep> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquJOrderstep> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding EquJOrderstep instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquJOrderstep model where model."
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
	 * Find all EquJOrderstep entities.
	 * 
	 * @return List<EquJOrderstep> all EquJOrderstep entities
	 */
	@SuppressWarnings("unchecked")
public PageObject findAll(String woCode,String enterpriseCode) {
		LogUtil.log("finding all EquJOrderstep instances", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n" + "  	FROM EQU_J_ORDERSTEP t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n" + "      AND t.enterprisecode = '"
					+ enterpriseCode + "'\n" + " ORDER BY t.orderby,\n"
					+ "          t.id";

//			String sqlCount = "SELECT count(*)\n"
//					+ "  		 FROM EQU_J_ORDERSTEP t\n"
//					+ " 		WHERE t.if_use = 'Y'\n" + "		 	  AND t.WO_CODE='"
//					+ woCode + "'\n" + "   		  AND t.enterprisecode = '"
//					+ enterpriseCode + "'";

			List<EquJOrderstep> list = dll.queryByNativeSQL(sql,
					EquJOrderstep.class);
		
//			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			Long count=(long)(list.size());
			result.setList(list);
			result.setTotalCount(count);
			return result;
//			Query query = entityManager.createQuery(queryString);
//			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageObject getEquCOrderstepList(String woCode,String enterpriseCode) {
		LogUtil.log("finding all EquJOrderstep instances", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n" + "  	FROM equ_c_standard_orderstep t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n" + "      AND t.enterprisecode = '"
					+ enterpriseCode + "'\n" + " ORDER BY t.orderby,\n"
					+ "          t.id";

//			String sqlCount = "SELECT count(*)\n"
//					+ "  		 FROM EQU_J_ORDERSTEP t\n"
//					+ " 		WHERE t.if_use = 'Y'\n" + "		 	  AND t.WO_CODE='"
//					+ woCode + "'\n" + "   		  AND t.enterprisecode = '"
//					+ enterpriseCode + "'";

			List<EquCStandardOrderstep> list = dll.queryByNativeSQL(sql,
					EquCStandardOrderstep.class);
			
			List<EquJOrderstep> jList = new ArrayList<EquJOrderstep>();
		
			int i;
		
			for(i=0;i<list.size();i++){
				
				EquJOrderstep data=new EquJOrderstep();	
				
				
				 data.setPlanDescription(list.get(i).getDescription());
				 data.setPlanOpDuration(list.get(i).getOpDuration());
				 data.setPlanOperationStepTitle(list.get(i).getOperationStepTitle());
				 data.setPlanPointName(list.get(i).getPointName());
				 data.setOperationStep(list.get(i).getOperationStep());
				 data.setEnterprisecode(enterpriseCode);
				 jList.add(data);	
					
				
			}
		
//			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			Long count=(long)(jList.size());
			result.setList(jList);
			result.setTotalCount(count);
			return result;
//			Query query = entityManager.createQuery(queryString);
//			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	

}