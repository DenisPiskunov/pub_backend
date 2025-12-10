namespace ServerDataConverters.Converters {
    public abstract class AbstractToSql {
        public abstract string ToSql(bool lastRecord);
    }
}