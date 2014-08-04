package controllers;

import models.User;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import static play.data.Form.form;

public class Admin extends Controller {

	@Restrict(@Group(Application.ADMIN_ROLE))
	public static Result index() {
		return ok(views.html.admin.index.render());
	}

}
