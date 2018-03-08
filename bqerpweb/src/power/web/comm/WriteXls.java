package power.web.comm;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.write.WritableCellFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import power.ejb.hr.LogUtil;

public class WriteXls {
	/** 业务名 */
	String businessName = "";
	/** 表头 */
	List<String> lstTitle = null;
	/** 表头 */
	String[] title = null;
	/**  */
	private static String ROW_NO = "行号";
	private boolean isNeededRowNo = true;
	/** 文件名 */
	private String strPropertyFileName = "outputFileType.properties";
	/** 读取设置内容 */
	private List readList = null;
	/** 读取设置内容 */
	private List setList = null;
	/** response */
	HttpServletResponse response = null;
	/** property文件 */
	public static Properties p = null;
	/** 数据对应业务ID */
	private String businessID = null;
	/** 常量字符串get */
	private static String GET_CONSTANT = "get";
	/** property中属性 */
	private String[] neededProperty = null;
	/** sheet名 */
	private String sheetName = "sheet1";
	/** 字符连接常量 */
	private static final String LINE_DEVIDE = "_";
	private HSSFWorkbook xslWorkBook = null;
	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	/**
	 * @return 表头
	 */
	public List getLstTitle() {
		return lstTitle;
	}

	/**
	 * @param 表头
	 *            设置表头
	 */
	public void setLstTitle(List lstTitle) {
		this.lstTitle = lstTitle;
	}

	/**
	 * @return response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * @param response
	 *            设置response
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public List getReadList() {
		return readList;
	}

	public void setReadList(List readList) {
		this.readList = readList;
	}

	public List getSetList() {
		return setList;
	}

	public void setSetList(List setList) {
		this.setList = setList;
	}

	public boolean isNeededRowNo() {
		return isNeededRowNo;
	}

	public void setNeededRowNo(boolean isNeededRowNo) {
		this.isNeededRowNo = isNeededRowNo;
	}

	/**
	 * 构造函数
	 * 
	 * @param 业务ID
	 * @param 导出的数据集
	 * @return
	 * @throws Exception
	 */
	public WriteXls(String businessID, List readList) {
		this.businessID = businessID;
		this.readList = readList;
	}

	/**
	 * 创建工作簿
	 * 
	 * @return
	 * @throws Exception
	 */
	public HSSFWorkbook createWorkbook(String ... cWidth) throws Exception {
		HSSFWorkbook xslWorkBook = new HSSFWorkbook();
		// 生成一个sheet
		HSSFSheet sheet = xslWorkBook.createSheet(sheetName);
		
		// 对sheet赋值
		if(cWidth!=null&&cWidth.length>0)
		{
			String strCwidth=cWidth[0];
			String [] arrWidth=strCwidth.split(";");
			for(int i=0;i<arrWidth.length;i++)
			{
				if(!arrWidth[i].equals("")&&arrWidth[i]!=null)
				{
					String [] arr=arrWidth[i].split(",");
					int cl=Integer.parseInt(arr[0]);
					int cw=Integer.parseInt(arr[1]);
					sheet.setColumnWidth((short)cl, (short)cw);
				}
			}
		}
	
		
//		sheet.setColumnWidth((short)3, (short)10000);
		setDetailValues(sheet, lstTitle, setList);
		
	
		// 返回工作簿
		return xslWorkBook;
	}

