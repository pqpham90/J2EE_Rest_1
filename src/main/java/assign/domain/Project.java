package assign.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {

	String name;

	List<String> link = null;

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLink() {
        return link;
    }

    public void setLink(List<String> link) {
        this.link = link;
    }
}
