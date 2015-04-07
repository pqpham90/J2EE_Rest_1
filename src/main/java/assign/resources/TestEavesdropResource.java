package assign.resources;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pqpham90 on 4/6/15.
 */
public class TestEavesdropResource {

	EavesdropResource eavesdropResource = null;

	@Before
	public void setUp() {
		eavesdropResource = new EavesdropResource();
	}

	@Test
	public void testGetAllProjects () throws Exception {
		Client client = ClientBuilder.newClient();
		try {
			System.out.println("*** Getting All Projects ***");

			Response response = client.target("http://localhost:8080/assignment4/myeavesdrop/projects/")
					.request().get();

			if (response.getStatus() != 200) {
				throw new RuntimeException("Get Failed");
			}
			else {
				System.out.println("Get Successful");
			}

			URL getUrl = new URL("http://localhost:8080/assignment4/myeavesdrop/projects/");
			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
			BufferedReader reader = new BufferedReader(new
					InputStreamReader(connection.getInputStream()));


			String line = reader.readLine();
			String test = "";
			while (line != null)
			{
				test += line;
				line = reader.readLine();
			}

			Document xmlDoc = Jsoup.parse(test, "", Parser.xmlParser());

			Elements links = xmlDoc.select("project");

			Assert.assertEquals(315, links.size());
			System.out.println("Project count is correct");

			response.close();
		} finally {
			client.close();
		}
	}

	@Test
	public void testGetProject () throws Exception {
		Client client = ClientBuilder.newClient();
		try {
			System.out.println("*** Getting Single Project ***");

			Response response = client.target("http://localhost:8080/assignment4/myeavesdrop/projects/%23openstack-api/irclogs")
					.request().get();

			if (response.getStatus() != 200) {
				throw new RuntimeException("Get Failed");
			}
			else {
				System.out.println("Get Successful");
			}

			URL getUrl = new URL("http://localhost:8080/assignment4/myeavesdrop/projects/%23openstack-api/irclogs");
			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
			BufferedReader reader = new BufferedReader(new
					InputStreamReader(connection.getInputStream()));


			String line = reader.readLine();
			String test = "";
			while (line != null)
			{
				test += line;
				line = reader.readLine();
			}

			boolean testContains = test.contains("http://eavesdrop.openstack.org/irclogs/%23openstack-api/Description");
			Assert.assertEquals(true, testContains);
			System.out.println("Project logs generated correctly");

			response.close();
		} finally {
			client.close();
		}
	}

	@Test
	public void testGetProjectNonExistent () throws Exception {
		Client client = ClientBuilder.newClient();
		try {
			System.out.println("*** Getting Non-Existent Project ***");

			Response response = client.target("http://localhost:8080/assignment4/myeavesdrop/projects/non-existent-project/irclogs")
					.request().get();

			if (response.getStatus() != 404) {
				throw new RuntimeException("Found something that shoulda been nothing");
			}
			else {
				System.out.println("Properly returned 404");
			}

			response.close();
		} finally {
			client.close();
		}
	}
}