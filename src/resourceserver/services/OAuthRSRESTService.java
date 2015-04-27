package resourceserver.services;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;

import resourceserver.OAuthRSConstants;
import resourceserver.exceptions.OAuthRSSystemException;

import com.sun.jersey.api.core.HttpContext;

/**
 * REST Services specific to OAuth Resource Server.
 * 
 * @author Venu Karna
 * 
 */
@Path("/v1")
public class OAuthRSRESTService {

	public OAuthRSRESTService() {

	}

	@POST
	@Produces({ "application/x-www-form-urlencoded" })
	@Path("authenticate")
	public void authenticateUser(@Context HttpContext context,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {

		String queryString = null;
		try {
			MultivaluedMap<String, String> formParams = context.getRequest()
					.getFormParameters();
			if (formParams.get("email") != null
					&& formParams.get("password") != null) {
				String password = formParams.get("password").get(0);
				String email = formParams.get("email").get(0);
				queryString = formParams.get("queryString").get(0);
				String redirectURL = null;

				response.sendRedirect(redirectURL);

			} else {
				throw new OAuthRSSystemException(
						OAuthRSConstants.OAUTH_LOGIN_FAILED);
			}

		} catch (IOException e) {
			throw new OAuthRSSystemException("error authenticating user");
		} catch (OAuthRSSystemException e) {
			throw new OAuthRSSystemException("error authenticating user");
		}
	}

}
