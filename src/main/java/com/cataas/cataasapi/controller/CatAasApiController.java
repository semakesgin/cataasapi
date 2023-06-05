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

    @GetMapping("/choices")
    public String showChoices() {
        return "choices";
    }


    @PostMapping("/choice-selected")
    public String choiceSelected(@RequestParam("choice") String choice, Model model) {
        model.addAttribute("choice", choice);
        return switch (choice) {
            case ("tagChoice") -> "tag";
            case ("textChoice") -> "text";
            case ("widthHeightChoice") -> "widthHeight";
            default -> "Invalid Choice";
        };

    }

    @PostMapping("/tag")
    public ResponseEntity<Resource> tagEntered(@RequestParam("tag") String tag, @RequestParam("fileName") String fileName, Model model) {
        model.addAttribute("tag", tag);
        model.addAttribute("fileName", fileName);

        if (tag != "" && fileName != "") {
            return service.downloadImageFile(catImageUrlStr + "/" + tag, fileName, "Tag");
        }

        return ResponseEntity.notFound().build();

    }

    @PostMapping("/text")
    public ResponseEntity<Resource> textEntered(@RequestParam("text") String text, @RequestParam("fileName") String fileName, Model model) {
        model.addAttribute("text", text);
        model.addAttribute("fileName", fileName);

        if (text != "" && fileName != "") {
            return service.downloadImageFile(catImageUrlStr + "/says/" + text, fileName, "Text");
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping("/width-height")
    public ResponseEntity<Resource> widthHeightEntered(@RequestParam("width") String width, @RequestParam("height") String height, @RequestParam("fileName") String fileName, Model model) {
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



