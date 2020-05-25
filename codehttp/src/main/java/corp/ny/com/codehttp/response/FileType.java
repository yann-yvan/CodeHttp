package corp.ny.com.codehttp.response;

import java.net.URLConnection;

import corp.ny.com.codehttp.exceptions.UnknownFileExtensionException;

public final class FileType {

    public static String getMediaType(String fileName) throws UnknownFileExtensionException {
        String mime = URLConnection.guessContentTypeFromName(fileName);
        if (mime == null)
        throw new UnknownFileExtensionException();
        return mime;
    }
}
