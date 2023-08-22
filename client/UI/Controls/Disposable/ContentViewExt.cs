namespace LingoHammer.UI.Controls.Disposable;

public class ContentViewExt : Microsoft.Maui.Controls.ContentView, IDisposable
{
    public virtual void Dispose()
    {
        (BindingContext as IDisposable)?.Dispose();
        BindingContext = null;

        UnapplyBindings();

        (Content as IDisposable)?.Dispose();
        Content = null;
    }
}
