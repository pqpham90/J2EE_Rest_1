package assign.resources;

import assign.domain.Project;
import assign.domain.Projects;
import assign.services.EavesdropService;

import javax.ws.rs.*;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

@Path("/myeavesdrop/projects")
public class EavesdropResource {
	EavesdropService eavesdropService;

	public EavesdropResource() {
		this.eavesdropService = new EavesdropService();
	}

	// get the union of meetings and irclogs
	@GET
	@Path("/")
	@Produces("application/xml")
	public StreamingOutput getAllProjects() throws Exception {
		final Projects projects = new Projects();
		projects.setProjects(new ArrayList<String>());

		eavesdropService.getAllProjects(projects);

		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputProjects(outputStream, projects);
			}
		};
	}

	// grabs all the logs for projects
	@GET
	@Path("{project}/irclogs")
	@Produces("application/xml")
	public StreamingOutput getProject(@PathParam("project") String projectName) throws Exception {
		final Project project = new Project();
		project.setName(projectName);
		project.setLink(new ArrayList<String>());

		// wasn't found return 404 error
		if(eavesdropService.getProject(project, projectName) == 404) {
			throw new WebApplicationException(HttpURLConnection.HTTP_NOT_FOUND);
		}

		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputProject(outputStream, project);
			}
		};
	}


	// output all the meeting and irclogs projects
	protected void outputProjects(OutputStream os, Projects projects) throws IOException {
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

	// output the logs of the project
	protected void outputProject(OutputStream os, Project project) throws IOException {
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
