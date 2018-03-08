package power.ejb.commodel;

import java.util.List;
import javax.ejb.Remote;


/**ϵͳ参数配置管理
 * 
 * @author slTang
 */
@Remote
public interface SysCParametersFacadeRemote {
	
//	public void save(SysCParameters entity);
//
//	
//	public void delete(SysCParameters entity);

	
	public SysCParameters update(SysCParameters entity);
	
	/**
	 * 根据参数编号查找参数值
	 * @param pamNo
	 * @param enterpriseCode
	 * @return
	 */
	public String findBypamNo(String pamNo,String enterpriseCode);

//	public SysCParameters findById(String id);
// 
//	
//	public List<SysCParameters> findAll(int... rowStartIdxAndCount);	

}