using LingoHammer.Services;

namespace LingoHammer.UI.Modules.Main;

public class MainPageModel
{
    public string UserName => S.Authentication.UserName;
}
