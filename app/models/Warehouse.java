package models;


import javax.persistence.*;
import play.data.format.Formats;

import org.joda.time.DateTime;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"owner_id", "name"}))
public class Warehouse extends Model{ 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1372072316570563931L;

	@Id
	public Long id;
	
	@ManyToOne
	public User owner;
	
	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	public DateTime created;

	public String name;

	public String comment;
	
	public boolean isPrivate;
		 
	public static final Finder<Long, Warehouse> find = new Finder<Long, Warehouse>(
			Long.class, Warehouse.class);

	@Override
	public String toString() {
		return "Warehouse [id=" + id + ", owner=" + owner + ", created="
				+ created + ", name=" + name + ", comment=" + comment + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Finder<Long, Warehouse> getFind() {
		return find;
	}
	
	
	
}
