package corp.ny.com.codehttp.response;

public final class FileType {

    public static Type getMediaType(String fileName) {
        String[] fileNamePart = fileName.split(".");
        String ext = fileNamePart[fileNamePart.length - 1].toLowerCase();
        for (Type type : Type.values()) {
            if (type.media.contains(ext)) {
                return type;
            }
        }
        return Type.TEXT;
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
