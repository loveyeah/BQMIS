package power.ejb.manage.client;

import java.util.ArrayList;
import java.util.Iterator;
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
import power.ejb.manage.client.form.ConJClientsContactForm;

/**
 * 合作伙伴联系人管理
 * @author YWLiu
 *
 */
@Stateless
public class ConJClientsContactFacade implements ConJClientsContactFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 保存一条合作伙伴联系人信息
	 * @param entity
	 */
	public void save(ConJClientsContact entity) {
		try {
			if(entity.getContactId() == null) {
				entity.setContactId(bll.getMaxId("CON_J_CLIENTS_CONTACT", "CONTACT_ID"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 删除合作伙伴联系人信息
	 * @param contactId 联系人ID
	 */
	public Integer delete(String contactId) {
		try {
			String sql = "delete CON_J_CLIENTS_CONTACT t where t.contact_id in ('" + contactId + "')";
			int result = bll.exeNativeSQL(sql);
			LogUtil.log("delete successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 修改一条合作伙伴联系人信息
	 * @param entity
	 * @return ConJClientsContact
	 */
	public ConJClientsContact update(ConJClientsContact entity) {
		LogUtil.log("updating ConJClientsContact instance", Level.INFO, null);
		try {
			ConJClientsContact result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据联系人ID查询对应的合作伙伴信息
	 * @param id
	 * @return
	 */
	public ConJClientsContact findById(Long id) {
		LogUtil.log("finding ConJClientsContact instance with id: " + id,
				Level.INFO, null);
		try {
			ConJClientsContact instance = entityManager.find(
					ConJClientsContact.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<ConJClientsContact> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding ConJClientsContact instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from ConJClientsContact model where model."
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
	 * 查询合作伙伴联系人信息
	 * @param contactNameOrclientName
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String contactNameOrclientName ,String enterpriseCode, String clientId, final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			if (contactNameOrclientName == null
					|| "".equals(contactNameOrclientName)) {
				contactNameOrclientName = "%";
			}
			String sqlCount = "select count(*) from CON_J_CLIENTS_CONTACT t where t.ENTERPRISE_CODE = '"
				    + enterpriseCode +"'"
					+ " and (t.CONTACT_NAME like '%"
					+ contactNameOrclientName
					+ "%'"
					+ " or GETCLIENTNAME(t.CLIEND_ID) like '%"
					+ contactNameOrclientName
					+ "%')";
			if(clientId != null && !"".equals(clientId)) {
				sqlCount += " and t.CLIEND_ID = '" + clientId + "'";
			}
			Long totalCount = Long.valueOf(bll.getSingal(sqlCount).toString());
			result.setTotalCount(totalCount);
			if (totalCount > 0) {
				String sql = "select t.* , GETCLIENTNAME(t.CLIEND_ID) , GETWORKERNAME(t.last_modified_by) from CON_J_CLIENTS_CONTACT t where t.ENTERPRISE_CODE = '" 
						+ enterpriseCode +"'"
						+ " and (t.CONTACT_NAME like '%"
						+ contactNameOrclientName
						+ "%'"
						+ " or GETCLIENTNAME(t.CLIEND_ID) like '%"
						+ contactNameOrclientName
						+ "%')";
				if(clientId != null && !"".equals(clientId)) {
					sql += " and t.CLIEND_ID = '" + clientId + "'";
				}
				List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
				List<ConJClientsContactForm> arraylist = new ArrayList<ConJClientsContactForm>();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					ConJClientsContactForm contactForm = new ConJClientsContactForm();
					if (data[0] != null) {
						contactForm.setContactId(Long.parseLong(data[0].toString()));
					}
					if(data[1] != null)
						contactForm.setClientId(Long.parseLong(data[1].toString()));
					if(data[2] != null)
						contactForm.setContactName(data[2].toString());
					if(data[3] != null)
						contactForm.setSex(data[3].toString());
					if(data[4] != null)
						contactForm.setAge(data[4].toString());
					if(data[5] != null)
						contactForm.setPosition(data[5].toString());
					if(data[6] != null)
						contactForm.setWorkAge(data[6].toString());
					if(data[7] != null)
						contactForm.setDepartment(data[7].toString());
					if(data[8] != null)
						contactForm.setIdentityCard(data[8].toString());
					if(data[9] != null)
						contactForm.setAddress(data[9].toString());
					if(data[10] != null)
						contactForm.setOfficephone(data[10].toString());
					if(data[11] != null)
						contactForm.setCellphone(data[11].toString());
					if(data[12] != null)
						contactForm.setFax(data[12].toString());
					if(data[13] != null)
						contactForm.setQq(data[13].toString());
					if(data[14] != null)
						contactForm.setEmail(data[14].toString());
					if(data[15] != null)
						contactForm.setMemo(data[15].toString());
					if(data[16] != null)
						contactForm.setLastModifiedBy(data[16].toString());
					if(data[17] != null)
						contactForm.setLastModifiedDate(data[17].toString());
					if(data[18] != null)
					contactForm.setEnterpriseCode(data[18].toString());
					if(data[19] != null)
						contactForm.setClientName(data[19].toString());
					if(data[20] != null)
						contactForm.setLastModifiedName(data[20].toString());
					arraylist.add(contactForm);
				}
				result.setList(arraylist);
			}
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}