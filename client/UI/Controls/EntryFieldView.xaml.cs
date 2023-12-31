using System.ComponentModel;
using System.Runtime.CompilerServices;

namespace LingoHammer.UI.Controls;

public partial class EntryFieldView
{
    private EntryFieldViewModel ViewModel => BindingContainer?.BindingContext as EntryFieldViewModel;

    //bindable Label property
    #region Label
    public static readonly BindableProperty LabelProperty = BindableProperty.Create(
            nameof(Label), typeof(string), typeof(EntryFieldView), default(string), BindingMode.TwoWay
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
            nameof(ErrorMessage), typeof(string), typeof(EntryFieldView), default(string), BindingMode.TwoWay
    );
    public string ErrorMessage
    {
        get => (string)GetValue(ErrorMessageProperty);
        set => SetValue(ErrorMessageProperty, value);
    }
    #endregion

    //bindable Text property
    #region Text
    public static readonly BindableProperty TextProperty = BindableProperty.Create(
                   nameof(Text), typeof(string), typeof(EntryFieldView), default(string), BindingMode.TwoWay
           );
    public string Text
    {
        get => (string)GetValue(TextProperty);
        set => SetValue(TextProperty, value);
    }
    #endregion

    public EntryFieldView()
    {
        InitializeComponent();

        ViewModel.PropertyChanged += OnViewModelPropertyChanged;
    }

    protected override void OnPropertyChanged([CallerMemberName] string propertyName = null)
    {
        base.OnPropertyChanged(propertyName);

        if (propertyName == LabelProperty.PropertyName)
        {
            ViewModel.Label = Label;
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
        if (e.PropertyName == nameof(EntryFieldViewModel.Label))
        {
            Label = ViewModel.Label;
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