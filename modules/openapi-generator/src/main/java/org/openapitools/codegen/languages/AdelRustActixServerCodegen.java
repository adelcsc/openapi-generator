package org.openapitools.codegen.languages;

import com.samskivert.mustache.BasicCollector;
import com.samskivert.mustache.DefaultCollector;
import com.samskivert.mustache.Mustache;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.*;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.parameters.Parameter;

import java.io.File;
import java.io.Reader;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import org.openapitools.codegen.api.TemplatingEngineAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdelRustActixServerCodegen extends DefaultCodegen implements CodegenConfig {
    private static final String uuidType = "uuid::Uuid";
    private static final String bytesType = "swagger::ByteArray";

    public static final String PROJECT_NAME = "projectName";

    static final Logger LOGGER = LoggerFactory.getLogger(AdelRustActixServerCodegen.class);

    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    public String getName() {
        return "adel-rust-actix";
    }

    public String getHelp() {
        return "Generates a adel-rust-actix server.";
    }

    public AdelRustActixServerCodegen() {
        super();
        outputFolder = "generated-code" + File.separator + "adel-rust-actix";
        modelTemplateFiles.put("models"+File.separator+"model.mustache", ".rs");
        modelTemplateFiles.put("models"+File.separator+"mod.mustache",".rs");
        apiTemplateFiles.put("controllers"+File.separator+"controller.mustache", ".rs");
        embeddedTemplateDir = templateDir = "adel-rust-actix";
        apiPackage = "src"+File.separator+"controllers";
        modelPackage = "src"+File.separator+"models";
        supportingFiles.add(new SupportingFile("README.mustache", "", "README.md"));
        // TODO: Fill this out.
        supportingFiles.add(new SupportingFile("Cargo.mustache","","Cargo.toml"));
        supportingFiles.add(new SupportingFile("main.mustache","","src"+File.separator+"main.rs"));
        supportingFiles.add(new SupportingFile("mod.mustache","",modelPackage+File.separator+"mod.rs"));
        supportingFiles.add(new SupportingFile("controllers"+File.separator+"mod.mustache",apiPackage,"mod.rs"));
        supportingFiles.add(new SupportingFile("config_routes.mustache","","src"+File.separator+"config"+File.separator+"mod.rs"));
        supportingFiles.add(new SupportingFile("services.mustache","","src"+File.separator+"services"+File.separator+"mod.rs"));


        //Mapping Types
        typeMapping.clear();
        typeMapping.put("number", "f64");
        typeMapping.put("integer", "i32");
        typeMapping.put("long", "i64");
        typeMapping.put("float", "f32");
        typeMapping.put("double", "f64");
        typeMapping.put("string", "String");
        typeMapping.put("UUID", uuidType);
        typeMapping.put("URI", "String");
        typeMapping.put("byte", "u8");
        typeMapping.put("ByteArray", bytesType);
        typeMapping.put("binary", bytesType);
        typeMapping.put("boolean", "bool");
        typeMapping.put("date", "chrono::DateTime::<chrono::Utc>");
        typeMapping.put("DateTime", "chrono::DateTime::<chrono::Utc>");
        typeMapping.put("password", "String");
        typeMapping.put("File", bytesType);
        typeMapping.put("file", bytesType);
        typeMapping.put("array", "Vec");
        typeMapping.put("map", "std::collections::HashMap");
        typeMapping.put("object", "serde_json::Value");
        typeMapping.put("AnyType", "serde_json::Value");

        setReservedWordsLowerCase(
                Arrays.asList(
                        // From https://doc.rust-lang.org/grammar.html#keywords
                        "abstract", "alignof", "as", "become", "box", "break", "const",
                        "continue", "crate", "do", "else", "enum", "extern", "false",
                        "final", "fn", "for", "if", "impl", "in", "let", "loop", "macro",
                        "match", "mod", "move", "mut", "offsetof", "override", "priv",
                        "proc", "pub", "pure", "ref", "return", "Self", "self", "sizeof",
                        "static", "struct", "super", "trait", "true", "type", "typeof",
                        "unsafe", "unsized", "use", "virtual", "where", "while", "yield"
                )
        );
        defaultIncludes = new HashSet<>(
                Arrays.asList(
                        "map",
                        "array",
                        "long",
                        "string",
                        "byte",
                        "boolean",
                        "number",
                        "float",
                        "double",
                        "URI",
                        "integer",
                        "object",
                        "Vec")
        );
        languageSpecificPrimitives = new HashSet<>(
                Arrays.asList(
                        "bool",
                        "char",
                        "i8",
                        "i16",
                        "i32",
                        "i64",
                        "u8",
                        "u16",
                        "u32",
                        "u64",
                        "isize",
                        "usize",
                        "f32",
                        "f64",
                        "str",
                        "String")
        );
        instantiationTypes.clear();
        instantiationTypes.put("array", "Vec");
        instantiationTypes.put("map", "std::collections::HashMap");

    }

    @Override
    public String toApiFilename(String name) {
        return name+"_Controller";
    }

    @Override
    public void processOpenAPI(OpenAPI openAPI) {
        super.processOpenAPI(openAPI);
        List<String> tags = new ArrayList<>();
        openAPI.getTags().forEach(tag -> tags.add(tag.getName()));
        additionalProperties.put("adel_tags",tags);
    }


    @Override
    public String modelFilename(String templateName, String modelName) {
        LOGGER.info("it is generating");
        if(templateName.equals("models"+File.separator+"mod.mustache"))
            return modelFileFolder() + File.separator + StringUtils.lowerCase(modelName) + File.separator  + "mod.rs";
        return modelFileFolder() + File.separator + StringUtils.lowerCase(modelName) + File.separator + StringUtils.lowerCase(modelName) + ".rs";
    }

}
