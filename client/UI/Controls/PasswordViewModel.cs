using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;

namespace LingoHammer.UI.Controls;

partial class PasswordViewModel : ObservableObject
{
    [ObservableProperty]
    private string label = string.Empty;

    [ObservableProperty]
    private string password = string.Empty;

    [ObservableProperty]
    private bool isShowPassword;

    [ObservableProperty]
    private bool isPasswordFocused;

    [ObservableProperty]
    private string errorMessage = string.Empty;


    public PasswordViewModel()
    {
    }

    //focus changed handler 
    partial void OnIsPasswordFocusedChanged(bool value)
    {
        if (!IsPasswordFocused)
        {
            IsShowPassword = false;
        }
    }

    [RelayCommand]
    private void TogglePassword()
    {
        IsShowPassword = !IsShowPassword;

        if (IsShowPassword)
        {
            MainThread.BeginInvokeOnMainThread(async () =>
            {
                await Task.Delay(3000);
                IsShowPassword = false;
            });
        }
    }
}
