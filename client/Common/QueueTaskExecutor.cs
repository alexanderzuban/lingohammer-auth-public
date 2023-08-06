using LingoHammer.Services;

namespace LingoHammer.Common
{
    public class QueueTaskExecutor
    {
        private readonly object _lock = new();
        private readonly Queue<TaskDefinition> _queue = new();
        private bool _isRunning;

        public void Execute(Action action, TimeSpan? timeout = null)
        {
            lock (_lock)
            {
                _queue.Enqueue(new TaskDefinition()
                {
                    Action = action,
                    Timeout = timeout
                });


                if (!_isRunning)
                {
                    _isRunning = true;
                    Task.Run(ExecuteNext);
                }
            }
        }

        private void ExecuteNext()
        {
            TaskDefinition taskDefinition;
            lock (_lock)
            {
                if (_queue.Count == 0)
                {
                    _isRunning = false;
                    return;
                }

                taskDefinition = _queue.Dequeue();
            }

            try
            {
                if (taskDefinition.Timeout.HasValue)
                {
                    var task = Task.Run(taskDefinition.Action);
                    if (!task.Wait(taskDefinition.Timeout.Value))
                    {
                        S.Log.Error("Task timeout");
                    }
                }
                else
                {
                    taskDefinition.Action();
                }

            }
            catch (Exception e)
            {
                S.Log.Error(e);
            }

            Task.Run(ExecuteNext);
        }


        private class TaskDefinition
        {
            public Action Action { get; set; }

            public TimeSpan? Timeout { get; set; }
        }
    }
}
