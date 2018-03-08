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
 * Facade for entity WoJWorkorder.
 * 
 * @see power.ejb.equ.failure.WoJWorkorder
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class WoJWorkorderFacade implements WoJWorkorderFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved WoJWorkorder entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            WoJWorkorder entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(WoJWorkorder entity) {
		LogUtil.log("saving WoJWorkorder instance", Level.INFO, null);
		try {
			if(entity.getId() == null)
			{
				entity.setId(bll.getMaxId("WO_J_WORKORDER", "id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent WoJWorkorder entity.
	 * 
	 * @param entity
	 *            WoJWorkorder entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(WoJWorkorder entity) {
		LogUtil.log("deleting WoJWorkorder instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(WoJWorkorder.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved WoJWorkorder entity and return it or a copy of
	 * it to the sender. A copy of the WoJWorkorder entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            WoJWorkorder entity to update
	 * @return WoJWorkorder the persisted WoJWorkorder entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public WoJWorkorder update(WoJWorkorder entity) {
		LogUtil.log("updating WoJWorkorder instance", Level.INFO, null);
		try {
			WoJWorkorder result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public WoJWorkorder findById(Long id) {
		LogUtil.log("finding WoJWorkorder instance with id: " + id, Level.INFO,
				null);
		try {
			WoJWorkorder instance = entityManager.find(WoJWorkorder.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all WoJWorkorder entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the WoJWorkorder property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<WoJWorkorder> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<WoJWorkorder> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding WoJWorkorder instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from WoJWorkorder model where model."
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
	 * Find all WoJWorkorder entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<WoJWorkorder> all WoJWorkorder entities
	 */
	@SuppressWarnings("unchecked")
	public List<WoJWorkorder> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all WoJWorkorder instances", Level.INFO, null);
		try {
			final String queryString = "select model from WoJWorkorder model";
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
 public PageObject findListByCode(String equCode,String enterpriseCode,int start,int limit)
 {
	 String countSql="select count(*)\n" +
					 "  from WO_J_WORKORDER t\n" + 
					 " where t.enterprisecode = '"+enterpriseCode+"'\n" + 
					 "   and t.is_use = 'Y'";
     //modify by ypan 20100916
	 String sql= "select t.id,\n" +
				 "       t.failure_code,\n" + 
				 "       t.attribute_code,\n" + 
				 "       t.equ_name,\n" + 
				 "       t.check_attr,\n" + 
				 "       t.pre_content,\n" + 
				 "       t.description,\n" + 
				 "       t.parameters,\n" + 
				 "       t.problem,\n" + 
				 "       t.spare_parts,\n" + 
				 "       t.annex,\n" + 
				 "       to_char(t.start_date, 'yyyy-MM-dd') start_date,\n" + 
				 "       to_char(t.end_date, 'yyyy-MM-dd') end_date,\n" + 
				 "       t.supervisor,\n" + 
				 "       getworkername(t.supervisor) supervisor_name,\n" + 
				 "       t.participants\n" + 
				 "  from WO_J_WORKORDER t\n" + 
				 " where \n" + 
				 "    t.enterprisecode = '"+enterpriseCode+"'\n" + 
				 "   and t.is_use = 'Y'  \n";
	 //级联查询
	 if(equCode.length()!=4)
	 {
		 sql+=" and t.attribute_code like '"+equCode.trim()+"%' order by t.start_date desc";
		 countSql+=" and t.attribute_code like '"+equCode.trim()+"%' order by t.start_date desc";
	 }
	 else
	 {
		 sql+=
			 "and (substr(t.attribute_code, 0, 5) in\n" +
			 "      (select a.attribute_code\n" + 
			 "          from equ_c_equipments a\n" + 
			 "         where a.p_attribute_code = '"+equCode.trim()+"'\n" + 
			 "           and a.is_use = 'Y') or t.attribute_code = '"+equCode.trim()+"') order by t.start_date desc\n";
		 countSql+="and (substr(t.attribute_code, 0, 5) in\n" +
		 "      (select a.attribute_code\n" + 
		 "          from equ_c_equipments a\n" + 
		 "         where a.p_attribute_code = '"+equCode.trim()+"'\n" + 
		 "           and a.is_use = 'Y') or t.attribute_code = '"+equCode.trim()+"') order by t.start_date desc\n";

	 }
	 Long count=Long.parseLong(bll.getSingal(countSql).toString());
	 List<WoJWorkorderForm> list=new ArrayList();
	 List arrayList=bll.queryByNativeSQL(sql, start,limit);
	 Iterator it= arrayList.iterator();
	 while (it.hasNext())
	 {
		 WoJWorkorderForm model=new WoJWorkorderForm();
		 WoJWorkorder order=new WoJWorkorder();
		 Object[] o=(Object[])it.next();
		 if(o[0] != null)
		 {
			order.setId(Long.parseLong(o[0].toString()));
		 }
		 if(o[1] != null)
		 {
			order.setFailureCode(o[1].toString());
		 }
		 else
		 {
			 order.setFailureCode("");
		 }
		 if(o[2] != null)
		 {
			order.setAttributeCode(o[2].toString());
		 }
		 else
		 {
			 order.setAttributeCode("");
		 }
		 if(o[3] != null)
		 {
			order.setEquName(o[3].toString());
		 }
		 else
		 {
			 order.setEquName("");
		 }
		 if(o[4] != null)
		 {
			order.setCheckAttr(o[4].toString());
		 }
		 else
		 {
			 order.setCheckAttr("");
		 }
		 if(o[5] != null)
		 {
			order.setPreContent(o[5].toString());
		 }
		 else
		 {
			 order.setPreContent("");
		 }
		 if(o[6] != null)
		 {
			order.setDescription(o[6].toString());
		 }
		 else
		 {
			 order.setDescription("");
		 }
		 if(o[7] != null)
		 {
			order.setParameters(o[7].toString());
		 }
		 else
		 {
			 order.setParameters("");
		 }
		 if(o[8] != null)
		 {
			order.setProblem(o[8].toString());
		 }
		 else
		 {
			 order.setProblem("");
		 }
		 if(o[9] != null)
		 {
			order.setSpareParts(o[9].toString());
		 }
		 else
		 {
			 order.setSpareParts("");
		 }
		 if(o[10] != null)
		 {
			order.setAnnex(o[10].toString());
		 }
		 else
		 {
			 order.setAnnex("");
		 }
		 if(o[11] != null)
		 {
			model.setStartDate(o[11].toString());
		 }
		 else
		 {
			 model.setStartDate("");
		 }
		 if(o[12] != null)
		 {
			model.setEndDate(o[12].toString());
		 }
		 else
		 {
			 model.setEndDate("");
		 }
		 if(o[13] != null)
		 {
			order.setSupervisor(o[13].toString());
		 }
		 else
		 {
			 order.setSupervisor("");
		 }
		 if(o[14] != null)
		 {
			model.setSupervisorName(o[14].toString());
		 }
		 else
		 {
			 model.setSupervisorName("");
		 }
		 if(o[15] != null)
		 {
			order.setParticipants(o[15].toString());
		 }
		 else
		 {
			 order.setParticipants("");
		 }
		 model.setOrder(order);
		 list.add(model);
	 }
	 PageObject obj=new PageObject();
	 obj.setList(list);
	 obj.setTotalCount(count);
	 return obj;
 }
 public WoJWorkorder findModelByFailure(String failureCode,String enterpriseCode)
 {
	 String sql= "select *\n" +
	 "  from WO_J_WORKORDER t\n" + 
	 " where t.failure_code = '"+failureCode+"'\n" + 
	 "   and t.enterprisecode = '"+enterpriseCode+"'\n" + 
	 "   and t.is_use = 'Y'";
	 List<WoJWorkorder> list=bll.queryByNativeSQL(sql, WoJWorkorder.class);
	 if(list.size() > 0)
	 {
		 return list.get(0);
	 }
	 else
	 {
		 return null;
	 }
 }
}