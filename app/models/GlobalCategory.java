package models;

import java.util.List;

import javax.persistence.*;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class GlobalCategory extends Model {

	@Id
	private Long Id;
	
	private String name;
	
	@ManyToOne
	@Column(nullable = true )
	private GlobalCategory parent;
	
	@OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
	private List<GlobalCategory> children;
	
	
	public static final Finder<Long, GlobalCategory> find = new Finder<Long, GlobalCategory>(
			Long.class, GlobalCategory.class);
	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GlobalCategory getParent() {
		return parent;
	}

	public void setParent(GlobalCategory parent) {
		this.parent = parent;
	}

	public List<GlobalCategory> getChildren() {
		return children;
	}

	public void setChildren(List<GlobalCategory> children) {
		this.children = children;
	}

	
	
	
}
