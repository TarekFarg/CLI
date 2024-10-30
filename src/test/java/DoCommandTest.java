import org.example.DoCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.delete;
import static org.junit.jupiter.api.Assertions.*;

public class DoCommandTest {

    private DoCommand doCommand;
    private Path testDirectory;

    @BeforeEach
    void setUp() throws IOException {
        doCommand = new DoCommand();
        testDirectory = Files.createTempDirectory("testDir");
        doCommand = new DoCommand("ls", new String[]{});
        doCommand.setCommand("pwd");
        doCommand._do();
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(System.out);
        Files.walk(testDirectory)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    @DisplayName("Test listDirectory - Non-hidden files only")
    void testListDirectoryNonHiddenFiles() {
        // Setup: create files
        try {
            Files.createFile(testDirectory.resolve("file1.txt"));
            Files.createFile(testDirectory.resolve(".hiddenFile"));

            doCommand = new DoCommand("ls", new String[]{});
            doCommand.setCommand("ls");
            doCommand._do();
        } catch (IOException e) {
            fail("Failed to set up test directory structure: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test listDirectory - Including hidden files")
    void testListDirectoryAllFiles() {
        // Setup: create files
        try {
            Files.createFile(testDirectory.resolve("file2.txt"));
            Files.createFile(testDirectory.resolve(".hiddenFile2"));
            doCommand = new DoCommand("ls", new String[]{"-a"});
            doCommand.setCommand("ls");
            doCommand._do();
        } catch (IOException e) {
            fail("Failed to set up test directory structure: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test changeDirectory - Change to valid directory")
    void testChangeDirectoryValid() {
        String newDir = testDirectory.toAbsolutePath().toString();
        doCommand.setArr(new String[]{newDir});
        doCommand.setCommand("cd");
        doCommand._do();
        assertEquals(newDir, doCommand.getCurrentDirectory(), "Expected to change to new directory.");
    }

    @Test
    @DisplayName("Test changeDirectory - Change to invalid directory")
    void testChangeDirectoryInvalid() {
        String invalidDir = testDirectory.resolve("invalidDir").toString();
        doCommand.setArr(new String[]{invalidDir});
        doCommand.setCommand("cd");
        doCommand._do();
        assertNotEquals(invalidDir, doCommand.getCurrentDirectory(), "Expected to stay in the same directory.");
    }

    @Test
    @DisplayName("Test printWorkingDirectory - Current directory")
    void testPrintWorkingDirectory() {
        String expectedDirectory = System.getProperty("user.home");
        assertEquals(expectedDirectory, doCommand.getCurrentDirectory(), "Expected current directory to be user home.");
    }

    // Test createFile (touch)
    @Test
    void createFileTestWhenFileDoseNotExist(){
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
