package api.foursquare;

import api.foursquare.entities.*;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Builds a custom Gson object that can handle some special entities.
 * Was extreme useful when backtracking was used in categories. Now it has
 * lost it's worth but in no case is it deprecated because any customisations
 * in Gson can take place here.
 * @author delta
 */
public final class CustomGsonProvider
{
    private static final Gson gson;

    static
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Response.class, new ResponseTypeAdapter());
        gson = builder.create();
    }

    protected static Gson getGson()
    {
        return gson;
    }

    /**
     * The serializer/deserializer for Response objects.
     * @author delta
     */
    public static class ResponseTypeAdapter implements JsonDeserializer<Response> , JsonSerializer<Response>
    {
        public ResponseTypeAdapter() { }

        public Response deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException
        {
            JsonObject metaObject = json.getAsJsonObject()
                                     .getAsJsonObject("meta");

            int code = metaObject.get("code").getAsInt();
            String requestID = metaObject.get("requestId").getAsString();


            JsonElement errorTypeElement = metaObject.get("errorType");
            String errorType = null;
            if(errorTypeElement != null)
                errorType = errorTypeElement.getAsString();

            JsonElement errorDetailElement = metaObject.get("errorDetail");
            String errorDetail = null;
            if(errorDetailElement != null)
                errorDetail = errorDetailElement.getAsString();

            ResponseMeta meta = new ResponseMeta(code,errorType,errorDetail,requestID);

            String responseJSON = json.getAsJsonObject()
                    .getAsJsonObject()
                    .get("response")
                    .toString();

            return new Response(meta,responseJSON);
        }


        @Override
        public JsonElement serialize(Response src, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject obj = new JsonObject();

            ResponseMeta meta = src.getMeta();
            JsonObject metaJson = new JsonObject();
            metaJson.addProperty("code",meta.getCode());
            if(meta.getErrorType() != null)
                metaJson.addProperty("errorType",meta.getErrorType());
            if(meta.getErrorDetail() != null)
                metaJson.addProperty("errorDetail",meta.getErrorDetail());

            metaJson.addProperty("requestId",meta.getRequestId());

            obj.add("meta",metaJson);
            obj.addProperty("response",src.getResponse());

            return obj;
        }
    }
}