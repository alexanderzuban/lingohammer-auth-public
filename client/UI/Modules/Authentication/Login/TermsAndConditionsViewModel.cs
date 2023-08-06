using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using LingoHammer.Services;
using LingoHammer.UI.Modules.Information;

namespace LingoHammer.UI.Modules.Authentication.Login
{
    partial class TermsAndConditionsViewModel : ObservableObject
    {


        public TermsAndConditionsViewModel()
        {
        }

        [RelayCommand(AllowConcurrentExecutions = false)]
        public async Task PrivacyPolicyAsync()
        {
            await S.Navigation.ShowPageAsync(() => new PrivacyPolicyPage());
        }

        [RelayCommand(AllowConcurrentExecutions = false)]
        public async Task TermsOfServiceAsync()
        {
            await S.Navigation.ShowPageAsync(() => new TermsOfServicePage());
        }
    }
}
