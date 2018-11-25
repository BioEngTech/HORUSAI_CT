package vigi.patient.utils.exceptions;

public class FirebaseException extends Exception{

    private String errorText ="";

        public String exceptionType (String errorCode){

            switch (errorCode) {
                case "ERROR_INVALID_CUSTOM_TOKEN":

                    errorText = "The custom token format is incorrect. Please check the documentation.";

                    break;
                case "ERROR_CUSTOM_TOKEN_MISMATCH":

                    errorText = "The custom token corresponds to a different audience.";

                    break;
                case "ERROR_INVALID_CREDENTIAL":

                    errorText = "The supplied auth credential is malformed or has expired.";

                    break;
                case "ERROR_INVALID_EMAIL":

                    errorText = "The email address is badly formatted.";

                    break;
                case "ERROR_WRONG_PASSWORD":

                    errorText = "The password is not correct.";

                    break;
                case "ERROR_USER_MISMATCH":

                    errorText = "The supplied credentials do not correspond to the previously signed in user.";

                    break;
                case "ERROR_REQUIRES_RECENT_LOGIN":

                    errorText = "This operation is sensitive and requires recent authentication. Log in again before retrying this request.";

                    break;
                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":

                    errorText = "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.";

                    break;
                case "ERROR_CREDENTIAL_ALREADY_IN_USE":

                    errorText = "This credential is already associated with a different user account.";

                    break;
                case "ERROR_USER_DISABLED":

                    errorText = "The user account has been disabled by an administrator.";

                    break;
                case "ERROR_USER_TOKEN_EXPIRED":

                    errorText = "The user's credential is no longer valid. The user must sign in again.";

                    break;
                case "ERROR_EMAIL_ALREADY_IN_USE":

                    errorText = "The email address is already in use by another account.";

                    break;
                case "ERROR_USER_NOT_FOUND":

                    errorText = "There is no user record corresponding to this identifier.";

                    break;
                case "ERROR_OPERATION_NOT_ALLOWED":

                    errorText = "This operation is not allowed. You must enable this service in the console.";

                    break;
                case "ERROR_WEAK_PASSWORD":

                    errorText = "The given password is weak.";

                    break;
                case "ERROR_INVALID_USER_TOKEN":

                    errorText = "The user's credential is no longer valid. The user must sign in again.";

                    break;
            }

            return errorText;
        }
}
