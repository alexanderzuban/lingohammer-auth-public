using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using LingoHammer.Converters;
using LingoHammer.Services;

namespace LingoHammer.UI.Controls;

partial class EmailViewModel : ObservableObject
{
    [ObservableProperty]
    private string label = string.Empty;

    [ObservableProperty]
    private string email = string.Empty;

    [ObservableProperty]
    private bool isEmailValid;

    public EmailViewModel()
    {
    }

    [RelayCommand]
    private async Task EmailErrorInfoAsync()
    {
        await S.Messages.ShortMessageAsync("Email is not valid");
    }

    partial void OnEmailChanged(string value)
    {
        IsEmailValid = EmailValidationConverter.IsEmailValid(value);
    }

}
