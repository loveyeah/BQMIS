package power.web.comm;

import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XlsConfigParser extends DefaultHandler {

	private static XlsConfigBean configBean = null;

	public static XlsConfigBean getXlsConfigBean() {
		if (configBean == null) {
			configBean = new XlsConfigBean();

			// 解析配置的xml文件，并且设置bean内容
			try {
				SAXParserFactory spfactory = SAXParserFactory.newInstance();
				SAXParser parser = spfactory.newSAXParser();
				parser.parse(XlsConfigParser.class
						.getResourceAsStream("xlsConfig.xml"),
						new XlsConfigParser());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return configBean;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {
		
		// 对应的excel追加
		if ("excel".equals(qName)) {
			XlsConfigBean.Excel excel = new XlsConfigBean.Excel();
			configBean.addExcel(excel);
			excel.setId(attributes.getValue("id"));
		} 
		// 对应的column追加
		else if ("column".equals(qName)) {
			List<?> excels = configBean.getExcels();
			XlsConfigBean.Excel excel = (XlsConfigBean.Excel) excels.get(excels
					.size() - 1);
			XlsConfigBean.Excel.Column column = new XlsConfigBean.Excel.Column();
			excel.addColumn(column);

			column.setId(attributes.getValue("id"));
			column.setName(attributes.getValue("name"));
			column.setType(attributes.getValue("type"));
			column.setLength(attributes.getValue("length"));
			column.setEmpty(attributes.getValue("empty"));
			column.setKey(attributes.getValue("key"));
			column.setMaxValue(attributes.getValue("maxValue"));
			column.setMinValue(attributes.getValue("minValue"));
			column.setHalf(attributes.getValue("half"));
		}
		// 对应的check的追加
		else if ("check".equals(qName)) {
			List<?> excels = configBean.getExcels();
			XlsConfigBean.Excel excel = (XlsConfigBean.Excel) excels.get(excels
					.size() - 1);
			XlsConfigBean.Excel.Check check = new XlsConfigBean.Excel.Check();
			excel.addCheck(check);

			check.setMethod(attributes.getValue("method"));
			check.setClazz(attributes.getValue("class"));
			check.setArgs(attributes.getValue("args"));
		}
		// 对应的隐藏列的追加
		else if("hiddenColumn".equals(qName)){
			List<?> excels = configBean.getExcels();
			XlsConfigBean.Excel excel = (XlsConfigBean.Excel) excels.get(excels
					.size() - 1);
			XlsConfigBean.Excel.HiddenColumn hiddenColumn = new XlsConfigBean.Excel.HiddenColumn();
			excel.setHiddenColumn(hiddenColumn);
			hiddenColumn.setId(attributes.getValue("id"));
		}
	}
}
