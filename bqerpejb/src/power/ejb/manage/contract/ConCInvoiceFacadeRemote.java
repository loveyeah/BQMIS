package power.ejb.manage.contract;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ejb.manage.contract.form.BusinessManangerInfo;

/**
 * 
 * @author sltang
 */
@Remote
public interface ConCInvoiceFacadeRemote {
	/**
	 * 
	 */
	public ConCInvoice save(ConCInvoice entity) throws CodeRepeatException;

	/**
	 * 
	 */
	public void delete(ConCInvoice entity) throws CodeRepeatException;

	/**
	 * 
	 */
	public ConCInvoice update(ConCInvoice entity) throws CodeRepeatException;

	public ConCInvoice findById(Long id);

	/**
	 * 
	 */
	public List<ConCInvoice> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * 
	 */
	public List<BusinessManangerInfo> findAll();
	public BusinessManangerInfo findBusinessManangerInfoById(Long id);
}