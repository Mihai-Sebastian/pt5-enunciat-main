package org.entdes.todolist;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TodolistController {
    
    private final GestorTasques gestor;

    @Autowired
    public TodolistController(GestorTasques gestor) {
        this.gestor = gestor;
    }
    @RequestMapping(value = { "/", "/tasques" }, method = RequestMethod.GET)
    public String llistarTasques(@RequestParam(value = "filtreDescripcio", required = false) String filtreDescripcio,
            @RequestParam(value = "filtreComplecio", required = false) Boolean filtreComplecio,
            Model model) {
        List<Tasca> tasques;
        if (filtreDescripcio != null && !filtreDescripcio.isEmpty()) {
            tasques = gestor.llistarTasquesPerDescripcio(filtreDescripcio);
        } else if (filtreComplecio != null) {
            tasques = gestor.llistarTasquesPerComplecio(filtreComplecio);
        } else {
            tasques = gestor.llistarTasques();
        }
        model.addAttribute("tasques", tasques);
        model.addAttribute("nombreTasques", gestor.getNombreTasques());
        model.addAttribute("filtreDescripcio", filtreDescripcio);
        model.addAttribute("filtreComplecio", filtreComplecio);
        return "index";
    }

    @PostMapping("/tasques")
    public String addTasca(@RequestParam String descripcio,
            @RequestParam(required = false) LocalDate dataInici,
            @RequestParam(required = false) LocalDate dataFiPrevista,
            @RequestParam(required = false) Integer prioritat,
            Model model) {
        try {
            gestor.afegirTasca(descripcio, dataInici, dataFiPrevista, prioritat);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            e.printStackTrace();
            return "crear_tasca";
        }
        return "redirect:/";
    }

    @PostMapping("/tasques/update/{id}/completar")
    public String completarTasca(@PathVariable int id, Model model) {
        try {
            gestor.marcarCompletada(id);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/tasques/edit/{id}")
    public String editTasca(@PathVariable int id, Model model) {
        try {
            Tasca tasca = gestor.obtenirTasca(id);
            model.addAttribute("tasca", tasca);

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            e.printStackTrace();
            return "redirect:/";
        }
        return "edit_tasca";
    }

    @GetMapping("/tasques/create")
    public String crearTasca() {
        return "crear_tasca";
    }

    @PostMapping("/tasques/update/{id}")
    public String modificarTasca(@PathVariable int id,
            @RequestParam String descripcio,
            @RequestParam(required = false) Boolean completada,
            @RequestParam(required = false) LocalDate dataInici,
            @RequestParam(required = false) LocalDate dataFiPrevista,
            @RequestParam(required = false) Integer prioritat,
            Model model) {
        try {
            Tasca tasca = gestor.obtenirTasca(id);
            model.addAttribute("tasca", tasca);
            gestor.modificarTasca(id, descripcio, completada, dataInici, dataFiPrevista, prioritat);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            e.printStackTrace();
            return "edit_tasca";
        }
        return "redirect:/";
    }

    @PostMapping("/tasques/delete/{id}")
    public String deleteTasca(@PathVariable int id, Model model) {
        try {
            gestor.eliminarTasca(id);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            e.printStackTrace();

        }
        return "redirect:/";
    }
}