using Newtonsoft.Json;

namespace ServerDataConverters.Utils {
    public class CustomJsonSerializerSettings : JsonSerializerSettings {
        public CustomJsonSerializerSettings() {
            NullValueHandling = NullValueHandling.Ignore;
        } 
    }
}