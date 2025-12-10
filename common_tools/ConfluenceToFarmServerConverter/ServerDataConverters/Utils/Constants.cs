namespace ServerDataConverters.Utils {
    public class Constants {
        private const string currentPath = "../../../../Data";
        
        public static readonly string AccountsAndServersJSONFileName = $"{currentPath}/account_and_servers_";
        public static readonly string AccountsAndServersSQLFileName = $"{currentPath}/account_and_servers_";
        private const string AccountsAndServersTableName = "accounts_and_servers";
        public static string InsertIntoAccountsAndServersTable = $"INSERT INTO {AccountsAndServersTableName}\n" +
                                                                 "(server_name, hosting_data, date_next_renewal, connection_data, hosting_email, " +
                                                                 "account_email_data, app_gallery, appstore, google_play, domain_email, " +
                                                                 "domain_email_next_renewal, site_data, site_data_next_renewal, domain_data, " +
                                                                 "domain_data_next_renewal)\n" +
                                                                 "VALUES ";
        
        
                
        public static readonly string FbAccountsJSONFileName = $"{currentPath}/fb_accounts_";
        public static readonly string FbAccountsSQLFileName = $"{currentPath}/fb_accounts_";
        private const string FbAccountsTableName = "fb_accounts";
        public static string InsertIntoFbAccountsTable = $"INSERT INTO {FbAccountsTableName}\n" +
                                                                 "(server_name, application, platform_info, " +
                                                                 "privacy_policy, access_data, email_data, status)\n" +
                                                                 "VALUES ";
        
        public const string Commit = "COMMIT;";


        public const int FbAccountsFieldsCount = 7;
        public const int AccountsAndServersFieldsCount = 15;
        
        /// <summary>
        /// Jira Confluence
        /// </summary>
        public const string JiraUser = "dpiskunov@mint-mobile.ru";
        public const string JiraAPIToken = "1mSs64zdrH1upVUSl0y18229";
        public const string JiraBaseUrl = "https://mintdev.atlassian.net/wiki/rest/api/content/";
        public const string AccountsAndServerPageId = "790495233";
        public const string FbAccountsPageId = "1371537409";
        
    }
}