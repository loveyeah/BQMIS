package power.web.hr.jxlreport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.opensymphony.util.jxl.JXLExcelUtils;

import jxl.Cell;
import jxl.CellType;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.CellFormat;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import power.ejb.report.JxtReportNewEmpFacadeRemote;
import power.ejb.report.form.JxtReportNewEmpForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class HrJxlReportAction  extends AbstractAction{ 
	
	
	protected JxtReportNewEmpFacadeRemote  remote;
	public HrJxlReportAction()
	{
		remote = (JxtReportNewEmpFacadeRemote) factory
		.getFacadeRemote("JxtReportNewFacade");
		
	}
	
	public void newWorkerOrder() throws Exception{
		InputStream is = HrJxlReportAction.class.getResourceAsStream("xingjingyuangongtemplate.xls");
		 WorkbookSettings setting = new WorkbookSettings();
		java.util.Locale locale = new java.util.Locale("zh", "CN");
		setting.setLocale(locale);
		setting.setEncoding("ISO-8859-1");
		Workbook fromWb = Workbook.getWorkbook(is, setting);
		is.close(); 
		response.setContentType("application/vnd.ms-excel"); 
		response.setHeader("Content-Disposition","attachment;filename="+ new String("新进通知单".getBytes("GB2312"), "ISO_8859_1") +".xls"); 
		OutputStream out = response.getOutputStream(); 
		WritableWorkbook toWb = Workbook.createWorkbook(out, fromWb);
		fromWb.close();
		
		WritableSheet sheet = toWb.getSheet(0);
		
		//取得小标题的数据
		String empId=request.getParameter("empId");
		String advicenoteNo=request.getParameter("advicenoteNo");
	     String noteNO=   ( advicenoteNo==null||"".equals(advicenoteNo))?"":advicenoteNo;
		String printDate=request.getParameter("printDate");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar pdc = Calendar.getInstance();
		String  year = "";
		String month="";
		String date = "";
		if(printDate!=null&&!"".equals(printDate)&&!"null".equals(printDate))
		{
			year=printDate.substring(0,4);
			month=printDate.substring(5,7);
			date=printDate.substring(8,10);
		}
		String newEmpids=request.getParameter("newEmpids");
	 
		 
	    
	    
		//取得单元格数据
		List<JxtReportNewEmpForm> result = null;
		result=  remote.getNewEmpInfo(newEmpids);
	
		JXLExcelUtils jxl = new JXLExcelUtils();
		
		//子标题
		WritableCell  cell = sheet.getWritableCell(0, 12); 
		jxl.changCellText(sheet,cell,"查照          "+ year+"年"+month+"月"+date+"日                人新进（"+pdc.get(Calendar.YEAR)+"）第"+noteNO+"号");
	    if(result != null)
		{
			for(int i = 0 ;i<result.size();i++){
				 if(i==5)
				 {
				    	break;
				 }
				JxtReportNewEmpForm f = result.get(i);
				int row = 15+i;
				cell = sheet.getWritableCell(0, row);    
				jxl.changCellText(sheet,cell,f.getChsName());
			    cell = sheet.getWritableCell(1, row);  
			    jxl.changCellText(sheet,cell,f.getStationName());
			    
			    cell = sheet.getWritableCell(2, row);  
			    jxl.changCellText(sheet,cell,f.getStaLevelName());
			    
			    cell = sheet.getWritableCell(3, row);  
			    jxl.changCellText(sheet,cell,f.getSalaryPoint());
			    
			    cell = sheet.getWritableCell(4, row);  
			    jxl.changCellText(sheet,cell,f.getDeptName());
			    
			    cell = sheet.getWritableCell(5, row);  
			    jxl.changCellText(sheet,cell,f.getMissonYear());
			    
			    cell = sheet.getWritableCell(6, row);  
			    jxl.changCellText(sheet,cell,f.getMissonMonth());
			    
			    cell = sheet.getWritableCell(7, row);  
			    jxl.changCellText(sheet,cell,f.getMissonDay());
			    
			    cell = sheet.getWritableCell(8, row);  
			    jxl.changCellText(sheet,cell,f.getStarsalaryYear());
			    
			    cell = sheet.getWritableCell(9, row);  
			    jxl.changCellText(sheet,cell,f.getStarsalaryMonth());
			    
			    cell = sheet.getWritableCell(10, row);  
			    jxl.changCellText(sheet,cell,f.getStarsalaryDay());
			    
			    cell = sheet.getWritableCell(11, row);  
			    jxl.changCellText(sheet,cell,f.getMemo());
			}
		}
		 cell = sheet.getWritableCell(0, 19); 
		 jxl.changCellText(sheet, cell, String.format("厂长：                   人力资源部主任：                         制单：%s", "刘辉" /*super.employee.getWorkerName()*/));

		 
        toWb.write();
		toWb.close();
		out.close();
		
	}
	
	public void linZhiOrder() throws BiffException, IOException, ParseException, WriteException{
		
		InputStream is = HrJxlReportAction.class.getResourceAsStream("lizhi.xls");
		 WorkbookSettings setting = new WorkbookSettings();
		java.util.Locale locale = new java.util.Locale("zh", "CN");
		setting.setLocale(locale);
		setting.setEncoding("ISO-8859-1");
		Workbook fromWb = Workbook.getWorkbook(is, setting);
		is.close(); 
		response.setContentType("application/vnd.ms-excel"); 
		response.setHeader("Content-Disposition","attachment;filename="+ new String("离厂通知单".getBytes("GB2312"), "ISO_8859_1") +".xls"); 
		OutputStream out = response.getOutputStream(); 
		WritableWorkbook toWb = Workbook.createWorkbook(out, fromWb);
		fromWb.close();
		
		WritableSheet sheet = toWb.getSheet(0);
		
		//取得小标题的数据
		String newEmpids=request.getParameter("newEmpids");
		System.out.println("the newEmpids"+newEmpids);
		String advicenoteNo=request.getParameter("advicenoteNo");
	     String noteNO=   ( advicenoteNo==null||"".equals(advicenoteNo))?"":advicenoteNo;
		String printDate=request.getParameter("printDate");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar pdc = Calendar.getInstance();
		String  year = "";
		String month="";
		String date = "";
		if(printDate!=null&&!"".equals(printDate)&&!"null".equals(printDate))
		{
			year=printDate.substring(0,4);
			month=printDate.substring(5,7);
			date=printDate.substring(8,10);
		}
		
	
	    
		//取得单元格数据
		List<Object[]> result = null;
		result=  remote.linZhiOrder(newEmpids);
	
		JXLExcelUtils jxl = new JXLExcelUtils();
		
		//子标题
		WritableCell  cell = sheet.getWritableCell(0, 4);  
	    jxl.changCellText(sheet, cell, "查照          "+year+"年"+month+"月"+date+"日                人离字（"+pdc.get(Calendar.YEAR)+"）"+noteNO+"号");

		if(result != null)
		{
			for(int i = 0 ;i<result.size();i++){
				 if(i==5)
				 {
				    	break;
				 }
				Object[] f = result.get(i);
				int row = 7+i;
				cell = sheet.getWritableCell(0, row);  
				 jxl.changCellText(sheet, cell,f[0] );
			    cell = sheet.getWritableCell(1, row);  
			    jxl.changCellText(sheet, cell,f[1] );
			    
			    cell = sheet.getWritableCell(2, row);  
			    jxl.changCellText(sheet, cell,f[2] );
			    
			    cell = sheet.getWritableCell(3, row);  
			    jxl.changCellText(sheet, cell,f[3] );
			    
			    cell = sheet.getWritableCell(4, row);  
			    jxl.changCellText(sheet, cell,f[4] );
			    
			    cell = sheet.getWritableCell(5, row);  
			    jxl.changCellText(sheet, cell,f[5] );
			    
			    cell = sheet.getWritableCell(6, row);  
			    jxl.changCellText(sheet, cell,f[6] );
			    
			    cell = sheet.getWritableCell(7, row);  
			    
			    jxl.changCellText(sheet, cell,f[7] );
			    
			    cell = sheet.getWritableCell(8, row);  
			    jxl.changCellText(sheet, cell,f[8] );
			    
			    cell = sheet.getWritableCell(9, row);  
			    jxl.changCellText(sheet, cell,f[9] );
			    
			}
		}
		 cell = sheet.getWritableCell(0, 12); 
		 jxl.changCellText(sheet, cell,String.format("厂长：                   人力资源部主任：                         制单：%s", "刘辉") );
		 
       toWb.write();
		toWb.close();
		out.close();
	
	
		
		
		
	
	}
	public void deptInMoveOrder() throws BiffException, IOException, ParseException, WriteException{
		
		
		InputStream is = HrJxlReportAction.class.getResourceAsStream("bumenneibu.xls");
		 WorkbookSettings setting = new WorkbookSettings();
		java.util.Locale locale = new java.util.Locale("zh", "CN");
		setting.setLocale(locale);
		setting.setEncoding("ISO-8859-1");
		Workbook fromWb = Workbook.getWorkbook(is, setting);
		is.close(); 
		response.setContentType("application/vnd.ms-excel"); 
		response.setHeader("Content-Disposition","attachment;filename="+ new String("部门内部调整通知单".getBytes("GB2312"), "ISO_8859_1") +".xls"); 
		OutputStream out = response.getOutputStream(); 
		WritableWorkbook toWb = Workbook.createWorkbook(out, fromWb);
		fromWb.close();
		
		WritableSheet sheet = toWb.getSheet(0);
		
		//取得小标题的数据
		String stationRemoveIds=request.getParameter("stationRemoveIds");
		String requisitionNo=request.getParameter("requisitionNo");
		
	     String noteNO=   ( requisitionNo==null||"".equals(requisitionNo))?"":requisitionNo;
	     if(noteNO!=null&&!"".equals(noteNO))
		    {
		    	if(Integer.parseInt(noteNO)<10)
		    	{
		    		noteNO="0"+noteNO;
		    	}
		    	
		    }
		String printDate=request.getParameter("printDate");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar pdc = Calendar.getInstance();
		
		String  year = "";
		String month="";
		String date = "";
		if(printDate!=null&&!"".equals(printDate)&&!"null".equals(printDate))
		{
			year=printDate.substring(0,4);
			month=printDate.substring(5,7);
			date=printDate.substring(8,10);
		}
		
	
	    
		//取得单元格数据
		List<Object[]> result = null;
		result=  remote.deptInMoveOrder(stationRemoveIds);
	
		JXLExcelUtils jxl = new JXLExcelUtils();
		
		//子标题
		WritableCell  cell = sheet.getWritableCell(2, 2);
	    jxl.changCellText(sheet, cell,year+"年"+month+"月"+date+"日 ");

	    
	    
		  cell = sheet.getWritableCell(6, 2);  
		  jxl.changCellText(sheet, cell," 内调字（"+pdc.get(Calendar.YEAR)+"）"+noteNO+"号");

		if(result != null)
		{
			for(int i = 0 ;i<result.size();i++){
				 if(i==5)
				 {
				    	break;
				 }
				Object[] f = result.get(i);
				int row = 5+i;
				cell = sheet.getWritableCell(0, row); 
				jxl.changCellText(sheet, cell,f[0]);
			    cell = sheet.getWritableCell(1, row);
				jxl.changCellText(sheet, cell,f[1]);
			    
			    cell = sheet.getWritableCell(2, row);
				jxl.changCellText(sheet, cell,f[2]);  
			    
			    cell = sheet.getWritableCell(3, row); 
				jxl.changCellText(sheet, cell,f[3]); 
			    
			    cell = sheet.getWritableCell(4, row);  
				jxl.changCellText(sheet, cell,f[4]);
			    
			    cell = sheet.getWritableCell(5, row);  
				jxl.changCellText(sheet, cell,f[5]);
			    
			    cell = sheet.getWritableCell(6, row); 
				jxl.changCellText(sheet, cell,f[6]); 
			    
			    cell = sheet.getWritableCell(7, row); 
				jxl.changCellText(sheet, cell,f[7]); 
			    
			    cell = sheet.getWritableCell(8, row); 
				jxl.changCellText(sheet, cell,f[8]); 
			    
			    cell = sheet.getWritableCell(9, row);  
				jxl.changCellText(sheet, cell,f[9]);
			    
			    cell = sheet.getWritableCell(10, row);  
				jxl.changCellText(sheet, cell,f[10]);
			    
			    cell = sheet.getWritableCell(11, row);  
				jxl.changCellText(sheet, cell,f[11]);
			    
			    cell = sheet.getWritableCell(12, row);  
				jxl.changCellText(sheet, cell,f[12]);
			}
		}
		 cell = sheet.getWritableCell(6, 11); 
		 
		jxl.changCellText(sheet, cell,String.format("                     制单：%s",  "刘辉"));
		
       toWb.write();
		toWb.close();
		
		out.close();
	
	}
	public void deptBetMoveOrder() throws BiffException, IOException, ParseException, WriteException{
		
		InputStream is = HrJxlReportAction.class.getResourceAsStream("bumenzhijian.xls");
		 WorkbookSettings setting = new WorkbookSettings();
		java.util.Locale locale = new java.util.Locale("zh", "CN");
		setting.setLocale(locale);
		setting.setEncoding("ISO-8859-1");
		Workbook fromWb = Workbook.getWorkbook(is, setting);
		is.close(); 
		response.setContentType("application/vnd.ms-excel"); 
		response.setHeader("Content-Disposition","attachment;filename="+ new String("内调通知单".getBytes("GB2312"), "ISO_8859_1") +".xls"); 
		OutputStream out = response.getOutputStream(); 
		WritableWorkbook toWb = Workbook.createWorkbook(out, fromWb);
		fromWb.close();
		
		WritableSheet sheet = toWb.getSheet(0);
		
		//取得小标题的数据
		String stationRemoveIds=request.getParameter("stationRemoveIds");
		String requisitionNo=request.getParameter("requisitionNo");
	     String noteNO=   ( requisitionNo==null||"".equals(requisitionNo))?"":requisitionNo;
		String printDate=(request.getParameter("printDate")==null||"".equals(request.getParameter("printDate")))?"":request.getParameter("printDate");
		Calendar pdc = Calendar.getInstance();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		
		String  year = "";
		String month="";
		String date = "";
		if(printDate!=null&&!"".equals(printDate)&&!"null".equals(printDate))
		{
			year=printDate.substring(0,4);
			month=printDate.substring(5,7);
			date=printDate.substring(8,10);
		}
		
	
	    
		//取得单元格数据
		List<Object[]> result = null;
		result=  remote.deptBetMoveOrder(stationRemoveIds);
	
		JXLExcelUtils jxl = new JXLExcelUtils();
		
		//子标题
		WritableCell  cell = sheet.getWritableCell(5, 6); 
		jxl.changCellText(sheet, cell,year+"年"+month+"月"+date+"日  ");
         
	      cell = sheet.getWritableCell(9, 6);  
	      jxl.changCellText(sheet, cell," 人内调字（"+pdc.get(Calendar.YEAR)+"）第"+noteNO+"号");
	         
	      
	      cell = sheet.getWritableCell(2, 7);  
	      jxl.changCellText(sheet, cell,"未  调  动  以  前");
	        
	      cell = sheet.getWritableCell(6, 7); 
	      jxl.changCellText(sheet, cell,"调   动   以   后"); 
	         
		if(result != null)
		{
			for(int i = 0 ;i<result.size();i++){
				 if(i==5)
				 {
				    	break;
				 }
				Object[] f = result.get(i);
				
				int row = 9+i;
				cell = sheet.getWritableCell(1, row);  
			    jxl.changCellText(sheet, cell,f[0]);
			    cell = sheet.getWritableCell(2, row);  
			    jxl.changCellText(sheet, cell,f[1]);
			    
			    cell = sheet.getWritableCell(3, row);
			    jxl.changCellText(sheet, cell,f[2]);  
			    
			    cell = sheet.getWritableCell(4, row);
			    jxl.changCellText(sheet, cell,f[3]);  
			    
			    cell = sheet.getWritableCell(5, row);
			    jxl.changCellText(sheet, cell,f[4]);  
		    
			    cell = sheet.getWritableCell(6, row);
			    jxl.changCellText(sheet, cell,f[5]);  
			    
			    cell = sheet.getWritableCell(7, row);
			    jxl.changCellText(sheet, cell,f[6]);  
			    
			    cell = sheet.getWritableCell(8, row);
			    jxl.changCellText(sheet, cell,f[7]);  
			    
			    cell = sheet.getWritableCell(9, row);
			    jxl.changCellText(sheet, cell,f[8]);  
			    
			    cell = sheet.getWritableCell(10, row);
			    jxl.changCellText(sheet, cell,f[9]);  
			    
			    cell = sheet.getWritableCell(11, row);
			    jxl.changCellText(sheet, cell,f[10]);  
			    
			    cell = sheet.getWritableCell(12, row);
			    jxl.changCellText(sheet, cell,f[11]);  
			    cell = sheet.getWritableCell(13, row);
			    jxl.changCellText(sheet, cell,f[12]);  
			}
		}
		 cell = sheet.getWritableCell(1, 14); 
		    jxl.changCellText(sheet, cell,String.format("厂长：                   人力资源部主任：                  制单：%s",  "刘辉"));  
		 
       toWb.write();
		toWb.close();
		out.close();
	
	
	
	}
	public void neiBuJieDiaoOrder() throws BiffException, IOException, ParseException, WriteException{

		
		InputStream is = HrJxlReportAction.class.getResourceAsStream("neibujiediao.xls");
		 WorkbookSettings setting = new WorkbookSettings();
		java.util.Locale locale = new java.util.Locale("zh", "CN");
		setting.setLocale(locale);
		setting.setEncoding("ISO-8859-1");
		Workbook fromWb = Workbook.getWorkbook(is, setting);
		is.close(); 
		response.setContentType("application/vnd.ms-excel"); 
		response.setHeader("Content-Disposition","attachment;filename="+ new String("内部借调通知单".getBytes("GB2312"), "ISO_8859_1") +".xls"); 
		OutputStream out = response.getOutputStream(); 
		WritableWorkbook toWb = Workbook.createWorkbook(out, fromWb);
		fromWb.close();
		
		WritableSheet sheet = toWb.getSheet(0);
		
		//取得小标题的数据
		String stationRemoveIds=request.getParameter("stationRemoveIds");
		String requisitionNo=request.getParameter("requisitionNo");
	     String noteNO=   ( requisitionNo==null||"".equals(requisitionNo))?"":requisitionNo;
	    if(noteNO!=null&&!"".equals(noteNO))
	    {
	    	if(Integer.parseInt(noteNO)<10)
	    	{
	    		noteNO="0"+noteNO;
	    	}
	    	
	    }
		String printDate=request.getParameter("printDate");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		System.out.println("the printdate");
		Calendar pdc = Calendar.getInstance();
		String  year = "";
		String month="";
		String date = "";
		if(printDate!=null&&!"".equals(printDate)&&!"null".equals(printDate))
		{
			year=printDate.substring(0,4);
			month=printDate.substring(5,7);
			date=printDate.substring(8,10);
		}
		
	
	    
		//取得单元格数据
		List<Object[]> result = null;
		result=  remote.deptBetMoveOrder(stationRemoveIds);
	
		JXLExcelUtils jxl = new JXLExcelUtils();
		
		//子标题
		WritableCell  cell = sheet.getWritableCell(2, 1);  
	    jxl.changCellText(sheet, cell,year+"年"+month+"月"+date+"日");
         
	    cell = sheet.getWritableCell(6, 1);   
	    jxl.changCellText(sheet, cell," 借调字（"+pdc.get(Calendar.YEAR)+"）"+noteNO+"号");

		if(result != null)
		{
			for(int i = 0 ;i<result.size();i++){
				 if(i==5)
				 {
				    	break;
				 }
				Object[] f = result.get(i);
				int row = 4+i;
				cell = sheet.getWritableCell(0, row); 
			    jxl.changCellText(sheet, cell, f[0]);
			    cell = sheet.getWritableCell(1, row); 
			    jxl.changCellText(sheet, cell, f[1]); 
			    
			    cell = sheet.getWritableCell(2, row);
			    jxl.changCellText(sheet, cell, f[2]);  
			    
			    cell = sheet.getWritableCell(3, row);
			    jxl.changCellText(sheet, cell, f[3]);  
			    
			    cell = sheet.getWritableCell(4, row);
			    jxl.changCellText(sheet, cell, f[4]);  
			    
			    cell = sheet.getWritableCell(5, row);
			    jxl.changCellText(sheet, cell, f[5]);  
			    
			    cell = sheet.getWritableCell(6, row);  
			    jxl.changCellText(sheet, cell, f[6]);
			    
			    cell = sheet.getWritableCell(7, row);  
			    jxl.changCellText(sheet, cell, f[7]);
			    
			    cell = sheet.getWritableCell(8, row);  
			    jxl.changCellText(sheet, cell, f[8]);
			    
			    cell = sheet.getWritableCell(9, row);  
			    jxl.changCellText(sheet, cell, f[9]);
			    
			    cell = sheet.getWritableCell(10, row);  
			    jxl.changCellText(sheet, cell, f[10]);
			    
			    cell = sheet.getWritableCell(11, row);  
			    jxl.changCellText(sheet, cell, f[11]);
			    
			    cell = sheet.getWritableCell(12, row);  
			    jxl.changCellText(sheet, cell, f[12]);
			}
		}
		 cell = sheet.getWritableCell(7, 12); 
		    jxl.changCellText(sheet, cell, String.format("   制单：%s", "刘辉"));
		 
       toWb.write();
		toWb.close();
		out.close();
	
	
	
	
	
	}
}
