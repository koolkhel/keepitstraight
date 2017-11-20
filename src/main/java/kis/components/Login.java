package kis.components;


import org.apache.tapestry5.Block;
import org.apache.tapestry5.util.TextStreamResponse;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.chenillekit.tapestry.core.components.InPlaceEditor;

public class Login {
    private String username;

    @Inject
    private Logger logger;

    @Inject
    private Cookies cookies;
    
    @SuppressWarnings("unused")
	@Component
	private InPlaceEditor inPlaceEditor;

    void setupRender() {
        String cookie = cookies.readCookieValue("username");
        if (cookie != null) {
            username = cookie;
        } else {
            username = "click to pass username";
        }
    }

    @OnEvent(component = "inPlaceEditor", value = InPlaceEditor.SAVE_EVENT)
	void actionFromEditor(String context, String newName) {
        cookies.writeCookieValue("username", newName);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String dummy, String username) {
        this.username = username;
    }
}
