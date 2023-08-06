using System.ComponentModel;
using System.Runtime.CompilerServices;

namespace LingoHammer.UI.Controls;

public partial class EmailView
{
    //bindable property Email
    public static readonly BindableProperty EmailProperty = BindableProperty.Create(
               nameof(Email), typeof(string), typeof(EmailView), default(string), BindingMode.TwoWay
    );
    public string Email
    {
        get => (string)GetValue(EmailProperty);
        set => SetValue(EmailProperty, value);
    }

    //bindable property Caption
    public static readonly BindableProperty CaptionProperty = BindableProperty.Create(
        nameof(Caption), typeof(string), typeof(EmailView), default(string), BindingMode.TwoWay
     );
    public string Caption
    {
        get => (string)GetValue(CaptionProperty);
        set => SetValue(CaptionProperty, value);
    }

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

        if (propertyName == CaptionProperty.PropertyName)
        {
            ViewModel.Caption = Caption;
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