package power.ejb.manage.stat;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity BpCAnalyseAccount.
 * 
 * @see power.ejb.manage.stat.BpCAnalyseAccount
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCAnalyseAccountFacade implements BpCAnalyseAccountFacadeRemote {
	// property constants

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	@EJB(beanName = "BpCAnalyseAccountItemFacade")
	protected BpCAnalyseAccountItemFacadeRemote ItemRemote;

	@EJB(beanName = "BpCAnalyseAccountSetupFacade")
	protected BpCAnalyseAccountSetupFacadeRemote timeRemote;

	/**
	 * 获取采集指标初始值SQL
	 */
	public String createStartData(Long itemcode, String itemcodeType,
			String enterpriseCode) {
		String strSQL = "";
		try {
			switch (Integer.parseInt(itemcodeType)) {
			// 时数据
			case 1:
				strSQL = "INSERT INTO bp_c_analyse_account_setup";
				for (int i = 1; i < 24; i++) {
					strSQL += " SELECT '" + itemcode + "'," + i + ",'1','"
							+ itemcodeType + "','1','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + itemcode + "',24,'1','" + itemcodeType
						+ "','1','" + enterpriseCode + "'FROM dual";
				break;
			// 日数据
			case 3:
				strSQL = "INSERT INTO bp_c_analyse_account_setup";
				for (int i = 1; i < 31; i++) {
					strSQL += " SELECT '" + itemcode + "'," + i + ",'1','"
							+ itemcodeType + "','1','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + itemcode + "',31,'1','" + itemcodeType
						+ "','1','" + enterpriseCode + "'FROM dual";
				break;
			case 4:
				strSQL = "INSERT INTO bp_c_analyse_account_setup";
				for (int i = 1; i < 12; i++) {
					strSQL += " SELECT '" + itemcode + "'," + i + ",'1','"
							+ itemcodeType + "','1','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + itemcode + "',12,'1','" + itemcodeType
						+ "','1','" + enterpriseCode + "'FROM dual";
				break;
			case 5:
				strSQL = "INSERT INTO bp_c_analyse_account_setup";
				for (int i = 1; i < 4; i++) {
					strSQL += " SELECT '" + itemcode + "'," + i + ",'1','"
							+ itemcodeType + "','1','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + itemcode + "',4,'1','" + itemcodeType
						+ "','1','" + enterpriseCode + "'FROM dual";
				break;
			case 6:
				strSQL = "INSERT INTO bp_c_analyse_account_setup\n"
						+ "VALUES\n" + "  ('" + itemcode + "',\n" + "   1,\n"
						+ "   '1',\n" + "   \n" + "   '" + itemcodeType
						+ "','1','" + enterpriseCode + "')";

				break;
			default:
				break;
			}
		} catch (Exception e) {

		}
		return strSQL;
	}

	/**
	 * Perform an initial save of a previously unsaved BpCAnalyseAccount entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCAnalyseAccount entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCAnalyseAccount entity) {
		LogUtil.log("saving BpCAnalyseAccount instance", Level.INFO, null);
		try {

			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<BpCAnalyseAccount> addList) {
		if (addList != null && addList.size() > 0) {
			Long id = dll.getMaxId("bp_c_analyse_account", "account_code");
			for (BpCAnalyseAccount entity : addList) {
				entity.setAccountCode(id++);
				this.save(entity);
			}
		}
	}

	/**
	 * Delete a persistent BpCAnalyseAccount entity.
	 * 
	 * @param entity
	 *            BpCAnalyseAccount entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCAnalyseAccount entity) {
		LogUtil.log("deleting BpCAnalyseAccount instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpCAnalyseAccount.class, entity
					.getAccountCode());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean delete(String ids) {
		try {
			String[] temp1 = ids.split(",");

			for (String i : temp1) {
				BpCAnalyseAccount entity = new BpCAnalyseAccount();
				entity = this.findById(Long.parseLong(i));
				this.delete(entity);
				ItemRemote.deleteAccountItem(entity.getAccountCode(), entity
						.getEnterpriseCode());
				timeRemote.deleteAccountSetup(entity.getAccountCode(), entity
						.getEnterpriseCode());

			}

			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved BpCAnalyseAccount entity and return it or a
	 * copy of it to the sender. A copy of the BpCAnalyseAccount entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCAnalyseAccount entity to update
	 * @return BpCAnalyseAccount the persisted BpCAnalyseAccount entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCAnalyseAccount update(BpCAnalyseAccount entity) {
		LogUtil.log("updating BpCAnalyseAccount instance", Level.INFO, null);
		try {
			BpCAnalyseAccount result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCAnalyseAccount update(BpCAnalyseAccount entity,
			String datetypeChange) {
		try {
			if (("N").equals(datetypeChange)) {
				dll.exeNativeSQL("DELETE FROM bp_c_analyse_account_setup t\n"
						+ " WHERE t.account_code = '" + entity.getAccountCode()
						+ "'\n" + "   AND t.enterprise_code = '"
						+ entity.getEnterpriseCode() + "'");
				dll.exeNativeSQL(createStartData(entity.getAccountCode(),
						entity.getAccountType(), entity.getEnterpriseCode()));
			}
			BpCAnalyseAccount result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void update(List<BpCAnalyseAccount> updateList) {

		try {
			for (BpCAnalyseAccount data : updateList) {
				Long accountCode = data.getAccountCode();

				String accountName = data.getAccountName();
				String accountType = data.getAccountType();
				if (!this.findById(accountCode).getAccountType().equals(
						accountType)) {
					dll
							.exeNativeSQL("DELETE FROM bp_c_analyse_account_setup t\n"
									+ " WHERE t.account_code = '"
									+ accountCode
									+ "'\n"
									+ "   AND t.enterprise_code = '"
									+ this.findById(accountCode)
											.getEnterpriseCode() + "'");
					dll.exeNativeSQL(createStartData(data.getAccountCode(),
							data.getAccountType(), this.findById(accountCode)
									.getEnterpriseCode()));
				}

				String sql = "update bp_c_analyse_account t\n"
						+ "set  t.account_name='" + accountName + "',"
						+ " t.account_type='" + accountType + "'"
						+ "where t.account_code='" + accountCode + "'\n";

				dll.exeNativeSQL(sql);

			}
		} catch (RuntimeException e) {
			throw e;
		}

	}

	public BpCAnalyseAccount findById(Long id) {
		LogUtil.log("finding BpCAnalyseAccount instance with id: " + id,
				Level.INFO, null);
		try {
			BpCAnalyseAccount instance = entityManager.find(
					BpCAnalyseAccount.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpCAnalyseAccount entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCAnalyseAccount property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCAnalyseAccount> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpCAnalyseAccount> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpCAnalyseAccount instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCAnalyseAccount model where model."
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
	 * Find all BpCAnalyseAccount entities.
	 * 
	 * @return List<BpCAnalyseAccount> all BpCAnalyseAccount entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode,String type,String workerCode, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.*\n" + "  	FROM BP_C_ANALYSE_ACCOUNT t\n"
					+ "    WHERE  t.enterprise_code = '" + enterpriseCode+"'\n" ;

			String sqlCount = "SELECT count(*)\n"
					+ "  	FROM BP_C_ANALYSE_ACCOUNT t\n"
					+ "    WHERE  t.enterprise_code = '" + enterpriseCode+ "'\n";
			
			if(type!=null && type.equals("query")){
				String str = " and (t.account_code || '_tz' in (select r.code from JXL_REPORTS_RIGHT r where r.worker_code='"+workerCode+"') or\n" +
					" t.account_code || '_tz' not in (select r.code from JXL_REPORTS_RIGHT r)\n" + 
					")";
				sqlCount+=str;
				sql+=str;
			}
			String oderStr = " ORDER BY t.account_code\n";
				sql+=oderStr;
				sqlCount+=oderStr;
			List<BpCAnalyseAccount> list = dll.queryByNativeSQL(sql,
					BpCAnalyseAccount.class, rowStartIdxAndCount);

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Long checkAccountCode(String acconutCode) {
		String sql = "select count(1) from BP_C_ANALYSE_ACCOUNT t"
				+ " where t.account_code=" + acconutCode;
		Long count = Long.valueOf(dll.getSingal(sql).toString());
		return count;
	}

}