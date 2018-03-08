package power.ejb.equ.standardpackage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity EquCStandardEqu.
 * 
 * @see power.ejb.equ.standardpackage.EquCStandardEqu
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCStandardEquFacade implements EquCStandardEquFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved EquCStandardEqu entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCStandardEqu entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	private boolean checksame(String woCode, String kksCode,
			String enterpriseCode) {
		String sqlCount = "SELECT count(*)\n"
				+ "  		 FROM EQU_C_STANDARD_EQU t\n"
				+ " 		WHERE t.if_use = 'Y'\n" + "		 	  AND t.WO_CODE='"
				+ woCode + "'\n" + "		 	  AND t.KKS_CODE='" + kksCode + "'\n"
				+ " 		  AND t.enterprisecode = '" + enterpriseCode + "'";
		Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
		if (count > 0)
			return false;
		else
			return true;
	}

	public boolean save(EquCStandardEqu entity) {
		try {
			if (checksame(entity.getWoCode(), entity.getKksCode(), entity
					.getEnterprisecode())) {
				entity.setId(dll.getMaxId("EQU_C_STANDARD_EQU", "ID"));// 取最大ID,主键
				if (entity.getOrderby() == null)
					entity.setOrderby(entity.getId());// 设置排序号
				entity.setStatus("C");// 状态默认正常
				entity.setIfUse("Y");// 将记录加删除字段默认值设为Y,使用
				entityManager.persist(entity);
			}
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Delete a persistent EquCStandardRepairmethod entity. 假删除
	 * 
	 * @param entity
	 *            EquCStandardRepairmethod entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids) {
		try {
			String sql = "UPDATE equ_c_standard_equ t\n"
					+ "   SET t.if_use = 'N'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public boolean lock(String ids) {
		try {
			String sql = "UPDATE equ_c_standard_equ t\n"
					+ "   SET t.status = 'L'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量锁定记录
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public boolean unlock(String ids) {
		try {
			String sql = "UPDATE equ_c_standard_equ t\n"
					+ "   SET t.status = 'C'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量解锁记录
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Delete a persistent EquCStandardEqu entity.
	 * 
	 * @param entity
	 *            EquCStandardEqu entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(Long id) {
		try {
			EquCStandardEqu entity = new EquCStandardEqu();
			entity = findById(id);
			entity.setIfUse("N");// 将记录加删除字段默认值设为N,不使用
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved EquCStandardEqu entity and return it or a copy
	 * of it to the sender. A copy of the EquCStandardEqu entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardEqu entity to update
	 * @return EquCStandardEqu the persisted EquCStandardEqu entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardEqu update(EquCStandardEqu entity) {
		try {
			EquCStandardEqu result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EquCStandardEqu findById(Long id) {
		try {
			EquCStandardEqu instance = entityManager.find(
					EquCStandardEqu.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all EquCStandardEqu entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardEqu property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardEqu> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardEqu> findByProperty(String propertyName,
			final Object value) {
		try {
			final String queryString = "select model from EquCStandardEqu model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all EquCStandardEqu entities.
	 * 
	 * @return List<EquCStandardEqu> all EquCStandardEqu entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String woCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.*,s.EQU_NAME\n"
					+ " FROM EQU_C_STANDARD_EQU t,equ_c_equipments s\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n" + "      AND t.enterprisecode = '"
					+ enterpriseCode + "'\n"
					+ "AND t.KKS_CODE=s.ATTRIBUTE_CODE \n "
					+ " ORDER BY t.orderby,\n" + "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM EQU_C_STANDARD_EQU t\n"
					+ " 		WHERE t.if_use = 'Y'\n" + "		 	  AND t.WO_CODE='"
					+ woCode + " '\n" + " AND t.enterprisecode = '"
					+ enterpriseCode + "'";

			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);

			// List<EquCStandardEquForm> list = dll.queryByNativeSQL(sql,
			// EquCStandardEquForm.class, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				EquCStandardEqu model = new EquCStandardEqu();
				EquCStandardEquForm omodel = new EquCStandardEquForm();
				Object[] data = (Object[]) it.next();

				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				;
				if (data[1] != null) {
					model.setWoCode(data[1].toString());
				}
				;
				if (data[2] != null) {
					model.setKksCode(data[2].toString());
				}
				;
				if (data[6] != null) {
					model.setMemo(data[6].toString());
				}
				;
				if (data[7] != null) {
					model.setStatus(data[7].toString());
				}
				;
				if (data[8] != null) {
					model.setOrderby(Long.parseLong(data[8].toString()));
				}
				;
				if (data[9] != null) {
					model.setEnterprisecode(data[9].toString());
				}
				;
				if (data[10] != null) {
					model.setIfUse(data[10].toString());
				}
				;
				if (data[11] != null) {
					omodel.setEquName(data[11].toString());
				}
				;
				omodel.setModel(model);
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

	/**
	 * 工单调用时的方法
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardEqu> findToUse(String enterpriseCode, String kksCode) {
		String sql = "SELECT *\n" + "  	FROM EQU_C_STANDARD_EQU t\n"
				+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.STATUS='C'\n"
				+ "		 AND t.KKS_CODE='" + kksCode + "'"
				+ "      AND t.enterprisecode = '" + enterpriseCode + "'\n"
				+ " ORDER BY t.orderby,\n" + "          t.id";
		List<EquCStandardEqu> list = dll.queryByNativeSQL(sql,
				EquCStandardEqu.class);

		return list;
	}
}