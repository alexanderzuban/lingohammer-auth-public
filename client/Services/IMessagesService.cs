using CommunityToolkit.Maui.Alerts;
using CommunityToolkit.Maui.Core;

namespace LingoHammer.Services;

public interface IMessagesService
{
    Task ShortMessageAsync(string message);
}

public class MessagesService : IMessagesService
{
    public async Task ShortMessageAsync(string message)
    {
        try
        {
            var cancellationTokenSource = new CancellationTokenSource();

            var duration = ToastDuration.Short;
            double fontSize = 14;

            var toast = Toast.Make(message, duration, fontSize);

            await toast.Show(cancellationTokenSource.Token);
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex.Message);
        }
    }
}
