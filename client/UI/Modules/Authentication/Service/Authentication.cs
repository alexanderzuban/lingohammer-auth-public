using LingoHammer.Auth;

namespace LingoHammer.UI.Modules.Authentication.Service
{
    public class Authentication
    {
        public UserInfo User { get; set; }

        public SecurityToken Token { get; set; }

        public Authentication()
        {
        }
    }
}
