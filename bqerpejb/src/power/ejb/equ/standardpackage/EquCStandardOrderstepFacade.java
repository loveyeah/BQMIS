package power.ejb.equ.standardpackage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.search.FromStringTerm;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;



import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.standardpackage.form.StandPackNeedMaterial;

/**
 * Facade for entity EquCStandardOrderstep.
 * 
 * @see power.ejb.equ.standardpackage.EquCStandardOrderstep
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCStandardOrderstepFacade implements
		EquCStandardOrderstepFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved EquCStandardOrderstep
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardOrderstep entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardOrderstep entity) {
		try {
			SimpleDateFormat codeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			Date codevalue = new Date();
			entity.setOperationStep("OS" + codeFormat.format(codevalue));// 按时间设置code
			entity.setIfUse("Y");// 将记录加删除字段默认值设为Y,使用
			if (entity.getOrderby() == null)// 如果没有指定排序号,就将主键值放入排序号字段
				entity.setOrderby(entity.getId());
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Delete a persistent EquCStandardOrderstep entity.
	 * 
	 * @param entity
	 *            EquCStandardOrderstep entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(Long id) {
		try {
			EquCStandardOrderstep entity = new EquCStandardOrderstep();
			entity = findById(id);
			entity.setIfUse("N");// 将记录加删除字段默认值设为N,不使用
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public boolean delete(String ids) {
		try {
			String sql = "UPDATE EQU_C_STANDARD_ORDERSTEP t\n"
					+ "   SET t.if_use = 'N'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved EquCStandardOrderstep entity and return it or
	 * a copy of it to the sender. A copy of the EquCStandardOrderstep entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardOrderstep entity to update
	 * @return EquCStandardOrderstep the persisted EquCStandardOrderstep entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardOrderstep update(EquCStandardOrderstep entity) {
		try {
			EquCStandardOrderstep result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 
	 * EditGrid的增删改方法
	 * 
	 * @param addList
	 * @param updateList
	 * @param delIds
	 */
	public boolean save(List<EquCStandardOrderstep> addList,
			List<EquCStandardOrderstep> updateList, String delIds) {
		try {
			if (addList != null && addList.size() > 0) {
				Long id = dll.getMaxId("EQU_C_STANDARD_ORDERSTEP", "ID");
				int i = 0;
				for (EquCStandardOrderstep entity : addList) {
					entity.setId(id + (i++));
					this.save(entity);
				}
			}
			for (EquCStandardOrderstep entity : updateList) {
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

	public EquCStandardOrderstep findById(Long id) {
		try {
			EquCStandardOrderstep instance = entityManager.find(
					EquCStandardOrderstep.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all EquCStandardOrderstep entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardOrderstep property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardOrderstep> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardOrderstep> findByProperty(String propertyName,
			final Object value) {
		try {
			final String queryString = "select model from EquCStandardOrderstep model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all EquCStandardOrderstep entities.
	 * 
	 * @return List<EquCStandardOrderstep> all EquCStandardOrderstep entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String woCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n" + "  	FROM EQU_C_STANDARD_ORDERSTEP t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n" + "      AND t.enterprisecode = '"
					+ enterpriseCode + "'\n" + " ORDER BY t.orderby,\n"
					+ "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM EQU_C_STANDARD_ORDERSTEP t\n"
					+ " 		WHERE t.if_use = 'Y'\n" + "		 	  AND t.WO_CODE='"
					+ woCode + "'\n" + "   		  AND t.enterprisecode = '"
					+ enterpriseCode + "'";

			List<EquCStandardOrderstep> list = dll.queryByNativeSQL(sql,
					EquCStandardOrderstep.class, rowStartIdxAndCount);

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 获得一个标准工作包中需要的所有物资 
	 * add by liuyi 20100326 
	 * @param woCode
	 * @param enterpriseCode
	 * @return
	 */
	public List<StandPackNeedMaterial> getAllNeedMaterialList(String woCode, String enterpriseCode) {
		List<StandPackNeedMaterial> arrlist = new ArrayList<StandPackNeedMaterial>();
		String sql = 
			"select a.wo_code, a.material_id, sum(a.plan_item_qty),\n" +
			"       sum(b.open_balance) + sum(b.receipt) - sum(b.issue) + sum(b.adjust)\n" + 
			"  from EQU_C_STANDARD_MAINMAT a, inv_j_lot b\n" + 
			" where a.if_use = 'Y'\n" + 
			"       and b.is_use='Y'\n" + 
			"   and a.enterprisecode = '"+enterpriseCode+"'\n" + 
			"   and b.enterprise_code='"+enterpriseCode+"'\n" + 
			"   and a.material_id=b.material_id\n";
		if(woCode != null && !woCode.equals(""))
			sql +="   and a.wo_code='"+woCode+"'\n"; 
		sql += " group by a.wo_code, a.material_id";
		List list = dll.queryByNativeSQL(sql);
		if(list != null && list.size() > 0)
		{
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				Object[] data = (Object[])it.next();
				StandPackNeedMaterial form = new StandPackNeedMaterial();
				if(data[0] != null)
					form.setWoCode(data[0].toString());
				if(data[1] != null)
					form.setMaterialId(Long.parseLong(data[1].toString()));
				if(data[2] != null)
					form.setNeedCount(Double.parseDouble(data[2].toString()));
				if(data[3] != null)
					form.setHasCount(Double.parseDouble(data[3].toString()));
				arrlist.add(form);
			}
		}
		return arrlist;
	}

}