package assign.resources;

import assign.domain.Course;
import assign.domain.Courses;
import assign.domain.Project;
import assign.domain.Projects;
import assign.services.EavesdropService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.ListIterator;

@Path("/myeavesdrop")
public class EavesdropResource {

	EavesdropService eavesdropService;

	public EavesdropResource() {
		this.eavesdropService = new EavesdropService();
	}

	@GET
	@Path("/helloworld")
	@Produces("text/html")
	public String helloWorld() {
		return "Hello world";
	}

	@GET
	@Path("/helloeavesdrop")
	@Produces("text/html")
	public String helloEavesdrop() {
		return this.eavesdropService.getData();
	}

	@GET
	@Path("/courses")
	@Produces("application/xml")
	public StreamingOutput getAllCourses() throws Exception {
		Course modernWebApps = new Course();
		modernWebApps.setDepartment("CS");
		modernWebApps.setName("Modern Web Applications");

		Course operatingSystems = new Course();
		operatingSystems.setDepartment("CS");
		operatingSystems.setName("Operating Systems");

		final Courses courses = new Courses();
		courses.setEmployees(new ArrayList<Course>());
		courses.getCourses().add(modernWebApps);
		courses.getCourses().add(operatingSystems);

		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputCourses(outputStream, courses);
			}
		};
	}

	@GET
	@Path("/projects")
	@Produces("application/xml")
	public StreamingOutput getAllProjects() throws Exception {
		String baseURL = "http://eavesdrop.openstack.org/";
		String irclogs = "irclogs/";
		String meetings = "meetings/";

		final Projects projects = new Projects();
		projects.setProjects(new ArrayList<String>());

		try {
			Document doc = Jsoup.connect(baseURL + irclogs).get();
			Elements links = doc.select("body a");

			ListIterator<Element> iter = links.listIterator();
			while(iter.hasNext()) {
				Element e = iter.next();
				String s = e.html();
				if (s.endsWith("/")) {
					s = s.replace("/", "");
					projects.getProjects().add(s);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Document doc = Jsoup.connect(baseURL + meetings).get();
			Elements links = doc.select("body a");

			ListIterator<Element> iter = links.listIterator();
			while(iter.hasNext()) {
				Element e = iter.next();
				String s = e.html();
				if (s.endsWith("/")) {
					s = s.replace("/", "");
					projects.getProjects().add(s);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

//		Project heat = new Project();
//		heat.setName("%23heat");

		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputCourses(outputStream, projects);
			}
		};
	}

	@GET
	@Path("/project")
	@Produces("application/xml")
	public StreamingOutput getProject() throws Exception {
		final Project heat = new Project();
		heat.setName("%23heat");
		heat.setLink(new ArrayList<String>());
		heat.getLink().add("l1");
		heat.getLink().add("l2");

		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputCourses(outputStream, heat);
			}
		};
	}

	protected void outputCourses(OutputStream os, Courses courses) throws IOException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Courses.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(courses, os);
		} catch (JAXBException jaxb) {
			throw new WebApplicationException();
		}
	}

	protected void outputCourses(OutputStream os, Projects projects) throws IOException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Projects.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(projects, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}

	protected void outputCourses(OutputStream os, Project project) throws IOException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(project, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
}
