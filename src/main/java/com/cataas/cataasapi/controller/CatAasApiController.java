package com.cataas.cataasapi.controller;

import com.cataas.cataasapi.service.CatAasApiService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CatAasApiController {

    private String catImageUrlStr = "https://cataas.com/cat";
    private final CatAasApiService service;


    public CatAasApiController(CatAasApiService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String showChoices() {
        return "choices";
    }


    @PostMapping("/handle-choice")
    public String handleChoice(@RequestParam("choice") String choice, Model model) {
        model.addAttribute("choice", choice);
        return switch (choice) {
            case ("tagChoice") -> "insertTag";
            case ("textChoice") -> "insertText";
            case ("widthHeightChoice") -> "insertWidthHeight";
            default -> "Invalid Choice";
        };

    }

    @PostMapping("/handle-tag-inserted")
    public ResponseEntity<Resource> handleTagInserted(@RequestParam("tag") String tag, @RequestParam("fileName") String fileName, Model model) {
        model.addAttribute("tag", tag);
        model.addAttribute("fileName", fileName);

        if (tag != "" && fileName != "") {
            return service.downloadImageFile(catImageUrlStr + "/" + tag, fileName, "Tag");
        }

        return ResponseEntity.notFound().build();

    }

    @PostMapping("/handle-text-inserted")
    public ResponseEntity<Resource> handleTextInserted(@RequestParam("text") String text, @RequestParam("fileName") String fileName, Model model) {
        model.addAttribute("text", text);
        model.addAttribute("fileName", fileName);

        if (text != "" && fileName != "") {
            return service.downloadImageFile(catImageUrlStr + "/says/" + text, fileName, "Text");
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping("/handle-width-height-inserted")
    public ResponseEntity<Resource> handleWidthHeightInserted(@RequestParam("width") String width, @RequestParam("height") String height, @RequestParam("fileName") String fileName, Model model) {
        model.addAttribute("width", width);
        model.addAttribute("height", height);
        model.addAttribute("fileName", fileName);

        if (fileName != "") {
            if (width != "") {
                return service.downloadImageFile(catImageUrlStr + "?width=" + width, fileName, "Width or Height");

            } else if (height != "") {
                return service.downloadImageFile(catImageUrlStr + "?height=" + height, fileName, "Width or Height");

            }
        }
        return ResponseEntity.notFound().build();

    }


}



