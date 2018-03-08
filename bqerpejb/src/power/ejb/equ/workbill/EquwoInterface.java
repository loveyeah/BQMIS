package power.ejb.equ.workbill;

import javax.ejb.Remote;

/**
 * @author sltang
 *
 */
@Remote
public interface EquwoInterface {
	
	/**
	 * 完成工单时，更新EquJWo实体，删除关联的工作票与关联的领料单
	 * @param EquJWo entity 工单对象
	 * @param String worktickectCodes 工作票编号集
	 * @param String matCodes 领料单编号集
	 */
	public void endEquwo(EquJWo entity,String worktickectCodes,String matCodes);
}
