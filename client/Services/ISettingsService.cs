namespace LingoHammer.Services
{
    public interface ISettingsService
    {

        public string Secret { get; set; }

        public string AuthService { get; set; }


        public string GetSetting(string key);

        public void SetSetting(string key, string value);
    }


    public class SettingsService : ISettingsService
    {
        //in memory cache 
        private readonly Dictionary<string, string> _cache = new();

        public string Secret
        {
            get => GetSetting(nameof(Secret));
            set => SetSetting(nameof(Secret), value);
        }

        public string AuthService
        {
            get => GetSetting(nameof(AuthService));
            set => SetSetting(nameof(AuthService), value);
        }

        public string GetSetting(string key)
        {
            if (_cache.TryGetValue(key, out var value)) return value;

            value = SecureStorage.GetAsync(key).Result;
            if (value is not null)
            {
                _cache[key] = value;
            }
            return value;
        }

        public void SetSetting(string key, string value)
        {
            _cache[key] = value;

            Task.Run(async () => await SecureStorage.SetAsync(key, value));
        }
    }
}
