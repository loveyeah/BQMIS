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

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.standardpackage.EquCStandardTools;
import power.ejb.equ.standardpackage.EquCStandardToolsAdd;

import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquJTools.
 * 
 * @see power.ejb.equ.workbill.EquJTools
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJToolsFacade implements EquJToolsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved EquJTools entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJTools entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */

	public boolean save(EquJTools entity) {

		try {

			if (entity.getOrderby() == null)
				entity.setOrderby(entity.getId());// 设置排序号
		
//			entity.setEnterprisecode("hfdc");
			entity.setIfUse("Y");// 将记录加删除字段默认值设为Y,使用
			entityManager.persist(entity);
			return true;

		} catch (RuntimeException re) {

			return false;
		}
	}

	public boolean save(List<EquJTools> addList, List<EquJTools> updateList,
			String delIds) {
		try {
			if (addList != null && addList.size() > 0) {
				
				Long id = dll.getMaxId("EQU_J_TOOLS", "ID");
                int i = 0;
				System.out.println(addList.size());
				for (EquJTools entity : addList) {
					entity.setId(id + (i++));
					entity.setId(id);
                 this.save(entity);
				}
			}
			if (updateList != null && updateList.size() > 0) {
				for (EquJTools entity : updateList) {
					this.update(entity);
				}
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
	 * Delete a persistent EquJTools entity.
	 * 
	 * @param entity
	 *            EquJTools entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids) {

		try {
			String sql = "UPDATE EQU_J_TOOLS t\n" + "   SET t.if_use = 'N'\n"
					+ " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			return false;
		}
	}

	/**
	 * Persist a previously saved EquJTools entity and return it or a copy of it
	 * to the sender. A copy of the EquJTools entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            EquJTools entity to update
	 * @return EquJTools the persisted EquJTools entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJTools update(EquJTools entity) {
		LogUtil.log("updating EquJTools instance", Level.INFO, null);
		try {
			EquJTools result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJTools findById(Long id) {
		LogUtil.log("finding EquJTools instance with id: " + id, Level.INFO,
				null);
		try {
			EquJTools instance = entityManager.find(EquJTools.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquJTools entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJTools property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<EquJTools> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquJTools> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding EquJTools instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquJTools model where model."
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
	 * Find all EquJTools entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJTools> all EquJTools entities
	 */

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.*,s.name replaceName,getworkername(t.fact_Tool_num) replaceName2\n"
					+ " FROM EQU_J_TOOLS t,equ_c_wo_tools s\n"
					+ "    WHERE t.if_use = 'Y'\n"
					+"and t.plan_Tool_num=s.code "
					+ "		 AND t.WO_CODE='"
					+ woCode
					+ "'\n"
					+ "		 AND t.OPERATION_STEP='"
					+ operationStep
					+ "'\n"
					+ "      AND t.enterprisecode = '"
					+ enterpriseCode
					+ "'\n"
					+ " ORDER BY t.orderby,\n" + "          t.id";

			String sqlCount = "SELECT count(*)\n" + "  		 FROM EQU_J_TOOLS t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n" + "		 AND t.OPERATION_STEP='"
					+ operationStep + "'\n" + "		 AND t.enterprisecode = '"
					+ enterpriseCode + "'";

			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				EquJTools baseInfo = new EquJTools();
				EquJToolsAdd omodel = new EquJToolsAdd();
				Object[] data = (Object[]) it.next();

				if (data[0] != null) {
					baseInfo.setId(Long.parseLong(data[0].toString()));
				}
				;
				if (data[1] != null) {
					baseInfo.setWoCode(data[1].toString());
				}
				;
				if (data[3] != null) {
					baseInfo.setOperationStep(data[3].toString());
				}
				;
				if (data[4] != null) {
					baseInfo.setPlanToolNum(data[4].toString());
				}
				;
				if (data[5] != null) {
					baseInfo.setPlanLocationId(data[5].toString());
				}
				;
				if (data[6] != null) {
					baseInfo.setPlanToolQty(Long.parseLong(data[6].toString()));
				}
				;
				if (data[7] != null) {
					baseInfo.setPlanToolHrs(Double.parseDouble(data[7]
							.toString()));
				}
				;
				if (data[8] != null) {
					baseInfo.setPlanToolPrice(Double.parseDouble(data[8]
							.toString()));
				}
				;
				if (data[9] != null) {
					baseInfo.setPlanToolDescription(data[9].toString());
				}
				;
				if (data[10] != null) {
					baseInfo.setFactToolNum(data[10].toString());
				}
				;
				if (data[11] != null) {
					baseInfo.setFactLocationId(data[11].toString());
				}
				;
				if (data[12] != null) {
					baseInfo
							.setFactToolQty(Long.parseLong(data[12].toString()));
				}
				;
				if (data[13] != null) {
					baseInfo.setFactToolHrs(Double.parseDouble(data[13]
							.toString()));
				}
				;
				if (data[14] != null) {
					baseInfo.setFactToolPrice(Double.parseDouble(data[14]
							.toString()));
				}
				;
				if (data[15] != null) {
					baseInfo.setFactToolDescription(data[15].toString());
				}
				;
				if (data[19] != null) {
					omodel.setToolsName(data[19].toString());
				}
				;
				if (data[20] != null) {
					omodel.setFactToolsName(data[20].toString());
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

	@SuppressWarnings("unchecked")
	public PageObject getEquCTools(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = 
				"select t.id," +
				"t.wo_code," +
				"t.operation_step," +
				"t.plan_tool_code," +
				"t.plan_location_id,\n" +
				"t.plan_tool_qty," +
				"t.plan_tool_hrs," +
				"t.plan_vendor," +
				"t.plan_tool_price," +
				"t.plan_tool_description," +
				"t.orderby,\n" + 
				"t.enterprisecode," +
				"t.if_use," +
				"(select s.name from equ_c_wo_tools s  where s.code= t.plan_tool_code)" +
				" from EQU_C_STANDARD_TOOLS  t"
					+ "    where t.if_use = 'Y'\n"
					+ "		 and t.wo_code='"
					+ woCode
					+ "'\n"
					+ "		 and t.operation_step='"
					+ operationStep
					+ "'\n"
					+ "      and t.enterprisecode = '"
					+ enterpriseCode
					+ "'\n"
					+ " order by t.orderby,\n"
					+ "          t.id";
//System.out.println("the sql"+sql);
			// String sqlCount = "SELECT count(*)\n"
			// + " FROM EQU_C_STANDARD_TOOLS t\n"
			// + " WHERE t.if_use = 'Y'\n" + " AND t.WO_CODE='"
			// + woCode + "'\n" + " AND t.OPERATION_STEP='"
			// + operationStep + "'\n" + " AND t.enterprisecode = '"
			// + enterpriseCode + "'";

			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List<EquCStandardToolsAdd> arrlist = new ArrayList<EquCStandardToolsAdd>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				EquCStandardTools baseInfo = new EquCStandardTools();
				EquCStandardToolsAdd omodel = new EquCStandardToolsAdd();
				Object[] data = (Object[]) it.next();

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
				if (data[7] != null) {
					baseInfo.setPlanVendor(data[7].toString());
				}
				;
				if (data[8] != null) {
					baseInfo.setPlanToolPrice(Double.parseDouble(data[8]
							.toString()));
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

			List<EquJToolsAdd> jList = new ArrayList<EquJToolsAdd>();

			int i;

			for (i = 0; i < arrlist.size(); i++) {

				EquJTools data = new EquJTools();
				EquJToolsAdd data2 = new EquJToolsAdd();
               if (arrlist.get(i).getBaseInfo().getOperationStep() != null
						&& !arrlist.get(i).getBaseInfo().getOperationStep()
								.equals("")) {
					data.setOperationStep(arrlist.get(i).getBaseInfo().getOperationStep());
				}
               if (arrlist.get(i).getBaseInfo()	.getPlanToolQty() != null
						&& !arrlist.get(i).getBaseInfo().getOperationStep()
								.equals("")) {
				data.setPlanToolQty(arrlist.get(i).getBaseInfo()
						
						.getPlanToolQty());
               }
               if (arrlist.get(i).getBaseInfo()
						.getPlanLocationId() != null
						&& !arrlist.get(i).getBaseInfo().getPlanLocationId().equals("")) {
				data.setPlanLocationId(arrlist.get(i).getBaseInfo()
						.getPlanLocationId());
               }
               if (arrlist.get(i).getBaseInfo()
						.getPlanToolCode() != null
						&& !arrlist.get(i).getBaseInfo().getPlanToolCode().equals("")) {
				data.setPlanToolNum(arrlist.get(i).getBaseInfo()
						.getPlanToolCode());
               }
               if (arrlist.get(i).getBaseInfo()
						.getPlanToolPrice()!= null) {
				data.setPlanToolPrice(arrlist.get(i).getBaseInfo()
						.getPlanToolPrice());
               }
				data.setPlanToolDescription(arrlist.get(i).getBaseInfo()
						.getPlanToolDescription());
				data.setPlanToolHrs(arrlist.get(i).getBaseInfo()
						.getPlanToolHrs());
				if(enterpriseCode!=null&&enterpriseCode.equals(""))
				data.setEnterprisecode(enterpriseCode);
				 if (arrlist.get(i).getToolsName() != null
							&& !arrlist.get(i).getToolsName().equals("")) {
				data2.setToolsName(arrlist.get(i).getToolsName());
				 }
				data2.setBaseInfo(data);

				jList.add(data2);

			}

			Long count = (long) (jList.size());
			result.setList(jList);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}

	}
}