package com.example.SecureStorage.utils;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.springframework.web.multipart.MultipartFile;

public class UploadCoercing implements Coercing<MultipartFile, Void> {

    @Override
    public Void serialize(Object dataFetcherResult) throws CoercingSerializeException {
        // This is not used for file uploads
        return null;
    }

    @Override
    public MultipartFile parseValue(Object input) throws CoercingParseValueException {
        if (input instanceof MultipartFile) {
            return (MultipartFile) input;
        }
        throw new CoercingParseValueException("Input is not a valid file");
    }

    @Override
    public MultipartFile parseLiteral(Object input) throws CoercingParseValueException {
        // This is not used for file uploads from variables
        throw new CoercingParseValueException("Parsing literal for file upload is not supported");
    }
}
