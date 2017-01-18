package org.profit.app.data;

import org.profit.persist.domain.sys.Menu;

import java.util.ArrayList;
import java.util.List;

public class MenuNode {

	private String id;
	private String parentId;
	private String text;
	private String iconCls;
	private String qtip;
	private boolean leaf;
	private List<MenuNode> children = new ArrayList<MenuNode>();

	public MenuNode(Menu menu) {
		this.id = menu.getMenuId();
		this.parentId = menu.getParentId();
		this.text = menu.getText();
		this.iconCls = menu.getIconCls();
		this.qtip = menu.getQtip();
		this.leaf = true;
	}
	
	public void addChildNode(MenuNode childNode) {
		children.add(childNode);
		this.leaf = false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public String getQtip() {
		return qtip;
	}

	public void setQtip(String qtip) {
		this.qtip = qtip;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public List<MenuNode> getChildren() {
		return children;
	}

	public void setChildren(List<MenuNode> children) {
		this.children = children;
	}
}
