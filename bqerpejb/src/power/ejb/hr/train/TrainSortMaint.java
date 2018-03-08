package power.ejb.hr.train;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.hr.form.EmpInfo;
import power.ejb.hr.train.form.OutTrainForm;

/**
 * Remote interface for HrCTrainSortFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface TrainSortMaint {

	/**
	 * 批量修改费用类别
	 * 
	 * @param entity
	 */
	public void saveTrainSort(List<HrCTrainSort> addList,
			List<HrCTrainSort> updateList, String ids);

	/**
	 * 显示费用类别列表信息
	 * 
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findTrainSortList(String enterpriseCode,
			int... rowStartIdxAndCount);

	/**
	 * 职工外出培训登记列表
	 * 
	 * @param trainDate
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findOutTrainList(String trainStartdate,
			String trainEnddate,String certificateStartTime,String certificateEndTime,String personName,String deptName,String certificateType, String enterpriseCode,
			int... rowStartIdxAndCount);

	/**
	 * 增加职工外出培训登记
	 * 
	 * @param entity
	 * @return
	 */
	public HrJOuttrain saveOuttrain(HrJOuttrain entity);

	/**
	 * 修改职工外出培训登记
	 * 
	 * @param entity
	 * @return
	 */
	public HrJOuttrain updateOuttrain(HrJOuttrain entity);

	public HrJOuttrain findById(Long id);

	/**
	 * 获取人员详细信息列表
	 * 
	 * @param workerCode
	 * @return
	 */
	public EmpInfo getEmpInfoInfo(String workerCode);

	/**
	 * 获取员工职务信息列表
	 * 
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findStationIdList(String enterpriseCode,
			int... rowStartIdxAndCount);

	/**
	 * 获取员工职称信息列表
	 * 
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findTechnologyTitlesType(int... rowStartIdxAndCount);

	/**
	 * 获取外出职工修改详细信息
	 */
	public OutTrainForm getOuttrainInfo(Long trainId, String enterpriseCode);

	/**
	 * 批量修改费用明细
	 */
	public void saveOuttrainDetail(List<HrJOuttrainDetail> addList,
			List<HrJOuttrainDetail> updateList, String ids);

	/**
	 * 显示费用明细列表信息
	 */
	public List<Object[]> findOuttrainDetailList(Long trainId,
			String enterpriseCode);

	/**
	 * *******************************证书类别维护 bjxu
	 * 0622********************************************
	 */

	public void saveCertificateType(HrCCertificateType entity);

	public HrCCertificateType findByCertificateTypeId(Long id);

	public void updateCertificateType(HrCCertificateType entity);

	public void deleteCertificateType(HrCCertificateType entity);

	public List<TreeNode> findTreeCertificateTypeList(Long id);
	
	public boolean findByPid(Long pid);
}