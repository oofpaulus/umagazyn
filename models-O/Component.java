package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Component extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6268192388421014553L;

	@Id
	public Long id;
	
	public String name;
	
	public Integer count;
	
		
		
		
	
}
