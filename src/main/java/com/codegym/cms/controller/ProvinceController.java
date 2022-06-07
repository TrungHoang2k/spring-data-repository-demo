package com.codegym.cms.controller;

import com.codegym.cms.model.Customer;
import com.codegym.cms.model.Province;
import com.codegym.cms.repository.ICustomerRepository;
import com.codegym.cms.service.province.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class ProvinceController {
    @Autowired
    private IProvinceService provinceService;

    @Autowired
    private ICustomerRepository customerService;

    @GetMapping("/provinces")
    public ModelAndView listProvince(Pageable pageable) {
        Iterable<Province> provinces = provinceService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("/province/list");
        modelAndView.addObject("provinces", provinces);
        return modelAndView;
    }

    @GetMapping("/view-province/{id}")
    public ModelAndView viewProvince(@PathVariable("id") Long id, Pageable pageable) {
        Optional<Province> provinceOptional = provinceService.findById(id);
        if (!provinceOptional.isPresent()) {
            return new ModelAndView("/province/error.404");
        }

        Iterable<Customer> customers = customerService.findAllByProvince(provinceOptional.get(), pageable);

        ModelAndView modelAndView = new ModelAndView("/province/view");
        modelAndView.addObject("province", provinceOptional.get());
        modelAndView.addObject("customers", customers);
        return modelAndView;
    }

    @GetMapping("create-province")
    public ModelAndView showCreateFomr() {
        ModelAndView modelAndView = new ModelAndView("/province/create");
        modelAndView.addObject("province", new Province());
        return modelAndView;
    }

    @PostMapping("create-province")
    public ModelAndView saveProvince(@ModelAttribute("province") Province province, Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/province/list");
        provinceService.save(province);
        Iterable<Province> provinces = provinceService.findAll(pageable);
        modelAndView.addObject("provinces", provinces);
        modelAndView.addObject("message", "New province created successfully");
        return modelAndView;
    }

    @GetMapping("edit-province/{id}")
    public ModelAndView showEditFrom(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/province/edit");
        modelAndView.addObject("province", provinceService.findById(id));
        modelAndView.addObject("message", "Province updated successfully");
        return modelAndView;
    }

    @PostMapping("edit-province")
    public ModelAndView updateProvince(@ModelAttribute("province") Province province, Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/province/list");
        provinceService.save(province);
        Iterable<Province> provinces = provinceService.findAll(pageable);
        modelAndView.addObject("provinces", provinces);
        return modelAndView;
    }

    @GetMapping("delete-province/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/province/delete");
        modelAndView.addObject("province", provinceService.findById(id).get());
        return modelAndView;
    }

    @PostMapping("delete-province")
    public ModelAndView deleteProvince(@ModelAttribute("province") Province province, Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/province/list");
        provinceService.remove(province.getId());
        Iterable<Province> provinces = provinceService.findAll(pageable);
        modelAndView.addObject("provinces", provinces);
        return modelAndView;
    }
}
