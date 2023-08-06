using LingoHammer.Auth;
using LingoHammer.Services;

namespace LingoHammer.UI.Modules.Authentication.Service
{
    public interface IAuthenticationService
    {
        public string UserName { get; }

        public bool IsAuthenticated { get; }

        public void StartRefreshToken(Callback callback);

        public void StartLogin(string email, string password, Callback callback);

        public void StartRegistration(string email, string password, string firstName, string lastName, Callback callback);

        public void StartRegistrationConfirmation(string email, string password, string confirmationCode, Callback callback);

        public void StartRequestPasswordReset(string email, Callback callback);

        public void StartRequestPasswordResetConfirmation(string email, string confirmationCode, string newPassword, Callback callback);

        public void Logout();
    }

    public class AuthenticationService : IAuthenticationService
    {
        private IStateService State { get; }
        private ISettingsService Settings { get; }
        private ITimeService Time { get; }

        private HttpClient HttpClient;
        private AuthClient AuthApi;

        public string UserName
        {
            get
            {
                var user = Authentication?.User;
                if (user?.Attributes?.TryGetValue("name", out var name) == true)
                {
                    return name;
                }
                return "Unknown";
            }

        }

        private Authentication Authentication
        {
            get => State.Get<Authentication>(nameof(Authentication));
            set => State.Set(nameof(Authentication), value);
        }

        public bool IsAuthenticated
        {
            get
            {
                var state = Authentication;
                if (state is null)
                {
                    return false;
                }

                return state.Token is not null && state.Token.RefreshToken is not null;
            }
        }


        public AuthenticationService(IStateService state, ISettingsService settings, ITimeService time)
        {
            State = state;
            Settings = settings;
            Time = time;

            HttpClient = new HttpClient
            {
                Timeout = TimeSpan.FromSeconds(30)
            };

            AuthApi = new AuthClient(Settings.AuthService, HttpClient);
        }


        public void Logout()
        {
            Authentication = null;
        }

        public void StartRefreshToken(Callback callback)
        {
            if (!IsAuthenticated)
            {
                callback.NotifyError("User is not authenticated");
                return;
            }

            if (IsAuthenticated && !ShouldRefreshToken())
            {
                callback.NotifySuccess();
                return;
            }

            callback.ExecuteAndNotifyOnException(async () =>
            {
                var request = new UserInfoRequest
                {
                    UserIdentifier = Authentication?.User?.UserIdentifier,
                    RefreshToken = Authentication?.Token?.RefreshToken
                };
                var response = await AuthApi.GetUserAsync(request);
                if (response.Success)
                {
                    Authentication = new Authentication
                    {
                        User = response.User,
                        Token = response.Token
                    };
                    callback.NotifyResult(response.Success, response.ErrorCode, response.ErrorMessage, null);
                }
            });
        }

        private bool ShouldRefreshToken()
        {
            var whenExpire = Authentication?.Token?.ExpiresAt;
            var now = Time.Now;

            whenExpire = whenExpire?.AddHours(-1);

            if (whenExpire is null)
            {
                return false;
            }

            return ((DateTimeOffset)whenExpire).LocalDateTime < now.LocalDateTime;
        }

        public void StartLogin(string email, string password, Callback callback)
        {
            callback.ExecuteAndNotifyOnException(async () =>
            {
                var request = new LoginRequest
                {
                    Email = email,
                    Password = password
                };

                var response = await AuthApi.LoginAsync(request) ?? throw new Exception("No response from server");
                if (response.Success)
                {
                    var user = response.User;
                    var token = response.Token;
                    Authentication = new Authentication
                    {
                        User = user,
                        Token = token
                    };
                }
                callback.NotifyResult(response.Success, response.ErrorCode, response.ErrorMessage, null);
            });
        }

        public void StartRegistration(string email, string password, string firstName, string lastName, Callback callback)
        {
            callback.ExecuteAndNotifyOnException(async () =>
            {
                var request = new RegisterRequest
                {
                    Email = email,
                    Password = password,
                    FirstName = firstName,
                    LastName = lastName
                };
                var response = await AuthApi.RegisterAsync(request) ?? throw new Exception("No response from server");
                callback.NotifyResult(response.Success, response.ErrorCode, response.ErrorMessage, null);
            });

        }

        public void StartRegistrationConfirmation(string email, string password, string confirmationCode, Callback callback)
        {
            callback.ExecuteAndNotifyOnException(async () =>
            {
                var request = new ConfirmRegistrationRequest
                {
                    Email = email,
                    Password = password,
                    ConfirmationCode = confirmationCode
                };
                var response = await AuthApi.ConfirmRegistrationAsync(request) ?? throw new Exception("No response from server");
                if (response.Success)
                {
                    var user = response.User;
                    var token = response.Token;
                    Authentication = new Authentication
                    {
                        User = user,
                        Token = token
                    };
                }

                callback.NotifyResult(response.Success, response.ErrorCode, response.ErrorMessage, null);
            });
        }

        public void StartRequestPasswordReset(string email, Callback callback)
        {
            callback.ExecuteAndNotifyOnException(async () =>
            {
                var request = new RestorePasswordRequest
                {
                    Email = email
                };
                var response = await AuthApi.PasswordResetAsync(request) ?? throw new Exception("No response from server");
                callback.NotifyResult(response.Success, response.ErrorCode, response.ErrorMessage, null);
            });
        }

        public void StartRequestPasswordResetConfirmation(string email, string confirmationCode, string newPassword, Callback callback)
        {
            callback.ExecuteAndNotifyOnException(async () =>
            {
                var request = new RestorePasswordConfirmRequest
                {
                    Email = email,
                    ConfirmationCode = confirmationCode,
                    NewPassword = newPassword
                };
                var response = await AuthApi.PasswordConfirmResetAsync(request) ?? throw new Exception("No response from server");
                if (response.Success)
                {
                    var user = response.User;
                    var token = response.Token;
                    Authentication = new Authentication
                    {
                        User = user,
                        Token = token
                    };
                }

                callback.NotifyResult(response.Success, response.ErrorCode, response.ErrorMessage, null);
            });
        }

    }
}
