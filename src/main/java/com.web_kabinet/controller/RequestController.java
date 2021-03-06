package com.web_kabinet.controller;

import com.web_kabinet.domain.Contragent;
import com.web_kabinet.domain.Nomenclature;
import com.web_kabinet.domain.User;
import com.web_kabinet.repos.RequestRepo;
import com.web_kabinet.request.Request;
import com.web_kabinet.service.ContragentService;
import com.web_kabinet.service.NomenclatureService;
import com.web_kabinet.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@Controller
@RequestMapping("/request")
public class RequestController {
    @Autowired
    ContragentService contragentService;

    @Autowired
    NomenclatureService nomenclatureService;

    @Autowired
    RequestService requestService;
    @Autowired
    RequestRepo requestRepo;

    @GetMapping
    public String getRequests(
            Map<String, Object> model
    ) {

        Iterable<Request> requests = requestRepo.findAll();
        model.put("requests", requests);
        return "request";
    }

    //
    @GetMapping("/requestEdit/")
    public String requestEdit(
            Map<String, Object> model
    ) {
        return "requestEdit";
    }

    @PostMapping
    public String addRequest(@AuthenticationPrincipal User user,
                             @RequestParam String contragent_id,
                             @RequestParam String nomenclature_id,
                             @RequestParam String weight,
                             @RequestParam(defaultValue = "false") String isChecked,

                             Map<String, Object> model) throws ParseException {

        Contragent contragent = contragentService.findContragentByUUID(contragent_id);
        Nomenclature nomenclature = nomenclatureService.findNomenclatureByUUID(nomenclature_id);
        Long num = requestService.getNumber();
        Request request = Request.builder()
                .author(user)
                .contragent(contragent)
                .nomenclature(nomenclature)
                .weight(weight)
                .isChecked(isChecked)
                .num(num)
                .build();

        if (requestService.checkWeight(request)) requestRepo.save(request);

        Iterable<Request> requests = requestRepo.findAll();
        model.put("requests", requests);
        return "redirect:/request";
    }

    @GetMapping("/{id}")
    public String requestEditor(
            @PathVariable("id") Request request,
            Model model) {
        model.addAttribute("request", request);
        return "requestEdit";
    }


    @PostMapping("/{id}")
    public String requestEditForm(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false, defaultValue = "") String contragent_id,
            @RequestParam(required = false, defaultValue = "") String nomenclature_id,
            @RequestParam(required = false, defaultValue = "") String weight,
            @RequestParam(required = false, defaultValue = "false") String isChecked,
            @RequestParam("reqId") Request request,
            Model model) {
        Contragent contragent = contragentService.findContragentByUUID(contragent_id);

        Nomenclature nomenclature = nomenclatureService.findNomenclatureByUUID(nomenclature_id);
        request.setContragent(contragent);
        request.setNomenclature(nomenclature);
//
        request.setWeight(Float.valueOf(weight.replaceAll("\\s", "0")));

        request.setChecked(isChecked.equals("on"));
        if (requestService.checkWeight(request) && isChecked.equals("on")) {
            requestService.ttnFromRequest(request, user);
            requestRepo.save(request);
        }

        return "redirect:/request";
    }
}
