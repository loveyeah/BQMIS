/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.purchaser.action;

import java.util.List;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvCMaterialClass;
import power.ejb.resource.InvCMaterialClassFacadeRemote;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.ejb.resource.PurCBuyer;
import power.ejb.resource.PurCBuyerFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 采购员维护
 * 
 * @author jincong
 * @version 1.0
 */
public class PurchaserMaintAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 树的节点 */
	private String node;
	/** 种类 */
	private String type;
	/** 采购员编码 */
	private String buyer;
	/** 采购员维护Remote */
	private PurCBuyerFacadeRemote buyerRemote;
	/** 物料分类Remote */
	private InvCMaterialClassFacadeRemote materialClassRemote;
	/** 物料Remote */
	private InvCMaterialFacadeRemote materialRemote;
	/** 人员信息共通接口 */
	private BaseDataManager personInfo;

	/**
	 * 构造函数
	 */
	public PurchaserMaintAction() {
		// 采购员维护Remote
		buyerRemote = (PurCBuyerFacadeRemote) factory
				.getFacadeRemote("PurCBuyerFacade");
		// 物料分类Remote
		materialClassRemote = (InvCMaterialClassFacadeRemote) factory
				.getFacadeRemote("InvCMaterialClassFacade");
		// 物料Remote
		materialRemote = (InvCMaterialFacadeRemote) factory
				.getFacadeRemote("InvCMaterialFacade");
		// 人员信息共通接口
		personInfo = (BaseDataManager) factory
				.getFacadeRemote("BaseDataManagerImpl");
	}

	/**
	 * 根据当前树的节点,从物料分类表中查询对应的子节点.
	 * 如果有,则生成子节点.
	 * 如果没有,则用对应的流水号从物料主文件中查询对应的物料信息,有则生成子节点.
	 */
	@SuppressWarnings("unchecked")
	public void getMaterialClassList() {
//		PageObject object = new PageObject();
//		StringBuffer JSONStr = new StringBuffer();
//		JSONStr.append("[");
//		// 叶子或者根
//		String icon = "";
//		// 根据当前节点编码查询所有子节点
//		object = materialClassRemote
//			.findByParentClassNo(node, employee.getEnterpriseCode());
//		List<InvCMaterialClass> listMaterialClass = object.getList();
//		// 如果有子节点
//		if (listMaterialClass != null && listMaterialClass.size() > 0) {
//			// 设置属性为根
//			icon = "folder";
//			// 生成输出字符串
//			for (int i = 0; i < listMaterialClass.size(); i++) {
//				InvCMaterialClass materialClass = listMaterialClass.get(i);
//				JSONStr.append("{\"text\":\"" + materialClass.getClassName()
//						+ "\",\"id\":\"" + materialClass.getClassNo()
//						+ "\",\"leaf\":" + false
//						+ ",\"cls\":\"" + icon
//						+ "\",\"kind\":\"" + "Y"
//						+ "\"},");
//			}
//		// 如果没有子节点，则从物料主文件中查询对应的物料信息
//		} else {
//			// 根据当前node,从物料主文件中查询物料
//			String strReturn = findLeaf(node);
//			if(strReturn != "" && (strReturn.length() > 0)) {
//				JSONStr.append(strReturn);
//			}
//		}
//		String str = findLeaf(node);
//		if(str != "" && (str.length() > 0)) {
//			JSONStr.append(str);
//		}
//		// 判断是否有结果
//		if (JSONStr.length() > 1) {
//			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
//		}
//		JSONStr.append("]");
//		// 输出
//		write(JSONStr.toString());
		
		//modify by fyyang 090511
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
				JSONStr.append("{\"text\":\"" + materialClass.getClassName()
						+ "\",\"id\":\"" + materialClass.getClassNo()
						+ "\",\"serial\":\"" + materialClass.getMaertialClassId()
						+ "\",\"leaf\":" + leafFlag + ",\"cls\":\"" + icon
						+ "\"},");
			}
		}
		// 如果大于0，就删除最后一个","
		if (JSONStr.length() > 1) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
		JSONStr.append("]");
		write(JSONStr.toString());
	}

	/**
	 * 根据当前node,从物料主文件中查询物料
	 * 
	 * @param strNode 树的节点
	 * @return 结果
	 */
	@SuppressWarnings("unchecked")
	public String findLeaf(String strNode) {
		String icon = "";
		StringBuffer JSONStr = new StringBuffer();
		PageObject object = new PageObject();
		// 根据节点编码查询节点对应的流水号
		object = materialClassRemote.findByClassNo(strNode, employee.getEnterpriseCode());
		if(object  != null && (object.getList().size() > 0)) {
			InvCMaterialClass materialClass = (InvCMaterialClass)object.getList().get(0);
			// 设置节点的值
			setNode(String.valueOf(materialClass.getMaertialClassId()));
			// 根据流水号查询物料主文件中对应的物料信息
			object =  materialRemote
				.findByMaertialClassId(node, employee.getEnterpriseCode());
			List<InvCMaterial> listMaterial =object.getList();
			// 设置属性为叶子
			icon = "file";
			// 生成输出字符串
			for (int i = 0; i < listMaterial.size(); i++) {
				InvCMaterial material = listMaterial.get(i);
				JSONStr.append("{\"text\":\"" + material.getMaterialName()
								+ "\",\"id\":\"" + material.getMaterialNo()
								+ "\",\"leaf\":" + true
								+ ",\"cls\":\"" + icon
								+ "\",\"kind\":\"" + "N"
								+ "\"},");
			}
		}
		return JSONStr.toString();
	}
	
	/**
	 * 根据是否使用查询采购员列表
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getPurchaserList() throws JSONException {
		PageObject object = new PageObject();
		// 查询
		object = buyerRemote.findByIsUse(Constants.IS_USE_Y,
				employee.getEnterpriseCode());
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("{\"list\":[");
		List<PurCBuyer> list = object.getList();
		for(int i = 0; i < list.size(); i++) {
			PurCBuyer purCBuyer = list.get(i);
			Employee emp = personInfo.getEmployeeInfo(purCBuyer.getBuyer());
			if(emp != null) {
				// 人员编码
				JSONStr.append("{\"buyer\":\"" + purCBuyer.getBuyer());
				// 人员姓名
				String strChsName = emp.getWorkerName();
				if(strChsName != null) {
					JSONStr.append("\",\"buyerName\":\"" + strChsName);
				} else {
					JSONStr.append("\",\"buyerName\":\" ");
				}
				// 手机号
				String strMobilePhone = emp.getMobilePhoneNo();
				if(strMobilePhone != null) {
					JSONStr.append("\",\"mobileNo\":\"" + strMobilePhone);
				} else {
					JSONStr.append("\",\"mobileNo\":\" ");
				}
				// 固定电话
				String strFamilyTel = emp.getImmobilePhoneNo();
				if(strFamilyTel != null) {
					JSONStr.append("\",\"telephone\":\"" + strFamilyTel);
				} else {
					JSONStr.append("\",\"telephone\":\" ");
				}
				// 传真
				String strFax = emp.getElectrographNo();
				if(strFax != null) {
					JSONStr.append("\",\"fax\":\"" + strFax);
				} else {
					JSONStr.append("\",\"fax\":\" ");
				}
				// 标识
				JSONStr.append("\",\"flag\":\"Y");
				JSONStr.append("\"},");
			}
			
		}
		// 判断是否有结果
		if (JSONStr.length() > 9) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
		JSONStr.append("]}");
		// 输出
		write(JSONStr.toString());
	}
	
	/**
	 * 查询人员信息
	 * 
	 * @throws JSONException 
	 */
	public void getPersonInfoBuyer() throws JSONException {
		// 取得查询参数
		String strBuyer = request.getParameter("buyer");
		Employee emp = personInfo.getEmployeeInfo(strBuyer);
		write(JSONUtil.serialize(emp));
	}
	
	/**
	 * 根据采购员编码,从采购员维护表中查询对应的信息,取得物料分类标识.
	 * 如果是物料分类,用对应的物料编码或物料分类编码从物料分类表中查询信息.
	 * 如果是物料，用对应的物料编码或物料分类编码从物料主文件表中查询信息.
	 */
	@SuppressWarnings("unchecked")
	public void getSelectedMaterialList() {
		PageObject object = new PageObject();
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("{\"list\":[");
		// 根据采购员编码查询出采购员信息
		object = buyerRemote.findByBuyer(buyer, employee.getEnterpriseCode());
		List<PurCBuyer> list = object.getList();
		// 循环处理
		for(int i = 0; i < list.size(); i++) {
			PurCBuyer purCBuyer = (PurCBuyer)list.get(i);
			// 取得对应的物料分类标识
			String strIsMaterialClass = purCBuyer.getIsMaterialClass();
			// 如果是物料分类
			if(Constants.FLAG_Y.equals(strIsMaterialClass)) {
				// 根据物料编码或物料分类编码,从物料分类表中查询对应的物料分类信息
				object = materialClassRemote.findByClassNo(
						purCBuyer.getMaterialOrClassNo(), employee.getEnterpriseCode());
				// 如果有值
				if((object != null) && (object.getList().size() > 0)) {
					// 生成输出字符串
					InvCMaterialClass materialClass = (InvCMaterialClass)object.getList().get(0);
					JSONStr.append("{\"name\":\"" + materialClass.getClassName()
							+ "\",\"no\":\"" + materialClass.getClassNo()
							+ "\",\"kind\":\"" + "Y"
							+ "\",\"id\":\"" + " "
							+ "\"},");
				}
			// 如果是物料
			} else if(Constants.FLAG_N.equals(strIsMaterialClass)) {
				// 根据物料编码或物料分类编码,从物料主文件中查询对应的物料信息
				object = materialRemote.findByMaterialNo(
						purCBuyer.getMaterialOrClassNo(), employee.getEnterpriseCode());
				// 如果有值
				if((object != null) && (object.getList().size() > 0)) {
					// 生成输出字符串
					for(int j = 0; j < object.getList().size(); j++) {
						InvCMaterial material = (InvCMaterial)object.getList().get(j);
						JSONStr.append("{\"name\":\"" + material.getMaterialName()
								+ "\",\"no\":\"" + material.getMaterialNo()
								+ "\",\"kind\":\"" + "N"
								+ "\",\"id\":\"" + " "
								+ "\"},");
					}
				}
			}
		}
		// 判断是否有结果
		if (JSONStr.length() > 9) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
		JSONStr.append("]}");
		// 输出
		write(JSONStr.toString());
	}
	
	/**
	 * 点击树的节点,用当前节点进行查询,获得当前节点下的所有叶子节点.
	 */
	@SuppressWarnings("unchecked")
	public void getWaitSelectMaterialList() {
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("{\"list\":[");
		List<InvCMaterial> list = materialRemote.findAllChildrenNode(node, employee.getEnterpriseCode());
		if(list!= null && (list.size() > 0)) {
			for (int i = 0; i < list.size(); i++) {
				InvCMaterial material = list.get(i);
				JSONStr.append("{\"name\":\"" + material.getMaterialName()
						+ "\",\"no\":\"" + material.getMaterialNo()
						+ "\",\"kind\":\"" + "N"
						+ "\",\"id\":\"" + " "
						+ "\"},");
			}
		}
		// 如果有值
		if (JSONStr.length() > 9) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
		JSONStr.append("]}");
		// 输出
		write(JSONStr.toString());
	}
	
	/**
	 * 逻辑删除一个采购员
	 */
	public void deletePurchaserByBuyer() {
		deleteByBuyer(buyer);	
	}
	
	/**
	 * 逻辑删除一个采购员
	 * 
	 * @param strBuyer 人员编码
	 */
	@SuppressWarnings("unchecked")
	public void deleteByBuyer(String strBuyer) {
		PageObject object = new PageObject();
		// 根据人员编码取出所有数据
		object = buyerRemote.findByBuyer(strBuyer, employee.getEnterpriseCode());
		// 循环处理
		if(object != null && object.getList().size() > 0) {
			List<PurCBuyer> list = object.getList();
			for(int i = 0; i < list.size(); i++) {
				PurCBuyer purCBuyer = list.get(i);
				purCBuyer.setIsUse(Constants.IS_USE_N);
				purCBuyer.setLastModifiedBy(employee.getWorkerCode());
				buyerRemote.update(purCBuyer);
			}
			
		}
	}
	
	/**
	 * 根据流水号删除一条数据
	 * 
	 * @param strsNumber 画面数据和数据库数据比较结果
	 * @param strsIdsOld 流水号
	 */
	public void deleteById(String[] strsNumber, String[] strsIdsOld) {
		// 画面上没有但数据库中有的，要删除
		for(int i = 0; i < strsNumber.length;i++) {
			int index = Integer.parseInt(strsNumber[i]);
			// 用流水号取出对应数据
			PurCBuyer purCBuyer = buyerRemote.findById(Long.parseLong(strsIdsOld[index]));
			if(purCBuyer != null) {
				// 更新
				purCBuyer.setIsUse(Constants.IS_USE_N);
				purCBuyer.setLastModifiedBy(employee.getWorkerCode());
				buyerRemote.update(purCBuyer);
			}
		}
	}
	
	/**
	 * 保存画面上的数据
	 */
	public void saveBuyerRecords() {
		// 取得画面上数据的字符串形式
		String strParameter = request.getParameter("string");
		// 按画面上的行分割
		String[] strsBuyer = strParameter.split(";");
		// 如果只有一行
		if((strsBuyer.length == 0) && strParameter.length() > 0) {
			strsBuyer = new String[1];
			strsBuyer[0] = strParameter;
		}
		// 循环处理
		for(int i = 0; i < strsBuyer.length; i++) {
			// 把行数据按画面上的列分割
			String[] strsBuyerInfo = strsBuyer[i].split(",");
			// 人员编码
			String strBuyer = strsBuyerInfo[0];
			// 采购员姓名
			String strName = strsBuyerInfo[1];
			// 标识
			String strFlag = strsBuyerInfo[2];
			// 物料编码
			String[] strsNosNew = strsBuyerInfo[4].split(":");
			// 物料名称
			String[] strsNamesNew = strsBuyerInfo[3].split(":");
			// 物料分类
			String[] strsKindsNew = strsBuyerInfo[5].split(":");
			// 如果此数据是从数据库中取出的
			if(Constants.FLAG_Y.equals(strFlag)) {
				// 如果此采购员已经没有对应的物料
				if(strsNamesNew.length < 1 || " ".equals(strsNamesNew[0])) {
					// 从数据库中删除此采购员
					deleteByBuyer(strBuyer);	
				} else {
					// 更新
					update(strBuyer, strName, strsNosNew, strsKindsNew);
				}
			// 如果此数据是画面增加的
			} else if(Constants.FLAG_N.equals(strFlag)) {
				addBuyer(strBuyer, strName, strsNosNew, strsKindsNew);
			}
		}
	}
	
	/**
	 * 更新
	 * 
	 * @param strBuyer 人员编码
	 * @param strName 采购员姓名
	 * @param strsNosNew 物料编码
	 * @param strsKindsNew 物料分类
	 */
	public void update(String strBuyer, String strName, String[] strsNosNew, String[] strsKindsNew) {
		PageObject object = new PageObject();
		// 根据人员编码取出对应的数据
		object = buyerRemote.findByBuyer(strBuyer, employee.getEnterpriseCode());
		int intCount = object.getList().size();
		// 数据库中的物料编码
		String[] strsNosOld = new String[intCount];
		// 数据库中的流水号
		String[] strsIdsOld = new String[intCount];
		if(object != null && intCount > 0) {
			// 生成
			for(int i = 0; i < intCount; i++) {
				PurCBuyer purCBuyer = (PurCBuyer)object.getList().get(i);
				strsNosOld[i] = purCBuyer.getMaterialOrClassNo();
				strsIdsOld[i] = String.valueOf(purCBuyer.getId());
			}
		}
		// 用画面上的数据和数据库中的数据进行比较,
		// 画面上有的,数据中没有,则要增加
		String intFlag = compareArray(strsNosNew, strsNosOld);
		if(!Constants.BLANK_STRING.equals(intFlag)) {
			String[] strsNumber = intFlag.split(",");
			addRecord(strBuyer, strName, strsNumber, strsNosNew, strsKindsNew);
		}
		// 用数据库中的数据和画面上的数据进行比较,
		// 数据库中有的,画面上没有,则要删除
		intFlag = compareArray(strsNosOld, strsNosNew);
		if(!Constants.BLANK_STRING.equals(intFlag)) {
			String[] strsNumber = intFlag.split(",");
			deleteById(strsNumber, strsIdsOld);
		}
	}
	
	/**
	 * 比较画面上的数据和数据库中的数据
	 * 
	 * @param strsOne 数据
	 * @param strsTwo 数据
	 * @return 差异
	 */
	public String compareArray(String[] strsOne, String[] strsTwo) {
		boolean boolFlag = true;
		String strNo = "";
		for(int i = 0; i < strsOne.length; i++) {
			boolFlag = true;
			for(int j = 0; j < strsTwo.length; j++) {
				if(strsOne[i].equals(strsTwo[j])) {
					boolFlag = false;
					break;
				}
			}
			if(boolFlag) {
				strNo += String.valueOf(i);
				strNo += ",";
			}
		}
		if(!Constants.BLANK_STRING.equals(strNo)) {
			strNo = strNo.substring(0, strNo.lastIndexOf(","));
		}
		return strNo;
	}
	
	/**
	 * 新增一个采购员
	 * 
	 * @param strBuyer 人员编码
	 * @param strName 采购员姓名
	 * @param strsNosNew 物料编码
	 * @param strsKindsNew 物料分类
	 */
	public void addBuyer(String strBuyer, String strName, String[] strsNosNew, String[] strsKindsNew) {
		for(int i = 0; i < strsNosNew.length; i++) {
			add(strBuyer, strName, strsNosNew[i], strsKindsNew[i]);
		}
	}
	
	/**
	 * 新增一条数据
	 * 
	 * @param strBuyer 人员编码
	 * @param strName 采购员姓名
	 * @param strsNumber 画面数据和数据库数据比较结果
	 * @param strsNosNew 物料编码
	 * @param strsKindsNew 物料分类
	 */
	public void addRecord(String strBuyer, String strName, String[] strsNumber, String[] strsNosNew, String[] strsKindsNew) {
		for(int i = 0; i < strsNumber.length; i++) {
			int index = Integer.parseInt(strsNumber[i]);
			add(strBuyer, strName, strsNosNew[index], strsKindsNew[index]);
		}
	}
	
	/**
	 * 新增数据
	 * 
	 * @param strBuyer 人员编码
	 * @param strName 采购员姓名
	 * @param strNo 物料编码
	 * @param strKind 物料分类
	 */
	public void add(String strBuyer, String strName, String strNo, String strKind) {
		PurCBuyer purCBuyer = new PurCBuyer();
		// 设置人员编号
		purCBuyer.setBuyer(strBuyer);
		// 设置人员名称
		purCBuyer.setBuyerName(strName);
		// 设置物料编码或物料分类编码
		purCBuyer.setMaterialOrClassNo(strNo);
		// 设置是否物料分类
		purCBuyer.setIsMaterialClass(strKind);
		// 设置企业编码
		purCBuyer.setEnterpriseCode(employee.getEnterpriseCode());
		// 设置是否使用
		purCBuyer.setIsUse(Constants.IS_USE_Y);
		// 设置修改人
		purCBuyer.setLastModifiedBy(employee.getWorkerCode());
		buyerRemote.save(purCBuyer);
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
}