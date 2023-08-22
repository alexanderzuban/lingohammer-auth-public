using LingoHammer.Auth;

namespace LingoHammer.Services;

public interface IAuthenticationService
{
    bool IsAuthenticated { get; }

    string UserName { get; }

    Authentication Authentication { get; }

    Task RefreshTokenAsync();

    Task LoginAsync(string email, string password);

    Task RegisterAsync(string email, string password, string firstName, string lastName);

    Task ConfirmRegistrationAsync(string email, string password, string confirmationCode);

    Task PasswordResetAsync(string email);

    Task PasswordConfirmResetAsync(string email, string confirmationCode, string newPassword);

    void Logout();
}


public class Authentication
{
    public UserInfo User { get; set; }

    public SecurityToken SecurityToken { get; set; }

    public Authentication()
    {
    }
}

public class AuthenticationServiceException : Exception
{
    public int ErrorCode { get; set; }

    public AuthenticationServiceException(string message) : base(message)
    {
    }

    public AuthenticationServiceException(string message, int errorCode) : base(message)
    {
        ErrorCode = errorCode;
    }

    public AuthenticationServiceException(string message, Exception innerException) : base(message, innerException)
    {
    }
}

public class AuthenticationService : IAuthenticationService
{
    private IStateService State { get; }
    private ISettingsService Settings { get; }
    private ITimeService Time { get; }

    private readonly HttpClient Client;
    private readonly AuthClient AuthApi;

    public Authentication Authentication
    {
        get => State.GetEncrypted<Authentication>(nameof(Authentication));
        set => State.SetEncrypted(nameof(Authentication), value);
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

            return state.SecurityToken is not null && state.SecurityToken.RefreshToken is not null;
        }
    }

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



    public AuthenticationService(IStateService state, ISettingsService settings, ITimeService time)
    {
        State = state;
        Settings = settings;
        Time = time;

        Client = new HttpClient
        {
            Timeout = TimeSpan.FromSeconds(30)
        };

        AuthApi = new AuthClient(Settings.AuthService, Client);
    }

    public void Logout()
    {
        Authentication = new Authentication();
    }


    public async Task RefreshTokenAsync()
    {
        if (!IsAuthenticated)
        {
            throw new Exception("User is not authenticated");
        }

        if (IsAuthenticated && !ShouldRefreshToken())
        {
            return;
        }

        try
        {
            var request = new RefreshTokenRequest
            {
                UserIdentifier = Authentication?.User?.UserIdentifier,
                RefreshToken = Authentication?.SecurityToken?.RefreshToken
            };

            var response = await AuthApi.RefreshUserTokenAsync(request);
            if (response.Success)
            {
                Authentication = new Authentication
                {
                    User = response.User,
                    SecurityToken = response.Token
                };
            }
            else
            {
                throw new Exception(response.ErrorMessage);
            }

        }
        catch (Exception ex)
        {
            if (ex is AuthApiException<IsFlawed> ae)
            {
                throw new AuthenticationServiceException(ae.Result.ErrorMessage, ae.Result.ErrorCode);
            }
            throw new AuthenticationServiceException(ex.Message, ex);
        }
    }

    private bool ShouldRefreshToken()
    {
        var whenExpire = Authentication?.SecurityToken?.ExpiresAt;
        var now = Time.Now;

        whenExpire = whenExpire?.AddHours(-1);

        if (whenExpire is null)
        {
            return false;
        }

        return ((DateTimeOffset)whenExpire).LocalDateTime < now.LocalDateTime;
    }

    public async Task LoginAsync(string email, string password)
    {
        var request = new LoginRequest
        {
            Email = email,
            Password = password
        };

        try
        {
            var response = await AuthApi.LoginAsync(request) ?? throw new Exception("No response from server");
            if (response.Success)
            {
                var user = response.User;
                var token = response.Token;
                Authentication = new Authentication
                {
                    User = user,
                    SecurityToken = token
                };
            }
            else
            {
                throw new Exception(response.ErrorMessage);
            }
        }
        catch (Exception ex)
        {
            if (ex is AuthApiException<IsFlawed> ae)
            {
                throw new AuthenticationServiceException(ae.Result.ErrorMessage, ae.Result.ErrorCode);
            }
            throw new AuthenticationServiceException(ex.Message, ex);
        }
    }

    public async Task RegisterAsync(string email, string password, string firstName, string lastName)
    {
        try
        {
            var request = new RegistrationRequest
            {
                Email = email,
                Password = password,
                FirstName = firstName,
                LastName = lastName
            };
            var response = await AuthApi.RegisterAsync(request) ?? throw new Exception("No response from server");
            if (!response.Success)
            {
                throw new Exception(response.ErrorMessage);
            }
        }
        catch (Exception ex)
        {
            if (ex is AuthApiException<IsFlawed> ae)
            {
                throw new AuthenticationServiceException(ae.Result.ErrorMessage, ae.Result.ErrorCode);
            }
            throw new AuthenticationServiceException(ex.Message, ex);
        }
    }

    public async Task ConfirmRegistrationAsync(string email, string password, string confirmationCode)
    {
        try
        {
            var request = new RegistrationConfirmRequest
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
                    SecurityToken = token
                };
            }
            else
            {
                throw new Exception(response.ErrorMessage);
            }
        }
        catch (Exception ex)
        {
            if (ex is AuthApiException<IsFlawed> ae)
            {
                throw new AuthenticationServiceException(ae.Result.ErrorMessage, ae.Result.ErrorCode);
            }
            throw new AuthenticationServiceException(ex.Message, ex);
        }

    }

    public async Task PasswordResetAsync(string email)
    {
        try
        {
            var request = new RestorePasswordRequest
            {
                Email = email
            };
            var response = await AuthApi.PasswordResetAsync(request) ?? throw new Exception("No response from server");
            if (!response.Success)
            {
                throw new Exception(response.ErrorMessage);
            }
        }
        catch (Exception ex)
        {
            if (ex is AuthApiException<IsFlawed> ae)
            {
                throw new AuthenticationServiceException(ae.Result.ErrorMessage, ae.Result.ErrorCode);
            }
            throw new AuthenticationServiceException(ex.Message, ex);
        }
    }

    public async Task PasswordConfirmResetAsync(string email, string confirmationCode, string newPassword)
    {
        try
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
                    SecurityToken = token
                };
            }
            else
            {
                throw new Exception(response.ErrorMessage);
            }
        }
        catch (Exception ex)
        {
            if (ex is AuthApiException<IsFlawed> ae)
            {
                throw new AuthenticationServiceException(ae.Result.ErrorMessage, ae.Result.ErrorCode);
            }
            throw new AuthenticationServiceException(ex.Message, ex);
        }
    }
}
