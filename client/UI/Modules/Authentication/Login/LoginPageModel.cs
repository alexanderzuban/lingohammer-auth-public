using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using LingoHammer.Services;
using LingoHammer.UI.Modules.Authentication.Register;
using LingoHammer.UI.Modules.Authentication.RestorePassword;
using System.ComponentModel;

namespace LingoHammer.UI.Modules.Authentication.Login;


partial class LoginPageModel : EmailPasswordModel
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
    public void Login()
    {
        ResetState();
        S.Authentication.StartLogin(Email, Password, (success, result, message, error) =>
        {
            IsBusy = false;
            if (success)
            {
                (Application.Current as App).OnUserLoggedIn();
            }
            else
            {
                Error = message ?? error?.Message ?? "Login failed";
            }
        });

    }

    private void ResetState()
    {
        IsBusy = true;
        Error = "";
    }
}
