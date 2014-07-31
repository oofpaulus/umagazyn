package controllers;

import static play.data.Form.form;

import java.util.List;

import javax.validation.ValidationException;

import org.joda.time.DateTime;

import com.avaje.ebean.Ebean;
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
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthProvider.MyLogin;
import views.html.*;

public class CategoryManager extends Controller{

	
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
