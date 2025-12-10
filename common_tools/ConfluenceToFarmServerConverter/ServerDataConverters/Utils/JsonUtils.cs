using System;
using System.Collections.Generic;
using System.IO;
using System.Text.RegularExpressions;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace ServerDataConverters.Utils {
    public class JsonUtils {
        public static string GetString(JObject jObj, string tag) {
            return jObj[tag] != null ? (string)jObj[tag] : null;
        }

        public static string GetBonusSum(JObject jObj, string tag) {
            string result;
            try {
                result = GetString(jObj, tag);
            }
            catch (ArgumentException) {
                result = Convert.ToString((int)GetInt(jObj, tag));
            }

            return result;
        }

        public static string GetString(JObject jObj, string tag, string defaultValue) {
            return jObj[tag] != null ? (string)jObj[tag] : defaultValue;
        }

        public static bool GetBool(JObject jObj, string tag) {
            return jObj[tag] != null && (bool)jObj[tag];
        }

        public static bool GetBool(JObject jObj, string tag, bool defaultValue) {
            return jObj[tag] != null ? (bool)jObj[tag] : defaultValue;
        }

        public static Int64 GetInt(JObject jObj, string tag) {
            long result;
            try {
                result = jObj[tag] != null ? (Int64)jObj[tag] : 0;
            }
            catch (ArgumentException) {
                result = 0;
            }

            return result;
        }

        public static Int64 GetInt(JObject jObj, string tag, int defaultValue) {
            return jObj[tag] != null ? (Int64)jObj[tag] : defaultValue;
        }

        public static float GetFloat(JObject jObj, string tag) {
            return jObj[tag] != null ? (float)jObj[tag] : 0f;
        }

        public static long GetLong(JObject jObj, string tag) {
            long result;
            try {
                result = jObj[tag] != null ? (long)jObj[tag] : 0L;
            }
            catch (InvalidCastException) {
                result = Convert.ToInt64(GetInt(jObj, tag));
            }
            catch (ArgumentException) {
                return Convert.ToInt64(GetString(jObj, tag));
            }

            return result;
        }

        public static double GetDouble(JObject jObj, string tag) {
            double result;
            try {
                result = jObj[tag] != null ? (double)jObj[tag] : 0.0;
            }
            catch (InvalidCastException) {
                result = Convert.ToDouble(GetInt(jObj, tag));
            }
            catch (ArgumentException) {
                return Convert.ToDouble(GetString(jObj, tag));
            }

            return result;
        }

        public static double GetDouble(JObject jObj, string tag, double defaultValue) {
            double result;
            try {
                result = jObj[tag] != null ? (double)jObj[tag] : defaultValue;
            }
            catch (InvalidCastException) {
                result = Convert.ToDouble(GetInt(jObj, tag));
            }
            catch (ArgumentException) {
                var d = GetString(jObj, tag);
                double n;
                result = double.TryParse(d, out n) ? n : defaultValue;
            }

            return result;
        }

        public static double GetStatusDouble(JObject jObj, string tag) {
            var result = 0.0;
            if (jObj[tag] != null) {
                var o = Regex.Replace(GetString(jObj, tag), "(\\.0{1,})", string.Empty);
                try {
                    result = Convert.ToDouble(o);
                }
                catch (InvalidCastException) {
                    result = Convert.ToDouble(Convert.ToInt64(o));
                }
            }

            return result;
        }

        public static JObject GetJObject(JObject jObj, string tag) {
            return jObj[tag] is JObject ? (JObject)jObj[tag] : null;
        }

        public static JArray GetJArray(JObject jObj, string tag) {
            return jObj[tag] is JArray ? (JArray)jObj[tag] : null;
        }

        public static List<T> JarrayToList<T>(JObject jObj, string tag) {
            var result = new List<T>();
            var array = GetJArray(jObj, tag);
            if (array != null) {
                foreach (JToken token in array) {
                    var item = (T)Convert.ChangeType(token.ToString(), typeof(T));
                    result.Add(item);
                }
            }

            return result;
        }

        public static T DeserializeFromFile<T>(string filePath) {
            var textAsset = File.ReadAllText(filePath);
            return JsonConvert.DeserializeObject<T>(textAsset);
        }

        public static void SerializeToFile(string filePath, object value) {
            var serializer = new JsonSerializer { NullValueHandling = NullValueHandling.Include };
            using (var sw = new StreamWriter(File.Create(filePath)))
            using (var writer = new JsonTextWriter(sw)) {
                serializer.Serialize(writer, value);
            }
        }
    }
}