package power.ejb.manage.contract.business;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.manage.contract.form.PaymentPlanForm;

/**
 * @author slTang
 */
@Remote
public interface ConJPaymentPlanFacadeRemote {
	/**
	 * 增加付款计划
	 */
	public ConJPaymentPlan save(ConJPaymentPlan entity);

	/**
	 *删除付款计划
	 */
	public void delete(ConJPaymentPlan entity);

	/**
	 * 修改付款计划
	 */
	public ConJPaymentPlan update(ConJPaymentPlan entity);

	public ConJPaymentPlan findById(Long id);
	
	/**
	 * 查找付款计划
	 */
	public List<PaymentPlanForm> findByConId(Long conId);
}