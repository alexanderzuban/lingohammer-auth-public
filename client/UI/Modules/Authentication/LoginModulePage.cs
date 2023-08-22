using LingoHammer.UI.Controls.Disposable;
using LingoHammer.UI.Modules.Authentication.Login;

namespace LingoHammer.UI.Modules.Authentication;

public class LoginModulePage : NavigationPageExt
{
    public LoginModulePage() : base(new LoginPage())
    {
    }
}