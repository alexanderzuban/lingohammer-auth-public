using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using LingoHammer.Auth;
using LingoHammer.Services;
using System.ComponentModel;

namespace LingoHammer.UI.Modules.Authentication.Register;


partial class RegisterPageModel : EmailPasswordModel
{
    [ObservableProperty]
    private string firstName = string.Empty;

    [ObservableProperty]
    private string lastName = string.Empty;

    [ObservableProperty]
    private string registrationConfirmationCode = string.Empty;

    [ObservableProperty]
    private bool isRegistrationDataValid = false;

    [ObservableProperty]
    private bool isRegistrationConfirmationDataValid = false;

    [ObservableProperty]
    public bool isUserAlreadyExists = false;

    [ObservableProperty]
    private bool isConfirmRegistrationFailed = false;

    [ObservableProperty]
    private string passwordStrengthError;

    public RegisterPageModel()
    {
        CurrentState = RegisterPageModelState.Init;
    }

    [RelayCommand]
    private void RestorePassword()
    {
    }

    [RelayCommand]
    private void EditRegistrationData()
    {
        CurrentState = RegisterPageModelState.Init;
    }

    [RelayCommand]
    private void ConfirmRegistration()
    {
        StartOperation();
        S.Authentication.StartRegistrationConfirmation(Email, Password, RegistrationConfirmationCode, ProcessRegistrationConfirmationResults);
    }

    [RelayCommand]
    private void Register()
    {
        StartOperation();
        S.Authentication.StartRegistration(Email, Password, FirstName, LastName, ProcessRegistrationResults);
    }


    protected override void OnPropertyChanged(PropertyChangedEventArgs e)
    {
        base.OnPropertyChanged(e);

        if (e.PropertyName == nameof(Password) || e.PropertyName == nameof(IsEmailValid) || e.PropertyName == nameof(IsPasswordConfirmMatches))
        {
            CheckRegistrationData();
        }
    }

    partial void OnRegistrationConfirmationCodeChanged(string value)
    {
        IsRegistrationConfirmationDataValid = !string.IsNullOrEmpty(value);
    }


    private void CheckRegistrationData()
    {
        PasswordStrengthError = PasswordValidator.GetPasswordStrengthError(Password);
        IsRegistrationDataValid = IsEmailValid && IsPasswordConfirmMatches && Password.Length > 0;
    }



    private void StartOperation()
    {
        Error = "";
        IsBusy = true;
        IsConfirmRegistrationFailed = false;
        IsUserAlreadyExists = false;
    }

    private void ProcessRegistrationConfirmationResults(bool success, object result, string message, Exception error)
    {
        IsBusy = false;
        if (success)
        {
            (Application.Current as App).OnUserLoggedIn();
        }
        else
        {
            IsConfirmRegistrationFailed = true;
            S.Log.Error("Registration confirmation failed", error);
            Error = message ?? error?.Message ?? "Registration confirmation failed";
        }
    }


    private void ProcessRegistrationResults(bool success, object result, string message, Exception ex)
    {
        IsBusy = false;
        if (success)
        {
            CurrentState = RegisterPageModelState.Confirm;
        }
        else
        {
            if (result is IsFlawed response)
            {
                if (response.ErrorCode == -1)
                {
                    IsUserAlreadyExists = true;
                    Error = response.ErrorMessage;
                    return;
                }
            }

            S.Log.Error("Registration failed", ex);
            Error = message ?? ex?.Message ?? "Registration failed";
        }
    }
}

public static class RegisterPageModelState
{
    public const string Init = nameof(Init);
    public const string Confirm = nameof(Confirm);
}
