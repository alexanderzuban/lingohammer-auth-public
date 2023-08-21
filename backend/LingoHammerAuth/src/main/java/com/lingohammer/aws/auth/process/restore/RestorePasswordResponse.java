package com.lingohammer.aws.auth.process.restore;

import com.lingohammer.aws.auth.data.IsFlawed;

public class RestorePasswordResponse extends IsFlawed {
    public static final int ERROR_CODE_MISSING_USER = -1;

    public static final int ERROR_CODE_REQUEST_ERROR = -2;
}
