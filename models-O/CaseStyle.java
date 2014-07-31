package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class CaseStyle extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7073672848844234120L;

	@Id
	public Long id;
	
	public String name;
	
	public Integer numberOfPins;
	
}
