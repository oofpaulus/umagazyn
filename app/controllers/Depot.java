package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.joda.time.DateTime;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;

import controllers.CategoryManager.CategoryItem;
import formObjects.UserAddComponentForm;
import models.Category;
import models.GlobalCategory;
import models.TokenAction;
import models.User;
import models.Warehouse;
import models.Component;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
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

public class Depot extends Controller{

	public static final Form<Warehouse> warehouseForm = form(Warehouse.class);
	
	@Restrict(@Group(Application.USER_ROLE))
	public static Result showWarehouse(Long warehouseId)
	{
		List<Component> components = getComponentsByWarehouseId(warehouseId); 
		return ok(views.html.warehouse.content.render(getWarehouses(), warehouseId, components ));
		
	}
	
	@Restrict(@Group(Application.USER_ROLE))
	public static Result index()
	{
		return redirect(controllers.routes.Depot.showWarehouse((long)0));
		//return ok(views.html.warehouse.content.render(getWarehouses(), (long)0, null));
		
	}
	
	@Restrict(@Group(Application.USER_ROLE))
	public static List<Component> getComponentsByWarehouseId(Long warehouseId)
	{
		final User user = Application.getLocalUser(session());
		Logger.error(warehouseId.toString());
		if (warehouseId == 0)
		{
			return Component.find.where()
					.eq("owner.id", user.getId())
					.findList();
		}
		
		Warehouse warehouse = Warehouse.find.byId(warehouseId);
		if (warehouse == null)
		{
			return new ArrayList<Component>();
		}
			
		
		
		if (warehouse.getOwner().getId() != user.getId())
			return new ArrayList<Component>();
		
		return Component.find.where()
				.eq("warehouse.id", warehouseId)
				.findList();
				
	}
	
	@Restrict(@Group(Application.USER_ROLE))
	public static List<Warehouse> getWarehouses()
	{
		final User user = Application.getLocalUser(session());
		List<Warehouse> warehouses = Warehouse.find.where().eq("owner", user).findList();
		return warehouses;
	}
	
	@Restrict(@Group(Application.USER_ROLE))
	public static Result create()
	{
		Warehouse w = new Warehouse();
		w.name="nazwa";
		w.comment="komentarz";
		Form<Warehouse> warehouseForm = form(Warehouse.class);
		Form<Warehouse> filledForm = warehouseForm.fill(w);
		
		return ok(views.html.warehouse.create.render(filledForm));	
	}
	
	@Restrict(@Group(Application.USER_ROLE))
	public static Result doAddComponent(Long warehouseId)
	{
		Form<UserAddComponentForm> filledForm = form(UserAddComponentForm.class).bindFromRequest();
		UserAddComponentForm addComponent = filledForm.get();
		
		GlobalCategory globalCategory = GlobalCategory.find.byId(addComponent.getCategoryId());
		
		if (globalCategory == null)
			return redirect(controllers.routes.Depot.showWarehouse(warehouseId));
		
		final User user = Application.getLocalUser(session());
		
		Category category = Category.getOrMakeCategoryFromGlobal(globalCategory, user);
		Component component = new Component();
		component.setName(addComponent.getElementName());
		component.setWarehouse(Warehouse.find.byId(warehouseId));
		component.setCategory(category);
		component.setOwner(user);
		component.save();
				
		return redirect(controllers.routes.Depot.showWarehouse(warehouseId));
	}
	
	
	//@Restrict(@Group(Application.USER_ROLE))
	public static Result doCreate() {

		Form<Warehouse> filledForm  = warehouseForm.bindFromRequest();
		Warehouse warehouseModel = filledForm.get();
		warehouseModel.owner = Application.getLocalUser(session());
		warehouseModel.created = DateTime.now();
		try {
		warehouseModel.save();
		}
		catch(Exception e)
		{
			filledForm = warehouseForm.fill(warehouseModel);
			filledForm.reject("Wybrana nazwa już istnieje");
			
			return ok(views.html.warehouse.create.render(filledForm));	
		}

		return ok(views.html.warehouse.content.render(getWarehouses(), (long)0, null));
		 
	}
	
	private static JsonNode createJsonFromCategoryList(List<Category> categories)
	{
		List<CategoryItem> ret = new ArrayList<CategoryItem>();
		for (Category cat : categories)
		{
			String parent = "#";
			if (cat.getParent() != null)
				parent = cat.getParent().getId().toString();
			ret.add(new CategoryItem(cat.getId(), parent, cat.getName()));
		}
		return Json.toJson(ret);
	}
	
	public static Result getCategoriesByWarehouseId(String warehouseId)
	{
		final User user = Application.getLocalUser(session());
		Long id = GlobalUtil.tryParseLong(warehouseId);
		List<Category> categories = Warehouse.getNeededCategoryTreeForComponentsInWarehouse(id, user);
		return ok(createJsonFromCategoryList(categories));
	}

	public static Result getCategoriesByComponentsOwner()
	{
		final User user = Application.getLocalUser(session());
		List<Category> categories = Warehouse.getNeededCategoryTreeForComponentsByOwner(user);
		return ok(createJsonFromCategoryList(categories));
	}
	
	/*
	public static Result doLogin() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final Form<MyLogin> filledForm = MyUsernamePasswordAuthProvider.LOGIN_FORM
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not fill everything properly
			return badRequest(login.render(filledForm));
		} else {
			// Everything was filled
			return UsernamePasswordAuthProvider.handleLogin(ctx());
		}
	}*/
	
}
