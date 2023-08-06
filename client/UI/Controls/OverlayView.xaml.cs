using System.Runtime.CompilerServices;

namespace LingoHammer.UI.Controls;

public partial class OverlayView
{

    //bindable property busyTimeout
    public static readonly BindableProperty BusyTimeoutProperty =
        BindableProperty.Create(nameof(BusyTimeout), typeof(int), typeof(OverlayView), 10000);
    public int BusyTimeout
    {
        get => (int)GetValue(BusyTimeoutProperty);
        set => SetValue(BusyTimeoutProperty, value);
    }

    //bindable property busyDelay
    public static readonly BindableProperty BusyDelayProperty =
        BindableProperty.Create(nameof(BusyDelay), typeof(int), typeof(OverlayView), 1000);
    public int BusyDelay
    {
        get => (int)GetValue(BusyDelayProperty);
        set => SetValue(BusyDelayProperty, value);
    }

    //bindable property busyMessage
    public static readonly BindableProperty BusyMessageProperty =
        BindableProperty.Create(nameof(BusyMessage), typeof(string), typeof(OverlayView), string.Empty);
    public string BusyMessage
    {
        get => (string)GetValue(BusyMessageProperty);
        set => SetValue(BusyMessageProperty, value);
    }

    //model 
    private OverlayViewModel Model => BindingContainer?.BindingContext as OverlayViewModel;

    public OverlayView()
    {
        InitializeComponent();

        Model.PropertyChanged += OnModelPropertyChanged;
    }

    private void OnModelPropertyChanged(object sender, System.ComponentModel.PropertyChangedEventArgs e)
    {
        if (e.PropertyName == nameof(IsVisible))
        {
            //timeout triggered
            if (!Model.IsVisible)
            {
                IsVisible = false;
            }
        }
    }

    private void ConsumeInput(object sender, TappedEventArgs e)
    {
        // Do nothing, prevet controls under the overlay from receiving input
    }

    protected override void OnPropertyChanged([CallerMemberName] string propertyName = null)
    {
        base.OnPropertyChanged(propertyName);

        //synchronize fields with model
        if (propertyName == BusyTimeoutProperty.PropertyName)
        {
            Model.BusyTimeout = BusyTimeout;
        }
        else if (propertyName == BusyDelayProperty.PropertyName)
        {
            Model.BusyDelay = BusyDelay;
        }
        else if (propertyName == BusyMessageProperty.PropertyName)
        {
            Model.BusyMessage = BusyMessage;
        }
        else if (propertyName == IsVisibleProperty.PropertyName)
        {
            Model.IsVisible = IsVisible;
        }
    }
}