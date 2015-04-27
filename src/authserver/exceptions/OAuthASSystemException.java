package authserver.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * @author Venu Karna
 *
 */

public class OAuthASSystemException extends WebApplicationException{

	/**
	 * need to build this yet. TODO
	 */
	private static final long serialVersionUID = 1L;

	public OAuthASSystemException(String message) {
		super(Response.status(Response.Status.BAD_REQUEST)
	             .entity(message).type(MediaType.TEXT_PLAIN).build());
	}
}
