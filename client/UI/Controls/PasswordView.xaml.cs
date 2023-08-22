using System.ComponentModel;
using System.Runtime.CompilerServices;

namespace LingoHammer.UI.Controls;

public partial class PasswordView
{
    //bindable Password property
    #region Password
    public static readonly BindableProperty PasswordProperty = BindableProperty.Create(
      nameof(Password), typeof(string), typeof(PasswordView), default(string), BindingMode.TwoWay
   );
    public string Password
    {
        get => (string)GetValue(PasswordProperty);
        set => SetValue(PasswordProperty, value);
    }
    #endregion

    //bindable Label property
    #region Label
    public static readonly BindableProperty LabelProperty = BindableProperty.Create(
       nameof(Label), typeof(string), typeof(PasswordView), default(string), BindingMode.TwoWay
    );
    public string Label
    {
        get => (string)GetValue(LabelProperty);
        set => SetValue(LabelProperty, value);
    }
    #endregion

    //bindable ErrorMessage property
    #region ErrorMessage
    public static readonly BindableProperty ErrorMessageProperty = BindableProperty.Create(
       nameof(ErrorMessage), typeof(string), typeof(PasswordView), default(string), BindingMode.TwoWay
    );
    public string ErrorMessage
    {
        get => (string)GetValue(ErrorMessageProperty);
        set => SetValue(ErrorMessageProperty, value);
    }
    #endregion  



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

        if (propertyName == LabelProperty.PropertyName)
        {
            ViewModel.Label = Label;
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