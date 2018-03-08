package power.ejb.equ.workbill;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquJWoFacade.
 * 
 * @author slTang
 */
@Remote
public interface EquJWoFacadeRemote {
	/**
	 * 增加一个工单
	 * @throws CodeRepeatException 
	 */
	public String save(EquJWo entity,String failureCode,String stdCode) throws CodeRepeatException;

	/**
	 * 删除一个工单
	 */
	public void delete(EquJWo entity);

	/**
	 * 修改一个工单
	 */
	public EquJWo update(EquJWo entity, String failureCode);
	/**
	 * 查询一条工单
	 * @param woCode
	 * @return
	 */
	public EquJWo update(EquJWo entity);
	public EquJWo findOnebill(String woCode);
/**
 * 查询工单
 * @param woCode
 * @return
 */
	public List<EquJWo>  findbill(String woCode);

	public EquJWo findById(Long id);

	/**
	 * 删除多条工单
	 * 
	 * @param ids
	 *            id集如："1,2,3"
	 * @return true 删除成功，false 删除失败
	 */
	public boolean delMulti(String ids);

	/**
	 * 查询工单信息
	 * 
	 * @param reqBeginTime
	 *            申请开始时间
	 * @param reqEndTime
	 *            申请结束时间
	 * @param ifWorkticket
	 *            是否关联工作票
	 * @param ifMaterials
	 *            是否关联物资
	 * @param workorderType
	 *            类型
	 * @param workorderStatus
	 *            状态 字符集 如："1,2,3"
	 * @param enterprisecode
	 *            企业编码
	 * @param int...
	 *            rowStartIdxAndCount
	 * @return PageObject
	 */
	/**
	 * 根据缺陷编码找标准包
	 * @param kksCode 编号
	 * @return PageObject
	 */
	public PageObject findstdPKageBykks(String kksCode,String queryKey,int... rowStartIdxAndCount);
	
	
	public PageObject findByCondition(String reqBeginTime, String reqEndTime,
			String ifWorkticket, String ifMaterials, String workorderType,
			String workorderStatus, String enterprisecode,
			int... rowStartIdxAndCount);
	/**
	 * 
	 * @param reqBeginTime
	 * @param reqEndTime
	 * @param ifWorkticket
	 * @param ifMaterials
	 * @param workorderType
	 * @param repairDepartment
	 * @param professionCode
	 * @param workorderStatus
	 * @param enterprisecode
	 * @param argFuzzy
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
		public PageObject findByFaWoCode(String reqBeginTime, String reqEndTime,
				String ifWorkticket,String ifMaterials,String workorderType,String repairDepartment,
				String professionCode,String workorderStatus,
				String enterprisecode,String argFuzzy,String woCode,String editWoCode,int... rowStartIdxAndCount)throws ParseException;
	/**
	 * 根据工单编号查找工单
	 * @param woCode 工单编号
	 * @param enterprisecode
	 * @return EquJWo
	 */
	public EquJWo findBywoCode(String woCode,String enterpriseCode);
	/**
	 * 查询工单信息列表
	 * @param reqBeginTime
	 *			申请开始时间
	 * @param reqEndTime
	 * 			申请结束时间
	 * @param workorderStatus
	 * 			工单状态
	 * @param workorderType
	 * 			工单类型
	 * @param professionCode
	 * 			专业编码
	 * @param repairDepartment
	 * 			（工作负责人）检修部门编码
	 * @param ifWorkticket
	 * 			是否关联工作票
	 * @param ifMaterials
	 * 			是否关联领料单
	 * @param enterprisecode
	 * 			企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject FindByMoreCondition(String reqBeginTime,String reqEndTime,
			String workorderStatus,String workorderType,String professionCode,
			String repairDepartment,String ifWorkticket,String ifMaterials,
			String enterprisecode,String argFuzzy,String flag,String entryIds,int... rowStartIdxAndCount);
	/**
	 * 根据关联工作票和关联领料单判断是否能删除该工单
	 * @param woCode
	 *         工单编号
	 * @param enterprisecode
	 *         企业编码
	 * @return boolean
	 */
	public boolean isDeleteWorkbillCheck(String woCode,String enterprisecode);
	/**
	 * 上报
	 * @param busitNo
	 * @param flowCode
	 * @param workerCode
	 * @param actionId
	 */
	public void reportWorkBill(String busitNo, String flowCode, String workerCode,String approveText,Long actionId);
	/**
	 * 审批
	 * @param entryId
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param identify
	 * @param applyId
	 * @param enterpriseCode
	 * @param nextRolePerson 下一步负责人Code
	 */
	public boolean approveWorkBill(String entryId, String workerCode, String actionId, String approveText, String nextRoles, String identify, String applyId, String enterpriseCode,String nextRolePerson);
	/**
	 * 通过缺陷单号判断是否已生成工单
	 * @param failureCode
	 * @param enterprisecode
	 * @return boolean 存在返回false
	 * add by kzhang 20101009
	 */
	public boolean checkWorkBillIsExists(String failureCode,String enterprisecode);
}