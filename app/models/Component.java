package models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Component extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6268192388421014553L;

	@Id
	private Long id;
		
	private String name;
	
	private Integer count;
	
	@ManyToOne
	private Warehouse warehouse;	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public static final Finder<Long, Component> find = new Finder<Long, Component>(
			Long.class, Component.class);
	
	public void setWarehouse(Warehouse warehouse)
	{
		this.warehouse = warehouse;
	}
	
	public Warehouse getWarehouse()
	{
		return warehouse;
	}
		
	public void setName(String name)
	{
		this.name = name;
	}
		
	public String getName()
	{
		return name;
	}
	
}
