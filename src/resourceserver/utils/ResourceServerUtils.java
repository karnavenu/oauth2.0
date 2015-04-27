package resourceserver.utils;


/**
 * Utils class specific to Resource Server.
 * @author Venu Karna.
 *
 */
public class ResourceServerUtils {

	private static ResourceServerUtils _obj = null;

	private ResourceServerUtils() {}
	
	public static ResourceServerUtils getInstance(){
		if(_obj == null){
			synchronized (ResourceServerUtils.class) {
				if (_obj == null) {
		        	_obj = new ResourceServerUtils();
		        }
			}
		}
		return _obj;
	}
	
	
	public boolean validateUser(String userName, String password) {
		return true;
	}
	
	public boolean validateUserByUserName(String userName) {
		return true;
	} 

    

}
