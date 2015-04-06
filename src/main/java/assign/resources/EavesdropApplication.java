package assign.resources;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/assignment4")
public class EavesdropApplication extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public EavesdropApplication() {
	}

	@Override
	public Set<Class<?>> getClasses() {
		classes.add(EavesdropResource.class);
		return classes;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}


}
