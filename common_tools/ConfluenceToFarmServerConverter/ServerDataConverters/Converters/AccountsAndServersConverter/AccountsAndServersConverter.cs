using System;
using System.Collections.Generic;
using ServerDataConverters.entities;
using ServerDataConverters.entities.AccountsAndServers;
using ServerDataConverters.Utils;

namespace ServerDataConverters.Converters.AccountsAndServersConverter {
    public class AccountsAndServersConverter : AbstractConverter {
        public override void LoadAndConvertJson() {
            var sqlQueries = new List<string>();
            var i = 1;
            var accountsAndServersList =
                JsonUtils.DeserializeFromFile<AccountsAndServersWrapped>(Constants.AccountsAndServersJSONFileName)
                    .AccountsAndServersList ?? new List<AccountsAndServers>();
            Console.WriteLine($"===>>> _accountsAndServersList.Count={accountsAndServersList.Count}");
            for (var index = accountsAndServersList.Count - 1; index >= 0; index--) {
                if (accountsAndServersList[index].IsEmpty()) {
                    accountsAndServersList.Remove(accountsAndServersList[index]);
                }
            }

            sqlQueries.Add(Constants.InsertIntoAccountsAndServersTable);
            foreach (var accountsAndServers in accountsAndServersList) {
                sqlQueries.Add(new AccountsAndServersToSql(accountsAndServers).ToSql(i == accountsAndServersList.Count));
                i++;
            }

            sqlQueries.Add(Constants.Commit);
            FileUtils.SaveStringListToFile(sqlQueries, Constants.AccountsAndServersSQLFileName + DateTime.Now.ToString("yyyy-MM-dd_HH-mm") + ".sql");
        }

        public override void ConvertObjectsListToJson(List<AbstractEntity> accountsList) {
            var sqlQueries = new List<string>();
            var i = 1;

            sqlQueries.Add(Constants.InsertIntoAccountsAndServersTable);
            foreach (var account in accountsList) {
                sqlQueries.Add(new AccountsAndServersToSql(account as AccountsAndServers).ToSql(i == accountsList.Count));
                i++;
            }

            sqlQueries.Add(Constants.Commit);
            FileUtils.SaveStringListToFile(sqlQueries, Constants.AccountsAndServersSQLFileName + DateTime.Now.ToString("yyyy-MM-dd_HH-mm") + ".sql");  
        }
    }
}