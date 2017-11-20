package kis.util;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.ContentType;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.services.Response;

import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;

/**
 * Created by
 * User: yury
 * at
 * Date: 12.05.2009
 * Time: 16:34:30
 */
public class BinaryStreamResponse implements StreamResponse {
    private final ContentType contentType;

    private final InputStream content;

    /**
     * Constructor allowing the content type and character set to the specified.
     *
     * @param contentType type of content, often "text/xml"
     * @param content        text to be streamed in the response
     * @see org.apache.tapestry5.SymbolConstants#CHARSET
     */
    public BinaryStreamResponse(String contentType, InputStream content)
    {
        this(new ContentType(Defense.notBlank(contentType, "contentType")), content);
    }

    public BinaryStreamResponse(ContentType contentType, InputStream content)
    {
        Defense.notNull(contentType, "contentType");
        Defense.notNull(content, "content");

        this.contentType = contentType;
        this.content = content;
    }

    public String getContentType()
    {
        return contentType.toString();
    }

    /**
     * Converts the text to a byte array (as per the character set, which is usually "UTF-8"), and returns a stream for
     * that byte array.
     *
     * @return the text as a byte array stram
     * @throws IOException
     */
    public InputStream getStream() throws IOException
    {
        return content;
    }

    /**
     * Does nothing; subclasses may override.
     */
    public void prepareResponse(Response response)
    {

    }
}
