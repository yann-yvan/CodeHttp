package corp.ny.com.codehttp.response;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FormPart {
    private String mediaType;
    private String property;
    private String fileName;
    private String filePath;

    public FormPart(String mediaType, String property, String fileName, String filePath) {
        this.mediaType = mediaType;
        this.property = property;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public RequestBody getFile() {
        return RequestBody.create(MediaType.parse(mediaType), new File(filePath));
    }
}
