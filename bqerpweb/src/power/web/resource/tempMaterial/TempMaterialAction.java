package power.web.resource.tempMaterial;

import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvCMaterialClass;
import power.ejb.resource.InvCMaterialClassFacadeRemote;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.ejb.resource.InvCTempMaterial;
import power.ejb.resource.InvCTempMaterialFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class TempMaterialAction extends AbstractAction{

	private InvCTempMaterial temp;
	private InvCTempMaterialFacadeRemote remote;
	private InvCMaterialFacadeRemote materialRemote;
	private InvCMaterialClassFacadeRemote materialClassRemote;
	private long classId;
	
	public TempMaterialAction()
	{
		remote = (InvCTempMaterialFacadeRemote)factory.getFacadeRemote("InvCTempMaterialFacade");
		materialRemote = (InvCMaterialFacadeRemote)factory.getFacadeRemote("InvCMaterialFacade");
		materialClassRemote = (InvCMaterialClassFacadeRemote)factory.getFacadeRemote("InvCMaterialClassFacade");
	}
	
	//增加
	public void addTempMaterial()
	{
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		temp.setLastModifiedBy(employee.getWorkerCode());
		temp.setCheckBy(employee.getWorkerCode());
		temp = remote.save(temp);
		if(temp == null)
		{
			write("{failure:true,msg:'此规格型号下的该物料名称已存在！'}");// modify by ywliu 090721 修改提示信息
		}
		else
		write("{success:true,id:'"+temp.getTempId()+"',msg:'增加成功！'}");
	}
	
	//修改
	public void updateTempMaterial()
	{
		InvCTempMaterial model = remote.findById(temp.getTempId());
		model.setMaterialName(temp.getMaterialName());
		model.setSpecNo(temp.getSpecNo());
		model.setParameter(temp.getParameter());
		model.setStockUmId(temp.getStockUmId());
		model.setDefaultWhsNo(temp.getDefaultWhsNo());
		model.setDefaultLocationNo(temp.getDefaultLocationNo());
		model.setMaertialClassId(temp.getMaertialClassId());
		model.setFactory(temp.getFactory());
		model.setActPrice(temp.getActPrice());
//		model.setCheckDate(temp.getCheckDate());
//		model.setCheckBy(temp.getCheckBy());
		model.setTelNo(temp.getTelNo());
		model.setMemo(temp.getMemo());
		model.setLastModifiedBy(employee.getWorkerCode());
		
		temp = remote.update(model);
		if(temp == null)
		{
			write("{failure:true,msg:'此规格型号下的该物料名称已存在！'}");
		}
		else
		write("{success:true,msg:'修改成功！'}");
	}
	
	//删除
	public void deleteTempMaterial()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	//登记列表查询
	public void findTempMaterialList() throws JSONException
	{
		PageObject obj=new  PageObject();
		
		String materialName = request.getParameter("materialName");
		String status = request.getParameter("status");
		
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findTempMaterialList(employee.getWorkerCode(),employee.getEnterpriseCode(), materialName, status, start,limit);
	    }
	    else
	    {
	    	obj=remote.findTempMaterialList(employee.getWorkerCode(),employee.getEnterpriseCode(), materialName, status);
	    }
	    String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	//已审核的列表查询
	public void findApproveList() throws JSONException
	{
		PageObject obj = new PageObject();
		String materialName = request.getParameter("materialName");
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			 obj=remote.findApprovelList(employee.getEnterpriseCode(), materialName, start,limit);
	    }
	    else
	    {
	    	 obj=remote.findApprovelList(employee.getEnterpriseCode(), materialName);
	    }
	    String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	//生成正式物资编码
	public void addForUpdateMaterial()
	{
		Long tempId = Long.parseLong(request.getParameter("tempId"));
		String whsNo = request.getParameter("whsNo");
		Long  materialClassId = Long.parseLong(request.getParameter("materialClassId"));
		//生成物资编码
	    String materialNo=materialRemote.createMaterialNo(whsNo, materialClassId);
		//修改物资临时表记录
		InvCTempMaterial model = remote.findById(tempId);
		model.setMaterialNo(materialNo);
		model.setStatusId(3L);
		//model.setCheckBy(employee.getWorkerCode());
		model.setEnterpriseCode(employee.getEnterpriseCode());
		model.setApproveBy(employee.getWorkerCode());
		model.setApproveDate(new Date());
		remote.update(model);
		//增加物料主文件表记录
		InvCMaterial materialModel = new InvCMaterial();
		materialModel.setMaterialNo(materialNo);
		materialModel.setMaterialName(model.getMaterialName());
		materialModel.setSpecNo(model.getSpecNo());
		materialModel.setParameter(model.getParameter());
		materialModel.setStockUmId(model.getStockUmId());
		materialModel.setDefaultWhsNo(model.getDefaultWhsNo());
		materialModel.setMaertialClassId(model.getMaertialClassId());
		materialModel.setFactory(model.getFactory());
		materialModel.setActualCostTotal(model.getActPrice());
		materialModel.setIsUse("Y");
		materialModel.setEnterpriseCode(employee.getEnterpriseCode());
		materialModel.setLastModifiedDate(new Date());
		materialModel.setLastModifiedBy(employee.getWorkerCode());
		materialModel.setQaControlFlag("Y");// 当生成正式物资编码的时候，默认设置免检 modify ywliu 09/10/10
		materialModel.setIsLot("N");
		materialModel.setPurUmId(model.getStockUmId());
		materialModel.setPurStockUm(1d);
		materialModel.setIsReserved("N");
		materialModel.setIsDanger("N");
		// 当生成正式物资编码的时候，默认设置库位信息 modify ywliu 09/07/15
		materialModel.setDefaultLocationNo("01");
		materialRemote.save(materialModel);
		write("{success:true,msg:'修改成功！'}");
	}
	
	public void reportTempMaterial()
	{
		String ids = request.getParameter("tempId");
		//modify by drdu 091028 同时上报多条记录
		String[] tempIds = ids.split(",");
		for (int i = 0; i < tempIds.length; i++) {
			if (!tempIds[i].equals("")) {
				InvCTempMaterial model = remote.findById(Long.parseLong(tempIds[i]));
				model.setCheckBy(employee.getWorkerCode());
				model.setCheckDate(new Date());
				model.setStatusId(2l);
				remote.update(model);
			}
		}
		write("{success:true,msg:'上报成功！'}");
	}
	
	@SuppressWarnings("unchecked")
	public void getParentCode() throws JSONException
	{
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("[");
		String parentCode = "";
		String splitCode = "";
		parentCode = materialClassRemote.getAllParentCode(classId);
		//String [] data=parentCode.toString().split(",");
		if(parentCode !="" && parentCode != null)
		{
			splitCode = parentCode.substring(0, parentCode.indexOf("-"));
		}
		PageObject obj = materialClassRemote.findByClassNo(splitCode, employee.getEnterpriseCode());
		
		String materialClassName=splitCode;
		if(obj!=null)
		{
			if(obj.getTotalCount()>0)
			{
				InvCMaterialClass model=(InvCMaterialClass) obj.getList().get(0);
				materialClassName=model.getClassName();
			}
		}
		write(JSONUtil.serialize(splitCode+","+materialClassName));
	}
	
	public void backTempMaterial()
	{
		//退回
		String backReason=request.getParameter("backReason");
		String tempId=request.getParameter("tempId");
		InvCTempMaterial model = remote.findById(Long.parseLong(tempId));
		model.setBackReason(backReason);
		model.setStatusId(4l);
		remote.update(model);
		write("{success:true,msg:'退回成功！'}");
	}
	
	public InvCTempMaterial getTemp() {
		return temp;
	}
	public void setTemp(InvCTempMaterial temp) {
		this.temp = temp;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}
}
