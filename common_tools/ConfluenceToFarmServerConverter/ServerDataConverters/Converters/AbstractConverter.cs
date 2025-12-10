using System.Collections.Generic;
using ServerDataConverters.entities;

namespace ServerDataConverters.Converters {
    public abstract class AbstractConverter {
        public abstract void LoadAndConvertJson();

        public abstract void ConvertObjectsListToJson(List<AbstractEntity> objectsList);
    }
}