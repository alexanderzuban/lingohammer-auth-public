using CommunityToolkit.Mvvm.ComponentModel;
using System.Windows.Input;

namespace LingoHammer.UI.Controls;

partial class ConfirmCancelViewModel : ObservableObject
{
    [ObservableProperty]
    private ICommand cancelCommand;

    [ObservableProperty]
    private ICommand confirmCommand;

    [ObservableProperty]
    private string cancelText = "Cancel";

    [ObservableProperty]
    private string confirmText = "Confirm";

    [ObservableProperty]
    private bool canConfirm = true;

    [ObservableProperty]
    private bool canCancel = true;

}
