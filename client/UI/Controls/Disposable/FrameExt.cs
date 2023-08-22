namespace LingoHammer.UI.Controls.Disposable;

public class FrameExt : Microsoft.Maui.Controls.Frame, IDisposable
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

