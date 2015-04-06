package assign.services;

import assign.domain.Projects;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ListIterator;

public class EavesdropService {

	public String getData() {
		return "Hello from Eavesdrop service.";
	}

	public void getAllProjects(Projects projects) throws Exception {
		String baseURL = "http://eavesdrop.openstack.org/";
		String irclogs = "irclogs/";
		String meetings = "meetings/";

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

		try {
			Document doc = Jsoup.connect(baseURL + irclogs).get();
			Elements links = doc.select("body a");

			ListIterator<Element> iter = links.listIterator();
			while(iter.hasNext()) {
				Element e = iter.next();
				String s = e.html();
				if (s.endsWith("/")) {
					s = s.replace("/", "");
					s = s.replace("#", "%23");
					projects.getProjects().add(s);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
