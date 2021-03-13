package jersey;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * Class that implements the MessageBodyReader and MessageBodyWrite to make Gson compatible with Jersey.
 * @author delta
 *
 * @see <a href="https://eclipsesource.com/blogs/2012/11/02/integrating-gson-into-a-jax-rs-based-application/">Integrating Gson into Jersey</a>
 * @see javax.ws.rs.ext.MessageBodyReader
 * @see javax.ws.rs.ext.MessageBodyWriter
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class GsonMessageBodyHandler implements MessageBodyWriter<Object>, MessageBodyReader<Object>
{

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
                              java.lang.annotation.Annotation[] annotations, MediaType mediaType)
    {
        return true;
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException
    {
        try (InputStreamReader streamReader = new InputStreamReader(entityStream, StandardCharsets.UTF_8))
        {
            Type jsonType;
            if (type.equals(genericType))
            {
                jsonType = type;
            } else
            {
                jsonType = genericType;
            }
            return GsonFactory.getGson().fromJson(streamReader, jsonType);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return true;
    }

    @Override
    public long getSize(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public void writeTo(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {
        try (OutputStreamWriter writer = new OutputStreamWriter(entityStream, StandardCharsets.UTF_8))
        {
            Type jsonType;
            if (type.equals(genericType))
            {
                jsonType = type;
            } else
            {
                jsonType = genericType;
            }
            GsonFactory.getGson().toJson(object, jsonType, writer);
        }
    }

    /**
     * Factory for Gson objects
     * @author delta
     */
    private static final class GsonFactory
    {
        private final static Gson gson;

        static
        {
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
        }

        public static Gson getGson()
        {
            return gson;
        }
    }
}