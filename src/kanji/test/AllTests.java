package kanji.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BoardTest.class, GameTest.class, IntersectionTest.class, 
				StoneTest.class, ClientCommunicatorTest.class})
public class AllTests {

}
