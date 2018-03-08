package power.ejb.manage.client;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 合作伙伴信息管理
 * 
 * @author YWLiu
 * 
 */
@Stateless
public class ConJClientsInfoFacade implements ConJClientsInfoFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved ConJClientsInfo entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            ConJClientsInfo entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public ConJClientsInfo save(ConJClientsInfo entity) throws CodeRepeatException {
		LogUtil.log("saving ConJClientsInfo instance", Level.INFO, null);
		try {
			if(this.checkClientName(entity.getClientName()) > 0) {
				throw new CodeRepeatException("合作伙伴名称不能重复！");
			} else {
				if(entity.getCliendId() == null) {
					entity.setCliendId(bll.getMaxId("CON_J_CLIENTS_INFO", "CLIEND_ID"));
				}
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent ConJClientsInfo entity.
	 * 
	 * @param entity
	 *            ConJClientsInfo entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public Integer delete(String cliendId) {
		LogUtil.log("deleting ConJClientsInfo instance", Level.INFO, null);
		try {
			String sql = "delete CON_J_CLIENTS_INFO t where t.CLIEND_ID in ('" + cliendId + "')";
			int result = bll.exeNativeSQL(sql);
			LogUtil.log("delete successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved ConJClientsInfo entity and return it or a copy
	 * of it to the sender. A copy of the ConJClientsInfo entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            ConJClientsInfo entity to update
	 * @return ConJClientsInfo the persisted ConJClientsInfo entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public ConJClientsInfo update(ConJClientsInfo entity) throws CodeRepeatException {
		try {
			if(this.checkClientName(entity.getClientName(),entity.getCliendId()) > 0) {
				throw new CodeRepeatException("危险点名称和危险点类型不能重复！");
			} else {
				ConJClientsInfo result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConJClientsInfo findById(Long id) {
		LogUtil.log("finding ConJClientsInfo instance with id: " + id,
				Level.INFO, null);
		try {
			ConJClientsInfo instance = entityManager.find(
					ConJClientsInfo.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all ConJClientsInfo entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ConJClientsInfo property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<ConJClientsInfo> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<ConJClientsInfo> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding ConJClientsInfo instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from ConJClientsInfo model where model."
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
	 * Find all ConJClientsInfo entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ConJClientsInfo> all ConJClientsInfo entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String clientCodeOrClientName, String approveFlag, String enterpriseCode, final int... rowStartIdxAndCount) {

		try {
			PageObject result = new PageObject();
			if (clientCodeOrClientName == null
					|| "".equals(clientCodeOrClientName)) {
				clientCodeOrClientName = "%";
			}
			String sqlCount = "select count(*) from CON_J_CLIENTS_INFO t where t.ENTERPRISE_CODE = '" 
					+ enterpriseCode + "'"
					+ " and (t.CLIENT_CODE like '%"
					+ clientCodeOrClientName
					+ "%'"
					+ " or t.CLIENT_NAME like '%"
					+ clientCodeOrClientName
					+ "%')";
			if(approveFlag != null && !"".equals(approveFlag)) {
				sqlCount += " and t.approve_flag = '2'";
			}
			Long totalCount = Long.valueOf(bll.getSingal(sqlCount).toString());
			result.setTotalCount(totalCount);
			if (totalCount > 0) {
				String sql = "select t.cliend_id ,t.type_id ,t.character_id ,t.importance_id ,t.trade_id ,t.client_code,t.client_name ,t.cliend_chargeby ,t.client_overview,\n" +
							"       t.main_products,t.main_performance,t.address ,t.telephone, t.zipcode, t.email, t.website,t.fax,t.legal_representative,t.tax_qualification,\n" + 
							"       t.bank,t.account,t.taxnumber ,t.reg_funds,t.reg_address,t.memo,t.approve_flag, (GETWORKERNAME(t.last_modified_by)) as last_modified_by ,t.last_modified_date,t.enterprise_code\n" + 
							"from CON_J_CLIENTS_INFO t where t.ENTERPRISE_CODE = '" 
						+ enterpriseCode + "'"
						+ " and (t.CLIENT_CODE like '%"
						+ clientCodeOrClientName
						+ "%'"
						+ " or t.CLIENT_NAME like '%"
						+ clientCodeOrClientName
						+ "%')";
				if(approveFlag != null && !"".equals(approveFlag)) {
					sql += " and t.approve_flag = '2'";
				}
				List<ConJClientsInfo> list = bll.queryByNativeSQL(sql,
						ConJClientsInfo.class, rowStartIdxAndCount);
				result.setList(list);
			}
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 检查合作伙伴名称是否存在
	 * @param clientName
	 * @return Long类型 大于0 表示名称重复 不大于0 表示名称不重复
	 */
	@SuppressWarnings("unused")
	private Long checkClientName(String clientName ,Long... cliendId) {
		String sql = "select count(*) from CON_J_CLIENTS_INFO t where t.CLIENT_NAME = '" + clientName + "'";
		if (cliendId != null && cliendId.length > 0) {
			sql += "  and t.CLIEND_ID <> " + cliendId[0];
		}
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		return count;
	}

}