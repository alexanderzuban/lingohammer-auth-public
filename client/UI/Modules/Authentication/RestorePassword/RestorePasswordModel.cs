using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using LingoHammer.Services;
using LingoHammer.UI.Modules.Authentication.Register;
using System.ComponentModel;

namespace LingoHammer.UI.Modules.Authentication.RestorePassword;


partial class RestorePasswordModel : AuthenticationModuleModel
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
    private async Task ConfirmRestorePasswordAsync()
    {
        StartOperation();
        try
        {
            await S.Authentication.PasswordConfirmResetAsync(Email, RestorePasswordConfirmationCode, Password);

            (Application.Current as App).OnUserLoggedIn();
        }
        catch (AuthenticationServiceException ex)
        {
            IsRestorePasswordConfirmationDataValid = false;
            Error = ex.Message;
        }
        finally
        {
            IsBusy = false;
        }
    }



    [RelayCommand]
    private async Task RestorePasswordAsync()
    {
        StartOperation();
        try
        {
            await S.Authentication.PasswordResetAsync(Email);
            CurrentState = RestorePasswordState.Confirm;
        }
        catch (AuthenticationServiceException ex)
        {
            Error = ex.Message;
        }
        finally
        {
            IsBusy = false;
        }

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





}


public static class RestorePasswordState
{
    public const string Init = nameof(Init);
    public const string Confirm = nameof(Confirm);
}
