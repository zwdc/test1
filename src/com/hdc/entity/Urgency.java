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
 * 缓急程度（先留着，存到了app.json中）
 * @author ZML
 *
 */
@Entity
@Table(name = "urgency")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Urgency extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8043344151811550005L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 5, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "title", length = 500)
	private String title;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
