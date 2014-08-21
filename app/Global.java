import java.util.Arrays;
import java.util.Collections;

import models.LinkedAccount;
import models.SecurityRole;
import models.User;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;

import controllers.routes;
import play.Application;
import play.GlobalSettings;
import play.mvc.Call;
import providers.MyUsernamePasswordAuthUser;

public class Global extends GlobalSettings {

	public void onStart(Application app) {
		PlayAuthenticate.setResolver(new Resolver() {

			@Override
			public Call login() {
				// Your login page
				return routes.Application.login();
			}

			@Override
			public Call afterAuth() {
				// The user will be redirected to this page after authentication
				// if no original URL was saved
				return routes.Application.index();
			}

			@Override
			public Call afterLogout() {
				return routes.Application.index();
			}

			@Override
			public Call auth(final String provider) {
				// You can provide your own authentication implementation,
				// however the default should be sufficient for most cases
				return com.feth.play.module.pa.controllers.routes.Authenticate
						.authenticate(provider);
			}

			@Override
			public Call askMerge() {
				return routes.Account.askMerge();
			}

			@Override
			public Call askLink() {
				return routes.Account.askLink();
			}

			@Override
			public Call onException(final AuthException e) {
				if (e instanceof AccessDeniedException) {
					return routes.Signup
							.oAuthDenied(((AccessDeniedException) e)
									.getProviderKey());
				}

				// more custom problem handling here...
				return super.onException(e);
			}
		});

		initialData();
	}

	/**
	 *  Create roles and admin account from application.conf
	 *  	account.admin.email 
	 *  	account.admin.password
	 */
	private void initialData() {
		
		if (SecurityRole.find.findRowCount() == 0) {
			
			SecurityRole role = new SecurityRole();
			role.roleName = controllers.Application.ADMIN_ROLE;
			role.save();
			
			role = new SecurityRole();
			role.roleName = controllers.Application.USER_ROLE;
			role.save();

			// create admin user

			User user = new User();
			String email = play.Play.application().configuration().getString("account.admin.email");
			String password = play.Play.application().configuration().getString("account.admin.password");
			
			user.setEmail(email);
			user.setActive(true);
			user.setEmailValidated(true);
			
			user.setRoles(Collections.singletonList(SecurityRole.findByRoleName(controllers.Application.ADMIN_ROLE)));
			user.save();
			user.saveManyToManyAssociations("roles");
			
			MyUsernamePasswordAuthUser authUser = new MyUsernamePasswordAuthUser(password);
			LinkedAccount linkedAccount = new LinkedAccount();
			linkedAccount.setUser(user);
			linkedAccount.setProviderKey("password");
			linkedAccount.setProviderUserId(authUser.getHashedPassword());
			linkedAccount.save();
			
			
		}
				
					
	}
}