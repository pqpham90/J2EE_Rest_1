package assign.services;

import assign.domain.Project;
import assign.domain.Projects;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ListIterator;

public class EavesdropService {

	// grabs all the projects in meetings and irclogs
	public void getAllProjects(Projects projects) throws Exception {
		String baseURL = "http://eavesdrop.openstack.org/";
		String meetings = "meetings/";
		String irclogs = "irclogs/";

		getAllProjects(projects, baseURL + meetings);
		getAllProjects(projects, baseURL + irclogs);
	}

	// grabs all the projects in the URL
	public void getAllProjects(Projects projects, String URL) throws Exception {
		try {
			Document doc = Jsoup.connect(URL).get();
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
	}

	// get the logs in the project
	public int getProject(Project project, String projectName) throws Exception {
		String URL = "http://eavesdrop.openstack.org/irclogs/" + projectName.replace("#", "%23");

		try {
			Document doc = Jsoup.connect(URL).get();
			Elements links = doc.select("body a");

			ListIterator<Element> iter = links.listIterator();
			while(iter.hasNext()) {
				Element e = iter.next();
				String s = e.html();

			    project.getLink().add(URL + "/" + s.replace("#", "%23"));

			}
		}
		catch (HttpStatusException e) {
			return e.getStatusCode();
		}
		catch (Exception e) {
			e.printStackTrace();
			return  -1;
		}

		return 0;
	}
}