	/**
	 * 创建excel文件
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void createXsl(String ... cWidth) throws IOException, Exception {
		LogUtil.log("ExportXsl:生成EXCEL开始", Level.INFO, null);
		// 添加文件名后缀
		String strFileName = getFileName() + ".xls";
		HSSFWorkbook xslWorkBook = createWorkbook(cWidth);
		//add by  wpzhu 
//			HSSFCellStyle   style   =   xslWorkBook.createCellStyle();   //HSSFCellStyle 设定单元格风格; 
//
//		     style.setAlignment((short) 2);     //单元格水平   0   普通   1   左对齐   2   居中   3   右对齐    
		

		
		try {
			// 将文件名统一为UTF-8编码
			String strName = URLEncoder.encode(strFileName, "UTF-8");
			// 设置编码方式为UTF-8
			response.setCharacterEncoding("utf-8");
			response.setContentType("charset=utf-8");

			// 设置响应头部参数
			response.setHeader("Content-Disposition", "attachment; filename="
					+ strName);
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			response.setContentType("application/vnd.ms-excel");
			// 获得输出流
			ServletOutputStream out = response.getOutputStream();
			// 写入内容
			xslWorkBook.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw e;
		}
		LogUtil.log("ExportXsl:生成EXCEL结束", Level.INFO, null);
	}

	/**
	 * 写excel文件
	 * 
	 * @throws Exception
	 */
	public void xlsExportFile(HttpServletResponse response,String ... cWidth) throws Exception {
		// 设置response
		setResponse(response);
		// 当前时间作为文件名以部分
		Date dte = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate = sdf.format(dte);
		// excel文件每列标题头
		getBusinessPoperties();

		// 从list中读取数据设置到excel文件中
		if (readList == null) {
			return;
		}
		
		// 设置行号头
		if (isNeededRowNo) {
			lstTitle.add(0, ROW_NO);
		}
		
		// setList的初期化
		setList = new ArrayList();
		for (int i = 0; i < readList.size(); i++) {
			// 行记录
			ArrayList<Object> rowData = new ArrayList<Object>();
			Object rowGetData = readList.get(i);
			// 设置行号
			if (isNeededRowNo) {
				rowData.add(i + 1 + "");
			}
			// 需要set的属性名称
			for (int w = 0; w < neededProperty.length; w++) {
				rowData.add(rowGetData.getClass().getMethod(
						GET_CONSTANT + neededProperty[w]).invoke(rowGetData));
			}
			// 添加到工作布中
			setList.add(rowData);
		}

		// 创建导出excel文件
		createXsl(cWidth);
	}

	private String getFileName() {
		// 当前时间作为文件名以部分
		Date dte = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate = sdf.format(dte);
		return businessName + strDate;
	}

	/**
	 * 设置title
	 * 
	 * @return List<String> title
	 *//*
	private List<String> setTitle(HashMap<String, String> properties) {
		if (properties == null || properties.size() == 0) {
			return null;
		}
		// 获取title
		Collection<String> titles = properties.keySet();
		Iterator<String> iterator = titles.iterator();
		// excel文件每列标题头
		List<String> lstHeader = new ArrayList<String>();

		// 是否已经有了排序好的属性
		if (neededProperty == null)
			return null;

		// 是否需要行号
		if (isNeededRowNo) {
			lstHeader.add(ROW_NO);
		}
		// 依次追加title
		for (int i = 0; i < neededProperty.length; i++) {
			// 属性名（已经排序过）
			String tempneededProperty = neededProperty[i].toLowerCase();
			while (iterator.hasNext()) {
               // 遍历器中的属性名
				String iterName = iterator.next();
				// 如果为当前的属性
				if(iterName.toLowerCase().contains(tempneededProperty))
				lstHeader.add(properties.get(iterName));
			}
		}

		// TODO header print
		System.out.println("header print begin");
		for (int i = 0; i < lstHeader.size(); i++) {
			System.out.println(lstHeader.get(i));
		}
		System.out.println("header print end");

		return lstHeader;
	}*/

	/**
	 * 文件中获取property名
	 * 
	 * @return String[] property名
	 */
	private HashMap<String, String> getBusinessPoperties() throws IOException {
		// 为空时，返回null
		if (businessID == null) {
			return null;
		}

		HashMap<String, String> tempKeyValue = new HashMap<String, String>();
		// 不为空时，读取文件
		p = new Properties();
		p.load(ReadXls.class.getResourceAsStream(strPropertyFileName));
		// 取得名字
		Enumeration<?> names = p.propertyNames();
		// 遍历找到对应的业务的colomn名字
		while (names.hasMoreElements()) {
			String tempName = names.nextElement().toString();
			// 该业务的colomn名字
			if (tempName.contains(businessID)) {
				String tempValue = p.getProperty(tempName);
				tempKeyValue.put(tempName, tempValue);
			}
		}

		// Property的排序设置处理
		setNeededProperty(new ArrayList<String>(tempKeyValue.keySet()));
		return tempKeyValue;
	}

