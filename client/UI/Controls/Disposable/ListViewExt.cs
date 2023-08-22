namespace LingoHammer.UI.Controls.Disposable;

public class ListViewExt : Microsoft.Maui.Controls.ListView, IDisposable
{
    public virtual void Dispose()
    {
        ItemsSource = null;

        (BindingContext as IDisposable)?.Dispose();
        BindingContext = null;

        UnapplyBindings();
    }
}
