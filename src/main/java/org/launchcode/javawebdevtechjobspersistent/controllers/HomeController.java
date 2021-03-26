package org.launchcode.javawebdevtechjobspersistent.controllers;

import org.launchcode.javawebdevtechjobspersistent.models.Employer;
import org.launchcode.javawebdevtechjobspersistent.models.Job;
import org.launchcode.javawebdevtechjobspersistent.models.Skill;
import org.launchcode.javawebdevtechjobspersistent.models.data.EmployerRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.JobRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    EmployerRepository employerRepository;

    @Autowired
    SkillRepository skillRepository;

    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("title", "My Jobs");
        model.addAttribute("jobs", jobRepository.findAll());

        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute(new Job());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                       Errors errors, Model model, @RequestParam int employerId,
                                    @RequestParam List<Integer> skills) {


        Optional<Employer> optEmployer = employerRepository.findById(employerId);

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            return "add";
        }

        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        Employer employer = (Employer) optEmployer.get();
        model.addAttribute("employer", employer);

        newJob.setEmployer(employer);
        jobRepository.save(newJob);
        newJob.setSkills(skillObjs);

        return "redirect:";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {

        Optional<Job> someJob = jobRepository.findById(jobId);

        if (someJob.isEmpty()) {
            model.addAttribute("title", "Invalid Job ID: " + jobId);
        } else {
            Job job = (Job) someJob.get();
            model.addAttribute("title", job.getName());
            model.addAttribute("job", job);
        }

        return "view";
    }


}
