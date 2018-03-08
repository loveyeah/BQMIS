package power.ejb.run.securityproduction.danger;

import java.util.List;
import javax.ejb.Remote;

/**
 * L值和B2值填写
 * 
 * @author kzhang2010728
 */
@Remote
public interface SpJDangerDeptValueFacadeRemote {
	/**
	 * 保存SpJDangerDeptValue
	 * @param entity
	 * @return SpJDangerDeptValue
	 */
	public SpJDangerDeptValue save(SpJDangerDeptValue entity);
	/**
	 * 更新SpJDangerDeptValue
	 * @param entity
	 * @return SpJDangerDeptValue
	 */
	public SpJDangerDeptValue update(SpJDangerDeptValue entity);
	/**
	 * 通过录入部门Id查找
	 * @param dangerId
	 * @param enterpriseCode
	 * @return List<SpJDangerDeptValue>
	 */
	public List<SpJDangerDeptValue> findByDangerId(Long dangerId,String enterpriseCode);
}