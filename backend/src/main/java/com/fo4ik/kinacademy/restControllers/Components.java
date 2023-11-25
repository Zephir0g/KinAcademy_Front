package com.fo4ik.kinacademy.restControllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/components")
@OpenAPIDefinition(tags = {@Tag(name = "Components", description = "Methods for getting components")})
public class Components {

    private final MessageSource messageSource;

    @GetMapping("/categories")
    @Operation(summary = "Get categories", description = "Get categories from file using current language", tags = {"Components"})
    public ResponseEntity<?> getCategories() {
        //return List<String>of("1", "2", "3");
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        return ResponseEntity.ok(list);

    }

    /*@RequestMapping(value = "/languages", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> getLanguages(
            @RequestBody Json requestWords,
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) Locale locale
    ) {
        String value = messageSource.getMessage("welcome-message", null, locale != null ? locale : Locale.getDefault());

        return ResponseEntity.ok(requestWords + " : " + locale + " : " + value);
    }*/

    @RequestMapping(value = "/languages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getResourceBundleAsJson(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) Locale locale
    ) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles/language", locale != null ? locale : Locale.getDefault());

        Map<String, String> resourceMap = new HashMap<>();
        for (String key : resourceBundle.keySet()) {
            String value = resourceBundle.getString(key);
            resourceMap.put(key, value);
        }

        return ResponseEntity.ok(resourceMap);
    }
}
