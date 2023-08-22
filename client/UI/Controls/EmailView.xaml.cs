using System.ComponentModel;
using System.Runtime.CompilerServices;

namespace LingoHammer.UI.Controls;

public partial class EmailView
{
    //bindable property Email
    #region Email
    public static readonly BindableProperty EmailProperty = BindableProperty.Create(
               nameof(Email), typeof(string), typeof(EmailView), default(string), BindingMode.TwoWay
    );
    public string Email
    {
        get => (string)GetValue(EmailProperty);
        set => SetValue(EmailProperty, value);
    }
    #endregion

    //bindable property Label
    #region Label
    public static readonly BindableProperty LabelProperty = BindableProperty.Create(
        nameof(Label), typeof(string), typeof(EmailView), default(string), BindingMode.TwoWay
     );
    public string Label
    {
        get => (string)GetValue(LabelProperty);
        set => SetValue(LabelProperty, value);
    }
    #endregion

    private EmailViewModel ViewModel => BindingContainer?.BindingContext as EmailViewModel;

    public EmailView()
    {
        InitializeComponent();
        ViewModel.PropertyChanged += OnViewModelPropertyChanged;
    }

    protected override void OnPropertyChanged([CallerMemberName] string propertyName = null)
    {
        base.OnPropertyChanged(propertyName);

        if (propertyName == EmailProperty.PropertyName)
        {
            ViewModel.Email = Email;
        }

        if (propertyName == LabelProperty.PropertyName)
        {
            ViewModel.Label = Label;
        }
    }

    private void OnViewModelPropertyChanged(object sender, PropertyChangedEventArgs e)
    {
        if (e.PropertyName == nameof(EmailViewModel.Email))
        {
            Email = ViewModel.Email;
        }
    }



}