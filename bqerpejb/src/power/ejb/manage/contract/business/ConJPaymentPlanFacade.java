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

import org.apache.taglibs.standard.lang.jstl.NullLiteral;

import com.sun.org.apache.xerces.internal.impl.dtd.models.DFAContentModel;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.contract.form.PaymentPlanForm;

/**
 * @author slTang
 */
@Stateless
public class ConJPaymentPlanFacade implements ConJPaymentPlanFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public ConJPaymentPlan save(ConJPaymentPlan entity) {
		LogUtil.log("saving ConJPaymentPlan instance", Level.INFO, null);
		try {
			if(entity.getPaymentId()==null){
				entity.setPaymentId(bll.getMaxId("CON_J_PAYMENT_PLAN", "PAYMENT_ID"));
			}
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(ConJPaymentPlan entity) {
		entity.setIsUse("N");
		this.update(entity);
	}

	
	public ConJPaymentPlan update(ConJPaymentPlan entity) {
		LogUtil.log("updating ConJPaymentPlan instance", Level.INFO, null);
		try {
			ConJPaymentPlan result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConJPaymentPlan findById(Long id) {
		LogUtil.log("finding ConJPaymentPlan instance with id: " + id,
				Level.INFO, null);
		try {
			ConJPaymentPlan instance = entityManager.find(
					ConJPaymentPlan.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PaymentPlanForm> findByConId(Long conId) {
		String sql=
//			"select t.payment_id,\n" +
//			"       t.con_id,\n" + 
//			"       t.payment_moment,\n" + 
//			"       t.pay_price,\n" + 
//			"       to_char(t.pay_date,'yyyy-mm-dd'),\n" + 
//			"       t.memo,\n" + 
//			"       t.last_modified_by,\n" + 
//			"       to_char(t.last_modified_date,'yyyy-mm-dd'),\n" + 
//			"       t.enterprise_code,\n" + 
//			"       t.is_use,\n" + 
//			"  (select b.bala_flag from con_j_balance b where b.is_use='Y' and b.con_id = t.con_id),"+
//			"       getworkername(t.last_modified_by),\n" + 
//			"		con.currency_type,\n"+
//			"		con.act_amount\n"+
//			"  from con_j_payment_plan t,CON_J_CONTRACT_INFO con\n" + 
//			" where  t.con_id = ?\n" + 
//			"   and t.is_use = 'Y'\n" + 
//			"   and con.con_id=t.con_id\n"+
//			"   and t.is_use = 'Y'";
			"select t.payment_id,\n" +
			"       t.con_id,\n" + 
			"       t.payment_moment,\n" + 
			"       t.pay_price,\n" + 
			"       to_char(t.pay_date, 'yyyy-mm-dd'),\n" + 
			"       t.memo,\n" + 
			"       t.last_modified_by,\n" + 
			"       to_char(t.last_modified_date, 'yyyy-mm-dd'),\n" + 
			"       t.enterprise_code,\n" + 
			"       t.is_use,\n" + 
			"       (select b.bala_flag\n" + 
			"          from con_j_balance b\n" + 
			"         where b.is_use = 'Y'\n" + 
			"           and b.con_id = t.con_id\n" + 
			"           and b.payment_id = t.payment_id and rownum=1) bala_flag,\n" + 
			"       getworkername(t.last_modified_by),\n" + 
			"       (select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id=con.currency_type) currencyName ,\n" + 
			"       con.act_amount,\n" + 
			"       con.operate_by,\n" + 
			"       getworkername(con.operate_by)\n" + 
			"  from con_j_payment_plan t, CON_J_CONTRACT_INFO con\n" + 
			" where t.con_id = "+conId+"\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and con.con_id = t.con_id\n" + 
			"   and t.is_use = 'Y'";

			List list=bll.queryByNativeSQL(sql);
			List<PaymentPlanForm> arrlist=new ArrayList();
			Iterator it=list.iterator();
			java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.#####");   
			while(it.hasNext()){
				PaymentPlanForm model=new PaymentPlanForm();
				Object[] o=(Object[])it.next();
				if(o[0]!=null)
					model.setPaymentId(Long.parseLong(o[0].toString()));
				if(o[1]!=null)
					model.setConId(Long.parseLong(o[1].toString()));
				if(o[2]!=null)
					model.setPaymentMoment(o[2].toString());
				if(o[3]!=null)
					model.setPayPrice(Double.parseDouble(o[3].toString()));
				if(o[4]!=null)
					model.setPayDate(o[4].toString());
				if(o[5]!=null)
					model.setMemo(o[5].toString());
				if(o[6]!=null)
					model.setLastModifiedBy(o[6].toString());
				if(o[7]!=null)
					model.setLastModifiedDate(o[7].toString());
				if(o[8]!=null)
					model.setEnterpriseCode(o[8].toString());
				if(o[9]!=null)
					model.setIsUse(o[9].toString());
				if(o[10]!=null)
					model.setPayStatu(o[10].toString());
				if(o[11]!=null)
					model.setLastModifyName(o[11].toString());
				if(o[12]!=null)
					model.setCurrencyName(o[12].toString());
				if(o[13]!=null){
					model.setActAmount(Double.parseDouble(o[13].toString()));
					if(o[3]!=null)
//						model.setPayRate(model.getPayPrice()/model.getActAmount());
						model.setPayRate(df.format(model.getPayPrice()/model.getActAmount()));
				}
			if (o[14] !=null) {
				model.setOperateBy(o[14].toString());
			}
			if (o[15] !=null) {
				model.setOperateName(o[15].toString());
			}
				arrlist.add(model);
			}
			return arrlist;
	}

}