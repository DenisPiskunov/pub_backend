using System.Collections.Generic;
using System.IO;

namespace ServerDataConverters.Utils {
    public static class FileUtils {
        public static void SaveStringListToFile(List<string> content, string fileName) {
            if (File.Exists(fileName)) File.Delete(fileName);
            File.WriteAllLines(fileName, content);
        }
    }
}