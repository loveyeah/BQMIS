/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.materialtype.action;

import java.util.Date;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvCMaterialType;
import power.ejb.resource.InvCMaterialTypeFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 物料类型维护action
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
public class LogisticsMaterialTypeAction extends AbstractAction { 
	  
    private static final long serialVersionUID = 1L;
    //ejb之been
    private InvCMaterialType typeBeen;
    //ejb之remote 
    private InvCMaterialTypeFacadeRemote typeRemote;

	public InvCMaterialType getTypeBeen() {
		return typeBeen;
	}

	public void setTypeBeen(InvCMaterialType typeBeen) {
		this.typeBeen = typeBeen;
	}
    
    /**
     * 构造函数
     */
    public LogisticsMaterialTypeAction() {
    	typeRemote = (InvCMaterialTypeFacadeRemote) factory.getFacadeRemote("InvCMaterialTypeFacade");
    }

    /**
     * 页面加载
     * 
     * @throws JSONException
     */
    public void getMaterialTypeList() throws JSONException {
		// 取得查询参数: 开始行
		int intStart = Integer.parseInt(request.getParameter("start"));
		// 取得查询参数: 结束行
		int intLimit = Integer.parseInt(request.getParameter("limit"));	
		
    	PageObject obj = typeRemote.getMaterialTypeList(employee.getEnterpriseCode(), intStart,intLimit);
    	// 查询结果为null,设置页面显示
		if(obj == null) {
		String str = "{\"list\":[],\"totalCount\":0}";
			write(str);
		 // 不为null
		} else {
			if(obj.getList() == null) {
				String str = "{\"list\":[],\"totalCount\":0}";
				write(str);
			}
			String str = JSONUtil.serialize(obj);
			write(str);
		}
    }

    /**
     * 增加
     * 
     * @throws CodeRepeatException
     */
    public void addMaterialType() throws CodeRepeatException {
        try {
        	// 获取企业编码
        	String enterpriseCode = employee.getEnterpriseCode();
    		// 增加的编码与名称
        	String newTypeNo = typeBeen.getTypeNo();
    		String newTypeName = typeBeen.getTypeName();
    		// 检查增加的编码与名称是否已经存在
    		if(typeRemote.isTypeNoExist(enterpriseCode, newTypeNo)) {
    			// 编码重复
    			write("{success:true,flag:'2'}");
    			return;
    		}    		
    		if(typeRemote.isTypeNameExist(enterpriseCode, newTypeName)) {    			
    		    // 名称重复
    	        write("{success:true,flag:'1'}");
    	        return;
    		}
    		// 设定企业编码
        	typeBeen.setEnterpriseCode(enterpriseCode);
        	// 设定修改者        
        	typeBeen.setLastModifiedBy(employee.getWorkerCode());
        	// 设定是否可用
        	typeBeen.setIsUse(Constants.IS_USE_Y);
        	// 修改时间
        	typeBeen.setLastModifiedDate(new Date());
        	// 流水号在保存时设定
        	typeRemote.save(typeBeen);
        	
        	// 保存成功	     		
     		write("{success:true,flag:'0'}");          
        } catch (Exception e) {
            write(Constants.ADD_FAILURE);
        }
    }

    /**
     * 更新
     * 
     * @throws CodeRepeatException
     */
    public void updateMaterialType() throws CodeRepeatException {
    	try {
    		// 根据画面选择项目的流水号找到类型实体
    		InvCMaterialType oldTypeBeen = typeRemote.findById(typeBeen.getMaterialTypeId()); 		
    		// 获取企业编码
            String enterpriseCode = employee.getEnterpriseCode(); 
    		// 修改前后的名称
    		String newTypeName = typeBeen.getTypeName();
    		String oldTypeName = oldTypeBeen.getTypeName();
    		// check修改后的名称是否已经存在,如已存在，则必须与修改前的名称相同
    		if(typeRemote.isTypeNameExist(enterpriseCode, newTypeName)) {
    			if(!oldTypeName.equals(newTypeName)) {
    				// 名称重复
    	     		write("{success:true,flag:'1'}");
    	     		return;
    			}
    		}
    		// 更新内容    		
	    	// 1.名称
    		oldTypeBeen.setTypeName(typeBeen.getTypeName());	
    		// 2.描述
    		oldTypeBeen.setTypeDesc(typeBeen.getTypeDesc());
    		// 3.设定修改者   
    		oldTypeBeen.setLastModifiedBy(employee.getWorkerCode());
    		// 4.修改日期
    		oldTypeBeen.setLastModifiedDate(new Date());
    		typeRemote.update(oldTypeBeen);
    		// 保存成功	     		
     		write("{success:true,flag:'0'}");
     	} catch (Exception e) {
	    	write(Constants.MODIFY_FAILURE_CARD);
	    }
    }

    /**
     * 删除 通过流水号找到实体，然后进行逻辑删除
     */
    public void deleteMaterialType() {
    	// 根据画面选择项目的流水号找到类型实体
		InvCMaterialType oldTypeBeen = typeRemote.findById(typeBeen.getMaterialTypeId()); 		

    	// 1.设定修改者   
		oldTypeBeen.setLastModifiedBy(employee.getWorkerCode());
		// 2.修改日期
		oldTypeBeen.setLastModifiedDate(new Date());
    	// 3.设定不可用
		oldTypeBeen.setIsUse(Constants.IS_USE_N);
		// 逻辑删除
		typeRemote.update(oldTypeBeen);		
    }
}