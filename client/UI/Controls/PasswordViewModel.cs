using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using System.Windows.Input;

namespace LingoHammer.UI.Controls
{
    partial class PasswordViewModel : ObservableObject
    {
        [ObservableProperty]
        private string caption = string.Empty;

        [ObservableProperty]
        private string password = string.Empty;

        [ObservableProperty]
        private bool isShowPassword;

        [ObservableProperty]
        private bool isPasswordFocused;

        [ObservableProperty]
        private string errorMessage = string.Empty;

        public ICommand TogglePasswordCommand { get; }

        public PasswordViewModel()
        {
            TogglePasswordCommand = new RelayCommand(OnTogglePasswordCommandExecuted);
        }

        //focus changed handler 
        partial void OnIsPasswordFocusedChanged(bool value)
        {
            if (!IsPasswordFocused)
            {
                IsShowPassword = false;
            }
        }

        private void OnTogglePasswordCommandExecuted()
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

}
