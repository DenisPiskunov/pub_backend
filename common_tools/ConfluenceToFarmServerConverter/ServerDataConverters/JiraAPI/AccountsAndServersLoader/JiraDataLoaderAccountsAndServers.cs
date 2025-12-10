using System;
using System.Collections.Generic;
using ServerDataConverters.Converters.AccountsAndServersConverter;
using ServerDataConverters.entities;
using ServerDataConverters.entities.AccountsAndServers;
using ServerDataConverters.Utils;

namespace ServerDataConverters.JiraAPI.AccountsAndServersLoader {
    public class JiraDataLoaderAccountsAndServers : JiraDataLoader{
        public override void ParseResponse() {
            var accountsAndServers = new List<AbstractEntity>();
            if (CellsCollection == null) return;

            for (var index = CellsCollection.Count - 1; index >= 0; index--) {
                if (CellsCollection[index].OuterHtml.Contains("colspan=\"15\"")) {
                    CellsCollection.Remove(CellsCollection[index]);
                }
            }
            
            for (var i = 0; i < CellsCollection.Count; i += Constants.AccountsAndServersFieldsCount) {

                var account = new AccountsAndServers {
                    ServerName = GetClearCellText(CellsCollection[i]),
                    HostingData = GetClearCellText(CellsCollection[i + 1]),
                    DateNextRenewal = GetClearCellText(CellsCollection[i + 2]),
                    ConnectionData = GetClearCellText(CellsCollection[i + 3]),
                    HostingEmail = GetClearCellText(CellsCollection[i + 4]),
                    AccountEmailData = GetClearCellText(CellsCollection[i + 5]),
                    AppGallery = GetClearCellText(CellsCollection[i + 6]),
                    Appstore = GetClearCellText(CellsCollection[i + 7]),
                    GooglePlay = GetClearCellText(CellsCollection[i + 8]),
                    DomainEmail = GetClearCellText(CellsCollection[i + 9]),
                    DomainEmailNextRenewal = GetClearCellText(CellsCollection[i + 10]),
                    SiteData = GetClearCellText(CellsCollection[i + 11]),
                    SiteDataNextRenewal = GetClearCellText(CellsCollection[i + 12]),
                    DomainData = GetClearCellText(CellsCollection[i + 13]),
                    DomainDataNextRenewal = GetClearCellText(CellsCollection[i + 14])
                };
                if (!account.IsEmpty()) accountsAndServers.Add(account);
            }

            JsonUtils.SerializeToFile(Constants.AccountsAndServersJSONFileName + DateTime.Now.ToString("yyyy-MM-dd_HH-mm") + ".json", accountsAndServers);
            new AccountsAndServersConverter().ConvertObjectsListToJson(accountsAndServers);
        }
    }
}