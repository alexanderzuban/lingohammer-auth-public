using LingoHammer.Services;
using LingoHammer.UI.Modules.Authentication;
using LingoHammer.UI.Modules.Main;

namespace LingoHammer;

public partial class App : Application
{
    public App()
    {
        Services = ConfigureServices();
        //TODO : API GATEWAY URL FOR AUTHENTICATION
        S.Settings.AuthService = "??????";


        InitializeComponent();

        MainPage = new EntryPage();

        MainThread.BeginInvokeOnMainThread(async () =>
        {
            try
            {
                await S.Authentication.RefreshTokenAsync();
                OnUserLoggedIn();
            }
            catch (Exception)
            {
                OnUserLoggedOut();
            }
        });


    }

    public void OnUserLoggedIn()
    {
        MainPage = new MainPage();
    }


    public void OnUserLoggedOut()
    {
        S.Authentication.Logout();
        MainPage = new LoginModulePage();
    }


    public IServiceProvider Services { get; }

    private static IServiceProvider ConfigureServices()
    {
        var services = new ServiceCollection();

        services.AddSingleton<INavigationService, NavigationService>();
        services.AddSingleton<IMessagesService, MessagesService>();
        services.AddSingleton<ILogService, LogService>();
        services.AddSingleton<IStorageService, StorageService>();
        services.AddSingleton<IEncryptedStorageService, EncryptedStorageService>();
        services.AddSingleton<IStateService, StateService>();
        services.AddSingleton<ISettingsService, SettingsService>();
        services.AddSingleton<ICryptographyService, CryptographyService>();
        services.AddSingleton<IAuthenticationService, AuthenticationService>();
        services.AddSingleton<ITimeService, TimeService>();


        return services.BuildServiceProvider();
    }
}
