package power.web.equ.failure.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.equ.failure.EquCFailureType;
import power.ejb.equ.failure.EquCFailureTypeFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * @author lyu
 * @since 2008-10-31 缺陷类别的相关操作
 */
public class EquFailureTypeAction extends AbstractAction {

	private EquCFailureTypeFacadeRemote remote;
	private EquCFailureType failureType;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public EquFailureTypeAction() {
		remote = (EquCFailureTypeFacadeRemote) factory
				.getFacadeRemote("EquCFailureTypeFacade");
	}

	/**
	 * 增加缺陷类别
	 */
	public void addFailureType() {
		try {
			List<EquCFailureType> modelList = remote
					.findByFailuretypeCode(failureType.getFailuretypeCode());
			if (modelList.size() == 0) {
				failureType.setEnterpriseCode(employee.getEnterpriseCode());
				remote.save(failureType);
				write("{success:true,msg:'增加成功'}");
			} else {
				write("{success:true,msg:'已经存在'}");
			}
		} catch (Exception e) {
			write("{success:false,msg:'保存失败'}");
		}
	}

	/**
	 * 修改缺陷类别
	 */
	public void updateFailureType() {
		EquCFailureType model = remote.findById(failureType.getId());
		model.setFailuretypeCode(failureType.getFailuretypeCode());
		model.setFailuretypeName(failureType.getFailuretypeName());
		model.setFailurePri(failureType.getFailurePri());
		model.setNeedCaclOvertime(failureType.getNeedCaclOvertime());
		model.setFailuretypeDesc(failureType.getFailuretypeDesc());
		model.setIsUse(failureType.getIsUse());
		remote.update(model);
		write("{success:true,msg:'保存成功'}");
	}

	/**
	 * 删除缺陷类别
	 */
	public void deleteFailureType() {
		EquCFailureType model = remote.findById(failureType.getId());
		model.setIsUse("N");
		remote.delete(model);
		write("{success:true,msg:'删除成功'}");
	}

	/**
	 * 根据类型编码查找缺陷类别
	 * 
	 * @throws JSONException
	 */
	public void findFailureTypeById() throws JSONException {
		List<EquCFailureType> list = remote.findByFailuretypeCode(failureType
				.getFailuretypeCode());
		String failureTypeStr = JSONUtil.serialize(list);
		write("{failureTypelist:" + failureTypeStr + "}");
	}

	/**
	 * 根据企业码查找缺陷类别列表
	 * 
	 * @throws Exception
	 */
	public void findFailureTypeList() throws Exception {
		List<EquCFailureType> list = remote.findByIsUse((Object) ("Y"));
		// List<EquCFailureType> list =
		// remote.findAllList(employee.getEnterpriseCode());
		String failureTypeStr = JSONUtil.serialize(list);
		write("{failureTypelist:" + failureTypeStr + "}");
	}

	public EquCFailureType getFailureType() {
		return failureType;
	}

	public void setFailureType(EquCFailureType failureType) {
		this.failureType = failureType;
	}

}
