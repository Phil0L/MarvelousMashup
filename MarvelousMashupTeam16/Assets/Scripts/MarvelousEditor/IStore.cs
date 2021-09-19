namespace MarvelousEditor
{
    public interface IStore
    {
        void LoadJson(string json);

        string ToJson();

        bool Savable();
    }
}
