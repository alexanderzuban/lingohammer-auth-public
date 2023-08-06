namespace LingoHammer.Services
{
    public interface INavigationService
    {
        public Task ShowPageAsync(Page page, bool animated = false);

        public Task ShowPageAsync(Func<Page> factory, bool animated = false);
    }


    public class NavigationService : INavigationService
    {
        public async Task ShowPageAsync(Page page, bool animated = false)
        {
            await Application.Current.MainPage?.Navigation.PushAsync(page, animated);
        }

        public async Task ShowPageAsync(Func<Page> factory, bool animated = false)
        {
            await Application.Current.MainPage?.Navigation.PushAsync(factory(), animated);
        }
    }

}
