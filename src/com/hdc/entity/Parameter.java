package com.hdc.entity;

public class Parameter {
	//模糊查询
	private String searchName;
	private String searchValue;
	//分页
	private Integer page;
	private Integer rows;
	private String sort; 
	private String order;
	//高级查询
	private String searchAnds;
	private String searchColumnNames;
	private String searchConditions;
	private String searchVals;
	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public String getSearchAnds() {
		return searchAnds;
	}
	public void setSearchAnds(String searchAnds) {
		this.searchAnds = searchAnds;
	}
	public String getSearchColumnNames() {
		return searchColumnNames;
	}
	public void setSearchColumnNames(String searchColumnNames) {
		this.searchColumnNames = searchColumnNames;
	}
	public String getSearchConditions() {
		return searchConditions;
	}
	public void setSearchConditions(String searchConditions) {
		this.searchConditions = searchConditions;
	}
	public String getSearchVals() {
		return searchVals;
	}
	public void setSearchVals(String searchVals) {
		this.searchVals = searchVals;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
}
