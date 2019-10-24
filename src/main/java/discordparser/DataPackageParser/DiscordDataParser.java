package discordparser.DataPackageParser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DiscordDataParser
{
    public static void main(String[] args)
    {
        String path = "";
        Path p = null;
        ArrayList<Path> data;

        try //Read in path to data directory
        {
            path = args[0];
        }
        catch(IndexOutOfBoundsException e)
        {
            System.err.println("You must specify a path to the directory containing your discord data package.");
            System.exit(0);
        }

        try //Ensures valid path
        {
            p = Paths.get(path);
        }
        catch(InvalidPathException e)
        {
            System.err.println("That is not a valid file path.");
            System.exit(0);
        }

        data = DiscordDataParser.getPathsInDirectory(p);
        List<String> results = parse(data);

        System.out.println("The results have been placed in the following files:");
        for(String r : results)
        {
            System.out.println(r);
        }



    }


    private static List<String> parse(Iterable<Path> messages)
    {
        String attachments = "Attachments.txt";
        StringBuilder sb = new StringBuilder();
        for(Path p : messages)
        {
            String contents = "";
            Iterable<CSVRecord> temp = DiscordDataParser.extractRecords(p);
            if(temp == null)
            {
                continue;
            }
            for(CSVRecord record: temp)
            {
                contents = record.get(3);
                if(!contents.equals("") && !contents.equals("Attachments"))
                {
                    String[] items = contents.split(" ");
                    for(String s: items)
                    {
                        sb.append(s);
                        sb.append("\n");
                    }

                }
            }
        }

        System.out.println(sb.toString());
        ArrayList<String> todo = new ArrayList<String>();
        todo.add("todo");

        return todo;
    }


    private static ArrayList<Path> getPathsInDirectory(Path p)
    {
        ArrayList<Path> paths = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(p)) //Try with resources ensures stream closure when done.
        {
            for (Path entry: stream)
            {
                if(entry.toFile().isDirectory())
                {
                    Path temp;
                    try
                    {
                        temp = entry.resolve("messages.csv");
                    }
                    catch(InvalidPathException e)
                    {
                        continue;
                    }

                    if(temp.toFile().exists())
                    {
                        paths.add(temp);
                    }
                }
            }
        }
        catch(IOException e)
        {
            System.err.println("That directory does not exist.");
        }
        return paths;
    }



    /*
    * @throws FileNotFoundException if parameter p does not point to a valid file.
    * @throws IOException if the file is not able to be parsed.
    * Returns an Iterable<CSVRecord> if the file specified by @param path is successfully parsed.
    * Else returns null.
     */
    private static Iterable<CSVRecord> extractRecords(Path p)
    {
        Reader input;
        Iterable<CSVRecord> records;
        try
        {
            input = new FileReader(p.toString());
        }
        catch(FileNotFoundException e)
        {
            System.err.println("FileNotFoundException while accessing: " + p.toString());
            return null;
        }

        try
        {
            records = CSVFormat.RFC4180.parse(input);
            return records;
        }
        catch (IOException e)
        {
            System.err.println("IOException while parsing message file: " + p.toString());
            return null;
        }

    }

}
