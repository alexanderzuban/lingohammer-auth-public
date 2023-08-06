using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using LingoHammer.Converters;
using LingoHammer.Services;
using System.Windows.Input;

namespace LingoHammer.UI.Controls
{
    partial class EmailViewModel : ObservableObject
    {
        [ObservableProperty]
        private string caption = string.Empty;

        [ObservableProperty]
        private string email = string.Empty;

        [ObservableProperty]
        private bool isEmailValid;

        public ICommand EmailErrorInfoCommand { get; }

        public EmailViewModel()
        {
            EmailErrorInfoCommand = new AsyncRelayCommand(OnEmailErrorInfoCommandExecuted);
        }


        private async Task OnEmailErrorInfoCommandExecuted()
        {
            await S.Messages.ShortMessageAsync("Email is not valid");
        }

        partial void OnEmailChanged(string value)
        {
            IsEmailValid = EmailValidationConverter.IsEmailValid(value);
        }

    }
}
