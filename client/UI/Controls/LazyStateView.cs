using CommunityToolkit.Maui.Layouts;
using CommunityToolkit.Maui.Views;
using System.Runtime.CompilerServices;

namespace LingoHammer.UI.Controls;

public class LazyStateView<TView> : LazyView<TView> where TView : View, new()
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



    protected override void OnPropertyChanged([CallerMemberName] string propertyName = null)
    {
        base.OnPropertyChanged(propertyName);

        if (propertyName == TriggerStateProperty.PropertyName)
        {
            //get attached StateView.StateKey value
            var stateKey = StateView.GetStateKey(this);
            if (stateKey == TriggerState)
            {
                MainThread.BeginInvokeOnMainThread(async () =>
                {
                    await LoadViewAsync();
                });
            }
        }
    }

}
