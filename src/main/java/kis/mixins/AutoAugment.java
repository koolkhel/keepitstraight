package kis.mixins;

import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by
 * User: yury
 * at
 * Date: 11.05.2009
 * Time: 15:39:25
 *
 * Увеличивает автоматом textArea
 */
@IncludeJavaScriptLibrary("util.js")
public class AutoAugment {
    /**
     * сколько строчек по умолчанию в textarea
     */
    @Parameter(value= "7", defaultPrefix= BindingConstants.LITERAL)
    private Integer size;

    @Inject
    private RenderSupport renderSupport;

    @InjectContainer
    private ClientElement element;

    @AfterRender
    public void afterRender() {
        // событие
        renderSupport.addScript(String.format("$('%s').setAttribute(\"onkeypress\", \"updateTextAreaSize(this, %s)\");",
                element.getClientId(), Integer.toString(size)));

        // чтобы при загрузке страницы отработало
        renderSupport.addScript(String.format("updateTextAreaSize($('%s'), %s);",
                element.getClientId(), Integer.toString(size)));
    }
}
