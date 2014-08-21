package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@JsonIgnore
	@ManyToOne
	private Warehouse warehouse;
	
	@ManyToOne
	private User owner;
	
	@JsonIgnore
	@ManyToOne
	private Category category;
	
	@JsonIgnore
	@OneToMany(mappedBy="component", cascade = CascadeType.ALL)
	private List<ProjectJoinComponent> projects;
	
	
	public List<ProjectJoinComponent> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectJoinComponent> projects) {
		this.projects = projects;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}


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

	@Override
	public String toString() {
		return "Component [id=" + id + ", name=" + name + ", count=" + count
				+ ", warehouse=" + warehouse + ", category=" + category + "]";
	}
	
	
}
