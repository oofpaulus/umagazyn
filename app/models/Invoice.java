package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Invoice extends Model{

	@Id 
	public Long id;
	
	
	
}
