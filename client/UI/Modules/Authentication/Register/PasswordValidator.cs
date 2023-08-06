namespace LingoHammer.UI.Modules.Authentication.Register
{
    internal class PasswordValidator
    {
        public const string SpecialCharacters = "^$*.[]{}()?-\"!@#%&/\\,><':;|_~`+=";
        public static bool Validate(string password)
        {
            //Password requirements
            //8 character(s)
            //Contains at least 1 number
            //Contains at least 1 special character
            //Contains at least 1 uppercase letter
            //Contains at least 1 lowercase letter
            if (string.IsNullOrEmpty(password))
            {
                return false;
            }

            if (password.Length < 8)
            {
                return false;
            }
            if (!password.Any(char.IsUpper))
            {
                return false;
            }

            if (!password.Any(char.IsLower))
            {
                return false;
            }

            if (!password.Any(char.IsDigit))
            {
                return false;
            }

            if (!password.Any(SpecialCharacters.Contains))
            {
                return false;
            }

            return true;
        }

        internal static string GetPasswordStrengthError(string password)
        {

            if (password.Length < 8)
            {
                return "Password must be at least 8 characters long";
            }
            if (!password.Any(char.IsUpper))
            {
                return "Password must contain at least 1 uppercase letter";
            }

            if (!password.Any(char.IsLower))
            {
                return "Password must contain at least 1 lowercase letter";
            }

            if (!password.Any(char.IsDigit))
            {
                return "Password must contain at least 1 number";
            }

            if (!password.Any(SpecialCharacters.Contains))
            {
                return "Password must contain at least 1 special character";
            }

            return "";
        }
    }
}
