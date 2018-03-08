package power.ejb.equ.base;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for EquCInstallationFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCInstallationFacadeRemote {
	
	/**
	 * 增加一条安装点信息
	 * @param entity
	 * @return
	 */
	public int save(EquCInstallation entity);

    /**
     * 删除一条安装点信息
     * @param installId
     * @return
     */
	public boolean delete(Long installId);

	/**
	 * 修改一条安装点信息
	 * @param entity
	 * @return
	 */
	public boolean update(EquCInstallation entity);

	/**
	 * 通过id获得一条安装点信息
	 * @param id
	 * @return
	 */
	public EquCInstallation findById(Long id);

    /**
     * 获得所有安装点信息列表
     * @param rowStartIdxAndCount
     * @return
     */
	public List<EquCInstallation> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 通过父编码获得安装点列表
	 * @param installCode
	 * @param enterpriseCode
	 * @return
	 */
	public List<EquCInstallation> getListByParent(String installCode,String enterpriseCode);
	
	/**
	 * 通过安装点码获得一条安装点信息
	 * @param installCode
	 * @param enterpriseCode
	 * @return
	 */
	public EquCInstallation findByCode(String installCode,String enterpriseCode);
	
	/**
	 * 判断是否有子节点
	 * @param installCode
	 * @param enterpriseCode
	 * @return
	 */
	public boolean ifHasChild(String installCode,String enterpriseCode);
}