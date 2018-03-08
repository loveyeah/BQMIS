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
 * Facade for entity EquCStandardMainmat.
 * 
 * @see power.ejb.equ.standardpackage.EquCStandardMainmat
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCStandardMainmatFacade implements
		EquCStandardMainmatFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 增加
	 */
	public void save(EquCStandardMainmat entity) {
		try {
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 批量增加
	 */
	public void saveEquCStandardMainmat(List<EquCStandardMainmat> addList) {
		if (addList != null && addList.size() > 0) {
			Long id = dll.getMaxId("EQU_C_STANDARD_MAINMAT", "ID");
			int i = 0;
			for (EquCStandardMainmat entity : addList) {
				entity.setId(id + (i++));
				this.save(entity);
			}
		}
	}

	/**
	 * 删除
	 */
	public void delete(EquCStandardMainmat entity) {
		try {
			entity = entityManager.getReference(EquCStandardMainmat.class,
					entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean deleteMainmat(String ids) {
		try {
			String sql = "update EQU_C_STANDARD_MAINMAT t\n"
					+ "   set t.if_use = 'N'\n" 
					+ " where t.id in (" + ids + ")";
			System.out.println("the deltet"+sql);
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}
	
	/**
	 * 修改
	 */
	public EquCStandardMainmat update(EquCStandardMainmat entity) {
		try {
			EquCStandardMainmat result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void update(List<EquCStandardMainmat> updateList) {
		int i;
		for (i = 0; i < updateList.size(); i++) {
			update(updateList.get(i));
		}

	}
	
	
	public EquCStandardMainmat findById(Long id) {
		try {
			EquCStandardMainmat instance = entityManager.find(
					EquCStandardMainmat.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * @return List<EquCStandardMainmat> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardMainmat> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding EquCStandardMainmat instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquCStandardMainmat model where model."
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
	 * @param rowStartIdxAndCount
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardMainmat> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCStandardMainmat instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from EquCStandardMainmat model";
			Query query = entityManager.createQuery(queryString);
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
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
			@SuppressWarnings("unchecked")
	public PageObject findAllmainmat(String enterpriseCode, String woCode,
					String operationStep, int... rowStartIdxAndCount) {
				try {
					PageObject result = new PageObject();
					String sql = 
						"select m.id,\n" +
						"       m.wo_code,\n" + 
						"       m.operation_step,\n" + 
						"       m.MATERIAL_id,\n" + 
						"       (select r.material_name\n" + 
						"          from INV_C_MATERIAL r\n" + 
						"         where r.material_id = m.material_id) as materialName,\n" + 
						"       m.plan_item_qty,\n" + 
						"       m.plan_material_price,\n" + 
						"       m.plan_vendor,\n" +      
                        "       (select c.client_name from CON_J_CLIENTS_INFO   c\n" +
                         "      where c.client_code=  m.plan_vendor)as supplyName,\n" + 
						"       m.order_by,\n" + 
						"       m.direct_req\n" + 
						"  from EQU_C_STANDARD_MAINMAT m"

							+ " where  m.if_use = 'Y'\n"
							+ " and  m.wo_code='"+ woCode + "'\n"
							+ " and  m.operation_step='"+ operationStep + "'\n"
							+ " and m.enterprisecode = '"+ enterpriseCode + "'\n"
						    + " order by m.order_by , m.id\n";

					String sqlCount = "select count(1) from ("+sql+")";
						System.out.println("the sql"+sql);

					List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);

					Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
					result.setList(list);
					result.setTotalCount(count);
					return result;
				} catch (RuntimeException re) {
					throw re;
				}
			}
}