package power.ejb.manage.contract.business;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.manage.contract.form.ConModifyForm;

@Remote
public interface ConJModifyFacadeRemote {
	/**
	 * 增加合同变更
	 */
	public ConJModify save(ConJModify entity);

	/**
	 * 删除合同变更
	 */
	public void delete(ConJModify entity);

	/**
	 * 变更合同修改
	 */
	public ConJModify update(ConJModify entity);

	public ConJModify findById(Long id);

	/**
	 * 根据合同变更编号查找一条记录
	 * @param conmodifyNo
	 * @return
	 */
	public ConModifyForm findConModifyModel(Long conmodifyId);
	/**
	 * 查找变更合同
	 * @param enterpriseCode
	 * @param sDate
	 * @param eDate
	 * @param fuzzy
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findConModifyList(String workCode,Long conTypeId,String enterpriseCode, String sDate,String eDate,String fuzzy,final int... rowStartIdxAndCount);
	/**
	 * 查询变更合同审批列表
	 * @param enterpriseCode 企业编码
	 * @param sDate 会签开始时间
	 * @param eDate 会签结束时间
	 * @param status 会签状态（approve:待签， ok ：已签）
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findConModifyApproveList(Long conTypeId ,String enterpriseCode, String sDate,String eDate,String status,String entryIds,final int... rowStartIdxAndCount);
	
	 //取审批记录
	public List<ConApproveForm>	getApproveList(Long id);
	
	/**
	 * 取变更ID
	 * @param conId
	 * @return
	 */
	public List<ConJModify> getconModifyId(String conId);
}