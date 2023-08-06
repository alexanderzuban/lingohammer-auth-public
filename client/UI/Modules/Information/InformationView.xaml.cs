using LingoHammer.Services;
using System.Runtime.CompilerServices;
using System.Windows.Input;

namespace LingoHammer.UI.Modules.Information;

public partial class InformationView : ContentView
{
    //Bindable Properties
    //Property to set resource key for content
    public static readonly BindableProperty ContentResourceKeyProperty = BindableProperty.Create(
        nameof(ContentResourceKey), typeof(string), typeof(InformationView), default(string)
    );
    public string ContentResourceKey
    {
        get => (string)GetValue(ContentResourceKeyProperty);
        set => SetValue(ContentResourceKeyProperty, value);
    }

    //property to set if the view has accept button
    public static readonly BindableProperty HasAcceptButtonProperty = BindableProperty.Create(
        nameof(HasAcceptButton), typeof(bool), typeof(InformationView), default(bool)
    );
    public bool HasAcceptButton
    {
        get => (bool)GetValue(HasAcceptButtonProperty);
        set => SetValue(HasAcceptButtonProperty, value);
    }

    //property to set if the view has decline button
    public static readonly BindableProperty HasDeclineButtonProperty = BindableProperty.Create(
        nameof(HasDeclineButton), typeof(bool), typeof(InformationView), default(bool)
   );
    public bool HasDeclineButton
    {
        get => (bool)GetValue(HasDeclineButtonProperty);
        set => SetValue(HasDeclineButtonProperty, value);
    }

    //property to set Accept button text
    public static readonly BindableProperty AcceptButtonTextProperty = BindableProperty.Create(
        nameof(AcceptButtonText), typeof(string), typeof(InformationView), default(string)
    );
    public string AcceptButtonText
    {
        get => (string)GetValue(AcceptButtonTextProperty);
        set => SetValue(AcceptButtonTextProperty, value);
    }

    //property to set Decline button text
    public static readonly BindableProperty DeclineButtonTextProperty = BindableProperty.Create(
        nameof(DeclineButtonText), typeof(string), typeof(InformationView), default(string)
    );
    public string DeclineButtonText
    {
        get => (string)GetValue(DeclineButtonTextProperty);
        set => SetValue(DeclineButtonTextProperty, value);
    }

    //property to set Accept button command
    public static readonly BindableProperty AcceptButtonCommandProperty = BindableProperty.Create(
        nameof(AcceptButtonCommand), typeof(ICommand), typeof(InformationView), default(ICommand)
    );
    public ICommand AcceptButtonCommand
    {
        get => (ICommand)GetValue(AcceptButtonCommandProperty);
        set => SetValue(AcceptButtonCommandProperty, value);
    }

    //property to set Decline button command
    public static readonly BindableProperty DeclineButtonCommandProperty = BindableProperty.Create(
        nameof(DeclineButtonCommand), typeof(ICommand), typeof(InformationView), default(ICommand)
    );
    public ICommand DeclineButtonCommand
    {
        get => (ICommand)GetValue(DeclineButtonCommandProperty);
        set => SetValue(DeclineButtonCommandProperty, value);
    }

    private InformationViewModel ViewModel => BindingContext as InformationViewModel;

    public InformationView()
    {
        InitializeComponent();
    }

    protected override void OnPropertyChanged([CallerMemberName] string propertyName = null)
    {
        base.OnPropertyChanged(propertyName);
        //transfer bindable properties to view model
        if (propertyName == HasAcceptButtonProperty.PropertyName)
        {
            ViewModel.HasAcceptButton = HasAcceptButton;
        }
        else if (propertyName == HasDeclineButtonProperty.PropertyName)
        {
            ViewModel.HasDeclineButton = HasDeclineButton;
        }
        else if (propertyName == AcceptButtonTextProperty.PropertyName)
        {
            ViewModel.AcceptButtonText = AcceptButtonText;
        }
        else if (propertyName == DeclineButtonTextProperty.PropertyName)
        {
            ViewModel.DeclineButtonText = DeclineButtonText;
        }
        else if (propertyName == AcceptButtonCommandProperty.PropertyName)
        {
            ViewModel.AcceptCommand = AcceptButtonCommand;
        }
        else if (propertyName == DeclineButtonCommandProperty.PropertyName)
        {
            ViewModel.DeclineCommand = DeclineButtonCommand;
        }
        else if (propertyName == ContentResourceKeyProperty.PropertyName)
        {
            LoadResourceAssetAsync();
        }
    }

    private async void LoadResourceAssetAsync()
    {
        try
        {
            using var stream = await FileSystem.OpenAppPackageFileAsync(ContentResourceKey);
            using var reader = new StreamReader(stream);

            var contents = reader.ReadToEnd();

            ViewModel.Message = contents;
        }
        catch (Exception ex)
        {
            S.Log.Error(ex);
        }
    }
}