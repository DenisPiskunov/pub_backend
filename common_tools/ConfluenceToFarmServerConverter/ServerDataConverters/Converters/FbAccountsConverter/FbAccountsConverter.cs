using System;
using System.Collections.Generic;
using ServerDataConverters.entities;
using ServerDataConverters.entities.FbAccounts;
using ServerDataConverters.Utils;

namespace ServerDataConverters.Converters.FbAccountsConverter {
    public class FbAccountsConverter : AbstractConverter{
        public override void LoadAndConvertJson() {
            var sqlQueries = new List<string>();
            var i = 1;
            var fbAccountsList =
                JsonUtils.DeserializeFromFile<FbAccountsWrapped>(Constants.FbAccountsJSONFileName)
                    .FbAccountsList ?? new List<FbAccount>();
            Console.WriteLine($"===>>> _accountsAndServersList.Count={fbAccountsList.Count}");
            for (var index = fbAccountsList.Count - 1; index >= 0; index--) {
                if (fbAccountsList[index].IsEmpty()) {
                    fbAccountsList.Remove(fbAccountsList[index]);
                }
            }

            sqlQueries.Add(Constants.InsertIntoFbAccountsTable);
            foreach (var fbAccount in fbAccountsList) {
                sqlQueries.Add(new FbAccountsToSql(fbAccount).ToSql(i == fbAccountsList.Count));
                i++;
            }

            sqlQueries.Add(Constants.Commit);
            FileUtils.SaveStringListToFile(sqlQueries, Constants.FbAccountsSQLFileName + DateTime.Now.ToString("yyyy-MM-dd_HH-mm") + ".sql");
        }

        public override void ConvertObjectsListToJson(List<AbstractEntity> accountsList) {
            var sqlQueries = new List<string>();
            var i = 1;

            sqlQueries.Add(Constants.InsertIntoFbAccountsTable);
            foreach (var fbAccount in accountsList) {
                sqlQueries.Add(new FbAccountsToSql(fbAccount as FbAccount).ToSql(i == accountsList.Count));
                i++;
            }

            sqlQueries.Add(Constants.Commit);
            FileUtils.SaveStringListToFile(sqlQueries, Constants.FbAccountsSQLFileName + DateTime.Now.ToString("yyyy-MM-dd_HH-mm") + ".sql");  
        }
    }
}