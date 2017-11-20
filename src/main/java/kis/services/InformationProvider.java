package kis.services;

import org.apache.tapestry5.ioc.annotations.Inject;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by
 * User: yury
 * at
 * Date: 28.04.2009
 * Time: 18:16:31
 */
public class InformationProvider {
    @Inject
    private HttpServletRequest request;

    public String getServerURL() {
        return  "http://" +
                request.getServerName() + ":" +
                request.getServerPort();
    }
}
