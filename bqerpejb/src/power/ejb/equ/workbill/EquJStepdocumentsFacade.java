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
import power.ejb.equ.standardpackage.EquCStandardStepdocuments;

import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquJStepdocuments.
 * 
 * @see power.ejb.equ.workbill.EquJStepdocuments
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJStepdocumentsFacade implements EquJStepdocumentsFacadeRemote {
	// property constants


	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	/**
	 * Perform an initial save of a previously unsaved EquJStepdocuments entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJStepdocuments entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */

	
	public boolean save(EquJStepdocuments entity) {
		try {
			entity.setId(dll.getMaxId("EQU_J_STEPDOCUMENTS", "ID"));// 取最大ID,主键					
			entity.setIfUse("Y");// 将记录加删除字段默认值设为Y,使用
			
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}
	
	public boolean save(List<EquJStepdocuments> addList) {
		try {
			
			if (addList != null && addList.size() > 0) {
				Long id = dll.getMaxId("EQU_J_STEPDOCUMENTS", "ID");
				int i = 0;
				for (EquJStepdocuments entity : addList) {
					entity.setId(id + (i++));
					entity.setIfUse("Y");// 将记录加删除字段默认值设为Y,使用
					entityManager.persist(entity);
				}
			}							
		
		
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}


	/**
	 * Delete a persistent EquJStepdocuments entity.
	 * 
	 * @param entity
	 *            EquJStepdocuments entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */

	public boolean delete(String ids) {
		try {
			String sql = "UPDATE EQU_J_STEPDOCUMENTS t\n"
					+ "   SET t.if_use = 'N'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}
	
	public boolean deleteFiles(String ids){
		try {
			String sql = "UPDATE EQU_J_STEPDOCUMENTS t\n"
					+ "   SET t.file_path = ''\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			return false;
		}
		
	}

	/**
	 * Persist a previously saved EquJStepdocuments entity and return it or a
	 * copy of it to the sender. A copy of the EquJStepdocuments entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquJStepdocuments entity to update
	 * @return EquJStepdocuments the persisted EquJStepdocuments entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJStepdocuments update(EquJStepdocuments entity) {
		LogUtil.log("updating EquJStepdocuments instance", Level.INFO, null);
		try {
			EquJStepdocuments result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJStepdocuments findById(Long id) {
		LogUtil.log("finding EquJStepdocuments instance with id: " + id,
				Level.INFO, null);
		try {
			EquJStepdocuments instance = entityManager.find(
					EquJStepdocuments.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquJStepdocuments entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJStepdocuments property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquJStepdocuments> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquJStepdocuments> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding EquJStepdocuments instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquJStepdocuments model where model."
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
	 * Find all EquJStepdocuments entities.
	 * 
	 * @return List<EquJStepdocuments> all EquJStepdocuments entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String woCode,
			 int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n"
					+ "  	FROM EQU_J_STEPDOCUMENTS t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n"
					+        "    AND t.enterprisecode = '" + enterpriseCode + "'\n"
					+ " ORDER BY   t.id";
//			if (operationStep != null)
//				sql += "	 AND t.OPERATION_STEP='" + operationStep + "'\n";
//			sql += "      	 AND t.enterprisecode = '" + enterpriseCode + "'\n"
//					+ " ORDER BY t.orderby,\n" + "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM EQU_J_STEPDOCUMENTS t\n"
					+ "    		WHERE t.if_use = 'Y'\n" + "		 	  AND t.WO_CODE='"
					+ woCode + "'\n"					
					+"		  AND t.enterprisecode = '" + enterpriseCode + "'";
//			if (operationStep != null)
//				sqlCount += "	  AND t.OPERATION_STEP='" + operationStep + "'\n";
//			sqlCount += "		  AND t.enterprisecode = '" + enterpriseCode + "'";

			List<EquJStepdocuments> list = dll.queryByNativeSQL(sql,
					EquJStepdocuments.class, rowStartIdxAndCount);

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	@SuppressWarnings("unchecked")
	public PageObject getEquCStepdocumentsList(String enterpriseCode, String woCode,
			 int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n"
					+ "  	FROM EQU_C_STANDARD_STEPDOCUMENTS t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n"
					+        "    AND t.enterprisecode = '" + enterpriseCode + "'\n"
					+ " ORDER BY   t.id";


//			String sqlCount = "SELECT count(*)\n"
//					+ "  		 FROM EQU_C_STANDARD_STEPDOCUMENTS t\n"
//					+ "    		WHERE t.if_use = 'Y'\n" + "		 	  AND t.WO_CODE='"
//					+ woCode + "'\n"					
//					+"		  AND t.enterprisecode = '" + enterpriseCode + "'";


			List<EquCStandardStepdocuments> list = dll.queryByNativeSQL(sql,
					EquCStandardStepdocuments.class, rowStartIdxAndCount);

List<EquJStepdocuments> jList = new ArrayList<EquJStepdocuments>();
			
			int i;
		
			for(i=0;i<list.size();i++){
				
				EquJStepdocuments data=new EquJStepdocuments();	
//				String temp="";
//
//				
//				if(list.get(i).getFilePath()!=null&&list.get(i).getFilePath()!=""&&list.get(i).getFilePath().indexOf(".")!=-1){
//			 temp=list.get(i).getFilePath().substring(list.get(i).getFilePath().indexOf(".")+1);
//
//				}
				
				 data.setFileCode(list.get(i).getFileCode());
				 data.setFileName(list.get(i).getFileName());
				 data.setFilePath(list.get(i).getFilePath());
				 data.setRelateFile(list.get(i).getRelateFile());
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