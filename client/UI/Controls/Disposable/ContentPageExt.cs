namespace LingoHammer.UI.Controls.Disposable;

public class ContentPageExt : Microsoft.Maui.Controls.ContentPage, IDisposable
{
    public virtual void Dispose()
    {

        (Content as IDisposable)?.Dispose();
        Content = null;

        (BindingContext as IDisposable)?.Dispose();
        BindingContext = null;

        UnapplyBindings();
    }
}