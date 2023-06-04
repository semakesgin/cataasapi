package com.cataas.cataasapi;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CatAasApiController {

    private String catImageUrlStr ="https://cataas.com/cat";
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
    public ResponseEntity<Resource> handleTagInserted(@RequestParam("tag") String tag,@RequestParam("fileName") String fileName, Model model) {
        model.addAttribute("tag", tag);
        model.addAttribute("fileName", fileName);

        if (tag != null && fileName != null  ) {
            catImageUrlStr ="https://cataas.com/cat/"+tag;
            return service.downloadImageFile(catImageUrlStr,fileName,"Tags");
        }
        return ResponseEntity.notFound().build();

    }
    @PostMapping("/handle-text-inserted")
    public ResponseEntity<Resource> handleTextInserted(@RequestParam("text") String text,@RequestParam("fileName") String fileName, Model model) {
        model.addAttribute("text", text);
        model.addAttribute("fileName", fileName);

        if (text != null && fileName != null  ) {
            catImageUrlStr = "https://cataas.com/cat/says/"+text;
            return service.downloadImageFile(catImageUrlStr,fileName,"Texts");
        }
        return ResponseEntity.notFound().build();

    }
    @PostMapping("/handle-width-height-inserted")
    public ResponseEntity<Resource> handleWidthHeightInserted(@RequestParam("width") String width,@RequestParam("height") String height,@RequestParam("fileName") String fileName, Model model) {
        model.addAttribute("width", width);
        model.addAttribute("height", height);
        model.addAttribute("fileName", fileName);

        if (width != null && height!= null && fileName != null  ) {
            catImageUrlStr = "https://cataas.com/cat?width="+width+"%20or%20/cat?height="+height;
            return service.downloadImageFile(catImageUrlStr,fileName,"Width and Heights");
        }
        return ResponseEntity.notFound().build();

    }


}



