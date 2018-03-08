package power.web.equ.standardpackage.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.equ.standardpackage.EquCStandardRepairmode;
import power.ejb.equ.standardpackage.EquCStandardRepairmodeFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * @author derek
 * @since 2009-03-16 维修方案
 */
@SuppressWarnings("serial")
public class EquCStandardRepairmodeAction extends AbstractAction {

	private EquCStandardRepairmodeFacadeRemote remote;



	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public EquCStandardRepairmodeAction() {
		remote = (EquCStandardRepairmodeFacadeRemote) factory
				.getFacadeRemote("EquCStandardRepairmodeFacade");
	}

	@SuppressWarnings("unchecked")
	public void saveEquCStandardRepairmode() throws JSONException {
		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<EquCStandardRepairmode> addList = new ArrayList<EquCStandardRepairmode>();
			List<EquCStandardRepairmode> updateList = new ArrayList<EquCStandardRepairmode>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				String id=null;
				String name =null;
				String orderBy=null;
				String memo=null;
				String status=null;
				if(data.get("id")!=null){
					id = data.get("id").toString();
				}
			
				if(data.get("repairmodeName")!=null){
			 name = data.get("repairmodeName").toString();
				}
				if(data.get("orderby")!=null){
					orderBy  = data.get("orderby").toString();
				}
				if(data.get("repairmodeMemo")!=null){
				memo = data.get("repairmodeMemo").toString();
				}
				if(data.get("status")!=null){
				status = data.get("status").toString();
				}
				EquCStandardRepairmode model = new EquCStandardRepairmode();
				// 增加
				if (id==null) {
					model.setRepairmodeName(name);
					if (orderBy!=null&&!orderBy.equals("")) {
						model.setOrderby(Long.parseLong(orderBy));
					}
					model.setRepairmodeMemo(memo);
					model.setEnterprisecode(employee.getEnterpriseCode());
					model.setStatus(status);

					addList.add(model);
				} else {
					model = remote.findById(Long.parseLong(id));

					model.setRepairmodeName(name);
					if (orderBy!=null&&!orderBy.equals("")) {
						model.setOrderby(Long.parseLong(orderBy));
					}
					
					model.setStatus(status);
					
					
					model.setRepairmodeMemo(memo);
					
					updateList.add(model);
				}
			}

		
			if (addList.size() > 0) 				
					remote.save(addList);
		

			if (updateList.size() > 0) 
			
					remote.update(updateList);
		
			if (deleteIds != null && !deleteIds.trim().equals("")) 
				
					remote.delete(deleteIds);
				

			

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	// /**
	// * 增加新记录
	// *
	// */
	// public void saveEquCStandardRepairmode() {
	// request.getParameter("baseInfo.repairmodeMemo");
	//
	// request.getParameter("baseInfo.orderby");
	// baseInfo.setEnterprisecode(employee.getEnterpriseCode());
	// if (remote.save(baseInfo)) {
	// write("{success:true,msg:'增加成功！'}");
	// } else {
	// write("{success:true,msg:'增加失败！'}");
	// }
	//
	// }
	//
	// /**
	// * 变更记录
	// *
	// */
	// public void updateEquCStandardRepairmode() {
	// try {
	// EquCStandardRepairmode model = new EquCStandardRepairmode();
	// model = remote.findById(baseInfo.getId());
	// model.setOrderby(baseInfo.getOrderby());
	// model.setRepairmodeMemo(baseInfo.getRepairmodeMemo());
	// model.setRepairmodeName(baseInfo.getRepairmodeName());
	// remote.update(model);
	// write("{success:true,msg:'修改成功！'}");
	// } catch (Exception e) {
	// write("{success:false,msg:'更新失败! '}");
	// }
	// }
	//
	// /**
	// * 删除单条记录
	// *
	// */
	// public void deleteEquCStandardRepairmode(Long id) {
	// try {
	// remote.delete(id);
	// write("{success:true,msg:'删除成功'}");
	// } catch (Exception e) {
	// write("{success:false,msg:'删除失败'}");
	// }
	// }
	//
	// /**
	// * 批量删除记录
	// *
	// */
	// public void deleteEquCStandardRepairmodes() {
	// try {
	// String id = ids.substring(0, ids.length() - 1);
	// remote.delete(id);
	// write("{success:true,msg:'删除成功'}");
	// } catch (Exception e) {
	// write("{success:false,msg:'删除失败'}");
	// }
	// }

	/**
	 * 批量锁定记录
	 * 
	 */
	public void lockEquCStandardRepairmodes() {
		
		try {
			String ids = request.getParameter("ids");
			String[] str = ids.split(",");
			for(int i=0;i<str.length;i++){
				remote.lock(str[i]);
			}
			write("{success:true,msg:'锁定成功'}");
		} catch (Exception e) {
			write("{success:false,msg:'锁定失败'}");
		}
	}

	/**
	 * 批量解锁记录
	 * 
	 */
	public void unlockEquCStandardRepairmodes() {
		try {
			String ids = request.getParameter("ids");
			String[] str = ids.split(",");
			for(int i=0;i<str.length;i++){
				remote.unlock(str[i]);
			}
			write("{success:true,msg:'解锁成功'}");
		} catch (Exception e) {
			write("{success:false,msg:'解锁失败'}");
		}
	}

	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */

	public void getEquCStandardRepairmodeList() throws JSONException {
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), start,
				limit);
		write(JSONUtil.serialize(obj));
	}

	@SuppressWarnings("unchecked")
	public void getEquCStandardRepairmodeListToUse() throws JSONException {
		PageObject obj = remote.findAlltoUse(employee.getEnterpriseCode(),
				start, limit);
		List<EquCStandardRepairmode> list = obj.getList();
		String str = "[";
		int i = 0;
		for (EquCStandardRepairmode model : list) {
			i++;
			str += "[\"" + model.getRepairmodeName() + "\",\""
					+ model.getRepairmodeCode() + "\"]";
			if (i < list.size()) {
				str += ",";
			}
		}
		str += "]";
		write(str);
	}

	// ******************************************get/set变量方法******************************************




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
