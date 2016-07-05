package com.hdc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName: Resource
 * @Description:资源表
 * @author: zml
 * @date: 2015-6-6 下午15:59:48
 *
 */

@Entity
@Table(name = "RESOURCES")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Resource implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7420427268913550324L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 5, nullable = false, unique = true)
	private Integer id; 							//编号
	
	@Column(name = "name", length = 50, nullable = false)
    private String name; 							//资源名称
	
	@Column(name = "type", length = 20, nullable = false)
    private String type;							//资源类型
	
	@Column(name = "url", length = 100)
    private String url; 							//资源路径
	
	@Column(name = "permission", length = 100, nullable = false)
    private String permission; 						//权限字符串
	
	@Column(name = "parentId", length = 5, nullable = false)
    private Integer parentId; 						//父编号
	
	@Column(name="sort", length = 2)				//排序编号
	private Integer sort;
	
	@Column(name = "note", length = 300)
    private String note; 							//说明
	
	@Column(name = "is_delete", length = 1)
    private Integer isDelete;
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public boolean isRootNode() {
        return parentId == 0;
    }

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj != null && obj instanceof Resource) {
			Resource resource = (Resource) obj;
			return this.id == resource.getId() && this.name.equals(resource.name);
		} else {
			return false;
		}
	}
}
