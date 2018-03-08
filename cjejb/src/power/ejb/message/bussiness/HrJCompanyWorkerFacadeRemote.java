package power.ejb.message.bussiness;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ejb.message.form.WorkerInfo;

/**
 * Remote interface for HrJCompanyWorkerFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJCompanyWorkerFacadeRemote {
	/**
	 * 保存
	 */
	public void save(HrJCompanyWorker entity);
	
	/**
	 * 
	 * @param enterpriseCode 
	 * @param companyCode 
	 * @param workerCodes 
	 */
	public void addWorkersToCompany(String enterpriseCode, String companyCode,String workerCodes);

	/**
	 * Delete a persistent HrJCompanyWorker entity.
	 * 
	 * @param entity
	 *            HrJCompanyWorker entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 *
	 *
	 * 删除联系人
	 */
	public void delete(HrJCompanyWorker entity);

	/**
	 * Persist a previously saved HrJCompanyWorker entity and return it or a
	 * copy of it to the sender. A copy of the HrJCompanyWorker entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJCompanyWorker entity to update
	 * @return HrJCompanyWorker the persisted HrJCompanyWorker entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 * 修改联系人
	 */
	public HrJCompanyWorker update(HrJCompanyWorker entity);

	public HrJCompanyWorker findById(Long id);
	
	public List<WorkerInfo> findByZbbmtxCode(String zbbmtxCode);
	
	public HrJCompanyWorker findAllByCode(String zbbmtxCode,String workerCode);
	
	public HrJCompanyWorker findAllByWorkerCode(String workerCode);
	
}