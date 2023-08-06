using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using LingoHammer.Services;
using LingoHammer.UI.Modules.Authentication.Register;
using System.ComponentModel;

namespace LingoHammer.UI.Modules.Authentication.RestorePassword;


partial class RestorePasswordModel : EmailPasswordModel
{
    [ObservableProperty]
    private string restorePasswordConfirmationCode = string.Empty;

    [ObservableProperty]
    private bool isRestorePasswordDataValid;

    [ObservableProperty]
    private bool isRestorePasswordConfirmationDataValid;

    [ObservableProperty]
    private string passwordStrengthError;

    public RestorePasswordModel()
    {
        CurrentState = RestorePasswordState.Init;
    }


    [RelayCommand]
    private void ConfirmRestorePassword()
    {
        StartOperation();
        S.Authentication.StartRequestPasswordResetConfirmation(Email, RestorePasswordConfirmationCode, Password, ProcessConfirmRestorePasswordResults);
    }

    [RelayCommand]
    private void RestorePassword()
    {
        StartOperation();
        S.Authentication.StartRequestPasswordReset(Email, ProcessRequestPasswordResults);
    }

    [RelayCommand]
    private void EditResetPasswordData()
    {
        CurrentState = RestorePasswordState.Init;
    }

    protected override void OnPropertyChanged(PropertyChangedEventArgs e)
    {
        base.OnPropertyChanged(e);

        if (e.PropertyName == nameof(Password) || e.PropertyName == nameof(IsPasswordConfirmMatches) || e.PropertyName == nameof(IsEmailValid) || e.PropertyName == nameof(IsPasswordConfirmMatches))
        {
            CheckRestorePasswordData();
        }
    }

    partial void OnRestorePasswordConfirmationCodeChanged(string value)
    {
        IsRestorePasswordConfirmationDataValid = !string.IsNullOrEmpty(value);
    }


    private void CheckRestorePasswordData()
    {
        PasswordStrengthError = PasswordValidator.GetPasswordStrengthError(Password);
        IsRestorePasswordDataValid = IsEmailValid
            && IsPasswordConfirmMatches
            && Password.Length > 0;
    }



    private void StartOperation()
    {
        Error = string.Empty;
        IsBusy = true;
        IsRestorePasswordConfirmationDataValid = true;
    }

    private void ProcessConfirmRestorePasswordResults(bool success, object result, string message, Exception error)
    {
        IsBusy = false;
        if (success)
        {
            (Application.Current as App).OnUserLoggedIn();
        }
        else
        {
            IsRestorePasswordConfirmationDataValid = false;
            Error = message ?? error.Message ?? "Password reset confirmation failed";
        }
    }




    private void ProcessRequestPasswordResults(bool success, object result, string message, Exception error)
    {
        IsBusy = false;
        if (success)
        {
            CurrentState = RestorePasswordState.Confirm;
        }
        else
        {
            Error = message ?? error.Message ?? "Password reset request failed";
        }
    }
}


public static class RestorePasswordState
{
    public const string Init = nameof(Init);
    public const string Confirm = nameof(Confirm);
}
