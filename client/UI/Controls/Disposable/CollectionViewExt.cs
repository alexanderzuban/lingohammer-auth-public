namespace LingoHammer.UI.Controls.Disposable;

public class CollectionViewExt : Microsoft.Maui.Controls.CollectionView, IDisposable
{
    public virtual void Dispose()
    {
        ItemsSource = null;

        (BindingContext as IDisposable)?.Dispose();
        BindingContext = null;

        UnapplyBindings();
    }
}
