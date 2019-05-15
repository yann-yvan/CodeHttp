package corp.ny.com.codehttp.exceptions;

public class TokenException extends Exception {
    private int code;

    public TokenException(String message, int code) {
        super(message);
        this.code = code;
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getCode() {
        return code;
    }
}
