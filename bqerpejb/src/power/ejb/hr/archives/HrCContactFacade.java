package power.ejb.hr.archives;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrJFamilymember;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrCContact.
 * 
 * @see power.ejb.hr.archives.HrCContact
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCContactFacade implements HrCContactFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(HrCContact entity) {
		if (entity.getContactId() == null) {
			entity.setContactId(bll.getMaxId("hr_c_contact", "contact_id"));
			entityManager.persist(entity);
		} else {
			this.update(entity);
		}
	}

	public void delete(HrCContact entity) {
		LogUtil.log("deleting HrCContact instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCContact.class, entity
					.getContactId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCContact update(HrCContact entity) {
		LogUtil.log("updating HrCContact instance", Level.INFO, null);
		try {
			HrCContact result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCContact findById(Long id) {
		LogUtil.log("finding HrCContact instance with id: " + id, Level.INFO,
				null);
		try {
			HrCContact instance = entityManager.find(HrCContact.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<HrCContact> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrCContact instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCContact model where model."
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

	@SuppressWarnings("unchecked")
	public List<HrCContact> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCContact instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCContact model";
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
	public HrCContact findByEmpId(Long empId, String enterpriseCode) {
		String sql = "select t.*" + "  from hr_c_contact t\n"
				+ " where t.emp_id = '" + empId + "'\n"
				+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterpriseCode + "'";

		PageObject result = new PageObject();
		List<HrCContact> list = bll.queryByNativeSQL(sql, HrCContact.class);
		String sqlCount = "select count(*)　" + "  from hr_c_contact t\n"
				+ " where t.emp_id = '" + empId + "'\n"
				+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterpriseCode + "'";
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setList(list);
		result.setTotalCount(totalCount);
		if (result.getList() != null) {
			if (result.getList().size() > 0) {
				return (HrCContact) result.getList().get(0);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void importPersonnelFilesContact(List<HrCContact> contactFashionList) {

		Long nextId = bll.getMaxId("hr_c_contact", "contact_id");
		for (HrCContact entity : contactFashionList) {
			String sql = "select t.*\n" + "  from hr_c_contact t\n"
					+ " where t.is_use = 'Y'\n" + "   and t.emp_id = '"
					+ entity.getEmpId() + "'\n";

			List<HrCContact> list = bll.queryByNativeSQL(sql, HrCContact.class);
			if (list != null && list.size() > 0) {
				HrCContact updated = list.get(0);
				updated.setContactCarrier(entity.getContactCarrier());
				updated.setContactTel(entity.getContactTel());
				updated.setContactMobile(entity.getContactMobile());
				updated.setContactEmail(entity.getContactEmail());
				updated.setContactPostalcode(entity.getContactPostalcode());
				updated.setContactAddress(entity.getContactAddress());
				entityManager.merge(updated);
			} else {
				entity.setContactId(nextId++);
				entityManager.persist(entity);
			}

		}

	}

}