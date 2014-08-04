package controllers;

import static play.data.Form.form;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.ValidationException;
import javax.xml.validation.ValidatorHandler;

import org.joda.time.DateTime;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;

import models.Category;
import models.GlobalCategory;
import models.TokenAction;
import models.User;
import models.Warehouse;
import models.Component;
import play.Logger;
import play.api.data.validation.ValidationError;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthProvider.MyLogin;
import utils.GlobalUtil;
import views.html.*;

public class CategoryManager extends Controller{

	static class CategoryId {
		
		private String Id;

		public String getId() {
			return Id;
		}

		public void setId(String id) {
			Id = id;
		}

		public CategoryId(String id) {
			super();
			Id = id;
		}
		
		
	}
	
	static class CategoryItem {
		
		private Long id;
		private String parent;
		private String text;
		
		public Long getId() {
			return id;
		}

		public void Long(Long id) {
			this.id = id;
		}

		public String getParent() {
			return parent;
		}

		public void setParent(String parent) {
			this.parent = parent;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public CategoryItem(Long id, String parent, String text) {
			this.id = id;
			this.parent = parent;
			this.text = text;
		}
		
		
	}
	
	public static Result deleteNode(String id)
	{
		Long nodeId = GlobalUtil.tryParseLong(id);
		if (nodeId == null)
		{
			return notFound();				
		}
		GlobalCategory category = GlobalCategory.find.byId(nodeId);
		if (category == null)
		{
			return notFound();
		}
		
		category.delete();
		
		return ok(Json.toJson(id));
	}
	
	public static Result renameNode(String id, String text)
	{
		Long nodeId = GlobalUtil.tryParseLong(id);
		if (nodeId == null)
		{
			return notFound();				
		}
		GlobalCategory category = GlobalCategory.find.byId(nodeId);
		if (category == null)
		{
			return notFound();
		}
		
		category.setName(text);
		category.update();
		
		return ok(Json.toJson(text));
	}
	
	public static Result addNode(String id, String position, String text)
	{
		Long parentId = GlobalUtil.tryParseLong(id);
		if (parentId == null)
		{
			GlobalCategory newCategory = new GlobalCategory();
			newCategory.setName(text);
			newCategory.save();
			Map<String, String> response = new HashMap<String, String>();
			response.put("id", newCategory.getId().toString());
			return ok(Json.toJson(response));	
		}
		
		GlobalCategory newCategory = new GlobalCategory();
		newCategory.setParent(GlobalCategory.find.byId(parentId));
		newCategory.save();
		CategoryId categoryId = new CategoryId(newCategory.getId().toString());
		return ok(Json.toJson(categoryId));
	}
	
	public static Result getNode(String id)
	{
		Integer intId = GlobalUtil.tryParseInt(id);

		List<GlobalCategory> categoryList = GlobalCategory.find.all();
		List<CategoryItem> categoryItems = new ArrayList<CategoryItem>();
		for (GlobalCategory gc : categoryList)
		{
			String parent = "#";
			GlobalCategory parentCategory = gc.getParent();
			if (parentCategory != null)
			{
				parent = parentCategory.getId().toString(); 
			}
			categoryItems.add(new CategoryItem(gc.getId(), parent, gc.getName()));
		}

		//if (intId == null)
			return ok(Json.toJson(categoryItems));
		//else
		//	return ok(Json.toJson(new String("siema" + id)));
	}
	
	public static Result show(){
		
		List<Category> globalCategories = Category.find.where().eq("parent_id", null).findList();
		
		/*Category gc = globalCategories.get(0);
		Logger.error("rozmiar: " + gc.getNames());
		Logger.error("children: " + gc.getChildren().get(0).getNames());
		Logger.error("children: " + gc.getChildren().get(0).getChildren().get(0).getNames());
		*/ 
		
		return ok(views.html.global.category.render(globalCategories));
	//	return ok("siema");
	}
	
}
