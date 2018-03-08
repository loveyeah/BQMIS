package power.web.hr.labor;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCDept;
import power.ejb.hr.labor.DynaicQueryClass;
import power.ejb.hr.labor.HrJLaborChange;
import power.ejb.hr.labor.HrJLaborDeptRegister;
import power.ejb.hr.labor.HrJLaborRegister;
import power.ejb.hr.labor.LaborManage;
import power.ejb.hr.labor.form.LoborRegisterForm;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class LaborRegisterAction extends AbstractAction{
	private LaborManage Remotea;
	private String[] planID;
	public LaborRegisterAction()
	{
		Remotea=(LaborManage)factory.getFacadeRemote("LaborManageImpl");
		planID=new String[100];
	}
	
	
	
	
	public void getDeptList()throws JSONException{
		PageObject obj = Remotea.getDeptList(employee.getEnterpriseCode());
		List<HrCDept> beanList=new ArrayList<HrCDept>();
		
		   List itList=obj.getList();
		   Iterator it=itList.iterator();
		   while(it.hasNext()){
			   HrCDept bean=new HrCDept();
			   Object[] ob=(Object[])it.next();
			   if (ob[0]!=null)  
				   bean.setDeptName(ob[0].toString());
			   beanList.add(bean);
		   }
		 
		   
			write(JSONUtil.serialize(beanList));
		}
		
		

	
	
	public void getAllRegisterInfo() throws JSONException,IOException, NamingException,ParseException{
		String year = request.getParameter("year");

		String season = request.getParameter("season");
		/*String  season="";
		if (datetime=="2009-10"){
			season="4";
		}else 
		if(datetime=="2009-07")
				season="3";
		else if (datetime=="2009-04")
		     season="2";
		else season="1";*/
		
		String[] plantcount = new String[10];
		String[] plantname = new String[20];
		//String[] planID=new String[20];
		DynaicQueryClass queryClass = Remotea.findDeptSendList(year,season,employee.getEnterpriseCode(),plantname,plantcount,planID);
		int pcount =queryClass.getHeaderCount();
		plantname=queryClass.getHeaderNames();
		planID=queryClass.getPlanID();
		List dataList=queryClass.getList();
		Iterator it = dataList.iterator();
		String pid="";
		String mycode = "";
		String myname = "";
		String myId="";
		String str = "{ 'action' : true,'message' : 'error!','data' : [";
		//对每行记录集进行迭代，使得GRID行记录赋值
		while (it.hasNext()) {
			mycode = "";
			myname = "";
			myId="";
			pid="";
			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				pid = data[0].toString();
			}
			if (data[1] != null) {
				mycode = data[1].toString();
			}
			if(data[2]!=null){
				myname=data[2].toString();
			}
			if(data[3]!=null){
				myId=data[3].toString();
			}
			str = str + " { 'itemName':'" + mycode + "', 'metricUnit':'"
					+ myname + "', 'deptCode':'" + myId + "'"+ ", 'pid':'" + pid + "'";
			//迭代列
			for (int i = 0; i <pcount * 1; i++) {
				mycode = "";
				if (data[i+4] != null) {
					mycode = data[i+4].toString();
				}
				str = str + ",'"+planID[i] +"':'"
						+ mycode + "'";
			}
			str = str + "},";
		}
		System.out.println( "the str is "+str);

		if (!str.equals("{ 'action' : true,'message' : 'error!','data' : [")) {
			str = str.substring(0, str.length() - 1);
		}
		//构造列头
		str = str
				+ "],'columModle' : [{header:'ID',width:150,'dataIndex' : 'pid',hidden:true},new Ext.grid.RowNumberer({header:'序号',width:50,sortable:true}),{'header' : '所属部门','dataIndex' : 'itemName','width' :100,'align':'left'"
				+ "},{header:'发放日期',width:150,'dataIndex' : 'metricUnit',sortable:true}";
		for (int j = 0; j <pcount; j++) {
			str = str + ",{'header':'"+plantname[j] +"','dataIndex' : '" 
					+ planID[j] + "','width' : 150,sortable:true,'editor':new Ext.form.NumberField({allowDecimals : true,decimalPrecision : 0})}";
		}
		//构造record
		str = str
				+ "],'fieldsNames' : [{ name : 'itemName'}, {name : 'metricUnit'},{name : 'deptCode'},{ name : 'pid'}";
		for (int k = 0; k <pcount; k++) {
			str = str + ",{name:'"+planID[k]+"'}";
		}
		str = str + "],'rows' : [[{header : '部门劳保发放登记表" 
				+ "',colspan :" + (pcount+1)+ ",align : 'center'}]";
		str = str + "]}";
		
		 System.out.println(str);
		write(str);

		 };
		
			public String changeNullToString(Object obj){
				if(obj!=null){
					return obj.toString();
				}
				return "";
			}
	public void saveLaborRegisterInfo() throws Exception{
		//modify by fyyang 100105
			String str = request.getParameter("addOrUpdateRecords");
			String year = request.getParameter("year");
			String season = request.getParameter("season");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Object obj = JSONUtil.deserialize(str);
			List<Map> list = (List<Map>) obj;
			List<HrJLaborDeptRegister> addlist =new ArrayList<HrJLaborDeptRegister>();
			List<HrJLaborDeptRegister> updatelist = new ArrayList<HrJLaborDeptRegister>();
			
			for (Map data : list) {
				
					String deptCode=data.get("deptCode").toString();	
					String ids=data.get("laborMaterialId").toString();
					ids=ids.substring(1, ids.length()-1);
					String nums=data.get("materialNum").toString();
					nums=nums.substring(1, nums.length()-1);
					String [] laborMaterialIds=ids.split(",");
					String [] materialNums=nums.split(",");
					for(int i=0;i<laborMaterialIds.length;i++)
					{
						if(materialNums[i]!=null&&!materialNums[i].trim().equals(""))
						{
						   
						HrJLaborDeptRegister model=  Remotea.findByDeptAndLabor(deptCode, Long.parseLong(laborMaterialIds[i].trim()), year, season, employee.getEnterpriseCode());
						  if(model==null)
						  {
							  model=new HrJLaborDeptRegister();
							  model.setDeptCode(deptCode);
							   model.setEnterpriseCode(employee.getEnterpriseCode());
							   model.setLaborMaterialId(Long.parseLong(laborMaterialIds[i].trim()));
							   model.setSendNum(Long.parseLong(materialNums[i].trim()));
							   addlist.add(model);
						  }
						  else
						  {
							  model.setSendNum(Long.parseLong(materialNums[i].trim()));
							  updatelist.add(model);
						  }
					     
						}
					}
			}
							
							
					
						
						
						
						if (Remotea.saveDeptSendInfo(year,season,addlist, updatelist, employee.getEnterpriseCode()))
						{
							write("{success:true}");
						}
						else
						{
							write("{success:false}");
						}

						
						
				
					
				
			
			
			

		
	}
	
