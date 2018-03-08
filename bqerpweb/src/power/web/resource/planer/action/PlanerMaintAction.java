/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.planer.action;

import java.util.List;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvCMaterialClass;
import power.ejb.resource.InvCMaterialClassFacadeRemote;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.ejb.resource.PurCPlaner;
import power.ejb.resource.PurCPlanerFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 计划员维护
 * 
 * @author jincong
 * @version 1.0
 */
public class PlanerMaintAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/** 树的节点 */
	private String node;
	/** 种类 */
	private String type;
	/** 计划员编码 */
	private String planer;
	/** 计划员维护Remote */
	private PurCPlanerFacadeRemote planerRemote;
	/** 物料分类Remote */
	private InvCMaterialClassFacadeRemote materialClassRemote;
	/** 物料Remote */
	private InvCMaterialFacadeRemote materialRemote;
	/** 人员信息共通接口 */
	private BaseDataManager personInfo;
	
	/**
	 * 构造函数
	 */
	public PlanerMaintAction() {
		// 计划员维护Remote
		planerRemote = (PurCPlanerFacadeRemote) factory
				.getFacadeRemote("PurCPlanerFacade");
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
	 * 根据是否使用查询计划员列表
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getPlanerList() throws JSONException {
		PageObject object = new PageObject();
		// 查询
		object = planerRemote.findByIsUse(Constants.IS_USE_Y,
				employee.getEnterpriseCode());
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("{\"list\":[");
		List<PurCPlaner> list = object.getList();
		for(int i = 0; i < list.size(); i++) {
			PurCPlaner purCPlaner = list.get(i);
			Employee emp = personInfo.getEmployeeInfo(purCPlaner.getPlaner());
			if(emp != null) {
				// 人员编码
				JSONStr.append("{\"planer\":\"" + purCPlaner.getPlaner());
				// 人员姓名
				String strChsName = emp.getWorkerName();
				if(strChsName != null) {
					JSONStr.append("\",\"planerName\":\"" + strChsName);
				} else {
					JSONStr.append("\",\"planerName\":\" ");
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
	public void getPersonInfoPlaner() throws JSONException {
		// 取得查询参数
		String strPlaner = request.getParameter("planer");
		Employee emp = personInfo.getEmployeeInfo(strPlaner);
		write(JSONUtil.serialize(emp));
	}
	
	/**
	 * 根据计划员编码,从计划员维护表中查询对应的信息,取得物料分类标识.
	 * 如果是物料分类,用对应的物料编码或物料分类编码从物料分类表中查询信息.
	 * 如果是物料，用对应的物料编码或物料分类编码从物料主文件表中查询信息.
	 */
	@SuppressWarnings("unchecked")
	public void getPlanerSelectedMaterialList() {
		PageObject object = new PageObject();
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("{\"list\":[");
		// 根据计划员编码查询出计划员信息
		object = planerRemote.findByPlaner(planer, employee.getEnterpriseCode());
		List<PurCPlaner> list = object.getList();
		// 循环处理
		for(int i = 0; i < list.size(); i++) {
			PurCPlaner purCPlaner = (PurCPlaner)list.get(i);
			// 取得对应的物料分类标识
			String strIsMaterialClass = purCPlaner.getIsMaterialClass();
			// 如果是物料分类
			if(Constants.FLAG_Y.equals(strIsMaterialClass)) {
				// 根据物料编码或物料分类编码,从物料分类表中查询对应的物料分类信息
				object = materialClassRemote.findByClassNo(
						purCPlaner.getMaterialOrClassNo(), employee.getEnterpriseCode());
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
						purCPlaner.getMaterialOrClassNo(), employee.getEnterpriseCode());
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
	 * 逻辑删除一个计划员
	 */
	public void deletePlanerByPlaner() {
		deleteByPlaner(planer);	
	}
	
	/**
	 * 逻辑删除一个采购员
	 * 
	 * @param strPlaner 人员编码
	 */
	@SuppressWarnings("unchecked")
	public void deleteByPlaner(String strPlaner) {
		PageObject object = new PageObject();
		// 根据人员编码取出所有数据
		object = planerRemote.findByPlaner(strPlaner, employee.getEnterpriseCode());
		// 循环处理
		if(object != null && object.getList().size() > 0) {
			List<PurCPlaner> list = object.getList();
			for(int i = 0; i < list.size(); i++) {
				PurCPlaner purCPlaner = list.get(i);
				purCPlaner.setIsUse(Constants.IS_USE_N);
				purCPlaner.setLastModifiedBy(employee.getWorkerCode());
				planerRemote.update(purCPlaner);
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
			PurCPlaner purCPlaner = planerRemote.findById(Long.parseLong(strsIdsOld[index]));
			if(purCPlaner != null) {
				// 更新
				purCPlaner.setIsUse(Constants.IS_USE_N);
				purCPlaner.setLastModifiedBy(employee.getWorkerCode());
				planerRemote.update(purCPlaner);
			}
		}
	}
	
	/**
	 * 保存画面上的数据
	 */
	public void savePlanerRecords() {
		// 取得画面上数据的字符串形式
		String strParameter = request.getParameter("string");
		// 按画面上的行分割
		String[] strsPlaner = strParameter.split(";");
		// 如果只有一行
		if((strsPlaner.length == 0) && strParameter.length() > 0) {
			strsPlaner = new String[1];
			strsPlaner[0] = strParameter;
		}
		// 循环处理
		for(int i = 0; i < strsPlaner.length; i++) {
			// 把行数据按画面上的列分割
			String[] strsPlanerInfo = strsPlaner[i].split(",");
			// 人员编码
			String strPlaner = strsPlanerInfo[0];
			// 采购员姓名
			String strName = strsPlanerInfo[1];
			// 标识
			String strFlag = strsPlanerInfo[2];
			// 物料编码
			String[] strsNosNew = strsPlanerInfo[4].split(":");
			// 物料名称
			String[] strsNamesNew = strsPlanerInfo[3].split(":");
			// 物料分类
			String[] strsKindsNew = strsPlanerInfo[5].split(":");
			// 如果此数据是从数据库中取出的
			if(Constants.FLAG_Y.equals(strFlag)) {
				// 如果此计划员已经没有对应的物料
				if(strsNamesNew.length < 1 || " ".equals(strsNamesNew[0])) {
					// 从数据库中删除此计划员
					deleteByPlaner(strPlaner);	
				} else {
					// 更新
					update(strPlaner, strName, strsNosNew, strsKindsNew);
				}
			// 如果此数据是画面增加的
			} else if(Constants.FLAG_N.equals(strFlag)) {
				addPlaner(strPlaner, strName, strsNosNew, strsKindsNew);
			}
		}
	}
	
	/**
	 * 更新
	 * 
	 * @param strPlaner 人员编码
	 * @param strName 计划员姓名
	 * @param strsNosNew 物料编码
	 * @param strsKindsNew 物料分类
	 */
	public void update(String strPlaner, String strName, String[] strsNosNew, String[] strsKindsNew) {
		PageObject object = new PageObject();
		// 根据人员编码取出对应的数据
		object = planerRemote.findByPlaner(strPlaner, employee.getEnterpriseCode());
		int intCount = object.getList().size();
		// 数据库中的物料编码
		String[] strsNosOld = new String[intCount];
		// 数据库中的流水号
		String[] strsIdsOld = new String[intCount];
		if(object != null && intCount > 0) {
			// 生成
			for(int i = 0; i < intCount; i++) {
				PurCPlaner purCPlaner = (PurCPlaner)object.getList().get(i);
				strsNosOld[i] = purCPlaner.getMaterialOrClassNo();
				strsIdsOld[i] = String.valueOf(purCPlaner.getPlanerId());
			}
		}
		// 用画面上的数据和数据库中的数据进行比较,
		// 画面上有的,数据中没有,则要增加
		String intFlag = compareArray(strsNosNew, strsNosOld);
		if(!Constants.BLANK_STRING.equals(intFlag)) {
			String[] strsNumber = intFlag.split(",");
			addRecord(strPlaner, strName, strsNumber, strsNosNew, strsKindsNew);
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
	 * 新增一个计划员
	 * 
	 * @param strPlaner 人员编码
	 * @param strName 计划员姓名
	 * @param strsNosNew 物料编码
	 * @param strsKindsNew 物料分类
	 */
	public void addPlaner(String strPlaner, String strName, String[] strsNosNew, String[] strsKindsNew) {
		for(int i = 0; i < strsNosNew.length; i++) {
			add(strPlaner, strName, strsNosNew[i], strsKindsNew[i]);
		}
	}
	
	/**
	 * 新增一条数据
	 * 
	 * @param strPlaner 人员编码
	 * @param strName 计划员姓名
	 * @param strsNumber 画面数据和数据库数据比较结果
	 * @param strsNosNew 物料编码
	 * @param strsKindsNew 物料分类
	 */
	public void addRecord(String strPlaner, String strName, String[] strsNumber, String[] strsNosNew, String[] strsKindsNew) {
		for(int i = 0; i < strsNumber.length; i++) {
			int index = Integer.parseInt(strsNumber[i]);
			add(strPlaner, strName, strsNosNew[index], strsKindsNew[index]);
		}
	}
	
	/**
	 * 新增数据
	 * 
	 * @param strPlaner 人员编码
	 * @param strName 计划员姓名
	 * @param strNo 物料编码
	 * @param strKind 物料分类
	 */
	public void add(String strPlaner, String strName, String strNo, String strKind) {
		PurCPlaner purCPlaner = new PurCPlaner();
		// 设置人员编号
		purCPlaner.setPlaner(strPlaner);
		// 设置人员名称
		purCPlaner.setPlanerName(strName);
		// 设置物料编码或物料分类编码
		purCPlaner.setMaterialOrClassNo(strNo);
		// 设置是否物料分类
		purCPlaner.setIsMaterialClass(strKind);
		// 设置企业编码
		purCPlaner.setEnterpriseCode(employee.getEnterpriseCode());
		// 设置是否使用
		purCPlaner.setIsUse(Constants.IS_USE_Y);
		// 设置修改人
		purCPlaner.setLastModifiedBy(employee.getWorkerCode());
		planerRemote.save(purCPlaner);
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlaner() {
		return planer;
	}

	public void setPlaner(String planer) {
		this.planer = planer;
	}

}
