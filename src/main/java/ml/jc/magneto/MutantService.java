package ml.jc.magneto;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class MutantService implements RequestStreamHandler {

    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private static final DynamoDB dynamoDB = new DynamoDB(client);
    private static final ObjectMapper om = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(MutantService.class);

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        PojoResponse response = new PojoResponse();

        try (OutputStreamWriter osw = new OutputStreamWriter(output)) {

            JsonNode jn = om.readTree(input);

            String resource = jn.get("resource").asText();

            Table t = dynamoDB.getTable("ML-Test");

            if(resource.contains("stats"))
            {
                Item i = t.getItem("id", "counters");

                long count_mutant = i.getLong("count_mutant");
                long count_human = i.getLong("count_human");
                double ratio = (double) count_mutant / (double) count_human;

                String statBody = String.format("{\"count_mutant_dna\": %d, \"count_human_dna\": %d, \"ratio\": %.2f}", count_mutant, count_human, ratio);
                response.setStatusCode(200);
                response.setBody(statBody);
                osw.write(om.writeValueAsString(response));
                return;
            }

            String body = jn.get("body").asText();

            PojoRequest p = om.readValue(body, PojoRequest.class);

            UpdateItemSpec updateItemSpec;



            if(MainClass.isMutant(p.getDna()))
            {
                updateItemSpec = new UpdateItemSpec()
                        .withPrimaryKey("id", "counters")
                        .withUpdateExpression("set count_mutant = count_mutant + :incr")
                        .withValueMap(new ValueMap().withNumber(":incr", 1));

                response.setBody("{\"message\":\"Dna belongs to a Mutant\"}");
                response.setStatusCode(200);
            }
            else
            {
                updateItemSpec = new UpdateItemSpec()
                        .withPrimaryKey("id", "counters")
                        .withUpdateExpression("set count_human = count_human + :incr")
                        .withValueMap(new ValueMap().withNumber(":incr", 1));

                response.setBody("{\"message\":\"Dna does not belong a Mutant\"}");
                response.setStatusCode(403);
            }

            t.updateItem(updateItemSpec);

            osw.write(om.writeValueAsString(response));

        } catch (Exception e)
        {
            OutputStreamWriter osw = new OutputStreamWriter(output);
            response.setStatusCode(500);
            response.setBody("{\"message\":\"There is an error in the service. Cause: " + e.getMessage() + "\"}");
            LOGGER.error("There is an error in the service. ", e);
            osw.write(om.writeValueAsString(response));
            osw.close();
        }


    }
}
