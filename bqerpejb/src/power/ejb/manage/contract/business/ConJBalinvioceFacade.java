package power.ejb.manage.contract.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;

import power.ejb.hr.LogUtil;
import power.ejb.manage.contract.form.BalinvioceForm;

/**
 * Facade for entity ConJBalinvioce.
 * 
 * @see power.ejb.manage.contract.business.ConJBalinvioce
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConJBalinvioceFacade implements ConJBalinvioceFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public void save(ConJBalinvioce entity) {
		LogUtil.log("saving ConJBalinvioce instance", Level.INFO, null);
		try {
//			if(entity.getInvoiceNo()==null){
//				entity.setInvoiceNo(bll.getMaxId("CON_J_BALINVIOCE", "INVOICE_NO"));
//			}
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void save(List<ConJBalinvioce> addList) {
		if (addList != null && addList.size() > 0) {
			Long invoiceNo = bll.getMaxId("CON_J_BALINVIOCE", "INVOICE_NO");
			int i = 0;
			for (ConJBalinvioce entity : addList) {
				entity.setInvoiceNo(invoiceNo + (i++));
				this.save(entity);
			}
		}
	}
	
	public boolean delete(String ids) {
		try {
			String sql = "UPDATE CON_J_BALINVIOCE t\n"
					+ "   SET t.is_use = 'N'\n" + " WHERE t.INVOICE_NO IN (" + ids + ")";
			bll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}


	public ConJBalinvioce update(ConJBalinvioce entity) {
		LogUtil.log("updating ConJBalinvioce instance", Level.INFO, null);
		try {
			ConJBalinvioce result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void update(List<ConJBalinvioce> updateList) {
		int i;
		for (i = 0; i < updateList.size(); i++) {
			update(updateList.get(i));
		}

	}

	
	public ConJBalinvioce findById(Long id) {
		LogUtil.log("finding ConJBalinvioce instance with id: " + id,
				Level.INFO, null);
		try {
			ConJBalinvioce instance = entityManager.find(ConJBalinvioce.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<BalinvioceForm> findByBalanceId(String enterpriseCode,Long balanceId){
		String sql=
			"select t.invoice_no,\n" +
			"       t.invoice_code,\n" + 
			"       t.invoice_id,\n" + 
			
			"       t.invoice_name,\n" + 
			"       to_char(t.invoice_date,'yyyy-MM-dd hh24:mi:ss'),\n" + 
			"       t.invoice_price,\n" + 
			"       t.drawer_by,\n" + 
			"       t.memo,\n" + 
			"       t.operate_by,\n" + 
			"       getworkername(t.operate_by),\n" + 
			"       getworkername(t.drawer_by),\n" + 
			"       t.balance_id\n" + 
			"  from con_j_balinvioce t\n" + 
			" where t.balance_id = ?\n" + 
			"   and t.enterprise_code = ?"+
			"   and t.is_use='Y'";
		List list=bll.queryByNativeSQL(sql,new Object[]{balanceId,enterpriseCode});
		Iterator it=list.iterator();
		List<BalinvioceForm> arrlist=new ArrayList();
		while(it.hasNext()){
			Object[] o=(Object[])it.next();
			BalinvioceForm model=new BalinvioceForm();
			if(o[0]!=null)
				model.setInvoiceNo(Long.parseLong(o[0].toString()));
			if(o[1]!=null)
				model.setInvoiceCode(o[1].toString());
			if(o[2]!=null)
				model.setInvoiceId(o[2].toString());
			if(o[3]!=null)
				model.setInvoiceName(o[3].toString());
			if(o[4]!=null)
				model.setInvoiceDate(o[4].toString());
			if(o[5]!=null)
				model.setInvoicePrice(Double.valueOf(o[5].toString()));
			if(o[6]!=null)
				model.setDrawerBy(o[6].toString());
			if(o[7]!=null)
				model.setMemo(o[7].toString());
			if(o[8]!=null)
				model.setOperateBy(o[8].toString());
			if(o[9]!=null)
				model.setOperateName(o[9].toString());
			if(o[10]!=null)
				model.setDrawerName(o[10].toString());
			if(o[11]!=null)
				model.setBalanceId(Long.parseLong(o[11].toString()));
			arrlist.add(model);
		}
		return arrlist;
	}
}