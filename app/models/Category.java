package models;

import java.util.List;

import javax.persistence.*;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(name = "categories")
public class Category extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 230675424813498892L;

	@Id
	private Long id;
	
	private String names;
	
	@ManyToOne
	@Column(nullable = true )
	private Category parent;
	
	@OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
	private List<Category> children;
	
	
	public static final Finder<Long, Category> find = new Finder<Long, Category>(
			Long.class, Category.class);
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public List<Category> getChildren() {
		return children;
	}

	public void setChildren(List<Category> children) {
		this.children = children;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@ManyToOne
	private User owner;
	
	
}
