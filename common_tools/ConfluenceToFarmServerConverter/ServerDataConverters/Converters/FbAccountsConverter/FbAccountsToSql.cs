using System.Text;
using Microsoft.VisualBasic;
using ServerDataConverters.entities.FbAccounts;
using ServerDataConverters.Utils;

namespace ServerDataConverters.Converters.FbAccountsConverter{
    public class FbAccountsToSql : AbstractToSql {
        private string _serverName;
        private string _application;
        private string _platformInfo;
        private string _privacyPolicy;
        private string _accessData;
        private string _emailData;
        private string _status;


        public FbAccountsToSql(FbAccount fbAccount) { 
            _serverName = fbAccount.ServerName;
            _application = fbAccount.Application;
            _platformInfo = fbAccount.PlatformInfo;
            _privacyPolicy = fbAccount.PrivacyPolicy;
            _accessData = fbAccount.AccessData;
            _emailData = fbAccount.EmailData;   
            _status = fbAccount.Status;   
        }
        
        public override string ToSql(bool lastRecord) {
        var singleQuery = new StringBuilder();
            singleQuery.Append(
                $"({(_serverName.IsNotNull() ? "'" + Strings.Replace(_serverName, "'", "''") + "'" : "NULL")}, " +
                $"{(_application.IsNotNull() ? "'" + Strings.Replace(_application, "'", "''") + "'" : "NULL")}, " +
                $"{(_platformInfo.IsNotNull() ? "'" + Strings.Replace(_platformInfo, "'", "''") + "'" : "NULL")}, " +
                $"{(_privacyPolicy.IsNotNull() ? "'" + Strings.Replace(_privacyPolicy, "'", "''") + "'" : "NULL")}, " +
                $"{(_accessData.IsNotNull() ? "'" + Strings.Replace(_accessData, "'", "''") + "'" : "NULL")}, " +
                $"{(_emailData.IsNotNull() ? "'" + Strings.Replace(_emailData, "'", "''") + "'" : "NULL")}, " +
                $"{(_status.IsNotNull() ? "'" + Strings.Replace(_status, "'", "''") + "'" : "NULL")})");
            singleQuery.Append(!lastRecord ? "," : ";");
            return singleQuery.ToString();    
        }
    }
}