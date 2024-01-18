package com.fo4ik.kinacademy.restControllers;

import com.fo4ik.kinacademy.core.Config;
import com.fo4ik.kinacademy.dto.CategoryDTO;
import com.fo4ik.kinacademy.dto.LanguagesDto;
import com.fo4ik.kinacademy.entity.data.service.UserService;
import com.fo4ik.kinacademy.entity.user.User;
import com.fo4ik.kinacademy.exceptions.AppException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam("language") String language
    ) {
        Locale locale = new Locale(language);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles/language", locale);

        Map<String, String> internationalization = new HashMap<>();
        for (String key : resourceBundle.keySet()) {
            String value = resourceBundle.getString(key);
            internationalization.put(key, value);
        }

        return ResponseEntity.ok(internationalization);
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
            @RequestParam("language") String language
    ) {
        Locale locale = new Locale(language);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles/category", locale);


       /* List<CategoryDTO> categoryDTO = new ArrayList<>();
        for (String key : resourceBundle.keySet()) {
            String value = resourceBundle.getString(key);
            categoryDTO.add(new CategoryDTO(value));
        }

        Collections.sort(categoryDTO, (category1, category2) -> category1.getName().compareToIgnoreCase(category2.getName()));*/

//        List<CategoryDTO> categories = buildCategoriesFromResourceBundle(resourceBundle);
        return ResponseEntity.ok(buildCategoriesFromResourceBundle(resourceBundle));
    }

    private Object buildCategoriesFromResourceBundle(ResourceBundle resourceBundle) {
        Enumeration<String> keys = resourceBundle.getKeys();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();


//        Map<String, Object> parent = new HashMap<>();
        List<CategoryDTO> parent = new ArrayList<>();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = resourceBundle.getString(key);
            String[] parts = key.split("\\.");


            List<CategoryDTO> subCategories = new ArrayList<>();
            List<CategoryDTO> categories = new ArrayList<>();
            try {
                if (parts.length == 1) {
                    CategoryDTO categoryDTO = checkCategory(parts[0], parent);
                    if (categoryDTO == null) {
                        parent.add(new CategoryDTO(parts[0], value));
                    } else {
                        categoryDTO.setLabel(value);
                    }
                }
                if (parts.length == 2) {
                    CategoryDTO categoryDTO = checkCategory(parts[0], parent);
                    CategoryDTO categoryDTOSub = new CategoryDTO();
                    if (categoryDTO == null) {
                        categoryDTO = new CategoryDTO(parts[0], "temp");
                        parent.add(categoryDTO);
                    }

                    categoryDTOSub = checkCategory(parts[1], categoryDTO.getItems());
                    if (categoryDTOSub == null) {
                        categoryDTOSub = new CategoryDTO(parts[1], value);
                        parent.get(parent.indexOf(categoryDTO)).getItems().add(categoryDTOSub);
                    } else {
                        categoryDTOSub.setLabel(value);
                    }


                }

                if (parts.length == 3) {
                    CategoryDTO categoryDTO = checkCategory(parts[0], parent);
                    CategoryDTO categoryDTOSub = new CategoryDTO();
                    if (categoryDTO == null) {
                        categoryDTO = new CategoryDTO(parts[0], "temp");
                        parent.add(categoryDTO);
                    }

                    categoryDTOSub = checkCategory(parts[1], categoryDTO.getItems());
                    if (categoryDTOSub == null) {
                        categoryDTOSub = new CategoryDTO(parts[1], "temp");
                        parent.get(parent.indexOf(categoryDTO)).getItems().add(categoryDTOSub);
                    }

                    CategoryDTO categoryDTOSubSub = new CategoryDTO();
                    categoryDTOSubSub = checkCategory(parts[2], categoryDTOSub.getItems());
                    if (categoryDTOSubSub == null) {
                        categoryDTOSubSub = new CategoryDTO(parts[2], value);
                        parent.get(parent.indexOf(categoryDTO)).getItems().get(parent.get(parent.indexOf(categoryDTO)).getItems().indexOf(categoryDTOSub)).getItems().add(categoryDTOSubSub);
                    } else {
                        categoryDTOSubSub.setLabel(value);
                    }
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error while parsing categories");
            }
        }


        return parent;
    }

    private CategoryDTO checkCategory(String name, List<CategoryDTO> categories) {
        if (categories == null) {
            return null;
        }
        for (CategoryDTO category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
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