using LingoHammer.Common;

namespace LingoHammer.Services
{
    public interface IStateService
    {
        public void Set<T>(string key, T value);

        public T Get<T>(string key);
    }


    public class StateService : IStateService
    {
        private readonly QueueTaskExecutor SaveQueue = new();

        private readonly Dictionary<string, object> _state = new();

        public void Set<T>(string key, T value)
        {

            _state[key] = value;

            Store(key, value);
        }

        public T Get<T>(string key)
        {
            _state.TryGetValue(key, out var value);
            if (value is T tValue)
            {
                return tValue;
            }

            value = Load<T>(key);

            return value is null ? default : (T)value;
        }


        private void Store<T>(string key, T value)
        {
            SaveQueue.Execute(() => StoreInternal(key, value), TimeSpan.FromSeconds(10));
        }

        private static void StoreInternal<T>(string key, T value)
        {
            S.EncryptedStorage.Write("state", key, value);
        }

        private static T Load<T>(string key)
        {
            return S.EncryptedStorage.Read<T>("state", key);
        }

    }
}
