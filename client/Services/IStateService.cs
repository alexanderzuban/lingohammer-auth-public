using LingoHammer.Common;

namespace LingoHammer.Services;

public interface IStateService
{
    void Set<T>(string key, T value);

    T Get<T>(string key);


    void SetEncrypted<T>(string key, T value);

    T GetEncrypted<T>(string key);
}


public class StateService : IStateService
{
    private readonly QueueTaskExecutor SaveQueue = new();

    private readonly Dictionary<string, object> _state = new();

    public IEncryptedStorageService EncryptedStorage { get; }

    public IStorageService Storage { get; }

    public ILogService Log { get; }

    public StateService(IEncryptedStorageService encryptedStorage, IStorageService storage, ILogService log)
    {
        EncryptedStorage = encryptedStorage;
        Storage = storage;
        Log = log;
    }

    private void SetInternal<T>(string key, T value, bool encrypted = true)
    {
        _state[key] = value;
        Store(key, value, encrypted);
    }

    private T GetInternal<T>(string key, bool encrypted = true)
    {
        _state.TryGetValue(key, out var value);
        if (value is T tValue)
        {
            return tValue;
        }

        value = Load<T>(key, encrypted);

        return value is null ? default : (T)value;
    }


    private void Store<T>(string key, T value, bool encrypted = true)
    {
        SaveQueue.Execute(() => StoreInternal(key, value, encrypted), TimeSpan.FromSeconds(10));
    }

    private void StoreInternal<T>(string key, T value, bool encrypted = true)
    {
        Log.Debug($"StateService.Store {key}");

        if (encrypted)
        {
            EncryptedStorage.Write("state", key, value);
        }
        else
        {
            Storage.Write("state", key, value);
        }
    }

    private T Load<T>(string key, bool encrypted = true)
    {
        if (encrypted)
        {
            return EncryptedStorage.Read<T>("state", key);
        }

        return Storage.Read<T>("state", key);
    }

    public void Set<T>(string key, T value)
    {
        SetInternal(key, value, false);
    }

    public T Get<T>(string key)
    {
        return GetInternal<T>(key, false);
    }

    public void SetEncrypted<T>(string key, T value)
    {
        SetInternal(key, value, true);
    }

    public T GetEncrypted<T>(string key)
    {
        return GetInternal<T>(key, true);
    }
}
