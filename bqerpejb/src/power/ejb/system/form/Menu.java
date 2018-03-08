package power.ejb.system.form;

import java.util.List;

/**
 * 功能模块功能
 * @author wzhyan
 *
 */
public class Menu implements java.io.Serializable{ 
	private static final long serialVersionUID = -6400317352156949573L;
	private Long id;
	private String text;
	private String iconCls;
	private String isLeaf;
	private String url;
	private String memo;
	private List<Menu> menu; 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<Menu> getMenu() {
		return menu;
	}
	public void setMenu(List<Menu> menu) {
		this.menu = menu;
	} 
 
	 
}
