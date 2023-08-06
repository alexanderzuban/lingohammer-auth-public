using System.ComponentModel;
using System.Runtime.CompilerServices;

namespace LingoHammer.UI.Controls;

public partial class PasswordView : ContentView
{
    //bindable Password property
    public static readonly BindableProperty PasswordProperty = BindableProperty.Create(
      nameof(Password), typeof(string), typeof(PasswordView), default(string), BindingMode.TwoWay
   );
    public string Password
    {
        get => (string)GetValue(PasswordProperty);
        set => SetValue(PasswordProperty, value);
    }


    //bindable Caption property
    public static readonly BindableProperty CaptionProperty = BindableProperty.Create(
       nameof(Caption), typeof(string), typeof(PasswordView), default(string), BindingMode.TwoWay
    );
    public string Caption
    {
        get => (string)GetValue(CaptionProperty);
        set => SetValue(CaptionProperty, value);
    }

    //bindable ErrorMessage property
    public static readonly BindableProperty ErrorMessageProperty = BindableProperty.Create(
       nameof(ErrorMessage), typeof(string), typeof(PasswordView), default(string), BindingMode.TwoWay
    );
    public string ErrorMessage
    {
        get => (string)GetValue(ErrorMessageProperty);
        set => SetValue(ErrorMessageProperty, value);
    }



    private PasswordViewModel ViewModel => BindingContainer?.BindingContext as PasswordViewModel;

    public PasswordView()
    {
        InitializeComponent();

        ViewModel.PropertyChanged += OnViewModelPropertyChanged;
    }

    protected override void OnPropertyChanged([CallerMemberName] string propertyName = null)
    {
        base.OnPropertyChanged(propertyName);

        if (propertyName == PasswordProperty.PropertyName)
        {
            ViewModel.Password = Password;
        }

        if (propertyName == CaptionProperty.PropertyName)
        {
            ViewModel.Caption = Caption;
        }

        if (propertyName == ErrorMessageProperty.PropertyName)
        {
            ViewModel.ErrorMessage = ErrorMessage;
        }
    }

    private void OnViewModelPropertyChanged(object sender, PropertyChangedEventArgs e)
    {
        if (e.PropertyName == nameof(PasswordViewModel.Password))
        {
            Password = ViewModel.Password;
        }
    }

    private void OnPasswordFocused(object sender, FocusEventArgs e)
    {
        if (ViewModel is not null)
        {
            ViewModel.IsPasswordFocused = true;
        }
    }

    private void OnPasswordUnfocused(object sender, FocusEventArgs e)
    {
        if (ViewModel is not null)
        {
            ViewModel.IsPasswordFocused = false;
        }
    }
}