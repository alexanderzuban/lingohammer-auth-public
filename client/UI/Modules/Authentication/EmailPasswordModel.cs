using CommunityToolkit.Mvvm.ComponentModel;
using LingoHammer.Converters;

namespace LingoHammer.UI.Modules.Authentication
{
    partial class EmailPasswordModel : ObservableObject
    {
        [ObservableProperty]
        private string firstName = string.Empty;

        [ObservableProperty]
        private string lastName = string.Empty;

        [ObservableProperty]
        private string email = string.Empty;

        [ObservableProperty]
        private bool isEmailValid;

        [ObservableProperty]
        private string password = string.Empty;

        [ObservableProperty]
        private string confirmPassword = string.Empty;

        [ObservableProperty]
        private bool isPasswordConfirmMatches;

        [ObservableProperty]
        private string confirmPasswordError;

        [ObservableProperty]
        private string error = string.Empty;

        [ObservableProperty]
        private string currentState;

        [ObservableProperty]
        private bool isBusy;

        public EmailPasswordModel()
        {
        }

        partial void OnEmailChanged(string value)
        {
            IsEmailValid = EmailValidationConverter.IsEmailValid(value);
        }

        partial void OnPasswordChanged(string value)
        {
            RefreshConfirmMatchError();
        }

        partial void OnConfirmPasswordChanged(string value)
        {

            RefreshConfirmMatchError();
        }

        private void RefreshConfirmMatchError()
        {
            IsPasswordConfirmMatches = (Password == ConfirmPassword);

            if (IsPasswordConfirmMatches || string.IsNullOrEmpty(Password) || string.IsNullOrEmpty(ConfirmPassword))
            {
                ConfirmPasswordError = string.Empty;
            }
            else
            {
                ConfirmPasswordError = "Password does not match";
            }
        }
    }
}
