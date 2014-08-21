package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import play.Logger;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(name = "categories")
public class Category extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 23067542457498892L;

	@Id
	private Long id;
	
	private String name;

	@ManyToOne
	private User owner;
	
	@ManyToOne
	@Column(nullable = true )
	private Category parent;
	
	@OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
	private List<Category> children;
	
	@ManyToOne
	@Column(nullable = true )
	private GlobalCategory globalCategoryEquivalent;
	
	public static final Finder<Long, Category> find = new Finder<Long, Category>(
			Long.class, Category.class);
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public GlobalCategory getGlobalCategoryEquivalent() {
		return globalCategoryEquivalent;
	}

	public void setGlobalCategoryEquivalent(GlobalCategory globalCategoryEquivalent) {
		this.globalCategoryEquivalent = globalCategoryEquivalent;
	}
	
	@Override
	public void update()
	{
		
		super.update();
	}
	
	private static void appendChildCategoriesIdToList(ArrayList<Long> idLists, Category parent)
	{
		List<Category> children = parent.getChildren();
		for (Category category : children)
		{
			idLists.add(category.id);
			appendChildCategoriesIdToList(idLists, category);
		}
	}
	
	public static List<Long> getChildCategoriesFromCategory(Category parent)
	{
		ArrayList<Long> res = new ArrayList<Long>();
		res.add(parent.getId());
		appendChildCategoriesIdToList(res, parent);	
		return 		res;
	}

	public static Category getOrMakeCategoryFromGlobal(GlobalCategory globalCategory, User categoryOwner)
	{
		Category res = Category.find.where()
							.eq("owner.id", categoryOwner.getId())
							.eq("globalCategoryEquivalent.Id", globalCategory.getId())
							.findUnique();
		if (res != null)
			return res;
		// create copy category for user using GlobalCategory
		GlobalCategory gc = globalCategory;
		Category localCategory = null;
		ArrayList<GlobalCategory> categoriesToAdd = new ArrayList<GlobalCategory>();

		do
		{
			localCategory = Category.find.where()
					.eq("owner.id", categoryOwner.getId())
					.eq("globalCategoryEquivalent.Id", gc.getId())
					.findUnique();
			if (localCategory != null)
				break;
	
			categoriesToAdd.add(0, gc);
		}
		while ((gc = gc.getParent()) != null);
		
		if (categoriesToAdd.size() == 0)
			return localCategory;
		
		Category category = null;
		for (GlobalCategory gcItem : categoriesToAdd)
		{
			category = new Category();
			category.setName(gcItem.getName());
			category.setParent(localCategory);
			category.setGlobalCategoryEquivalent(gcItem);
			category.setOwner(categoryOwner);
			category.save();
			
			localCategory = category;
		}
		
		return localCategory;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", owner=" + owner
				+ ", parent=" + parent + ", children=" + children
				+ ", globalCategoryEquivalent=" + globalCategoryEquivalent
				+ "]";
	}


		
	
}
