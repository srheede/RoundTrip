package za.co.rheeders.roundtrip;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNameBySizeSortingComparator implements Comparator<String> {

    @Override
    public int compare(String fileName, String t1) {
        Pattern pattern = Pattern.compile("(\\d)+");
        Matcher matcherFileName = pattern.matcher(fileName);
        Matcher matcherT1 = pattern.matcher(t1);

        if (matcherFileName.find())
        {
            if (matcherT1.find()) {
                return Integer.parseInt(matcherFileName.group()) - Integer.parseInt(matcherT1.group());
            }
            return -1;
        }
        return 1;
    }
}
