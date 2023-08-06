using LingoHammer.Auth;

namespace LingoHammer.Services
{
    public delegate void Callback(bool success, object result, string message, Exception error);

    public static class CallbackExtensions
    {

        public static void ExecuteAndNotifyOnException(this Callback val, Func<Task> asyncAction)
        {
            Task.Run(async () =>
            {
                try
                {
                    await asyncAction();
                }
                catch (ApiException<IsFlawed> ex)
                {
                    var response = ex.Result;
                    val.NotifyResult(false, response, response?.ErrorMessage, ex);
                }
                catch (Exception ex)
                {
                    val.NotifyError(ex);
                }
            });
        }

        public static void NotifyError(this Callback val, string message, Exception error)
        {
            val.NotifyResult(false, null, message, error);
        }

        public static void NotifyError(this Callback val, string message)
        {
            val.NotifyResult(false, null, message, null);
        }

        public static void NotifyError(this Callback val, Exception error)
        {
            val.NotifyResult(false, null, null, error);
        }

        public static void NotifySuccess(this Callback val)
        {
            val.NotifyResult(true, null, null, null);
        }

        public static void NotifySuccess(this Callback val, object result)
        {
            val.NotifyResult(true, result, null, null);
        }

        public static void NotifySuccess(this Callback val, object result, string message)
        {
            val.NotifyResult(true, result, message, null);
        }

        public static void NotifyResult(this Callback val, bool success, object result, string message, Exception ex)
        {
            MainThread.BeginInvokeOnMainThread(() =>
            {
                try
                {
                    val?.Invoke(success, result, message, ex);
                }
                catch (Exception ex) { S.Log.Error(ex); }
            });
        }
    }

}
