package models;

import javax.persistence.*;

import play.db.ebean.Model;

@Entity
@Table(name = "categories")
public class Category extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 230675424813498892L;

	@Id
	public Long id;
	
	public Category parentOfCategory;
	
	public String names;
	
	@OneToOne
	@Column(nullable = true )
	public Category parentCategory;
	
	public boolean isGlobal;
	
	@ManyToOne
	public User owner;
	
	
}