//-----------------------------------------------add by drdu 091229---------------------------------------	

	@SuppressWarnings("unchecked")
	public void findLaborRegisterList()
	{
		String sendSeason = request.getParameter("sendSeason");
		String deptId = request.getParameter("deptId");
		 String str = "{'data':[";
		 PageObject result = Remotea.findLaborRegisterList(employee.getEnterpriseCode(), deptId, sendSeason);
		 int matralCount = result.getTotalCount().intValue();
		 List dataList = result.getList();
		 if(dataList != null && dataList.size() > 2)
			{			
				for (int i = 2; i < dataList.size(); i++) {
					str += "{";
					Object[] data = (Object[])dataList.get(i);
				
					Object[] dataIndex = (Object[]) dataList.get(0);
					System.out.println(data.length+"aaa"+dataIndex.length);
					for (int j = 0; j < data.length; j++) {
						String cellValue = "";
						if(data[j] != null)
							cellValue = data[j].toString();
							str += "'" + dataIndex[j].toString() + "':'" + cellValue + "',";
					}	
					if(str.endsWith(","))
					{
						str = str.substring(0,str.length() - 1);
					}
					str += "},";
				}			
			}
		 if(str.equals("{'data':[")){
			 str += "]";
		 }else{
			 str = str.substring(0, str.length()-1) + "]";
		 }
		
		str += ",'columModel':[new Ext.grid.RowNumberer({header : '序号',width : 32}),";
		str +="{'header' : '发放季度','hidden' : true,'width':100,'dataIndex' : 'sendSeason','align':'center'},"
			+"{'header' : '部门Id','hidden' : true,'width':100,'dataIndex' : 'd','align':'center'},"
			 +"{'header' : '部门编码','hidden' : true,'width':100,'dataIndex' : 'd','align':'center'},"
			 +"{'header' : '所属部门','hidden' : false,'width':100,'dataIndex' : 'd','align':'center'},"
			 +"{'header' : '劳保工种Id','hidden':true,'width':100,'dataIndex' : 'lbWorkId','align':'center'},"
			 +"{'header' : '劳保工种','width':100,'dataIndex' : 'lbWorkName','align':'center'},"	
			 +"{'header' : '人员编码','hidden' : true,'width':100,'dataIndex' : 'empCode','align':'center'},"	
			 +"{'header' : '人员名称','width':100,'dataIndex' : 'chsName','align':'center'},"	
			+"{'header' : '性别','width':100,'dataIndex' : 'sex','align':'center'}";	
		
		if(matralCount > 0)
		{
			if(dataList != null && dataList.size() > 1)
			{
				Object[] header = (Object[])dataList.get(1);
				Object[] dataIndex = (Object[])dataList.get(0);
				for(int i = 9; i <= 9 + matralCount -1; i++)
				{
					System.out.println("aaa " + header[i].toString() + "  bbb");
					str += ",{'header':'" + header[i].toString() + "','width': 100,'dataIndex': '"
						+ dataIndex[i].toString() + "','align':'center'";
					str += ",'editor':new Ext.form.NumberField({allowDecimals : true,decimalPrecision : 0})";
					str += "}";
				}
			}				
		}
		 str += "],'fieldsNames' : [";
		 if(dataList != null && dataList.size() > 0)
		 {
			 Object[] feilds = (Object[])dataList.get(0);
			 for(int i = 0; i<= feilds.length - 1; i++)
			 {
				 str += "{'name':'" + feilds[i] + "'},";
			 }
			 if(str.endsWith(","))
			{
				str = str.substring(0,str.length() - 1);
			}
		 }
		 str += "]}";
		 System.out.println(str);
		 write(str);		  	
	}

	/**
	 * 劳保用品发放登记保存
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void saveLoborRgister() throws JSONException
	{
		String mod = request.getParameter("mod");
		String method = request.getParameter("method");
		
		Object modObj = JSONUtil.deserialize(mod);
		List<Map> modList = (List<Map>)modObj;
		
		List<LoborRegisterForm> arrlist = new ArrayList<LoborRegisterForm>();
		for(Map map : modList)
		{
			LoborRegisterForm temp = this.parseLoborRegisterForm(map);
			arrlist.add(temp);
		}
		Remotea.saveLoborRegisterRecord(method, arrlist);
	}
	
	/**
	 * 将一个映射转成SalaryAmountForm对象
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public LoborRegisterForm parseLoborRegisterForm(Map map)
	{
		LoborRegisterForm temp = new LoborRegisterForm();
		List<HrJLaborRegister> loborlList = new ArrayList<HrJLaborRegister>();
		
		 Long laborRegisterId=null;
		 String workCode =null;
		 Date sendDate = new Date();
		 Long laborMaterialId = null;
		 Long sendNum = null;
		 Long actualNum = null;
		 String status ="0";
		 String enterpriseCode = employee.getEnterpriseCode();
		 String isUse = "Y";
		 String sendSeason = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		
//		if(map.get("laborRegisterId") != null && !map.get("laborRegisterId").toString().equals(""))
//			laborRegisterId = Long.parseLong(map.get("laborRegisterId").toString());
		if(map.get("empCode") != null && !map.get("empCode").toString().equals(""))
			workCode = map.get("empCode").toString();
		if(map.get("sendSeason") != null && !map.get("sendSeason").toString().equals(""))
			sendSeason = map.get("sendSeason").toString();
				
		// 类别组成的字符串
		String typeStr = "";
		// 类别对应金额组成的字符串
		String numStr = "";
		if (map.get("laborMaterialId") != null&& !map.get("laborMaterialId").toString().equals("")) {
			typeStr = map.get("laborMaterialId").toString();
			typeStr = typeStr.substring(1,typeStr.length() -1);
			System.out.println("bbbbbtype "+typeStr);
			
			numStr = map.get("materialNum").toString();
			numStr = numStr.substring(1,numStr.length() -1);
			System.out.println("aaaaanum "+numStr);
		}
		String[] typeArr = typeStr.split(",");
		String[] numArr = numStr.split(",");
		int arrNum = typeArr.length;
		for(int i = 0 ;i < arrNum ; i++)
		{
			HrJLaborRegister lobor = new HrJLaborRegister();
			lobor.setLaborMaterialId(Long.parseLong(typeArr[i].trim()));
			
			if(numArr[i] != null && !numArr[i].equals(""))
				lobor.setSendNum(Long.parseLong(numArr[i].trim()));
			
			lobor.setLaborRegisterId(laborRegisterId);
			lobor.setWorkCode(workCode);
			lobor.setSendDate(sendDate);
			lobor.setActualNum(actualNum);
			lobor.setStatus(status);
			lobor.setIsUse(isUse);
			lobor.setEnterpriseCode(enterpriseCode);
			lobor.setSendSeason(sendSeason);
			
			
			loborlList.add(lobor);
		}
		
		temp.setLoborlList(loborlList);
		
		return temp;
	}
	//-----------------------------------------------add by drdu 091229  end---------------------------------------	

}
		
	
		
		



	
	
	
	
	  

