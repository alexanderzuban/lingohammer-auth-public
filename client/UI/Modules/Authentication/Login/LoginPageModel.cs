using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using LingoHammer.Services;
using LingoHammer.UI.Modules.Authentication.Register;
using LingoHammer.UI.Modules.Authentication.RestorePassword;
using System.ComponentModel;

namespace LingoHammer.UI.Modules.Authentication.Login;


partial class LoginPageModel : AuthenticationModuleModel
{

    [ObservableProperty]
    private bool isLoginDataValid = false;


    public LoginPageModel()
    {
    }

    protected override void OnPropertyChanged(PropertyChangedEventArgs e)
    {
        base.OnPropertyChanged(e);

        if (e.PropertyName == nameof(Password) || e.PropertyName == nameof(IsEmailValid) || e.PropertyName == nameof(IsPasswordConfirmMatches))
        {
            CheckLoginData();
        }
    }

    private void CheckLoginData()
    {
        IsLoginDataValid = IsEmailValid && Password.Length > 0;
    }


    [RelayCommand(AllowConcurrentExecutions = true)]
    public async Task RegisterAsync()
    {
        await S.Navigation.ShowPageAsync(() => new RegisterPage());
    }


    [RelayCommand(AllowConcurrentExecutions = true)]
    public async Task ForgotPasswordAsync()
    {
        await S.Navigation.ShowPageAsync(() => new RestorePasswordPage());
    }

    [RelayCommand]
    public async Task LoginAsync()
    {
        ResetState();
        try
        {
            await S.Authentication.LoginAsync(Email, Password);

            (Application.Current as App).OnUserLoggedIn();
        }
        catch (AuthenticationServiceException ex)
        {
            Error = ex.ErrorCode switch
            {
                -1 => "User not found",
                -2 => "Wrong password",
                _ => ex.Message,
            };
        }
        finally
        {
            IsBusy = false;
        }

    }

    private void ResetState()
    {
        IsBusy = true;
        Error = "";
    }
}
