package com.Zephir0g.kinacademy.restControllers;

import com.Zephir0g.kinacademy.entity.data.service.CourseService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Hidden
@RequestMapping("api/v1/admin")
public class Admin {

    private final CourseService courseService;

//    @RequestMapping(value = "/deleteCourse/{id}", method = RequestMethod.POST)
    @GetMapping("/deleteCourse/{id}")
    public String deleteCourse(@PathVariable Long id){
        System.out.println("deleteCourse: " + id);
        return courseService.deleteCourse(id);
    }
}
