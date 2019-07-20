package corp.ny.com.codehttp.response;

public final class FileType {

    /**
     * Authentication media
     */
    public enum Type {
        PDF("application/pdf"),
        JSON("application/json"),
        PNG("image/png"),
        JPEG("image/jpeg"),
        GIF("image/gif"),
        DOC("image/gif");

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
