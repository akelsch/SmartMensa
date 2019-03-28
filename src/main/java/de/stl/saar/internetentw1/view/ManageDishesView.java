package de.stl.saar.internetentw1.view;

import de.stl.saar.internetentw1.model.Dish;
import de.stl.saar.internetentw1.repository.DishRepository;
import de.stl.saar.internetentw1.util.FacesContextUtils;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class ManageDishesView implements Serializable {

    @Getter
    private Iterable<Dish> dishes;

    @Getter
    @Setter
    private Dish selectedDish;

    private final DishRepository dishRepository;

    @Inject
    public ManageDishesView(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @PostConstruct
    public void init() {
        dishes = dishRepository.findAll();
    }

    public String changeSelectedDish() {
        if (selectedDish != null) {
            FacesContextUtils.putFlashObject("dish", selectedDish);
            return "create_dish?faces-redirect=true";
        }

        return "";
    }

    public void deleteSelectedDish() {
        if (selectedDish != null) {
            dishRepository.delete(selectedDish);
            dishes = dishRepository.findAll();
        }
    }
}
