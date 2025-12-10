using Newtonsoft.Json;

namespace ServerDataConverters.entities.Confluence {
    public class ConfluenceResponse {
        [JsonProperty(PropertyName = "body")]
        public View CBody;              
    }

    public class View {
        [JsonProperty(PropertyName = "view")]
        public Value CView;    
    }

    public class Value {
        [JsonProperty(PropertyName = "value")]
        public string CValue;    
    }
}