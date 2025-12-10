using System.Collections.Generic;
using JetBrains.Annotations;
using Newtonsoft.Json;
using ServerDataConverters.Utils;

namespace ServerDataConverters.entities.FbAccounts {
    public class FbAccountsWrapped {
        [JsonProperty(PropertyName = "data")] public List<FbAccount> FbAccountsList;
    }

    public class FbAccount : AbstractEntity {
        [JsonProperty(PropertyName = "server_name")] [CanBeNull]
        public string ServerName;

        [JsonProperty(PropertyName = "application")] [CanBeNull]
        public string Application;

        [JsonProperty(PropertyName = "platform_info")] [CanBeNull]
        public string PlatformInfo;

        [JsonProperty(PropertyName = "privacy_policy")] [CanBeNull]
        public string PrivacyPolicy;

        [JsonProperty(PropertyName = "access_data")] [CanBeNull]
        public string AccessData;

        [JsonProperty(PropertyName = "email_data")] [CanBeNull]
        public string EmailData;

        [JsonProperty(PropertyName = "status")] [CanBeNull]
        public string Status;

        public override bool IsEmpty() {
            return ServerName.TrimVal().IsNull() &&
                   Application.TrimVal().IsNull() &&
                   PlatformInfo.TrimVal().IsNull() &&
                   PrivacyPolicy.TrimVal().IsNull() &&
                   AccessData.TrimVal().IsNull() &&
                   EmailData.TrimVal().IsNull() &&
                   Status.TrimVal().IsNull();
        }
    }
}