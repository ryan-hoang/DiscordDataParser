package discordparser.DataPackageParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class DiscordDataParser
{
    public static void main(String[] args) throws IOException {
        String path = "";
        Path p = null;

        ArrayList<Path> data = new ArrayList<Path>();

        try
        {
            path = args[0];
        }
        catch(IndexOutOfBoundsException e)
        {
            System.err.println("You must specify a path to the directory containing your discord data package.");
            System.exit(0);
        }

        try
        {
            p = Paths.get(path);
        }
        catch(InvalidPathException e)
        {
            System.err.println("That is not a valid file path.");
            System.exit(0);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(p))
        {
            for (Path entry: stream)
            {
                if(entry.toFile().isDirectory())
                {
                    Path temp;
                    try { temp = entry.resolve("messages.csv"); }
                    catch(InvalidPathException e) { continue; }
                    data.add(temp);
                }
            }
        }

    }
}
