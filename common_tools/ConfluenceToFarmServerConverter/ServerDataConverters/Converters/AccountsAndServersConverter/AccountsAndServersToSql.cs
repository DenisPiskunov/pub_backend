using System.Text;
using Microsoft.VisualBasic;
using ServerDataConverters.entities.AccountsAndServers;
using ServerDataConverters.Utils;

namespace ServerDataConverters.Converters.AccountsAndServersConverter {
    public class AccountsAndServersToSql : AbstractToSql {
        private readonly string _serverName;
        private readonly string _hostingData;
        private readonly string _dateNextRenewal;
        private readonly string _connectionData;
        private readonly string _hostingEmail;
        private readonly string _accountEmailData;
        private readonly string _appGallery;
        private readonly string _appstore;
        private readonly string _googlePlay;
        private readonly string _domainEmail;
        private readonly string _domainEmailNextRenewal;
        private readonly string _siteData;
        private readonly string _siteDataNextRenewal;
        private readonly string _domainData;
        private readonly string _domainDataNextRenewal;

        public AccountsAndServersToSql(AccountsAndServers accountsAndServers) {
            _serverName = accountsAndServers.ServerName;
            _hostingData = accountsAndServers.HostingData;
            _connectionData = accountsAndServers.ConnectionData;
            _hostingEmail = accountsAndServers.HostingEmail;
            _accountEmailData = accountsAndServers.AccountEmailData;
            _appGallery = accountsAndServers.AppGallery;
            _appstore = accountsAndServers.Appstore;
            _googlePlay = accountsAndServers.GooglePlay;
            _domainEmail = accountsAndServers.DomainEmail;
            _siteData = accountsAndServers.SiteData;
            _domainData = accountsAndServers.DomainData;
            _dateNextRenewal = accountsAndServers.DateNextRenewal;
            _domainEmailNextRenewal = accountsAndServers.DomainEmailNextRenewal;
            _siteDataNextRenewal = accountsAndServers.SiteDataNextRenewal;
            _domainDataNextRenewal = accountsAndServers.DomainDataNextRenewal;
        }

        public override string ToSql(bool lastRecord) {
            var singleQuery = new StringBuilder();

            singleQuery.Append(
                $"({(_serverName.IsNotNull() ? "'" + Strings.Replace(_serverName, "\n", " ") + "'" : "NULL")}, " +
                $"{(_hostingData.IsNotNull() ? "'" + _hostingData + "'" : "NULL")}, " +
                $"{(_dateNextRenewal.IsNotNull() ? "'" + _dateNextRenewal + "'" : "NULL")}, " +
                $"{(_connectionData.IsNotNull() ? "'" + _connectionData + "'" : "NULL")}, " +
                $"{(_hostingEmail.IsNotNull() ? "'" + _hostingEmail + "'" : "NULL")}, " +
                $"{(_accountEmailData.IsNotNull() ? "'" + _accountEmailData + "'" : "NULL")}, " +
                $"{(_appGallery.IsNotNull() ? "'" + _appGallery + "'" : "NULL")}, " +
                $"{(_appstore.IsNotNull() ? "'" + _appstore + "'" : "NULL")}, " +
                $"{(_googlePlay.IsNotNull() ? "'" + _googlePlay + "'" : "NULL")}, " +
                $"{(_domainEmail.IsNotNull() ? "'" + _domainEmail + "'" : "NULL")}, " +
                $"{(_domainEmailNextRenewal.IsNotNull() ? "'" + _domainEmailNextRenewal + "'" : "NULL")}, " +
                $"{(_siteData.IsNotNull() ? "'" + _siteData + "'" : "NULL")}, " +
                $"{(_siteDataNextRenewal.IsNotNull() ? "'" + _siteDataNextRenewal + "'" : "NULL")}, " +
                $"{(_domainData.IsNotNull() ? "'" + _domainData + "'" : "NULL")}, " +
                $"{(_domainDataNextRenewal.IsNotNull() ? "'" + _domainDataNextRenewal + "'" : "NULL")})");
            singleQuery.Append(!lastRecord ? "," : ";");
            return singleQuery.ToString();
        }
    }
}