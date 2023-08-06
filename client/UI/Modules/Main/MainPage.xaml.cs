namespace LingoHammer.UI.Modules.Main;

public partial class MainPage
{
    int count = 0;

    public MainPage()
    {
        InitializeComponent();
    }

    private void OnCounterClicked(object sender, EventArgs e)
    {
        count++;

    }

    private void OnLogout(object sender, EventArgs e)
    {
        (Application.Current as App).OnUserLoggedOut();
    }
}

