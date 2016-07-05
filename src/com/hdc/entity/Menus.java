package com.hdc.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 前端展示菜单用
 * @author zhao
 *
 */
public class Menus implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8756339448786244160L;
	private Integer id;
	private Integer parentId;
	private String name;
	private String url;
	private String iconCls;
	private List<Menus> children = new ArrayList<Menus>();
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public List<Menus> getChildren() {
		return children;
	}
	public void setChildren(List<Menus> children) {
		this.children = children;
	}
	
}
