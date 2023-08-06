using CommunityToolkit.Mvvm.ComponentModel;

namespace LingoHammer.UI.Controls
{
    partial class OverlayViewModel : ObservableObject
    {
        [ObservableProperty]
        private bool isVisible;

        [ObservableProperty]
        private bool isBusy;

        [ObservableProperty]
        private int busyDelay = 1000;

        [ObservableProperty]
        private int busyTimeout = 10000;

        [ObservableProperty]
        private string busyMessage = string.Empty;

        private long busyTimestamp;

        public OverlayViewModel()
        {
        }

        partial void OnIsVisibleChanged(bool value)
        {
            var currentTimestamp = DateTimeOffset.Now.ToUnixTimeMilliseconds();
            busyTimestamp = currentTimestamp;
            IsBusy = false;
            if (value)
            {
                StartFadeTimer(currentTimestamp);
                StartTimeoutTime(currentTimestamp);
            };
        }

        private void StartFadeTimer(long currentTimestamp)
        {
            if (BusyDelay <= 0)
            {
                //no delay
                IsBusy = true;
                return;
            }

            MainThread.BeginInvokeOnMainThread(async () =>
            {
                await Task.Delay(BusyDelay);
                if (busyTimestamp == currentTimestamp)
                {
                    IsBusy = true;
                }
            });
        }

        private void StartTimeoutTime(long currentTimestamp)
        {
            if (BusyTimeout <= 0)
            {
                return;
            }
            MainThread.BeginInvokeOnMainThread(async () =>
            {
                await Task.Delay(BusyTimeout);
                if (busyTimestamp == currentTimestamp)
                {
                    IsBusy = false;
                    IsVisible = false;
                }
            });
        }
    }

}
