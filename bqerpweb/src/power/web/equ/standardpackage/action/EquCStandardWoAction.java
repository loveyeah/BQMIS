package power.web.equ.standardpackage.action;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.standardpackage.EquCStandardWo;
import power.ejb.equ.standardpackage.EquCStandardWoFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class EquCStandardWoAction extends AbstractAction {

	private EquCStandardWoFacadeRemote remote;
	private EquCStandardWo baseInfo;
	private String ids;
	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public EquCStandardWoAction() {
		remote = (EquCStandardWoFacadeRemote) factory
				.getFacadeRemote("EquCStandardWoFacade");
	}

	/**
	 * 增加记录
	 * 
	 */
	public void saveEquCStandardWo() {
		try {

			if (remote.checkJobCode(baseInfo.getJobCode()) == 0) {
				baseInfo.setEnterprisecode(employee.getEnterpriseCode());

				if (remote.save(baseInfo))
					write("{success:true,eMsg:'添加成功'}");
				else
					write("{success:false,eMsg:'添加失败'}");
			} else
			{	write("{success:false,eMsg:'工作指令编码已被占用！'}");}

		} catch (Exception e) {
			write("{success:false,eMsg:'添加失败'}");
		}
	}

	/**
	 * 修改记录
	 * 
	 */
	public void updateEquCStandardWo() {
		try {

			EquCStandardWo model = new EquCStandardWo();
			model = remote.findById(baseInfo.getWoId());
			
	       Long count=		remote.checkJobCode(baseInfo.getJobCode());
			if(!model.getJobCode().equals(baseInfo.getJobCode())&&count>0){
				write("{success:false,msg:'工作指令编码已被占用！'}");
				
			} else
			{
				baseInfo.setEnterprisecode(model.getEnterprisecode());
				if (baseInfo.getOrderby() == null)
					baseInfo.setOrderby(model.getOrderby());
				baseInfo.setIfUse(model.getIfUse());
				baseInfo.setStatus(model.getStatus());
				baseInfo.setWoCode(model.getWoCode());
				remote.update(baseInfo);
				write("{success:true,msg:'更新成功'}");
			}
				

		} catch (Exception e) {
			write("{success:false,msg:'更新失败'}");
		}
	}

	/**
	 * 删除记录
	 * 
	 */
	public void deleteEquCStandardWo() {
		try {
			if (remote.delete(ids))
				write("{success:true,msg:'删除成功'}");
			else
				write("{success:false,msg:'删除失败'}");
		} catch (Exception e) {
			write("{success:false,msg:'删除失败'}");
		}
	}

	/**
	 * 锁定记录
	 * 
	 */
	public void lockEquCStandardWo() {
		try {
			if (remote.lock(ids))
				write("{success:true,msg:'锁定成功'}");
			else
				write("{success:false,msg:'锁定失败'}");
		} catch (Exception e) {
			write("{success:false,msg:'锁定失败'}");
		}
	}

	/**
	 * 解锁记录
	 * 
	 */
	public void unlockEquCStandardWo() {
		try {
			if (remote.unlock(ids))
				write("{success:true,msg:'解锁成功'}");
			else
				write("{success:false,msg:'解锁失败'}");
		} catch (Exception e) {
			write("{success:false,msg:'解锁失败'}");
		}
	}

	/**
	 * 取列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getEquCStandardWoList() throws JSONException {
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), start,
				limit);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * @return the baseInfo
	 */
	public EquCStandardWo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * @param baseInfo
	 *            the baseInfo to set
	 */
	public void setBaseInfo(EquCStandardWo baseInfo) {
		this.baseInfo = baseInfo;
	}

	/**
	 * @return the ids
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * @param ids
	 *            the ids to set
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
