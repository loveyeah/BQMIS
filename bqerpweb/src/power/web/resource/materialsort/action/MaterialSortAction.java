/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.materialsort.action;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvCMaterialClass;
import power.ejb.resource.InvCMaterialClassFacadeRemote;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 物料分类维护
 * 
 * @author chenshoujiang
 * @version 1.0
 */
public class MaterialSortAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 树的节点 */
	private String node;
	/** 物料分类Remote */
	private InvCMaterialClassFacadeRemote materialClassRemote;

	/**
	 * 构造函数
	 */
	public MaterialSortAction() {
		// 物料分类Remote
		materialClassRemote = (InvCMaterialClassFacadeRemote) factory
				.getFacadeRemote("InvCMaterialClassFacade");
	}
	
	/**
	 * 查找物料分类列表
	 * modify by fyyang 090623 
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	public void getMaterialClass() throws JSONException {
		// 创建一个对象
		PageObject object = new PageObject();
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("[");
		// 初始化
		String icon = "";
		// 通过父编码找到子节点的信息
		object = materialClassRemote
			.findByParentClassNo(node, employee.getEnterpriseCode());
		// 获取信息
		List<InvCMaterialClass> listMaterialClass = object.getList();
		// 如果list非空，就执行操作
		List<TreeNode> arrayList=new ArrayList();
		if (listMaterialClass != null && listMaterialClass.size() > 0) {
			
			icon = "folder";
			// 将子节点的内容再到JSPNStr中
			for (int i = 0; i < listMaterialClass.size(); i++) {
				Boolean leafFlag = true;
				InvCMaterialClass materialClass = listMaterialClass.get(i);
				// 如果该子节点有子节点的话，就设置该结点为非叶子结点
				if(materialClassRemote.findByParentClassNo(materialClass.getClassNo(), 
						employee.getEnterpriseCode()).getList() != null) {
					// 如果该子节点的子节点非空
					if(materialClassRemote.findByParentClassNo(materialClass.getClassNo(), 
							employee.getEnterpriseCode()).getList().size() > 0) {
						// 并且大小大于0 ，这是改为非叶子结点
						leafFlag = false;
					}
				}
				TreeNode node=new TreeNode();
				node.setText(materialClass.getClassName()+"("+materialClass.getClassNo()+")");// modify by ywliu 2009/6/26 在类别名称后加上类别编码
				node.setId(materialClass.getClassNo());
				node.setLeaf(leafFlag);
				node.setCls(icon);
				node.setCode(materialClass.getMaertialClassId()+"");
				arrayList.add(node);
//				JSONStr.append("{\"text\":\"" + materialClass.getClassName()
//						+ "\",\"id\":\"" + materialClass.getClassNo()
//						+ "\",\"serial\":\"" + materialClass.getMaertialClassId()
//						+ "\",\"leaf\":" + leafFlag + ",\"cls\":\"" + icon
//						+ "\"},");
			}
		}
//		// 如果大于0，就删除最后一个","
//		if (JSONStr.length() > 1) {
//			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
//		}
//		JSONStr.append("]");
	//	write(JSONStr.toString());
		write(JSONUtil.serialize(arrayList));
	}

	/**
	 * 查找父编码和父名称
	 */
	@SuppressWarnings("unchecked")
	public void getFatherCodeName() {
		// 编码
		String code = request.getParameter("code");
		// 通过编码，查询父编码和父名称
		List<InvCMaterialClass> list = materialClassRemote.findByParentCodeName(code, employee.getEnterpriseCode());
		StringBuilder sb = new StringBuilder();
		// 如果查询的数据非空，并且大小大于0
		if (list != null && list.size() > 0) {
			String fatherCode = list.get(0).getClassNo();
			String fatherName = list.get(0).getClassName();
			// 返回前台信息
			sb.append("{").append("fatherCode").append(":\"").append(fatherCode)
			.append("\",").append("fatherName").append(":\"").append(
					fatherName).append("\"}");
		}else {
			// 否则返回空信息
			sb.append("{").append("fatherCode").append(":\"")
			.append("\",").append("fatherName").append(":\"").append("\"}");
		}
		// 返回给前台
		write(sb.toString());
	}
	
	/**
	 * 删除物料分类信息
	 * modify by fyyang 091123 此分类下有物资的则不能删除
	 * @throws CodeRepeatException 
	 */
	@SuppressWarnings("unchecked")
	public void deleteMaterialTypeSort() throws CodeRepeatException{
		try {
			// 新建一个bean
			InvCMaterialClass entity = new InvCMaterialClass();
			// 流水号
			String serial = request.getParameter("serial");
			entity = materialClassRemote.findById(Long.parseLong(serial));
			InvCMaterialFacadeRemote materialRemote=(InvCMaterialFacadeRemote)factory.getFacadeRemote("InvCMaterialFacade");
			 List<InvCMaterial> materialList=materialRemote.findAllChildrenNode(entity.getClassNo(), employee.getEnterpriseCode());
			 if(materialList!=null&&materialList.size()>0)
			 {
				 write("NO");
			 }
			 else
			 {
			
			// 通过主键查找
			List<InvCMaterialClass> list = materialClassRemote.findAllChildrenNode(entity.getClassNo(),
					 	employee.getEnterpriseCode());
			InvCMaterialClass model = new InvCMaterialClass();
			if (list != null && list.size() > 0) {
				for(int i = 0; i < list.size(); i++) {
					model = (InvCMaterialClass)list.get(i);
					// 设置上次修改人
					model.setLastModifiedBy(employee.getWorkerCode());
					// 删除该结点
					materialClassRemote.delete(model);
				}
			}
			write("true");
			 }
		} catch (Exception e) {
			write("false");
		}

	}
	
	/**
	 *  check编码是否唯一
	 */
	public void checkCode() {
		// 编码
		String code = request.getParameter("code");
		// 如果不唯一，返回true，否则返回false
		Boolean flag = materialClassRemote.checkClassNo(code,employee.getEnterpriseCode());
		if(flag) {
			// 如果不唯一
			write("true");
		} else {
			// 如果唯一
			write("false");
		}
	}
	
	/**
	 * 保存物料分类信息
	 */
	public void saveMaterialType() {
		try {
			// 新建一个bean
			InvCMaterialClass entity = new InvCMaterialClass();
			// 父编码
			String fatherCode = request.getParameter("fatherCode");
			// 编码
			String code = request.getParameter("code");
			// 名称
			String name = request.getParameter("name");
			// 设置父编码
			entity.setParentClassNo(fatherCode);
			// 设置编码
			//entity.setClassNo(code);  modify by drdu 090626
			if(fatherCode != null&&!fatherCode.equals("-1"))
			{
			  entity.setClassNo(fatherCode+"-"+code);
			}
			else{
				entity.setClassNo(code);
			}
			// 设置名称
			entity.setClassName(name);
			// 设置上次修改人
			entity.setLastModifiedBy(employee.getWorkerCode());
			// 设置企业编码
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			materialClassRemote.save(entity);
			write("true");
		} catch (Exception e) {
			write("false");
		}
	}
	
	/**
	 * 修改物料分类信息
	 * @return
	 */
	public void modifyMaterialType() {
		try {
			// 流水号
			String serial = request.getParameter("serial");
			// 编码
			String code = request.getParameter("code");
			// 名称
			String name = request.getParameter("name");
			// 通过主键查找
			InvCMaterialClass entity = materialClassRemote.findById(Long.parseLong(serial));
			// 设置编码
			entity.setClassNo(code);
			// 设置名称
			entity.setClassName(name);
			// 设置上次修改人
			entity.setLastModifiedBy(employee.getWorkerCode());
			// 更新数据
			materialClassRemote.update(entity);
			write("true");
		} catch (Exception e) {
			write("false");
		}
	}

	/**
	 * 获得树的节点
	 * 
	 * @return 树的节点
	 */
	public String getNode() {
		return node;
	}

	/**
	 * 设置树的节点
	 * 
	 * @param node
	 *            树的节点
	 */
	public void setNode(String node) {
		this.node = node;
	}
}