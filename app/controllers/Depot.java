package controllers;

import static play.data.Form.form;

import java.util.List;

import javax.validation.ValidationException;

import org.joda.time.DateTime;

import com.avaje.ebean.Ebean;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;

import models.TokenAction;
import models.User;
import models.Warehouse;
import models.Component;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import play.Logger;
import play.api.data.validation.ValidationError;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthProvider.MyLogin;
import views.html.*;

public class Depot extends Controller{

	public static final Form<Warehouse> warehouseForm = form(Warehouse.class);
	
	
	public static Result test()
	{
		
	//	TokenAction ta = TokenAction.findByToken("766ca9ad-da84-4cfa-b486-1b273ee31343", TokenAction.Type.EMAIL_VERIFICATION);
	
		//Warehouse w = Warehouse.find.byId((long) 1);
		
		//User u = User.find.byId(w.getOwner().getId());
		//return ok(w.toString() + ":::" + u);
		
		User a = User.find.byId((long)1);
		a.verify();
		//a.setEmailValidated(true);
		a.save();
		return ok("aa");
	}
	
	@Restrict(@Group(Application.USER_ROLE))
	public static Result showWarehouse(Long warehouseId)
	{
		List<Component> components = getComponentsByWarehouseId(warehouseId); 
		return ok(views.html.warehouse.content.render(getWarehouses(), warehouseId, components ));
		
	}
	
	@Restrict(@Group(Application.USER_ROLE))
	public static Result show()
	{
		return ok(views.html.warehouse.content.render(getWarehouses(), (long)0, null));
		
	}
	
	@Restrict(@Group(Application.USER_ROLE))
	public static List<Component> getComponentsByWarehouseId(Long warehouseId)
	{
		return Component.find.where().eq("warehouse_id", warehouseId).findList();
				
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
	
	public static Result doAddComponent(Long warehouseId)
	{
		Form<Component> filledForm = form(Component.class).bindFromRequest();
		Component component = filledForm.get();
		Warehouse warehouse = Warehouse.find.byId(warehouseId);
		component.setWarehouse(warehouse);
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