	/**
	 * 属性的排序处理
	 * 
	 * @param HashMap
	 *            属性对
	 */
	private void setNeededProperty(List<String> tempKey) {
		if (tempKey == null) {
			return;
		}
		int[] order = new int[tempKey.size()];
		lstTitle = new ArrayList<String>();
		title = new String[tempKey.size()];
		neededProperty = new String[tempKey.size()];
		for (int i = 0; i < tempKey.size(); i++) {
			// 分割key的内容
			String[] text = tempKey.get(i).split(LINE_DEVIDE);
			// 形式的正确判断
			if (text.length != 3)
				continue;
			else {
				// 顺序的设置
				order[i] = Integer.parseInt(text[1]);
				// 属性的设置
				neededProperty[i] = ("" + text[2].charAt(0)).toUpperCase()
						+ text[2].substring(1);
				// title设置
				title[i] = p.getProperty(tempKey.get(i));
			}

		}

		// 冒泡排序
		for (int i = 0; i < order.length; i++)
			for (int j = i + 1; j < order.length; j++) {
				if (order[i] > order[j]) {
					int tempOrder = order[i];
					order[i] = order[j];
					order[j] = tempOrder;
					String tempString = neededProperty[i];
					neededProperty[i] = neededProperty[j];
					neededProperty[j] = tempString;
					String tempTitle = title[i];
					title[i] = title[j];
					title[j] = tempTitle;
				}
			}
		// 设置title
		for(int i =0;i<title.length;i++){
			lstTitle.add(title[i]);
		}
	}

	/**
	 * 设表内容具体值
	 * 
	 * @param sheet
	 * @param lstHeader
	 *            表头
	 * @param lstRow
	 *            表行
	 * @throws Exception
	 */
	private void setDetailValues(HSSFSheet sheet, List lstHeader, List lstRow)
			throws Exception {
		int intStartRow = 0;
		List lstDtlRow = null;
		// 生成EXCEL行
		HSSFRow row = sheet.createRow(intStartRow);
		
		// 设一行cell的值
		setDtlCellValues(row, lstHeader);
		intStartRow++;
		for (int i = 0; i < lstRow.size(); i++) {
			// 生成EXCEL行
			row = sheet.createRow(intStartRow);
			lstDtlRow = (List) lstRow.get(i);
			
			// 设一行cell的值
			setDtlCellValues(row, lstDtlRow);
			intStartRow++;
		}
	}

	/**
	 * 设一行cell的值
	 * 
	 * @param row
	 *            一行
	 * @param lstRow
	 *            行list
	 * @throws Exception
	 */




	private void setDtlCellValues(HSSFRow row, List lstRow) throws Exception {
		HSSFCell cell = null;
		try {
//			HSSFWorkbook xslWorkBook1 = new HSSFWorkbook();
//			HSSFCellStyle cellStyle = xslWorkBook1.createCellStyle();   
//			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER );
			
			
			for (int i = 0; i < lstRow.size(); i++) {
				// 生成EXCEL单元格
				cell = row.createCell((short) i);
			  // HSSFCell 设定单元格;
//				HSSFCellStyle   style   =   xslWorkBook.createCellStyle();   
//			    style.setAlignment((short) 2);    
				//wzhyan 2010-10-15 加入jxls 包冲突注释下面一行
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				

				if (lstRow.get(i) != null) {
					String cellValue = lstRow.get(i).toString();
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("   \n"+new String(cellValue.getBytes("UTF-16"),//modify by  wpzhu 
							"UTF-16"));
				}else
				{//add by wpzhu 20100709
					
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(new String(""));
				}
				//cell.setCellStyle(cellStyle);
				
			}
		} catch (Exception e) {
			throw e;
		}

	}
}