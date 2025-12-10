using System;
using System.Collections.Generic;
using System.Linq;

namespace ServerDataConverters.Utils {
    public static class StringExtensions {
        public static bool IsNull(this string value) {
            return string.IsNullOrEmpty(value);
        }
        
        public static string TrimVal(this string value) {
            return value.IsNull() ? string.Empty : value.Trim();
        }
        
        public static bool IsNotNull(this string value) {
            return !IsNull(value);
        }
        
        public static bool StartWithIgnoreCase(this string value, string compare) {
            return value.StartsWith(compare, StringComparison.OrdinalIgnoreCase);
        }
        
        public static bool EndWithIgnoreCase(this string value, string compare) {
            return value.EndsWith(compare, StringComparison.OrdinalIgnoreCase);
        }
        
        public static string List2Str<T>(this IList<T> list, string separator = ",") {
            if (list == null || list.Count <= 0) return string.Empty;
            var result = list.Select(s => s.ToString()).ToList();
            return string.Join(separator, result.ToArray());
        }
   
    }
}