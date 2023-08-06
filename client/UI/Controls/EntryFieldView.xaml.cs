using System.ComponentModel;
using System.Runtime.CompilerServices;

namespace LingoHammer.UI.Controls;

public partial class EntryFieldView
{
    private EntryFieldViewModel ViewModel => BindingContainer?.BindingContext as EntryFieldViewModel;

    //bindable Caption property
    public static readonly BindableProperty CaptionProperty = BindableProperty.Create(
            nameof(Caption), typeof(string), typeof(EntryFieldView), default(string), BindingMode.TwoWay
     );
    public string Caption
    {
        get => (string)GetValue(CaptionProperty);
        set => SetValue(CaptionProperty, value);
    }

    //bindable ErrorMessage property
    public static readonly BindableProperty ErrorMessageProperty = BindableProperty.Create(
            nameof(ErrorMessage), typeof(string), typeof(EntryFieldView), default(string), BindingMode.TwoWay
    );
    public string ErrorMessage
    {
        get => (string)GetValue(ErrorMessageProperty);
        set => SetValue(ErrorMessageProperty, value);
    }

    //bindable Text property
    public static readonly BindableProperty TextProperty = BindableProperty.Create(
                   nameof(Text), typeof(string), typeof(EntryFieldView), default(string), BindingMode.TwoWay
           );
    public string Text
    {
        get => (string)GetValue(TextProperty);
        set => SetValue(TextProperty, value);
    }

    public EntryFieldView()
    {
        InitializeComponent();

        ViewModel.PropertyChanged += OnViewModelPropertyChanged;
    }

    protected override void OnPropertyChanged([CallerMemberName] string propertyName = null)
    {
        base.OnPropertyChanged(propertyName);

        if (propertyName == CaptionProperty.PropertyName)
        {
            ViewModel.Caption = Caption;
        }

        if (propertyName == ErrorMessageProperty.PropertyName)
        {
            ViewModel.ErrorMessage = ErrorMessage;
        }

        if (propertyName == TextProperty.PropertyName)
        {
            ViewModel.Text = Text;
        }
    }

    private void OnViewModelPropertyChanged(object sender, PropertyChangedEventArgs e)
    {
        if (e.PropertyName == nameof(EntryFieldViewModel.Caption))
        {
            Caption = ViewModel.Caption;
        }

        if (e.PropertyName == nameof(EntryFieldViewModel.ErrorMessage))
        {
            ErrorMessage = ViewModel.ErrorMessage;
        }

        if (e.PropertyName == nameof(EntryFieldViewModel.Text))
        {
            Text = ViewModel.Text;
        }
    }

}