import org.example.DoCommand;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.delete;
import static org.junit.jupiter.api.Assertions.*;

public class DoCommandTest {


    // Test createFile (touch)
    @Test
    void createFileTestWhenFileDoseNotExist() throws IOException {
        DoCommand doCommand = new DoCommand();
        Path checkPath = Paths.get(doCommand.getCurrentDirectory(),"TestWhenFileDoseNotExist.txt");

        // check that (TestWhenFileDoseNotExist.txt) does not exist in the current directory
        if(!Files.exists(checkPath))
        {
            // create the file (TestWhenFileDoseNotExist.txt)
            doCommand.setCommand("touch");
            doCommand.setArr(new String[]{"TestWhenFileDoseNotExist.txt"});
            doCommand._do();
        }

        // check that the file exists now
        assertTrue(Files.exists(checkPath));

        //delete the file after the test
        if(Files.exists(checkPath))
        {
            try{
                Files.delete(checkPath);
                System.out.println("File deleted after the check: " + checkPath.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to delete File: " + e.getMessage());
            }
        }
    }

    //Test remove directory (rmdir)
    @Test
    void removeDirectoryTestWhenDirectoryDoseExist()
    {
        DoCommand doCommand = new DoCommand() ;
        Path checkPath = Paths.get(doCommand.getCurrentDirectory(),"TestRemoveDirectoryWhenExist") ;

        // create (TestRemoveDirectoryWhenExist)directory if not exist
        if(!Files.exists(checkPath))
        {
            try {
                Files.createDirectory(checkPath) ;
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // delete the directory
        doCommand.setCommand("rmdir");
        doCommand.setArr(new String[]{"TestRemoveDirectoryWhenExist"});
        doCommand._do();

        //check that the directory was deleted
        assertFalse(Files.exists(checkPath));
    }

    //Test make directory (mkdir)
    @Test
    void makeDirectoryTestWhenDirectoryDoseNotExist()
    {
        DoCommand doCommand = new DoCommand() ;
        Path checkPath = Paths.get(doCommand.getCurrentDirectory(),"TestMakeDirectoryWhenNotExist");

        // check that the directory not exist
        if(!Files.exists(checkPath))
        {
            // create the directory(TestMakeDirectoryWhenNotExist)
            doCommand.setCommand("mkdir");
            doCommand.setArr(new String[]{"TestMakeDirectoryWhenNotExist"});
            doCommand._do();
        }

        // check that the directory exists now
        assertTrue(Files.exists(checkPath));

        // delete the directory after the test
        if(Files.exists(checkPath))
        {
            try{
                Files.delete(checkPath);
                System.out.println("Directory deleted after the check: " + checkPath.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to delete Directory: " + e.getMessage());
            }
        }
    }
}
