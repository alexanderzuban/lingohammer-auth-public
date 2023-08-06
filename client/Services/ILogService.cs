namespace LingoHammer.Services
{
    public interface ILogService
    {
        public void Log(string message);
        public void Error(string message);
        public void Error(string message, Exception exception);
        public void Error(Exception exception);
    }

    public class LogService : ILogService
    {
        public void Log(string message)
        {
            Console.WriteLine(message);
        }

        public void Error(string message)
        {
            Console.WriteLine(message);
        }

        public void Error(string message, Exception exception)
        {
            Console.WriteLine(message);
            Console.WriteLine(exception.Message);
        }

        public void Error(Exception exception)
        {
            Console.WriteLine(exception.Message);
        }
    }
}
