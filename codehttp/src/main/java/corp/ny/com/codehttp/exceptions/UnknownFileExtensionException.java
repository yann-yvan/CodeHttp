package corp.ny.com.codehttp.exceptions;

import corp.ny.com.codehttp.R;
import corp.ny.com.codehttp.system.App;

/**
 * Created by yann-yvan on 03/01/18.
 */

public class UnknownFileExtensionException extends Exception {
    public UnknownFileExtensionException() {
        super(App.getContext().getString(R.string.unknown_file_extension));
    }

}