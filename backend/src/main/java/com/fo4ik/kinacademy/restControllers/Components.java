package com.fo4ik.kinacademy.restControllers;

import com.fo4ik.kinacademy.configuration.Config;
import com.fo4ik.kinacademy.dto.LanguagesDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/components")
@OpenAPIDefinition(tags = {@Tag(name = "Components", description = "Methods for getting components")})
public class Components {

    private final MessageSource messageSource;


    @RequestMapping(value = "/internationalization", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get internationalization language", description = "Get all words as Json to internationalization program", tags = {"Components"})
    public ResponseEntity<Map<String, String>> getInternationalization(
            @Parameter(description = "User language", example = "English")
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String language
    ) {
        Locale locale = new Locale(language);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles/language", locale);

        Map<String, String> resourceMap = new HashMap<>();
        for (String key : resourceBundle.keySet()) {
            String value = resourceBundle.getString(key);
            resourceMap.put(key, value);
        }

        return ResponseEntity.ok(resourceMap);
    }

    @RequestMapping(value = "/languages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get languages", description = "Get languages from file", tags = {"Components"})
    public ResponseEntity<List<LanguagesDto>> getLanguages() {

        //List<LanguagesDto>
        List<LanguagesDto> languagesDtoList = new ArrayList<>();
        //add all languages to list from config.availableLanguages (Map<String, String>)
        Config.availableLanguages.forEach((key, value) -> languagesDtoList.add(new LanguagesDto(key, value)));
        System.out.println(languagesDtoList);


        return ResponseEntity.ok(languagesDtoList);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get categories", description = "Get categories from file using current language", tags = {"Components"})
    public ResponseEntity<?> getCategories(
            @Parameter(description = "User language", example = "English")
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) Locale locale
    ) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles/category", locale != null ? locale : Locale.getDefault());

        //TODO make return categories

        return ResponseEntity.ok("");

    }
}
