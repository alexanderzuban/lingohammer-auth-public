using CommunityToolkit.Maui.Layouts;
using CommunityToolkit.Maui.Views;
using System.Runtime.CompilerServices;

namespace LingoHammer.UI.Controls;

public class LazyStateView<TView> : LazyView<TView>, IDisposable where TView : View, new()
{


    //Trigger state, bindable string property to activate view loading
    public static readonly BindableProperty TriggerStateProperty = BindableProperty.Create(
               nameof(TriggerState), typeof(string), typeof(LazyStateView<TView>), default(string), BindingMode.TwoWay
           );
    public string TriggerState
    {
        get => (string)GetValue(TriggerStateProperty);
        set => SetValue(TriggerStateProperty, value);
    }

    public void Dispose()
    {
        (BindingContext as IDisposable)?.Dispose();
        BindingContext = null;

        UnapplyBindings();

        (Content as IDisposable)?.Dispose();
        Content = null;
    }

    protected override void OnPropertyChanged([CallerMemberName] string propertyName = null)
    {
        base.OnPropertyChanged(propertyName);

        if (propertyName == TriggerStateProperty.PropertyName)
        {
            //get attached StateView.StateKey value
            var stateKey = StateView.GetStateKey(this);
            if (stateKey == TriggerState && !HasLazyViewLoaded)
            {
                MainThread.BeginInvokeOnMainThread(async () =>
                {
                    await LoadViewAsync();
                });
            }
        }
    }

}
