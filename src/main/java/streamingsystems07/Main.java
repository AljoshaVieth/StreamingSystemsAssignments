package streamingsystems07;

import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.runtime.client.EPDeployException;

/**
 * A class for starting Kafka test data generation and parsing thereof.
 */
public class Main {

    /**
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        new TestDataGeneratorThread().start();
    }

}

