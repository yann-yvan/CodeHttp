package corp.ny.com.codehttp.response;

import android.text.TextUtils;

import corp.ny.com.codehttp.exceptions.UnknownFileExtensionException;

public final class FileType {

    public static Type getMediaType(String fileName) throws UnknownFileExtensionException {
        String[] fileNamePart = TextUtils.split(fileName, ".");
        String ext = fileNamePart[fileNamePart.length - 1].toLowerCase();
        for (Type type : Type.values()) {
            if (type.media.contains(ext)) {
                return type;
            }
        }
        throw new UnknownFileExtensionException();
    }

    /**
     * Authentication media
     */
    public enum Type {
        PDF("application/pdf"),
        JSON("application/json"),
        PNG("image/png"),
        JPEG("image/jpeg"),
        GIF("image/gif"),
        DOC("application/msword"),
        DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        TEXT("text/plain");

        private String media;

        Type(String media) {
            this.media = media;
        }

        @Override
        public String toString() {
            return this.media;
        }
    }
}
