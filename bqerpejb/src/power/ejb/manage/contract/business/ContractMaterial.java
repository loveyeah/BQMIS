package power.ejb.manage.contract.business;
import java.util.List;

import javax.ejb.Remote;
import power.ejb.manage.contract.form.MaterialDetailsForm;

@Remote 
public interface ContractMaterial {
	/**
	 * 根据合同编码查找所有对应关联物资明细
	 * @param contractNo
	 * 		合同编码
	 * @param enterpriseCdoe
	 * 		企业编码	
	 * @return MaterialDetailsForm
	 */
	public List<MaterialDetailsForm> findAllMaterialsByContractNo(String contractNo,String enterpriseCdoe);
	/**
	 *根据合同编码给对应的合同添加采购单(更新合同和采购单的关联)
	 * 
	 * @param purNo
	 * 		采购单编码
	 * @param contractNo
	 * 		合同编码
	 * @param enterpriseCode
	 */
	public void updateContractMaterial(String purNo,String contractNo,String enterpriseCode,String method);
	/**
	 * 根据合同ID查找所有对应关联物资明细
	 * 
	 * @param conId
	 * 		合同ID
	 * @param enterpriseCode
	 * 		企业编码
	 * @return
	 */
	public List<MaterialDetailsForm> findAllMaterialsByConId(Long conId,String enterpriseCode);
	/**
	 * 根据合同ID给对应的合同添加采购单(更新合同和采购单的关联)
	 * @param purNo
	 * 		采购单编码
	 * @param conId
	 * 		合同ID
	 * @param enterpriseCode
	 * 		企业编码
	 */
	public void updateContractMaterialByConId(String purNo,String conId,String enterpriseCode,String method);
}
