using System.Security.Cryptography;
using System.Text;

namespace LingoHammer.Services
{
    public interface ICryptographyService


    {
        public Task<byte[]> EncryptDataAsync(byte[] data, string password);

        public byte[] EncryptData(byte[] data, string password);

        public Task<byte[]> DecryptDataAsync(byte[] data, string password);

        public byte[] DecryptData(byte[] data, string password);

    }

    public class CryptographyService : ICryptographyService
    {
        private readonly byte[] IV = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16 };

        public async Task<byte[]> EncryptDataAsync(byte[] data, string password)
        {
            if (data == null) return null;

            using Aes aes = Aes.Create();
            aes.Key = DeriveKeyFromPassword(password);
            aes.IV = IV;
            using MemoryStream output = new();
            using CryptoStream cryptoStream = new(output, aes.CreateEncryptor(), CryptoStreamMode.Write);
            await cryptoStream.WriteAsync(data);
            await cryptoStream.FlushFinalBlockAsync();
            return output.ToArray();
        }



        public byte[] EncryptData(byte[] data, string password)
        {
            if (data == null) return null;

            using Aes aes = Aes.Create();
            aes.Key = DeriveKeyFromPassword(password);
            aes.IV = IV;
            using MemoryStream output = new();
            using CryptoStream cryptoStream = new(output, aes.CreateEncryptor(), CryptoStreamMode.Write);
            cryptoStream.Write(data);
            cryptoStream.FlushFinalBlock();

            return output.ToArray();
        }

        public async Task<byte[]> DecryptDataAsync(byte[] data, string password)
        {
            if (data == null) return null;

            using Aes aes = Aes.Create();
            aes.Key = DeriveKeyFromPassword(password);
            aes.IV = IV;
            using MemoryStream input = new(data);
            using CryptoStream cryptoStream = new(input, aes.CreateDecryptor(), CryptoStreamMode.Read);
            using MemoryStream output = new();
            await cryptoStream.CopyToAsync(output);
            return output.ToArray();
        }

        public byte[] DecryptData(byte[] data, string password)
        {
            if (data == null) return null;

            using Aes aes = Aes.Create();
            aes.Key = DeriveKeyFromPassword(password);
            aes.IV = IV;
            using MemoryStream input = new(data);
            using CryptoStream cryptoStream = new(input, aes.CreateDecryptor(), CryptoStreamMode.Read);
            using MemoryStream output = new();
            cryptoStream.CopyTo(output);

            return output.ToArray();
        }

        private static byte[] DeriveKeyFromPassword(string password)
        {
            var emptySalt = Array.Empty<byte>();
            var iterations = 100;
            var desiredKeyLength = 16; // 16 bytes equal 128 bits.
            var hashMethod = HashAlgorithmName.SHA384;
            return Rfc2898DeriveBytes.Pbkdf2(Encoding.Unicode.GetBytes(password),
                                             emptySalt,
                                             iterations,
                                             hashMethod,
                                             desiredKeyLength);
        }
    }
}
