package com.khulnasoft.gemini.path;

import java.io.IOException;
import java.lang.reflect.Type;

import com.khulnasoft.gemini.Context;
import com.khulnasoft.gemini.GeminiConstants;
import com.khulnasoft.gemini.Request;
import com.khulnasoft.js.JavaScriptError;

/**
 * A request body adapter that uses the application's default
 * JavaScriptReader to de-serialize the request body. This
 * adapter expects that the request content type is application/json
 * and throws an exception if that is not the case.
 *
 * @see com.khulnasoft.gemini.path.annotation.ConsumesJson
 */
public class JsonRequestBodyAdapter implements RequestBodyAdapter<Context>
{
  @Override
  public Object read(Context context, Type type) throws RequestBodyException
  {
    final Request request = context.getRequest();

    // Ensure that the requests had the right content type.
    if (!request.getRequestContentType().contains(GeminiConstants.CONTENT_TYPE_JSON))
    {
      throw new RequestBodyException(400, "Invalid content type: "
          + request.getRequestContentType());
    }

    try
    {
      // Attempt de-serialization.
      return context.getApplication().getJavaScriptReader().read(request.getInputStream(), type);
    }
    catch (IOException e)
    {
      throw new RequestBodyException(500, "Internal server error", e);
    }
    catch(JavaScriptError e)
    {
      throw new RequestBodyException(400, "Error parsing request body as JSON.", e);
    }
  }
}
