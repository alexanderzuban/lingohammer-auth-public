namespace LingoHammer.UI.Controls.Disposable;

public class GridExt : Microsoft.Maui.Controls.Grid, IDisposable
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
