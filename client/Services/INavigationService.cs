using CommunityToolkit.Maui.Views;
using LingoHammer.UI.Controls.Disposable;
using LingoHammer.UI.Modules.Main;

namespace LingoHammer.Services;

public interface INavigationService
{
    public Task ShowPageAsync(Page page, bool animated = false);

    public Task ShowPageAsync(Func<Page> factory, bool animated = false);

    public Task ShowModalPageAsync(Page page, bool animated = false);

    public Task ShowModalPageAsync(Func<Page> factory, bool animated = false);

    public Task<object> ShowPopupAsync(Popup page);

    public Task<object> ShowPopupAsync(Func<Popup> factory);

    public Task HidePageAsync();

}


public class NavigationService : INavigationService
{
    private Page CurrentPage
    {
        get
        {
            var currentPage = Application.Current.MainPage;

            //page is navigation page
            var stack = currentPage.Navigation.NavigationStack;
            if (stack.Count > 0)
            {
                return stack[stack.Count - 1];
            }

            //modal stack was activated
            stack = currentPage.Navigation.ModalStack;
            if (stack.Count > 0)
            {
                return stack[stack.Count - 1];
            }

            if (currentPage is MainPage main)
            {
                currentPage = main;
                stack = currentPage.Navigation.NavigationStack;
                if (stack.Count > 0)
                {
                    return stack[stack.Count - 1];
                }
            }
            return currentPage;
        }
    }

    public async Task HidePageAsync()
    {
        var mainPageNavigation = Application.Current.MainPage.Navigation;
        Page removed;
        if (mainPageNavigation.ModalStack.Count > 0)
        {

            removed = await mainPageNavigation.PopModalAsync();
        }
        else
        {
            removed = await mainPageNavigation.PopAsync();
        }

        (removed as IDisposable)?.Dispose();//dispose of the page 
    }


    public async Task ShowModalPageAsync(Page page, bool animated = false)
    {
        var mainPageNavigation = Application.Current.MainPage.Navigation;
        if (mainPageNavigation.ModalStack.Count > 0)
        {
            await mainPageNavigation.PushModalAsync(page, animated);
        }
        else
        {
            await mainPageNavigation.PushModalAsync(new NavigationPageExt(page), animated);
        }
    }

    public async Task ShowModalPageAsync(Func<Page> factory, bool animated = false)
    {
        await ShowModalPageAsync(factory(), animated);
    }

    public async Task ShowPageAsync(Page page, bool animated = false)
    {
        await CurrentPage?.Navigation.PushAsync(page, animated);
    }

    public async Task ShowPageAsync(Func<Page> factory, bool animated = false)
    {
        await CurrentPage?.Navigation.PushAsync(factory(), animated);
    }

    public async Task<object> ShowPopupAsync(Popup page)
    {
        return await CurrentPage.ShowPopupAsync(page);
    }

    public async Task<object> ShowPopupAsync(Func<Popup> factory)
    {
        return await CurrentPage.ShowPopupAsync(factory());
    }
}
