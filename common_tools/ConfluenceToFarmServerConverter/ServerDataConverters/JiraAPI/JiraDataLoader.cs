using System;
using System.Text.RegularExpressions;
using Newtonsoft.Json;
using RestSharp;
using RestSharp.Authenticators;
using ServerDataConverters.entities.Confluence;
using ServerDataConverters.Utils;
using HtmlAgilityPack;
using RestSharp.Extensions;

namespace ServerDataConverters.JiraAPI {
    public abstract class JiraDataLoader {
        protected readonly CustomJsonSerializerSettings JsonSettings = new CustomJsonSerializerSettings();
        protected static HtmlNodeCollection CellsCollection;
        public abstract void ParseResponse();

        public static void LoadJiraData(string pageId) {
            var client =
                new RestClient(
                    new Uri($"{Constants.JiraBaseUrl}{pageId}")) {
                    Authenticator = new HttpBasicAuthenticator(Constants.JiraUser, Constants.JiraAPIToken)
                };

            var response = client.Execute(new RestRequest(Method.GET).AddParameter("expand", "body.view"));
            var r = JsonConvert.DeserializeObject<ConfluenceResponse>(response.Content);

            if (r != null) {
                var htmlDoc = new HtmlDocument();
                htmlDoc.LoadHtml(r.CBody.CView.CValue);
                CellsCollection = htmlDoc.DocumentNode.SelectNodes("//tr/td");
            } else {
                CellsCollection = null;
            }
        }

        protected static string GetClearCellText(HtmlNode tdNode) {
            if (!tdNode.InnerText.IsNotNull()) return string.Empty;
            var cellText = tdNode.InnerHtml;
            cellText = Regex.Replace(cellText, @"<br>|</p>|</div>| ", "\n");
            var tmpDoc = new HtmlDocument();
            tmpDoc.LoadHtml(cellText);
            var res = tmpDoc.DocumentNode.InnerText;
            return Regex.Replace(res, @"(^\n){1,}", string.Empty, RegexOptions.Multiline).HtmlDecode().Trim('\n');
        }
    }
}