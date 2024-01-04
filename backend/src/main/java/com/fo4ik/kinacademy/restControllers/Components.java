package com.fo4ik.kinacademy.restControllers;

import com.fo4ik.kinacademy.core.Config;
import com.fo4ik.kinacademy.dto.LanguagesDto;
import com.fo4ik.kinacademy.entity.data.service.UserService;
import com.fo4ik.kinacademy.entity.user.User;
import com.fo4ik.kinacademy.exceptions.AppException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("api/v1/components")
@OpenAPIDefinition(tags = {@Tag(name = "Components", description = "Methods for getting components")})
public class Components {

    private final UserService userService;


    @RequestMapping(value = "/internationalization", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get internationalization language", description = "Get all words as Json to internationalization program", tags = {"Components"})
    public ResponseEntity<Map<String, String>> getInternationalization(
            @Parameter(description = "User language", example = "English")
//            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String language
            @RequestParam("language") String language
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
    @Operation(summary = "Get languages", description = "Get available languages", tags = {"Components"})
    public ResponseEntity<List<LanguagesDto>> getLanguages() {

        List<LanguagesDto> languagesDtoList = new ArrayList<>();
        Config.availableLanguages.forEach((key, value) -> languagesDtoList.add(new LanguagesDto(key, value)));

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

    @RequestMapping(value = "/authorname", method = RequestMethod.GET)
    public ResponseEntity<String> getUserByUsername(
            @RequestParam("authorUsername") String authorUsername) throws AppException {

        User user = userService.getUserByUsername(authorUsername);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user.getFirstName() + " " + user.getSurname());
    }
}
