using ServerDataConverters.JiraAPI;
using ServerDataConverters.JiraAPI.AccountsAndServersLoader;
using ServerDataConverters.JiraAPI.FbAccountsLoader;
using ServerDataConverters.Utils;

namespace ServerDataConverters {
    class ServerDataConverter {

        static void Main(string[] args) {
            JiraDataLoader.LoadJiraData(Constants.FbAccountsPageId);
            new JiraDataLoaderFbAccounts().ParseResponse();
            
            JiraDataLoader.LoadJiraData(Constants.AccountsAndServerPageId);
            new JiraDataLoaderAccountsAndServers().ParseResponse();
        }
    }
}