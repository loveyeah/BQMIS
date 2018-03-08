package power.web.equ.standardpackage.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.standardpackage.EquCStandardEqu;
import power.ejb.equ.standardpackage.EquCStandardEquFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquCStandardEquAction extends AbstractAction {

	private EquCStandardEquFacadeRemote remote;

	private String ids;

	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public EquCStandardEquAction() {
		remote = (EquCStandardEquFacadeRemote) factory
				.getFacadeRemote("EquCStandardEquFacade");
	}

	/**
	 * 增加新记录
	 * 
	 */
	public void saveEquCStandardEqu() {
		try {

			EquCStandardEqu baseInfo = new EquCStandardEqu();
			String str = request.getParameter("ids2");
			String[] ids3 = str.split(",");
			String woCode = request.getParameter("woCode");
			int i;

			for (i = 0; i < ids3.length; i++) {

				baseInfo.setEnterprisecode(employee.getEnterpriseCode());

				baseInfo.setWoCode(woCode);
				baseInfo.setKksCode(ids3[i]);

				remote.save(baseInfo);

			}

			if (remote.save(baseInfo))
				write("{success:true,msg:'添加成功'}");
			else
				write("{success:false,msg:'添加失败'}");

		} catch (Exception e) {
			write("{success:false,msg:'添加失败'}");
		}
	}

	/**
	 * 变更记录
	 * 
	 */
	// public void updateEquCStandardEqu() {
	// try {
	// EquCStandardEqu model = new EquCStandardEqu();
	// model = remote.findById(baseInfo.getId());
	// baseInfo.setIfUse(model.getIfUse());
	// baseInfo.setEnterprisecode(model.getEnterprisecode());
	// if (baseInfo.getOrderby() == null)
	// baseInfo.setOrderby(model.getOrderby());
	// baseInfo.setStatus(model.getStatus());
	// baseInfo.setWoCode(model.getWoCode());
	// remote.update(baseInfo);
	// write("{success:true,msg:'更新成功'}");
	// } catch (Exception e) {
	// write("{success:false,msg:'更新失败'}");
	// }
	// }
	/**
	 * 删除记录
	 * 
	 */
	public void deleteEquCStandardEqu() {
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
	public void lockEquCStandardEqu() {
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
	public void unlockEquCStandardEqu() {
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
	 */
	public void getEquCStandardEquList() throws JSONException {
		String woCode = request.getParameter("woCode");
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), woCode,
				start, limit);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 取用列表
	 * 
	 */
	// public void getEquCStandardEquListToUse() throws JSONException {
	// String kksCode = request.getParameter("kksCode");
	// List<EquCStandardEqu> list = remote.findToUse(employee
	// .getEnterpriseCode(), kksCode);
	// write(JSONUtil.serialize(list));
	// }
	/**
	 * @return the baseInfo
	 */

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
