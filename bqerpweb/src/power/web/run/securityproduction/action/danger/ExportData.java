package power.web.run.securityproduction.action.danger;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.util.ObjectList;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.metalSupervise.PtJReport;
import power.ejb.run.securityproduction.danger.SpCDangerAssess;
import power.ejb.run.securityproduction.danger.SpCDangerAssessFacade;
import power.ejb.run.securityproduction.danger.SpCDangerAssessFacadeRemote;
import power.ejb.run.securityproduction.danger.SpJDangerDeptRegister;
import power.ejb.run.securityproduction.danger.SpJDangerDeptRegisterFacade;
import power.ejb.run.securityproduction.danger.SpJDangerDeptRegisterFacadeRemote;
import power.ejb.run.securityproduction.danger.SpJDangerDeptValue;
import power.web.comm.AbstractAction;
import power.web.comm.UploadFileAbstractAction;
/**
 * 
 * @author qxjiao 20100729
 *@description 
 *
 */
@SuppressWarnings("serial")
public class ExportData extends UploadFileAbstractAction {
	
	private String year ;
	private SpJDangerDeptRegisterFacadeRemote remote ;
	private SpJDangerDeptRegisterFacade assess ;
	
	//private String en_code = employee.getEnterpriseCode();
	
	public ExportData() {
		remote = (SpJDangerDeptRegisterFacadeRemote)factory.getFacadeRemote("SpJDangerDeptRegisterFacade");
	}

	@SuppressWarnings("unchecked")
	public void export() throws FileNotFoundException, JSONException,IOException{
		
		String type = request.getParameter("type");
		StringBuffer exportData = new StringBuffer();
		
		if("D".equals(type)){
			exportData.append("<table border=1><th>危险源名称</th><th>相对扣分率（%）D1值</th><th>等级</th><th>监控单位</th>");
			
			List dataList = new ArrayList();
			List<Object[]> result = remote.getExportList(type, year, employee.getEnterpriseCode(),employee.getWorkerCode());
			int total = 0;
			if(result != null)
			{
				for(Object[] rec: result)
				{
					String jk_unit = "";
					String level = rec[3]==null?"":rec[3].toString();
					if(level.equals("1")||level.equals("2")){
						jk_unit = "无";
					}else if(level.equals("3")){
						jk_unit = "发电公司";
					}
					else if(level.equals("4")){
						jk_unit = "本厂";
					}
					exportData.append("<tr>"); 
					 exportData.append("<td>").append(rec[1]==null?"":rec[1]).append("</td><td>").append(rec[2]==null?"":rec[2])
					 .append("</td><td>").append(rec[3]==null?"":rec[3]+"级").append("</td><td>").append(jk_unit)
					;
					exportData.append("</td></tr>");
					total +=rec[2]==null?0: Integer.parseInt(rec[2].toString());
				}
			}
			 exportData.append("<tr><td>合计</td><td>").append(total).append("</td><td></td><td></td></tr>");
			 exportData.append("</table>");
			String filename = new String("危险源D1值报告".getBytes("gbk"), "ISO8859_1");
			this.exportData(exportData.toString(), filename);
		}else{
		
		exportData.append("<table border=1><th>危险源名称</th><th>1分</th><th>2分</th><th>3分</th><th>4分</th><th>5分</th><th>6分</th><th>7分</th><th>8分</th><th>9分</th><th>10分</th>");
		
		List dataList = new ArrayList();
		List<Object[]> result = remote.getExportList(type, year, employee.getEnterpriseCode(),employee.getWorkerCode());
		int s1=0,s2=0,s3=0,s4=0,s5=0,s6=0,s7=0,s8=0,s9=0,s10 = 0;
		if(result != null)
		{
			
			for(Object[] rec: result)
			{
				exportData.append("<tr>"); 
				 exportData.append("<td>").append(rec[1]==null?"":rec[1]).append("</td><td>").append(rec[2]==null?"":rec[2].toString().split(",").length)
				 .append("</td><td>").append(rec[3]==null?"":rec[3].toString().split(",").length).append("</td><td>").append(rec[4]==null?"":rec[4].toString().split(",").length)
				 .append("</td><td>").append(rec[5]==null?"":rec[5].toString().split(",").length).append("</td><td>").append(rec[6]==null?"":rec[6].toString().split(",").length)
				 .append("</td><td>").append(rec[7]==null?"":rec[7].toString().split(",").length).append("</td><td>").append(rec[8]==null?"":rec[8].toString().split(",").length)
				 .append("</td><td>").append(rec[9]==null?"":rec[9].toString().split(",").length).append("</td><td>").append(rec[10]==null?"":rec[10].toString().split(",").length)
				 .append("</td><td>").append(rec[11]==null?"":rec[11].toString().split(",").length);
				exportData.append("</td></tr>");
				
				s1+=rec[2]==null?0:rec[2].toString().split(",").length;
				s2+=rec[3]==null?0:rec[3].toString().split(",").length;
				s3+=rec[4]==null?0:rec[4].toString().split(",").length;
				s4+=rec[5]==null?0:rec[5].toString().split(",").length;
				s5+=rec[6]==null?0:rec[6].toString().split(",").length;
				s6+=rec[7]==null?0:rec[7].toString().split(",").length;
				s7+=rec[8]==null?0:rec[8].toString().split(",").length;
				s8+=rec[9]==null?0:rec[9].toString().split(",").length;
				s9+=rec[10]==null?0:rec[10].toString().split(",").length;
				s10+=rec[11]==null?0:rec[11].toString().split(",").length;
			}
		}
			exportData.append("<tr><td>合计</td><td>").append(s1).append("</td><td>")
						.append(s2).append("</td><td>").append(s3).append("</td><td>")
						.append(s4).append("</td><td>").append(s5).append("</td><td>")
						.append(s6).append("</td><td>").append(s7).append("</td><td>")
						.append(s8).append("</td><td>").append(s9).append("</td><td>")
						.append(s10).append("</td></tr>");
			exportData.append("</table>");
			String filename = new String("危险源L,B2值报告".getBytes("gbk"), "ISO8859_1");
			this.exportData(exportData.toString(), filename);
		}
		System.out.println("data String is :---"+exportData.toString());
	}
	
	public void exportData(String data,String fileName){
		
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition","inner; filename="+fileName+".xls");
		PrintWriter write = null;
		try {
			write = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		write.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
		 write.write(data); 
		 //response.setContentType("application/vnd.ms-excel;charset=utf-8");
		 
		write.flush();
		write.close();  
	}

	
	public SpJDangerDeptRegisterFacade getAssess() {
		return assess;
	}
	public void setAssess(SpJDangerDeptRegisterFacade assess) {
		this.assess = assess;
	}
	
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
}

