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


    @Test
    public void testMoveFile() throws IOException {
      // Create a temporary directory for the test
      Path testDirectory = Files.createTempDirectory("testDir");
      // Create a temporary source file within the temp directory
      Path sourceFilePath = Files.createTempFile(testDirectory, "sourceFile", ".txt");
      // Define the destination path within the same temp directory
      Path destDirectory = testDirectory.resolve("destDir");
      Files.createDirectory(destDirectory);
      Path destFilePath = destDirectory.resolve(sourceFilePath.getFileName());

      // Execute the move command
      DoCommand moveCommand = new DoCommand("mv", new String[]{sourceFilePath.toString(), destDirectory.toString()});
      moveCommand._do();

      // Assertion to check if the source file has been moved to the destination
      assertFalse(Files.exists(sourceFilePath), "Source file should not exist after move.");
      assertTrue(Files.exists(destFilePath), "Destination file should exist after move.");
    }


    @Test
    public void testRemoveFile() throws IOException {
        // Create a temporary file for the test
        Path filePath = Files.createTempFile(testDirectory, "fileToRemove", ".txt");

        // Execute the remove command
        DoCommand removeCommand = new DoCommand("rm", new String[]{filePath.toString()});
        removeCommand._do();

        // Assertion to check if the file has been removed
        assertFalse(Files.exists(filePath), "File should be deleted");
    }

    @Test
    public void testReadFile() throws IOException {
        // Create a temporary file and write content to it
        Path filePath = Files.createTempFile(testDirectory, "fileToRead", ".txt");
        String content = "Test content for reading file";
        Files.writeString(filePath, content);

        // Capture System output to verify `cat` command output
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        // Execute the read (cat) command
        DoCommand readCommand = new DoCommand("cat", new String[]{filePath.toString()});
        readCommand._do();

        // Assertions to check if the content matches
        assertEquals(content + System.lineSeparator(), outContent.toString(), "Content read from file should match the content written");

        // Reset System output
        System.setOut(System.out);
    }


    @Test
    @DisplayName("Test Output Redirection with >")
    void testOutputRedirection() throws IOException {
        Path outputFile = testDirectory.resolve("output.txt");

        // Content to append
        String content = " This is appended content.";


        DoCommand doCommand = new DoCommand(">", new String[]{"output.txt"});
        doCommand.currentDirectory = testDirectory.toString();
        doCommand.output = content;
        doCommand._do();

        assertTrue(Files.exists(outputFile), "Output file should exist.");
        assertEquals(content, Files.readString(outputFile), "Content of output file should match appended output.");

        // Clean up
        Files.deleteIfExists(outputFile);
    }
    @Test
    @DisplayName("Test Output Redirection with >>")
    void testOutputAppendRedirection() throws IOException {
        Path outputFile = testDirectory.resolve("appendOutput.txt");

        // Create the file and write initial content
        String initialContent = "Initial content.";
        Files.writeString(outputFile, initialContent);

        // Content to append
        String appendContent = " This is appended content.";


        DoCommand doCommand = new DoCommand(">>", new String[]{"appendOutput.txt"});
        doCommand.currentDirectory = testDirectory.toString();
        doCommand.output = appendContent;
        doCommand._do();

        assertTrue(Files.exists(outputFile), "Output file should exist.");
        assertEquals(initialContent + appendContent, Files.readString(outputFile), "Content of output file should match appended output.");

        // Clean up
        Files.deleteIfExists(outputFile);
    }

    @Test
    @DisplayName("Test creating a file and piping ls output to grep")
    void testTouchAndGrep() throws IOException {
        // Step 1: Create a file named test.txt
        DoCommand touchCommand = new DoCommand("touch", new String[]{"test.txt"});
        touchCommand._do();

        // Step 2: Execute ls command and pipe it to grep to search for test.txt
        DoCommand lsCommand = new DoCommand("ls", new String[]{});

        // Capture the output of the ls command
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        // Execute ls command
        lsCommand._do();

        // Step 3: Store the output and check for test.txt
        String lsOutput = outContent.toString();

        // Step 4: Check if the output contains "test.txt"
        assertTrue(lsOutput.contains("test.txt"), "Output should contain 'test.txt'.");

        // Clean up: remove the created file after the test
        Files.deleteIfExists(Paths.get(doCommand.getCurrentDirectory(), "test.txt"));
    }
    

}
