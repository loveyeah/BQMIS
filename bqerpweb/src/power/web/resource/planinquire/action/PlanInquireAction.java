package power.web.resource.planinquire.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.resource.MrpJPlanInquireDetail;
import power.ejb.resource.MrpJPlanInquireDetailFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementDetailFacadeRemote;
import power.ejb.resource.form.MrpJPlanRequirementDetailInfo;
import power.web.comm.UploadFileAbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
// modified by liuyi 20100409 
//public class PlanInquireAction extends AbstractAction{
public class PlanInquireAction extends UploadFileAbstractAction{
	protected MrpJPlanInquireDetailFacadeRemote remote;
	private HrJEmpInfoFacadeRemote empRemote;
	private File solutionFile;
	
	public File getSolutionFile() {
		return solutionFile;
	}

	public void setSolutionFile(File solutionFile) {
		this.solutionFile = solutionFile;
	}

	public PlanInquireAction()
	{
		remote=(MrpJPlanInquireDetailFacadeRemote)factory.getFacadeRemote("MrpJPlanInquireDetailFacade");
		empRemote = (HrJEmpInfoFacadeRemote)factory.getFacadeRemote("HrJEmpInfoFacade");
	}
	
	public void findPlanInquireListForBill() throws JSONException
	{
		String gatherIds=request.getParameter("gatherIds");
		// modified by liuyi 091127 去掉分页
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
        PageObject obj = new PageObject();
        if(start!=null&&limit!=null)
        {
        	/** 开始查询位置 */
            Object objstart = Integer.parseInt(start);
            /** 查询行数 */
            Object objLimit = Integer.parseInt(limit);
        	int startInt=Integer.parseInt(objstart.toString());
        	int limitInt=Integer.parseInt(objLimit.toString());
        	obj=remote.findInquireDetailByGatherId(gatherIds,startInt,limitInt);
        }
        else
        {
        	obj=remote.findInquireDetailByGatherId(gatherIds);
        }
        
        String str = JSONUtil.serialize(obj);
        write(str);
	}
	
	public void findQuotedPriceList() throws JSONException
	{
		String materialName=request.getParameter("materialName");
		String buyer=request.getParameter("buyer");
		/** 开始查询位置 */
        Object objstart = Integer.parseInt(request.getParameter("start"));
        /** 查询行数 */
        Object objLimit = Integer.parseInt(request.getParameter("limit"));
        PageObject obj = new PageObject();
        if(objstart!=null&&objLimit!=null)
        {
        	int start=Integer.parseInt(objstart.toString());
        	int limit=Integer.parseInt(objLimit.toString());
        	obj=remote.findQuotedPriceList(materialName, buyer, employee.getEnterpriseCode(), start,limit);
        }
        else
        {
        	obj=remote.findQuotedPriceList(materialName, buyer, employee.getEnterpriseCode());
        }
        
        String str = JSONUtil.serialize(obj);
        write(str);
	}
	
