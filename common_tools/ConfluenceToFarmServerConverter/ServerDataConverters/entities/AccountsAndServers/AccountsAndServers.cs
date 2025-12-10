using System.Collections.Generic;
using JetBrains.Annotations;
using ServerDataConverters.Utils;
using Newtonsoft.Json;

namespace ServerDataConverters.entities.AccountsAndServers {
    
    public class AccountsAndServersWrapped {
        [JsonProperty(PropertyName = "data")]
        public List<AccountsAndServers> AccountsAndServersList;
    }
    public class AccountsAndServers : AbstractEntity {
        [JsonProperty(PropertyName = "server_name")] [CanBeNull]
        public string ServerName;

        [JsonProperty(PropertyName = "hosting_data")] [CanBeNull]
        public string HostingData;
        
        [JsonProperty(PropertyName = "date_next_renewal")] [CanBeNull]
        public string DateNextRenewal;

        [JsonProperty(PropertyName = "connection_data")] [CanBeNull]
        public string ConnectionData;
        
        [JsonProperty(PropertyName = "hosting_email")] [CanBeNull]
        public string HostingEmail;   
        
        [JsonProperty(PropertyName = "account_email_data")] [CanBeNull]
        public string AccountEmailData;
        
        [JsonProperty(PropertyName = "app_gallery")] [CanBeNull]
        public string AppGallery;
        
        [JsonProperty(PropertyName = "appstore")] [CanBeNull]
        public string Appstore;
        
        [JsonProperty(PropertyName = "google_play")] [CanBeNull]
        public string GooglePlay;

        [JsonProperty(PropertyName = "domain_email")] [CanBeNull]
        public string DomainEmail;
        
        [JsonProperty(PropertyName = "domain_email_next_renewal")] [CanBeNull]
        public string DomainEmailNextRenewal;

        [JsonProperty(PropertyName = "site_data")] [CanBeNull]
        public string SiteData;

        [JsonProperty(PropertyName = "site_data_next_renewal")] [CanBeNull]
        public string SiteDataNextRenewal;

        [JsonProperty(PropertyName = "domain_data")] [CanBeNull]
        public string DomainData;

        [JsonProperty(PropertyName = "domain_data_next_renewal")] [CanBeNull]
        public string DomainDataNextRenewal;

        public override bool IsEmpty() {
            return ServerName.TrimVal().IsNull() &&
                   HostingData.TrimVal().IsNull() &&
                   DateNextRenewal.TrimVal().IsNull() &&
                   ConnectionData.TrimVal().IsNull() &&
                   HostingEmail.TrimVal().IsNull() &&
                   AccountEmailData.TrimVal().IsNull() &&
                   AppGallery.TrimVal().IsNull() &&
                   Appstore.TrimVal().IsNull() &&
                   GooglePlay.TrimVal().IsNull() &&
                   DomainEmail.TrimVal().IsNull() &&
                   DomainEmailNextRenewal.TrimVal().IsNull() &&
                   SiteData.TrimVal().IsNull() &&
                   SiteDataNextRenewal.TrimVal().IsNull() &&
                   DomainData.TrimVal().IsNull() &&
                   DomainDataNextRenewal.TrimVal().IsNull();
        }
    }
}