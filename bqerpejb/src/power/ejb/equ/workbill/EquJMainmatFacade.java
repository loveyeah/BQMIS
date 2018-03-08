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
import power.ejb.equ.standardpackage.EquCStandardMainmat;
import power.ejb.equ.standardpackage.EquCStandardMainmatAdd;


import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquJMainmat.
 * 
 * @see power.ejb.equ.workbill.EquJMainmat
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJMainmatFacade implements EquJMainmatFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;


	public boolean save(EquJMainmat entity) {
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

	public boolean save(List<EquJMainmat> addList,
			List<EquJMainmat> updateList, String delIds) {
		try {
			if (addList != null && addList.size() > 0) {
				Long id = dll.getMaxId("EQU_J_MAINMAT", "ID");
				int i = 0;
				for (EquJMainmat entity : addList) {
					entity.setId(id + (i++));
					this.save(entity);
				}
			}
			for (EquJMainmat entity : updateList) {
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
	 * Delete a persistent EquJMainmat entity.
	 * 
	 * @param entity
	 *            EquJMainmat entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids) {
		try {
			String sql = "UPDATE EQU_J_MAINMAT t\n"
					+ "   SET t.if_use = 'N'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved EquJMainmat entity and return it or a copy of
	 * it to the sender. A copy of the EquJMainmat entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            EquJMainmat entity to update
	 * @return EquJMainmat the persisted EquJMainmat entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJMainmat update(EquJMainmat entity) {
		LogUtil.log("updating EquJMainmat instance", Level.INFO, null);
		try {
			EquJMainmat result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJMainmat findById(Long id) {
		LogUtil.log("finding EquJMainmat instance with id: " + id, Level.INFO,
				null);
		try {
			EquJMainmat instance = entityManager.find(EquJMainmat.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquJMainmat entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJMainmat property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<EquJMainmat> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquJMainmat> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding EquJMainmat instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquJMainmat model where model."
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
	 * Find all EquJMainmat entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJMainmat> all EquJMainmat entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.*,getworkername(t.PLAN_MATERIAL_CODE) replaceName,getworkername(t.FACT_MATERIAL_CODE) replaceName2\n" + "  	FROM EQU_J_MAINMAT t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n" + "		 AND t.OPERATION_STEP='"
					+ operationStep + "'\n" + "      AND t.enterprisecode = '"
					+ enterpriseCode + "'\n" + " ORDER BY t.orderby,\n"
					+ "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM EQU_J_MAINMAT t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n" + "		 AND t.OPERATION_STEP='"
					+ operationStep + "'\n" + "		 AND t.enterprisecode = '"
					+ enterpriseCode + "'";

			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				EquJMainmat baseInfo = new EquJMainmat();
				EquJMainmatAdd omodel = new EquJMainmatAdd();
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
					baseInfo.setPlanMaterialCode(data[4].toString());
				}
				;
				if (data[5] != null) {
					baseInfo.setPlanLocationId(data[5].toString());
				}
				;
				if (data[6] != null) {
					baseInfo.setPlanItemQty(Double.parseDouble(data[6].toString()));
				}
				;
				if (data[7] != null) {
					baseInfo.setPlanUnit(data[7].toString());
				}
				;
				if (data[8] != null) {
					baseInfo.setPlanMaterialPrice(Double.parseDouble(data[8].toString()));
				}
				if (data[12] != null) {
					baseInfo.setFactMaterialCode(data[12].toString());
				}
				;
				if (data[13] != null) {
					baseInfo.setFactLocationId(data[13].toString());
				}
				;
				if (data[14] != null) {
					baseInfo.setFactItemQty(Double.parseDouble(data[14].toString()));
				}
				;
				if (data[15] != null) {
					baseInfo.setFactUnit(data[15].toString());
				}
				;
				if (data[16] != null) {
					baseInfo.setFactMaterialPrice(Double.parseDouble(data[16].toString()));
				}
				
				if (data[23] != null) {
					omodel.setMatName(data[23].toString());
				}
				
				if (data[24] != null) {
					omodel.setFactMatName(data[24].toString());
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
	public PageObject getEquCMainmat(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount) {
		try {
			
			PageObject result = new PageObject();
			String sql = 
				"select m.id,\n" +
				"       m.wo_code,\n" + 
				"       m.operation_step,\n" + 
				"       m.MATERIAL_id,\n" + 
				"       m.plan_item_qty,\n" + 
				"       m.plan_material_price,\n" + 
				"       m.plan_vendor,\n" +      
				"       m.order_by,\n" + 
				"       m.direct_req," +
				"       m.enterprisecode," +
				"       m.if_use,"+"" +
				"       (select r.material_name\n" +   
				"          from INV_C_MATERIAL r\n" + 
				"         where r.material_id = m.material_id) as materialName\n" +
				"     from EQU_C_STANDARD_MAINMAT m"

					+ " where  m.if_use = 'Y'\n"
					+ " and  m.wo_code='"+ woCode + "'\n"
					+ " and  m.operation_step='"+ operationStep + "'\n"
					+ " and m.enterprisecode = '"+ enterpriseCode + "'\n"
				    + " order by m.order_by , m.id\n";

			String sql1 = "SELECT t.*,getworkername(t.PLAN_MATERIAL_CODE) replaceName\n" + "  	FROM EQU_C_STANDARD_MAINMAT t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n" + "		 AND t.OPERATION_STEP='"
					+ operationStep + "'\n" + "      AND t.enterprisecode = '"
					+ enterpriseCode + "'\n" + " ORDER BY t.orderby,\n"
					+ "          t.id";

//			String sqlCount = "SELECT count(*)\n"
//					+ "  		 FROM EQU_C_STANDARD_MAINMAT t\n"
//					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
//					+ woCode + "'\n" + "		 AND t.OPERATION_STEP='"
//					+ operationStep + "'\n" + "		 AND t.enterprisecode = '"
//					+ enterpriseCode + "'";

			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List<EquCStandardMainmatAdd> arrlist = new ArrayList<EquCStandardMainmatAdd>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				EquCStandardMainmat baseInfo = new EquCStandardMainmat();
				EquCStandardMainmatAdd omodel = new EquCStandardMainmatAdd();
				Object[] data = (Object[]) it.next();
				

				if (data[2] != null) {
					baseInfo.setOperationStep(data[2].toString());
				}

				if (data[3] != null) {
					baseInfo.setMaterialId(Long.parseLong(data[3].toString()));
				}
				if (data[4] != null) {
					baseInfo.setPlanItemQty(Double.parseDouble(data[4].toString()));
				}
				
				if (data[5] != null) {
					baseInfo.setPlanMaterialPrice(Double.parseDouble(data[5].toString()));
				}
				if (data[6] != null) {
					baseInfo.setPlanVendor(Long.parseLong(data[6].toString()));
				}
				if (data[8] != null) {
					baseInfo.setDirectReq(data[6].toString());
				}


				
				if (data[11] != null) {
					omodel.setMatName(data[11].toString());
				}
				
				

				omodel.setBaseInfo(baseInfo);
				
				
				arrlist.add(omodel);
			}

List<EquJMainmatAdd> jList = new ArrayList<EquJMainmatAdd>();
			
			int i;
		
			for(i=0;i<arrlist.size();i++){

				
				EquJMainmat data=new EquJMainmat();	
				EquJMainmatAdd data2=new EquJMainmatAdd();	
                if(arrlist.get(i).getBaseInfo().getOperationStep()!=null&&!arrlist.get(i).getBaseInfo().getOperationStep().equals(""))
                {
				data.setOperationStep(arrlist.get(i).getBaseInfo().getOperationStep());
                }
                if(arrlist.get(i).getBaseInfo().getPlanItemQty()!=null)
                {
				 data.setPlanItemQty(arrlist.get(i).getBaseInfo().getPlanItemQty());
                }
                if(arrlist.get(i).getBaseInfo().getPlanMaterialPrice()!=null)
                {
				 data.setPlanMaterialPrice(arrlist.get(i).getBaseInfo().getPlanMaterialPrice());
                }
                if(arrlist.get(i).getBaseInfo().getPlanVendor()!=null&&!arrlist.get(i).getBaseInfo().getPlanVendor().toString().equals(""))
                {
				 data.setPlanVendor(arrlist.get(i).getBaseInfo().getPlanVendor().toString());
                }
                if(arrlist.get(i).getBaseInfo().getDirectReq()!=null&&!arrlist.get(i).getBaseInfo().getDirectReq().equals(""))
                {
				 data.setPlanDirectReq(arrlist.get(i).getBaseInfo().getDirectReq());
                }
                // modified by liuyi 20100519
                if(arrlist.get(i).getBaseInfo().getMaterialId()!=null&&!arrlist.get(i).getBaseInfo().getMaterialId().toString().equals(""))
//                if(arrlist.get(i).getBaseInfo().getMaterialId().toString()!=null&&!arrlist.get(i).getBaseInfo().getMaterialId().toString().equals(""))
                {
				 data.setPlanMaterialCode(arrlist.get(i).getBaseInfo().getMaterialId().toString());
                }
				 data.setEnterprisecode(enterpriseCode);
				 if(arrlist.get(i).getMatName()!=null)
	                {
				 data2.setMatName(arrlist.get(i).getMatName());
	                }
				 data2.setBaseInfo(data);
				 
				 jList.add(data2);	
					
				
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