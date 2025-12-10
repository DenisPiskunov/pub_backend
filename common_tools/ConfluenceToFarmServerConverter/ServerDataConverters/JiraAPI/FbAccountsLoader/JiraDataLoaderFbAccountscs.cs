using System;
using System.Collections.Generic;
using ServerDataConverters.Converters.FbAccountsConverter;
using ServerDataConverters.entities;
using ServerDataConverters.entities.FbAccounts;
using ServerDataConverters.Utils;

namespace ServerDataConverters.JiraAPI.FbAccountsLoader {
    public class JiraDataLoaderFbAccounts : JiraDataLoader{
        public override void ParseResponse() {
            var fbAccounts = new List<AbstractEntity>();
            if (CellsCollection == null) return;
            
            for (var i = 0; i < CellsCollection.Count; i += Constants.FbAccountsFieldsCount) {
                var fbAccount = new FbAccount {
                    ServerName = GetClearCellText(CellsCollection[i]),
                    Application = GetClearCellText(CellsCollection[i + 1]),
                    PlatformInfo = GetClearCellText(CellsCollection[i + 2]),
                    PrivacyPolicy = GetClearCellText(CellsCollection[i + 3]),
                    AccessData = GetClearCellText(CellsCollection[i + 4]),
                    EmailData = GetClearCellText(CellsCollection[i + 5]),
                    Status = GetClearCellText(CellsCollection[i + 6])
                };
                if (!fbAccount.IsEmpty()) fbAccounts.Add(fbAccount);
            }

            JsonUtils.SerializeToFile(Constants.FbAccountsJSONFileName + DateTime.Now.ToString("yyyy-MM-dd_HH-mm") + ".json", fbAccounts);
            new FbAccountsConverter().ConvertObjectsListToJson(fbAccounts);
        }
    }
    
    
}