package corp.ny.com.codehttp.response;

/**
 * Created by yann-yvan on 15/11/17.
 */

public final class RequestCode {

    /**
     * Call this to compare all authentication response code
     *
     * @param code rhe value you want to check
     * @return the corresponding enum
     */
    public static Auth authMessage(int code) {
        for (Auth auth :
                Auth.values()) {
            if (auth.equals(code)) {
                return auth;
            }
        }
        return Auth.UNKNOWN_CODE;
    }


    /**
     * Call this to compare all token response code
     *
     * @param code rhe value you want to check
     * @return the corresponding enum
     */
    public static Token tokenMessage(int code) {
        for (Token token :
                Token.values()) {
            if (token.equals(code)) {
                return token;
            }
        }
        return Token.UNKNOWN_CODE;
    }

    public static DefResponse requestMessage(int code) {
        for (DefResponse response : DefResponse.values()
                ) {
            if (response.equals(code))
                return response;
        }
        return DefResponse.UNKNOWN_CODE;
    }

    /**
     * Authentication code
     */
    public enum Auth implements Required {
        ACCOUNT_NOT_VERIFY(1100),
        WRONG_USERNAME(1101),
        WRONG_PASSWORD(1102),
        WRONG_CREDENTIALS(1103),
        ACCOUNT_VERIFIED(1104),
        UNKNOWN_CODE(0);

        private int code;

        Auth(int code) {
            this.code = code;
        }

        public int toCode() {
            return this.code;
        }

        public boolean equals(int code) {
            return this.code == code;
        }

        @Override
        public String toString() {
            return this.name();
        }

    }

    /**
     *
     */
    public enum DefResponse implements Required {
        SUCCESS(1000),
        FAILURE(1001),
        MISSING_DATA(1002),
        EXPIRED(1003),
        UNKNOWN_CODE(0);
        private int code;

        DefResponse(int code) {
            this.code = code;
        }

        @Override
        public int toCode() {
            return this.code;
        }

        public boolean equals(int code) {
            return this.code == code;
        }

        @Override
        public String toString() {
            return this.name();
        }

    }

    /**
     * token code
     */
    public enum Token implements Required {

        TOKEN_EXPIRED(1),
        BLACK_LISTED_TOKEN(2),
        INVALID_TOKEN(3),
        NO_TOKEN(4),
        USER_NOT_FOUND(5),
        UNKNOWN_CODE(0);

        private int code;

        Token(int code) {
            this.code = code;
        }

        @Override
        public int toCode() {
            return this.code;
        }

        public boolean equals(int code) {
            return this.code == code;
        }

        @Override
        public String toString() {
            return this.name();
        }
    }

    private interface Required {
        int toCode();

        boolean equals(int code);

        String toString();
    }
}
