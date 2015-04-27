package resourceserver.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class OAuthRSSystemException extends WebApplicationException{

	/**
	 * need to build this yet. TODO
	 */
	private static final long serialVersionUID = 1L;

	public OAuthRSSystemException(String message) {
		super(Response.status(Response.Status.BAD_REQUEST)
	             .entity(message).type(MediaType.TEXT_PLAIN).build());
	}
	
	public OAuthRSSystemException(int errorCode, String message) {
		super(getResponse(errorCode, message));
	}	
	
	public static Response getResponse(int errorCode, String message) {
		Response response = null;
		
		if(errorCode == 400){
			response = Response.status(Response.Status.BAD_REQUEST)
					.entity(message).type(MediaType.TEXT_PLAIN).build();
		}else if(errorCode == 401){			
			response = Response.status(Response.Status.UNAUTHORIZED)
						.entity(message).type(MediaType.TEXT_PLAIN).build();
		}else if(errorCode == 403){
			response = Response.status(Response.Status.FORBIDDEN)
		            	.entity(message).type(MediaType.TEXT_PLAIN).build();			
		}else{
			response = Response.status(Response.Status.BAD_REQUEST)
        			.entity(message).type(MediaType.TEXT_PLAIN).build();
		}
		
		return response;
		
	}
}