	/**
	 * 批量保存询价内容修改
	 * 
	 */
	public void modifyContents() {
		try {
			String addOrUpdateRecords = request.getParameter("addOrUpdateRecords"); 
			String delIds = request.getParameter("deleteRecords");
			if (addOrUpdateRecords != null || delIds != null) {
				List<MrpJPlanInquireDetail> list = parseContent(addOrUpdateRecords);
				remote.modifyRecords(list, delIds);
				write("{success:true}");
			} 
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success:false}");
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<MrpJPlanInquireDetail> parseContent(String records) throws JSONException {
		List<MrpJPlanInquireDetail> result = new ArrayList<MrpJPlanInquireDetail>();
		Object object = JSONUtil.deserialize(records);
		List list=(List)object;
		int intLen = list.size();
		for(int i=0;i<intLen;i++){
			Map map=(Map)list.get(i);
			MrpJPlanInquireDetail m = this.parseContentModel(map);
			result.add(m);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private MrpJPlanInquireDetail parseContentModel(Map map) {
		
MrpJPlanInquireDetail model=new MrpJPlanInquireDetail(); 
		
		Object inquireDetailId = map.get("inquireDetailId");
		Object gatherId = map.get("gatherId"); 
		Object billNo = map.get("billNo");
		Object inquireQty = map.get("inquireQty");
		Object unitPrice = map.get("unitPrice");
		Object totalPrice = map.get("totalPrice");
		Object qualityTime = map.get("qualityTime");  
		Object offerCycle = map.get("offerCycle"); 
		Object memo = map.get("memo"); 
		Object isSelectSupplier = map.get("isSelectSupplier"); 
		Object effectStartDate = map.get("effectStartDate"); 
		Object effectEndDate = map.get("effectEndDate");
		Object inquireSupplier = map.get("inquireSupplier");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(inquireDetailId!=null&&!inquireDetailId.equals(""))
		{
			model=remote.findById(Long.parseLong(inquireDetailId.toString()));
			if(gatherId != null&&!gatherId.equals("")) 
				model.setGatherId(Long.parseLong(gatherId.toString()));
//			if(billNo != null ) 
//				model.setBillNo(billNo.toString());
			if(inquireQty !=null&&!inquireQty.equals("")) 
				model.setInquireQty(Double.parseDouble(inquireQty.toString()));
			if(unitPrice !=null&&!unitPrice.equals("")) 
				model.setUnitPrice(Double.parseDouble(unitPrice.toString()));
			if(totalPrice !=null&&!totalPrice.equals("")) 
				model.setTotalPrice(Double.parseDouble(totalPrice.toString()));
			if(qualityTime !=null&&!qualityTime.equals(""))
				model.setQualityTime(qualityTime.toString());
			if(offerCycle !=null&&!offerCycle.equals(""))
				model.setOfferCycle(offerCycle.toString());
			if(memo !=null)
					model.setMemo(memo.toString());
			if(isSelectSupplier !=null&&!isSelectSupplier.equals(""))
				model.setIsSelectSupplier(isSelectSupplier.toString());
			if(inquireSupplier != null&&!inquireSupplier.equals(""))
				model.setInquireSupplier(Long.parseLong(inquireSupplier.toString()));
			if(effectStartDate !=null&&!effectStartDate.equals(""))
				try {
					model.setEffectStartDate(sdf.parse(effectStartDate.toString()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			
			if(effectEndDate !=null&&!effectEndDate.equals(""))
				try {
					model.setEffectEndDate(sdf.parse(effectEndDate.toString()));
				} catch (ParseException e) {
					e.printStackTrace();
				} 
		}else {
//			if(inquireDetailId != null&&!inquireDetailId.equals("")) 
//				model.setInquireDetailId(Long.parseLong(inquireDetailId.toString()));
			if(gatherId != null&&!gatherId.equals("")) 
				model.setGatherId(Long.parseLong(gatherId.toString()));
//			if(billNo != null ) 
//				model.setBillNo(billNo.toString());
			if(inquireQty !=null&&!inquireQty.equals("")) 
				model.setInquireQty(Double.parseDouble(inquireQty.toString()));
			if(unitPrice !=null&&!unitPrice.equals("")) 
				model.setUnitPrice(Double.parseDouble(unitPrice.toString()));
			if(totalPrice !=null&&!totalPrice.equals("")) 
				model.setTotalPrice(Double.parseDouble(totalPrice.toString()));
			if(qualityTime !=null&&!qualityTime.equals(""))
				model.setQualityTime(qualityTime.toString());
			if(offerCycle !=null&&!offerCycle.equals(""))
				model.setOfferCycle(offerCycle.toString());
			if(memo !=null)
					model.setMemo(memo.toString());
			if(isSelectSupplier !=null&&!isSelectSupplier.equals(""))
				model.setIsSelectSupplier(isSelectSupplier.toString());
			if(inquireSupplier != null&&!inquireSupplier.equals(""))
				model.setInquireSupplier(Long.parseLong(inquireSupplier.toString()));
			if(effectStartDate !=null&&!effectStartDate.equals(""))
				try {
					model.setEffectStartDate(sdf.parse(effectStartDate.toString()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			
			if(effectEndDate !=null&&!effectEndDate.equals(""))
				try {
					model.setEffectEndDate(sdf.parse(effectEndDate.toString()));
				} catch (ParseException e) {
					e.printStackTrace();
				} 
//			model.setIsUse("Y");
			model.setModifyBy(employee.getWorkerCode());
//			model.setCreateDate(new Date());
			model.setEnterpriseCode(employee.getEnterpriseCode());
		}
		
		
		return model;
	}
	
	
	public void chooseSupplierForInquire()
	{
		String detailId=request.getParameter("detailId");
		remote.chooseSupplier(Long.parseLong(detailId));
		write("{success:true,msg:'保存成功！'}");
	}
	
	/**
	 * add by fyyang 091105
	 * 查询该汇总对应的需求物资明细
	 * @throws JSONException
	 */
	public void findMaterialDetailByGatherId() throws JSONException
	{
		MrpJPlanRequirementDetailFacadeRemote detailRemote=(MrpJPlanRequirementDetailFacadeRemote)factory.getFacadeRemote("MrpJPlanRequirementDetailFacade");
		String gatherId=request.getParameter("gatherId");
		List<MrpJPlanRequirementDetailInfo> list =detailRemote.getMaterialDetailByGatherId(Long.parseLong(gatherId));
		PageObject obj=new PageObject();
		obj.setList(list);
		obj.setTotalCount(Long.parseLong(list.size()+""));
		 String str = JSONUtil.serialize(obj);
	        write(str);
	}
	
	/**
	 * add by liuyi 091202
	 * @throws JSONException
	 */
	public void getWorkerInfoByWorkerId() throws JSONException
	{
		HrJEmpInfo aEmplyee = new HrJEmpInfo();
		String empId = request.getParameter("empId");
		if(empId != null && !empId.equals(""))
		{
			aEmplyee = empRemote.findById(Long.parseLong(empId));
		}
		write(JSONUtil.serialize(aEmplyee));
	}
	
	/**
	 * add by liuyi 20100409 询价单附件上传
	 */
	public void addInquireDetailsFile(){
		String filePath= request.getParameter("filepath"); 
		String detailId= request.getParameter("detailId");  
		if (!filePath.equals("")) {  
			String fileName =filePath.substring(filePath.lastIndexOf("\\")+1);
			String calluploadPath = uploadFile(solutionFile,fileName,"inquire");
			if(detailId != null && !detailId.equals("")){
				MrpJPlanInquireDetail entity = remote.findById(Long.parseLong(detailId));
				entity.setFilePath(calluploadPath);
				remote.update(entity);
				write("{success:true,msg:'增加附件成功！'}");
			}
		} 
	}
	
	/**
	 * add by liuyi 20100409 询价单附件删除
	 */
	public void deleteInquireFile(){
		String detailId = request.getParameter("detailId");
		if(detailId != null && !detailId.equals("")){
			MrpJPlanInquireDetail entity = remote.findById(Long.parseLong(detailId));
			entity.setFilePath(null);
			remote.update(entity);
		}
	}
}
