package com.sp.utils.dump;

import org.apache.commons.cli.Options;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.nutch.parse.ParseText;
import org.apache.nutch.util.NutchConfiguration;

import java.io.IOException;

public class ContentReader {
    public static void main(String[] args) throws IOException, IOException {
        Configuration conf = NutchConfiguration.create();
        Options opts = new Options();
        GenericOptionsParser parser = new GenericOptionsParser(conf, opts, args);
        String[] remainingArgs = parser.getRemainingArgs();
        FileSystem fs = FileSystem.get(conf);
        //String segment = remainingArgs[0];
        String segment = "/Users/pankajmishra/source/software/apache-nutch-1.12/YahooCrawl/segments/20160817121340";
        Path file = new Path(segment, ParseText.DIR_NAME + "/part-00000/data");
        SequenceFile.Reader reader = new SequenceFile.Reader(fs, file, conf);
        Text key = new Text();
        ParseText content = new ParseText();
        // Loop through sequence files
        int count = 0;
        while (reader.next(key, content)) {
            try {
                System.out.println(key + "--" + (++count));
                System.out.println("Content ==== " + content.getText());
            } catch (Exception e) {
            }
        }
    }
}