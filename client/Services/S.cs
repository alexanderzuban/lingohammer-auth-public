using LingoHammer.UI.Modules.Authentication.Service;

namespace LingoHammer.Services
{
    public static class S
    {
        private static IServiceProvider Provider => (Application.Current as App)?.Services;

        public static IMessagesService Messages => Provider?.GetService<IMessagesService>();

        public static INavigationService Navigation => Provider?.GetService<INavigationService>();

        public static ILogService Log => Provider?.GetService<ILogService>();

        public static IStateService State => Provider?.GetService<IStateService>();

        public static IStorageService Storage => Provider?.GetService<IStorageService>();

        public static IEncryptedStorageService EncryptedStorage => Provider?.GetService<IEncryptedStorageService>();

        public static ISettingsService Settings => Provider?.GetService<ISettingsService>();

        public static ICryptographyService Cryptography => Provider?.GetService<ICryptographyService>();

        public static IAuthenticationService Authentication => Provider?.GetService<IAuthenticationService>();

        public static ITimeService Time => Provider?.GetService<ITimeService>();
    }
}
