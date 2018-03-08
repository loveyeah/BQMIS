package power.ejb.equ.workbill;

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
import power.ejb.manage.project.PrjJInfo;
import power.ejb.manage.project.PrjJInfoAdd;

/**
 * Facade for entity EquCWoService.
 * 
 * @see power.ejb.equ.workbill.EquCWoService
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCWoServiceFacade implements EquCWoServiceFacadeRemote {
	// property constants
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String TYPE = "type";
	public static final String FROM_COM = "fromCom";
	public static final String SER_UNIT = "serUnit";
	public static final String FEE = "fee";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved EquCWoService entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCWoService entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquCWoService entity) {
		LogUtil.log("saving EquCWoService instance", Level.INFO, null);
		try {
			if(entity.getId() == null){
				entity.setId(bll.getMaxId("EQU_C_WO_SERVICE", "id"));	
			}
			entity.setCode(getCode(entity.getId()));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent EquCWoService entity.
	 * 
	 * @param entity
	 *            EquCWoService entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCWoService entity) {
		LogUtil.log("deleting EquCWoService instance", Level.INFO, null);
		try {
//			entity = entityManager.getReference(EquCWoService.class, entity
//					.getId());
//			entityManager.remove(entity);
			entity.setIsUse("N");
			this.update(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved EquCWoService entity and return it or a copy
	 * of it to the sender. A copy of the EquCWoService entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCWoService entity to update
	 * @return EquCWoService the persisted EquCWoService entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCWoService update(EquCWoService entity) {
		LogUtil.log("updating EquCWoService instance", Level.INFO, null);
		try {
			EquCWoService result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	//根据类型选服务记录
	@SuppressWarnings("unchecked")
	public PageObject getServiceByType(String typeId,String queryKey,int... rowStartIdxAndCount){

		LogUtil.log("getServiceByType instances", Level.INFO, null);
		PageObject pg = new PageObject();
		String strWhere = " where t.is_use='Y'" ;
		if (typeId != null && !"".equals(typeId)) {
			strWhere += "   and t.type in (select a.id \n"
				+ "     from equ_c_wo_servicetype a\n"
				+ "  where a.is_use = 'Y'\n"
				+ "    start with a.ID =' "
				+ typeId+ "' \n"
				+ "  connect by prior a.ID = a.PID)";
		}
		if (queryKey != null && queryKey.length() > 0) {
			strWhere += " and (t.code like '%" + queryKey
					+ "%' or t.NAME like '%" + queryKey
					
					+ "%')";
		}
		strWhere += " order by t.code asc,t.id desc";
		String sqlCount = "select count(DISTINCT t.id) from equ_c_wo_service t ";
		sqlCount += strWhere;
		String str = bll.getSingal(sqlCount).toString();
		Long count = Long.parseLong(str);
		// System.out.println(count);
		if (count > 0) {
			String sql = "select t.* "
				
					+ " from equ_c_wo_service t ";
			sql += strWhere;
			List<EquCWoService> list=bll.queryByNativeSQL(sql, EquCWoService.class, rowStartIdxAndCount);
			
//			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
//			List<PrjJInfoAdd> arraylist = new ArrayList<PrjJInfoAdd>();
//			Iterator it = list.iterator();
//			while (it.hasNext()) {
//				Object[] data = (Object[]) it.next();
//				PrjJInfoAdd model = new PrjJInfoAdd();
//				PrjJInfo basemodel = new PrjJInfo();
//				if (data[1] != null) {
//					basemodel.setPrjNo(data[1].toString());
//				}
//				if (data[2] != null) {
//					basemodel.setPrjNoShow(data[2].toString());
//				}
//				if (data[3] != null) {
//					basemodel.setPrjName(data[3].toString());
//				}
//				if (data[30] != null) {
//					model.setChargeByName(data[30].toString());
//				}
//				model.setPrjjInfo(basemodel);
//				arraylist.add(model);
//			}
			pg.setList(list);
			pg.setTotalCount(count);
			return pg;
		} else {
			return null;
		}
	
	}
	public EquCWoService findById(Long id) {
		LogUtil.log("finding EquCWoService instance with id: " + id,
				Level.INFO, null);
		try {
			EquCWoService instance = entityManager.find(EquCWoService.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquCWoService entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCWoService property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCWoService> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCWoService> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding EquCWoService instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquCWoService model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}
	

	public List<EquCWoService> findByCode(Object code) {
		return findByProperty(CODE, code);
	}

	public List<EquCWoService> findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List<EquCWoService> findByType(Object type) {
		return findByProperty(TYPE, type);
	}

	public List<EquCWoService> findByFromCom(Object fromCom) {
		return findByProperty(FROM_COM, fromCom);
	}

	public List<EquCWoService> findBySerUnit(Object serUnit) {
		return findByProperty(SER_UNIT, serUnit);
	}

	public List<EquCWoService> findByFee(Object fee) {
		return findByProperty(FEE, fee);
	}

	public List<EquCWoService> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<EquCWoService> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all EquCWoService entities.
	 * 
	 * @return List<EquCWoService> all EquCWoService entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCWoService instances", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			String sql ="select t.id,\n" +
				"       t.code,\n" + 
				"       t.name,\n" + 
				"       t.fee,\n" + 
				"       t.type,\n" + 
				"       (select g.typename from Equ_c_Wo_Servicetype g where t.type = g.id) typename\n" + 
				"  from equ_c_wo_service t\n" + 
				" where t.is_use = 'Y'";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List<EquCWoServiceAdd> arr = new ArrayList<EquCWoServiceAdd>();
			Long count=(long)(list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				EquCWoService model = new EquCWoService();
				EquCWoServiceAdd model_1 = new EquCWoServiceAdd();
				Object[] ob = (Object[]) it.next();
				if (ob[0] != null)
					model.setId(Long.parseLong(ob[0].toString()));
				if (ob[1] != null)
					model.setCode(ob[1].toString());
				if (ob[2] != null)
					model.setName(ob[2].toString());
				if (ob[3] != null)
					model.setFee(Double.parseDouble(ob[3].toString()));
				if (ob[4] != null)
					model.setType(ob[4].toString());
				if (ob[5] != null)
					model_1.setTypename(ob[5].toString());
				model_1.setService(model);
				arr.add(model_1);
			}
			result.setList(arr);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	private String  getCode(Long Id){
		String issueNo = "S";
		String id = String.valueOf(Id);
		if(id.length() > 6){
			issueNo += id.substring(0,6);
		}else{
			String pad = "000000";
			issueNo +=pad.substring(0, 6 - id.length()) + id;
		}
		return issueNo;
	}
//同名判断
 public boolean checkName(String name){
	 String sql ="select count(1) from equ_c_wo_service t where t.name=? and t.is_use='Y'";
	 int size =Integer.parseInt(bll.getSingal(sql,new Object[] { name }).toString()) ;
	 if(size > 0){
		 return true;
	 } else {
		 return false;
	 }
 }

public boolean checkUpdateName(String name, Long id) {
	 String sql ="select count(1) from equ_c_wo_service t where t.name=? and t.is_use='Y' and t.id <> ?";
	 int size =Integer.parseInt(bll.getSingal(sql,new Object[] { name,id }).toString()) ;
	 if(size > 0){
		 return true;
	 } else {
		 return false;
	 }
}
 
}