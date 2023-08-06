using CommunityToolkit.Mvvm.ComponentModel;
using System.Windows.Input;

namespace LingoHammer.UI.Modules.Information
{

    partial class InformationViewModel : ObservableObject
    {
        [ObservableProperty]
        bool hasAcceptButton;

        [ObservableProperty]
        bool hasDeclineButton;

        [ObservableProperty]
        string acceptButtonText = "Accept";

        [ObservableProperty]
        string declineButtonText = "Decline";

        [ObservableProperty]
        string message = "<html><body></body></html>";

        [ObservableProperty]
        ICommand acceptCommand;

        [ObservableProperty]
        ICommand declineCommand;


        public InformationViewModel() { }
    }
}
