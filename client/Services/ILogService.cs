namespace LingoHammer.Services;

public interface ILogService
{
    void Log(string message);

    void Error(string message);

    void Error(string message, Exception exception);

    void Error(Exception exception);

    void Debug(string message);
}

public class LogService : ILogService
{

    public void Debug(string message)
    {
        Console.WriteLine(message);
    }

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
