using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using LingoHammer.Services;
using System.ComponentModel;

namespace LingoHammer.UI.Modules.Authentication.Register;


partial class RegisterPageModel : AuthenticationModuleModel
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

    [RelayCommand(AllowConcurrentExecutions = false)]
    private async Task ConfirmRegistrationAsync()
    {
        StartOperation();

        try
        {
            await S.Authentication.ConfirmRegistrationAsync(Email, Password, RegistrationConfirmationCode);

            (Application.Current as App).OnUserLoggedIn();
        }
        catch (AuthenticationServiceException ex)
        {
            IsConfirmRegistrationFailed = true;
            Error = ex.Message;
        }
        finally
        {
            IsBusy = false;
        }
    }


    [RelayCommand]
    private async Task RegisterAsync()
    {
        StartOperation();
        try
        {
            await S.Authentication.RegisterAsync(Email, Password, FirstName, LastName);
            CurrentState = RegisterPageModelState.Confirm;
        }
        catch (AuthenticationServiceException ex)
        {
            if (ex.ErrorCode == -1)
            {
                IsUserAlreadyExists = true;
            }

            Error = ex.Message;
        }
        finally
        {
            IsBusy = false;
        }
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


}

public static class RegisterPageModelState
{
    public const string Init = nameof(Init);
    public const string Confirm = nameof(Confirm);
}
