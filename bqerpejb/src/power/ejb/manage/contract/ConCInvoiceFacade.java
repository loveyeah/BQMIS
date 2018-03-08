package power.ejb.manage.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.contract.form.BusinessManangerInfo;

/**
 * Facade for entity ConCInvoice.
 * 
 * @see power.ejb.manage.contract.ConCInvoice
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConCInvoiceFacade implements ConCInvoiceFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public ConCInvoice save(ConCInvoice entity) throws CodeRepeatException{
		LogUtil.log("saving ConCInvoice instance", Level.INFO, null);
		try {
			if(!this.checkInvoiceNameForAdd(entity.getInvoiceName(), entity.getEnterpriseCode())){
				if (entity.getInvoiceId() == null) {
					entity
							.setInvoiceId(bll.getMaxId("CON_C_INVOICE",
									"INVOICE_ID"));
				}
				entity.setIsUse("Y");
				entity.setLastModifiedDate(new Date());
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			}
			else{
				throw new CodeRepeatException("发票类型名称不能重复");
			}
		
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(ConCInvoice entity) throws CodeRepeatException{
		entity.setIsUse("N");
		this.update(entity);
	}

	public ConCInvoice update(ConCInvoice entity) throws CodeRepeatException{
		LogUtil.log("updating ConCInvoice instance", Level.INFO, null);
		try {
			if(!this.checkInvoiceNameForAdd(entity.getInvoiceName(), entity.getEnterpriseCode(), entity.getInvoiceId())){
				ConCInvoice result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			}
			else{
				throw new CodeRepeatException("发票类型名称不能重复");
			}
			
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConCInvoice findById(Long id) {
		LogUtil.log("finding ConCInvoice instance with id: " + id, Level.INFO,
				null);
		try {
			ConCInvoice instance = entityManager.find(ConCInvoice.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ConCInvoice> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding ConCInvoice instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from ConCInvoice model where model."
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
	public List<BusinessManangerInfo> findAll() {
		String sql = "select\n" + "t.invoice_id,\n" + "t.invoice_code,\n"
				+ "t.invoice_name,\n" + "t.tax,\n" + "t.memo,\n"
				+ "t.last_modified_by,\n"
				+ "getworkername(t.last_modified_by),\n"
				+ "to_char(t.last_modified_date,'yyyy-mm-dd'),\n"
				+ "t.enterprise_code,\n" + "t.is_use\n" + "\n"
				+ "from CON_C_INVOICE t\n" + "where t.is_use='Y'";
		List list = bll.queryByNativeSQL(sql);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] ob = (Object[]) it.next();
			BusinessManangerInfo bmi = new BusinessManangerInfo();
			bmi.setInvoiceId(Long.parseLong(ob[0].toString()));
			if (ob[1] != null)
				bmi.setInvoiceCode(ob[1].toString());
			if (ob[2] != null)
				bmi.setInvoiceName(ob[2].toString());
			if (ob[3] != null)
				bmi.setTax(Double.parseDouble(ob[3].toString()));
			if (ob[4] != null)
				bmi.setMemo(ob[4].toString());
			if (ob[5] != null)
				bmi.setLastModifiedBy(ob[5].toString());
			if (ob[6] != null)
				bmi.setLastModifiedName(ob[6].toString());
			if (ob[7] != null)
				bmi.setLastModifiedDate(ob[7].toString());
			if (ob[8] != null)
				bmi.setEnterpriseCode(ob[8].toString());
			if (ob[9] != null)
				bmi.setIsUse(ob[9].toString());
			arrlist.add(bmi);
		}
		return arrlist;
	}

	public BusinessManangerInfo findBusinessManangerInfoById(Long id) {
		String sql = "select\n" + "t.invoice_id,\n" + "t.invoice_code,\n"
				+ "t.invoice_name,\n" + "t.tax,\n" + "t.memo,\n"
				+ "t.last_modified_by,\n"
				+ "getworkername(t.last_modified_by),\n"
				+ "to_char(t.last_modified_date,'yyyy-mm-dd'),\n"
				+ "t.enterprise_code,\n" + "t.is_use\n" + "\n"
				+ "from CON_C_INVOICE t\n" + "where t.invoice_id='" + id + "'";
		Object[] ob = (Object[]) bll.getSingal(sql);

		BusinessManangerInfo bmi = new BusinessManangerInfo();
		bmi.setInvoiceId(Long.parseLong(ob[0].toString()));
		if (ob[1] != null)
			bmi.setInvoiceCode(ob[1].toString());
		if (ob[2] != null)
			bmi.setIsUse(ob[2].toString());
		if (ob[3] != null)
			bmi.setTax(Double.parseDouble(ob[3].toString()));
		if (ob[4] != null)
			bmi.setMemo(ob[4].toString());
		if (ob[5] != null)
			bmi.setLastModifiedBy(ob[5].toString());
		if (ob[6] != null)
			bmi.setInvoiceName(ob[6].toString());
		if (ob[7] != null)
			bmi.setLastModifiedDate(ob[7].toString());
		if (ob[8] != null)
			bmi.setEnterpriseCode(ob[8].toString());
		if (ob[9] != null)
			bmi.setIsUse(ob[9].toString());

		return bmi;
	}
	 
	public boolean checkInvoiceNameForAdd(String invoiceName,String enterpriseCode,Long... invoiceId){
		
		boolean isSame = false;
		String sql=
			"select count(*) from con_c_invoice t\n" +
			"where t.invoice_name='"+invoiceName+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";
	    if(invoiceId !=null&& invoiceId.length>0){
	    	sql += "  and t.invoice_id <> " + invoiceId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
}