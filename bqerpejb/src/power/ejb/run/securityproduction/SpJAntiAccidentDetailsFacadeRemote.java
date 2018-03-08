package power.ejb.run.securityproduction;

import java.util.List;
import javax.ejb.Remote;

/**
 * 25项反事故措施明细管理
 * 
 * @author slTang
 */
@Remote
public interface SpJAntiAccidentDetailsFacadeRemote {
	/**
	 * 增加
	 */
	public void save(SpJAntiAccidentDetails entity);

	/**
	 * 删除
	 */
	public void delete(String ids);

	/**
	 * 修改
	 */
	public SpJAntiAccidentDetails update(SpJAntiAccidentDetails entity);

	public SpJAntiAccidentDetails findById(Long id);
	/**
	 * 保存所有的修改
	 * @param addList 新增的25项反事故措施详细记录
	 * @param updateList 修改的25项反事故措施详细记录
	 * @param deleteCode 删除的25项反事故措施详细记录
	 * @param mCode 25项反事故措施编码
	 */
	@SuppressWarnings("unchecked")
	public void saveModified(List addList,List updateList,String deleteCode,String mCode);
	
	/**
	 * 根据反事故措施编码查找详细信息
	 * @param mCode
	 * @return
	 */
	public List<SpJAntiAccidentDetails> findByCode(String mCode);
}