import org.example.CMDInterpreter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class CMDInterpreterTest {
    private final CMDInterpreter cli = new CMDInterpreter();
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    void testPwd() {
        cli.executeCommand("pwd");
        assertTrue(outContent.toString().trim().endsWith(System.getProperty("user.dir")));
    }

    @Test
    void testMkdirAndCd() {
        cli.executeCommand("mkdir testDir");
        File dir = new File("testDir");
        assertTrue(dir.exists() && dir.isDirectory());

        cli.executeCommand("cd testDir");
        assertEquals(dir.getAbsolutePath(), System.getProperty("user.dir"));

        dir.delete(); // Cleanup
    }

    @Test
    void testTouchAndRm() {
        cli.executeCommand("touch testFile.txt");
        File file = new File("testFile.txt");
        assertTrue(file.exists() && file.isFile());

        cli.executeCommand("rm testFile.txt");
        assertFalse(file.exists());
    }

    @Test
    void testRmdir() {
        cli.executeCommand("mkdir emptyDir");
        File dir = new File("emptyDir");
        assertTrue(dir.exists() && dir.isDirectory());

        cli.executeCommand("rmdir emptyDir");
        assertFalse(dir.exists());
    }

    @Test
    void testCat() {
        File file = new File("testFile.txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Hello, CLI!");
        } catch (IOException e) {
            fail("Setup failed: " + e.getMessage());
        }

        cli.executeCommand("cat testFile.txt");
        assertTrue(outContent.toString().contains("Hello, CLI!"));
        file.delete(); // Cleanup
    }
}
