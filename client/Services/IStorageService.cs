using Newtonsoft.Json;
using System.Text;

namespace LingoHammer.Services;

public interface IStorageService
{
    public void Write<T>(string path, string key, T value);

    public T Read<T>(string path, string key);

    public List<string> ListKeys(string path);

    public void Delete(string path, string key);

    public void WriteData(string path, string key, byte[] value);

    public byte[] ReadData(string path, string key);

    public Task WriteAsync<T>(string path, string key, T value);

    public Task<T> ReadAsync<T>(string path, string key);

    public Task WriteDataAsync(string path, string key, byte[] value);

    public Task<byte[]> ReadDataAsync(string path, string key);
}

public interface IEncryptedStorageService
{
    public void Write<T>(string path, string key, T value);

    public T Read<T>(string path, string key);

    public List<string> ListKeys(string path);

    public void Delete(string path, string key);

    public void WriteData(string path, string key, byte[] value);

    public byte[] ReadData(string path, string key);

    public Task WriteAsync<T>(string path, string key, T value);

    public Task<T> ReadAsync<T>(string path, string key);

    public Task WriteDataAsync(string path, string key, byte[] value);

    public Task<byte[]> ReadDataAsync(string path, string key);
}

public class EncryptedStorageService : StorageServiceBase, IEncryptedStorageService
{
    public EncryptedStorageService(ISettingsService settings) : base(true)
    {

        //initilize  random encryption secret
        if (string.IsNullOrEmpty(settings.Secret))
        {
            settings.Secret = Guid.NewGuid().ToString();
        }
    }
}

public class StorageService : StorageServiceBase, IStorageService
{

    public StorageService() : base(false)
    {
    }
}

public class StorageServiceBase
{
    protected bool IsEncrypted { get; }
    protected string BasePath { get; }

    public StorageServiceBase(bool encrypted)
    {
        if (encrypted)
        {
            BasePath = "secure";
        }
        else
        {
            BasePath = "plain";
        }
        IsEncrypted = encrypted;
    }

    public void WriteData(string path, string key, byte[] value)
    {
        //write to the applocation local file system
        string fileName = FilePath(path, key);

        if (File.Exists(fileName))
        {
            File.Delete(fileName);
        }

        value = EncryptData(value, S.Settings.Secret);

        //write the file
        File.WriteAllBytes(fileName, value);
    }



    public async Task WriteDataAsync(string path, string key, byte[] value)
    {
        string fileName = FilePath(path, key);

        if (File.Exists(fileName))
        {
            File.Delete(fileName);
        }
        value = await EncryptDataAsync(value, S.Settings.Secret);

        //write the file
        await File.WriteAllBytesAsync(fileName, value);
    }


    public void Write<T>(string path, string key, T value)
    {
        var data = Serialize(value);
        WriteData(path, key, data);
    }


    public async Task WriteAsync<T>(string path, string key, T value)
    {
        var data = await Task.Run(() => Serialize(value));
        await WriteDataAsync(path, key, data);
    }



    private string DirectoryPath(string path)
    {
        var filePath = Path.Combine(FileSystem.Current.CacheDirectory, "storage", BasePath, path);
        //create the directory if it does not exist
        if (!Directory.Exists(filePath))
        {
            Directory.CreateDirectory(filePath);
        }
        return filePath;
    }

    private string FilePath(string path, string key)
    {
        //write to the applocation local file system            
        return Path.Combine(DirectoryPath(path), key);
    }

    public T Read<T>(string path, string key)
    {
        return Deserialize<T>(ReadData(path, key));
    }

    public List<string> ListKeys(string path)
    {
        var files = Directory.GetFiles(DirectoryPath(path));
        return files.Select(Path.GetFileName).ToList();
    }

    public void Delete(string path, string key)
    {
        var filePath = FilePath(path, key);
        if (File.Exists(filePath))
        {
            File.Delete(filePath);
        }
    }


    public byte[] ReadData(string path, string key)
    {
        var filePath = FilePath(path, key);
        if (!File.Exists(filePath))
        {
            return null;
        }
        var result = File.ReadAllBytes(filePath);

        return DecryptData(result, S.Settings.Secret);
    }



    public async Task<T> ReadAsync<T>(string path, string key)
    {
        var data = await ReadDataAsync(path, key);

        return await Task.Run(() => Deserialize<T>(data));
    }

    public async Task<byte[]> ReadDataAsync(string path, string key)
    {
        var filePath = FilePath(path, key);
        if (!File.Exists(filePath))
        {
            return null;
        }
        var result = await File.ReadAllBytesAsync(filePath);
        return await DecryptDataAsync(result, S.Settings.Secret);
    }

    private byte[] EncryptData(byte[] data, string secret)
    {
        if (IsEncrypted)
        {
            return S.Cryptography.EncryptData(data, secret);
        }
        return data;
    }

    private async Task<byte[]> EncryptDataAsync(byte[] data, string secret)
    {
        if (IsEncrypted)
        {
            return await S.Cryptography.EncryptDataAsync(data, secret);
        }
        return data;
    }


    private byte[] DecryptData(byte[] data, string secret)
    {
        if (IsEncrypted)
        {
            return S.Cryptography.DecryptData(data, secret);
        }
        return data;
    }

    private async Task<byte[]> DecryptDataAsync(byte[] data, string secret)
    {
        if (IsEncrypted)
        {
            return await S.Cryptography.DecryptDataAsync(data, secret);
        }
        return data;
    }

    private static byte[] Serialize<T>(T value)
    {
        string json = JsonConvert.SerializeObject(value);
        return Encoding.UTF8.GetBytes(json);

        // using var buffer = new MemoryStream(4 * 1024);
        // using var writer = new HessianStreamWriter(buffer);
        // var output = new HessianOutputV2(writer);
        // output.WriteObject(value);
        // return buffer.ToArray();
    }

    private static T Deserialize<T>(byte[] data)
    {
        if (data == null) return default;

        var json = Encoding.UTF8.GetString(data);
        return JsonConvert.DeserializeObject<T>(json);

        //try
        // {
        //    using var buffer = new MemoryStream(data);
        //   using var reader = new HessianStreamReader(buffer);
        //   var input = new HessianInputV2(reader);
        //   return (T)input.ReadObject();
        // }
        //catch (Exception)
        // {
        //    return default;
        // }
    }

}
