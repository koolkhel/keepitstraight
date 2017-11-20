package kis.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.MarkupWriter;

/**
 * Created by
 * User: yury
 * at
 * Date: 02.05.2009
 * Time: 20:49:34
 *
 * Нужен для вывода textile. Грамотность результирующего кода
 * лежит на textile.
 */
public class RawOutput {
    @Parameter(required = true)
    private String data;

    @BeginRender
    public void beginRender(MarkupWriter mw) {
        mw.writeRaw(data);
    }
}
