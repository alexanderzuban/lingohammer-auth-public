namespace LingoHammer.Common
{
    public class Bag<TItem>
    {
        public TItem Item { get; }

        public Bag() { }

        public Bag(TItem item)
        {
            Item = item;
        }
    }
}
