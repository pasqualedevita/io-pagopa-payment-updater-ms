package it.gov.pagopa.paymentupdater.consumer.utils.types;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.AvroTypeException;
import org.apache.avro.Schema;

import it.gov.pagopa.paymentupdater.consumer.utils.JsonToAvroReader;
import it.gov.pagopa.paymentupdater.consumer.utils.PathsPrinter;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static it.gov.pagopa.paymentupdater.consumer.utils.PathsPrinter.print;
import static java.util.stream.Collectors.joining;

public class UnionConverter implements AvroTypeConverter {
    private final JsonToAvroReader jsonToAvroReader;

    public UnionConverter(JsonToAvroReader jsonToAvroReader) {
        this.jsonToAvroReader = jsonToAvroReader;
    }

    @Override
    public Object convert(Schema.Field field, Schema schema, Object jsonValue, Deque<String> path, boolean silently) {
        List<Schema> types = schema.getTypes();
        List<String> incompatibleTypes = new ArrayList<>();
        for (Schema type : types) {
            try {
                Object nestedValue = this.jsonToAvroReader.read(field, type, jsonValue, path, true);
                if (nestedValue instanceof Incompatible) {
                    incompatibleTypes.add(((Incompatible) nestedValue).expected);
                } else {
                    return nestedValue;
                }
            } catch (AvroRuntimeException e) {
                // thrown only for union of more complex types like records
                continue;
            }
        }
        throw unionException(field.name(), String.join(", ", incompatibleTypes), path);
    }

    @Override
    public boolean canManage(Schema schema, Deque<String> path) {
        return schema.getType().equals(Schema.Type.UNION);
    }

    private static AvroTypeException unionException(String fieldName, String expectedTypes, Deque<String> offendingPath) {
        return new AvroTypeException("Could not evaluate union, field " +
                fieldName +
                " is expected to be one of these: " +
                expectedTypes +
                ". If this is a complex type, check if offending field: " + print(offendingPath) + " adheres to schema.");
    }
}
