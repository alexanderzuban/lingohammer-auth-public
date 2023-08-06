namespace LingoHammer.Services
{
    public interface ITimeService
    {
        DateTimeOffset Now { get; }
    }

    public class TimeService : ITimeService
    {
        public DateTimeOffset Now => DateTimeOffset.Now;
    }
}
