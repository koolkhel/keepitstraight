package kis.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.BindingConstants;

public class Layout {
    /**
     * Титул страницы
     */
    @Parameter(required = false, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String pageTitle;
}
