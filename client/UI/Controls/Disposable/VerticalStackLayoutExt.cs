namespace LingoHammer.UI.Controls.Disposable;

public class VerticalStackLayoutExt : Microsoft.Maui.Controls.VerticalStackLayout, IDisposable
{
    public virtual void Dispose()
    {
        (BindingContext as IDisposable)?.Dispose();
        BindingContext = null;

        UnapplyBindings();

        Children.OfType<IDisposable>()
           .ToList().ForEach(x => x.Dispose());
        Children.Clear();
    }
}
