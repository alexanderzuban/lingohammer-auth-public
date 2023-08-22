namespace LingoHammer.UI.Controls.Disposable;

public class NavigationPageExt : Microsoft.Maui.Controls.NavigationPage, IDisposable
{
    public NavigationPageExt(Page page) : base(page)
    {
        Popped += OnPagePopped;

    }

    public virtual void Dispose()
    {
        (BindingContext as IDisposable)?.Dispose();
        BindingContext = null;

        UnapplyBindings();

        (RootPage as IDisposable)?.Dispose();
    }

    private void OnPagePopped(object sender, NavigationEventArgs e)
    {
        (e.Page as IDisposable)?.Dispose();
    }
}
