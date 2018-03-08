package power.ejb.manage.contract;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**人员签名管理
 * @author slTang
 */
@Remote
public interface ConCSignatoryFacadeRemote {
	/**
	 * 增加
	 */
	public void save(ConCSignatory entity);

	/**
	 * 删除
	 */
	public void delete(ConCSignatory entity);

	/**
	 * 修改
	 */
	public ConCSignatory update(ConCSignatory entity);

	public ConCSignatory findById(Long id);
	
	/**
	 * 取得人员签名列表
	 */
	public PageObject findSignatoryList(String enterpriseCode,final int... rowStartIdxAndCount);
}